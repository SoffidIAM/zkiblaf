package es.caib.zkib.util;

import java.io.Serializable;

public class SynchronizableBoolean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean bool=true;
	public SynchronizableBoolean(boolean value){
		this.bool=value;
	}
	
	public synchronized boolean get(){
		return this.bool;
	}
	
	public synchronized void set(boolean value){
		this.bool=value;
	}
	
	
}
