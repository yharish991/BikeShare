package com.project11.bikeshare.DBImpl;

import java.net.UnknownHostException;

import org.jongo.MongoCursor;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.project11.bikeshare.Beans.Bikes;
import com.project11.bikeshare.Beans.BikesBean;
import com.project11.bikeshare.Beans.RentDetails;
import com.project11.bikeshare.Beans.User;
import com.project11.bikeshare.Beans.UserFeedback;

public class BikeConfirmationDAO extends BikeShareDB{

	public BikesBean confirmRent(String bike_id) {
		bikesCollectionJongo.update("{bike_id: '"+bike_id+"'}").with("{$set:{isBikeAvailable: 'no'}}");
		BikesBean bikes= bikesCollectionJongo.findOne("{bike_id: '"+bike_id+"'}").as(BikesBean.class);
	return bikes;
	}

	public User findUser(String user_id) {
		User user = userCollectionJongo.findOne("{user_name: '"+user_id+"'}").as(User.class);
		return user;
	}
	
	public Bikes findBike(String bikeid) throws UnknownHostException {
		MongoClient client = new MongoClient(new ServerAddress("ds051160.mongolab.com",51160));
		DB database = client.getDB("bikeshare");
	    database.authenticate("bikeshare","bikeshare".toCharArray());
	    DBCollection collection = database.getCollection("bikes");
	    BasicDBObject query = new BasicDBObject("bike_id",bikeid);
	    DBCursor cursor = collection.find(query);
	    Bikes b=new Bikes();
	    while(cursor.hasNext())
	    {
	    	DBObject obj=cursor.next();
	    	b.setBike_id(String.valueOf(obj.get("bike_id")));
	    	b.setBikeModel(String.valueOf(obj.get("bikeModel")));
	    	b.setStart_time(String.valueOf(obj.get("start_time")));
	    	b.setEnd_time(String.valueOf(obj.get("end_time")));
	    	b.setAccessCode(String.valueOf(obj.get("accessCode")));
	    	
	    }
	     return b;		

	}

	//when rent is confirmed change the renter id.
	public RentDetails updateRentDetails(String bike_id,String username) {
		/*rentDetailsCollectionJongo.update("{user_id_renter: '"+username+"'}").with("{$set:{bikeid: '"+bike_id+"'},{received:'nyr'}}");*/
		rentDetailsCollectionJongo.update("{bike_id: '"+bike_id+"'},{received:'nyr'}").with("{$set:{user_id_renter: '"+username+"'}}");
		RentDetails rentDetails = rentDetailsCollectionJongo.findOne("{bike_id: '"+bike_id+"'},{received:'nyr'}").as(RentDetails.class);
		rentDetailsCollectionJongo.update("{bike_id: '"+bike_id+"'},{received:'nyr'}").with("{$set:{received:'no'}}");
		return rentDetails;
	}

	public MongoCursor getUsersFeedBackHistory(String user_id) {
		// TODO Auto-generated method stub
		MongoCursor<UserFeedback> all = feedbackCollectionJongo.find("{user_id_renter: '"+user_id+"'}").as(UserFeedback.class);
		return all;
	}
	

}
