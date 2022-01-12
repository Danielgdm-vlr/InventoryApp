package com.gdm.forma.inventory.app.utils.service;

import com.gdm.forma.inventory.backend.domain.entity.Product;
import com.gdm.forma.inventory.backend.service.impl.ProductServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DatabaseService {

    //method only for demo project
    public static void initDataBaseData(ProductServiceImpl productService){

        List<Product> productList = new ArrayList<>();
        List<String> productNames = getProductNamesFromFile();

        productNames
                .forEach(productName -> productList.add(new Product(
                        productName,
                        3,
                        7
                )));

        productService.saveOrUpdateAllList(productList);
    }

    //method only for demo project
    private static List<String> getProductNamesFromFile(){

        Path path = Path.of("src/main/resources/db/products.txt");

        List<String> productNames = new ArrayList<>();

        try(Stream<String> productsFileLines = Files.lines(path)) {
            productsFileLines.forEach(productNames::add);
        }catch (Exception exception){
            exception.printStackTrace();
        }

        return productNames;
    }
}