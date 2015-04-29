package es.caib.zkib.zkiblaf.export;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;

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
			Filedownload.save(new AMedia(filename, null, mime, contingut.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
