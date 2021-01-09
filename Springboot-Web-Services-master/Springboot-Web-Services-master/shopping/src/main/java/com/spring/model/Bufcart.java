package com.spring.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Bufcart implements Serializable {

	private static final long serialVersionUID = 4049687597028261161L;



	@Override
	public String toString() {
		return "Bufcart [bufcartId=" + bufcartId + ", orderId=" + orderId + ", email=" + email + ", dateAdded="
				+ dateAdded + ", quantity=" + quantity + ", price=" + price + ", productId=" + productId
				+ ", productname=" + productname + "]";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int bufcartId;

	private String email;

	private Date dateAdded;

	private int quantity;

	private double price;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="productid")
	private Product productId;

	private String productname;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private PlaceOrder orderId;

	public PlaceOrder getOrderId() {
		return orderId;
	}

	public void setOrderId(PlaceOrder orderId) {
		this.orderId = orderId;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public Product getProductId() {
		return productId;
	}

	public void setProductId(Product productId) {
		this.productId = productId;
	}

	public int getBufcartId() {
		return bufcartId;
	}

	public void setBufcartId(int bufcartId) {
		this.bufcartId = bufcartId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}