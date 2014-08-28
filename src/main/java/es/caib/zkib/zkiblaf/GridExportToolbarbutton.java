package es.caib.zkib.zkiblaf;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Toolbarbutton;

import es.caib.zkib.component.DataGrid;
import es.caib.zkib.zkiblaf.export.ExportContingutMime;
import es.caib.zkib.zkiblaf.export.Exporter;

public class GridExportToolbarbutton extends Toolbarbutton {
	/**
	 * Permitimos mostrar en formato exportable los datos que se muestran como
	 * resultados de un gridbox
	 * 
	 * Alejandro Usero Ruiz - 29/01/2010
	 * 
	 */
	private static final long serialVersionUID = -530199585286130617L;
	private String nomGridContenidor; // Elemento que contiene los datos
	private Boolean acces = new Boolean(false);

	public GridExportToolbarbutton() {
		setLabel(Messages.getString("GridExportButton.ExportResults")); //$NON-NLS-1$
		setImage("~./img/exporta.gif"); //$NON-NLS-1$
		setVisible(acces.booleanValue());
		addEventListener(Events.ON_CLICK, new EventListener() {
			public boolean isAsap() {
				return true;
			}

			public void onEvent(org.zkoss.zk.ui.event.Event event) {
				if (nomGridContenidor != null) {
					Component c = Path.getComponent(getSpaceOwner(),
							nomGridContenidor);
					
					if (c instanceof DataGrid) {
						DataGrid grid = (DataGrid) c;
						Exporter exp = new Exporter(conservaSaltLinia);
						String res = exp.export(grid);
						ExportContingutMime.export("text/csv", res, "export.csv", c); //$NON-NLS-1$ //$NON-NLS-2$
					}					

				}

			}
		});
	}

	public void setGrid(String nomGrid) {
		this.nomGridContenidor = nomGrid;
	}

	public String getGrid(String nomGrid) {
		return this.nomGridContenidor;
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
