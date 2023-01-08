package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadedTaskExectutor {
	@FunctionalInterface
	public interface Task {
	  void execute();
	}
	private ExecutorService executor;
	private List<Callable<Boolean>> tasks;
	public ThreadedTaskExectutor(int numberOfTasks) {
		executor = Executors.newFixedThreadPool(numberOfTasks);
		tasks = new ArrayList<>();
	}
	public boolean addTask(Task task) {
		tasks.add((Callable<Boolean>) task);
		return true;
	}
}
