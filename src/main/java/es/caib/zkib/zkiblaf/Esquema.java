package es.caib.zkib.zkiblaf;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Box;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import es.caib.zkib.component.DataModel;
import es.caib.zkib.datasource.CommitException;

public class Esquema extends Window implements AfterCompose, Frameable
{
	/**
	 * u88683: ajustem el tamany automàticament (no tenim mida fixa)
	 * 
	 * - Permitimos que no haya ventana de criteris (u88683) [atributo senseCriteris]
	 * 16/06/2011 - Afegim setFocusCriteri per indicar quin criteri ha de tindre el focus
	 * inicial (u88683) 02/02/2012
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dataModel;
	private boolean _created = false;
	HtmlBasedComponent criteris;
	HtmlBasedComponent llista;
	HtmlBasedComponent formulari;
	Box criterisHolder;
	Box llistaHolder;
	Box formulariHolder;
	public boolean saveContent;
	String ample = "98%"; //$NON-NLS-1$
	Toolbar opt_toolbar; // Option toolbar
	Toolbarbutton clear_criteria; // Clear search parameters
	ImageClic img_clear;
	boolean senseCriteris = false; // Allow no window criteria
	private final String IMG_CLEAR_CRITERIA = "~./img/reload-petit16.png"; //$NON-NLS-1$

	public boolean isSaveContent ()
	{
		return saveContent;
	}

	public void setSaveContent (boolean saveContent)
	{
		this.saveContent = saveContent;
	}

	public boolean canClose = true;

	public boolean canClose ()
	{
		boolean result = false;
		if (isCommitPending())
		{
			try
			{
				result = Missatgebox.confirmaYES_NO(Labels.getLabel("task.msgDeseaSalir"), //$NON-NLS-1$
						Labels.getLabel("task.titleDeseaSalir"), //$NON-NLS-1$
						Messagebox.QUESTION);

				if (result)
				{
					Component c = getModelDades();// Path.getComponent(getSpaceOwner(), dataModel);
					if (c != null && c instanceof DataModel)
					{
						DataModel dm  =(DataModel) c;
						dm.refresh();
					}
				}
				return result;
			}
			catch (Exception ex)
			{
				// Si hay error salimos sin guardar
				return true;
			}
		}
		else
		{
			return true;
		}
	}

	public void setCanClose (boolean canClose)
	{
		this.canClose = canClose;
	}

	private void prepareSchema ()
	{
		this.setWidth(ample);

		// Part d'adalt; criteris
		Hbox hbox = new Hbox();
		hbox.setWidth("100%"); //$NON-NLS-1$
		super.insertBefore(hbox, null);

		criterisHolder = new Vbox();
		hbox.appendChild(criterisHolder);
		criterisHolder.setWidth("100%");

		HtmlBasedComponent criterisCap = new Hbox();
		criterisHolder.appendChild(criterisCap);
		Label titol = new Label();
		titol.setValue(Messages.getString("Esquema.SearchCritera")); //$NON-NLS-1$
		titol.setSclass("titol_capsa"); //$NON-NLS-1$
		criterisCap.appendChild(titol);

		defineToolbarElements();

		HtmlBasedComponent show_criteria_opt = new Vbox();
		show_criteria_opt.setWidth("100%"); //$NON-NLS-1$
		criterisHolder.appendChild(show_criteria_opt);
		show_criteria_opt.appendChild(opt_toolbar);

		hbox.setVisible(!isSenseCriteris());

		// Part d'enmig; llista i formulari
		hbox = new Hbox();
		hbox.setWidth("100%"); //$NON-NLS-1$
		super.insertBefore(hbox, null);

		// Llista
		llistaHolder = new Vbox();
		hbox.appendChild(llistaHolder);
		titol = new Label();
		titol.setValue(Messages.getString("Esquema.Browser")); //$NON-NLS-1$
		titol.setSclass("titol_capsa"); //$NON-NLS-1$
		llistaHolder.appendChild(titol);
		// Formulari
		formulariHolder = new Vbox();
		hbox.appendChild(formulariHolder);
		Hbox headerBox = new Hbox();
		headerBox.setWidth("100%"); //$NON-NLS-1$
		formulariHolder.appendChild(headerBox);
		titol = new Label();
		titol.setValue(Messages.getString("Esquema.DetailsLabel")); //$NON-NLS-1$
		titol.setSclass("titol_capsa"); //$NON-NLS-1$
		headerBox.appendChild(titol);
		ImageClic botoTancar = new ImageClic();
		botoTancar.setSrc("~./img/tanca.png"); //$NON-NLS-1$
		botoTancar.setAlign("right"); //$NON-NLS-1$
		botoTancar.setTooltiptext(Messages.getString("Esquema.CloseDetail")); //$NON-NLS-1$
		botoTancar.addEventListener("onClick", new EventListener() { //$NON-NLS-1$
					public boolean isAsap ()
					{
						return true;
					};

					public void onEvent (org.zkoss.zk.ui.event.Event event)
					{
						event.stopPropagation();
						hideFormulari();
					};
				});
		headerBox.appendChild(botoTancar);
		formulariHolder.setVisible(false);
	}

	/**
	 * @brief Define toolbar elements
	 * 
	 *        Implements the functionality to create a toolbar and define the components.
	 */
	private void defineToolbarElements ()
	{
		clear_criteria = new Toolbarbutton();
		clear_criteria.setLabel(Messages.getString("Esquema.ClearCriteria")); //$NON-NLS-1$
		clear_criteria.addEventListener("onClick", new EventListener() //$NON-NLS-1$
				{
					public boolean isAsap ()
					{
						return true;
					};

					public void onEvent (org.zkoss.zk.ui.event.Event event)
					{
						recursiveClear(criteris);
					}
				});

		img_clear = new ImageClic(IMG_CLEAR_CRITERIA);
		img_clear.addEventListener("onClick", new EventListener() //$NON-NLS-1$
				{
					public boolean isAsap ()
					{
						return true;
					}

					public void onEvent (org.zkoss.zk.ui.event.Event event) throws Exception
					{
						recursiveClear(criteris);
					}
				});

		// Add elements to toolbar
		opt_toolbar = new Toolbar();
		opt_toolbar.appendChild(img_clear);
		opt_toolbar.appendChild(clear_criteria);
		opt_toolbar.setClass("toolbar"); //$NON-NLS-1$
	}

