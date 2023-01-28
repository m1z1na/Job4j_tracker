package ru.job4j.tracker.store;

import ru.job4j.tracker.model.Item;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SqlTracker implements Store {

    private Connection cn;

    public SqlTracker() {
        init();
    }

    public SqlTracker(Connection cn) {
        this.cn = cn;
    }

    private void init() {
        /*
        try (InputStream in = SqlTracker.class.getClassLoader().getResourceAsStream("app.properties")) {

         */
        try (InputStream in = new FileInputStream("db/liquibase.properties")) {

            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            cn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    @Override
    public void close() throws SQLException {
        if (cn != null) {
            cn.close();
        }
    }

    @Override
    public Item add(Item item) {

        try (PreparedStatement ps = cn.prepareStatement("insert into items(name,created) values(?,?);", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getName());
            ps.setString(2, String.valueOf(item.getCreated()));
            ps.execute();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return item;
    }


    @Override
    public boolean replace(int id, Item item) {

        boolean result = false;
        try (PreparedStatement ps = cn.prepareStatement("update items set name = ?, created = ?where id = ?;")) {
            ps.setString(1, item.getName());
            ps.setString(2, String.valueOf(item.getCreated()));
            ps.setInt(3, id);
            result = ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return result;
    }

    @Override
    public boolean delete(int id) {
        boolean result = false;
        try (PreparedStatement ps = cn.prepareStatement("delete from items where id = ?;")) {
            ps.setInt(1, id);
            result = ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return result;
    }

    @Override
    public List<Item> findAll() {
        List<Item> result = new ArrayList<>();
        try (PreparedStatement ps = cn.prepareStatement("select * from items;")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new Item(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("created")
                    ));
                }
            }

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        return result;
    }

    @Override
    public List<Item> findByName(String key) {

        List<Item> result = new ArrayList<>();
        try (PreparedStatement ps = cn.prepareStatement("select * from items where name like ?;")) {
            ps.setString(1, "%" + key + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new Item(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("created")
                    ));
                }
            }

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return result;
    }

    @Override
    public Item findById(int id) {
        Item result = null;
        try (PreparedStatement ps = cn.prepareStatement("select * from items where id = ?;")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = new Item(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("created")
                    );
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return result;
    }



}