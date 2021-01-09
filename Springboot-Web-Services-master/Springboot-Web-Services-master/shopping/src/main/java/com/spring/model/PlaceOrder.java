package com.spring.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PlaceOrder")
public class PlaceOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int orderId;
	private String email, orderStatus;
	private Date orderDate;
	private double totalCost;
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "userid", nullable = false)
	private User user;
	public User getUser() {
		return user;
	}
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "orderId")
	private Bufcart buf;
	public Bufcart getBuf() {
		return buf;
	}

	public void setBuf(Bufcart buf) {
		this.buf = buf;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public PlaceOrder(int orderId, String email, String orderStatus, Date orderDate, double totalCost) {
		super();
		this.orderId = orderId;
		this.email = email;
		this.orderStatus = orderStatus;
		this.orderDate = orderDate;
		this.totalCost = totalCost;
	}

	public PlaceOrder() {
		super();
	}

}