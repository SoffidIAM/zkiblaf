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
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
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

/**
 * EsquemaVertical - Añadimos la opción de anclar la ventana de criteris (u88683) -
 * Permitimos que no haya ventana de criteris (u88683) [atributo senseCriteris] -
 * Afegim setFocusCriteri per indicar quin criteri ha de tindre el focus inicial
 * (u88683) 02/02/2012
 * 
 * @author u88683
 * 
 */
/**
 * @author u88683
 * 
 */
public class EsquemaVertical extends Window implements AfterCompose, Frameable
{
	private static final long serialVersionUID = 1L;
	private String dataModel;
	private final boolean _created = false;
	HtmlBasedComponent criteris;
	HtmlBasedComponent llista;
	HtmlBasedComponent formulari;
	boolean hideSearchToolbar;
	Div criterisHolder;
	Div llistaHolder;
	Div formulariHolder;
	Toolbar opt_toolbar; // Option toolbar
	Toolbarbutton show_hide_search; // Show / hide search form
	Checkbox check_show_always; // Show search form always
	ImageClic botoAmagaCriteris; // Show / hide search form image
	Toolbarbutton clear_criteria; // Clear search parameters
	ImageClic img_clear;
	boolean senseCriteris = false; // Allow no window criteria
	private boolean ancoraCriteris = true;
	private boolean criterisVisible = true;
	private final String IMG_CRITERIS_VISIBLE = "~./img/fletxa-baix-marro.gif"; //$NON-NLS-1$
	private final String IMG_CRITERIS_NO_VISIBLE = "~./img/fletxa-marro.gif"; //$NON-NLS-1$
	private final String IMG_CLEAR_CRITERIA = "~./img/reload-petit16.png"; //$NON-NLS-1$
	private boolean botoAmagarVisible = true; // Hide show search button
	private Div criterisDiv;
	private ImageClic botoTancar;

	private String getImgCriterisVisible ()
	{
		return criterisVisible ? IMG_CRITERIS_VISIBLE : IMG_CRITERIS_NO_VISIBLE;
	}

	private String getTextCriterisVisible ()
	{
		return criterisVisible ? Messages.getString("EsquemaVertical.NoShowCriteria") //$NON-NLS-1$
				: Messages.getString("EsquemaVertical.ShowCriteria"); //$NON-NLS-1$
	}

	private boolean getCriterisVisible ()
	{
		return ancoraCriteris ? true : criterisVisible;
	}

