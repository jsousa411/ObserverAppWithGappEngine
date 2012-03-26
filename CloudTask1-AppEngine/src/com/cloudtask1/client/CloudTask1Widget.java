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
package com.cloudtask1.client;

//import java.awt.Window;
import java.util.List;

//import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import com.cloudtask1.client.MyRequestFactory.HelloWorldRequest;
import com.cloudtask1.client.MyRequestFactory.MessageRequest;
import com.cloudtask1.client.MyRequestFactory.RegistrationInfoRequest;
import com.cloudtask1.server.Task;
import com.cloudtask1.shared.CloudTask1Request;
import com.cloudtask1.shared.MessageProxy;
import com.cloudtask1.shared.TaskProxy;

public class CloudTask1Widget extends Composite {

  
  private static final int STATUS_DELAY = 4000;
  private static final String STATUS_ERROR = "status error";
  private static final String STATUS_NONE = "status none";
  private static final String STATUS_SUCCESS = "status success";

  interface CloudTask1UiBinder extends UiBinder<Widget, CloudTask1Widget> {
  }

  private static CloudTask1UiBinder uiBinder = GWT.create(CloudTask1UiBinder.class);

  @UiField
  TextAreaElement messageArea;

  @UiField
  InputElement recipientArea;
  
  
  @UiField
  TextAreaElement nameMessage;

  @UiField
  InputElement name;

  @UiField
  InputElement nameId;

  @UiField
  DivElement status;

  @UiField
  Button sayHelloButton;

  @UiField
  Button sendMessageButton;
  
  @UiField
  Button addTaskButton;
  
  @UiField
  Button updateTaskButton;
  
  @UiField
  Button queryTaskButton;
  
  @UiField 
  Button deleteTaskButton;
  
  Long id = null;
  List<Long> ids;

  TaskProxy taskProxy;
  /**
   * Timer to clear the UI.
   */
  Timer timer = new Timer() {
    @Override
    public void run() {
      status.setInnerText("");
      status.setClassName(STATUS_NONE);
      recipientArea.setValue("");
      messageArea.setValue("");
      name.setValue("");
      nameId.setValue("1");
      nameMessage.setValue("");
    }
  };

  private void setStatus(String message, boolean error) {
    status.setInnerText(message);
    if (error) {
      status.setClassName(STATUS_ERROR);
    } else {
      if (message.length() == 0) {
        status.setClassName(STATUS_NONE);
      } else {
        status.setClassName(STATUS_SUCCESS);
      }
    }

    timer.schedule(STATUS_DELAY);
  }

