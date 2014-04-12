/*
 * ItemAdapter.java
 * 
 * SFSU Fall 2013
 * CSC 875 - Term Project
 * Joao Sousa
 * Notes:  This file contains the code that performs the CRUD
 * of the application
 * 
 * 12/17/2013
 */
package com.observer.notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

 





import java.util.Date;
import java.util.Locale;

import com.google.api.services.drive.model.File;
import com.google.cloud.backend.android.R;
import com.google.cloud.backend.android.sample.guestbook.GuestbookActivity;



public class ItemAdapter extends ArrayAdapter<Item> {	
	private static final int SELECT_ITEM = 2;

	private static final int GET_IMAGE = 11;

	private static final String IMAGE_DIRECTORY_NAME = "ObserverImages";
	

	int IMAGE_CAPTURE = 10,
		GALLERY = 1,
		URI = 12;
	
	Activity activity;
	int layoutResourceId;
	Item myItem;
	ArrayList<Item> data = new ArrayList<Item>();

    public ItemAdapter(Activity activity, int textViewResourceId, ArrayList<Item> objects) {

        super(activity, textViewResourceId, objects);

        
        this.activity = activity;
        this.data = objects;
        this.layoutResourceId = textViewResourceId;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View row = convertView;
	    ItemHolder holder = null;
    	
	    
	    if(row == null){
	    	
	    	LayoutInflater inflater = LayoutInflater.from(activity); //(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	row = inflater.inflate(layoutResourceId, parent, false);
	    	holder = new ItemHolder();
	    	holder.name = (TextView) row.findViewById(R.id.item_name_txt);
	    	holder.add_notes = (TextView) row.findViewById(R.id.item_notes_txt);
	    	holder.url = (TextView) row.findViewById(R.id.item_url_txt);
	    	holder.edit = (Button) row.findViewById(R.id.btn_update);
	    	holder.delete = (Button) row.findViewById(R.id.btn_delete);
	    	holder.image = (Button) row.findViewById(R.id.list_image);
	    	holder.key_value = -1;
	    	
	    	row.setTag(holder);
	    	
	    }
	    else{
	    	
	    	 holder = (ItemHolder) row.getTag();
	    	 
	    }
        
        
        	String temp = "";
        	int ti = -1;
        	myItem = data.get(position);
        	ti = myItem.getId();
		    holder.edit.setTag(ti);
		    holder.delete.setTag(ti);
		    holder.image.setTag(ti);
		    holder.key_value = ti;
		    
		    temp = myItem.getKeyName();//it should be key_name but it's under name.
        	holder.name.setText(myItem.getKeyName());
		    //temp = myItem.getDataNotes();
		   
		    holder.add_notes.setText(myItem.getDataNotes());
		    holder.url.setText(myItem.getImageUrl());
        
	    row.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				Context currentContext = getApplicationContext();
				DatabaseHandler dbHandler = new DatabaseHandler(currentContext);
				ItemHolder tempItem = (ItemHolder)v.getTag();
				String result = tempItem.url.getText().toString()+","+
								tempItem.name.getText().toString()+","+
								tempItem.add_notes.getText().toString();
								
				Log.i("URL:  ", tempItem.url.getText().toString());
				Log.i("NAME:  ", tempItem.name.getText().toString());
				Log.i("Notes:  ", tempItem.add_notes.getText().toString());
			    
				//return values to main activity
				Intent intent = new Intent();//new Intent(activity.getApplicationContext(), GuestbookActivity.class);
			     	
				intent.putExtra("com.observer.notes", result);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				if(activity.getParent() == null){
					activity.setResult(SELECT_ITEM, intent);
				}else{
					
					activity.getParent().setResult(SELECT_ITEM, intent);
				}
				 
				Log.i("ItemAdpater:  ", "Setting Row Result");
								
				activity.finish();
				 
			}

