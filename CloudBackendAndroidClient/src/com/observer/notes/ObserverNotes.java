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
	private static final String TAG = "CALL_CAMERA_FROM_ObserverNotes";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;
	private static int RESULT_LOAD_IMAGE = 1;//activity state for loading an image
	private static final int SELECT_PICTURE = 1;
	private static final int SELECT_ITEM = 2;
	private static final String ITEM_FOUND = "itemFound";
	private static final URI SERVER_URL = null;
    private String selectedImagePath;
	
	ListView listView;
	Item anItem;
	
	//Uri fileUri;
	ImageView photoImage = null;
	File file;
	TextView textTargetUri;
	ImageView imgView1;
	ObserverNotes_Model userNotes;
	
	DatabaseHandler db;
	Activity mCurrentActivity;
	ItemAdapter adapter;
	
	final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };

    final String orderBy = MediaStore.Images.Media._ID;
    String[] ids;
	
	static int recentSelected = -1;
	public static ArrayList<Item> Items;
	ArrayList<String> values = new ArrayList<String>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {		

		 
		super.onCreate(savedInstanceState);
        setContentView(R.layout.observer_main);

        //Button buttonLoadImage = (Button)findViewById(R.id.loadimage);
        Button addObservation = (Button)findViewById(R.id.addnotes);
       
        listView = (ListView) findViewById(R.id.list);
        
        userNotes = new ObserverNotes_Model();
        userNotes.LoadModel(this);
        
        InputStream ims = null;
        Items = new ArrayList<Item>();
        
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
	
	
	
    
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	   
		super.onActivityResult(requestCode, resultCode, data);

		
		 	
	}

	
	
	/*
	 * 
	 * @see com.google.cloud.backend.android.CloudBackendActivity#onResume()
	 */
    @Override
    public void onResume() {
		
		super.onResume();
		Toast.makeText(getApplicationContext(),"resuming ObserverNotes",Toast.LENGTH_LONG).show();  
		
		Set_Referash_Data();

    }
    
    
    /*
     * This is used to refresh ListView 
     */
    public void Set_Referash_Data() {
	 		
	 		Item item1;
	 		
        	if(Items != null){
        		Items.clear();
        	}
        	else{
        		
        		Items = new ArrayList<Item>();
        	}
        		
	    	db = new DatabaseHandler(this);
	    	ArrayList<Item> item_array_from_db = db.Get_items();
	
	    	for (int i = 0; i < item_array_from_db.size(); i++) {
	
	    	    int tidno = item_array_from_db.get(i).getId();
	    	    String name = item_array_from_db.get(i).getKeyName();
	    	    String datanotes = item_array_from_db.get(i).getDataNotes();
	    	    String url = item_array_from_db.get(i).getImageUrl();
	    	    item1 = new Item();
	    	    item1.setId(tidno);
	    	    item1.setKeyName(name);
	    	    item1.setDataNotes(datanotes);
	    	    item1.setImageUrl(url);
	
	    	    Items.add(item1);
	    	}
	    	db.close();
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


