package com.project11.bikeshare.Service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.project11.bikeshare.Beans.BikeContext;


import org.neo4j.cypher.internal.compiler.v2_1.ast.rewriters.isolateAggregation;

import com.project11.bikeshare.Beans.Bikes;
import com.project11.bikeshare.Beans.Coordinates;
import com.project11.bikeshare.DBImpl.BikesDAO;

public class BikesService {
	final BikesDAO bikesDAO = new BikesDAO();
	public List<Bikes> getLocations(Coordinates coordinates) throws UnknownHostException
	{
		List<Bikes> list=new BikesDAO().getLocations(coordinates);
		//System.out.println(list.size());
		List<Bikes> newList=new ArrayList<Bikes>();
		for(int i=0;i<list.size();i++)
		{
			Bikes b=list.get(i);
			//System.out.println(b.getIsBikeAvailable());
			if(b.getIsBikeAvailable().equals("yes"))
			{
				newList.add(b);
			}
		}
		return newList;
	}
	
public List<Bikes> getAllBikes(String user_id) throws UnknownHostException{
		
		return new BikesDAO().getAllBikes(user_id);
	}
public BikeContext getBikeDetails(String bike_id) throws UnknownHostException{
	
	return new BikesDAO().getBikeDetails(bike_id);
}	

public void lendBike(BikeContext bikecontext) throws UnknownHostException {
	
	bikesDAO.lendBike(bikecontext);
}
public void gotMyBike(String bikeid) throws UnknownHostException {

	bikesDAO.gotMyBike(bikeid);
}
public void bikeRevoke(String bikeid) throws UnknownHostException {
	
	bikesDAO.bikeRevoke(bikeid);
	
}
}
