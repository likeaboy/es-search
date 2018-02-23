package secfox.soc.netty.server;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * HTTP请求参数解析器, 支持GET, POST
 * @author wangzhijie
 */
public class NettyFullRequestParser {
    private FullHttpRequest fullReq;

    /**
     * 构造一个解析器
     * @param req
     */
    public NettyFullRequestParser(FullHttpRequest req) {
        this.fullReq = req;
    }
    
    public static void main(String[] args) {
    	
    		Map<String,Object> parmMap = test("https://127.0.0.1?phone=phoneNumber&port=123&bps=haha&content=%E5%91%8A%E8%AD%A6%E6%A0%87%E9%A2%98%EF%BC%9A%3B%E6%BA%90IP%E5%9C%B0%E5%9D%80%EF%BC%9A%3Btest");
//    	Map<String,Object> parmMap = test("/alarm?phone=18519664027\\;port=/dev/ttyUSB0\\;bps=115200&content=%E5%91%8A%E8%AD%A6%E6%A0%87%E9%A2%98%EF%BC%9A;%E6%BA%90IP%E5%9C%B0%E5%9D%80%EF%BC%9A;test");
	}
    
    public static Map<String,Object> test(String uri){
    	Map<String, Object> parmMap = new HashMap<>();
    	// 是GET请求
        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        Set<Entry<String, List<String>>> entrySet = decoder.parameters().entrySet();
        System.out.println(entrySet);
        for(Entry entry : entrySet){
//        	((List<String>)entry.getValue()).get(0);
        	parmMap.put((String)entry.getKey(), ((List<String>)entry.getValue()).get(0));
        }
        return parmMap;
    }

    /**
     * 解析请求参数
     * @return 包含所有请求参数的键值对, 如果没有参数, 则返回空Map
     *
     * @throws BaseCheckedException
     * @throws IOException
     */
    public Map<String, Object> parse() throws IOException {
        HttpMethod method = fullReq.method();

        Map<String, Object> parmMap = new HashMap<>();

        if (HttpMethod.GET == method) {
            // 是GET请求
            QueryStringDecoder decoder = new QueryStringDecoder(fullReq.uri());
            Set<Entry<String, List<String>>> entrySet = decoder.parameters().entrySet();
            System.out.println(entrySet);
            for(Entry entry : entrySet){
            	parmMap.put((String)entry.getKey(), ((List<String>)entry.getValue()).get(0));
            }
        } else if (HttpMethod.POST == method) {
            // 是POST请求
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(fullReq);
            decoder.offer(fullReq);

            List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();

            for (InterfaceHttpData parm : parmList) {

                Attribute data = (Attribute) parm;
                parmMap.put(data.getName(), data.getValue());
            }

        } else {
        }

        return parmMap;
    }
}