			private Context getApplicationContext() {
				// TODO Auto-generated method stub
				return null;
			}
			
			
		});
		    
	    //image button click listener
	    holder.image.setOnClickListener(new OnClickListener() {
		    @Override
			public void onClick(View v) {
			    
		    	
		    	
		    	String title = "Update Photo";
		        CharSequence[] itemlist ={"Take a new image",
					                      "Pick from Gallery",
					                      "Download"};

		        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		        builder.setIcon(R.drawable.castnotes);
		        builder.setTitle(title);
		        builder.setItems(itemlist, new DialogInterface.OnClickListener() {

		        	//prompt user for options on selecting new images
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	
			            	/*final Intent galleryIntent = new Intent(Intent.);

			            	    //galleryIntent.setType("* / *");

			            	    activity.startActivityForResult(galleryIntent, GET_IMAGE);*/

			            	//define intent to prompt user for image selection options
			            	Intent imageIntent;
			            	
			            	
			                switch (which) {
			                case 0:// Take a new image
			                	imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			                	
			                    activity.startActivityForResult(imageIntent,IMAGE_CAPTURE); 
			                    break;
			                case 1:// Pick from Gallery
			                	imageIntent = new Intent(Intent.ACTION_PICK );
			                	imageIntent.setType("image/*");
			                     
			                	activity.startActivityForResult(imageIntent,GALLERY);
			                    break;
			                case 2:// Download
			                	String url = "www.reuters.com";
			                	imageIntent = new Intent(Intent.ACTION_VIEW);
			                	imageIntent.setData(Uri.parse(url));
			                	
			                	activity.startActivityForResult(imageIntent, URI);
			                    break;
			                default:
			                    break;
			                }
			            }
		        });
		        AlertDialog alert = builder.create();
		        alert.setCancelable(true);
		        alert.show();
	
			}
	    });
	    
	    //record edit button click listener
	    holder.edit.setOnClickListener(new OnClickListener() {
		    @Override
			public void onClick(View v) {
			    // TODO Auto-generated method stub
			    Log.i("Edit Button Clicked", "**********");
	
			    Intent update_item = new Intent(activity,
				    AddUpdateData.class);
			    update_item.putExtra("UPDATE", "update");
			    update_item.putExtra("ID", v.getTag().toString());
			    activity.startActivity(update_item);
	
			}
	    });
	    
	    //delete list record button listener
	    holder.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
			    // TODO Auto-generated method stub
	
			    // show a message while loader is loading
	
			    AlertDialog.Builder adb = new AlertDialog.Builder(activity);
			    adb.setTitle("Delete?");
			    adb.setMessage("Are you sure you want to delete ");
			    final int item_id = Integer.parseInt(v.getTag().toString());
			    adb.setNegativeButton("Cancel", null);
			    adb.setPositiveButton("Ok",
				    new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
						int which) {
					    // MyDataObject.remove(positionToRemove);
					    DatabaseHandler dBHandler = new DatabaseHandler(
						    activity.getApplicationContext());
					    //dBHandler.Deleted_Item_Marked(item_id);
					    dBHandler.Delete_Item(item_id);
					    //ObserverNotes.this.onResume();
					   
					   
					    
					    //com.observer.notes.ObserverNotes.this.onResume();
					    
	
					}
				    });
			    adb.show();
			}

	    });
	    

	    
	    return row;


	    
    }
    
    
   
    
    
    class ItemHolder {
	    TextView name;
	    TextView add_notes;
	    TextView url;
	    int key_value;
	    Button edit;
	    Button delete;
	    Button image;
	    
	}

}



/*

public class ItemAdapter1 extends ArrayAdapter<String> {
	
	
	

    private final Context context;
    private final String[] Ids;
    private final int rowResourceId;

    public ItemAdapter(Context context, int textViewResourceId, String[] objects) {

        super(context, textViewResourceId, objects);

        this.context = context;
        this.Ids = objects;
        this.rowResourceId = textViewResourceId;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(rowResourceId, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        TextView textView = (TextView) rowView.findViewById(R.id.textView);

        int id = Integer.parseInt(Ids[position]);
        String imageFile = ObserverNotes_Model.GetbyId(id)._icon_file;

        textView.setText(ObserverNotes_Model.GetbyId(id)._name);
        // get input stream
        InputStream ims = null;
        try {
            ims = context.getAssets().open(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // load image as Drawable
        Drawable d = Drawable.createFromStream(ims, null);
        // set image to ImageView
        imageView.setImageDrawable(d);
        return rowView;

    }


*/