package common;
import java.io.Serializable;

import enums.TaskType;
/**
 * This class represents a message object that can be sent between client and server.
 * It contains a task type and an associated object.
 * 
 * @author [Your Name]
 * @version 1.0
 * @serial 1L
 * @see java.io.Serializable
 */
public class Message implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private TaskType task;
  
  private Object object;
 
  /**
   * Constructs a new message with the given task and object.
   *
   * @param task the task type of the message
   * @param object the associated object of the message
   */
  public Message(TaskType task, Object object) {
    this.task = task;
    this.object = object;
  }
  /**

  Constructor for the Message class. It Initialize a new message with the given task type and no object
  @param task the task type of the message
  */
  public Message(TaskType task) {
	this.task = task;
	this.object = null;
}
  /**

  This method returns the task type of this message.
  @return the task type of this message
  */
public TaskType getTask() {
    return this.task;
  }
/**

This method sets the task type of this message to the given task type.
@param task the task type to set for this message
*/
  public void setTask(TaskType task) {
    this.task = task;
  }
  /**

  This method returns the object of this message.
  @return the object of this message
  */
  public Object getObject() {
    return this.object;
  }
  /**

  This method sets the object of this message to the given object.
  @param object the object to set for this message
  */
  public void setObject(Object object) {
    this.object = object;
  }
}