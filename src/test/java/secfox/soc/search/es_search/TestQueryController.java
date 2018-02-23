package secfox.soc.search.es_search;

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

import secfox.soc.es.search.response.ESQueryResponse;
import secfox.soc.netty.HttpSearchMethod;
import secfox.soc.netty.client.SIMEventObject;

import com.alibaba.fastjson.JSON;

/**
 * 本机测试类
 * @author wangzhijie
 *
 */
public class TestQueryController {
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
        "http://127.0.0.1:8090" + HttpSearchMethod.HITCOUNT);

        try {
        	long start = System.currentTimeMillis();
        	
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(16);
            
            nameValuePairs.add(new BasicNameValuePair("startTime", "1514304000000"));
            
            nameValuePairs.add(new BasicNameValuePair("endTime", "1514494800000"));
            
            nameValuePairs.add(new BasicNameValuePair("hitIndex","skyeye-las_event-2017.12.28"));

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