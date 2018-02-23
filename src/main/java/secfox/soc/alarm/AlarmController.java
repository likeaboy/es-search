package secfox.soc.alarm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.netty.server.NettyConfig;

public class AlarmController {

	private final static Logger logger = LoggerFactory
			.getLogger(AlarmController.class);

	private static volatile AlarmController instance = new AlarmController();

	private AlarmController() {
	}

	public static AlarmController getInstance() {
		return instance;
	}

	private AlarmFilter filter = new AlarmFilter();
	
	private void writeParams(AlarmRequest alarmReq){
		logger.info("[pyshell params path : " + NettyConfig.getInstance().getConfigValue(NettyConfig.KEY_PYSHELL_PARAMS_PATH)+"]");
		logger.info("[write params : "+alarmReq.getPhone() + alarmReq.getPort() + alarmReq.getBps() + alarmReq.getContent() +"]");
		 File file = new File(NettyConfig.getInstance().getConfigValue(NettyConfig.KEY_PYSHELL_PARAMS_PATH));
		 file.delete();  
		 BufferedWriter writer = null;
		 FileOutputStream fos = null;
		 OutputStreamWriter osw = null;
		 try {
	        file.createNewFile();  
	       
	        fos = new FileOutputStream(file);
	        osw = new OutputStreamWriter(fos);
	        writer = new BufferedWriter(osw);
	            writer.write(alarmReq.getPhone());
	            writer.write("\n");
//	            writer.newLine();//换行
	            writer.write(alarmReq.getPort());
	            writer.write("\n");
//	            writer.newLine();//换行
	            writer.write(alarmReq.getBps());
	            writer.write("\n");
//	            writer.newLine();//换行
	            writer.write(alarmReq.getContent());
	            
	            writer.flush();
	        } catch (FileNotFoundException e) {
	        	logger.error("",e);
	        }catch (IOException e) {
	        	logger.error("",e);
	        }finally{
	            try {
	            	 writer.close();
	            	 osw.close();
	            	fos.close();
	            } catch (IOException e) {
	            	logger.error("",e);
	            }
	        }
	}
	
