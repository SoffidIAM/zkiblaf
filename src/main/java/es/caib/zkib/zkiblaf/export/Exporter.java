package es.caib.zkib.zkiblaf.export;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import es.caib.zkib.component.DataGrid;
import es.caib.zkib.component.DataLabel;
import es.caib.zkib.component.DataRow;

/**
 * Integrem la exportació de listbox i grid en un únic component per no tindre
 * que repetir les modificacions en els diferents components que exporten el seu
 * contingut.
 * 
 * Alejandro Usero Ruiz - 29/02/2012 10:20h
 * 
 * @author u88683
 * 
 */
public class Exporter {

	public Exporter() {

	}

	private String conservaSaltLinia = "N"; //$NON-NLS-1$

	public Exporter(String conservaSaltLinia) {
		this.conservaSaltLinia = conservaSaltLinia;
	}
	
	private void obteContingutFills(Object fill, StringBuffer s) {

		if (fill instanceof Listcell) {
			// Primer afegim contingut
			Listcell cell = (Listcell) fill;
			String t = cell.getLabel().replaceAll("\"", "\'"); //$NON-NLS-1$
			// I després mirem si en té fills:
			s.append ( t );
			boolean any = t.length() > 0 ;
			List fills = cell.getChildren();
			if (fills!=null) for (int i=0; i < fills.size(); i++) {
				StringBuffer sb = new StringBuffer();
				obteContingutFills(fills.get(i), sb);

				if (any && sb.length() > 0)
					s.append (' ');
				s.append (sb);
				if (sb.length() > 0)
					any = true;
			}			
		} else if (fill instanceof Datebox) {
			// Datebox
			Datebox datebox = (Datebox) fill;
			s.append(datebox.getText());
		} else if (fill instanceof Textbox) {
			// Datebox
			Textbox tb = (Textbox) fill;
			s.append(tb.getText().replaceAll("\"", "\'")); //$NON-NLS-1$
		} else if (fill instanceof Div || fill instanceof HtmlMacroComponent) {
			HtmlBasedComponent div = (HtmlBasedComponent) fill;
			// Hem de mirar els fills
			List fills = div.getChildren();
			boolean any = false;
			if (fills!=null) for (int i=0; i < fills.size(); i++) {
				StringBuffer sb = new StringBuffer();
				obteContingutFills(fills.get(i), sb);

				if (any && sb.length() > 0)
					s.append (' ');
				s.append (sb);
				if (sb.length() > 0)
					any = true;
			}			
		} else if (fill instanceof Label) {
			s.append(((Label)fill).getValue().replaceAll("\"", "\'")); //$NON-NLS-1$
		}
	}
	
	private final String SEPARADOR = ";"; //$NON-NLS-1$
	
	public String export(Listbox listbox) {

		if (listbox.getItemCount() == 0) {
			return ""; //$NON-NLS-1$
		}
		// Los cargamos todos (puede que no
		// estén cargados todavía)
		listbox.renderAll();
		StringBuffer sb = new StringBuffer();
		Collection heads = listbox.getHeads();
		
		for (Iterator it = heads.iterator(); it.hasNext();) {
			Listhead head = (Listhead) it.next();
			StringBuffer h = new StringBuffer(""); //$NON-NLS-1$
			Collection fills = head.getChildren();
			boolean first = true;
			for (Iterator fill = fills.iterator(); fill.hasNext();) {
				if (first)
					first = false;
				else
					h.append (SEPARADOR);
				Listheader header = (Listheader) fill.next();
				h.append('"').append(header.getLabel()).append('"');
			}
			sb.append(h.toString() + "\r\n"); //$NON-NLS-1$
		}
		Collection items = listbox.getItems();
		if (items!=null) for (Iterator it = items.iterator(); it.hasNext();) {
			Listitem item = (Listitem) it.next();
			StringBuffer i = new StringBuffer(""); //$NON-NLS-1$
			Collection cells = item.getChildren();
			boolean first = true;
			if (cells!=null) for (Iterator itcell = cells.iterator(); itcell.hasNext();) {
				Listcell cell = (Listcell) itcell.next();
				if (first)
					first = false;
				else
					i.append (SEPARADOR);
				if (cell.getChildren()!=null && cell.getChildren().size() != 0) {
					try {
						// Analitzem els fills
						i.append("\"");
						obteContingutFills(cell,i); //si en té fills s'analitcen
						i.append("\"");
					} catch (Exception ex) {
						// Afegim el contingut
						i.append("\"")
							.append(cell.getLabel())
							.append("\"");
					}
				} else {
					// Añadimos el contenido
					i.append("\"")
						.append(cell.getLabel())
						.append("\"");
				}
			}
			// Sustituim - si cal - el salto de linia per un espai
			String res = "S".equals(conservaSaltLinia) ? i.toString() : i //$NON-NLS-1$
					.toString().replaceAll("\n", "  ").replaceAll("\r", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

			sb.append(res.toString() + "\r\n"); //$NON-NLS-1$
		}

		return sb.toString();

	}
	
	
	public String export (DataGrid grid) {
		
			if (grid.getChildren().size() == 0) {
				return ""; //$NON-NLS-1$
			}
			// Los cargamos todos (puede que no
			// estén cargados todavía)
			grid.renderAll(); 
			
			StringBuffer sb = new StringBuffer();
			Collection heads = grid.getHeads();
			final String SEPARADOR = ";"; //$NON-NLS-1$
			for (Iterator it = heads.iterator(); it.hasNext();) {
				Columns head = (Columns) it.next();
				StringBuffer h = new StringBuffer(""); //$NON-NLS-1$
				Collection fills = head.getChildren();
				boolean first = true;
				for (Iterator fill = fills.iterator(); fill
						.hasNext();) {
					if (first)
						first = false;
					else
						h.append (SEPARADOR);
					Column header = (Column) fill.next();
					h.append('"').append(header.getLabel()).append('"');
				}
				sb.append(h.toString() + "\r\n"); //$NON-NLS-1$
			}
			Rows rows = grid.getRows();
			Collection items = rows.getChildren();
			if (items!=null) for (Iterator it = items.iterator(); it.hasNext();) {
				DataRow item = (DataRow) it.next();
				List fillsFila = item.getChildren();
				StringBuffer i = new StringBuffer(""); //$NON-NLS-1$
				boolean first = true;
				if (fillsFila!=null) for (Iterator f = fillsFila.iterator(); f.hasNext();) {
					if (first)
						first = false;
					else
						i.append (SEPARADOR);
					AbstractComponent cell = (AbstractComponent) f.next();
					if (cell.getChildren()!=null && cell.getChildren().size() != 0) {
						try {
							// Analitzem els fills
							i.append("\"");
							obteContingutFills(cell,i); //si en té fills s'analitcen
							i.append("\"");
						} catch (Exception ex) {
							// Afegim el contingut
							if (cell instanceof Label) {
								Label l_fill = (Label) cell;
								i.append("\"")
									.append(l_fill.getValue())
									.append("\"");
							}
						}
					} else {
						if (cell instanceof Label) {
							Label l_fill = (Label) cell;
							i.append("\"")
								.append(l_fill.getValue())
								.append("\"");
						}
					}
				}
				
				// Sustituim - si cal - el salto de linia per un espai
				String res = "S".equals(conservaSaltLinia) ? i.toString() : i //$NON-NLS-1$
						.toString().replaceAll("\n", "  ").replaceAll("\r", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

				sb.append(res.toString() + "\r\n"); //$NON-NLS-1$
			}

			return sb.toString();
	}
}
