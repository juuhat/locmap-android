package net.locmap.locmap.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Juuso Hatakka
 * @author Janne Heikkinen
 * 
 * Collection is a set of locations
 */
public class CollectionModel {
	private String id;
	private String title;
	private String description;
	private ArrayList<LocationModel> locations;
	private Date updated;
	private Date created;
	
	public CollectionModel() {
		this.id = "";
		this.title = "";
		this.description = "";
		this.locations = new ArrayList<LocationModel>();
		this.created = new Date();
		this.updated = new Date();
	}
	
	public CollectionModel(String id, String title, String description, ArrayList<LocationModel> locations, Date updated, Date created) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.locations = locations;
		this.updated = updated;
		this.created = created;
	}
	
	
	/** Deletes location with given ID from the collection. Doesn't delete the location
	 * @param locationId ID of the location to be deleted
	 */
	public void deleteLocation(String locationId) {
		for (int i = 0; i < locations.size(); ++i) {
			if (locations.get(i).getId().equals(locationId)) 
				locations.remove(i);
		}
	}
	
	
	/**
	 * Adds Location to collection datastructure
	 * @param loc location to be added
	 */
	public void addLocation(LocationModel loc) {
		locations.add(loc);
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

	public ArrayList<LocationModel> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<LocationModel> locations) {
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
