package com.cloudtask1;

import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ScheduledThreadPoolExecutor;

 

import com.cloudtask1.client.MyRequestFactory;
import com.cloudtask1.shared.CloudTask1Request;
import com.cloudtask1.shared.TaskProxy;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.web.bindery.requestfactory.shared.Receiver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

/**
 * This application uses the Google API to display a map of San Francisco. This
 * API is not part of the core Android API but is Google-specific. The Google
 * API offers its own MapActivity from which the application can be derived.
 * Note that certain permissions need to be set in AndroidManifest.xml to make
 * this application work. Furthermore, it is required that every application
 * using the Google Maps API has to register with Google to obtain a Map API
 * Key. The key needs to be included in the android:apiKey attribute of the
 * layout resource referencing the map. See the following link for details:
 * http://code.google.com/android/add-ons/google-apis/mapkey.html
 */


    
    public class MapsActivity extends com.google.android.maps.MapActivity {
        private static final String TAG = null;
		GeoPoint point;
        MapView mapView;
        MapController mc;
        
        String message;
        String myPoints;
        StringTokenizer tokens;
        
        Integer p1 = null;
        Integer p2;
        
        private UpdateTask updateTask;//used to execute a thread to fetch server data
       // private CountdownTask countdownTask;
        
        class MapOverlay extends com.google.android.maps.Overlay
        {
            @Override
            public boolean draw(Canvas canvas, MapView mapView, 
            boolean shadow, long when) 
            {
                super.draw(canvas, mapView, shadow);                   
     
                //---translate the GeoPoint to screen pixels---
                Point screenPts = new Point();
                mapView.getProjection().toPixels(point, screenPts);
     
                //---add the marker---
                Bitmap bmp = BitmapFactory.decodeResource(
                    getResources(), R.drawable.pushpin);            
                canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50, null);         
                return true;
            }
        } 
        
           
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.main);
            updateTask = new UpdateTask(getBaseContext());
           // while (true){
	            mapView = (MapView) findViewById(R.id.mapview);
	            mapView.setBuiltInZoomControls(true);
	
	            /**
	             * To show a map of San Francisco, we need to create a geo point object
	             * with longitude and latitude in center of SF.
	             */
	            //point = new GeoPoint(40752000, -73995000);
	
	            /**
	             * MapController is needed to set view location and zooming.
	             */
	            
	            
	            
	            point = new GeoPoint(40752000, -73995000);
	            
	            
	            mc = mapView.getController();
	            mc.setCenter(point);
	            mc.setZoom(14);
	            
	          //---Add a location marker---
	            MapOverlay mapOverlay = new MapOverlay();
	            List<Overlay> listOfOverlays = mapView.getOverlays();
	            listOfOverlays.clear();
	            listOfOverlays.add(mapOverlay);
	            
	           // String [] l ={"hello!!!!","Made it!"};
	             
	            myPoints = "3993939,39399393939";
	            //http://mobiforge.com/developing/story/using-google-maps-android
	            //display a label with the point's coordinates
	           final Toast myToast = Toast.makeText(getBaseContext(),
	        		   "Value of points string is:  "+myPoints.toString(), Toast.LENGTH_LONG);
	           
	           
	          /* myToast = Toast.makeText(getBaseContext(), 
	                    point.getLatitudeE6() / 1E6 + "," + 
	                    point.getLongitudeE6() /1E6 , 
	                    Toast.LENGTH_LONG);
	     	   
	            */
	           
	            myToast.show();
	            
	           //increase the display time 
	           new CountDownTimer(9000, 1000)
	            {
	                public void onTick(long millisUntilFinished) {myToast.show();}
	                public void onFinish() {myToast.show();}
	
	            }.start();
	            
	            mapView.invalidate();
	            
	            
	            
	           
	         //  updateTask.execute();
	          // myPoints = updateTask.serverPoints;
	            
	            myPoints = "Mike,37798272,-122394605"; 
	           
	           Log.i(TAG, "MyPoints are:  "+myPoints.toString());
	             tokens = new StringTokenizer(myPoints,",");
		          
		         /* if(p1 == null)
		          {
		          	Intent listener = getIntent();
		          	myPoints = listener.getStringExtra("message");
		           
		          
		          }
		          */
		         
		          tokens.nextToken();
		          p1 = Integer.parseInt(tokens.nextToken());
		          p2 = Integer.parseInt(tokens.nextToken());
		          point = new GeoPoint(p1,p2);
		            
           // }
        }

        @Override
        protected boolean isRouteDisplayed() {
            return false;
        }

    }
 
