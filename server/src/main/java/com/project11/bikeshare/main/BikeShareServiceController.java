package com.project11.bikeshare.main;


import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project11.bikeshare.Beans.BikeContext;
import com.project11.bikeshare.Beans.Bikes;
import com.project11.bikeshare.Beans.BikesList;
import com.project11.bikeshare.Beans.Coordinates;
import com.project11.bikeshare.Beans.RentDetails;
import com.project11.bikeshare.Beans.RentHistory;
import com.project11.bikeshare.Beans.User;
import com.project11.bikeshare.Beans.UserContext;
import com.project11.bikeshare.DBImpl.BikeConfirmationDAO;
import com.project11.bikeshare.DBImpl.MyAccountDAO;
import com.project11.bikeshare.DBImpl.MyHistoryDAO;
import com.project11.bikeshare.Service.BikeConfirmationService;
import com.project11.bikeshare.Service.BikesService;
import com.project11.bikeshare.Service.MyHistoryService;
import com.project11.bikeshare.Service.OfflineModeService;
import com.project11.bikeshare.Service.RegistrationService;
import com.project11.bikeshare.DBImpl.MyAccountDAO;
import com.project11.bikeshare.Beans.UserFeedback;
import com.twilio.sdk.TwilioRestException;

@RestController
public class BikeShareServiceController {
	@Autowired
    IArchiveService archiveService;
	RegistrationService registrationService = new RegistrationService();
	BikesService bikesService = new BikesService();
	@RequestMapping(value="/register_user",method = RequestMethod.POST)
    public String createUsers(@RequestParam String userContext) {
		System.out.println("in post"+ userContext);
		Gson gson = new Gson();
        UserContext uc=gson.fromJson(userContext, UserContext.class);
		return registrationService.registerUser(uc);
    	//return "Successfully registered"; //commented
    }
	
	@RequestMapping(value="/login",method = RequestMethod.GET)
	public String login(@RequestParam String user_id) throws UnknownHostException
	{
		Gson gson = new Gson();
		User u=new RegistrationService().login(user_id);
        GsonBuilder builder = new GsonBuilder();
        Gson gson1 = builder.create();
        String str=gson1.toJson(u);
        return str;
	}
	
	@RequestMapping(value="/locations",method = RequestMethod.GET)
	public String getLocations(@RequestParam String coordinates) throws UnknownHostException
	{
		
		Gson gson = new Gson();
        Coordinates coord=gson.fromJson(coordinates, Coordinates.class);
        List<Bikes> list=new BikesService().getLocations(coord);
        BikesList bl=new BikesList();
        bl.setList(list);
        GsonBuilder builder = new GsonBuilder();
        Gson gson1 = builder.create();
        String str=gson1.toJson(bl);
        System.out.println("---"+str);
		return str;
	}
    @RequestMapping(value="/my_account",method = RequestMethod.GET)
    public String getMyAccount(@RequestParam String user_name) throws UnknownHostException
    {
        Gson gson = new Gson();
        User u = new MyAccountDAO().getUserFromDB(user_name);
        GsonBuilder builder = new GsonBuilder();
        Gson gson1 = builder.create();
        String str=gson1.toJson(u);
        return str;
    }


    @RequestMapping(value="/edit_my_account",method = RequestMethod.POST)
    public String editMyAccount(@RequestParam String user) {
        Gson gson = new Gson();
        User u=gson.fromJson(user, User.class);
        new MyAccountDAO().updateAccount(u);
        return "Successfully updated";
    }

	@RequestMapping(value="/bike",method = RequestMethod.GET)
	 public String findBike(@RequestParam String bikeid) throws UnknownHostException
	{	
			System.out.println("11"+bikeid);
			Bikes bike=new BikeConfirmationDAO().findBike(bikeid);
			GsonBuilder builder = new GsonBuilder();
	        Gson gson = builder.create();
	        String str=gson.toJson(bike);
	        System.out.println(str);
			return str;
	}
    
    @RequestMapping(value="/feedback",method = RequestMethod.POST)
    public String getUserFeedback(@RequestParam String userFeedbackStr, @RequestParam String user_id_renter) {
		System.out.println("in feedback");
		Gson gson = new Gson();
		System.out.println("Feedback string"+userFeedbackStr);
		UserFeedback uf=gson.fromJson(userFeedbackStr, UserFeedback.class);
		registrationService.registerFeedback(uf,user_id_renter);
    	return "Successfully submitted feedback";
    }
    
    @RequestMapping(value="/register_bike",method = RequestMethod.POST)
    public String getRegisterBike(@RequestParam String registerBikeStr) {
		System.out.println("in Register Bike");
		Gson gson = new Gson();
		System.out.println("Feedback string"+registerBikeStr);
		Bikes bk=gson.fromJson(registerBikeStr, Bikes.class);
		return registrationService.registerBike(bk);
    	//return "Successfully submitted feedback";
    }
    
	    //Used for my history
    @RequestMapping(value="/get_my_history",method = RequestMethod.GET)
    public String getMyHistory(@RequestParam String user_id) {
        RentHistory renthistory = new RentHistory();
        List<RentDetails> rentDetailsList = new MyHistoryService().getMyHistory(user_id);
        renthistory.setRent_history(rentDetailsList);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String rentDetailsListJson = gson.toJson(renthistory);
        System.out.println(rentDetailsListJson);
        return rentDetailsListJson;
    }
    
