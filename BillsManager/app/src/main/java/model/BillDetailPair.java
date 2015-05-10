package model;

import java.io.Serializable;

import org.apache.http.entity.SerializableEntity;

public class BillDetailPair implements Serializable{
	
	private String key;
	private String value;
	private int type;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	

}
