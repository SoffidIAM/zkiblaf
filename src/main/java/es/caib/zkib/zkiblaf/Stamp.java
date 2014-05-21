package es.caib.zkib.zkiblaf;

import java.net.URL;

import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.Command;
import org.zkoss.zk.au.ComponentCommand;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.UiException;

public class Stamp extends AbstractComponent {
	private static final long serialVersionUID = 1L;


	public static final String COMMAND_UPDATE = "update"; //$NON-NLS-1$
	
	
	public String bgSrc=null;
	public int bgHeight=-1; //max height
	public int bgWidth=-1; //max width
	
	public String stampSrc=null;
	public int stampHeight=-1;
	public int stampWidth=-1;
	public Integer stampTop=null;
	public Integer stampLeft=null;

	public Integer originalStampTop=null;
	public Integer originalStampLeft=null;
	
	public String displayStyle="none"; //$NON-NLS-1$
	public String debugEnabled=null;
	
	/********************END OF VAR DECLARATION *****************************/
	
	public Stamp() {
		super();
	}
	
	
	//cuando se invoca a invalidate, se hace un reset de los parámetros de estilo, así que aquí también debemos hacer el reset
	public void invalidate() {
		super.invalidate();
		
		this.stampTop=originalStampTop;
		this.stampLeft=originalStampLeft;
	}
	
	/**
	 * Administració de comandes que s'envien des del javascript al servidor
	 */
	public Command getCommand(String cmdId) {
		if(COMMAND_UPDATE.equals(cmdId))
			return _updateStampPosition;
		return super.getCommand(cmdId);
	}
	
	/**
	 * Comanda per a actualitzar les variables de posició de la estampa a Java del servidor des del navegador del client.
	 * Envia l'event COMMAND_UPDATE a la classe Java que gestiona la finestra que conté el component
	 */
	private Command _updateStampPosition  = new ComponentCommand (COMMAND_UPDATE, 0) {

		protected void process(AuRequest request) {
			try {
				stampTop=Integer.valueOf(request.getData()[0]);
				stampLeft=Integer.valueOf(request.getData()[1]);
			} catch (Exception e) {
				throw new UiException(e);
			}
		}
		
	};
	
	/*********************BEAN GETTERS AND SETTERS **************************/
	
	
	/**
	 * @return the bgSrc
	 */
	public synchronized String getBgSrc() {
		return bgSrc;
	}
	/**
	 * @param bgSrc the bgSrc to set
	 */
	public synchronized void setBgSrc(String bgSrc) {
		this.bgSrc = bgSrc;
		invalidate();
	}
	/**
	 * @return the bgHeight
	 */
	public synchronized int getBgHeight() {
		return bgHeight;
	}
	/**
	 * @param bgHeight the bgHeight to set
	 */
	public synchronized void setBgHeight(int bgHeight) {
		this.bgHeight = bgHeight;
		invalidate();
	}
	/**
	 * @return the bgWidth
	 */
	public synchronized int getBgWidth() {
		return bgWidth;
	}
	/**
	 * @param bgWidth the bgWidth to set
	 */
	public synchronized void setBgWidth(int bgWidth) {
		this.bgWidth = bgWidth;
		invalidate();
	}
	/**
	 * @return the stampSrc
	 */
	public synchronized String getStampSrc() {
		return stampSrc;
	}
	/**
	 * @param stampSrc the stampSrc to set
	 */
	public synchronized void setStampSrc(String stampSrc) {
		this.stampSrc = stampSrc;
		invalidate();
	}
	/**
	 * @return the stampHeight
	 */
	public synchronized int getStampHeight() {
		return stampHeight;
	}
	/**
	 * @param stampHeight the stampHeight to set
	 */
	public synchronized void setStampHeight(int stampHeight) {
		this.stampHeight = stampHeight;
		invalidate();
	}
	/**
	 * @return the stampWidth
	 */
	public synchronized int getStampWidth() {
		return stampWidth;
	}
	/**
	 * @param stampWidth the stampWidth to set
	 */
	public synchronized void setStampWidth(int stampWidth) {
		this.stampWidth = stampWidth;
		invalidate();
	}
	/**
	 * @return the stampTop
	 */
	public synchronized int getStampTop() {
		return (stampTop!=null)?stampTop.intValue():-1;
	}
	/**
	 * @param stampTop the stampTop to set
	 */
	public synchronized void setStampTop(int stampTop) {
		this.stampTop = new Integer(stampTop);
		
		if(originalStampTop==null)originalStampTop=this.stampTop;

		invalidate();
	}
	/**
	 * @return the stampLeft
	 */
	public synchronized int getStampLeft() {
		return (stampLeft!=null)?stampLeft.intValue():-1;
	}
	/**
	 * @param stampLeft the stampLeft to set
	 */
	public synchronized void setStampLeft(int stampLeft) {
		this.stampLeft = new Integer(stampLeft);
		
		if(originalStampLeft==null)originalStampLeft=this.stampLeft;
		
			
		invalidate();
	}
	

	
	
}
