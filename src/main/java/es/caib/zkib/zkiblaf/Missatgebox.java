/**
 * Reemplacem la MessageBox de zk per eliminar els Modal Windows
 * by Alejandro Usero Ruiz - miércoles 15 de diciembre de 2010, 09:35:25
 */
package es.caib.zkib.zkiblaf;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Window;

/**
 * @author u88683
 *
 */
// Revisió 1.3 - u88683, 22/06/2012 14:34h
//  - Afegim el paràmetre ample a la Missatgebox.info per especificar
//  l'ample desitjat de la finestra
//
// Revisió 1.2 - u88683, 18/01/2010 14:36h
//  - Reemplacem el MessageboxDlg per MissatgeboxDlg, per evitar detach que
//  pot donar problemes en la crida des d'un EventListener
//  - Afegim el mode missatge() que no inclou cap botó
//
// Revisió 1.1 - u88683, 13/01/2010 13:47h
// 	- Añadimos la posibilidad de que los Missatgebox sin confirmación tengan
// 	un listener, para poder ejecutar acciones.
//  - Hacemos que devuelvan un MessageboxDlg para poder sobreescribir funciones
//


public class Missatgebox extends Window {

	private static final long serialVersionUID = 1L;

	private static String _templ = "~./zul/html/missatgebox.zul"; //$NON-NLS-1$

	/** A symbol consisting of a question mark in a circle. */
	public static final String QUESTION = "~./zul/img/question.gif"; //$NON-NLS-1$
	/** A symbol consisting of an exclamation point in a triangle with
	 * a yellow background.
	 */
	public static final String EXCLAMATION  = "~./zul/img/exclamation.gif"; //$NON-NLS-1$
	/** A symbol of a lowercase letter i in a circle.
	 */
	public static final String INFORMATION = "~./zul/img/information.gif"; //$NON-NLS-1$
	/** A symbol consisting of a white X in a circle with a red background. */
	public static final String ERROR = "~./zul/img/error.gif"; //$NON-NLS-1$
	/** Contains no symbols. */
	public static final String NONE = null;

	/** Sense Botons */
	public static final int SENSE_BOTONS = 0x0079;
	
	/** A OK button. */
	public static final int OK = 0x0001;
	/** A Cancel button. */
	public static final int CANCEL = 0x0002;
	/** A Yes button. */
	public static final int YES = 0x0010;
	/** A No button. */
	public static final int NO = 0x0020;
	/** A Abort button. */
	public static final int ABORT = 0x0100;
	/** A Retry button. */
	public static final int RETRY = 0x0200;
	/** A IGNORE button. */
	public static final int IGNORE = 0x0400;
	

	
	/**
	 * Mostra una finestra de missatges amb el missatge indicat
	 * @param missatge
	 */
	public static final MissatgeboxDlg info(String missatge) {
		return show(missatge, null, OK, INFORMATION, 0, null);
	}
	
	/**
	 * Mostra una finestra de missatges amb el missatge indicat i l'amplària indicada
	 * @param missatge
	 */
	public static final MissatgeboxDlg info(String missatge, int ample) {
		HashMap params = new HashMap();
		params.put("width", ample+"px"); //$NON-NLS-1$ //$NON-NLS-2$
		return show(missatge, null, OK, INFORMATION, 0, null, params);
	}	
	
	
	/**
	 * Mostra una finestra de missatges amb el missatge indicat
	 * @param missatge
	 * @param event
	 */
	public static final MissatgeboxDlg info(String missatge, EventListener event) {
		return show(missatge, null, OK, INFORMATION, 0, event);
	}
	
	/**
	 * Mostra una finestra de missatges amb el missatge i el titol indicats
	 * @param missatge
	 * @param titol
	 */
	public static final MissatgeboxDlg info(String missatge, String titol) {
		return show(missatge, titol, OK, INFORMATION, 0, null);
	}
	
	/**
	 * Mostra una finestra de missatges amb el missatge i el titol indicats i 
	 * l'amplària indicada
	 * @param missatge
	 * @param titol
	 */
	public static final MissatgeboxDlg info(String missatge, String titol, int ample) {
		HashMap params = new HashMap();
		params.put("width", ample+"px"); //$NON-NLS-1$ //$NON-NLS-2$
		return show(missatge, titol, OK, INFORMATION, 0, null, params);
	}	
	
	/**
	 * Mostra una finestra de missatges amb el missatge i el titol indicats
	 * @param missatge
	 * @param titol
	 * @param event
	 */
	public static final MissatgeboxDlg info(String missatge, String titol, EventListener event) {
		return show(missatge, titol, OK, INFORMATION, 0, event);
	}	
	
	
	/**
	 * Mostra una finestra de missatges amb el missatge indicat
	 * @param missatge
	 */
	public static final MissatgeboxDlg avis(String missatge) {
		return show(missatge, null, OK, EXCLAMATION, 0, null);
	}
	
	
	/**
	 * Mostra una finestra de missatges amb el missatge indicat
	 * @param missatge
	 * @param event
	 */
	public static final MissatgeboxDlg avis(String missatge, EventListener event) {
		return show(missatge, null, OK, EXCLAMATION, 0, event);
	}
	
