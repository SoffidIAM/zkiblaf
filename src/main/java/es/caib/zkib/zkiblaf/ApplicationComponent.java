package es.caib.zkib.zkiblaf;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.zkoss.mesg.Messages;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import es.caib.zkib.component.DataModel;
import es.caib.zkib.datasource.CommitException;
import es.caib.zkib.events.SerializableEventListener;

import org.zkoss.zk.ui.Path;

/**
 * @author u88683
 *
 */
public class ApplicationComponent extends Vbox {
	String initialPage;
	String logoutPage;
	String name;
	String menu;
	String favorits; //ruta del menu favorits
	boolean embed = false;
	public boolean isEmbed() {
		return embed;
	}

	public void setEmbed(boolean embed) {
		this.embed = embed;
	}

	/*
	 * Indica si es mostren els favorits. Per defecte amagats -- u88683
	 */
	boolean showFavorites = false;
	
	/*
	 * Indica si s'ha de mostrar o no el perfil de l'usuari (Es mostra a la consola,
	 * s'amaga al selfservice
	 */
	boolean showProfile = true;
	
	/*
	 * Indica si al pitjar F5 s'ha de recarregar la darrera pàgina oberta (per
	 * defecte desactivat) -- u88683
	 */
	private boolean autoReload = false;
	
	/*
	 * Incica si acceptem que les finestres s'obren en una altra pestanya al
	 * polsar Ctrl. Per defecte desactivat -- u88683
	 */
	private boolean allowOpenMultipleTabs = false;
	
	/**
	 * Indica el titol que es posarà a la finestra del a navegador com a prefixe
	 * en el format "mainWindowTitle - Titol de la frame actual" -- u88683
	 */
	private String mainWindowTitle = null; 
	
	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getName() {
		return name;
	}

	public void setName(String appName) {
		this.name = appName;
	}

	public String getLogoutPage() {
		return logoutPage;
	}

	public void setLogoutPage(String logoutPage) {
		this.logoutPage = logoutPage;
	}

	private Component workArea;
	private Window window;
	private Label titleLabel;
	private String template = "~./zkiblaf/zul/application.zul";
	
	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	Stack stack = new Stack ();
	
	public Stack getStack() {
		return stack;
	}

	public void setStack(Stack stack) {
		this.stack = stack;
	}

	public String getInitialPage() {
		return initialPage;
	}

	public void setInitialPage(String initialPage) {
		this.initialPage = initialPage;
	}
	
	public void setShowProfile(boolean showProfile){
		this.showProfile = showProfile;
	}
	
