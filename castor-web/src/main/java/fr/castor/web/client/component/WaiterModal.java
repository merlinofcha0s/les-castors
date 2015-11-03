package fr.castor.web.client.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Waiter modal, blocks the UI on ajax operations
 * @author Adnane
 *
 */
public class WaiterModal extends Panel{
	
	private static final long serialVersionUID = -3213218283451226026L;
	
	/**
	 * JAVASCRIPT FUNCTIONS
	 */
	private static final String SHOW_WAITER_MODAL = "showWaiterModal()";
	private static final String HIDE_WAITER_MODAL = "hideWaiterModal()";
	
	private boolean isShowing = false; 

	public WaiterModal(String id) {
		super(id);
	}
	
	/**
	 * Shows the waiter modal on ajax calls
	 * @param target The ajax request target
	 */
	public void show(AjaxRequestTarget target){
		isShowing = true;
		target.appendJavaScript(SHOW_WAITER_MODAL);
	}
	
	/**
	 * Hide the waiter modal on ajax calls
	 * @param target The ajax request target
	 */
	public void hide(AjaxRequestTarget target){
		isShowing = false;
		target.appendJavaScript(HIDE_WAITER_MODAL);
	}
	
	/**
	 * Tells if the component is active or not
	 * @return true if visible, false if hidden
	 */
	public boolean isShowing(){
		return isShowing;
	}

}
