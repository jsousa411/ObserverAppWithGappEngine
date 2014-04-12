/*
 * ObserverNotes.java
 * 
 * SFSU Fall 2013
 * CSC 875 - Term Project
 * Joao Sousa
 * Notes:  This file contains the code for the main user's interface with the 
 * project's data, which is displayed in a listview.
 * 
 * Through this application the user is able to add, delete, update and select
 * a data record.  The data record that is selected gets passed back to the main
 * screen in GuestbookActivity for a possible broadcast.
 * 
 * 12/17/2013
 */


package com.observer.notes;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;



import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.cloud.backend.android.CloudBackendActivity;
import com.google.cloud.backend.android.CloudCallbackHandler;
import com.google.cloud.backend.android.CloudEntity;
import com.google.cloud.backend.android.CloudQuery.Order;
import com.google.cloud.backend.android.CloudQuery.Scope;
import com.google.cloud.backend.android.R;





import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.cloud.backend.android.R;


@SuppressWarnings("unused")
public class ObserverNotes extends CloudBackendActivity {
	private static final String TAG = "CALL_CAMERA_FROM_ObserverNotes";//this was an attempt to get picture taken from Observernotes
																		
	private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;  //also not used
	private static int RESULT_LOAD_IMAGE = 1;//activity state for loading an image
	private static final int SELECT_PICTURE = 1;
	private static final int SELECT_ITEM = 2;
	private String selectedImagePath;
	//Uri fileUri;
		ImageView photoImage = null;
		File file;
		TextView textTargetUri;
		ImageView imgView1;
		
		final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };

	    final String orderBy = MediaStore.Images.Media._ID;
	    String[] ids;
	//the above variables are early design flaw and should not be used...they were created with the thought that
	//a picture would be inserted/viewed through the Listview
	
	
	private static final String ITEM_FOUND = "itemFound";//during insertion of new data from broadcast - indicator
	private static final URI SERVER_URL = null; //the URL to be saved in the database
    
	
	ListView listView;//Listview to be displayed to the user
	Item anItem;	//data model
	
	
	ObserverNotes_Model userNotes;//actual Listview inflator
	
	DatabaseHandler db;//database instance to get data
	Activity mCurrentActivity;//current activity with context
	ItemAdapter adapter;//the controller that gets/put data into the database via Database Handler
	
	
	
	static int recentSelected = -1;//item clicked indicator
	public static ArrayList<Item> Items;//list of items from local database
	ArrayList<String> values = new ArrayList<String>();//list of strings
	
	@Override
    public void onCreate(Bundle savedInstanceState) {		

		 
		super.onCreate(savedInstanceState);
        setContentView(R.layout.observer_main);

        Log.i("ObserverNotes:  ", "ONCREATE");
        //Button buttonLoadImage = (Button)findViewById(R.id.loadimage);
        Button addObservation = (Button)findViewById(R.id.addnotes);//button to allow user to insert a new record
       
        listView = (ListView) findViewById(R.id.list);//listview for user to interact with
        
        userNotes = new ObserverNotes_Model();//listview inflator
        //userNotes.LoadModel(this);
        
        InputStream ims = null;//used to read data from a file.  But is not being used as we are 
        						//getting data from user's input
        Items = new ArrayList<Item>();//list of data objects
        
        try {
        	
            ims = getApplicationContext().getAssets().open("ic_launcher.png");
            
        } catch (IOException e) {
        	
            e.printStackTrace();
            
        }
        
        
        this.Set_Referash_Data();   //display listview latest data
        
        //allow 
        addObservation.setOnClickListener(new View.OnClickListener() {

    	    @Override
    	    public void onClick(View v) {
    	    	
    	    		Intent intent=new Intent(ObserverNotes.this,AddUpdateData.class);
    	    		intent.putExtra("UPDATE", "add");
    	    		startActivity(intent);
    	    	
    	    }
        });  
        
	}//End OnCreate()
	
	
	
    //Once we return from an activity, get the result(s) here
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	   
		super.onActivityResult(requestCode, resultCode, data);

		Log.i("ObserverNotes:  ", "onActivityResult");
		 String result = "";
		    boolean hasextra = false;
		    
		    //try to get result from ItemAdapter: usually after user inserted or updated a record
		    hasextra = data.hasExtra("com.observer.notes");
		    
		    if(hasextra){
		  	  
		    	result = data.getStringArrayExtra("com.observer.notes").toString();
			      if(result != null)
			    	  Toast.makeText(getApplicationContext(),"At Observer notes Received: "+result,Toast.LENGTH_LONG).show();
		    }
		 	
	}

	
	
	//Application life cycle.  Placed here to refresh the Listview after the user inserts/updates the
	//Listview
    @Override
    public void onResume() {
		
		super.onResume();
		//Toast.makeText(getApplicationContext(),"resuming ObserverNotes",Toast.LENGTH_LONG).show();  
		Log.i("ObserverNotes:  ", "onResume");
		
		//call to re-display the listview with the latest data
		Set_Referash_Data();

    }
    
    
    /*
     * This is used to display the latest data into ListView 
     */
    public void Set_Referash_Data() {
	 		
    		//List to be displayed to users
    		//upon app start
	 		Item item1;
	 		
	 		
	 		//If we have data clear it
	 		//this is usually if wea returning
	 		//from another activity
        	if(Items != null){
        		Items.clear();
        	}
        	else{
        		//otherwise on a fresh start
        		//instantiate a new List
        		Items = new ArrayList<Item>();
        	}
        		
        	//get database object
	    	db = new DatabaseHandler(this);//instantiate a database object
	    	
	    	//read all of the database data into the List object to be displayed
	    	ArrayList<Item> item_array_from_db = db.Get_items();//get the latest data
	
	    	//break down the records fetched from database and insert 
	    	//each record into an Item object.
	    
	    	for (int i = 0; i < item_array_from_db.size(); i++) {
	
	    	    int tidno = item_array_from_db.get(i).getId();
	    	    String name = item_array_from_db.get(i).getKeyName();
	    	    String datanotes = item_array_from_db.get(i).getDataNotes();
	    	    String url = item_array_from_db.get(i).getImageUrl();
	    	    if(item_array_from_db.get(i)._deleted != "D" && 
	    	    		!item_array_from_db.get(i).getKeyName().contains("whitepaper")	
	    	    		){//insert item only if it's not marked for deletion
		    	    item1 = new Item();
		    	    item1.setId(tidno);
		    	    item1.setKeyName(name);
		    	    item1.setDataNotes(datanotes);
		    	    item1.setImageUrl(url);
		
		    	    //add each item read from database into the LIST
		    	    Items.add(item1);
	    	    }
	    	}
	    	
	    	
	    	
	    	db.close();
	    	
	    	//insert the list of items into the model 
	    	adapter = new ItemAdapter(ObserverNotes.this, R.layout.row,
	    		Items);
	    	listView.setAdapter(adapter);
	    	adapter.notifyDataSetChanged();
 }


	
	
	public void Show_Toast(String msg) {
    	
    	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    	
    }
        
    
	
	/*
	 * Assigns photo to selected List View Item
	 */
	private void showPhoto(Uri photoUri) {
		  File imageFile = new File(photoUri.toString());  
		  if (imageFile.exists()){
		     Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
		     BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
		     photoImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
		     photoImage.setImageDrawable(drawable);
		  }       
		}

	public Activity getCurrentActivity() {
		
		return mCurrentActivity;
	}
	
	public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
  }
}


