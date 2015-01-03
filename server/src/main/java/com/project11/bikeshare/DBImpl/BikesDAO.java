package com.project11.bikeshare.DBImpl;

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
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.project11.bikeshare.Beans.BikeContext;
import com.project11.bikeshare.Beans.Bikes;
import com.project11.bikeshare.Beans.Coordinates;
import com.project11.bikeshare.Beans.Location;
import com.project11.bikeshare.Beans.RentDetails;

public class BikesDAO extends BikeShareDB{

	private Class<Object> Bikes = null;

	public List<Bikes> getLocations(Coordinates coordinates) throws UnknownHostException
	{
		MongoClient client = new MongoClient(new ServerAddress("ds051160.mongolab.com",51160));
		DB database = client.getDB("bikeshare");
	    database.authenticate("bikeshare","bikeshare".toCharArray());
	    DBCollection collection = database.getCollection("bikes");
	    BasicDBList myLocation = new BasicDBList();
		myLocation.put(1, Double.parseDouble(coordinates.getLatitude()));
		myLocation.put(0, Double.parseDouble(coordinates.getLongitude()));
		ArrayList<Bikes> list=new ArrayList<Bikes>();
		DBCursor cursor= collection.find(
	            new BasicDBObject("location",
	                new BasicDBObject("$near",
	                        new BasicDBObject("$geometry",
	                                new BasicDBObject("type", "Point")
	                                    .append("coordinates", myLocation))
	                             .append("$maxDistance",  2000)
	                        )
	                )
	            );
		while(cursor.hasNext())
		{
			//System.out.println("in while");
			DBObject obj=cursor.next();
			Bikes b=new Bikes();
			b.setUser_id(String.valueOf(obj.get("user_id")));
			b.setBike_id(String.valueOf(obj.get("bike_id")));
			b.setAccessCode(String.valueOf(obj.get("accessCode")));
			Location loc=new Location();
			BasicDBObject location=(BasicDBObject) obj.get("location");
			BasicDBList coord1=(BasicDBList) location.get("coordinates");
			Coordinates coord=new Coordinates();
			coord.setLatitude(String.valueOf(coord1.get(1)));
			coord.setLongitude(String.valueOf(coord1.get(0)));
			loc.setCoordinates(coord);
			b.setLocation(loc);
			b.setIsBikeAvailable((String) obj.get("isBikeAvailable"));
			b.setPincode(String.valueOf(obj.get("pincode")));
			b.setBikeModel(String.valueOf(obj.get("bikeModel")));
			b.setStart_time(String.valueOf(obj.get("start_time")));
			b.setEnd_time(String.valueOf(obj.get("end_time")));
	    	list.add(b);
		}
		
		return list;


	}
	
public List<Bikes> getAllBikes(String uid) throws UnknownHostException
	{
		Bikes b=null;
		MongoClientURI uri  = new MongoClientURI("mongodb://bikeshare:bikeshare@ds051160.mongolab.com:51160/bikeshare"); 
		MongoClient client = new MongoClient(uri);
		DB db = client.getDB("bikeshare");
		ArrayList<Bikes> list= new ArrayList<Bikes>();
		DBCollection coll = db.getCollection("bikes");
		BasicDBObject query = new BasicDBObject("user_id",uid);
		DBCursor cursor = coll.find(query);
		try {
			   while(cursor.hasNext() ) {
			   	   b=new Bikes();
				   DBObject obj = cursor.next();
				   b.setBike_id(String.valueOf(obj.get("bike_id")));
				   b.setIsBikeAvailable(String.valueOf(obj.get("isBikeAvailable")));
				   b.setPincode(String.valueOf(obj.get("pincode")));
				   b.setBikeModel(String.valueOf(obj.get("bikeModel")));
				   list.add(b);
			   }
			} finally {
			   cursor.close();
			}
			return list;
	}
	public BikeContext getBikeDetails(String bid) throws UnknownHostException
	{
		Bikes b=new Bikes();
		RentDetails rd = new RentDetails();
		BikeContext bc = new BikeContext();
		MongoClientURI uri  = new MongoClientURI("mongodb://bikeshare:bikeshare@ds051160.mongolab.com:51160/bikeshare"); 
		MongoClient client = new MongoClient(uri);
		DB db = client.getDB("bikeshare");
		DBCollection coll = db.getCollection("bikes");
		BasicDBObject query = new BasicDBObject("bike_id",bid);
		DBCursor cursor = coll.find(query);
			   while(cursor.hasNext() ) {
				   DBObject obj = cursor.next();
				   b.setUser_id((String.valueOf(obj.get("user_id"))));
				   b.setIsBikeAvailable(String.valueOf(obj.get("isBikeAvailable")));
				   b.setPincode(String.valueOf(obj.get("pincode")));
				   b.setBikeModel(String.valueOf(obj.get("bikeModel")));
				   b.setAccessCode((String.valueOf(obj.get("accessCode"))));
				   bc.setBike(b);
				   DBCollection coll1 = db.getCollection("rent_details");
				   DBObject sort=new BasicDBObject();
				   sort.put("rent_details_id",-1);
				   
				   BasicDBObject query1 = new BasicDBObject("bike_id",bid);
				   DBCursor cursor1 = coll1.find(query1);
				      while(cursor1.hasNext() ) {	
						   DBObject obj1 = cursor1.next();
						   rd.setReceived(String.valueOf(obj1.get("received")));
						   rd.setEnd_time(String.valueOf(obj1.get("end_time")));
						   rd.setStart_time(String.valueOf(obj1.get("start_time")));
						   bc.setRentdetails(rd);
					   }
			   	}
				   	return bc;
	}	

