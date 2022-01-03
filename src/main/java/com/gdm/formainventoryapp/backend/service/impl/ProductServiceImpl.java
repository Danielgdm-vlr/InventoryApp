package com.gdm.formainventoryapp.backend.service.impl;

import com.gdm.formainventoryapp.backend.model.entity.Product;
import com.gdm.formainventoryapp.backend.repository.ProductRepository;
import com.gdm.formainventoryapp.backend.service.intrf.ProductService;

import javax.persistence.Persistence;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(){

        try{
            productRepository = new ProductRepository(Persistence.createEntityManagerFactory(
                    "demofinalev1"
            ));
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public ProductServiceImpl(String persistenceUnitName){

        try{
            productRepository = new ProductRepository(Persistence.createEntityManagerFactory(
                    persistenceUnitName
            ));
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void createTable(){

        productRepository.createTable();
    }

    public List<Product> getAll(){

        return productRepository.getAll();
    }

    public Product saveOrUpdate(Product product){

        return productRepository.saveOrUpdate(product);
    }

    public List<Product> saveOrUpdateAllArray(Product... products){

        return productRepository.saveOrUpdateAllArray(products);
    }

    public List<Product> saveOrUpdateAllList(List<Product> products){

        return productRepository.saveOrUpdateAllList(products);
    }

    public void delete(Product product){

        productRepository.delete(product.getId());}

    public void truncate(){

        productRepository.truncate(Product.class);
    }

    public Product update(Product product){

        return productRepository.update(product);
    }


    public List<Product> updateAllArray(Product... products){

        return productRepository.updateAllArray(products);
    }

    public List<Product> updateAllList(List<Product> products){

        return productRepository.updateAllList(products);
    }


    public void dropTable(){

        productRepository.dropTable();
    }

    public void close(){

        if(productRepository != null){
            productRepository.close();
        }
    }
}
