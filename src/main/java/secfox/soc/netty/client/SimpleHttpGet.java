/*package secfox.soc.netty.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class SimpleHttpGet {
	public static void main(String[] args) {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("http://www.vogella.com");
		HttpResponse response = client.execute(request);
		
		// Get the response
		BufferedReader rd = new BufferedReader
		    (new InputStreamReader(
		    response.getEntity().getContent()));
		
		String line = "";
		StringBuilder textView = new StringBuilder();
		while ((line = rd.readLine()) != null) {
		    textView.append(line);
		}
	}
}

*/