	/**
	 * @brief Clear search parameters
	 * 
	 *        Implements the functionality to search and delete the content on textbox of
	 *        search component.
	 * @param criteris
	 *          Page component to search elements to clear.
	 */
	private void recursiveClear (AbstractComponent criteris)
	{
		for (Object c : criteris.getChildren())
		{
			if (c instanceof Textbox)
			{
				((Textbox) c).setValue(""); //$NON-NLS-1$
			}
			else
				recursiveClear((AbstractComponent) c);
		}
	};

	public void finestraAvis ()
	{
		Window window = (Window) Executions.createComponents(
				"~./zkiblaf/zul/avisSortir.zul", this.getParent(), null); //$NON-NLS-1$
		Events.postEvent("onInicia", window, this); //$NON-NLS-1$
	}

	public void updateCanvis ()
	{
		if (dataModel != null)
		{
			Component c = getModelDades();// Path.getComponent(getSpaceOwner(), dataModel);
			if (c != null && c instanceof DataModel)
			{
				DataModel dm = (DataModel) c;
				try
				{
					dm.commit();
				}
				catch (CommitException e)
				{
					throw new UiException(e);
				}
			}
		}
	}

	public boolean isCommitPending ()
	{
		Component c = getModelDades();// Path.getComponent(getSpaceOwner(), dataModel);
		if (c != null && c instanceof DataModel)
		{
			return ((DataModel) c).isCommitPending();
		}
		else
		{
			return false;
		}
	}

	public String getDatamodel ()
	{
		return dataModel;
	}

	public void setDatamodel (String listbox)
	{
		this.dataModel = listbox;
		if (_created)
			installHooks();
	}

	protected void installHooks ()
	{
		Component c = Path.getComponent(getSpaceOwner(), dataModel);
		if (c != null && c instanceof DataModel)
		{
			c.addEventListener("onChange", new EventListener() { //$NON-NLS-1$
						public boolean isAsap ()
						{
							return false;
						}

						public void onEvent (org.zkoss.zk.ui.event.Event event)
						{
							checkCommitPending();
						};
					});
			c.addEventListener("onCommit", new EventListener() { //$NON-NLS-1$
						public boolean isAsap ()
						{
							return false;
						}

						public void onEvent (org.zkoss.zk.ui.event.Event event)
						{
							checkCommitPending();
						};
					});
		}
		checkCommitPending();
	}

	protected void checkCommitPending ()
	{
		Component c = Path.getComponent(getSpaceOwner(), dataModel);
		if (c != null && c instanceof DataModel)
			setVisible(((DataModel) c).isCommitPending());
		else
			setVisible(false);
	}

	public boolean insertBefore (Component arg0, Component arg1)
	{
		if (criterisHolder == null)
			prepareSchema();

		if (criteris == null)
		{
			criteris = (HtmlBasedComponent) arg0;
		}
		else if (llista == null)
		{
			llista = (HtmlBasedComponent) arg0;
		}
		else if (formulari == null)
		{
			formulari = (HtmlBasedComponent) arg0;
		}
		return super.insertBefore(arg0, arg1);
		// return true;
	}

	public void showFormulari ()
	{
		Event ev2 = new Event("onShowFormulari", this); //$NON-NLS-1$
		Events.sendEvent(this, ev2);
		String thisUnits = getUnits(getAmple());
		String ancho = getAmple().substring(0, getAmple().indexOf(thisUnits));
		try {
			if (! "%".equals(thisUnits))
			{
				int anchoDiv2 = (int) ((Integer.parseInt(ancho) - 10) / 2);
				llistaHolder.setWidth(anchoDiv2 + thisUnits); 
				llista.setWidth(anchoDiv2 + thisUnits);
				formulariHolder.setWidth(anchoDiv2 + thisUnits);
			} else {
				llistaHolder.setWidth("50%"); 
				llista.setWidth("100%");
				formulariHolder.setWidth("50%");
			}
		} catch (NumberFormatException e) {
			llistaHolder.setWidth("50%"); 
			llista.setWidth("100%");
			formulariHolder.setWidth("50%");
		}
		llistaHolder.invalidate();
		formulariHolder.setVisible(true);
	}

