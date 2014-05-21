package es.caib.zkib.zkiblaf.export;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Clase para exportar el contenido de los listbox, etc.
 * Creada para solucionar el problema de exportación del 
 * contenido de los listbox en Internet Explorer cuando
 * el entorno se ejecuta en modo mixto http/https
 * 
 * Alejandro Usero Ruiz - 15/03/2011 8:32h
 * 
 * @author u88683
 *
 */
public class ContentExportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String mime = (String) request.getParameter("mime"); //$NON-NLS-1$
		String contingut = (String) request.getParameter("contingut"); //$NON-NLS-1$
		
		if (mime != null && contingut !=null) {
			mime = URLDecoder.decode(mime, "UTF-8"); //$NON-NLS-1$
			contingut = URLDecoder.decode(contingut, "UTF-8"); //$NON-NLS-1$
		}

		if ("text/csv".equals(mime) && !"".equals(contingut) ) { //$NON-NLS-1$ //$NON-NLS-2$
			response.reset();
			response.resetBuffer();
			response.setContentType(mime);
			response.getWriter().print(contingut);
		}

	}

}
