package net.locmap.locmap.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

/**
 * Class that takes care all the network traffic.
 * Sends and receives stuff from locmap API
 * @author Janne Heikkinen
 */
public class Network {

	public final static String base = "http://api.locmap.net/v1/";
	public final static String registerUrl = base + "auth/register";
	public final static String loginUrl = base + "auth/login";
	
	
	/**
	 * Sends HTTP POST request, returns the response
	 * @param url where to send request
	 * @param json data to send
	 * @return response
	 */
	public static String Post(String url, String json) {
		return Post(url, json, "");
	}
	
	
	/** 
	 * Sends HTTP POST request, returns the response
	 * TODO: Returns only response as string, should return also access token!
	 * @param url where to send post request
	 * @param json data to send
	 * @param token Authentication key
	 * @return Response
	 */
	public static String Post(String url, String json, String token) {
		String result = "";
		InputStream inputstream = null;
		StringEntity se;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			se = new StringEntity(json);
			post.setEntity(se);
			post.setHeader("Accept", "application/json");
	        post.setHeader("Content-type", "application/json");
	        post.setHeader("Authorization", "Bearer " + token);

			HttpResponse response = client.execute(post);
			
			inputstream = response.getEntity().getContent();
			
			if (inputstream != null)
				result = convertInputStreamToString(inputstream);
			else
				result = "";
			
		} catch (ClientProtocolException e) {
			Log.d("ClientProtocolException", e.getMessage());
		} catch (IOException e) {
			Log.d("IOException: ", e.getMessage());
		}
		
		return result;
	}
	
	
	/**
	 * @param inputStream to convert
	 * @return inputStream as string
	 * @throws IOException if something goes terribly wrong
	 */
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
    }   
	

}