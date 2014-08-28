package es.caib.zkib.zkiblaf.jboss;

import java.util.List;

import org.jboss.security.SecurityAssociation;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventThreadCleanup;

public class JBossEventThreadCleanup implements EventThreadCleanup {

	public void cleanup(Component comp, Event evt, List errs) throws Exception {
		SecurityAssociation.popSubjectContext();
	}

	public void complete(Component comp, Event evt) throws Exception {
	}

}