  public CloudTask1Widget() {
    initWidget(uiBinder.createAndBindUi(this));
    sayHelloButton.getElement().setClassName("send centerbtn");
    sendMessageButton.getElement().setClassName("send");
    addTaskButton.getElement().setClassName("add");
    updateTaskButton.getElement().setClassName("update");
    queryTaskButton.getElement().setClassName("query");
    deleteTaskButton.getElement().setClassName("delete");
    

    final EventBus eventBus = new SimpleEventBus();
    final MyRequestFactory requestFactory = GWT.create(MyRequestFactory.class);
    //final CloudTask1Request myCloudTasks = GWT.create(CloudTask1Request.class);
   
    requestFactory.initialize(eventBus);  

    sendMessageButton.addClickHandler(new ClickHandler() {
    public void onClick(ClickEvent event) {
        String recipient = recipientArea.getValue();
        String message = messageArea.getValue();
        setStatus("Connecting...", false);
        sendMessageButton.setEnabled(false);

        // Send a message using RequestFactory
        MessageRequest request = requestFactory.messageRequest();
        MessageProxy messageProxy = request.create(MessageProxy.class);
        messageProxy.setRecipient(recipient);
        messageProxy.setMessage(message);
        Request<String> sendRequest = request.send().using(messageProxy);
        sendRequest.fire(new Receiver<String>() {
          @Override
          public void onFailure(ServerFailure error) {
            sendMessageButton.setEnabled(true);
            setStatus(error.getMessage(), true);
          }

          @Override
          public void onSuccess(String response) {
            sendMessageButton.setEnabled(true);
            setStatus(response, response.startsWith("Failure:"));
          }
        });
      }
    });

    sayHelloButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        sayHelloButton.setEnabled(false);
        HelloWorldRequest helloWorldRequest = requestFactory.helloWorldRequest();
        helloWorldRequest.getMessage().fire(new Receiver<String>() {
          @Override
          public void onFailure(ServerFailure error) {
            sayHelloButton.setEnabled(true);
            setStatus(error.getMessage(), true);
          }

          @Override
          public void onSuccess(String response) {
            sayHelloButton.setEnabled(true);
            setStatus(response, response.startsWith("Failure:"));
          }
        });
      }
    });
    
    //Add a task
    addTaskButton.addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			addTaskButton.setEnabled(false);
			
			CloudTask1Request myCloudTasks = requestFactory.cloudTask1Request();
			myCloudTasks.createTask().fire(new Receiver<TaskProxy>(){

				@Override
				public void onSuccess(TaskProxy task) {
					Window.alert("CREATE SUCCESS:(" + task.getId() + ")" );
					taskProxy = task;
					id = task.getId();
					ids.add(id);
					
				}
								
			});
		 
			
		}    	
     });
    
    
  //Delete a task
    deleteTaskButton.addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			
			try{
				if(nameId.getValue().length() > 0){
					
					int temp;
					String tempstr;
					
					tempstr = nameId.getValue();
					
					//temp =  tempstr;
					
					//Window.alert("value of tmp str is : "+tempstr);
					
					id = Long.valueOf(tempstr); //ids.get(temp);
					
					//Window.alert("value of id is : "+id);
				}
				else{
					id = 1L;
				}
			}
			catch(Exception e){
				
				
				Window.alert(e.toString());
			}
			
			
			requestFactory.cloudTask1Request().readTask(id).fire( new Receiver<TaskProxy>() {

				//Need to Implement
				//public void onFailure(){
				//}
				/*
				 * 
				*/
				@Override
				public void onSuccess(TaskProxy response) {
					
					
						requestFactory.cloudTask1Request().deleteTask(response).fire(new Receiver<Void>(){
							
							

							@Override
							public void onSuccess(Void response) {
								Window.alert("Deleted Task: "+id);
								
							}
							
						});
					}
					
				});
		  }
		});
    
    
    //Update a task
    updateTaskButton.addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			
			//get id number
			//name and notes from text fields
			try{
				if(nameId.getValue().length() > 0){
					
					int temp;
					String tempstr;
					
					tempstr = nameId.getValue();
					
					//temp =  tempstr;
					
					//Window.alert("value of tmp str is : "+tempstr);
					
					id = Long.valueOf(tempstr); //ids.get(temp);
					
					//Window.alert("value of id is : "+id);
				}
				else{
					id = 1L;
				}
			}
			catch(Exception e){
				
				
				Window.alert(e.toString());
			}
			finally{
				
				//id = 1L;
			}
			
			requestFactory.cloudTask1Request().readTask(id).fire( new Receiver<TaskProxy>() {

				//this is supposed to be the way to get the task to be updated
				@Override
				public void onSuccess(TaskProxy response) {
					
					//String nameStr = name.getValue();
					//requestFactory.cloudTask1Request().
					//response.setNote(nameMessage.getValue());
					
					//taskProxy = response;
					
					
					//Window.alert("UPDATED TASK:  "+response.getId()+" with "+
					//name.getValue()+" and "+nameMessage.getValue());
					
					//update1(response);
					
				}
				
				
			});
			
			//we just passs in taskProxy for now to avoid errors...
			update1(taskProxy);
			
		}
     
		private void update1(TaskProxy task) { 
			CloudTask1Request request =  requestFactory.cloudTask1Request();
		  	taskProxy = request.edit(taskProxy); 
		  	
		  	taskProxy.setName("TESTS");//name.getValue());
		  	
		  	//taskProxy.setNote(getTaskName());
		  	
		  	
		  	  	request.updateTask(task).fire(new Receiver<TaskProxy>() {
			  		@Override    public void onSuccess(TaskProxy task) {
			  			Window.alert("UPDATE SUCCESS:(" 
			  		+ task.getId().toString() + "): " + task.getName());
			  			} 
			  		}); 
		}
      		
		private String getTaskName(){
			
			 //String recipient = name.getValue();
		     String message = nameMessage.getValue();
		   	     
		     return message;
		}   
		 
      });
      
  //Query Tasks added
  		queryTaskButton.addClickHandler(new ClickHandler(){

  				@Override
  				public void onClick(ClickEvent event) {
  					
  					query();
  					
  				}
  		
  				
		  		private void query() { 
		  			
		  			
		  	    	requestFactory.cloudTask1Request().queryTasks().fire( new Receiver<List<TaskProxy>>() {
		  	    		@Override    
		  	    		public void onSuccess(List<TaskProxy> taskList) {
		  	    			String names = "\n";
		  	    			for (TaskProxy task : taskList) {
		  	    				names += " (" + task.getId().toString() + "): " + task.getName() + "\n";       
		  	    				
		  	    			//	ids.add(task.getId().intValue(),task.getId());
		  	    			}
		  	    			Window.alert("QUERY SUCCESS: Count[" + taskList.size()  + "] Values:" + names);
		  	    			} 
		  	    		});
		  	     }
  	    
         });
   
  
  }
}
