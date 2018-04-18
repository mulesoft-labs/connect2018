package com.mulesoft.social;

import java.io.Serializable;
import java.util.Arrays;

@SuppressWarnings("serial")
public class Recommendation implements Serializable{
	String clientId;	
	String name;
	Integer count[];
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer[] getCount() {
		return count;
	}
	public void setCount(Integer[] count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "Recommendation [clientId=" + clientId + ", name=" + name + ", count=" + Arrays.toString(count) + "]";
	}

	
}