	private void prepareSchema ()
	{
		criterisDiv = new Div();
		super.insertBefore(criterisDiv, null);
		Label titol = new Label();
		titol.setValue(Messages.getString("EsquemaVertical.FindCriteria")); //$NON-NLS-1$
		titol.setSclass("titol_capsa"); //$NON-NLS-1$
		criterisDiv.insertBefore(titol, null);

		if (!hideSearchToolbar)
		{
			defineToolbarElements();
	
			HtmlBasedComponent show_criteria_opt = new Div();
			show_criteria_opt.setWidth("100%"); //$NON-NLS-1$
			show_criteria_opt.appendChild(opt_toolbar);
			criterisDiv.insertBefore(show_criteria_opt, null);
		}

		// Part d'adalt; criteris
		criterisHolder = new Div();
		criterisHolder.setSclass("query");
		criterisDiv.insertBefore(criterisHolder, null);
		criterisHolder.setVisible(getCriterisVisible());

		// Part d'enmig; llista i formulari
		// Llista
		llistaHolder = new Div();
		llistaHolder.setSclass("record-list");
		super.insertBefore(llistaHolder, null);
		titol = new Label();
		titol.setValue(Messages.getString("EsquemaVertical.Browser")); //$NON-NLS-1$
		titol.setSclass("titol_capsa"); //$NON-NLS-1$
		llistaHolder.appendChild(titol);
		// Formulari
		formulariHolder = new Div();
		formulariHolder.setClass("record-form-shrinked");
		super.insertBefore(formulariHolder, null);
		Div headerBox = new Div();
		headerBox.setWidth("100%"); //$NON-NLS-1$
		formulariHolder.appendChild(headerBox);
		titol = new Label();
		titol.setValue(Messages.getString("EsquemaVertical.Details")); //$NON-NLS-1$
		titol.setSclass("titol_capsa"); //$NON-NLS-1$
		headerBox.appendChild(titol);
		botoTancar = new ImageClic();
		botoTancar.setSrc("~./img/tanca.png"); //$NON-NLS-1$
		botoTancar.setAlign("right"); //$NON-NLS-1$
		botoTancar.setTooltiptext(Messages.getString("EsquemaVertical.CloseDetails")); //$NON-NLS-1$
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
		botoAmagaCriteris = new ImageClic();
		botoAmagaCriteris.setSrc(getImgCriterisVisible());
		botoAmagaCriteris.addEventListener("onClick", new EventListener() { //$NON-NLS-1$
					public boolean isAsap ()
					{
						return true;
					};

					public void onEvent (org.zkoss.zk.ui.event.Event event)
					{
						event.stopPropagation();

						manageShowCriteria();
					};
				});

		show_hide_search = new Toolbarbutton();
		show_hide_search.addEventListener("onClick", new EventListener() { //$NON-NLS-1$
					public boolean isAsap ()
					{
						return true;
					};

					public void onEvent (org.zkoss.zk.ui.event.Event event)
					{
						event.stopPropagation();

						manageShowCriteria();
					};
				});
		show_hide_search.setLabel(getTextCriterisVisible());

		check_show_always = new Checkbox();
		check_show_always.setLabel(Messages.getString("EsquemaVertical.ShowAlways")); //$NON-NLS-1$
		check_show_always.setClass("toolbar-checkbox"); //$NON-NLS-1$
		check_show_always.addEventListener("onCheck", new EventListener() { //$NON-NLS-1$
					public boolean isAsap ()
					{
						return true;
					};

					public void onEvent (org.zkoss.zk.ui.event.Event event)
					{
						event.stopPropagation();

						ancoraCriteris = !ancoraCriteris;

						if (ancoraCriteris)
							criterisVisible = true;

						mostraCriteris();
					};
				});

		clear_criteria = new Toolbarbutton();
		clear_criteria.setLabel(Messages.getString("EsquemaVertical.ClearCriteria")); //$NON-NLS-1$
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

		opt_toolbar.appendChild(botoAmagaCriteris);

		if (botoAmagarVisible)
			opt_toolbar.appendChild(botoAmagaCriteris);

		opt_toolbar.setVisible(!isSenseCriteris());
		opt_toolbar.appendChild(show_hide_search);
		opt_toolbar.appendChild(check_show_always);
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

	public void manageShowCriteria ()
	{
		if (!check_show_always.isChecked())
		{
			criterisVisible = !criterisVisible;

			if (!criterisVisible)
			{
				ancoraCriteris = false;
			}

			mostraCriteris();
		}
	}

	@Override
	public boolean insertBefore (Component arg0, Component arg1)
	{
		if (criterisHolder == null)
			prepareSchema();

		if (criteris == null && !isSenseCriteris())
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
	}

	public void showFormulari ()
	{
		Event ev2 = new Event("onShowFormulari", this); //$NON-NLS-1$
		Events.postEvent(ev2);
		if (!formulariHolder.isVisible())
		{
			llistaHolder.setSclass("record-list-shrinked");
			llistaHolder.invalidate();
			formulariHolder.setSclass("record-form");
			formulariHolder.setVisible(true);
		}
	}

	public void hideFormulari ()
	{
		Event ev2 = new Event("onHideFormulari", this); //$NON-NLS-1$
		Events.postEvent(ev2);

		if (formulariHolder.isVisible())
		{
			formulariHolder.setVisible(false);
			llistaHolder.setSclass("record-list");
			formulariHolder.setSclass("record-form-shrinked");
			llistaHolder.invalidate();
		}
	}

	public boolean isFormulariVisible ()
	{
		return formulariHolder.isVisible();
	}

	private void mostraCriteris ()
	{
		botoAmagaCriteris.setSrc(getImgCriterisVisible());
		show_hide_search.setLabel(getTextCriterisVisible());
		criterisHolder.setVisible(getCriterisVisible());
	}

	public void showCriteris ()
	{
		Event ev2 = new Event("onShowCriteris", EsquemaVertical.this); //$NON-NLS-1$

		Events.postEvent(ev2);
		criterisVisible = true;

		mostraCriteris();
	}

	public void hideCriteris ()
	{
		Event ev2 = new Event("onHideCriteris", EsquemaVertical.this); //$NON-NLS-1$

		Events.postEvent(ev2);
		criterisVisible = true;

		manageShowCriteria();
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
			llista.setParent(llistaHolder);
		if (formulari != null)
			formulari.setParent(formulariHolder);

		// Ens establim com a component frameable (canclose)
		if (Application.getActiveFrameInfo() != null)
		{
			Application.getActiveFrameInfo().addFrameCheckCanClose(this);
		}
	}

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
			Component c = getModelDades();// Path.getComponent(getSpaceOwner(),
																		// dataModel);

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
		Component c = getModelDades(); // Path.getComponent(getSpaceOwner(), dataModel);

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

	public boolean isBotoAmagarVisible ()
	{
		return botoAmagarVisible;
	}

	public void setBotoAmagarVisible (boolean botoAmagarVisible)
	{
		this.botoAmagarVisible = botoAmagarVisible;
	}

	public boolean isSenseCriteris ()
	{
		return senseCriteris;
	}

	public void setSenseCriteris (boolean senseCriteris)
	{
		this.senseCriteris = senseCriteris;
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
						Labels.getLabel("task.titleDeseaSalir") //$NON-NLS-1$
						, Messagebox.QUESTION);

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

	public boolean saveContent;

	public boolean isSaveContent ()
	{
		return saveContent;
	}

	public void setSaveContent (boolean saveContent)
	{
		this.saveContent = saveContent;
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
				// TODO: se obtiene por siempre el mismo id: arreglar
				comp = getPage().getFellowIfAny("model"); //$NON-NLS-1$
			}

			catch (Exception ex)
			{
			}
		}
		return comp;
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

	// Amaguem els detalls
	public void tancaDetalls ()
	{
		hideFormulari();
	}

	public String getSclass() {
		return super.getSclass()+" frame-vertical";
	}
	
	public void removeCriteria () {
		criterisDiv.setVisible(false);
	}
	
	public void removeList () {
		llistaHolder.setVisible(false);
		botoTancar.setVisible(false);
	}

	public boolean isHideSearchToolbar() {
		return hideSearchToolbar;
	}

	public void setHideSearchToolbar(boolean hideSearchToolbar) {
		this.hideSearchToolbar = hideSearchToolbar;
	}
}
