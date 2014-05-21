package es.caib.zkib.zkiblaf;

import java.util.Collection;
import java.util.Iterator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Toolbarbutton;

import es.caib.zkib.zkiblaf.export.ExportContingutMime;
import es.caib.zkib.zkiblaf.export.Exporter;


public class ListboxExportToolbarButton extends Toolbarbutton {
	/**
	 * Permitimos mostrar en formato exportable los datos que se muestran como
	 * resultados de un lisbox.
	 * 
	 * Alejandro Usero Ruiz - 29/01/2010 ver 1.0
	 * 
	 * Versió 1.1 - 29/02/2012
	 * - Si la cel·la en té de fills, afegim el seu contingut també mitjançant la
	 *   classe Exporter (per unificar exporters)
	 * 
	 */
	private static final long serialVersionUID = -530199585286130615L;
	private String nomListboxContenidor; // Elemento que contiene los datos
	private Boolean acces = new Boolean(false); // Por defecto OCULTO (!!)

	public ListboxExportToolbarButton() {
		setLabel(Messages.getString("ListboxExportToolbarButton.ExportResults")); //$NON-NLS-1$
		setImage("~./img/exporta.gif"); //$NON-NLS-1$
		setVisible(acces.booleanValue());
		addEventListener(Events.ON_CLICK, new EventListener() {
			public boolean isAsap() {
				return true;
			}

			public void onEvent(org.zkoss.zk.ui.event.Event event) {
				if (nomListboxContenidor != null) {
					Component c = Path.getComponent(getSpaceOwner(),
							nomListboxContenidor);
					if (c instanceof Listbox) {
						Listbox listbox = (Listbox) c;
						Exporter exp = new Exporter(conservaSaltLinia);
						String res = exp.export(listbox);
						ExportContingutMime.export("text/csv", res, "export.csv", c); //$NON-NLS-1$ //$NON-NLS-2$
					}

				}
			}
		});
	}

	public String getListbox() {
		return nomListboxContenidor;
	}

	public void setListbox(String listbox) {
		this.nomListboxContenidor = listbox;
	}

	public Boolean getAcces() {
		return acces;
	}

	public void setAcces(Boolean acces) {
		this.acces = acces;
		this.setVisible(acces.booleanValue());
	}
	
	// Establim si s'ha de conservar el salt de linia o
	// bé sustituir-lo per un espai (per defecte es sustitueix)
	String conservaSaltLinia = "N"; //$NON-NLS-1$

	public String isConservaSaltLinia() {
		return conservaSaltLinia;
	}

	public void setConservaSaltLinia(String conservaSaltLinia) {
		this.conservaSaltLinia = conservaSaltLinia;
	}
	

}
