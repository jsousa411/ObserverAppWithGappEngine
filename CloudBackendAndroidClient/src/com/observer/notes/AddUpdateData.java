/*
 * AddUpdate.java
 * 
 * SFSU Fall 2013
 * CSC 875 - Term Project
 * Joao Sousa
 * Notes:  This file contains the code used to insert data 
 * via Intents from Guestbook.
 * 12/17/2013 
 * 
 */
package com.observer.notes;


import java.util.ArrayList;
import java.util.StringTokenizer;

import com.google.cloud.backend.android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddUpdateData extends Activity{
	private static final int BROADCAST_INSERT = 3;
	/*
	 item.setId(idno);
    	    item.setName(name);
    	    item.setDataNotes(data_notes);
    	    item.setImageUrl(url);
	*/
	EditText 	add_name, 
				add_data_notes, 
				add_url;

	Button 	add_save_btn, 
			add_view_all, 
			update_btn, 
			update_view_all;

	LinearLayout add_view, 
				 update_view;

	String 	valid_url = null, 
			valid_name = null,
			valid_data_notes = null,
			Toast_message = null, 
			valid_id = "";
	int ID;
    DatabaseHandler dbHandler = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_update);
	
		// set screen
		Set_Add_Update_Screen();
	
		// set visibility of view as per calling activity
		//String called_from = getIntent().getStringExtra("UPDATE");
		
		Intent intent = getIntent();
		String called_from = intent.getStringExtra("UPDATE");
		 
	
		if (called_from.equalsIgnoreCase("add")) {
		    add_view.setVisibility(View.VISIBLE);
		    update_view.setVisibility(View.GONE);
		}
		else if(called_from.equalsIgnoreCase("getItem")){
			
			ID = Integer.parseInt(getIntent().getStringExtra("ID"));
		    Item c = dbHandler.Get_item(ID);
		    
		    Intent update_item = new Intent();
		    Bundle b = new Bundle();
		    
		    b.putSerializable("itemFound",c);
		    
			update_item.putExtras(b);
			    
			setResult(RESULT_OK, update_item);
			finish();
			
		}
		else if(called_from.equalsIgnoreCase("broadcasted")){
			//Guestbook
			String messages = getIntent().getStringExtra("Guestbook").toString();			
			String[] individualItems ;
			String[] itemsCasted = SplitUsingTokenizer(messages,"|");
			String itemName = "";
			Item  tempItem;
			int i = 0;
			int j = 0;
			int countRemoved = 0;
			int[] removeIndex = new int[itemsCasted.length];
			ArrayList<Item> item_list;// = new ArrayList<Item>();
			ArrayList<Item> newItems;
			
			
			Log.i("Inserting DATA:  ", messages);					
			
			//get items from database
			item_list = dbHandler.Get_All_items();
			//verify which item is not in database
	
			//this code is used to update the database manually.  Leave it for debugging..
			//i=0;
			//while(i< item_list.size()){
				
				//tempItem = item_list.get(i);
//				if(tempItem.getKeyName().contains("whitepaper")){
//					
//					dbHandler.Deleted_Item_Marked(tempItem.getId());
//				}
//				else if(tempItem.getKeyName().trim().length() == 0 ){
//					
//					dbHandler.Deleted_Item_Marked(tempItem.getId());
//				}
//				else if(tempItem.getKeyName().contains("pear")){
//					
//					dbHandler.Deleted_Item_Marked(tempItem.getId());
//				}
//				else{}
				
				//i++;
			//}
			
			i=0;
			 
			j = 0;
				
			while( j < itemsCasted.length){
				removeIndex[j]	= -1;
				//itemsCasted[j].replace(",", "").trim();
				individualItems =  SplitUsingTokenizer(itemsCasted[j],"@#");//.toString().split("@#");
				
				Log.i("value of individualItems[0].toString() is: ",individualItems[0].toString());
				while(i < item_list.size() ){
					
					tempItem = item_list.get(i);										
				   
					if(individualItems[1].contains(tempItem.getKeyName())){
						if(removeIndex[j] == -1)
						{
							removeIndex[j]	= j;
							countRemoved++;
						}
					}
					
					
					i++;
				}
				i=0;
				j++;
				
			}
				 
			
			//insert items missing from database
			
			if(countRemoved < itemsCasted.length){
				i=0;
				while(i < itemsCasted.length){
					
					if(removeIndex[i]  == -1){
						//tempItem = item_list.get(i);
						individualItems =  SplitUsingTokenizer(itemsCasted[i],"@#");
						tempItem = new Item(individualItems[1],individualItems[2],individualItems[0],"");
						
						dbHandler.Add_item(tempItem);
						
						Log.i("Inserting item:  ", tempItem.getKeyName());
					}
					i++;
				}
			}
			
			Intent finish_intent = new Intent();			
			
			finish_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			if(this.getParent()== null){
				this.setResult(BROADCAST_INSERT,finish_intent);
			}
			else{
				
				this.getParent().setResult(BROADCAST_INSERT, intent);
			}
			
			this.finish();
			
		}
		else {
	
		    update_view.setVisibility(View.VISIBLE);
		    add_view.setVisibility(View.GONE);
		    ID = Integer.parseInt(getIntent().getStringExtra("ID"));
		    Item c = dbHandler.Get_item(ID);
		    add_name.setText(c.getKeyName());
		    add_data_notes.setText(c.getDataNotes());
		    add_url.setText(c.getImageUrl());
		    // dbHandler.close();
		}
		
		add_view_all.setOnClickListener(new View.OnClickListener() {

		    @Override
		    public void onClick(View v) {
			 
		    	return_to_previousActivity();
		    }
		});
		
		/*
		 * Updates Database
		 */
		update_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int dbResult = 0;				
				
				//Item tmpItem;
				
				//tmpItem = dbHandler.Get_itemByName(valid_name);
				
				
				
				dbResult = dbHandler.Update_item(new Item( ID, valid_name,
					    valid_data_notes, valid_url));
				
			    if( dbResult == 1){
			    	
			    	Toast_message = String.valueOf("Data updated in DB"); //"Data inserted successfully";
			    	Show_Toast(Toast_message);
				    //Reset_Text();
				    
			    }
			    else{
			    	
			    	Toast_message = "Failed to insert data";
			    	Show_Toast(Toast_message);
				    
			    }
			    return_to_previousActivity();
			}
		});
		
		add_save_btn.setOnClickListener(new View.OnClickListener() {

		    @Override
		    public void onClick(View v) {
				int dbResult = 0;
				
				
				dbResult = dbHandler.Add_item(new Item(valid_name,
					    valid_data_notes, valid_url,""));
				
			    if( dbResult == 1){
			    	
			    	Toast_message = String.valueOf("Data inserted successfully");
			    	Show_Toast(Toast_message);
				    //Reset_Text();
				    
			    }
			    else{
			    	
			    	Toast_message = "Failed to insert data";
			    	Show_Toast(Toast_message);
				    
			    }
				
			    return_to_previousActivity();
				// check the value state is null or not
//				if (valid_url != null && valid_url.length() != 0
//					&& valid_data_notes != null && valid_data_notes.length() != 0
//					&& valid_name != null && valid_name.length() != 0) {
//	
//					dbResult = dbHandler.Add_item(new Item(valid_name,
//						    valid_data_notes, valid_url));
//					
//				    if( dbResult == 1){
//				    	
//				    	Toast_message = "Data inserted successfully";
//				    	Show_Toast(Toast_message);
//					    Reset_Text();
//				    }
//				    else{
//				    	
//				    	Toast_message = "Failed to insert data";
//				    	Show_Toast(Toast_message);
//					    
//				    }
//				    	
//				    
//	
//				}

		    }
		});
		
		add_url.addTextChangedListener(new TextWatcher() {

		    @Override
		    public void onTextChanged(CharSequence s, int start, int before,
			    int count) {
			// TODO Auto-generated method stub

		    }

		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count,
			    int after) {
			// TODO Auto-generated method stub

		    }

		    @Override
		    public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		    	Is_Valid_Url(add_url);
		    }
		});
		
		add_data_notes.addTextChangedListener(new TextWatcher() {

		    @Override
		    public void onTextChanged(CharSequence s, int start, int before,
			    int count) {
			// TODO Auto-generated method stub

		    }

		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count,
			    int after) {
			// TODO Auto-generated method stub

		    }

		    @Override
		    public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		    	Is_Valid_DataNotes(add_data_notes);
		    }
		});
		
		add_name.addTextChangedListener(new TextWatcher() {

		    @Override
		    public void onTextChanged(CharSequence s, int start, int before,
			    int count) {
			// TODO Auto-generated method stub

		    }

		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count,
			    int after) {
			// TODO Auto-generated method stub

		    }

		    @Override
		    public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		    	Is_Valid_Name(add_name);
		    }
		});

		
    }//end OnCreate()
    
    
    public static String[] SplitUsingTokenizer(String Subject, String Delimiters) 
    {
     StringTokenizer StrTkn = new StringTokenizer(Subject, Delimiters);
     ArrayList<String> ArrLis = new ArrayList<String>(Subject.length());
     while(StrTkn.hasMoreTokens())
     {
       ArrLis.add(StrTkn.nextToken());
     }
     return ArrLis.toArray(new String[0]);
    }
    
    
    //this method's code was turned off because it was causing errors
    //even when the data was valid.  Leave it for now, but will pick up 
    //the functionality later.
    public void Is_Valid_Name(EditText edt) throws NumberFormatException {
    	
    	valid_name = edt.getText().toString();
    	
    	
    	
//		if (edt.getText().toString().length() <= 0) {
//		    edt.setError("Accept Alphabets Only.");
//		    valid_name = null;
//		} else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
//		    edt.setError("Accept Alphabets Only.");
//		    valid_name = null;
//		} else {
//		    valid_name = edt.getText().toString();
//		}

    }
    

    //this method's code was turned off because it was causing errors
    //even when the data was valid.  Leave it for now, but will pick up 
    //the functionality later.
    public void Is_Valid_DataNotes(EditText edt) throws NumberFormatException {
		
    	valid_data_notes = edt.getText().toString();
    	
//    	if (edt.getText().toString().length() <= 0) {
//		    edt.setError("Accept Alphabets Only.");
//		    valid_name = null;
//		} else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
//		    edt.setError("Accept Alphabets Only.");
//		    valid_name = null;
//		} else {
//		    valid_data_notes = edt.getText().toString();
//		}

    }
    

    //this method's code was turned off because it was causing errors
    //even when the data was valid.  Leave it for now, but will pick up 
    //the functionality later.
    public void Is_Valid_Url(EditText edt) throws NumberFormatException {
    	 
    	valid_url = edt.getText().toString();
    	 
//		if (edt.getText().toString().length() <= 0) {
//		    edt.setError("Accept URL Only.");
//		    valid_name = null;
//		} else if (!URLUtil.isValidUrl((edt.getText().toString()))) {
//		    edt.setError("Accept URL Only.");
//		    valid_name = null;
//		} else {
//		    
//		}

    }
    
    //User wants to return to previous activity
    @Override
    public void onBackPressed() {        
    	super.onBackPressed();
    }
    public void return_to_previousActivity(){
    	
    	finish();
    }
    
    //User clicked the Pencil Image or Add Notes button
    public void Set_Add_Update_Screen() {

    	//text fields for user to insert data
    	add_name = (EditText) findViewById(R.id.add_name);
    	add_data_notes = (EditText) findViewById(R.id.add_data_notes);
    	add_url = (EditText) findViewById(R.id.add_url);

    	//buttons for user to interact with
    	add_save_btn = (Button) findViewById(R.id.add_save_btn);
    	update_btn = (Button) findViewById(R.id.update_btn);
    	add_view_all = (Button) findViewById(R.id.add_view_all);
    	update_view_all = (Button) findViewById(R.id.update_view_all);

    	add_view = (LinearLayout) findViewById(R.id.add_view);
    	update_view = (LinearLayout) findViewById(R.id.update_view);

    	add_view.setVisibility(View.GONE);
    	update_view.setVisibility(View.GONE);

    }

    //display a message to the user
    public void Show_Toast(String msg) {
    	
    	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    	
    }

    //Display a slate clean screen for a new set of notes to be added
    public void Reset_Text() {

		add_name.getText().clear();
		add_url.getText().clear();
		add_data_notes.getText().clear();

    }
}
