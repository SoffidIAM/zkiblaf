package es.caib.zkib.zkiblaf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;


public class Application {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static FrameInfo activeFrame = null;

	public static void registerPage(Component f) {
		Stack s = getStack();
		if (s != null && ! s.isEmpty()) {
			Frameable frame = null;
			if ( f instanceof Frameable )
				frame = (Frameable) f;
			
			FrameInfo fi = (FrameInfo) s.peek();
			if (fi != null)
			{
				fi.component = f;
				fi.frame = frame;
				if (fi.frame != null)
					setTitle(fi.frame.getTitle());
			}
		}
	}

	private static Stack getStack() {
		return getApplication().getStack();
	}
	
	public static void setPage (String page)
	{
		if (page != null) {
			Stack s = getStack ();
			ApplicationComponent app = getApplication();
			while (! s.isEmpty())
			{
				FrameInfo fi = (FrameInfo) s.peek();
				if (fi.frame != null)
				{
					if (fi.component!=null) Events.sendEvent(new Event ("onPrepareClose", fi.component)); //$NON-NLS-1$
					if (!fi.frame.canClose())
						return ;
				}
				// Comprobem que els components frameable de la frame actual es poden tancar
				if (!fi.checkCanClose()) return;
				s.pop();
				app.deleteLastFrame();
			}
			
			activatePage(page);
		}
	}

	public static void jumpTo (String page)
	{
		if (page != null) {
			Stack s = getStack ();
			ApplicationComponent app = getApplication();
			if (! s.isEmpty())
			{
				FrameInfo fi = (FrameInfo) s.peek();
				if (fi.frame != null)
				{
					Events.sendEvent(new Event ("onPrepareClose", fi.component)); //$NON-NLS-1$
					if (!fi.frame.canClose())
						return ;
				}
				s.pop();
				app.deleteLastFrame();
			}
			
			activatePage(page);
		}
	}

	private static void activatePage(String page) {
		FrameInfo fi;
		ApplicationComponent app = getApplication();
		
		Stack s = getStack();
		
		fi = new FrameInfo();
		fi.frame = null;
		fi.url = page;
		
		s.push(fi);
		
		setTitle(Labels.getLabel("main.loading")); //$NON-NLS-1$

		app.newFrame(page);
		// Guardem la pàgina actual en la sessió
		// només si és actiu el modo autoReload a l'aplicació
		if (getApplication()!=null && getApplication().isAutoReload()) {
			Session zksessio = getApplication().getDesktop().getSession();
			if (zksessio != null) {
				// el guardem per al possible reload (F5)
				zksessio.setAttribute("paginaActual", page); //$NON-NLS-1$
			}
		}

		// Guardamos el Frameinfo actual como estático de la clase
		activeFrame = fi;
	}

	public static void setTitle(String title) {
		// comprovem que no siga null
		if (title != null)
			getApplication().setTitle(title);
	}

	public static void call (String page)
	{
		Stack s = getStack();
		if (s.size() > 0)
		{
			FrameInfo fi = (FrameInfo) s.peek();
			if (fi.component!=null) 
				fi.component.setVisible(false);
			else {// cas dels zuls de SEU
				if (!fi.checkCanClose()) return; //Verifiquem canvis pendents
				getApplication().deleteLastFrame(); //Esborrem frame per tal que no se superpose
			}

			activatePage(page);
		}
	}


	public static ApplicationComponent getApplication() {
		Execution ex = Executions.getCurrent();
		ApplicationComponent app = (ApplicationComponent) ex.getDesktop().getAttribute("zkiblaf$application"); //$NON-NLS-1$
		return app;
	}
	
	public static boolean isFavoritesVisible() {
		ApplicationComponent app = getApplication();
		if (app !=null)
			return app.isShowFavorites();
		return false;
	}

	public static void goBack ()
	{
		Stack s = getStack();
		if (s.size() > 1)
		{
			FrameInfo fi = (FrameInfo) s.peek();
			if (fi != null && fi.frame != null)
			{
				Events.sendEvent(new Event ("onPrepareClose", fi.component)); //$NON-NLS-1$
				if (!fi.frame.canClose())
					return ;
			}
			s.pop();
			ApplicationComponent app = getApplication();
			app.deleteLastFrame();
			fi = (FrameInfo) s.peek();
			if (fi.component!=null) 
				fi.component.setVisible(true);
			if (fi.frame != null)
				setTitle(fi.frame.getTitle());
			if (fi.component!=null) 
				Events.sendEvent(new Event ("onReturn", fi.component)); //$NON-NLS-1$
		}else if(s.size()==1){
			//com que no tenim historial, tornem a la pagina principal de l'aplicacio
			if(Application.getApplication().getInitialPage()!=null)
			{
				Application.jumpTo(Application.getApplication().getInitialPage());
				Events.postEvent("onExit", getApplication(), null);
			}
		}
	}

	public static void registerApplication(
			ApplicationComponent applicationComponent) {
		Execution ex = Executions.getCurrent();
		ex.getDesktop().setAttribute("zkiblaf$application", applicationComponent); //$NON-NLS-1$
	}

	public static FrameInfo getActiveFrame() {
		return activeFrame;
	}
	

}

class FrameInfo {
	String url;
	String title;
	Frameable frame;
	Component component;
	ArrayList framesCanClose = new ArrayList();
	
	public void addFrameCheckCanClose(Frameable frame) {
		framesCanClose.add(frame);
	}
	
	public boolean checkCanClose() {
		// Cridem al mètode canclose dels components que implementen frameable
		for (Iterator it = framesCanClose.iterator(); it.hasNext();) {
			if (!((Frameable) it.next()).canClose()) return false;
		}
		return true;
	}
}
