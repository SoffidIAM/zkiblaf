package es.caib.zkib.zkiblaf;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Treeitem;

import es.caib.zkib.component.DataTree;

public class DeleteTreeButton extends Toolbarbutton {
	/**
	 * 
	 */
	private static final long serialVersionUID = -530199585286130616L;
	private String tree;
	private Boolean acces = new Boolean(false);
	private boolean confirmarAbansEsborrar = false;
	private boolean potEsborrarBranquesAmbFills = true;
	
	public DeleteTreeButton ()
	{
		setLabel (Messages.getString("DeleteTreeButton.DeleteChanges")); //$NON-NLS-1$
		setImage ("~./img/list-remove.gif"); //$NON-NLS-1$
		setVisible (acces.booleanValue());
		addEventListener(Events.ON_CLICK, new EventListener()
		{
			public boolean isAsap() {return true;}
			public void onEvent(org.zkoss.zk.ui.event.Event event) {
				if (tree != null)
				{
					Component c = Path.getComponent(getSpaceOwner(), tree);
					if (c != null && c instanceof DataTree)
					{
						DataTree dlb = (DataTree) c;
						// Verifiquem si es pot esborrar abans de confirmar els canvis
						if (isConfirmarAbansEsborrar() && dlb.isCommitPending()) {
							// Si queda per confirmar canvis no deixem esborrar
							try {
								Missatgebox.avis (Messages.getString("DeleteTreeButton.ConfirmChangesDialog")); //$NON-NLS-1$
								return;
							} catch (Exception ex) {}
							
						}
						// Verifiquem si pot esborrar branques sense fills
						if (!potEsborrarBranquesAmbFills) {
							Treeitem seleccionat = dlb.getSelectedItem();
							if (seleccionat !=null) {
								if (!seleccionat.isOpen()) {seleccionat.setOpen(true);} //abrimos para obtener hijos (si tiene)
								if (seleccionat.getTreechildren()!=null && seleccionat.getTreechildren().getItemCount()!=0) {
									try {
										Missatgebox.avis (Messages.getString("DeleteTreeButton.DeleteBrancheError")); //$NON-NLS-1$
										return;
									} catch (Exception ex) {}
								}
							}
						}
						try {
							dlb.delete();
						} catch (Exception ex ) {
							ex.printStackTrace();
						}
					}
				}
			}
		});
	}

	public String getTree() {
		return tree;
	}

	public void setTree(String tree) {
		this.tree = tree;
	}

	public Boolean getAcces() {
		return acces;
	}

	public void setAcces(Boolean acces) {
		this.acces = acces;
		this.setVisible(acces.booleanValue());
	}

	public boolean isConfirmarAbansEsborrar() {
		return confirmarAbansEsborrar;
	}

	public void setConfirmarAbansEsborrar(boolean confirmarAbansEsborrar) {
		this.confirmarAbansEsborrar = confirmarAbansEsborrar;
	}

	public boolean isPotEsborrarBranquesAmbFills() {
		return potEsborrarBranquesAmbFills;
	}

	public void setPotEsborrarBranquesAmbFills(boolean potEsborrarBranquesAmbFills) {
		this.potEsborrarBranquesAmbFills = potEsborrarBranquesAmbFills;
	}

}
