package com.project11.bikeshare.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.project11.bikeshare.Beans.RentDetails;
import com.project11.bikeshare.DBImpl.MyHistoryDAO;

public class MyHistoryService {

	public List<RentDetails>  getMyHistory(String user_id) {
		List<RentDetails> rentDetailsList = new ArrayList<RentDetails>();
		Iterator<RentDetails> it = new MyHistoryDAO().getMyHistory(user_id).iterator();
		while(it.hasNext()){
			RentDetails rent = it.next();
			System.out.println("Bike Id:"+rent.getBike_id()+" User id:"+rent.getUser_id_owner());
			rentDetailsList.add(rent);
		}
		return rentDetailsList;
		
	}

}
