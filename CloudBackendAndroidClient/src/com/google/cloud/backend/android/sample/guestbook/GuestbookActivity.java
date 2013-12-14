/*
 * Copyright (c) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.backend.android.sample.guestbook;

//import android.accounts.AccountManager;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.os.Bundle;
//import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
//import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.cloud.backend.android.CloudBackendActivity;
import com.google.cloud.backend.android.CloudCallbackHandler;
import com.google.cloud.backend.android.CloudEntity;
import com.google.cloud.backend.android.CloudQuery.Order;
import com.google.cloud.backend.android.CloudQuery.Scope;
import com.google.cloud.backend.android.MyHandler;
import com.google.cloud.backend.android.R;
//import com.observer.notes.ObserverNotes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Sample Guestbook app with Mobile Backend Starter.
 *
 */
public class GuestbookActivity extends CloudBackendActivity {

  // data formatter for formatting createdAt property
  private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss ", Locale.US);

  private static final String BROADCAST_PROP_DURATION = "duration";

  private static final String BROADCAST_PROP_MESSAGE = "message";

  private static final int SELECT_ITEM = 2;
  
  // UI components
  private TextView tvPosts;
  private EditText etMessage;
  private Button btSend;
  private Button btNotes;
  private Button btnImgs;
  
  
  
  // a list of posts on the UI
  List<CloudEntity> posts = new LinkedList<CloudEntity>();
  
  
  static HandlerThread  mainThread = new HandlerThread("main thread");

  // initialize UI
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    mainThread.start();
    Looper mLooper = mainThread.getLooper(); 
    MyHandler mHandler = new MyHandler(mLooper); 

    //Toast.makeText(getApplicationContext(),"INSIDE Guestbook CREATE",Toast.LENGTH_LONG).show();  
    
    tvPosts = (TextView) findViewById(R.id.tvPosts);
    etMessage = (EditText) findViewById(R.id.etMessage);
    btSend = (Button) findViewById(R.id.btSend);
    //btnImgs = (Button) findViewById(R.id.btImg);
    
