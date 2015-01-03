package com.project11.bikeshare.Beans;

public class RentDetails {
	private String user_id_owner;
	private String user_id_renter;
	private String bike_id;
	private String start_time;
	private String end_time;
	private String remarks;
	private String received;
	private String isNotificationReceived;
	private String rent_details_id;
	
	public String getRent_details_id() {
		return rent_details_id;
	}
	public void setRent_details_id(String rent_details_id) {
		this.rent_details_id = rent_details_id;
	}
	public String getIsNotificationReceived() {
		return isNotificationReceived;
	}
	public void setIsNotificationReceived(String isNotificationReceived) {
		this.isNotificationReceived = isNotificationReceived;
	}
	public String getUser_id_owner() {
		return user_id_owner;
	}
	public void setUser_id_owner(String user_id_owner) {
		this.user_id_owner = user_id_owner;
	}
	public String getUser_id_renter() {
		return user_id_renter;
	}
	public void setUser_id_renter(String user_id_renter) {
		this.user_id_renter = user_id_renter;
	}
	public String getBike_id() {
		return bike_id;
	}
	public void setBike_id(String bike_id) {
		this.bike_id = bike_id;
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
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getReceived() {
		return received;
	}
	public void setReceived(String received) {
		this.received = received;
	}
	
	
	
}
