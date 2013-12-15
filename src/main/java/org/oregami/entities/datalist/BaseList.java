package org.oregami.entities.datalist;

import javax.persistence.MappedSuperclass;

import org.oregami.entities.BaseEntity;

@MappedSuperclass
public abstract class BaseList extends BaseEntity {

	private static final long serialVersionUID = 8492563207129737767L;

	public BaseList(String value) {
		super();
		this.value = value;
	}
	
	private String value = null;

	public String getValue() {
		return value;
	}

	
}