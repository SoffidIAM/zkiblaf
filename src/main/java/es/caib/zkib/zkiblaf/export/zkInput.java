/* Input.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Tue Nov 29 21:59:11     2005, Created by tomyeh
}}IS_NOTE

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package es.caib.zkib.zkiblaf.export;

import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.ext.client.InputableX;


/**
 * The input tag.
 * Adaptada per Alejandro Usero per a poder enviar dades a un servlet
 * sense que siga per paràmetres url (problemes IE amb llargària)
 * u88683 - 15/03/2011 
 * 
 * @author tomyeh
 */
public class zkInput extends zkAbstractTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public zkInput() {
		this("input"); //$NON-NLS-1$
	}
	protected zkInput(String tagnm) {
		super(tagnm);
		setValue(""); //$NON-NLS-1$
	}

	/** Returns the value of this input.
	 */
	public String getValue() {
		return (String)getDynamicProperty("value"); //$NON-NLS-1$
	}
	/** Sets the vallue of this input.
	 */
	public void setValue(String value) throws WrongValueException {
		setDynamicProperty("value", value); //$NON-NLS-1$
	}

	//-- super --//
	public Object newExtraCtrl() {
		return new InputableX() {
			//-- InputableX --//
			public boolean setTextByClient(String value) throws WrongValueException {
				if (!Objects.equals(value, getValue())) {
					setValue(value);
					return true;
				}
				return false;
			}
		};
	}
}
