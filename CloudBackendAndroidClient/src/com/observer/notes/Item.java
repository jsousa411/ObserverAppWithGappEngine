package com.observer.notes;

import java.io.Serializable;

public class Item implements Serializable {

    public int _id;
    public String _icon_file;
    //public String _name;
    public String _details;
    public String _key_name ;
	public String _taxon; 
	public String _common; 
	public String _lifeform; 
	public String _status; 
	public String _family; 
	public String _bloom; 
	public String _data_notes; 
	public String _other_notes; 
	public String _image_url;
	public String _date_taken;

//    public Item(int id, String icon_file, String name, String details) {
//
//        _id = id;
//        _icon_file = icon_file;        
//        _name = name;
//        _details = details;
//        
//
//    }
//    
    public Item()
    {
    
    }
    public Item(int id, String icon_file,   String details,
    		String key_name, String taxon, String common, String lifeform,
    		String status, String family, String bloom, String data_notes,
    			String other_notes, String image_url, String date_taken) {

        _id = id;
        _icon_file = icon_file;        
        
        _details = details;
        _key_name = key_name;
    	_taxon = taxon; 
    	_common = common;
    	_lifeform = lifeform; 
    	_status = status; 
    	_family = family; 
    	_bloom = bloom; 
    	_data_notes = data_notes; 
    	_other_notes = other_notes; 
    	_image_url = image_url;
    	_date_taken = date_taken;

    }
    
    public Item( int id,  
    			 String key_name, 	 String taxon, 		String common, 	String lifeform,
    			 String status, 	 String family, 	String bloom, 	String data_notes,
    			 String other_notes, String image_url, 	String date_taken) {

        _id = id;
         
        _key_name = key_name;
    	_taxon = taxon; 
    	_common = common;
    	_lifeform = lifeform; 
    	_status = status; 
    	_family = family; 
    	_bloom = bloom; 
    	_data_notes = data_notes; 
    	_other_notes = other_notes; 
    	_image_url = image_url;
    	_date_taken = date_taken;

    }
    
    //this is used for temporary updates to the database
    public Item(int id, String key_name, String data_notes, String image_url) {

		   	_id = id;
		    
		   	_key_name = key_name;
			_taxon = ""; 
			_common = "";
			_lifeform = ""; 
			_status = ""; 
			_family = ""; 
			_bloom = ""; 
			_data_notes = data_notes; 
			_other_notes = ""; 
			_image_url = image_url;
			_date_taken = "";
	
	}

    //this is used for new items being added to the database
    public Item( String key_name, String data_notes, String image_url) {

	   	_id = -1;
	    
	   	_key_name = key_name;
		_taxon = ""; 
		_common = "";
		_lifeform = ""; 
		_status = ""; 
		_family = ""; 
		_bloom = ""; 
		_data_notes = data_notes; 
		_other_notes = ""; 
		_image_url = image_url;
		_date_taken = "";

    }

    
    public void setIdNameDetail(int id, String name, String details)
    {
	    _id = id;	          
	      
	    _details = details;
    }
    
    public void setId (int id )
    {
	    _id = id;	          
	    
    }
    
   
    
    public void setDetail( String details)
    {
	    
	    _details = details;
    }
    
    public String getDetails()
    {
	             
	    return this._details;
	    
    }
    
   
    
    public int getId()
    {
	    return this._id;	          
	    
    }
    // getting keyname
    public String getKeyName() {
    	return this._key_name;
    }

    // setting key name
    public void setKeyName(String key_name) {
    	this._key_name = key_name;
    }

    public String getTaxon()
    {
    	return this._taxon;
    }
    
    public void setTaxon(String taxon)
    {
    	this._taxon = taxon;
    }
    
    public String getCommon()
    {
    	return this._common;
    }
    
    public void setCommon(String common)
    {
    	this._common = common;
    }
    
    public String getLifeForm()
    {
    	return this._lifeform;
    }
    
    public void setLifeForm(String lifeform)
    {
    	this._lifeform = lifeform;
    }
    
    public String getStatus()
    {
    	return this._status;
    }
    
    public void setStatus(String status)
    {
    	this._status = status;
    }
    
    public String getFamily()
    {
    	return this._family;
    }
    
    public void setFamily(String family)
    {
    	this._family = family;
    }
     
    public String getBloom()
    {
    	return this._bloom;
    }
    
    public void setBloom(String bloom)
    {
    	this._bloom = bloom;
    }
	  
	
    public String getDataNotes()
    {
    	return this._data_notes;
    }
    
    public void setDataNotes(String data_notes)
    {
    	this._data_notes = data_notes;
    }
	   
	public String getOtherNotes()
    {
    	return this._other_notes;
    }
    
    public void setOtherNotes(String other_notes)
    {
    	this._other_notes = other_notes;
    }
	  
	
	public String getImageUrl()
    {
    	return this._image_url;
    }
    
    public void setImageUrl(String image_url)
    {
    	this._image_url = image_url;
    }
	 
	public String getDateTaken()
    {
    	return this._date_taken;
    }
    
    public void setDateTaken(String date_taken)
    {
    	this._date_taken = date_taken;
    }
}
