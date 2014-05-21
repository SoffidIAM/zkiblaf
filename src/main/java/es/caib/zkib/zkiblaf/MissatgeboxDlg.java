/**
 * Reemplacem MessageBoxDlg de zk per eliminar els Modal Windows
 * by Alejandro Usero Ruiz - miércoles 18 de enero de 2010, 14:33:25
 * 
 * - Versió 1.0.1: 25/02/2011 - u88683
 *   Fem que quan es tanca la finestra es genere un event onClose 
 *   
 */
package es.caib.zkib.zkiblaf;

import org.zkoss.mesg.Messages;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import org.zkoss.zul.mesg.MZul;

public class MissatgeboxDlg extends Window {
	private static final long serialVersionUID = 1L;
	/** A OK button. */
	public static final int OK = Messagebox.OK;
	/** A Cancel button. */
	public static final int CANCEL = Messagebox.CANCEL;
	/** A Yes button. */
	public static final int YES = Messagebox.YES;
	/** A No button. */
	public static final int NO = Messagebox.NO;
	/** A Abort button. */
	public static final int ABORT = Messagebox.ABORT;
	/** A Retry button. */
	public static final int RETRY = Messagebox.RETRY;
	/** A IGNORE button. */
	public static final int IGNORE = Messagebox.IGNORE;

	/** What buttons are allowed. */
	private int _buttons;
	/** Which button is pressed. */
	private int _result;
	/** The event lisetener. */
	private EventListener _listener;

    protected Integer _mutex=new Integer(0);
	
	public void onOK() {
		if ((_buttons & OK) != 0) endModal(OK);
		else if ((_buttons & YES) != 0) endModal(YES);
		else if ((_buttons & RETRY) != 0) endModal(RETRY);
	}
	public void onCancel() {
		if (_buttons == OK) endModal(OK);
		else if ((_buttons & CANCEL) != 0) endModal(CANCEL);
		else if ((_buttons & NO) != 0) endModal(NO);
		else if ((_buttons & ABORT) != 0) endModal(ABORT);
	}

	/** Sets what buttons are allowed. */
	public void setButtons(int buttons) {
		_buttons = buttons;
	}
	/** Sets the event listener.
	 * @param listener the event listener. If null, no invocation at all.
	 * @since 3.0.4
	 */
	public void setEventListener(EventListener listener) {
		_listener = listener;
	}
	/** Sets the focus.
	 * @param button the button to gain the focus. If 0, the default one
	 * (i.e., the first one) is assumed.
	 * @since 3.0.0
	 */
	public void setFocus(int button) {
		if (button > 0) {
			final Button btn = (Button)getFellowIfAny("btn" + button); //$NON-NLS-1$
			if (btn != null)
				btn.focus();
		}
	}

	public void doModal() throws InterruptedException, SuspendNotAllowedException {	
			doHighlighted();
			invalidate();
			setVisible(true); //if MODAL, it must be visible; vice versa
			
			try {
				enterModal();
			} catch (SuspendNotAllowedException ex) {
				throw ex;
			}
		
		
	}
	/** Set mode to MODAL and suspend this thread. */
	private void enterModal() throws InterruptedException {
		//no need to synchronized (_mutex) because no racing is possible
		Executions.wait(_mutex);
	}
	/** Called only internally.
	 */
	public void endModal(int button) {
		_result = button;
		Executions.notifyAll(_mutex);
		
		try {
			detach();
		} catch (UiException ex) {
			// Evitamos que aparezca el error que ocurre al cerrar sesión
			if (!"You cannot access components belong to other desktop".equals(ex.getMessage())) throw ex; //$NON-NLS-1$
		}
	}
	/** Returns the result which is the button being pressed.
	 */
	public int getResult() {
		return _result;
	}
	
	

	/**
	 * Represents a button on the message box.
	 * @since 3.0.0
	 */
	public static class Button extends org.zkoss.zul.Button {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int _button;
		private String _evtnm;

		/** Sets the identity.
		 */
		public void setIdentity(int button) {
			_button = button;

			final int label;
			switch (button) {
			case YES:
				label = MZul.YES;
				_evtnm = "onYes"; //$NON-NLS-1$
				break;
			case NO:
				label = MZul.NO;
				_evtnm = "onNo"; //$NON-NLS-1$
				break;
			case RETRY:
				label = MZul.RETRY;
				_evtnm = "onRetry"; //$NON-NLS-1$
				break;
			case ABORT:
				label = MZul.ABORT;
				_evtnm = "onAbort"; //$NON-NLS-1$
				break;
			case IGNORE:
				label = MZul.IGNORE;
				_evtnm = "onIgnore"; //$NON-NLS-1$
				break;
			case CANCEL:
				label = MZul.CANCEL;
				_evtnm = "onCancel"; //$NON-NLS-1$
				break;
			default:
				label = MZul.OK;
				_evtnm = "onOK"; //$NON-NLS-1$
				break;
			}
			setLabel(Messages.get(label));
			setId("btn" + _button); //$NON-NLS-1$
		}
		public void onClick() throws Exception {
			final MissatgeboxDlg dlg = (MissatgeboxDlg)getSpaceOwner();
			if (dlg._listener != null) {
				final Event evt = new Event(_evtnm, dlg, new Integer(_button));
				dlg._listener.onEvent(evt);
				if (!evt.isPropagatable())
					return; //no more processing
			}
			dlg.endModal(_button);
		}
	}



	// Sobreescribim el onclose, per generar un event
	// u88683 - 25/02/2001
	public void onClose() {
		super.onClose();
		// Creem l'event
		try {
		if (_listener != null) {
			final Event evt = new Event("onClose", this); //$NON-NLS-1$
			_listener.onEvent(evt);
			if (!evt.isPropagatable())
				return; //no more processing
		}	
		} catch (Throwable th) {
			//No fem res
		}
	}
}
