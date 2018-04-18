package com.mulesoft.social;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ChoiceResult implements Serializable{
	
	String artPiece;	
	String paintColor;
	Integer count;
	ArrayList<String> latest = new ArrayList<String>();
	
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
	public void increaseCounter() {
		this.count++;
	}
	public void decreaseCounter() {
		this.count--;
	}
	public ArrayList<String> getLatest() {
		return latest;
	}
	public void setLatest(ArrayList<String> latest) {
		this.latest = latest;
	}
	
	public void addName(String name) {
		this.latest.add(name);
	}
	
	public void removeName(String name) {
		ArrayList<String> allNames = new ArrayList<String>();
		for (String existingName: this.latest) {
			if (!existingName.equals(name)) {
				allNames.add(existingName);
			}
		}
		this.latest = allNames;
	}
	
	@Override
	public String toString() {
		return "ChoiceResults [artPiece=" + artPiece + ", paintColor=" + paintColor + ", count=" + count + ", latest="
				+ latest + "]";
	}
	
}
