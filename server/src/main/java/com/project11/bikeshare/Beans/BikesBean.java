package com.project11.bikeshare.Beans;

public class BikesBean {
	
	private String user_id;
	private String isBikeAvailable;
	private String bike_id;
	private String accessCode;
	private LocationBean location;
	private String pincode;
	private String bikeModel;
	private String start_time;
	private String end_time;
	private String uuid;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getBikeModel() {
		return bikeModel;
	}
	public void setBikeModel(String bikeModel) {
		this.bikeModel = bikeModel;
	}
	public LocationBean getLocation() {
		return location;
	}
	public void setLocation(LocationBean location) {
		this.location = location;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getIsBikeAvailable() {
		return isBikeAvailable;
	}
	public void setIsBikeAvailable(String isBikeAvailable) {
		this.isBikeAvailable = isBikeAvailable;
	}
	public String getBike_id() {
		return bike_id;
	}
	public void setBike_id(String bike_id) {
		this.bike_id = bike_id;
	}
	public String getAccessCode() {
		return accessCode;
	}
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

}
