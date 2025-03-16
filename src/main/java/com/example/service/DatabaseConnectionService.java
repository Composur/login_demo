package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class DatabaseConnectionService {

    @Autowired
    private DataSource dataSource;

    public boolean testDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(10); // 10 seconds timeout
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
