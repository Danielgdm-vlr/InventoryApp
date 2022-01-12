package com.gdm.forma.inventory.backend.service.intrf;

import java.util.List;

public interface GenericService<T> {

    void createTable();

    List<T> getAll();

    T saveOrUpdate(T t);

    List<T> saveOrUpdateAllArray(T... Ts);

    List<T> saveOrUpdateAllList(List<T> TList);

    T update(T T);

    List<T> updateAllArray(T... ts);

    List<T> updateAllList(List<T> tList);

    void delete(T t);

    void truncate();

    void dropTable();

    void close();
}