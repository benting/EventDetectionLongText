package com.EventDetection.MySQL;

import java.sql.*;

public class MySQLAPI {
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String URL = "jdbc:MySQL://202.117.16.97:3306/yuqing";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "root";

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public Connection getConnection() throws Exception {
	Class.forName(DRIVER);
	connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	return connection;
    }

    public ResultSet executeQuery(String sql) throws Exception {
	preparedStatement = connection.prepareStatement(sql);
	resultSet = preparedStatement.executeQuery();
	return resultSet;
    }

    public int executeUpdate(String sql) throws Exception {
	preparedStatement = connection.prepareStatement(sql);
	return preparedStatement.executeUpdate();
    }

    public void closeAll() throws Exception {
	if (null != resultSet)
	    resultSet.close();
	if (null != preparedStatement)
	    preparedStatement.close();
	if (null != connection)
	    connection.close();
    }
}