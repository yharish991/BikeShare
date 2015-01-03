package com.project11.bikeshare.util;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.project11.bikeshare.Beans.RentDetails;
import com.project11.bikeshare.Beans.User;

public class FindUsers {
	
	public List<User> findUsers() throws UnknownHostException
	{
		MongoClient client = new MongoClient(new ServerAddress("ds051160.mongolab.com",51160));
		DB database = client.getDB("bikeshare");
	    database.authenticate("bikeshare","bikeshare".toCharArray());
	    DBCollection collection = database.getCollection("rent_details");
	    Date curr_date=new Date();
	    BasicDBObject query = new BasicDBObject("end_time", new BasicDBObject("$lt",String.valueOf(curr_date.getTime())))
        						.append("end_time", new BasicDBObject("$gt",String.valueOf(curr_date.getTime()-900000)))
        						.append("received","no")
        						.append("isNotificationReceived","no");
	    DBCursor cursor = collection.find(query);
	    ArrayList<User> list=new ArrayList<User>();
	    while(cursor.hasNext())
		{
			//System.out.println("in while");
			DBObject obj=cursor.next();
			RentDetails rent=new RentDetails();
			String renter=String.valueOf(obj.get("user_id_renter"));
			DBCollection collection1 = database.getCollection("user");
			BasicDBObject query1=new BasicDBObject("user_name",renter);
			DBCursor cursor1=collection1.find(query1);
			while(cursor1.hasNext())
			{
				User user=new User();
				DBObject obj1=cursor1.next();
				System.out.println(obj1);
				user.setUsername(String.valueOf(obj1.get("user_name")));
				user.setMobile_number(String.valueOf(obj1.get("mobile_number")));
				obj.put("isNotificationReceived","yes");
				collection.save(obj);
				list.add(user);
				
			}	
		}
	    client.close();
		return list;


	}

}
