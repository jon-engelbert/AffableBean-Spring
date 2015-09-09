package affableBean.cart;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import affableBean.domain.Product;

// Synchronized (all class methods are synchronized with an implicit class-instance lock) protects the list from crazy states by only allowing one edit
@Component
public class Cart implements java.io.Serializable {
    /**
	 * 
	 */
	
	public static final Double _deliverySurcharge = 3.00d;
	
	private static final long serialVersionUID = 1L;
	List<CartItem> items;
    int numberOfItems;
    double total;

    public Cart() {
        items = new ArrayList<CartItem>();
        numberOfItems = 0;
        total = 0;
    }

    /**
     * Adds a <code>ShoppingCartItem</code> to the <code>ShoppingCart</code>'s
     * <code>items</code> list. If item of the specified <code>product</code>
     * already exists in shopping cart list, the quantity of that item is
     * incremented.
     *
     * @param product the <code>Product</code> that defines the type of shopping cart item
     * @see ShoppingCartItem
     */
    public synchronized void addItem(Product product) {

        boolean newItem = true;

        for (CartItem scItem : items) {

            if (scItem.getProduct().getId() == product.getId()) {

                newItem = false;
                scItem.incrementQuantity();
            }
        }

        if (newItem) {
        	CartItem scItem = new CartItem(product);
            items.add(scItem);
        }
    }

    /**
     * Updates the <code>ShoppingCartItem</code> of the specified
     * <code>product</code> to the specified quantity. If '<code>0</code>'
     * is the given quantity, the <code>ShoppingCartItem</code> is removed
     * from the <code>ShoppingCart</code>'s <code>items</code> list.
     *
     * @param product the <code>Product</code> that defines the type of shopping cart item
     * @param quantity the number which the <code>ShoppingCartItem</code> is updated to
     * @see ShoppingCartItem
     */
    public synchronized void update(Product product, String quantity) {

        short qty = -1;

        // cast quantity as short
        qty = Short.parseShort(quantity);

        if (qty >= 0) {

        	CartItem item = null;

            for (CartItem scItem : items) {

                if (scItem.getProduct().getId() == product.getId()) {

                    if (qty != 0) {
                        // set item quantity to new value
                        scItem.setQuantity(qty);
                    } else {
                        // if quantity equals 0, save item and break
                        item = scItem;
                        break;
                    }
                }
            }

            if (item != null) {
                // remove from cart
                items.remove(item);
            }
        }
    }

    /**
     * Returns the list of <code>ShoppingCartItems</code>.
     *
     * @return the <code>items</code> list
     * @see ShoppingCartItem
     */
    public synchronized List<CartItem> getItems() {

        return items;
    }

    /**
     * Returns the sum of quantities for all items maintained in shopping cart
     * <code>items</code> list.
     *
     * @return the number of items in shopping cart
     * @see ShoppingCartItem
     */
    public synchronized int getNumberOfItems() {

        numberOfItems = 0;

        for (CartItem scItem : items) {

            numberOfItems += scItem.getQuantity();
        }

        return numberOfItems;
    }

    /**
     * Returns the sum of the product price multiplied by the quantity for all
     * items in shopping cart list. This is the total cost excluding the surcharge.
     *
     * @return the cost of all items times their quantities
     * @see ShoppingCartItem
     */
    public synchronized double getSubtotal() {

        double amount = 0;

        for (CartItem scItem : items) {
            Product product = (Product) scItem.getProduct();
            amount += (scItem.getQuantity() * product.getPrice().doubleValue());
        }

        return amount;
    }

    /**
     * Calculates the total cost of the order. This method adds the subtotal to
     * the designated surcharge and sets the <code>total</code> instance variable
     * with the result.
     *
     * @param surcharge the designated surcharge for all orders
     * @see ShoppingCartItem
     */
    public synchronized void calculateTotal(String surcharge) {

        double amount = 0;

        // cast surcharge as double
        double s = Double.parseDouble(surcharge);

        amount = this.getSubtotal();
        amount += s;

        total = amount;
    }

    /**
     * Returns the total cost of the order for the given
     * <code>ShoppingCart</code> instance.
     *
     * @return the cost of all items times their quantities plus surcharge
     */
    public synchronized double getTotal() {

        return total;
    }

    /**
     * Empties the shopping cart. All items are removed from the shopping cart
     * <code>items</code> list, <code>numberOfItems</code> and
     * <code>total</code> are reset to '<code>0</code>'.
     *
     * @see ShoppingCartItem
     */
    public synchronized void clear() {
        items.clear();
        numberOfItems = 0;
        total = 0;
    }

	@Override
	public String toString() {
		return "Cart [items=" + items + ", numberOfItems=" + numberOfItems
				+ ", total=" + total + "]";
	}
}
