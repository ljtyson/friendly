package springapp.service;

import java.io.Serializable;
import java.util.List;

import springapp.domain.Product;

public interface ProductManager extends Serializable {

	public void increasePrice(int percentage) throws Exception;
	
	List<Product> getProducts() throws Exception;


}