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

import secfox.soc.es.search.response.ESQueryResponse;
import secfox.soc.netty.HttpSearchMethod;

/**
 * 本机测试类
 * @author wangzhijie
 *
 */
public class TestMoreHttpClient {
    /**
     * @param args
     */
    public static void main(String[] args) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(
                "http://127.0.0.1:8090/" + HttpSearchMethod.SIMQUERY);

        try {
        	long start = System.currentTimeMillis();
        	
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(16);
            nameValuePairs.add(new BasicNameValuePair("startTime", "1511118660000"));
            nameValuePairs
                    .add(new BasicNameValuePair("endTime", "1511193420000"));
           /* nameValuePairs.add(new BasicNameValuePair("startTime", "1510741200000"));
            nameValuePairs
                    .add(new BasicNameValuePair("endTime", "1511229600000"));*/
            
            nameValuePairs.add(new BasicNameValuePair("devAddr", "~127.0.0.1"));
            
            nameValuePairs.add(new BasicNameValuePair("hitIndex", "skyeye-las_event-2017.11.20"));
            nameValuePairs.add(new BasicNameValuePair("hitSlotKey", "slot_1511132400000"));
            nameValuePairs.add(new BasicNameValuePair("slotFrom", "2002"));
            
//            nameValuePairs.add(new BasicNameValuePair("sysType", "1"));
//            nameValuePairs.add(new BasicNameValuePair("priority", "2"));
            
            
//            nameValuePairs.add(new BasicNameValuePair("order", "asc"));

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));	
            
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            String line = rd.readLine();
            ESQueryResponse esqResp = JSON.parseObject(line, ESQueryResponse.class);
            System.out.println(esqResp.toString());
           
//            System.out.println(esqResp.getData());
           /* List<SIMEventObject> evtList = JSON.parseArray("["+esqResp.getData()+"]", SIMEventObject.class);
            System.out.println(evtList.size());*/
            System.out.println(System.currentTimeMillis()-start+"ms");
            rd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}