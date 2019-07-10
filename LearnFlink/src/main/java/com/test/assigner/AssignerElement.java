package com.test.assigner;

import java.io.Serializable;

public interface AssignerElement<E> extends Serializable{
	public E getElement();
}
