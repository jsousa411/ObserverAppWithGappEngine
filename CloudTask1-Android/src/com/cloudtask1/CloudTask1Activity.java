/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.cloudtask1;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudtask1.client.MyRequestFactory;
import com.cloudtask1.shared.CloudTask1Request;
import com.cloudtask1.shared.TaskProxy;
//import com.google.gwt.user.client.Window;
import com.google.web.bindery.requestfactory.shared.Receiver;

/**
 * Main activity - requests "Hello, World" messages from the server and provides
 * a menu item to invoke the accounts activity.
 */
public class CloudTask1Activity extends Activity {
	
	
	
     
	Intent myintent;
	String result;
	//myLocationOverlay;
	/*
	GeoPoint point;
	MapView mapView;
	MapController mc;
	*/
	
	//MapsActivity LocationOverLay;
	
	/**
     * Tag for logging.
     */
    private static final String TAG = "Locator";

    /**
     * The current context.
     */
    private Context mContext = this;

    /**
     * A {@link BroadcastReceiver} to receive the response from a register or
     * unregister request, and to update the UI.
     */
    private final BroadcastReceiver mUpdateUIReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String accountName = intent.getStringExtra(DeviceRegistrar.ACCOUNT_NAME_EXTRA);
            int status = intent.getIntExtra(DeviceRegistrar.STATUS_EXTRA,
                    DeviceRegistrar.ERROR_STATUS);
            String message = null;
            String connectionStatus = Util.DISCONNECTED;
            if (status == DeviceRegistrar.REGISTERED_STATUS) {
                message = getResources().getString(R.string.registration_succeeded);
                connectionStatus = Util.CONNECTED;
            } else if (status == DeviceRegistrar.UNREGISTERED_STATUS) {
                message = getResources().getString(R.string.unregistration_succeeded);
            } else {
                message = getResources().getString(R.string.registration_error);
            }

            // Set connection status
            SharedPreferences prefs = Util.getSharedPreferences(mContext);
            prefs.edit().putString(Util.CONNECTION_STATUS, connectionStatus).commit();

            // Display a notification
            Util.generateNotification(mContext, String.format(message, accountName));
        }
    };
    
    

    /**
     * Begins the activity.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Register a receiver to provide register/unregister notifications
        registerReceiver(mUpdateUIReceiver, new IntentFilter(Util.UPDATE_UI_INTENT));
        // Intent myintent = new Intent(this, MapsActivity.class);
        
        //startActivityForResult(myintent, 0);
        //registerReceiver(mUpdateUIReceiver, new IntentFilter(this, MapsActivity.class));
         //LocationOverLay = new MapsActivity();
		 
        
    }
    
    
    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = Util.getSharedPreferences(mContext);
        String connectionStatus = prefs.getString(Util.CONNECTION_STATUS, Util.DISCONNECTED);
        if (Util.DISCONNECTED.equals(connectionStatus)) {
            startActivity(new Intent(this, AccountsActivity.class));
        	
        }
        
        setScreenContent(R.layout.hello_world);
    }

    /**
     * Shuts down the activity.
     */
    @Override
    public void onDestroy() {
        unregisterReceiver(mUpdateUIReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        // Invoke the Register activity
        menu.getItem(0).setIntent(new Intent(this, AccountsActivity.class));
        //menu.getItem(0).setIntent(new Intent(this, MapsActivity.class));
        return true;
    }

    // Manage UI Screens

    private void setHelloWorldScreenContent() {
        setContentView(R.layout.hello_world);
        
        
        final TextView helloWorld = (TextView) findViewById(R.id.hello_world);
        final Button sayHelloButton = (Button) findViewById(R.id.say_hello);
        final Button addTask = (Button) findViewById(R.id.see_map);
        
        addTask.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				sayHelloButton.setEnabled(false);
				addTask.setEnabled(false);
                
                helloWorld.setText(R.string.contacting_server);
                
                // Use an AsyncTask to avoid blocking the UI thread
                new AsyncTask<Void, Void, String>() {
                    private String message;

                                        
                    protected String doInBackground(Void... arg0) {
                        MyRequestFactory requestFactory = Util.getRequestFactory(mContext,
                                MyRequestFactory.class);
                        final CloudTask1Request request = requestFactory.cloudTask1Request();
                        Log.i(TAG, "Sending request to server");
                        
                            request.createTask().fire(new Receiver<TaskProxy>(){
                                
                                @SuppressWarnings("unused")
    							public void onFailure(Throwable error) {
                                	
                                	final Toast myToast = Toast.makeText(getBaseContext(),
                         	    		   "YOU FAILED TO QUERY DATA!  ERROR: "+error.toString(),Toast.LENGTH_LONG);
                         	                           	       
                                	myToast.show();
                                }

                                
                               

								@Override
								public void onSuccess(TaskProxy arg0) {
									final Toast myToast = Toast.makeText(getBaseContext(),
	                         	    		   "Task Created Successfully ",Toast.LENGTH_LONG);
	                         	                           	       
									myToast.show();
									
								}
								
								 
                            });
							return message;
                            
                             
                    }

                        @Override
                        protected void onPostExecute(String result) {
                            helloWorld.setText(result);
                            sayHelloButton.setEnabled(true);
                          
                        	
                        	 
                        	
                        	//Intent intent = new Intent(this, MapsActivity.class);
      	    		        /*
      	    		         * Start MapsActivity and wait for the result.
      	    		         */
      	    		        //startActivityForResult(intent, 0);
                            
                        	
      	    				
                        }
                    }.execute();

			}
        	
        	
        	
        });
       
