package net.locmap.locmap.models;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class UserModel {
	private String id;
	private String email;
	private String username;
	private Date created;
	private ArrayList<LocationModel> locations;
	private ArrayList<CollectionModel> collections;
	
	public UserModel() {
		this.id = "";
		this.email = "";
		this.username = "";
		this.created = new Date();
		this.locations = new ArrayList<LocationModel>();
		this.collections = new ArrayList<CollectionModel>();
	}
	
	/**
	 * Constructor using JSON String
	 * @param json
	 */
	public UserModel(String json) {
		try {
			JSONObject jsonObj = new JSONObject(json);
			this.id = jsonObj.getString("_id");
			this.email = jsonObj.optString("email");
			this.username = jsonObj.getString("username");
			
			this.locations = new ArrayList<LocationModel>();
			JSONArray jsonLocations = jsonObj.optJSONArray("locations");
			if (jsonLocations != null) {
				for (int i = 0; i < jsonLocations.length(); i++) {
					JSONObject row = jsonLocations.getJSONObject(i);
					LocationModel loc = new LocationModel(row.toString());
					this.locations.add(loc);
				}	
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

	public ArrayList<LocationModel> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<LocationModel> locations) {
		this.locations = locations;
	}

	public ArrayList<CollectionModel> getCollections() {
		return collections;
	}

	public void setCollections(ArrayList<CollectionModel> collections) {
		this.collections = collections;
	}
	
}