	/**
	 * Mostra una finestra de missatges amb el missatge i el titol indicats
	 * @param missatge
	 */
	public static final MissatgeboxDlg avis(String missatge, String titol) {
		return show(missatge, titol, OK, EXCLAMATION, 0, null);
	}	
	
	
	/**
	 * Mostra una finestra de missatges amb el missatge i el titol indicats
	 * @param missatge
	 * @param titol
	 * @param event
	 */
	public static final MissatgeboxDlg avis(String missatge, String titol, EventListener event) {
		return show(missatge, titol, OK, EXCLAMATION, 0, event);
	}	
	
	
	/**
	 * Mostra una finestra d'error amb el missatge indicat
	 * @param missatge
	 */
	public static final MissatgeboxDlg error(String missatge) {
		return show(missatge, null, OK, ERROR, 0, null);
	}
	
	/**
	 * Mostra una finestra d'error amb el missatge indicat
	 * @param missatge
	 * @param event
	 */
	public static final MissatgeboxDlg error(String missatge, EventListener event) {
		return show(missatge, null, OK, ERROR, 0, event);
	}
	
	/**
	 * Mostra una finestra d'error amb el missatge i el titol indicats
	 * @param missatge
	 */
	public static final MissatgeboxDlg error(String missatge, String titol) {
		return show(missatge, titol, OK, ERROR, 0, null);
	}
	
	/**
	 * Mostra una finestra d'error amb el missatge i el titol indicats
	 * @param missatge
	 * @param titol
	 * @param event
	 */
	public static final MissatgeboxDlg error(String missatge, String titol, EventListener event) {
		return show(missatge, titol, OK, ERROR, 0, event);
	}	
	
	/**
	 * Mostra una finestra de confirmació OK-CANCEL amb el missatge indicat
	 * @param missatge
	 */
	public static final MissatgeboxDlg confirmaOK_CANCEL(String missatge, EventListener events) {
		return show(missatge, null, OK|CANCEL, QUESTION, 0, events);
	}
	
	/**
	 * Mostra una finestra de confirmació OK-CANCEL amb el missatge i el titol indicats
	 * @param missatge
	 * @param titol
	 */
	public static final MissatgeboxDlg confirmaOK_CANCEL(String missatge, String titol, EventListener events) {
	   return show(missatge, titol, OK|CANCEL, QUESTION, 0, events);
	}
	
	/**
	 * Mostra una finestra de confirmació OK-CANCEL amb el missatge indicat
	 * @param missatge
	 */
	public static final MissatgeboxDlg confirmaOK_CANCEL(String missatge, EventListener events, String tipoIcono) {
		return show(missatge, null, OK|CANCEL, tipoIcono, 0, events);
	}
	
	/**
	 * Mostra una finestra de confirmació OK-CANCEL amb el missatge i el titol indicats
	 * @param missatge
	 * @param titol
	 */
	public static final MissatgeboxDlg confirmaOK_CANCEL(String missatge, String titol, EventListener events, String tipoIcono) {
	   return show(missatge, titol, OK|CANCEL, tipoIcono, 0, events);
	}	
	
	/**
	 * Mostra una finestra de confirmació YES-NO amb el missatge indicat
	 * @param missatge
	 */
	public static final MissatgeboxDlg confirmaYES_NO(String missatge, EventListener events) {
		return show(missatge, null, YES|NO, QUESTION, 0, events);
	}
	
	/**
	 * Mostra una finestra de confirmació YES-NO amb el missatge, el titol i el tipus d'icona indicats
	 * @param missatge
	 * @param titol
	 */
	public static final MissatgeboxDlg confirmaYES_NO(String missatge, String titol, EventListener events) {
	   return show(missatge, titol, YES|NO, QUESTION, 0, events);
	}
	
	
	/**
	 * Mostra una finestra de confirmació YES-NO amb el missatge i el tipus d'icona indicats
	 * @param missatge
	 */
	public static final MissatgeboxDlg confirmaYES_NO(String missatge, EventListener events, String tipoIcono) {
		return show(missatge, null, YES|NO, tipoIcono, 0, events);
	}
	
	/**
	 * Mostra una finestra de confirmació YES-NO amb el missatge i el titol indicats
	 * @param missatge
	 * @param titol
	 */
	public static final MissatgeboxDlg confirmaYES_NO(String missatge, String titol, EventListener events, String tipoIcono) {
	   return show(missatge, titol, YES|NO, tipoIcono, 0, events);
	}	
	
	/**
	 * Mostra una finestra de missatge sense botons
	 * @param missatge
	 */
	public static final MissatgeboxDlg missatge(String missatge) {
		return show(missatge, null, SENSE_BOTONS, QUESTION, 0, null);
	}
	
	/**
	 * Mostra una finestra de missatge sense botons
	 * @param missatge
	 */
	public static final MissatgeboxDlg missatge(String missatge, EventListener event) {
		return show(missatge, null, SENSE_BOTONS, QUESTION, 0, event);
	}
	
