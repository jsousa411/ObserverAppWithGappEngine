package com.observer.notes;

 

import com.google.cloud.backend.android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddUpdateData extends Activity{
	
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
					    valid_data_notes, valid_url));
				
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
    
    @Override
    public void onBackPressed() {        
    	super.onBackPressed();
    }
    public void return_to_previousActivity(){
    	
    	finish();
    }
    
    public void Set_Add_Update_Screen() {

    	add_name = (EditText) findViewById(R.id.add_name);
    	add_data_notes = (EditText) findViewById(R.id.add_data_notes);
    	add_url = (EditText) findViewById(R.id.add_url);

    	add_save_btn = (Button) findViewById(R.id.add_save_btn);
    	update_btn = (Button) findViewById(R.id.update_btn);
    	add_view_all = (Button) findViewById(R.id.add_view_all);
    	update_view_all = (Button) findViewById(R.id.update_view_all);

    	add_view = (LinearLayout) findViewById(R.id.add_view);
    	update_view = (LinearLayout) findViewById(R.id.update_view);

    	add_view.setVisibility(View.GONE);
    	update_view.setVisibility(View.GONE);

    }

    public void Show_Toast(String msg) {
    	
    	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    	
    }

    public void Reset_Text() {

		add_name.getText().clear();
		add_url.getText().clear();
		add_data_notes.getText().clear();

    }
}
