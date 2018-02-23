package secfox.soc.netty.client.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;

import secfox.soc.es.search.request.ESQueryRequestParam;
import secfox.soc.es.search.response.ESQueryResponse;
import secfox.soc.netty.HttpSearchMethod;
import secfox.soc.netty.client.SIMEventObject;

/**
 * 本机测试类
 * @author wangzhijie
 *
 */
public class TestHttpClient {
    /**
     * @param args
     */
    public static void main(String[] args) {
        HttpClient client = new DefaultHttpClient();
        /*HttpPost post = new HttpPost(
                "http://10.95.32.29:8090" + HttpSearchMethod.SIMQUERY);*/
        HttpPost post = new HttpPost(
                "http://10.95.32.23:8090/" + HttpSearchMethod.SIMQUERY);
        /*HttpPost post = new HttpPost(
                "http://10.74.12.121:8090/" + HttpSearchMethod.SIMQUERY);*/

        try {
        	long start = System.currentTimeMillis();
        	
        	/*public static final String KEY_DEVADDR="devAddr";
        	public static final String KEY_SADDR="sAddr";
        	public static final String KEY_SPORT="sPort";
        	public static final String KEY_DADDR="dAddr";
        	public static final String KEY_DPORT="dPort";
        	public static final String KEY_ID="id";
        	public static final String KEY_SYSTYPE="sysType";
        	public static final String KEY_PRIORITY="priority";
        	public static final String KEY_DEVTYPE="devType";
        	public static final String KEY_ACTION="action";
        	public static final String KEY_RESULT="result";
        	
        	public static final String KEY_SUSERNAME="sUserName";
        	public static final String KEY_STADDR="stAddr";
        	public static final String KEY_STPORT="stPort";
        	public static final String KEY_DTADDR="dtAddr";
        	public static final String KEY_DTPORT="dtPort";*/
        	
        	
//    		condMap.put(ConditionParser.KEY_DEVADDR, "~10.33.232.2");
//    		condMap.put(ConditionParser.KEY_SADDR, "~10.181.117.201");
//    		condMap.put(ConditionParser.KEY_SPORT, "3844");
//    		condMap.put(ConditionParser.KEY_DADDR, "10.22.35.17");
//    		condMap.put(ConditionParser.KEY_DPORT, "56412");
    		//151063200002621
//    		condMap.put(ConditionParser.KEY_ID, "151063200002621");
//    		condMap.put(ConditionParser.KEY_SYSTYPE, "3");
//    		condMap.put(ConditionParser.KEY_PRIORITY, "2,3");
//    		condMap.put(ConditionParser.KEY_DEVTYPE, "100");
        	
        	
        	/*public static final String KEY_SADDR="sAddr";
        	public static final String KEY_SPORT="sPort";
        	public static final String KEY_DADDR="dAddr";
        	public static final String KEY_DPORT="dPort";
        	public static final String KEY_ID="id";
        	public static final String KEY_SYSTYPE="sysType";
        	public static final String KEY_PRIORITY="priority";
        	public static final String KEY_DEVTYPE="devType";
        	public static final String KEY_ACTION="action";
        	public static final String KEY_RESULT="result";*/
        	
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(16);
            
//            nameValuePairs.add(new BasicNameValuePair("stPort", "0"));
            
//            nameValuePairs.add(new BasicNameValuePair("customS1", "55686"));
            
//            nameValuePairs.add(new BasicNameValuePair("rawID", "0"));
            
//            nameValuePairs.add(new BasicNameValuePair("collectType", "6"));
//            nameValuePairs.add(new BasicNameValuePair("name", "dsfs"));
//            nameValuePairs.add(new BasicNameValuePair("object", "dsfs"));
           
            
//            nameValuePairs.add(new BasicNameValuePair("startTime", "1511196360000"));
//            nameValuePairs
//                    .add(new BasicNameValuePair("endTime", "1511279160000"));
           /* nameValuePairs.add(new BasicNameValuePair("startTime", "1510741200000"));
            nameValuePairs
                    .add(new BasicNameValuePair("endTime", "1511229600000"));*/
            
//            nameValuePairs.add(new BasicNameValuePair("devAddr", "~10.33.232.2"));
//            nameValuePairs.add(new BasicNameValuePair("sysType", "1"));
//            nameValuePairs.add(new BasicNameValuePair("priority", "2"));
//            nameValuePairs.add(new BasicNameValuePair(ESQueryRequestParam.KEY_ID, "151123068018828"));
            
//            nameValuePairs.add(new BasicNameValuePair("order", "desc"));

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));	
            
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            String line = rd.readLine();
            ESQueryResponse esqResp = JSON.parseObject(line, ESQueryResponse.class);
            System.out.println(esqResp.toString());
           
            System.out.println(esqResp.getData());
            List<SIMEventObject> evtList = JSON.parseArray("["+esqResp.getData()+"]", SIMEventObject.class);
            System.out.println(evtList.size());
            System.out.println(System.currentTimeMillis()-start+"ms");
            rd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}