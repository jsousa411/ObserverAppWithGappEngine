package com.cloudtask1;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
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
    GeoPoint point;
    MapView mapView;
    MapController mc;
    
    String myPoints;
    
    
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

        Intent listener = getIntent();
        
       
        //get message from cloudtask activity
		myPoints = listener.getStringExtra("message");
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);

        /**
         * To show a map of San Francisco, we need to create a geo point object
         * with longitude and latitude in center of SF.
         */
        point = new GeoPoint(40752000, -73995000);

        /**
         * MapController is needed to set view location and zooming.
         */
        mc = mapView.getController();
        mc.setCenter(point);
        mc.setZoom(14);
        
      //---Add a location marker---
        MapOverlay mapOverlay = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);
        
        
        
        //http://mobiforge.com/developing/story/using-google-maps-android
        //display a label with the point's coordinates
       final Toast myToast = Toast.makeText(getBaseContext(),
    		   myPoints, Toast.LENGTH_LONG);
       
       
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
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

}
