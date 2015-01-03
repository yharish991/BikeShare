package com.project11.bikeshare.DBImpl;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.project11.bikeshare.Beans.Bikes;
import com.project11.bikeshare.Beans.User;

public class OfflineModeDAO extends BikeShareDB{
	
	public List<Bikes> findByZipCode(String zipcode) throws UnknownHostException
	{
		MongoClient client = new MongoClient(new ServerAddress("ds051160.mongolab.com",51160));
		DB database = client.getDB("bikeshare");
	    database.authenticate("bikeshare","bikeshare".toCharArray());
	    DBCollection collection = database.getCollection("bikes");
	    BasicDBObject query=new BasicDBObject("pincode",zipcode).append("isBikeAvailable","yes");
	    DBCursor cursor = collection.find(query);
	    List<Bikes> list=new ArrayList<Bikes>();
	    while(cursor.hasNext())
		{
			DBObject obj=cursor.next();
			Bikes b=new Bikes();
			b.setUser_id(String.valueOf(obj.get("user_id")));
			b.setBike_id(String.valueOf(obj.get("bike_id")));
			b.setAccessCode(String.valueOf(obj.get("accesscode")));
			b.setIsBikeAvailable((String) obj.get("isBikeAvailable"));
			b.setPincode(String.valueOf(obj.get("pincode")));
			b.setBikeModel(String.valueOf(obj.get("bikeModel")));
			b.setStart_time(String.valueOf(obj.get("start_time")));
			b.setEnd_time(String.valueOf(obj.get("end_time")));
	    	list.add(b);
		}
	    return list;
	}
	
	public boolean checkUsername(String username) throws UnknownHostException
	{
		boolean isValid=false;
		MongoClient client = new MongoClient(new ServerAddress("ds051160.mongolab.com",51160));
		DB database = client.getDB("bikeshare");
	    database.authenticate("bikeshare","bikeshare".toCharArray());
	    DBCollection collection = database.getCollection("user");
	    BasicDBObject query=new BasicDBObject("user_name",username);
	    DBCursor cursor = collection.find(query);
	    if(cursor.size()==1)
	    {
	    	isValid=true;
	    }
	    else
	    	isValid=false;
	    
	    return isValid;
		
	}

	public User getUser(String user_id) {
		User u =userCollectionJongo.findOne("{user_name:'"+user_id+"'}").as(User.class);
		return u;
	}
	
	public boolean authUser(String user_id,String password) {
		User u =userCollectionJongo.findOne("{user_name:'"+user_id+"'},{password:'"+password+"'}").as(User.class);
		if(u!=null && u.getPassword().equals(password) && (u.getUsername().equals(user_id))){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

}
