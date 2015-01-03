package com.project11.bikeshare.Service;

import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Random;

import com.project11.bikeshare.Beans.Bikes;
import com.project11.bikeshare.Beans.User;
import com.project11.bikeshare.Beans.UserContext;
import com.project11.bikeshare.Beans.UserFeedback;
import com.project11.bikeshare.DBImpl.RegistrationDAO;
import com.project11.bikeshare.util.BikeShareUtil;
import com.project11.bikeshare.util.TwilioMessage;
import com.twilio.sdk.TwilioRestException;


public class RegistrationService {
	final RegistrationDAO registrationDAO = new RegistrationDAO();
	private Random random = new SecureRandom();
	public String registerUser(UserContext userContext){
		//change the business logic of user details
		/*String passwordHash = BikeShareUtil.makePasswordHash(user.getPassword(), Integer.toString(random.nextInt()));
		user.setPassword(passwordHash);*/
		
		try {
			sendUserOfflineInstructionMessage(userContext);
		} catch (TwilioRestException e) {
			// TODO Auto-generated catch block
			System.out.println("Registration message not sent");
			e.printStackTrace();
		}
		return registrationDAO.registerUser(userContext);
	}
	
	private void sendUserOfflineInstructionMessage(UserContext userContext) throws TwilioRestException {
		// TODO Auto-generated method stub
		if(userContext!=null){
			TwilioMessage twilioMessage = new TwilioMessage();
			StringBuilder sb = new StringBuilder("");
			sb.append("Hello "+userContext.getUser().getUsername()+", Welcome to BikeShare11");
			sb.append("\n");
			sb.append("Follow the Instructions to Rent a bike offline");
			sb.append("\n");
			sb.append("1. You need to login to our system and provide your pin code");
			sb.append("\n");
			sb.append("[Username]<Space>[Password]<Space>[PinCode]");
			sb.append("\n");
			sb.append("For eg:amoghrao2003 MyPass@1234 95112");
			sb.append("\n");
			sb.append("2. For Finalizing the bike to Rent provide the bike id you like");
			sb.append("\n");
			sb.append("Rent<Space>[BikeID]");
			sb.append("\n");
			sb.append("For eg:Rent b_092");
			sb.append("\n");
			sb.append("Thats it! Please save this message for reference");
			twilioMessage.sendMessage(userContext.getUser().getMobile_number(), sb.toString());
		}
	}

	public User login(String user) throws UnknownHostException{
		return registrationDAO.login(user);
	}

	public void registerFeedback(UserFeedback uf, String user_id_renter) {
		// TODO Auto-generated method stub
		UserFeedback newFeedback ;
		UserFeedback existingFeedback = registrationDAO.getUserFeedback(uf,user_id_renter);
		 if(existingFeedback!=null){
			 newFeedback = calculateFeedback(existingFeedback,uf);
			 uf=newFeedback;
			 registrationDAO.feedback(uf);
		 }else{
			 registrationDAO.feedback(uf);
		 }
	}

	private UserFeedback calculateFeedback(UserFeedback existingFeedback,
			UserFeedback uf) {
		
		double existingScore =Double.parseDouble(existingFeedback.getRatings());
		double currentScore =Double.parseDouble(uf.getRatings());
		
		int newScore = (int) ((existingScore + currentScore)/2);
		
		uf.setRatings(String.valueOf(newScore));
		return uf;
	}
	
	public String registerBike(Bikes bk) {
		// TODO Auto-generated method stub
		return registrationDAO.registerBike(bk);
	}
	

}
