package net.locmap.locmap.models;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class User {
	private String id;
	private String email;
	private String username;
	private Date created;
	private ArrayList<Location> locations;
	private ArrayList<Collection> collections;
	
	public User() {
		this.id = "";
		this.email = "";
		this.username = "";
		this.created = new Date();
		this.locations = new ArrayList<Location>();
		this.collections = new ArrayList<Collection>();
	}
	
	/**
	 * Constructor using JSON String
	 * @param json
	 */
	public User(String json) {
		try {
			JSONObject jsonObj = new JSONObject(json);
			this.id = jsonObj.getString("_id");
			//this.email = jsonObj.getString("email");
			this.username = jsonObj.getString("username");
			
			this.locations = new ArrayList<Location>();
			JSONArray jsonLocations = jsonObj.getJSONArray("locations");
			for (int i = 0; i < jsonLocations.length(); i++) {
				JSONObject row = jsonLocations.getJSONObject(i);
				Location loc = new Location(row.toString());
				this.locations.add(loc);
			}
			
			//TODO get collections from JSON
			//TODO get created from JSON
			this.created = new Date();
			
		} catch (JSONException e) {
			Log.e("e", e.toString());
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ArrayList<Location> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<Location> locations) {
		this.locations = locations;
	}

	public ArrayList<Collection> getCollections() {
		return collections;
	}

	public void setCollections(ArrayList<Collection> collections) {
		this.collections = collections;
	}
	
}
