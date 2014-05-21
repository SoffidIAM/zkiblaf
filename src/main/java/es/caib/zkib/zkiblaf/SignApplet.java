package es.caib.zkib.zkiblaf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.zkoss.util.resource.Labels;
import org.zkoss.web.servlet.http.Encodes;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.AuResponse;
import org.zkoss.zk.au.Command;
import org.zkoss.zk.au.ComponentCommand;
import org.zkoss.zk.au.out.AuInvoke;
import org.zkoss.zk.au.out.AuShowBusy;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;


public class SignApplet extends AbstractComponent {
	private static final String LOAD_CERTS_EVENT = "onLoadCerts"; //$NON-NLS-1$
	private static final String SIGN_EVENT = "onSign"; //$NON-NLS-1$
	private static final String JAVA_NOT_AVAILABLE_EVENT = "onJavaNotAvailable"; //$NON-NLS-1$
	private static Command _onJavaNotAvailableCommand;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String source;
	public String width;
	public String height;
	private String target;
	
	
	public SignApplet() {
		_onJavaNotAvailableCommand  = new ComponentCommandExt (this,JAVA_NOT_AVAILABLE_EVENT, 0);
	}
	
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}


	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		if (this.source == null)
		{
			this.source = source;
		}
		else
			this.source = source;
		loadCerts();
	}
	
	public Command getCommand(String cmdId) {
		if (LOAD_CERTS_EVENT.equals(cmdId))
			return _onLoadCertsCommand;
		if (SIGN_EVENT.equals(cmdId))
			return _onSignCommand;
		if(JAVA_NOT_AVAILABLE_EVENT.equals(cmdId))
			return _onJavaNotAvailableCommand;
		
		return super.getCommand(cmdId);
	}

	private String getEncodedSource ()
	{
		final Desktop dt = getDesktop(); //it might not belong to any desktop
		HttpSession session = (HttpSession) dt.getSession().getNativeSession();
		return dt.getExecution().encodeURL(source)+";jsessionid="+session.getId(); //$NON-NLS-1$
	}
	
        private String getEncodedTarget ()
        {
                final Desktop dt = getDesktop(); //it might not belong to any desktop
                HttpSession session = (HttpSession) dt.getSession().getNativeSession();
                return dt.getExecution().encodeURL(target)+";jsessionid="+session.getId(); //$NON-NLS-1$
        }
        
	public void loadCerts()
	{	
		response ("ctrl", new AuInvoke (this, "getCerts", getEncodedSource())); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void sign(String certificate)
	{
		response ("ctrl", new AuInvoke (this, "sign", certificate, getEncodedSource())); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
    public void signPDF(String certificate, String url, int position)
    {
        response ("wait1", new AuShowBusy ("Signing", true)); //$NON-NLS-1$ //$NON-NLS-2$
        response ("prepare1", new AuInvoke (this, "setSignPDF_url", url)); //$NON-NLS-1$ //$NON-NLS-2$
        response ("prepare2", new AuInvoke (this, "setSignPDF_position",Integer.toString(position))); //$NON-NLS-1$ //$NON-NLS-2$
        response ("sign", new AuInvoke (this, "signPDF", certificate, getEncodedSource(), getEncodedTarget())); //$NON-NLS-1$ //$NON-NLS-2$
        response ("wait2", new AuShowBusy (null, false)); //$NON-NLS-1$

    }
    
    public void signPDFExtended(String certificate, String url, int position, String top, String left, String height, String width, String rotation)
    {
        response ("wait1", new AuShowBusy ("Signing", true)); //$NON-NLS-1$ //$NON-NLS-2$
        response ("prepare1", new AuInvoke (this, "setSignPDF_url", url)); //$NON-NLS-1$ //$NON-NLS-2$
        response ("prepare2", new AuInvoke (this, "setSignPDF_position",Integer.toString(position))); //$NON-NLS-1$ //$NON-NLS-2$
        response ("prepare3", new AuInvoke (this, "setSignPDFExtended_top",top)); //$NON-NLS-1$ //$NON-NLS-2$
        response ("prepare4", new AuInvoke (this, "setSignPDFExtended_left",left)); //$NON-NLS-1$ //$NON-NLS-2$
        response ("prepare5", new AuInvoke (this, "setSignPDFExtended_height",height)); //$NON-NLS-1$ //$NON-NLS-2$
        response ("prepare6", new AuInvoke (this, "setSignPDFExtended_width",width)); //$NON-NLS-1$ //$NON-NLS-2$
        response ("prepare7", new AuInvoke (this, "setSignPDFExtended_rotation",rotation)); //$NON-NLS-1$ //$NON-NLS-2$

        response ("sign", new AuInvoke (this, "signPDFExtended", certificate, getEncodedSource(), getEncodedTarget())); //$NON-NLS-1$ //$NON-NLS-2$
        response ("wait2", new AuShowBusy (null, false)); //$NON-NLS-1$

    }
    
        
    public void certifyPDF(String certificate, String x,String y, String degrees, String url,String location)
    {
        response ("wait1", new AuShowBusy ("Signing", true)); //$NON-NLS-1$ //$NON-NLS-2$
	    response ("prepare1", new AuInvoke (this, "setCertifyPDF_x", x)); //$NON-NLS-1$ //$NON-NLS-2$
	    response ("prepare2", new AuInvoke (this, "setCertifyPDF_y", y)); //$NON-NLS-1$ //$NON-NLS-2$
	    response ("prepare3", new AuInvoke (this, "setCertifyPDF_degrees", degrees));	    		 //$NON-NLS-1$ //$NON-NLS-2$
		response ("prepare4", new AuInvoke (this, "setCertifyPDF_url", url)); //$NON-NLS-1$ //$NON-NLS-2$
		response ("prepare5", new AuInvoke (this, "setCertifyPDF_location", location)); //$NON-NLS-1$ //$NON-NLS-2$
	    response ("sign", new AuInvoke (this, "certifyPDF", certificate, getEncodedSource(), getEncodedTarget())); //$NON-NLS-1$ //$NON-NLS-2$
        response ("wait6", new AuShowBusy (null, false)); //$NON-NLS-1$

    }
        
	private static Command _onLoadCertsCommand  = new ComponentCommand (LOAD_CERTS_EVENT, 0) {

		protected void process(AuRequest request) {
			final SignApplet applet = (SignApplet) request.getComponent();
			if (request.getData() == null)
				Events.postEvent(new Event (LOAD_CERTS_EVENT, applet, new String[0]));
			else
				Events.postEvent(new Event (LOAD_CERTS_EVENT, applet, request.getData()));
		}
		
	};

	private static Command _onSignCommand  = new ComponentCommand (SIGN_EVENT, 0) {

		protected void process(AuRequest request) {
			try {
				final SignApplet applet = (SignApplet) request.getComponent();
				String b64Object = request.getData()[0];
				if (b64Object == null || b64Object.length() == 0)
				{
					Events.postEvent(new Event (SIGN_EVENT, applet,"")); //$NON-NLS-1$
				} else {
					byte bObject[] = Base64.decodeBase64(b64Object.getBytes("ISO-8859-1")); //$NON-NLS-1$
					//enviamos el pkcs7
					Events.postEvent(new Event (SIGN_EVENT, applet, bObject));
				}
			} catch (Exception e) {
				throw new UiException(e);
			}
		}
		
	};
	
	
    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * By default /signaturaPeopertiesServlet
     * can be configured to another URL by establishing es.caib.zkiblaf.signatura.properties.url property
     */
	public String getSignaturaPropertiesServletURL(){
		final Desktop dt = getDesktop(); //it might not belong to any desktop
		HttpSession session = (HttpSession) dt.getSession().getNativeSession();
		
		javax.servlet.http.HttpServletRequest request =(HttpServletRequest) Executions.getCurrent().getNativeRequest();
		String defaultURL=request.getScheme() 
		+ "://" + request.getServerName() //$NON-NLS-1$
		+ ":" + request.getServerPort() //$NON-NLS-1$
		+ request.getContextPath()+"/signaturaPropertiesServlet"+";jsesssionid="+session.getId(); //$NON-NLS-1$ //$NON-NLS-2$
		

		
		return defaultURL;
	}
	

	public boolean setVisible(boolean visible) {
		if(!isVisible()){
			//una vegada carregat ja ha de desapareixer per tal de poder recordar la contrassenya mitja hora
			//així que comentem el invalidate()
				//invalidate();
		}			
		
		return super.setVisible(visible);
	}
	
	
	public class ComponentCommandExt extends ComponentCommand{
		
		AbstractComponent caller=null;
		
		public ComponentCommandExt(AbstractComponent caller,String id, int flags) {
			super(id, flags);
			this.caller=caller;
		}

		/**
		 * Si Java està habilitat, fa visible el component
		 * Si Java no està habilitat, fa visible una finestra modal amb ajuda de com habilitar Java
		 */
		protected void process(AuRequest request) {
			if(request.getData()!=null && request.getData().length>0){	
				caller.setVisible(true);				
			}else{
				throw new UiException(String.format(Messages.getString("SignApplet.UnexpectedBehaviour"), //$NON-NLS-1$
						request.getData())); //$NON-NLS-1$
			}
		}		
	};
}
