package com.project11.bikeshare.DBImpl;

import java.net.UnknownHostException;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class BikeShareDB {
	final private String mongoURIString = "mongodb://bikeshare:bikeshare@ds051160.mongolab.com:51160/bikeshare";
	final private String bikeShareDBName = "bikeshare";
	private MongoClient mongoClient ;
	protected DB bikeShareDB ;
	protected Jongo jongo;
	protected MongoCollection userCollectionJongo;
	protected MongoCollection bikesCollectionJongo;
	protected MongoCollection rentDetailsCollectionJongo;
	protected MongoCollection feedbackCollectionJongo;
	public BikeShareDB() {
		 try {
			mongoClient = new MongoClient(new MongoClientURI(mongoURIString));
			bikeShareDB = mongoClient.getDB(bikeShareDBName);
			jongo = new Jongo(bikeShareDB);
			userCollectionJongo = jongo.getCollection("user");
			bikesCollectionJongo = jongo.getCollection("bikes");
			rentDetailsCollectionJongo = jongo.getCollection("rent_details");
			feedbackCollectionJongo = jongo.getCollection("feedback");
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	    
	}
	

}
