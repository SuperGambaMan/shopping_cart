package org.iesvdm.shopping_cart.service;

import org.iesvdm.shopping_cart.model.Product;
import org.iesvdm.shopping_cart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getAll (){
        return productRepository.findAll();
    }

}
