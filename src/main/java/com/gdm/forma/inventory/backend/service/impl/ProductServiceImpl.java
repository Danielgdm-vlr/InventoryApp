package com.gdm.forma.inventory.backend.service.impl;

import com.gdm.forma.inventory.app.utils.logging.CustomLoggerType;
import com.gdm.forma.inventory.app.utils.logging.CustomLogger;
import com.gdm.forma.inventory.app.utils.service.CustomService;
import com.gdm.forma.inventory.backend.domain.entity.Product;
import com.gdm.forma.inventory.backend.repository.ProductRepository;
import com.gdm.forma.inventory.backend.service.intrf.ProductService;
import com.gdm.forma.inventory.frontend.utils.service.CustomAlertService;

import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(){

        try{
            productRepository = new ProductRepository(Persistence.createEntityManagerFactory(
                    "demofinalev1"
            ));
            CustomLogger.log(CustomLoggerType.DB, "Product service object initialized");
        }catch (Exception exception) {
            CustomAlertService.getExceptionAlert(exception);
            CustomLogger.log(CustomLoggerType.EXCEPTION,
                    String.format("%1$s\n%2$s",
                            "Exception occurred while initializing product service object",
                            CustomService.getExceptionFullMessage(exception)));
        }

        ;
    }

    public ProductServiceImpl(String persistenceUnitName){

        try{
            productRepository = new ProductRepository(Persistence.createEntityManagerFactory(
                    persistenceUnitName
            ));
            CustomLogger.log(CustomLoggerType.DB, "Product service object initialized");
        }catch (Exception exception) {
            CustomAlertService.getExceptionAlert(exception);
            CustomLogger.log(CustomLoggerType.EXCEPTION,
                    String.format("%1$s\n%2$s",
                            "Exception occurred while initializing product service object",
                            Arrays.toString(CustomService.getExceptionFullMessage(exception))));
        }
    }

    public void createTable(){

        productRepository.createTable();
        CustomLogger.log(CustomLoggerType.DB, "User created a product table");
    }

    public List<Product> getAll(){

        CustomLogger.log(CustomLoggerType.DB, "User returned all the products from db");
        return productRepository.getAll();
    }

    public Product saveOrUpdate(Product product){

        CustomLogger.log(CustomLoggerType.DB, "User saved or updated a product");
        return productRepository.saveOrUpdate(product);
    }

    public List<Product> saveOrUpdateAllArray(Product... products){

        CustomLogger.log(CustomLoggerType.DB, "User saved or updated an array of products");
        return productRepository.saveOrUpdateAllArray(products);
    }

    public List<Product> saveOrUpdateAllList(List<Product> products){

        CustomLogger.log(CustomLoggerType.DB, "User saved or updated a list of products");
        return productRepository.saveOrUpdateAllList(products);
    }

    public Product update(Product product){

        CustomLogger.log(CustomLoggerType.DB, "User updated a product");
        return productRepository.update(product);
    }

    public List<Product> updateAllArray(Product... products){

        CustomLogger.log(CustomLoggerType.DB, "User updated an array of products");
        return productRepository.updateAllArray(products);
    }

    public List<Product> updateAllList(List<Product> products){

        CustomLogger.log(CustomLoggerType.DB, "User updated a list of products");
        return productRepository.updateAllList(products);
    }

    public void delete(Product product){

        CustomLogger.log(CustomLoggerType.DB, "User deleted a product");
        productRepository.delete(product.getId());}

    public void truncate(){

        CustomLogger.log(CustomLoggerType.DB, "User truncated product table");
        productRepository.truncate(Product.class);
    }

    public void dropTable(){

        CustomLogger.log(CustomLoggerType.DB, "User dropped product table");
        productRepository.dropTable();
    }

    public void close(){

        CustomLogger.log(CustomLoggerType.DB, "Product service object has closed");
        if(productRepository != null){
            productRepository.close();
        }
    }
}