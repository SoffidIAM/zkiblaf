package es.caib.zkib.zkiblaf.tomee;

import java.security.Principal;

import org.apache.openejb.loader.SystemInstance;
import org.apache.openejb.spi.Assembler;
import org.apache.openejb.spi.SecurityService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventThreadInit;

public class TomeeThreadInit implements EventThreadInit {

	Object principal;
	
	public TomeeThreadInit() {
	}

	public void prepare(Component comp, Event event) throws Exception {
        final SecurityService<?> ss = SystemInstance.get().getComponent(Assembler.class).getSecurityService();
        principal = ss.currentState();
	}

	public boolean init(Component comp, Event event) throws Exception {
        final SecurityService<?> ss = SystemInstance.get().getComponent(Assembler.class).getSecurityService();
        ss.setState(principal);
		return true;
	}

}
