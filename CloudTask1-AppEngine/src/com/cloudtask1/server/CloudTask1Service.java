package com.cloudtask1.server;

import java.util.List;

import com.cloudtask1.annotation.ServiceMethod;


public class CloudTask1Service {

	static DataStore db = new DataStore(); 

	@ServiceMethod
	public Task createTask() {
		
		 return db.update(new Task()); 
			
	}

	@ServiceMethod
	public Task readTask(Long id) {

		return db.find(id);
	}

	@ServiceMethod
	public Task updateTask(Task task) {

		return db.update(task);
		
		//to notify android that a new data is available
		//u can use c2dm or just send a message to the phone
		
		//via c2dm
		//DataStore.sendC2DMUpdate(TaskChange.Update + TaskChange.separator + task.getId());
	}

	 
	
	@ServiceMethod
	public void deleteTask(Task task) {

		db.delete(task.getId());

	}

	@ServiceMethod
	public List<Task> queryTasks() {

		return db.findAll();
	}
}
