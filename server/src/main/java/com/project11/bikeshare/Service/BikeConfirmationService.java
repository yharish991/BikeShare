package com.project11.bikeshare.Service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jongo.MongoCursor;

import com.project11.bikeshare.Beans.Bikes;
import com.project11.bikeshare.Beans.BikesBean;
import com.project11.bikeshare.Beans.RentDetails;
import com.project11.bikeshare.Beans.User;
import com.project11.bikeshare.Beans.UserFeedback;
import com.project11.bikeshare.DBImpl.BikeConfirmationDAO;
import com.project11.bikeshare.util.TwilioMessage;
import com.twilio.sdk.TwilioRestException;

public class BikeConfirmationService {
	BikeConfirmationDAO bikeConfirmationDAO=new BikeConfirmationDAO();
	TwilioMessage twilioMessage = new TwilioMessage();
	
	public String confirmRent(String bike_id,String user_id) {
		BikesBean bike;
		User user = null ;
		RentDetails rentDetails;
		String returnString="";
		user = bikeConfirmationDAO.findUser(user_id);
		//check if renter is eligible to rent bike
		if(isUserEligible(user_id)){
			bike = bikeConfirmationDAO.confirmRent(bike_id);
			
			rentDetails=bikeConfirmationDAO.updateRentDetails(bike_id,user_id);
			sendUserConfirmationTextMessage(user,bike);
			sendBikeOwnerTextMessage(rentDetails);
			returnString = "success";
		}else{
			returnString = "fail";
			sendUserRefusalTextMessage(user);
		}
		System.out.println(returnString);
		return returnString;
		
		
	}
	
	private void sendUserRefusalTextMessage(User user) {
		// TODO Auto-generated method stub
		StringBuffer b = new StringBuffer("");
		b.append("Your rating is too low to rent a bike");
		b.append("\n");
		b.append("Please contact out helpdesk");
		
		try {
			twilioMessage.sendMessage(user.getMobile_number(),b.toString());
		} catch (TwilioRestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean isUserEligible(String user_id) {
		// TODO Auto-generated method stub
		MongoCursor userFeedbackCursor = bikeConfirmationDAO.getUsersFeedBackHistory(user_id);
		if(userFeedbackCursor.count()==0){
			return Boolean.TRUE;
		}
		Iterator<UserFeedback> it = userFeedbackCursor.iterator();
		UserFeedback  userFeedback;
		List<Integer> userScoreList= new ArrayList<Integer>();
		while(it.hasNext()){
			userFeedback = it.next();
			userScoreList.add(Integer.parseInt(userFeedback.getRatings()));
		}
		int userScore = (int) calculateAverage(userScoreList);
		if(userScore<=2){
			return Boolean.FALSE;
		}else{
			return Boolean.TRUE;
		}
		
	}
	
	private double calculateAverage(List <Integer> scores) {
		  Integer sum = 0;
		  if(!scores.isEmpty()) {
		    for (Integer mark : scores) {
		        sum += mark;
		    }
		    return sum.doubleValue() / scores.size();
		  }
		  return sum;
		}

	public Bikes findBike(String bikeid) throws UnknownHostException
	{
		return new BikeConfirmationDAO().findBike(bikeid);
	}
	private void sendBikeOwnerTextMessage(RentDetails rentDetails) {
		User owner = bikeConfirmationDAO.findUser(rentDetails.getUser_id_owner());
		StringBuilder sb = new StringBuilder("");
		sb.append("Your Bike is in use !");
		sb.append("\n");
		sb.append("Renter id :"+rentDetails.getUser_id_renter());
		
		try {
			twilioMessage.sendMessage(owner.getMobile_number(), sb.toString());
		} catch (TwilioRestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendUserConfirmationTextMessage(User user,BikesBean bike){
		StringBuffer b = new StringBuffer("");
		b.append("Access Code :"+bike.getAccessCode());
		b.append("\n");
		b.append("Start Time :"+new Date(Long.parseLong(bike.getStart_time())));
		b.append("\n");
		b.append("End Time :"+new Date(Long.parseLong(bike.getEnd_time())));
		b.append("\n");
		b.append("Thanks for using our service please return the bike on time.");
		
		try {
			twilioMessage.sendMessage(user.getMobile_number(),b.toString());
		} catch (TwilioRestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
