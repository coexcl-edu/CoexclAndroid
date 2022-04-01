package com.home.coexcleducation.database;

/**
 * Created by ABHIshek on 18/02/16.
 */
public interface QueryHelper {

    void insertAll(Object listObj);
    void insert(Object obj);
    void deleteAllRecord();
    void deleteById(String id);
    int getCount();
    Object getAll();
    Object getById(String id);

}
