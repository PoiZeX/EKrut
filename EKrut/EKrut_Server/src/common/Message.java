package common;
import java.io.Serializable;

public class Message implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private MessageType task;
  
  private Object object;
  
  public Message(MessageType task, Object object) {
    this.task = task;
    this.object = object;
  }
  
  public MessageType getTask() {
    return this.task;
  }
  
  public void setTask(MessageType task) {
    this.task = task;
  }
  
  public Object getObject() {
    return this.object;
  }
  
  public void setObject(Object object) {
    this.object = object;
  }
}