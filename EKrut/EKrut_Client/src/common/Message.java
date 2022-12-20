package common;
import java.io.Serializable;

public class Message implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private TaskType task;
  
  private Object object;
  
  public Message(TaskType task, Object object) {
    this.task = task;
    this.object = object;
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