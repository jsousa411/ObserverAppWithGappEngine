/*
 * DatabaseHandler.java
 * 
 * SFSU Fall 2013
 * CSC 875 - Term Project
 * Joao Sousa
 * Notes:  This file contains the code that performs all the database operations:
 * it creates new table, performs CRUD, contains the column name definitions
 *...handles multiple types of select statements.  It's based on SQL lite for Android
 * 
 * 12/17/2013
 */

package com.observer.notes;

import java.sql.Date;
import java.util.ArrayList;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHandler extends SQLiteOpenHelper{
	
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    
    //Log Cat Info
	private static final String LOGCAT = "Instatiating DatabaseHandler";
     
	//Database Name
    private static final String DATABASE_NAME = "ObserverData";

    // Information table name
    private static final String TABLE_INFO = "Observations";
    private static final String TABLE_NAME = "ObserverNotes";
    
    // items Table Columns names
    
    private static final String DELETED = "DELETED";
    private static final String KEY_NAME = "NOTE_ID";
	private static final String TAXON = "TAXON";
	private static final String COMMON = "COMMON";
	private static final String LIFEFORM = "LIFEFORM";
	private static final String STATUS = "STATUS";
	private static final String FAMILY = "FAMILY";
	private static final String BLOOM = "BLOOM";
	private static final String Data_NOTES = "NOTES";
	private static final String Other_Notes = "OTHER";
	private static final String IMAGE_URL = "IMAGEURL";
	private static final String KEY_ID = "KEY_ID";
	private static final String DATE_TAKEN = "DATE_TAKEN"; //new Date(System.currentTimeMillis()).toString();

	private final ArrayList<Item> item_list = new ArrayList<Item>();
	
	public DatabaseHandler(Context applicationcontext) {
			
			super(applicationcontext, DATABASE_NAME, null, DATABASE_VERSION);
			
			
	}

	//create database
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		 
		//Create Database
		 
		String CREATE_itemS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_NAME + " TEXT,"
				+ TAXON + " TEXT," 
				+ COMMON + " TEXT," 
				+ LIFEFORM + " TEXT," 
				+ STATUS + " TEXT," 
				+ FAMILY + " TEXT," 
				+ BLOOM + " TEXT," 
				+ Data_NOTES + " TEXT," 
				+ Other_Notes + " TEXT," 
				+ IMAGE_URL + " TEXT,"
				+ DATE_TAKEN + " TEXT,"
				+ DELETED +" TEXT"
				+");";
				
		Log.d(LOGCAT,"Database created");
				//db.execSQL("IF NOT EXISTS " + TABLE_NAME + " " + CREATE_itemS_TABLE);
				  db.execSQL(""   + CREATE_itemS_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// Create tables again
		onCreate(db);
		
	}
	
	// Adding new item
    public int Add_item(Item item) {
    	int retval = 1;
    	
    	
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		
	
		values.put(KEY_NAME, item.getKeyName()); 	 
		values.put(TAXON, item.getTaxon());
		values.put(COMMON, item.getCommon()); 	   
		values.put(LIFEFORM, item.getLifeForm()); 	 
		values.put(STATUS, item.getStatus()); 	   
		values.put(FAMILY, item.getFamily()); 	   
		values.put(BLOOM, item.getBloom()); 	    
		values.put(Data_NOTES, item.getDataNotes()); 
		values.put(Other_Notes, item.getOtherNotes());
		values.put(IMAGE_URL, item.getImageUrl());  
		values.put(DATE_TAKEN, item.getDateTaken()); 
		values.put(DELETED, "");//this is an indicator for data that gets deleted
		
		try{
		// Inserting Row
		db.insert(TABLE_NAME, null, values);
		db.close(); // Closing database connection
		}
		catch(Exception e){
		
			//Show_Toast(Toast_message);
			retval = 0;
			 
		
		}
		
		
		return retval;
		
    }
    
    
 // Getting single item
    Item Get_item(int id) {
	SQLiteDatabase db = this.getReadableDatabase();

	Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
														KEY_NAME,   
														TAXON, 
														COMMON,     
														LIFEFORM,   
														STATUS,     
														FAMILY,     
														BLOOM,  
														Data_NOTES, 
														Other_Notes,
														IMAGE_URL,  
														DATE_TAKEN,
														DELETED}, KEY_ID + "=?",
														new String[] { String.valueOf(id) },
														null, null, null, null);
		if (cursor != null){
		    cursor.moveToFirst();
		}
		
		//set data into Item object
		Item item = new Item(Integer.parseInt(cursor.getString(0)),
			cursor.getString(1), cursor.getString(2), cursor.getString(3),
			cursor.getString(4), cursor.getString(5), cursor.getString(6),
			cursor.getString(7), cursor.getString(8), cursor.getString(9),
			cursor.getString(10), cursor.getString(11),cursor.getString(12));
		// return item
		cursor.close();
		db.close();
	
		return item;
    }

    
    
    // Getting single item by Name
       Item Get_itemByName(String name) {
   	SQLiteDatabase db = this.getReadableDatabase();

   	Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
   														KEY_NAME,   
   														TAXON, 
   														COMMON,     
   														LIFEFORM,   
   														STATUS,     
   														FAMILY,     
   														BLOOM,  
   														Data_NOTES, 
   														Other_Notes,
   														IMAGE_URL,  
   														DATE_TAKEN, 
   														DELETED}, KEY_NAME + "=?",
   														new String[] { name },
   														null, null, null, null);
   		if (cursor != null){
   		    cursor.moveToFirst();
   		}
   		
   		Item item = new Item(Integer.parseInt(cursor.getString(0)),
   			cursor.getString(1), cursor.getString(2), cursor.getString(3),
   			cursor.getString(4), cursor.getString(5), cursor.getString(6),
   			cursor.getString(7), cursor.getString(8), cursor.getString(9),
   			cursor.getString(10), cursor.getString(11), cursor.getString(12));
   		// return item
   		cursor.close();
   		db.close();
   	
   		return item;
       }

    // Getting All items
    public ArrayList<Item> Get_items() {
	 
	try {
	    item_list.clear();

	    
	     
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_NAME  + " WHERE "+DELETED+" != 'D'";

	    SQLiteDatabase db = this.getWritableDatabase();
	/*    db.delete(TABLE_NAME, null, null);
	  //Create Database
	  		String CREATE_itemS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
	  				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
	  				+ KEY_NAME + " TEXT,"
	  				+ TAXON + " TEXT," 
	  				+ COMMON + " TEXT," 
	  				+ LIFEFORM + " TEXT," 
	  				+ STATUS + " TEXT," 
	  				+ FAMILY + " TEXT," 
	  				+ BLOOM + " TEXT," 
	  				+ Data_NOTES + " TEXT," 
	  				+ Other_Notes + " TEXT," 
	  				+ IMAGE_URL + " TEXT,"
	  				+ DATE_TAKEN + " TEXT"
	  				+ DELETED +"TEXT"
	  				+")";
	  				
	  				db.execSQL(CREATE_itemS_TABLE);*/
	  				
	    Cursor cursor = db.rawQuery(selectQuery, null);

	    // looping through all rows and adding to list
	    /*
			KEY_ID,
			KEY_NAME,   
			TAXON, 
			COMMON,     
			LIFEFORM,   
			STATUS,     
			FAMILY,     
			BLOOM,  
			Data_NOTES, 
			Other_Notes,
			IMAGE_URL,  
			DATE_TAKEN, 
	     
	     */
		    if (cursor.moveToFirst()) {
				do {
				    Item item = new Item();
				    item.setId(Integer.parseInt(cursor.getString(0)));
				    item.setKeyName(cursor.getString(1));
				    item.setTaxon(cursor.getString(2));
				    item.setCommon(cursor.getString(3));
				    item.setLifeForm(cursor.getString(4));
				    item.setStatus(cursor.getString(5));
				    item.setFamily(cursor.getString(6));
				    item.setBloom(cursor.getString(7));
				    item.setDataNotes(cursor.getString(8));
				    item.setOtherNotes(cursor.getString(9));
				    item.setImageUrl(cursor.getString(10));
				    item.setDateTaken(cursor.getString(11));
				    item.setDeleted(cursor.getString(12));
				    // Adding item to list
				    item_list.add(item);
				} while (cursor.moveToNext());
		    }
	
		    // return item list
		    cursor.close();
		    db.close();
		    return item_list;
		} catch (Exception e) {
		    // TODO: handle exception
		    Log.e("all_item", "" + e);
		}
	
		return item_list;
    }

    // Getting All items even the ones marked for deletion
    public ArrayList<Item> Get_All_items() {
	 
	try {
	    item_list.clear();

	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_NAME ;

	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);

	    // looping through all rows and adding to list
	    /*
			KEY_ID,
			KEY_NAME,   
			TAXON, 
			COMMON,     
			LIFEFORM,   
			STATUS,     
			FAMILY,     
			BLOOM,  
			Data_NOTES, 
			Other_Notes,
			IMAGE_URL,  
			DATE_TAKEN, 
	     
	     */
		    if (cursor.moveToFirst()) {
				do {
				    Item item = new Item();
				    item.setId(Integer.parseInt(cursor.getString(0)));
				    item.setKeyName(cursor.getString(1));
				    item.setTaxon(cursor.getString(2));
				    item.setCommon(cursor.getString(3));
				    item.setLifeForm(cursor.getString(4));
				    item.setStatus(cursor.getString(5));
				    item.setFamily(cursor.getString(6));
				    item.setBloom(cursor.getString(7));
				    item.setDataNotes(cursor.getString(8));
				    item.setOtherNotes(cursor.getString(9));
				    item.setImageUrl(cursor.getString(10));
				    item.setDateTaken(cursor.getString(11));
				    item.setDeleted(cursor.getString(12));
				    // Adding item to list
				    item_list.add(item);
				} while (cursor.moveToNext());
		    }
	
		    // return item list
		    cursor.close();
		    db.close();
		    return item_list;
		} catch (Exception e) {
		    // TODO: handle exception
		    Log.e("all_item", "" + e);
		}
	
		return item_list;
    }

    
    // Updating single item
    public int Update_item(Item item) {
		SQLiteDatabase db = this.getWritableDatabase();
	
		ContentValues values = new ContentValues();
		int i =0;
		 
		
		values.put(KEY_NAME, item.getKeyName()); 	 
		values.put(TAXON, item.getTaxon());
		values.put(COMMON, item.getCommon()); 	   
		values.put(LIFEFORM, item.getLifeForm()); 	 
		values.put(STATUS, item.getStatus()); 	   
		values.put(FAMILY, item.getFamily()); 	   
		values.put(BLOOM, item.getBloom()); 	    
		values.put(Data_NOTES, item.getDataNotes()); 
		values.put(Other_Notes, item.getOtherNotes());
		values.put(IMAGE_URL, item.getImageUrl());  
		values.put(DATE_TAKEN, item.getDateTaken()); 
		values.put(DELETED, item.getDeleted());

		// updating row
		i = db.update(TABLE_NAME, values, KEY_ID + " = ?",
		new String[] { String.valueOf(item.getId()) });
		
		if(db != null)
			db.close();
		
		return i;
    }

    public int Deleted_Item_Marked(int id){
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	
		ContentValues values = new ContentValues();
		
		int i = 0;
		 
		Item item = this.Get_item(id);
		
		values.put(KEY_NAME, item.getKeyName()); 	 
		values.put(TAXON, item.getTaxon());
		values.put(COMMON, item.getCommon()); 	   
		values.put(LIFEFORM, item.getLifeForm()); 	 
		values.put(STATUS, item.getStatus()); 	   
		values.put(FAMILY, item.getFamily()); 	   
		values.put(BLOOM, item.getBloom()); 	    
		values.put(Data_NOTES, item.getDataNotes()); 
		values.put(Other_Notes, item.getOtherNotes());
		values.put(IMAGE_URL, item.getImageUrl());  
		values.put(DATE_TAKEN, item.getDateTaken()); 
		values.put(DELETED, item.getDeleted());
		values.put(DELETED, "D");
		
		
		 
		
		// updating row
		i = db.update(TABLE_NAME, values, KEY_ID + " = ?",
		new String[] { String.valueOf(item.getId()) });
		
		 
		
		return i;
    }
    // Deleting single item
    public void Delete_Item(int id) {
    	
    	Log.d(LOGCAT,"Deleting record");
    	
		SQLiteDatabase db = this.getWritableDatabase();
		try{
		db.delete(TABLE_NAME, KEY_ID + " = ?",
			new String[] { String.valueOf(id) });
		}
		catch(Exception e){
			Log.d(LOGCAT,"Error while Deleting record");
			Log.d(LOGCAT,e.toString());
		}
		db.close();
    }

    // Getting items Count
    public int Get_Total_Items() {
		String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE DELETED != 'D'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int i =0;
		cursor.close();
	
		// return count
		i = cursor.getCount();
		
		db.close();
		
		return i ;
    }
    
//this is only needed during debugging. Keep it here for now.
//    public void Show_Toast(String msg) {
//    	
//    	Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//    	
//    }


}
