/* Form.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Tue Dec 13 14:52:28     2005, Created by tomyeh
}}IS_NOTE

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package es.caib.zkib.zkiblaf.export;



/**
 * The FORM tag. 
 * Adaptada per Alejandro Usero per a poder enviar dades a un servlet
 * sense que siga per paràmetres url (problemes IE amb llargària)
 * u88683 - 15/03/2011 
 * 
 * @author tomyeh
 */
public class zkForm extends zkAbstractTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public zkForm() {
		super("form"); //$NON-NLS-1$
	}
}
