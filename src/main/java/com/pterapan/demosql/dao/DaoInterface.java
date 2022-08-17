package com.pterapan.demosql.dao;

import javafx.collections.ObservableList;

public interface DaoInterface<T> {
    ObservableList<T> getData();
    void addData(T data);
    int delData(T data);
    int updateData(T data);
}