// 
// Intent listener = getIntent();
//        
//        changedLocation = false;
//        //get message from cloudtask activity
//		myPoints = listener.getStringExtra("message");
//        message=myPoints;
//        mapView = (MapView) findViewById(R.id.mapview);
//        mapView.setBuiltInZoomControls(true);
//
//        /**
//         * To show a map of San Francisco, we need to create a geo point object
//         * with longitude and latitude in center of SF.
//         */
//        
//        
//         tokens = new StringTokenizer(myPoints,",");
//        
//        if(tokens != null)
//        {
//        	
//        	changedLocation = true;
//        }
//        tokens.nextToken();
//        p1 = Integer.parseInt(tokens.nextToken());
//        p2 = Integer.parseInt(tokens.nextToken());
//        point = new GeoPoint(p1,p2);
//        
//        
//      
//        //point = new GeoPoint(40752000, -73995000);
//        //point = new GeoPoint(37792625,-12239348);
//
//        
//        
//        
//        
//        int i = 0;
//       /* while(i < 5)
//        {
//        */	
//        	//mapView.invalidate();    	
//        	
//        	
//            /**
//             * MapController is needed to set view location and zooming.
//             */
//	         mc = mapView.getController();
//	        mc.setCenter(point);
//	        mc.setZoom(90);
//	        
//	        //---Add a location marker---
//	        MapOverlay mapOverlay = new MapOverlay();
//	        List<Overlay> listOfOverlays = mapView.getOverlays();
//	        listOfOverlays.clear();
//	        
//	        OverlayItem item = new OverlayItem(point, "User1 at location:  ", point.toString());
//	        
//	       
//	        Drawable pin = this.getResources().getDrawable(R.drawable.pushpin);
//	        
//	        //MapOverlay itemzidedOverlay = new MapOverlay(pin,mapView);
//	        
//	       //MapOverlay temp =  new MapOverlay(pin,mapView);
//	        //temp.add(item);
//	        
//	        
//	        listOfOverlays.add(mapOverlay);
//	        
//	        
//
//	        //http://mobiforge.com/developing/story/using-google-maps-android
//	        //display a label with the point's coordinates
//	       final Toast myToast = Toast.makeText(getBaseContext(),
//	    		   myPoints,Toast.LENGTH_LONG);
//	    		   //(p1.toString()+","+p2.toString()), Toast.LENGTH_LONG);     
//	       
//	       
//	        myToast.show();
//	        
//	        //increase the display time 
//	        new CountDownTimer(9000, 1000)
//	        {
//	            public void onTick(long millisUntilFinished) {myToast.show();}
//	            public void onFinish() {myToast.show();}
//	
//	        }.start();
//        
//	        //mapView.invalidate();
//	        
//	        i++;
//	        
//	       
//	        //////////////////////////////////////////////////////////////////
//	        //See if point has changed
//	        
//	      //ScheduledThreadPoolExecutor
//	       
//	        
//	        new AsyncTask<Void, Void, String>() {
//                private String message;
//
//                                    
//                protected String doInBackground(Void... arg0) {
//		        MyRequestFactory requestFactory1 = Util.getRequestFactory(mContext,
//	                    MyRequestFactory.class);
//	            final CloudTask1Request request2 = requestFactory1.cloudTask1Request();
//	            Log.i(TAG, "Sending request to server");
//	            request2.queryTasks().fire(new Receiver<List<TaskProxy>>() {
//	                private boolean shadow;
//	
//					@Override
//					public void onSuccess(List<TaskProxy> taskList) {
//	                	 message = "";
//	                	 
//	                	 
//	                	 message="";
//	                	 myPoints="";
//	                	 //get the Geo point for each user id
//	  	    			for (TaskProxy task : taskList) {
//	  	    				   	    				 
//	  	    				message += task.getId()+","+task.getNote()+",";
//	  	    				myPoints += task.getId()+"("+task.getNote()+")\n"; 
//	  	    			}
//	  	    			
//	  	    			
//	  	    			
//	  	    			
//	  	    			tokens = new StringTokenizer(message,",");
//	  	    			
//	  	    			//get the id of the current user being tracked
//	  	    			//we do nothing with it for now
//	  	    			tokens.nextToken();
//	  	    			
//	  	    			//see if the new Geo point obtained is different from the previously 
//	  	    			//plotted point
//	  	    			Integer p3 = Integer.parseInt(tokens.nextToken());
//	  	    			
//	  	    			if (p1 == p3)
//	  	    			{
//	  	    				changedLocation = false;
//	  	    			}
//	  	    			else
//	  	    			{
//	  	    				p1 = p3;
//	  	    				
//	  	    				p2 = Integer.parseInt(tokens.nextToken());
//	  	    				
//	  	    				point = new GeoPoint(p1,p2);
//	  	    				
//	  	    				//display the new point to the user....
//	  	    				final Toast myToast1 = Toast.makeText(getBaseContext(),
//	  	    		    		   "Here is the new point: "+point.toString(),Toast.LENGTH_LONG);
//	  	    				
//	  	    				myToast1.show();
//	  	    			}
//					}
//	                
//	            });
//	            
//            
//            MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this, mapView);
//            mapView.getOverlays().add(myLocationOverlay);
//            myLocationOverlay.enableMyLocation();
//	        
//            listOfOverlays.clear();
//	        listOfOverlays.add(myLocationOverlay);
//	        
//	        
//       // }////while loop bracket//////
// 
// 
// 
