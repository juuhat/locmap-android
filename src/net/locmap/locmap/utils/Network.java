package net.locmap.locmap.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Class that takes care all the network traffic. Sends and receives stuff from
 * locmap API
 * 
 * @author Janne Heikkinen
 */
public class Network {

	public final static String base = "http://api.locmap.net/v1/";
	public final static String registerUrl = base + "auth/register/";
	public final static String loginUrl = base + "auth/login/";
	public final static String logoutUrl = base + "auth/logout/";
	public final static String locationsUrl = base + "locations/";
	public final static String imagesUrl = base + "images/";
	public final static String usersUrl = base + "users/";

	/**
	 * Sends HTTP POST request, returns the response
	 * 
	 * @param url where to send request
	 * @param json data to send
	 * @return response
	 */
	public static Response Post(String url, String json) {
		return Post(url, json, "");
	}

	/**
	 * Checks whether device is connected to the Internet or not
	 * 
	 * @return
	 */
	public static boolean isNetworkAvailable(Activity act) {
		ConnectivityManager connectivityManager = (ConnectivityManager) act
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	/**
	 * Sends HTTP POST request, returns the response
	 * 
	 * @param url  where to send post request
	 * @param json data to send
	 * @param token Authentication key
	 * @return Response
	 */
	public static Response Post(String url, String json, String token) {
		Response res = new Response();
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

			String body = "";
			inputstream = response.getEntity().getContent();
			if (inputstream != null)
				body = convertInputStreamToString(inputstream);

			res.setHeaders(response.getAllHeaders());
			res.setStatusCode(response.getStatusLine().getStatusCode());
			res.setBody(body);

		} catch (ClientProtocolException e) {
			Log.d("ClientProtocolException", e.getMessage());
		} catch (IOException e) {
			Log.d("IOException: ", e.getMessage());
		}

		return res;
	}

	/**
	 * Sends HTTP POST Multipart request, returns response
	 * 
	 * @param url where to send post request
	 * @param file to send
	 * @param location ObjectId where this image will be attached
	 * @param token Authentication key
	 * @return Response
	 */
	public static Response Post(String url, File file, String location,
			String token) {
		Response res = new Response();
		InputStream inputstream = null;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			post.setHeader("Authorization", "Bearer " + token);

			// Build multipart entity
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder
					.create();
			entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			entityBuilder.addTextBody("location", location);
			entityBuilder.addBinaryBody("image", file);

			HttpEntity entity = entityBuilder.build();
			post.setEntity(entity);

			HttpResponse response = client.execute(post);

			String body = "";
			inputstream = response.getEntity().getContent();
			if (inputstream != null)
				body = convertInputStreamToString(inputstream);

			res.setHeaders(response.getAllHeaders());
			res.setStatusCode(response.getStatusLine().getStatusCode());
			res.setBody(body);

		} catch (ClientProtocolException e) {
			Log.d("ClientProtocolException", e.getMessage());
		} catch (IOException e) {
			Log.d("IOException: ", e.getMessage());
		}

		return res;

	}

	/**
	 * Send HTTP GET request
	 * @param url
	 * @return response
	 */
	public static Response Get(String url) {
		HttpClient client = new DefaultHttpClient();
		Response res = new Response();
		InputStream inputstream = null;
		
		try {
			HttpGet get = new HttpGet(url);
			get.setHeader("Accept", "application/json");
			get.setHeader("Content-type", "application/json");

			HttpResponse response = client.execute(get);
			
			String body = "";
			inputstream = response.getEntity().getContent();
			if (inputstream != null)
				body = convertInputStreamToString(inputstream);
			
			res.setHeaders(response.getAllHeaders());
			res.setStatusCode(response.getStatusLine().getStatusCode());
			res.setBody(body);
			
		} catch (ClientProtocolException e) {
			Log.d("ClientProtocolException", e.getMessage());
		} catch (IOException e) {
			Log.d("IOException: ", e.getMessage());
		}

		return res;
	}

	/**
	 * @param inputStream to convert
	 * @return inputStream as string
	 * @throws IOException if something goes terribly wrong
	 */
	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	}

}
