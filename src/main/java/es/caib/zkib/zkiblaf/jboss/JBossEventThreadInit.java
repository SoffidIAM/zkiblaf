package es.caib.zkib.zkiblaf.jboss;

import java.security.Principal;

import org.jboss.security.SecurityAssociation;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventThreadInit;

public class JBossEventThreadInit implements EventThreadInit {
    private Principal _principal;    
    private Object _credential;    
    /** Retrieve info at the constructor, which runs at the servlet thread. */    
    public JBossEventThreadInit() {    
    }    
    //-- EventThreadInit --//    
    /** Initial the event processing thread at this method. */    
	public void prepare(Component comp, Event event) throws Exception {
        _principal = SecurityAssociation.getPrincipal();        
        _credential = SecurityAssociation.getCredential();        
	}
	public boolean init(Component comp, Event event) throws Exception {
        SecurityAssociation.setPrincipal(_principal);        
        SecurityAssociation.setCredential(_credential);        
		return true;
	}    

}
