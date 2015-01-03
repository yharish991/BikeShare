package com.project11.bikeshare.Beans;

public class BikeContext {
	private RentDetails rentdetails;
	private Bikes bike;
	public RentDetails getRentdetails() {
		return rentdetails;
	}
	public void setRentdetails(RentDetails rentdetails) {
		this.rentdetails = rentdetails;
	}
	public Bikes getBike() {
		return bike;
	}
	public void setBike(Bikes bike) {
		this.bike = bike;
	}

}
