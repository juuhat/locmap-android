package net.locmap.locmap.models;

import java.util.ArrayList;
import java.util.Date;

public class Collection {
	private String id;
	private String title;
	private String description;
	private ArrayList<String> locations;
	private Date updated;
	private Date created;
	
	public Collection() {
		this.id = "";
		this.title = "";
		this.description = "";
		this.locations = new ArrayList<String>();
		this.created = new Date();
		this.updated = new Date();
	}
	
	public Collection(String id, String title, String description, ArrayList<String> locations, Date updated, Date created) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.locations = locations;
		this.updated = updated;
		this.created = created;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<String> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<String> locations) {
		this.locations = locations;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
}
