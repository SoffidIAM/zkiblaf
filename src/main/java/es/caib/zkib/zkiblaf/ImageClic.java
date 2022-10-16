package es.caib.zkib.zkiblaf;

import org.zkoss.xml.HTMLs;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Image;

public class ImageClic extends Image
{
	private static final long serialVersionUID = 1L;

	private boolean disabled = false;
	private String title;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ImageClic ()
	{
		super();
		setSclass("imageclic"); //$NON-NLS-1$
	}

	public ImageClic (String src)
	{
		super(src);
		setSclass("imageclic"); //$NON-NLS-1$
	}

	/**
	 * @return the disabled
	 */
	public boolean isDisabled ()
	{
		return disabled;
	}

	/**
	 * @param disabled
	 *            the disabled to set
	 */
	public void setDisabled (boolean disabled)
	{
		this.disabled = disabled;
		invalidate();
	}

	// -- component developer only --//
	/**
	 * Returns the attributes for onClick, onRightClick and onDoubleClick by checking
	 * whether the corresponding listeners are added, or null if none is added.
	 * 
	 * @since 3.0.5
	 */
	protected String getAllOnClickAttrs ()
	{
		StringBuffer sb = null;
		if (!disabled)
		{
			sb = appendAsapAttr(sb, Events.ON_CLICK);
			sb = appendAsapAttr(sb, Events.ON_DOUBLE_CLICK);
			sb = appendAsapAttr(sb, Events.ON_RIGHT_CLICK);
		}
		return sb != null ? sb.toString() : null;
	}

	public String getInnerAttrs() {
		final StringBuffer sb =
			new StringBuffer(64).append(super.getInnerAttrs());
		HTMLs.appendAttribute(sb, "title", title);
		return sb.toString();
	}

}
