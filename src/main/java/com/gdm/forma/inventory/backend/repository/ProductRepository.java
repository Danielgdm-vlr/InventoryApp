package com.gdm.forma.inventory.backend.repository;

import com.gdm.forma.inventory.backend.domain.entity.Product;
import com.gdm.forma.inventory.frontend.utils.service.CustomAlertService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ProductRepository extends GenericRepository<Product> {

    private final EntityManagerFactory entityManagerFactory;

    public ProductRepository(EntityManagerFactory entityManagerFactory){

        super(Product.class);
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public EntityManager getEntityManager(){

        try{
            return entityManagerFactory.createEntityManager();
        }catch (Exception exception){
            CustomAlertService.getExceptionAlert(exception);
            return null;
        }
    }

    @Override
    public void createTable(){

        EntityManager entityManager = getEntityManager();

        try {
            entityManager.getTransaction().begin();

            String query = "CREATE TABLE IF NOT EXISTS product(" +
                    "id bigint auto_increment NOT NULL PRIMARY KEY," +
                    "name varchar(255)," +
                    "quantity bigint," +
                    "quantityNeeded" +
                    "defaultQuantityPerWeek bigint);";
            System.out.println();

            entityManager.createNativeQuery(query).executeUpdate();
            entityManager.getTransaction().commit();
        }catch (Exception exception){
            entityManager.getTransaction().rollback();
            CustomAlertService.getExceptionAlert(exception);
        }
        finally {
            entityManager.close();
        }
    }

    @Override
    public void dropTable() {

        EntityManager entityManager = getEntityManager();

        try {
            entityManager.getTransaction().begin();

            String query = "DROP TABLE product CASCADE";
            System.out.println();

            entityManager.createNativeQuery(query).executeUpdate();
            entityManager.getTransaction().commit();
        }catch (Exception exception){
            entityManager.getTransaction().rollback();
            CustomAlertService.getExceptionAlert(exception);
        }
        finally {
            entityManager.close();
        }
    }
}