    Log.i("GuestBookActivity:  ", "onCreate");
   
  }
  
  @Override
  public void onResume(){
	  super.onResume();
	  //Toast.makeText(getApplicationContext(),"resuming GuestBook",Toast.LENGTH_LONG).show();  
	  Log.i("GuestBookActivity:  ", "onResume");    
	  
  }
  
  @Override  
  public boolean onCreateOptionsMenu(Menu menu) {  
      // Inflate the menu; this adds items to the action bar if it is present.  
      getMenuInflater().inflate(R.menu.activity_main, menu);//Menu Resource, Menu  
       
      return true; 
      
  }  
    
  //This is a menu bar listener...if user selects anything in the menu bar this method
  //gets called
  //Look under res/menu/ for activity_main file to input a menu option
  @Override  
  public boolean onOptionsItemSelected(MenuItem item) {  
      switch (item.getItemId()) {  
          case R.id.takeNotes:  
        	  Toast.makeText(getApplicationContext(),"Make An Observation",Toast.LENGTH_LONG).show();  
                     	  
	          Intent callObserver = new Intent(getBaseContext(), com.observer.notes.ObserverNotes.class);
	          
	          //startActivity(callObserver);        	
	          this.startActivityForResult(callObserver, SELECT_ITEM);
        	  	          
	          return true; 
//          case R.id.takePicture:
//        	  
//        	  Toast.makeText(getApplicationContext(),"Preparing camera for a picture",Toast.LENGTH_LONG).show();  
//              
//        	  
//	          Intent callImageTaker = new Intent(getBaseContext(), com.observer.uploadimages.ImageUpload.class);
//	          
//	          startActivity(callImageTaker);
//        	  
//        	  return true;
          default:  
              return super.onOptionsItemSelected(item);  
      }
  }

  @Override
  protected void onPostCreate() {
    super.onPostCreate();
    
    /**********THIS IS THE LINE THAT DISPLAYS ALL THE BROADCASTS
     * 
     */
    listAllPosts();
  }
  
  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    
    	Log.i("GuestBookActivity:  ", "onActivityResult");
	    String result = "";
	    boolean hasextra = false;
	    
	    //try to get result from ItemAdapter
	    //hasextra = data.hasExtra("com.observer.notes");
	    
	    if(requestCode == SELECT_ITEM){
	  	  
	    	
		      if(resultCode == SELECT_ITEM){
		    	  result = data.getStringExtra("com.observer.notes");
		    			  //data.getStringArrayExtra("com.observer.notes").toString();
		    	  Toast.makeText(getApplicationContext(),"Received: "+result,Toast.LENGTH_LONG).show();
		    	  etMessage.setText(result);
		      }
	    }
  
	   
    }


  // execute query "SELECT * FROM Guestbook ORDER BY _createdAt DESC LIMIT 50"
  // this query will be re-executed when matching entity is updated
  private void listAllPosts() {

    // create a response handler that will receive the query result or an error
    CloudCallbackHandler<List<CloudEntity>> handler = new CloudCallbackHandler<List<CloudEntity>>() {
      @Override
      public void onComplete(List<CloudEntity> results) {
        posts = results;
        updateGuestbookUI();
      }

      @Override
      public void onError(IOException exception) {
        handleEndpointException(exception);
      }
    };

    // execute the query with the handler
    
      getCloudBackend().listByKind("Guestbook", CloudEntity.PROP_CREATED_AT, Order.DESC, 10,
        Scope.FUTURE_AND_PAST, handler);
     
    /*getCloudBackend().listByKind("observerNotes", CloudEntity.PROP_CREATED_AT, Order.DESC, 10,
        Scope.FUTURE_AND_PAST, handler);
        */
  }

  private void handleEndpointException(IOException e) {
    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
    btSend.setEnabled(true);
  }

  // convert posts into string and update UI
  private void updateGuestbookUI() {
    final StringBuilder sb = new StringBuilder();
    for (CloudEntity post : posts) {
      sb.append(sdf.format(post.getCreatedAt()) + getCreatorName(post) + ": " + post.get("message")
         + "\n");
    }
    tvPosts.setText(sb.toString());
  }

  // removing the domain name part from email address
  private String getCreatorName(CloudEntity b) {
    if (b.getCreatedBy() != null) {
      return " " + b.getCreatedBy().replaceFirst("@.*", "");
    } else {
      return "<anonymous>";
    }
  }

  // post a new message to server
  public void onSendButtonPressed(View view) {

//    // create a CloudEntity with the new post
//    CloudEntity newPost = new CloudEntity("Guestbook");
//    //CloudEntity newPost = new CloudEntity("observerNotes");
//    
//    String parts = etMessage.getText().toString();//.split(",");
//    newPost.put("message", parts);
//    //newPost.setUrl(parts[0]);
//    //newPost.setDetail(parts[2]);
	  
	  
	// create a CloudEntity with the new post
	    CloudEntity newPost = new CloudEntity("Guestbook");
	    newPost.put("message", etMessage.getText().toString());


    // create a response handler that will receive the result or an error
    CloudCallbackHandler<CloudEntity> handler = new CloudCallbackHandler<CloudEntity>() {
      @Override
      public void onComplete(final CloudEntity result) {
        posts.add(0, result);
        updateGuestbookUI();
        etMessage.setText("");
        btSend.setEnabled(true);
      }

      @Override
      public void onError(final IOException exception) {
        handleEndpointException(exception);
      }
    };

     
    // execute the insertion with the handler
    getCloudBackend().insert(newPost, handler);
    btSend.setEnabled(false);
  }

  // handles broadcast message and show a toast
  @Override
  public void onBroadcastMessageReceived(List<CloudEntity> l) {
    for (CloudEntity e : l) {
      String message = (String) e.get(BROADCAST_PROP_MESSAGE);
      int duration = Integer.parseInt((String) e.get(BROADCAST_PROP_DURATION));
      Toast.makeText(this, message, duration).show();
    }
  }
}
