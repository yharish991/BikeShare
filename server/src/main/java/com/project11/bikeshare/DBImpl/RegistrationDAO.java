package com.project11.bikeshare.DBImpl;

import java.net.UnknownHostException;

import org.jongo.MongoCollection;










import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.project11.bikeshare.Beans.Bikes;
import com.project11.bikeshare.Beans.User;
import com.project11.bikeshare.Beans.UserContext;
import com.project11.bikeshare.Beans.UserFeedback;

public class RegistrationDAO extends BikeShareDB{

	/*
	public void registerUser(UserContext userContext){
		//save this user in mongo
		System.out.println("Inserting user");
		userCollectionJongo.save(userContext.getUser());
		bikesCollectionJongo.save(userContext.getBike());
		
		
	}
	*/
	
	public String registerUser(UserContext userContext){
		//save this user in mongo
		System.out.println("Inserting user");
		
		User userDetails = userCollectionJongo.findOne("{username:'"+ userContext.getUser().getUsername()+"'}").as(User.class);
		
		if (userDetails==null)
		{
			System.out.println("Inside null");
			if(!userContext.getUser().getUsername().equals(""))
			{
				
			  userCollectionJongo.save(userContext.getUser());
			}
			/*
			BasicDBObject doc=new BasicDBObject();
			
			doc.put("username",userContext.getUser().getUser_name());
			doc.put("password",userContext.getUser().getPassword());
			doc.put("email",userContext.getUser().getEmail_id());
			doc.put("mobilenumber",userContext.getUser().getMobile_number());
			doc.put("ssn",userContext.getUser().getSsn());

			userCollectionJongo.save(doc);
			*/
			if(!userContext.getBike().getBike_id().equals(""))
			{
			bikesCollectionJongo.save(userContext.getBike());
			}
			/*
			BasicDBObject bikeDoc=new BasicDBObject();
			bikeDoc.put("user_id",userContext.getBike().getUser_id());
			bikeDoc.put("isbikeavailable",userContext.getBike().getIsBikeAvailable());
			bikeDoc.put("bikeid",userContext.getBike().getBike_id());
			bikeDoc.put("pincode",userContext.getBike().getPincode());
			bikeDoc.put("bike_model",userContext.getBike().getBikeModel());
			bikesCollectionJongo.save(bikeDoc);
			*/
			return "Success";
		}
		else
		{
			System.out.println("Inside nt null");			
			return "Username Exist! Please choose anothe UserName";
		}
		
	}

	public User login(String user) throws UnknownHostException{
		MongoClient client = new MongoClient(new ServerAddress("ds051160.mongolab.com",51160));
		DB database = client.getDB("bikeshare");
	    database.authenticate("bikeshare","bikeshare".toCharArray());
	    DBCollection collection = database.getCollection("user");
	    BasicDBObject query = new BasicDBObject("user_name",user);
	    DBCursor cursor = collection.find(query);
	    User u=new User();
	    while(cursor.hasNext())
	    {
	    	DBObject obj=cursor.next();
	    	u.setUsername(String.valueOf(obj.get("user_name")));
	    	u.setPassword(String.valueOf(obj.get("password")));
	    }
	     return u;		
		
	}

	public void feedback(UserFeedback uf) {
		// TODO Auto-generated method stub
		System.out.println("Inserting feedback");
		 feedbackCollectionJongo.save(uf);
	}

	public UserFeedback getUserFeedback(UserFeedback userFeedback,String user_id_renter) {
		//userFeedback.setUser_id_renter(user_id_renter);
		 userFeedback  = feedbackCollectionJongo.findOne("{user_id_renter:'"+user_id_renter+"'}").as(UserFeedback.class);
		 return userFeedback;
	}
	
	public String registerBike(Bikes bk) {
		// TODO Auto-generated method stub
		Bikes regBike = bikesCollectionJongo.findOne("{bike_id:'"+ bk.getBike_id()+"'}").as(Bikes.class);
		if(regBike==null)
		{
			System.out.println("Inserting Bike");
			bikesCollectionJongo.save(bk);
			return "Success";
		}
		else
		{
			return "Bike id already exists";
		}
		
	}


}
