package Store;

import java.util.HashMap;
import java.util.LinkedList;

import controller.WindowControllerBase;

// The class handles the navigation store for different pages
public class NavigationStoreController {
	private HashMap<Class<? extends WindowControllerBase>, WindowControllerBase> windowControllers;
	private WindowControllerBase currentWindowController;
	private LinkedList<WindowControllerBase> windowControllerHistory;
	public NavigationStoreController() {
		
		windowControllers  = new HashMap<>();
		windowControllerHistory = new LinkedList<>();
		windowControllers.put(WindowControllerBase.class, new WindowControllerBase());
		
	}
	
	public WindowControllerBase getCurrentScreenController() {
		return currentWindowController;
	}
	
	public void setCurrentScreen(WindowControllerBase wc) {
		if(wc != null)
			store(wc);
	}
	
	public void store(WindowControllerBase wc) {
		windowControllers.put(wc.getClass(), wc);
	}
	
	/*
	 * switch to other controller if saved. or create and switch
	 */
	public <T extends WindowControllerBase> void switchView(T wc){
		if(windowControllers.get(wc.getClass()) == null)
			store(wc);
		currentWindowController = windowControllers.get(wc.getClass());
	
		// need to insert here the 'hide' and 'set stage' and stuff to really change VIEW
	
	}
	
	

	public WindowControllerBase getPrevious() {
		return windowControllerHistory.peekLast();
	}
	
	/*
	 * add new instance of wc to linkedlist if doesn't exist
	 */
	public <T extends WindowControllerBase> WindowControllerBase setNext(T wc) {
		if(!windowControllerHistory.contains(currentWindowController))
		{
			windowControllerHistory.addLast(wc);	
		}
	}
	
}
