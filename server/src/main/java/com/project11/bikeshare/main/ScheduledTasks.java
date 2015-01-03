package com.project11.bikeshare.main;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.project11.bikeshare.Beans.User;
import com.project11.bikeshare.util.FindUsers;
import com.project11.bikeshare.util.TwilioMessage;
import com.twilio.sdk.TwilioRestException;



@EnableScheduling
public class ScheduledTasks {


    @Scheduled(fixedRate = 300000)
    public void NotifyUser() throws TwilioRestException, UnknownHostException{
        //System.out.println("The time is now " + dateFormat.format(new Date()));
        List<com.project11.bikeshare.Beans.User> list=new FindUsers().findUsers();
        if(list==null || list.size()==0)
        {
        	System.out.println("no records");
        	//System.out.println(new Date().getTime());
        }
        else
        {
        	for(int i=0;i<list.size();i++)
        	{
        		User user=list.get(i);
        		String message="Hello "+user.getUsername()+", Your rental time is about to end in less than 15 minutes.Kindly return the bike to the owner.Hope you enjoyed our service.Thanks :)";
        		new TwilioMessage().sendMessage(user.getMobile_number(),message);
        		
        	}
        }
        
    	
    }
}
