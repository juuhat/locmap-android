package net.locmap.locmap.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

/**
 * Class for storing HTTP responses from API
 * @author Juuso Hatakka
 */

public class Response {
	private int statusCode;
	private Map<String, String> headers;
	private String body;
	
	public Response() {
		this.statusCode = 0;
		this.headers = new HashMap<String, String>();
		this.body = "";
	}
	
	public Response(int statusCode, Map<String, String> headers, String body) {
		this.statusCode = statusCode;
		this.headers = headers;
		this.body = body;
	}
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
	
	public String getHeader(String name) {
		return this.headers.get(name);
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public void setHeaders(Header[] headers) {
		for (Header h : headers) {
			this.headers.put(h.getName(), h.getValue());
		}
	}
	
	public void addHeader(String name, String value) {
		this.headers.put(name, value);
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
}
