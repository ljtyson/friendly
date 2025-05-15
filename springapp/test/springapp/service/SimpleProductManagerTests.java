package springapp.service

import junit.framework.TestCase;

public class SimpleProductManagerTests extends TestCase {
	
	private SimpleProductManager productManager;
	
	protected void setUp() {
		productManager = new SimpleProductManager();
	}
	
	public void testGetProductsWithNoProducts() {
		productManager = new SimpleProductManager();
		assertNotNull(productManager.getProducts());
	}

	public void testGetProducts() {
		assertNotNull(productManager.getProducts());
	}
	
	public void testIncreasePrice() {
		productManager.increasePrice(10);
		assertEquals(110, productManager.getProducts().get(0).getPrice());
	}
	
	public void testSetProducts() {
		List<Product> products = new ArrayList<>();
		Product product = new Product();
		product.setPrice(100);
		products.add(product);
		
		productManager.setProducts(products);
		
		assertEquals(1, productManager.getProducts().size());
		assertEquals(100, productManager.getProducts().get(0).getPrice());
	}
}