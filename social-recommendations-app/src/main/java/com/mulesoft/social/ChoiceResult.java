package com.mulesoft.social;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class ChoiceResult implements Serializable{
	
	String artPiece;	
	String paintColor;
	Integer count;
	
	private Map<String,String> nameMap = new HashMap<String, String>();
	List<String> last = new ArrayList<String>();
	
	public List<String> getLast() {
		int size = this.last.size();
		List<String> last3 = this.last;
		if (size >= 3) {
			last3 = this.last.subList(size - 3, size);
		}
		return last3;
	}
	public void setLast(List<String> lastThree) {
		this.last = lastThree;
	}
	public String getArtPiece() {
		return artPiece;
	}
	public void setArtPiece(String artPiece) {
		this.artPiece = artPiece;
	}
	public String getPaintColor() {
		return paintColor;
	}
	public void setPaintColor(String paintColor) {
		this.paintColor = paintColor;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public synchronized void increaseCounter() {
		this.count++;
	}
	public synchronized void decreaseCounter() {
		this.count--;
	}
	
	public synchronized void addName(String clientId, String name) {
		if (!this.nameMap.containsKey(clientId)) {
			this.nameMap.put(clientId, name);
			this.last.add(name);
		}
	}
	
	public synchronized void removeName(String clientId) {
		String name = this.nameMap.get(clientId);
		this.nameMap.remove(clientId);	
		this.last.remove(name);
	}
	@Override
	public String toString() {
		return "ChoiceResult [artPiece=" + artPiece + ", paintColor=" + paintColor + ", count=" + count + ", nameMap="
				+ nameMap + ", last=" + last + "]";
	}
	
}
