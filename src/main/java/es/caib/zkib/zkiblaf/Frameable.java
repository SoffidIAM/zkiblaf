package es.caib.zkib.zkiblaf;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Vbox;


public interface Frameable {

	public boolean canClose(EventListener listener) ;
	public void setCanClose(boolean canClose) ;
	public String getTitle() ;
	public void setTitle(String title) ;
	public boolean isSaveContent() ;
	public void setSaveContent(boolean saveContent) ;

}
