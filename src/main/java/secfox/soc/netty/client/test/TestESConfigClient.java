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
public class TestESConfigClient {
    /**
     * @param args
     */
    public static void main(String[] args) {
        HttpClient client = new DefaultHttpClient();
        /*HttpPost post = new HttpPost(
                "http://10.95.32.29:8090" + HttpSearchMethod.SIMQUERY);*/
        /*HttpPost post = new HttpPost(
                "http://10.95.32.23:8090/" + HttpSearchMethod.SIMQUERY);*/
        /*HttpPost post = new HttpPost(
                "http://10.74.12.121:8090/" + HttpSearchMethod.SIMQUERY);*/
        
        HttpPost post = new HttpPost(
        "http://10.95.32.29:8090" + HttpSearchMethod.ES_MODIFY);

        try {
        	long start = System.currentTimeMillis();
        	
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(16);
            
            nameValuePairs.add(new BasicNameValuePair("isCompatibility", "0"));
//            nameValuePairs.add(new BasicNameValuePair("isCompatibility", "1"));
            nameValuePairs.add(new BasicNameValuePair("delLogFactor", "84"));
            nameValuePairs.add(new BasicNameValuePair("alarmLogFactor", "95"));
            
            nameValuePairs.add(new BasicNameValuePair("esIp", "10.95.32.29"));
            

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