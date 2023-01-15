package common;
import java.io.Serializable;
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
  
  public Message(TaskType task) {
	this.task = task;
	this.object = null;
}

public TaskType getTask() {
    return this.task;
  }
  
  public void setTask(TaskType task) {
    this.task = task;
  }
  
  public Object getObject() {
    return this.object;
  }
  
  public void setObject(Object object) {
    this.object = object;
  }
}