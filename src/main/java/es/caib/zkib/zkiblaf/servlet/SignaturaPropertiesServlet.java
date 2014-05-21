package es.caib.zkib.zkiblaf.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Recupera propiedades de firma de una URL remota configurada por la propiedad de sistema es.caib.signatura.properties.servlet<br>
 * Si no está establecida, por defecto http://www.caib.es/signaturacaib/signatura_api.properties
 * Con esto evitamos que el applet de firma deba ir firmado para poder acceder a las propiedades cuando estas se encuentran en otro dominio, o utilizar un fichero crossdomain.xml
 * 
 * @author u91940
 *
 */
public class SignaturaPropertiesServlet extends HttpServlet {
	
	protected final String DATE_ONE =
        (new SimpleDateFormat( "EEE, dd MMM yyyy HH:mm:ss zzz", //$NON-NLS-1$
                              Locale.US)).format(new Date(1));
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//PJR 20100201-1 inici de las modificaciones para evitar el bug de descarga de documentos por SSL sobre explorer 6
		//http://community.jboss.org/wiki/DisableCacheControl
		
		resp.reset();
		resp.addHeader("Expires", DATE_ONE); //$NON-NLS-1$
		resp.setDateHeader("Last-Modified",new Date().getTime()); //$NON-NLS-1$
		
		//PFR 20100201-1 fin
		
		URL signaturaPropertiesServletURL= new URL(System.getProperty("es.caib.signatura.properties.servlet","http://www.caib.es/signaturacaib/signatura_api.properties")); //$NON-NLS-1$ //$NON-NLS-2$
		resp.setContentType("plain/text"); //$NON-NLS-1$
		resp.addHeader("content-disposition", "inline; filename=signatura_api.properties"); //$NON-NLS-1$ //$NON-NLS-2$
		
		OutputStream out=resp.getOutputStream();
		InputStream in=signaturaPropertiesServletURL.openStream();
		byte[] buf=new byte[1024];
		int readed=0;
		do{
			readed=in.read(buf);
			if(readed==-1) break;
			out.write(buf,0,readed);
		}while(readed!=-1);
		
	}
}
