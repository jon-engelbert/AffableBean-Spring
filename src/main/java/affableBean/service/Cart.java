package affableBean.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import affableBean.domain.Product;

@Component
public class Cart implements java.io.Serializable {
	private Map<Product, Integer> items = new HashMap<Product, Integer>(0);

	public Map<Product, Integer> getItems() {
		return Collections.unmodifiableMap(this.items);
	}

}
