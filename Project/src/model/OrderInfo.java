package model;

import java.io.Serializable;

public class OrderInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String menu;
	private int quantity;
	private int price;
	
	public OrderInfo(String menu, int quantity, int price) {
		this.menu = menu;
		this.quantity = quantity;
		this.price = price;
	}

	public String getMenu() {
		return menu;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public int getPrice() {
		return price;
	}
	
	public String toString() {
		String order = "메뉴: " + menu + ", 수량: " + quantity + ", 가격: " + price;
		return order;
	}
	

}
