package affableBean.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import affableBean.cart.Cart;
import affableBean.cart.CartItem;
import affableBean.domain.Member;
import affableBean.domain.PaymentInfo;
import affableBean.domain.CustomerOrder;
import affableBean.domain.OrderedProduct;
import affableBean.domain.OrderedProductPK;
import affableBean.domain.Product;
import affableBean.repository.CustomerOrderRepository;
import affableBean.repository.MemberRepository;
import affableBean.repository.PaymentInfoRepository;
import affableBean.repository.OrderedProductRepository;
import affableBean.repository.ProductRepository;

@Service
@Transactional
public class OrderService {
	
	@Autowired 
	private MemberRepository customerRepo;

	@Autowired 
	private PaymentInfoService paymentInfoService;

	@Autowired
	private CustomerOrderRepository customerOrderRepo;

	@Autowired
	private OrderedProductRepository orderedProductRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
    public int placeOrder(PaymentInfo newPaymentInfo, Cart cart) {
    	
        try {
            PaymentInfo paymentInfo = paymentInfoService.editPaymentInfo(newPaymentInfo);
            System.out.println("after new paymentInfo " + paymentInfo.getOwner().getName() + " id " + paymentInfo.getId());

            CustomerOrder order = addOrder(paymentInfo, cart);
            addOrderedItems(order, cart);
            return order.getId();
        } catch (Exception e) {
//            context.setRollbackOnly();
        	e.printStackTrace();
            return 0;
        }
    }

    private CustomerOrder addOrder(PaymentInfo paymentInfo, Cart cart) {

        // set up customer order
        CustomerOrder order = new CustomerOrder();
        order.setPaymentInfo(paymentInfo);
        order.setAmount(BigDecimal.valueOf(cart.getTotal()));

        // create confirmation number
        Random random = new Random();
        int i = random.nextInt(999999999);
        order.setConfirmationNumber(i);
        order.setDateCreated(new Date());

//        em.persist(order);

        return customerOrderRepo.saveAndFlush(order);
    }

    private void addOrderedItems(CustomerOrder order, Cart cart) {

//        em.flush();

        List<CartItem> items = cart.getItems();

        // iterate through shopping cart and create OrderedProducts
        for (CartItem scItem : items) {

            int productId = scItem.getProduct().getId();

            // set up primary key object
            OrderedProductPK orderedProductPK = new OrderedProductPK();
            orderedProductPK.setCustomerOrderId(order.getId());
            orderedProductPK.setProductId(productId);

            // create ordered item using PK object
            OrderedProduct orderedItem = new OrderedProduct(orderedProductPK);

            // set quantity
            orderedItem.setQuantity(scItem.getQuantity());

            try {
            	orderedProductRepo.saveAndFlush(orderedItem);
            } catch (Exception e) {
            	System.out.println("error saving OrderedProduct: " + e.getMessage());
            }
            

        }
    }

    public Map<String, Object> getOrderDetails(int orderId) {

        Map<String, Object> orderMap = new HashMap<String, Object>();

        // get order
        CustomerOrder order = customerOrderRepo.findById(orderId);

        // get customer
        Member customer = order.getCustomer();

        // get all ordered products
        List<OrderedProduct> orderedProducts = orderedProductRepo.findByCustomerOrderId(orderId);

        // get product details for ordered items
        List<Product> products = new ArrayList<Product>();

        for (OrderedProduct op : orderedProducts) {

            Product p = (Product) productRepo.findById(op.getOrderedProductPK().getProductId());
            products.add(p);
        }

        // add each item to orderMap
        orderMap.put("orderRecord", order);
        orderMap.put("customer", customer);
        orderMap.put("orderedProducts", orderedProducts);
        orderMap.put("products", products);

        return orderMap;
    }
}
