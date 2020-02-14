package com.collinsrichard.myapi.storage;

import java.sql.*;
import java.util.ArrayList;

public class MySQL {
    String user = "";
    String database = "";
    String password = "";
    String hostname = "";
    Connection connection = null;


    public MySQL(String hostname, String database, String username, String password) {
        this.hostname = hostname;
        this.database = database;
        this.user = username;
        this.password = password;
    }

    public Connection open() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":3306/" + this.database, this.user, this.password);
            return connection;
        } catch (SQLException e) {
            System.out.println("Could not connect to MySQL server! because: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found!");
        }
        return this.connection;
    }

    public boolean checkConnection() {
        if (this.connection != null) {
            return true;
        }
        return false;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void close() {
        try {
            connection.close();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> listTables() {

        ArrayList<String> toRet = new ArrayList<String>();

        try {
            DatabaseMetaData metadata = this.getConnection().getMetaData();
            ResultSet set = metadata.getTables(null, null, null, new String[]{"TABLE"});

            while (set.next()) {
                String user = set.getString(3);
                toRet.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toRet;
    }

    public Boolean tableExists(String table) {
        try {
            ResultSet tables = getConnection().getMetaData().getTables(null, null, table, null);
            return tables.next();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to check if table '" + table + "' exists: " + e.getMessage());
            return false;
        }
    }

    public Boolean columnExists(String tabel, String column) {
        try {
            ResultSet columns = getConnection().getMetaData().getColumns(null, null, tabel, column);
            return columns.next();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to check if column '" + column + "' exists: " + e.getMessage());
            return false;
        }
    }

    public ResultSet select(String query) {
        try {
            return getConnection().createStatement().executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public void insert(String query) {
        try {
            getConnection().createStatement().executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(String query) {
        try {
            getConnection().createStatement().executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(String query) {
        try {
            getConnection().createStatement().executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Boolean execute(String query) {
        try {
            getConnection().createStatement().execute(query);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }
}
