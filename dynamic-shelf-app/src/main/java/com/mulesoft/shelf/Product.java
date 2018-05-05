package com.mulesoft.shelf;

import java.io.Serializable;

public class Product implements Serializable{

	private static final long serialVersionUID = -7560608042940550195L;
	Integer id;
	String name;
	Double price;
	String description;
	Double discountedPrice;
	public Double getDiscountedPrice() {
		return discountedPrice;
	}
	public void setDiscountedPrice(Double discountedPrice) {
		this.discountedPrice = discountedPrice;
	}
	String image;
	Boolean checkedOut;
	public Integer getId() {
		return id;
	}
	public void setId(String id) {
		this.id = Integer.valueOf(id);
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Boolean getCheckedOut() {
		return checkedOut;
	}
	public void setCheckedOut(Boolean checkedOut) {
		this.checkedOut = checkedOut;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", price=" + price + ", description=" + description
				+ ", discountedPrice=" + discountedPrice + ", image=" + image + ", checkedOut=" + checkedOut + "]";
	}
}