	public boolean isShowProfile(){
		return showProfile;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private void registerSaveEvent ()
	{
		addEventListener("onCtrlKey", new CtrlKeyEventListener());
	}
	
	class CtrlKeyEventListener implements EventListener, Serializable {
		private static final long serialVersionUID = 1L;

		public void onEvent(Event event) throws Exception {
			FrameInfo frame = Application.getActiveFrame();
			try {
				doCommit (frame.component);
			} catch (CommitException e) {
				throw new UiException (e);				
			}
		}

		private void doCommit(Component frame) throws CommitException {
			if (frame instanceof DataModel)
				((DataModel) frame).commit();
			else
			{
				for (Component child: (Collection<Component>)frame.getChildren())
					doCommit(child);
			}
		}
		
	}
	
	public void onCreate () {
		Application.registerApplication (this);
		
		setCtrlKeys("^S@S");
		registerSaveEvent();
		HashMap hmap = new HashMap();
		hmap.put("showProfile", new Boolean (showProfile));
		hmap.put("embed", new Boolean (embed));
		window = (Window) Executions.createComponents(template, this, hmap); //$NON-NLS-1$
		if (window != getChildren().get(0))
			this.insertBefore(window, (Component) getChildren().get(0));
		workArea = window.getFellow("workArea"); //$NON-NLS-1$
		titleLabel = (Label) window.getFellow("title"); //$NON-NLS-1$
		titleLabel.setSclass("maintitle");//Posem estils css //$NON-NLS-1$
		if (initialPage != null) {
			// Verifiquem última pàgina carregada a la sessió (si existeix)
			Object paginaActual = null;
			// Implementem el recarrega() per recarregar darrera pàgina cargada (u88683)
			// només si és actiu el mode autoReload
			if (isAutoReload() && getDesktop().getSession()!=null &&
					((paginaActual = getDesktop().getSession().getAttribute("paginaActual"))!=null)) { //$NON-NLS-1$
				Application.setPage((String)paginaActual);
			} else { // Si no en tenim cap pàgina de sessió, ens situem a la pàgina Inicial
				Application.setPage(initialPage);
			}
		}
		Component button = window.getFellowIfAny("logoutButton"); //$NON-NLS-1$

		if (button == null)
		{
				button = window.getFellowIfAny("otherlogoutButton");
		}
		if (button != null)
			button.addEventListener("onClick", new SerializableEventListener() { //$NON-NLS-1$
			public void onEvent(Event event) throws Exception {
				Missatgebox.confirmaOK_CANCEL(
					Labels.getLabel("zkiblaf.tancarSessioConfirm"), //$NON-NLS-1$
					Labels.getLabel("zkiblaf.tancarSessioTitle"), //$NON-NLS-1$
					new EventListener() {

						public void onEvent(Event event) throws Exception {
							if ("onOK".equals(event.getName())) { //$NON-NLS-1$
								Execution ex = Executions.getCurrent();
								
								HttpServletRequest req = (HttpServletRequest) ex.getNativeRequest();
								HttpServletResponse resp = (HttpServletResponse) ex.getNativeResponse();
								try {
									Class c = Class.forName("es.caib.loginModule.util.SessionManager"); //$NON-NLS-1$
									
									Object sessionManager = c.newInstance();
									Method m = c.getMethod("endSession", //$NON-NLS-1$
										new Class [] {
											HttpServletRequest.class,
											HttpServletResponse.class
										});
									
									m.invoke(sessionManager, 
										new Object[] {
											req,
											resp
										});
									HttpSession httpSession = req.getSession();
									httpSession.invalidate();
								} catch (Exception e) {
									
								}
								
								Executions.sendRedirect(logoutPage);
							}
						}
						
					});						
							
			}
		});

		if (! embed)
		{
			Component menubutton = window.getFellow("menuButton"); //$NON-NLS-1$
			menubutton.addEventListener("onClick", new EventListener() { //$NON-NLS-1$
				public void onEvent(Event event) throws Exception {
					if (getMenu() == null)
					{
						throw new UiException(es.caib.zkib.zkiblaf.Messages.getString("ApplicationComponent.MenuItemSelectedError")); //$NON-NLS-1$
					}
					Window menu = (Window) Path.getComponent(ApplicationComponent.this.getSpaceOwner(), getMenu());
					menu.setTop("29px"); //$NON-NLS-1$
					menu.setLeft("5px"); //$NON-NLS-1$
					if(!menu.isVisible())
						menu.doPopup();
				}
			});
		}
		if (isShowFavorites()) {
			Component favoritswindow = window.getFellow("favoritsButton"); //$NON-NLS-1$
			favoritswindow.addEventListener("onClick", new EventListener() { //$NON-NLS-1$
				public void onEvent(Event event) throws Exception {
					if (getFavorits() == null)
					{
						throw new UiException(es.caib.zkib.zkiblaf.Messages.getString("ApplicationComponent.FavoriteAttributeNotEspecifiedError")); //$NON-NLS-1$
					}
					Window favs = (Window) Path.getComponent(ApplicationComponent.this.getSpaceOwner(), getFavorits());
					favs.setTop("29px"); //$NON-NLS-1$
					favs.setLeft("80px"); //$NON-NLS-1$
					favs.doPopup();
				}
			});		
		}

	}

	public void setTitle(String title) {
		if (name != null)
			titleLabel.setValue(
					String.format(es.caib.zkib.zkiblaf.Messages.getString("ApplicationComponent.TwoArgsSepparator"), name, title)); //$NON-NLS-1$
		else
			titleLabel.setValue(title);
		
		/*
		 * Si el mainWindowTitle té valor: afegim el titol de la frame actual al
		 * títol de la finestra del navegador  -- u88683
		 * Es posa primer el titol de la frame abans del titol de l'aplicació 
		 */
		if (mainWindowTitle!=null) {
			org.zkoss.zk.ui.Page index = getDesktop().getPage("index"); //$NON-NLS-1$
			if (index != null)
				index.setTitle(
						String.format(es.caib.zkib.zkiblaf.Messages.getString("ApplicationComponent.TwoArgsSepparator"), title, mainWindowTitle));  //$NON-NLS-1$
		}
	}
	
	public Include newFrame (String url)
	{
		for ( Iterator it= workArea.getChildren().iterator(); it.hasNext();)
		{
			Include inc = (Include) it.next();
			if (inc.getSrc() == null)
			{
				inc.setSrc(url);
				return inc;
			}
		}
		throw new UiException(es.caib.zkib.zkiblaf.Messages.getString("ApplicationComponent.TooManyFramesError")); //$NON-NLS-1$
	}

	public void clearFrames ()
	{
		for ( Iterator it= workArea.getChildren().iterator(); it.hasNext();)
		{
			Include inc = (Include) it.next();
			inc.setSrc(null);
		}
	}
	
	public void deleteLastFrame ()
	{
		Include lastInc = null;
		for ( Iterator it= workArea.getChildren().iterator(); it.hasNext();)
		{
			Include inc = (Include) it.next();
			if (inc.getSrc() == null)
				break;
			else
				lastInc = inc;
		}
		if (lastInc != null)
			lastInc.setSrc(null);
	}

	public boolean isShowFavorites() {
		return showFavorites;
	}

	public void setShowFavorites(boolean showFavorites) {
		this.showFavorites = showFavorites;
	}

	public String getFavorits() {
		return favorits;
	}

	public void setFavorits(String favorits) {
		this.favorits = favorits;
	}

	public boolean isAllowOpenMultipleTabs() {
		return allowOpenMultipleTabs;
	}

	public void setAllowOpenMultipleTabs(boolean allowOpenMultipleTabs) {
		this.allowOpenMultipleTabs = allowOpenMultipleTabs;
	}

	public boolean isAutoReload() {
		return autoReload;
	}

	public void setAutoReload(boolean autoReload) {
		this.autoReload = autoReload;
	}

	public String getMainWindowTitle() {
		return mainWindowTitle;
	}

	public void setMainWindowTitle(String mainWindowTitle) {
		this.mainWindowTitle = mainWindowTitle;
	}

}