	private AlarmResponse readResult(){
		AlarmResponse alarmResp = new AlarmResponse();
		File file=new File(NettyConfig.getInstance().getConfigValue(NettyConfig.KEY_PYSHELL_RESULT_PATH));  
        BufferedReader reader=null;  
        try{  
                reader=new BufferedReader(new FileReader(file));
                String status = reader.readLine();
                
                if(StringUtils.isEmpty(status)){
                	alarmResp.setCode(AlarmResponse.Type.ERROR.code);
            		return alarmResp;
                }
                
                if("1".equals(status)){
                	alarmResp.setCode(AlarmResponse.Type.OK.code);
					alarmResp.setMsg(AlarmResponse.Type.OK.msg);
                }else{
                	alarmResp.setCode(AlarmResponse.Type.ERROR.code);
            		alarmResp.setMsg(AlarmResponse.Type.ERROR.msg);
                }
                logger.info("[read result status: "+status+"]");
                return alarmResp;
        }  
        catch(Exception e){ 
        	logger.error("",e); 
        }  
        finally{  
            if(reader!=null){  
                try{  
                    reader.close();  
                }  
                catch(Exception e){  
                	logger.error("",e);
                }  
            }  
        }  
        
        alarmResp.setCode(AlarmResponse.Type.ERROR.code);
		alarmResp.setMsg(AlarmResponse.Type.ERROR.msg);
        
        return alarmResp;
	}
	
	
	public AlarmResponse directCallPython(Map<String, Object> params){
		try {
			String pyShellPath = NettyConfig.getInstance().getConfigValue(
					NettyConfig.KEY_PYSHELL_PATH);
			
			logger.info("[alarm py shell path : " + pyShellPath
					+ ",process start...]");
			
			AlarmRequest alarmRequest = filter.convert(params);
			alarmRequest.convert();

			AlarmResponse checkResp = filter.checkParams(alarmRequest);
			if (checkResp.getCode() != AlarmResponse.Type.OK.code)
				return checkResp;
			
			writeParams(alarmRequest);
			
			//确保写完params文件之后执行
			Process pr = Runtime.getRuntime().exec("python " + pyShellPath);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					pr.getErrorStream()));
			
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				logger.info("[process callback content : "+line+"]");
			}
			in.close();
			pr.waitFor();
			logger.info("[process end...]");
			
			return readResult();
		} catch (Exception e) {
			logger.error("",e);
			AlarmResponse alarmResp = new AlarmResponse();
			alarmResp.setCode(AlarmResponse.Type.ERROR.code);
			alarmResp.setMsg(AlarmResponse.Type.ERROR.msg);
			return alarmResp;
		}
	}

	/*public AlarmResponse execPythonShell(Map<String, Object> params) {

		AlarmResponse alarmResp = new AlarmResponse();
		try {
			PythonInterpreter interpreter = new PythonInterpreter();

			String pyShellPath = NettyConfig.getInstance().getConfigValue(
					NettyConfig.KEY_PYSHELL_PATH);
			String pyShellFunction = NettyConfig.getInstance().getConfigValue(
					NettyConfig.KEY_PYSHELL_FUNCTION);

			logger.info("[alarm py shell path : " + pyShellPath
					+ ",function : " + pyShellFunction + "]");
			
			
			PySystemState sys = Py.getSystemState(); 
//			sys.path.add("D:/Python27/Lib/logging");
			sys.path.add("C:/Users/wangzhijie/PycharmProjects/msg-sender/sms_send.py");
//			interpreter.exec("import sms_send");
//			interpreter.exec("import sys");
//			interpreter.exec("sys.path.append('C:/Users/wangzhijie/PycharmProjects/msg-sender')");//jython自己的
//			interpreter.exec("sys.path.append('D:/Python27/Lib/logging/py')");//jython自己的
//			interpreter.exec("import logging");
			System.out.println(sys.path);
			//call python shell
			interpreter.execfile(pyShellPath);
			

			PyFunction func = (PyFunction) interpreter.get(pyShellFunction,
					PyFunction.class);

			AlarmRequest alarmRequest = filter.convert(params);
			alarmRequest.convert();

			AlarmResponse checkResp = filter.checkParams(alarmRequest);
			if (checkResp.getCode() != AlarmResponse.Type.OK.code)
				return checkResp;
			PyObject pyobj = null;
			if (StringUtils.isEmpty(alarmRequest.getContent())) {
				pyobj = func.__call__(new PyList(alarmRequest.getPhoneList()),
						new PyString(alarmRequest.getPort()), new PyInteger(
								alarmRequest.getBps()));
			} else {
				pyobj = func.__call__(new PyList(alarmRequest.getPhoneList()),
						new PyString(alarmRequest.getPort()), new PyInteger(
								alarmRequest.getBps()), new PyString(
								alarmRequest.getContent()));
			}

			logger.info("[python anwser = " + unicodeToString(pyobj.toString())
					+ "]");

			if (pyobj instanceof PyTuple) {
				int status = Integer.parseInt(((PyTuple) pyobj).getArray()[0]
						.toString());
				String msg = ((PyTuple) pyobj).getArray()[1].toString();

				switch (status) {
				case 0:
					alarmResp.setCode(AlarmResponse.Type.ERROR.code);
					alarmResp.setMsg(msg);
					return alarmResp;
				case 1:
					alarmResp.setCode(AlarmResponse.Type.OK.code);
					alarmResp.setMsg(AlarmResponse.Type.OK.msg);
					return alarmResp;
				default:
					alarmResp.setCode(AlarmResponse.Type.ERROR.code);
					alarmResp.setMsg(AlarmResponse.Type.ERROR.msg);
					return alarmResp;
				}
			} else {
				alarmResp.setCode(AlarmResponse.Type.CALLPYERROR.code);
				alarmResp.setMsg(AlarmResponse.Type.CALLPYERROR.msg);
				return alarmResp;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
			alarmResp.setCode(AlarmResponse.Type.ERROR.code);
			alarmResp.setMsg(AlarmResponse.Type.ERROR.msg);
			return alarmResp;
		}
	}*/

	public static void main(String[] args) {
		// System.out.println(stringToUnicode("360告警处置中心测试短信!"));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("phone", "1862783728");
		params.put("content", "360告警处置中心测试短信!");
		params.put("bps", "115200");
		AlarmResponse rep =  AlarmController.getInstance().directCallPython(params);
		System.out.println(rep.getCode());
//		params.put("content", "考虑的是否老实交代");
		
//		AlarmController.getInstance().execPythonShell(params);
		// AlarmController.getInstance().test();
	}

	/**
	 * Unicode转 汉字字符串
	 * 
	 * @param str
	 *            \u6728
	 * @return '木' 26408
	 */
	private String unicodeToString(String str) {

		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			// group 6728
			String group = matcher.group(2);
			// ch:'木' 26408
			ch = (char) Integer.parseInt(group, 16);
			// group1 \u6728
			String group1 = matcher.group(1);
			str = str.replace(group1, ch + "");
		}
		return str;
	}

	/**
	 * 获取字符串的unicode编码 汉字“木”的Unicode 码点为Ox6728
	 * 
	 * @param s
	 *            木
	 * @return \ufeff\u6728 \ufeff控制字符 用来表示「字节次序标记（Byte Order Mark）」不占用宽度
	 *         在java中一个char是采用unicode存储的 占用2个字节 比如 汉字木 就是 Ox6728
	 *         4bit+4bit+4bit+4bit=2字节
	 */
	public String stringToUnicode(String s) {
		try {
			StringBuffer out = new StringBuffer("");
			// 直接获取字符串的unicode二进制
			byte[] bytes = s.getBytes("unicode");
			// 然后将其byte转换成对应的16进制表示即可
			for (int i = 0; i < bytes.length - 1; i += 2) {
				out.append("\\u");
				String str = Integer.toHexString(bytes[i + 1] & 0xff);
				for (int j = str.length(); j < 2; j++) {
					out.append("0");
				}
				String str1 = Integer.toHexString(bytes[i] & 0xff);
				out.append(str1);
				out.append(str);
			}
			return out.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
