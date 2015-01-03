package com.project11.bikeshare.Service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project11.bikeshare.Beans.Bikes;
import com.project11.bikeshare.Beans.BikesList;
import com.project11.bikeshare.DBImpl.OfflineModeDAO;
import com.project11.bikeshare.util.TwilioMessage;
import com.twilio.sdk.TwilioRestException;

public class OfflineModeService {
	static Map<String, String> sessionMap = new HashMap<String, String>();
	OfflineModeDAO offlineModeDAO = new OfflineModeDAO();
	public void offlineMode(String From,String Body) throws TwilioRestException, UnknownHostException
	{
		TwilioMessage twilioMessage = new TwilioMessage();
		
		boolean authUser= Boolean.FALSE;
		String content[];
		String message="";
		List<Bikes> blist=new ArrayList<Bikes>();
		if(Body==null || Body=="")
		{
			//content=Body.split(" ");
			message="Message should not be empty.Please try searching again";
			twilioMessage.sendMessage(From, message);
		}else{
			content = formatBody(Body);
			String user_id = content[0];
			String password = content[1];
			String pincode = content[2];
			String rentKeyword = content[3];
			String bike_id = content[4];
			
			if(sessionMap.isEmpty() || !sessionMap.containsKey(From)){
				//if map is empty of value is not present in session add user to session map
				authUser = authenticateUser(user_id,password);
				if(authUser){sessionMap.put(From,user_id);}
			}if(sessionMap != null && sessionMap.containsKey(From)){
				
				if(pincode!=null && pincode.length() > 0){
					//For the first text message
					String listofBikesString =giveUserListOfBikes(pincode);
					twilioMessage.sendMessage(From, listofBikesString);
				}else if(rentKeyword!=null && rentKeyword.length()>0){
					//User is in session we authenticate him to rent bike
					new BikeConfirmationService().confirmRent(bike_id, sessionMap.get(From));
				}
				
			}else if(!authUser || sessionMap.containsKey(From)){
				twilioMessage.sendMessage(From, "Not Authenticated");
			}
			}
			
		
		
	}
	private String giveUserListOfBikes(String pincode) throws UnknownHostException {
		List<Bikes> blist= new ArrayList<Bikes>();
		blist=new OfflineModeDAO().findByZipCode(pincode);
		String message="";
		for(int i=0;i<blist.size();i++)
		{
			Bikes b=blist.get(i);
			message=message+"\n"+"Bike Id : "+b.getBike_id()+"\n"+
						"Bike Model : "+b.getBikeModel()+"\n"+
						"Start Time : "+b.getStart_time()+"\n"+
						"End Time : "+b.getEnd_time()+"\n";
		}
		return message;
	}

	private boolean authenticateUser(String user_id, String password) {
		return offlineModeDAO.authUser(user_id,password);
	}

	private String[] formatBody(String body) {
		String content[] = new String[5];
		String inputContent[]=body.split(" ");
		if(inputContent.length == 0){
			content[5]="ERROR";
		}else if(inputContent.length == 3){
			// save username password pincode
			content[0]= 		inputContent[0];
			content[1]= 		inputContent[1];
			content[2]= 		inputContent[2];
			}else if(inputContent.length == 2){
				// save keyword rent and bikeid
				content[3]= 		inputContent[0];
				content[4]= 		inputContent[1];
			}
		
		return content;
	}

	
}
