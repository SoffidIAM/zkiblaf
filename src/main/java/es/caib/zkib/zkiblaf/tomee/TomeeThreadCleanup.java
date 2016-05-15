package es.caib.zkib.zkiblaf.tomee;

import java.util.List;

import org.apache.openejb.loader.SystemInstance;
import org.apache.openejb.spi.Assembler;
import org.apache.openejb.spi.SecurityService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventThreadCleanup;

public class TomeeThreadCleanup implements EventThreadCleanup {

	public TomeeThreadCleanup() {
	}

	public void cleanup(Component comp, Event evt, List errs) throws Exception {
        final SecurityService<?> ss = SystemInstance.get().getComponent(Assembler.class).getSecurityService();
        ss.disassociate();
	}

	public void complete(Component comp, Event evt) throws Exception {
	}

}