	public void lendBike(BikeContext bikecontext) throws UnknownHostException {
		Bikes b = bikecontext.getBike();
		RentDetails rd = bikecontext.getRentdetails();
		System.out.println(bikecontext.getBike().getBike_id());
		MongoClient client = new MongoClient(new ServerAddress("ds051160.mongolab.com",51160));
		DB db2 = client.getDB("bikeshare");
		client.setWriteConcern(WriteConcern.JOURNALED);
	    db2.authenticate("bikeshare","bikeshare".toCharArray());
		DBCollection coll2 = db2.getCollection("bikes");
		BasicDBObject query2 = new BasicDBObject("bike_id",bikecontext.getBike().getBike_id());
		DBCursor cursor2 = coll2.find(query2);
		try {
			   while(cursor2.hasNext() ) {
				   DBObject obj2 = cursor2.next();
					BasicDBList coordinates = new BasicDBList();
					obj2.put("isBikeAvailable","yes");
					obj2.put("start_time",bikecontext.getRentdetails().getStart_time());
					obj2.put("end_time",bikecontext.getRentdetails().getEnd_time());
					obj2.put("accessCode",b.getAccessCode());
					obj2.put("pincode", b.getPincode());
					coordinates.put(0, Double.parseDouble(b.getLocation().getCoordinates().getLongitude()));
					coordinates.put(1, Double.parseDouble(b.getLocation().getCoordinates().getLatitude()));
					obj2.put("location",  new BasicDBObject("type", "Point").append("coordinates", coordinates));	
					System.out.println("entered finals");
				    coll2.save(obj2);
			   }
			} finally {
			   cursor2.close();
			}
		DBCollection rdCollection = db2.getCollection("rent_details");
			System.out.println("entered RENT DETAILS");
			BasicDBObject obj = new BasicDBObject().append("received", "nyr")
													.append("bike_id", bikecontext.getBike().getBike_id());
			DBCursor cursor = rdCollection.find(obj);
			System.out.println(cursor.size());
			if(cursor.size()==0){
			
			
				  BasicDBObject obj5 = new BasicDBObject().append("received", "nyr")
						   									.append("bike_id",b.getBike_id())
						   									.append("start_time", bikecontext.getRentdetails().getStart_time())
						   									.append("user_id_owner", bikecontext.getBike().getUser_id())
						   									.append("end_time", bikecontext.getRentdetails().getEnd_time())
				   											.append("rent_details_id", new Date().getTime());
				rdCollection.save(obj5);
			}
			else
			{
				DBObject obj3 = cursor.next();
				obj3.put("start_time", b.getStart_time());
				obj3.put("end_time", b.getEnd_time());
				obj3.put("received","nyr");
				rdCollection.save(obj3);
				
				
				
			}
				   
	}
	
	public void gotMyBike(String bikeid) throws UnknownHostException {
		MongoClient client = new MongoClient(new ServerAddress("ds051160.mongolab.com",51160));
		DB db3 = client.getDB("bikeshare");
		client.setWriteConcern(WriteConcern.JOURNALED);
	    db3.authenticate("bikeshare","bikeshare".toCharArray());
		DBCollection coll3 = db3.getCollection("bikes");
		BasicDBObject query3 = new BasicDBObject("bike_id",bikeid);
		DBCursor cursor3 = coll3.find(query3);
		try {
			   while(cursor3.hasNext() ) {
				   DBObject obj3 = cursor3.next();
					obj3.put("isBikeAvailable","no");
				    coll3.save(obj3);
				    DBCollection coll4 = db3.getCollection("rent_details");
					BasicDBObject query4 = new BasicDBObject("bike_id",bikeid);
					DBObject sort=new BasicDBObject();
					   sort.put("rent_details_id",-1);
					DBCursor cursor4 = coll4.find(query4).sort(sort).limit(1);
					System.out.println(cursor4.size());
					
				    try {
						   while(cursor4.hasNext() ) {
							   DBObject obj4 = cursor4.next();
							   System.out.println("~~"+ obj4);
							   obj4.put("received", "yes");
							   coll4.save(obj4);
						   }
						} finally {
						   cursor4.close();
						}				    
			   }
			} finally {
			   cursor3.close();
			}
	}
	public void bikeRevoke(String bikeid) throws UnknownHostException {
		MongoClient client = new MongoClient(new ServerAddress("ds051160.mongolab.com",51160));
		DB db4 = client.getDB("bikeshare");
		client.setWriteConcern(WriteConcern.JOURNALED);
	    db4.authenticate("bikeshare","bikeshare".toCharArray());
		DBCollection coll4 = db4.getCollection("bikes");
		BasicDBObject query4 = new BasicDBObject("bike_id",bikeid);
		System.out.println(bikeid);
		DBCursor cursor4 = coll4.find(query4);
		try {
			   while(cursor4.hasNext() ) {
				   DBObject obj4 = cursor4.next();
				   System.out.println("00"+obj4);
					obj4.put("isBikeAvailable","no");
				    coll4.save(obj4);			    
			   }
			} finally {
			   cursor4.close();
			}
		
	}
}
