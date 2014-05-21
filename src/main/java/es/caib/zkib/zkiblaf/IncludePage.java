package es.caib.zkib.zkiblaf;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.impl.PageImpl;
import org.zkoss.zk.ui.metainfo.PageDefinition;
import org.zkoss.zk.ui.sys.DesktopCtrl;
import org.zkoss.zk.ui.sys.PageConfig;
import org.zkoss.zk.ui.sys.UiEngine;
import org.zkoss.zk.ui.sys.WebAppCtrl;
import org.zkoss.zul.impl.XulElement;

public class IncludePage extends XulElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String url;
	Page includedPage = null;
	boolean initialized;
	private PageDefinition pageDefinition;
	Map args;
	
	public void setPage(Page page) {
		super.setPage(page);
		if (page == null && includedPage != null)
		{
			Desktop d = Executions.getCurrent().getDesktop();
			DesktopCtrl dc = (DesktopCtrl) d;
			includedPage = d.getPage(includedPage.getId());
			includedPage.removeComponents();
			dc.removePage(includedPage);
		}
	}

	public void setParent(Component parent) {
		super.setParent(parent);
		if (parent == null && includedPage != null)
		{
			Desktop d = Executions.getCurrent().getDesktop();
			DesktopCtrl dc = (DesktopCtrl) d;
			includedPage = d.getPage(includedPage.getId());
			includedPage.removeComponents();
			dc.removePage(includedPage);
		}
	}


	public void redraw(Writer out) throws IOException {
		out.write("<div id=\""); //$NON-NLS-1$
		out.write(getUuid());
		out.write('"');
		out.write(getOuterAttrs());
		out.write(getInnerAttrs());
		out.write(">\n"); //$NON-NLS-1$
		for (Iterator it = includedPage.getRoots().iterator(); it.hasNext(); )
		{
			Component c = (Component) it.next();
			c.redraw(out);
		}
		out.write("\n</div>"); //$NON-NLS-1$
	}

	
	public IncludePage(String url, Map args) {
		super();
		this.url = url;
		this.args = args;
		if ( !initialized )
		{
			initialized = true;

			Execution execution = Executions.getCurrent();
			final UiEngine ueng = ((WebAppCtrl)execution.getDesktop().getWebApp()).getUiEngine();

			pageDefinition = execution.getPageDefinition(url);
			includedPage = new PageImpl(pageDefinition);
			ueng.createComponents(execution, pageDefinition, includedPage, null, args);
			pageDefinition.init(includedPage, true);
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
