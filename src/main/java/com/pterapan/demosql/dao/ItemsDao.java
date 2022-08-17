package com.pterapan.demosql.dao;

import com.pterapan.demosql.model.Category;
import com.pterapan.demosql.model.Items;
import com.pterapan.demosql.util.MyConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemsDao implements DaoInterface<Items>{

    @Override
    public ObservableList<Items> getData() {
        ObservableList<Items> ilist;
        ilist = FXCollections.observableArrayList();

        Connection conn = MyConnection.getConnection();
        String kalimat_sql = "SELECT i.id, i.name, i.price, i.description, i.category_id, c.name AS category_name FROM items i JOIN category c ON i.category_id = c.id";
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(kalimat_sql);
            ResultSet hasil = ps.executeQuery();
            while (hasil.next()) {
                int c_id = hasil.getInt("category_id");
                String c_name = hasil.getString("category_name");
                Category c = new Category(c_id, c_name);

                int id = hasil.getInt("id");
                String name = hasil.getString("name");
                Double price = hasil.getDouble("price");
                String desc = hasil.getString("description");
                Category category = c;
                Items i = new Items(id, name, price, desc, category);
                ilist.add(i);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ilist;
    }

    @Override
    public void addData(Items data) {
        Connection conn = MyConnection.getConnection();
        String kalimat_sql = "INSERT INTO items(name, price, description, category_id) VALUES(?,?,?,?)";
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(kalimat_sql);
            ps.setString(1, data.getName());
            ps.setDouble(2, data.getPrice());
            ps.setString(3, data.getDescription());
            ps.setInt(4, data.getCategory().getId());
            int hasil = ps.executeUpdate();

            if (hasil > 0) {
                System.out.println("berhasil masukin nilai");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delData(Items data) {
        Connection conn;
        conn = MyConnection.getConnection();

        String query = "DELETE FROM items WHERE id=?";
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
    public int updateData(Items data) {
        Connection conn;
        conn = MyConnection.getConnection();

        String query = "UPDATE items SET name=?, price=?, description=?, category_id=? WHERE id=?";
        int hasil = 0;
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, data.getName());
            ps.setDouble(2, data.getPrice());
            ps.setString(3, data.getDescription());
            ps.setInt(4, data.getCategory().getId());
            ps.setInt(5, data.getId());
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
