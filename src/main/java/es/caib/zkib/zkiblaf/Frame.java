package es.caib.zkib.zkiblaf;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Vbox;

public class Frame extends Vbox implements Frameable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String title;
	public boolean saveContent;
	public boolean canClose = true;

	public Frame ()
	{
		addEventListener("onCreate", new EventListener() { //$NON-NLS-1$
			public void onEvent(Event event) throws Exception {
				Application.registerPage(Frame.this);
			}
		});
	}
	public boolean canClose() {
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
	
	
}
