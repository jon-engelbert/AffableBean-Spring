package affableBean.cart;

import affableBean.domain.Product;

public class CartItem {


    Product product;
    int quantity;

    public CartItem(Product product) {
        this.product = product;
        quantity = 1;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void incrementQuantity() {
        quantity++;
    }

    public void decrementQuantity() {
        quantity--;
    }

    public double getTotal() {
        double amount = 0;
        amount = (this.getQuantity() * product.getPrice().doubleValue());
        return amount;
    }

	@Override
	public String toString() {
		return "CartItem [product=" + product + ", quantity=" + quantity + "]";
	}
}
