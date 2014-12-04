package net.locmap.locmap.models;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Location class to save coordinates, title, info and images about a certain
 * location
 * 
 * @author Juuso Hatakka
 */
public class LocationModel implements Parcelable {

	private String id;
	private String title;
	private String description;
	private float latitude;
	private float longitude;
	private ArrayList<String> owners;
	private ArrayList<String> images;
	private Date updated;
	private Date created;

	public LocationModel() {
		this.id = "";
		this.title = "";
		this.description = "";
		this.latitude = 0f;
		this.longitude = 0f;
		this.owners = new ArrayList<String>();
		this.images = new ArrayList<String>();
		this.created = new Date();
		this.updated = new Date();
	}

	public LocationModel(String id, String title, String description,
			float latitude, float longitude, ArrayList<String> owners ,ArrayList<String> images,
			Date updated, Date created) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
		this.owners = owners;
		this.images = images;
		this.updated = updated;
		this.created = created;
	}
	
	/**
	 * Constructor using JSON String
	 * @param json
	 */
	public LocationModel(String json) {
		try {
			JSONObject jsonObj = new JSONObject(json);
			this.id = jsonObj.getString("_id");
			this.title = jsonObj.getString("title");
			this.description = jsonObj.getString("description");
			this.latitude = (float) jsonObj.getDouble("latitude");
			this.longitude = (float) jsonObj.getDouble("longitude");
			
			this.owners = new ArrayList<String>();
			JSONArray jsonOwners = jsonObj.optJSONArray("owners");
			if (jsonOwners != null) {
				for (int i = 0; i < jsonOwners.length(); ++i) {
					this.owners.add(jsonOwners.getString(i));
				}
			}
			
			this.images = new ArrayList<String>();
			JSONArray jsonImages = jsonObj.optJSONArray("images");
			if (jsonImages != null) {
				for (int i = 0; i < jsonImages.length(); i++) {
					this.images.add(jsonImages.getString(i));
				}
			}
			
			//TODO get values from json
			this.created = new Date();
			this.updated = new Date();
			
		} catch (JSONException e) {
			Log.e("e", e.toString());
		}
	}

	public ArrayList<String> getOwners() {
		return owners;
	}

	public void setOwners(ArrayList<String> owners) {
		this.owners = owners;
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

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
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
	
	
	/**
	 * Constructor using Parcel data
	 * @param in
	 */
	public LocationModel(Parcel in) {
		this.id = in.readString();
		this.title = in.readString();
		this.description = in.readString();
		this.latitude = in.readFloat();
		this.longitude = in.readFloat();
		this.owners= new ArrayList<String>();
		in.readList(this.owners, String.class.getClassLoader());
		this.images = new ArrayList<String>();
		in.readList(this.images, String.class.getClassLoader());
		this.updated = new Date(in.readLong());
		this.created = new Date(in.readLong());
	}

	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.title);
		dest.writeString(this.description);
		dest.writeFloat(this.latitude);
		dest.writeFloat(this.longitude);
		dest.writeList(this.owners);
		dest.writeList(this.images);
		dest.writeLong(this.updated.getTime());
		dest.writeLong(this.created.getTime());
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<LocationModel> CREATOR = new Parcelable.Creator<LocationModel>() {
		public LocationModel createFromParcel(Parcel in) {
			return new LocationModel(in);
		}

		public LocationModel[] newArray(int size) {
			return new LocationModel[size];
		}
	};

}
