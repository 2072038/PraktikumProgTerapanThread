package com.pterapan.demosql.dao;

import com.pterapan.demosql.model.Category;
import com.pterapan.demosql.util.MyConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDao implements DaoInterface<Category> {
    @Override
    public ObservableList<Category> getData() {
        ObservableList<Category> clist;
        clist = FXCollections.observableArrayList();

        Connection conn = MyConnection.getConnection();
        String kalimat_sql = "SELECT * FROM category";
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(kalimat_sql);
            ResultSet hasil = ps.executeQuery();
            while (hasil.next()) {
                int id = hasil.getInt("id");
                String name = hasil.getString("name");
                Category c = new Category(id, name);
                clist.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clist;
    }

    @Override
    public void addData(Category data) {
        Connection conn = MyConnection.getConnection();
        String kalimat_sql = "INSERT INTO category(name) VALUES(?)";
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(kalimat_sql);
            ps.setString(1, data.getName());
            int hasil = ps.executeUpdate();

            if (hasil > 0) {
                System.out.println("berhasil masukin nilai");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delData(Category data) {
        Connection conn;
        conn = MyConnection.getConnection();

        String query = "DELETE FROM category WHERE id=?";
        int hasil = 0;
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, data.getId());
            hasil = ps.executeUpdate();
            if (hasil > 0) {
                System.out.println("berhasil hapus data");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hasil;
    }

    @Override
    public int updateData(Category data) {
        Connection conn;
        conn = MyConnection.getConnection();

        String query = "UPDATE category SET name=? WHERE id=?";
        int hasil = 0;
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, data.getId());
            hasil = ps.executeUpdate();
            if (hasil > 0) {
                System.out.println("berhasil update data");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hasil;
    }
}
