package springapp.domain;

import junit.framework.TestCase;

public class ProductTests {

	private Product product;

	protected void setUp() throws Exception {
		product = new Product();
	}

	public void testSetAndGetDescription() {
		String description = "aDescription";
		assertNull(product.getDescription());
		product.setDescription(description);
		assertEquals(description, product.getDescription());
	}

	public void testSetAndGetPrice() {
		Double testPrice = 100.0;
		assertEquals(0,0,0);
		product.setPrice(testPrice);
		assertEquals(testPrice, product.getPrice(),0);
	}
	
}