    //used to confirm the rent thus the marker disappears from map
    @RequestMapping(value="/confirm_rent",method = RequestMethod.POST)
    public String confirmRenting(@RequestParam String bike_id,@RequestParam String user_id) {
    	return new BikeConfirmationService().confirmRent(bike_id,user_id);
        
    }
    
    @RequestMapping(value="/twilio_offline",method = RequestMethod.POST)
    public String OfflineMode(@RequestParam String From,@RequestParam String Body) {
    	try {
			new OfflineModeService().offlineMode(From, Body);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TwilioRestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "Successfully updated";
    }
  @RequestMapping(value="/getBike",method = RequestMethod.GET)
	public String getLendBikes(@RequestParam String bikeid) throws UnknownHostException
	{
	  //  BikeContext bc= new BikeContext();
        BikesService bs = new BikesService();
    	BikeContext bb = new BikeContext();
       // bc=bs.getBikeDetails(bikeid);
    	 bb=bs.getBikeDetails(bikeid);
        GsonBuilder builder = new GsonBuilder();
        Gson gson1 = builder.create();
        //String str=gson1.toJson(bc);
        String str=gson1.toJson(bb);
        System.out.println("--"+str);
		return str;
	}
	
	@RequestMapping(value="/allbikes",method = RequestMethod.GET)
	public String getAllBikes(@RequestParam String user_id) throws UnknownHostException
	{
		BikesList b2=new BikesList();
		List<Bikes> l1=new BikesService().getAllBikes(user_id);
		b2.setList(l1);
		GsonBuilder builder = new GsonBuilder();
		Gson gson1 = builder.create();
		String str=gson1.toJson(b2);
		System.out.println(str);
		return str;
	}
	
	
	@RequestMapping(value="/lendBike",method = RequestMethod.POST)
	public String postLendBikes(@RequestParam String bikecontext) throws UnknownHostException
	{
		System.out.println(bikecontext);
		Gson gson = new Gson();
		BikeContext bikecontext2=gson.fromJson(bikecontext, BikeContext.class);
		System.out.println("entered");
		System.out.println(bikecontext2.getBike().getBike_id());
		bikesService.lendBike(bikecontext2);
		return "Lend Successful";
		
	}
	
	@RequestMapping(value="/gotMyBike",method = RequestMethod.POST)
	public String gotMyBike(@RequestParam String bikeid) throws UnknownHostException
	{
		bikesService.gotMyBike(bikeid);
		return "Got My Bike successfull";
		
	}
	
	@RequestMapping(value="/revoke",method = RequestMethod.POST)
	public String bikeRented(@RequestParam String bikeid) throws UnknownHostException
	{
		bikesService.bikeRevoke(bikeid);
		return "bike revoke successfull";
		
	}
	
	
	//---------------------------------Image upload Controller------------------
	
	 	

	    /**
	     * Adds a document to the archive.
	     * 
	     * Url: /archive/upload?file={file}&person={person}&date={date} [POST]
	     * 
	     * @param file A file posted in a multipart request
	     * @param person The name of the uploading person
	     * @param date The date of the document
	     * @return The meta data of the added document
	     */
	    @RequestMapping(value = "/upload", method = RequestMethod.POST)
	    public @ResponseBody DocumentMetadata handleFileUpload(
	            @RequestParam(value="file", required=true) MultipartFile file ,
	            @RequestParam(value="id", required=true) String person,
	            @RequestParam(value="date", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date date) {
	        
	        try {
	            Document document = new Document(file.getBytes(), file.getOriginalFilename(), date, person );
	            getArchiveService().save(document);
	            return document.getMetadata();
	        } catch (RuntimeException e) {
	            throw e;
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }      
	    }
	    
	    /**
	     * Finds document in the archive. Returns a list of document meta data 
	     * which does not include the file data. Use getDocument to get the file.
	     * Returns an empty list if no document was found.
	     * 
	     * Url: /archive/documents?person={person}&date={date} [GET]
	     * 
	     * @param person The name of the uploading person
	     * @param date The date of the document
	     * @return A list of document meta data
	     */
	    @RequestMapping(value = "/documents", method = RequestMethod.GET)
	    public HttpEntity<List<DocumentMetadata>> findDocument(
	            @RequestParam(value="person", required=false) String person,
	            @RequestParam(value="date", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date) {
	        HttpHeaders httpHeaders = new HttpHeaders();
	        return new ResponseEntity<List<DocumentMetadata>>(getArchiveService().findDocuments(person,date), httpHeaders,HttpStatus.OK);
	    }
	    
	    /**
	     * Returns the document file from the archive with the given UUID.
	     * 
	     * Url: /archive/document/{id} [GET]
	     * 
	     * @param id The UUID of a document
	     * @return The document file
	     */
	    @RequestMapping(value = "/document/{id}", method = RequestMethod.GET)
	    public HttpEntity<byte[]> getDocument(@PathVariable String id) {         
	        // send it back to the client
	        HttpHeaders httpHeaders = new HttpHeaders();
	        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
	        return new ResponseEntity<byte[]>(getArchiveService().getDocumentFile(id), httpHeaders, HttpStatus.OK);
	    }

	    public IArchiveService getArchiveService() {
	        return archiveService;
	    }

	    public void setArchiveService(IArchiveService archiveService) {
	        this.archiveService = archiveService;
	    }
	 
	 
}
