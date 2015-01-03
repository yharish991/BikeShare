package com.project11.bikeshare.DBImpl;

import org.jongo.MongoCursor;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.project11.bikeshare.Beans.RentDetails;
import com.project11.bikeshare.Beans.User;

public class MyHistoryDAO extends BikeShareDB {

	public MongoCursor<RentDetails>  getMyHistory(String user_id) {
		MongoCursor<RentDetails> rentDetailsList = rentDetailsCollectionJongo
				.find("{ $or: [ { user_id_owner: '"+user_id+"' },{ user_id_renter: '"+user_id+"' } ] } ")
				.as(RentDetails.class);
		return rentDetailsList;
	}

}