////////////////////////////////////////////////////////////////////////////////////////////////////
        sayHelloButton.setOnClickListener(new OnClickListener() {
        	
        	
        	
        	public void onClick(View v) {
                sayHelloButton.setEnabled(false);
                
                helloWorld.setText(R.string.contacting_server);
                
                // Use an AsyncTask to avoid blocking the UI thread
                new AsyncTask<Void, Void, String>() {
                    private String message;

                                        
                    protected String doInBackground(Void... arg0) {
                        MyRequestFactory requestFactory = Util.getRequestFactory(mContext,
                                MyRequestFactory.class);
                        final CloudTask1Request request = requestFactory.cloudTask1Request();
                        Log.i(TAG, "Sending request to server");
                        request.queryTasks().fire(new Receiver<List<TaskProxy>>() {
                            private boolean shadow;

							
                            @SuppressWarnings("unused")
							public void onFailure(Throwable error) {
                            	
                            	final Toast myToast = Toast.makeText(getBaseContext(),
                     	    		   "YOU FAILED TO QUERY DATA!  ERROR: "+error.toString(),Toast.LENGTH_LONG);
                     	    		        
                     	       
                     	       
                     	        myToast.show();
                            }

                            
                            @SuppressWarnings("null")
							@Override
                            public void onSuccess(List<TaskProxy> taskList) {
                                //message = result;
                            	
                            	
                            	message = "\n";
                            	String [] points = new String[taskList.size()];
                            	int i = 0;
    		  	    			for (TaskProxy task : taskList) {
    		  	    				/*message += " (" + task.getId().toString() + "): " + task.getName() + 
    		  	    						" is located at: ("+ task.getNote()+")\n"; 
    		  	    				*/
    		  	    				
    		  	    				message += task.getId()+","+task.getNote()+",";
    		  	    				
    		  	    				
    		  	    				points[i] = task.getNote();
    		  	    				
    		  	    			}
    		  	    			
    		  	    			Intent intentMaps = new Intent(CloudTask1Activity.this,MapsActivity.class);
    		  	    			
    		  	    			 
    		  	    			intentMaps.putExtra("points", points);
		  	    				intentMaps.putExtra("message", message);
		  	    			
		  	    				
		  	    				startActivity(intentMaps);
		  	    				
		  	    				
                            }
                        });
                        
                        result = message;
                        return message;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        helloWorld.setText(result);
                        sayHelloButton.setEnabled(true);
                      
                    	
                    }
                }.execute();
            }
        });
    }
    
    
    /*
     * Start Maps activity
     * 
     */
   /*public void onClick1(View v) {
         
    	sayHelloButton.setEnabled(false);
        createLocation.setEnabled(false);
        helloWorld.setText(R.string.contacting_server);
        
         
         // Use an AsyncTask to avoid blocking the UI thread
         new AsyncTask<Void, Void, String>() {
             private String message;

                                 
             protected String doInBackground(Void... arg0) {
                 MyRequestFactory requestFactory = Util.getRequestFactory(mContext,
                         MyRequestFactory.class);
                 final CloudTask1Request request = requestFactory.cloudTask1Request();
                 Log.i(TAG, "Sending request to server");
                 request.createTask().fire(new Receiver<TaskProxy>(){
                      

					@Override
					public void onSuccess(TaskProxy task) {
						
						String message = "CREATE SUCCESS:(" + task.getId() + ")";
						
						final Toast myToast = Toast.makeText(getBaseContext(),
              	    		   message,Toast.LENGTH_LONG);
              	    	
						myToast.show();
					}

						
                   
	  	    				
                     
                 });
                 
                 result = message;
                 return message;
             }

             @Override
             protected void onPostExecute(String result) {
                 
            	 sayHelloButton.setEnabled(true);
                 createLocation.setEnabled(true);
                 helloWorld.setText(R.string.contacting_server);
   				
             }
         }.execute();
    }*/

    /**
     * Sets the screen content based on the screen id.
     */
    private void setScreenContent(int screenId) {
        setContentView(screenId);
        switch (screenId) {
            case R.layout.hello_world:
                setHelloWorldScreenContent();
                break;
        }
    }
}
