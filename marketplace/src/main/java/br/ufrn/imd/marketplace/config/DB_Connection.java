package br.ufrn.imd.marketplace.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Component
public class DB_Connection {

    @Value("${database.url}")
    private String url;

    @Value("${database.username}")
    private String username;

    @Value(("${database.password}"))
    private String password;

    private Connection connection;

    public Connection getConnection() throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