	/**
	 * Mostra una finestra de missatge sense botons amb el missatge i el titol
	 * @param missatge
	 * @param titol
	 */
	public static final MissatgeboxDlg missatge(String missatge, String titol) {
	   return show(missatge, titol, SENSE_BOTONS, QUESTION, 0, null);
	}
	
	/**
	 * Mostra una finestra de missatge sense botons amb el missatge i el titol
	 * @param missatge
	 * @param titol
	 */
	public static final MissatgeboxDlg missatge(String missatge, String titol, EventListener event) {
	   return show(missatge, titol, SENSE_BOTONS, QUESTION, 0, event);
	}
	
	/**
	 * Mostra una finestra de missatge sense botons amb el missatge, el titol i el tipus d'icona indicat
	 * @param missatge
	 * @param titol
	 */
	public static final MissatgeboxDlg missatge(String missatge, String titol, String tipoIcono) {
	   return show(missatge, titol, SENSE_BOTONS, tipoIcono, 0, null);
	}
	
	/**
	 * Mostra una finestra de missatge sense botons amb el missatge, el titol i el tipus d'icona indicat
	 * @param missatge
	 * @param titol
	 */
	public static final MissatgeboxDlg missatge(String missatge, String titol, String tipoIcono, EventListener event) {
	   return show(missatge, titol, SENSE_BOTONS, tipoIcono, 0, event);
	}

	
	/**
	 * Mostra una finestra de confirmació OK amb el missatge indicat
	 * @param missatge
 
	 */
	public static final void confirmaOK(String missatge, EventListener lsnr) {
		show(missatge, null, OK, INFORMATION, 0, lsnr, null);
	}	
	
	/**
	 * Mostra una finestra de confirmació OK amb el missatge i el titol indicats
	 * @param missatge
	 * @param titol
	 */
	public static final void confirmaOK(String missatge, String titol, EventListener lsnr) {
		show(missatge, titol, OK, INFORMATION, 0, lsnr, null);
	}	
	
	
	/**
	 * Mostra una finestra de confirmació OK amb el missatge indicat
	 * @param missatge
 
	 */
	public static final void confirmaYES(String missatge, EventListener lsnr) {
		show(missatge, null, YES, INFORMATION, 0, lsnr, null);
	}	
	
	/**
	 * Mostra una finestra de confirmació OK amb el missatge i el titol indicats
	 * @param missatge
	 * @param titol
	 */
	public static final void confirmaYES(String missatge, String titol, EventListener lsnr) {
		show(missatge, titol, YES, INFORMATION, 0, lsnr, null);
	}	
	
	
	private static final MissatgeboxDlg show(String message, String title,
			int buttons, String icon, int focus, EventListener listener) {
		return show(message, title, buttons, icon, focus, listener, null);		
	}
	
	private static final MissatgeboxDlg show(String message, String title,
			int buttons, String icon, int focus, EventListener listener, Map params)
	{
		if (params == null) 
		/*final Map */params = new HashMap();
		params.put("message", message); //$NON-NLS-1$
		params.put("title", title != null ? title : null); //$NON-NLS-1$
		params.put("icon", icon); //$NON-NLS-1$
		if (buttons != SENSE_BOTONS) {
			params.put("buttons", new Integer((buttons & (OK | CANCEL | YES //$NON-NLS-1$
					| NO | ABORT | RETRY | IGNORE)) != 0 ? buttons : OK));
			if ((buttons & OK) != 0)
				params.put("OK", new Integer(OK)); //$NON-NLS-1$
			if ((buttons & CANCEL) != 0)
				params.put("CANCEL", new Integer(CANCEL)); //$NON-NLS-1$
			if ((buttons & YES) != 0)
				params.put("YES", new Integer(YES)); //$NON-NLS-1$
			if ((buttons & NO) != 0)
				params.put("NO", new Integer(NO)); //$NON-NLS-1$
			if ((buttons & RETRY) != 0)
				params.put("RETRY", new Integer(RETRY)); //$NON-NLS-1$
			if ((buttons & ABORT) != 0)
				params.put("ABORT", new Integer(ABORT)); //$NON-NLS-1$
			if ((buttons & IGNORE) != 0)
				params.put("IGNORE", new Integer(IGNORE)); //$NON-NLS-1$
		} else {
			params.put("SENSE_BUTONS", new Boolean(true)); //$NON-NLS-1$
		}

		final MissatgeboxDlg dlg = (MissatgeboxDlg) Executions.createComponents(
				_templ, null, params);
		dlg.setButtons(buttons);
		dlg.setEventListener(listener);
		if (focus > 0)
			dlg.setFocus(focus);

		dlg.doHighlighted();
		return dlg;
	}	

	
	/** Sets the template used to create the message dialog.
	 *
	 * <p>The template must follow the default template:
	 * ~./zul/html/messagebox.zul
	 *
	 * <p>In other words, just adjust the label and layout and don't
	 * change the component's ID.
	 */
	public static void setTemplate(String uri) {
		if (uri == null || uri.length() == 0)
			throw new IllegalArgumentException("empty"); //$NON-NLS-1$
		_templ = uri;
	}
	/** Returns the template used to create the message dialog.
	 */
	public static String getTemplate() {
		return _templ;
	}	
}