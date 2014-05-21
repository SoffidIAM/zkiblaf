package es.caib.zkib.zkiblaf;

import org.zkoss.zul.Vbox;


public interface Frameable {

	public boolean canClose() ;
	public void setCanClose(boolean canClose) ;
	public String getTitle() ;
	public void setTitle(String title) ;
	public boolean isSaveContent() ;
	public void setSaveContent(boolean saveContent) ;

}
