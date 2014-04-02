/*
 * ObserverNotes_Model.java
 * SFSU Fall 2013
 * CSC 875 - Term Project
 * Joao Sousa
 * Notes:  This file contains the code that inflates data into the Listview adapter before it gets displayed in
 * to the user in a Listview
 * 
 * 12/17/2013
 */


package com.observer.notes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
 
import com.google.cloud.backend.android.CloudBackendActivity;
import com.google.cloud.backend.android.R;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.database.sqlite.SQLiteQueryBuilder;

public class ObserverNotes_Model {
	public static ArrayList<Item> Items = new ArrayList<Item>();
	 
	    ItemAdapter cAdapter;
	    DatabaseHandler db;
	    String Toast_msg;
	    static Context currentContext = null;

    public static void LoadModel(Context applicationcontext) {

    	currentContext = applicationcontext;
    	

    }

    public ItemAdapter LoadData(){
    	
    	String[] ids;
    	
    	Items.clear();//clear all data within the data structure.
    	
    	db = new DatabaseHandler(currentContext);
    	ArrayList<Item> item_array_from_db = db.Get_items();
    	
    	ids = new String[item_array_from_db.size()];
    	
    	for (int i = 0; i < item_array_from_db.size(); i++) {

    	    int idno = item_array_from_db.get(i).getId();
    	    String name = item_array_from_db.get(i).getKeyName();
    	    String data_notes = item_array_from_db.get(i).getDataNotes();
    	    String url = item_array_from_db.get(i).getImageUrl();

    	    Item item = new Item();
    	    
    	    ids[i] = String.valueOf(idno);
    	    item.setId(idno);
    	    item.setKeyName(name);
    	    item.setDataNotes(data_notes);
    	    item.setImageUrl(url);
    	    
    	    Items.add(item);
    	    
    	}
    	
    	db.close();
    	
    	//this is how the row layout will get displayed
    	cAdapter = new ItemAdapter( ((ObserverNotes)currentContext.getApplicationContext()).getCurrentActivity(), 
    								R.layout.row, item_array_from_db);
    		//Contact_listview.setAdapter(cAdapter);
    		//cAdapter.notifyDataSetChanged();
    	
    	return cAdapter;
    }
    
    public static Item GetbyId(int id){

        for(Item item : Items) {
            if (item._id == id) {
                return item;
            }
        }
        return null;
    }

}
