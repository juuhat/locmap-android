package net.locmap.locmap.models;

import java.util.ArrayList;
import java.util.Date;

/** 
 * Location class to save coordinates, title, info and images about a certain location
 * @author Juuso Hatakka
 */
public class Location {
	private String id;
	private String title;
	private String description;
	private float latitude;
	private float longitude;
	private ArrayList<String> images;
	private Date updated;
	private Date created;
	
	public Location() {
		this.id = "";
		this.title = "";
		this.description = "";
		this.latitude = 0f;
		this.longitude = 0f;
		this.images = new ArrayList<String>();
		this.created = new Date();
		this.updated = new Date();
	}
	
	public Location(String id, String title, String description, Float latitude, Float longitude, ArrayList<String> images, Date updated, Date created) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
		this.images = images;
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

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setFloat(Float longitude) {
		this.longitude = longitude;
	}
	
	public void setImages(ArrayList<String> images) {
		this.images = images;
	}
	
	public ArrayList<String> getImages() {
		return this.images;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
}
