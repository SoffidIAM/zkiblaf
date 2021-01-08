package es.caib.zkib.zkiblaf;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Vbox;

public class Frame extends Vbox implements Frameable, AfterCompose {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String title;
	public boolean saveContent;
	public boolean canClose = true;

	public Frame ()
	{
		setSclass("outer-frame");
	}
	public boolean canClose(EventListener action) {
		return canClose;
	}
	public void setCanClose(boolean canClose) {
		this.canClose = canClose;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isSaveContent() {
		return saveContent;
	}
	public void setSaveContent(boolean saveContent) {
		this.saveContent = saveContent;
	}

	public void afterCompose() {
		Application.registerPage(Frame.this);
	}
	
	
}
