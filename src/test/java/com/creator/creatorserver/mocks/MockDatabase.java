package com.creator.creatorserver.mocks;

import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MockDatabase {
    public static final String DB_URL = "jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_DELAY=-1";
    public static final String DB_USER = "sa";
    public static final String DB_PASSWORD = "";

    private static Server server;

    public static void start() throws SQLException {
        server = Server.createTcpServer().start();
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
       connection.createStatement().execute("CREATE TABLE users (id integer, name varchar(50), email varchar(50))");
    }

    public static void stop() {
        server.stop();
    }
}
