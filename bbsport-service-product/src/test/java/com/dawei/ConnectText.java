package com.dawei;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;


public class ConnectText {
	@Test
	public void testName() throws Exception {
		String url="jdbc:mysql://localhost:3306/buycar?characterEncoding=UTF-8"; 
		String name="root";
		String pwd="root"; 
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(url,name,pwd);
		System.out.println(conn);
	}
}
