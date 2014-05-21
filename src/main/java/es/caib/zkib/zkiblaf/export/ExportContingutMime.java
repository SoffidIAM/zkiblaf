package es.caib.zkib.zkiblaf.export;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;

/**
 * Exportem el contingut mitjançant el servlet ContentExportServlet
 * Alejandro Usero Ruiz - 15/03/2011
 * 
 * @author u88683
 *
 */
public class ExportContingutMime {
	
	public static void export (String mime, String contingut, String filename, Component c) {
		try {
			mime = URLEncoder.encode(mime, "UTF-8"); //$NON-NLS-1$
			contingut = URLEncoder.encode(contingut, "UTF-8"); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			return;
		}
		
		zkForm form = new zkForm();
		form.setDynamicProperty("action", "./contentexport/"+filename); //$NON-NLS-1$ //$NON-NLS-2$
		form.setDynamicProperty("target", "_blank"); //$NON-NLS-1$ //$NON-NLS-2$
		form.setDynamicProperty("method", "post"); //$NON-NLS-1$ //$NON-NLS-2$
		form.setPage(c.getPage());
		
		zkInput i_mime = new zkInput();
        i_mime.setParent(form);
        i_mime.setDynamicProperty("type", "hidden"); //$NON-NLS-1$ //$NON-NLS-2$
        i_mime.setDynamicProperty("name", "mime"); //$NON-NLS-1$ //$NON-NLS-2$
        i_mime.setValue(mime);
        
        zkInput i_contingut = new zkInput();
        i_contingut.setParent(form);
        i_contingut.setDynamicProperty("type", "hidden"); //$NON-NLS-1$ //$NON-NLS-2$
        i_contingut.setDynamicProperty("name", "contingut"); //$NON-NLS-1$ //$NON-NLS-2$
        i_contingut.setValue(contingut);
		
		Clients.submitForm(form);		
	}

}