	public void hideFormulari ()
	{
		Event ev2 = new Event("onHideFormulari", Esquema.this); //$NON-NLS-1$
		Events.sendEvent(Esquema.this, ev2);
		formulariHolder.setVisible(false);
		llistaHolder.setWidth(getAmple());
		llista.setWidth(getAmple());
		llistaHolder.invalidate();
	}

	private String getUnits (String s)
	{
		if (s == null)
			return ""; //$NON-NLS-1$
		if (s.endsWith("%")) //$NON-NLS-1$
			return "%"; //$NON-NLS-1$
		if (s.endsWith("px")) //$NON-NLS-1$
			return "px"; //$NON-NLS-1$
		if (s.endsWith("pt")) //$NON-NLS-1$
			return "pt"; //$NON-NLS-1$
		return ""; //$NON-NLS-1$
	}

	public void afterCompose ()
	{
		if (criteris != null)
		{
			criteris.setParent(criterisHolder);
			if (idComponentFocus != null)
				ponFocusFill(criteris.getChildren(), idComponentFocus);
		}
		if (llista != null)
		{
			llista.setParent(llistaHolder);
		}
		if (formulari != null)
		{
			formulari.setParent(formulariHolder);
		}

		// Ens establim com a component frameable (canclose)
		if (Application.getActiveFrame() != null)
		{
			Application.getActiveFrame().addFrameCheckCanClose(this);
		}

	}

	private Component getModelDades ()
	{
		Component comp = null;
		try
		{
			if (dataModel != null)
				comp = Path.getComponent(getSpaceOwner(), dataModel);
		}
		catch (Exception ex)
		{
		}
		if (comp == null)
		{
			try
			{
				comp = getPage().getFellowIfAny("model"); //TODO: se obtiene por siempre el mismo id: arreglar //$NON-NLS-1$
			}
			catch (Exception ex)
			{
			}
		}
		return comp;
	}

	public String getAmple ()
	{
		return ample;
	}

	public void setAmple (String ample)
	{
		this.ample = ample;
	}

	public String getWidth ()
	{
		return getAmple();
	}

	public void setWidth (String width)
	{
		super.setWidth(width);
	}

	public boolean isSenseCriteris ()
	{
		return senseCriteris;
	}

	public void setSenseCriteris (boolean senseCriteris)
	{
		this.senseCriteris = senseCriteris;
	}

	/**
	 * Iterem en els fills fins que trobem un amb el id corresponent i li posem el focus.
	 * by Alejandro Usero Ruiz - 02/02/2012 14:20h
	 * 
	 * @param fills
	 * @param idComponentFocus
	 */
	private void ponFocusFill (List fills, String idComponentFocus)
	{
		if (fills == null || idComponentFocus == null)
			return;

		try
		{
			for (Iterator it = fills.iterator(); it.hasNext();)
			{
				Component c = (Component) it.next();
				if (idComponentFocus.equals(c.getId()))
				{
					// Fem cas especial als macros: mirem TOTS els seus fills
					// fins que trobem el primer textbox
					if (c instanceof HtmlMacroComponent)
					{
						Object f = null;
						LinkedList fillsAnalitzar = new LinkedList();
						fillsAnalitzar.add(c.getChildren());
						while ((f = fillsAnalitzar.poll()) != null)
						{
							if (f instanceof Textbox)
							{ // Ja l'hem trobat
								((Textbox) f).setFocus(true);
								return; // leva livet
							}
							// afegim fills..
							if (f instanceof List)
							{
								List fl = ((List) f);
								for (int i = 0; i < fl.size(); i++)
								{
									fillsAnalitzar.add(fl.get(i));
								}
							}
							if (f instanceof HtmlBasedComponent)
								fillsAnalitzar.addAll(((HtmlBasedComponent) f).getChildren());
						}
						return; // no trobat
					}
					// Resta de components directes (no hem de mirar els seus
					// fills)
					if (c instanceof HtmlBasedComponent)
					{
						// Mirem si és una macro i cerquem el primer textbox
						HtmlBasedComponent h = (HtmlBasedComponent) c;
						h.setFocus(true);
						return;
					}
				}
				ponFocusFill(c.getChildren(), idComponentFocus);
			}
		}
		catch (Throwable th)
		{
			// Ignorem qualque posible error
		}

	}

	/**
	 * Component que se li haurà de posar el focus quan es pinte la finestra
	 */
	private String idComponentFocus = null;

	public void setFocusCriteri (String idComponentFocus)
	{
		this.idComponentFocus = idComponentFocus;
	}

}
