package com.cloudtask1.shared;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName(value = "com.cloudtask1.server.CloudTask1Service", locator = "com.cloudtask1.server.CloudTask1ServiceLocator")
public interface CloudTask1Request extends RequestContext {

	Request<TaskProxy> createTask();

	Request<TaskProxy> readTask(Long id);

	Request<TaskProxy> updateTask(TaskProxy task);

	Request<Void> deleteTask(TaskProxy task);

	Request<List<TaskProxy>> queryTasks();

}
