/**
 * 
 */
package com.transnet.common.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**

 * @ClassName: DatabaseBridge

 * @Description: Database connection pool

 * @author: wubo.neusoft@gmail.com

 * @date: 2013-10-01 15:07:51


 */
public class DatabaseBridge {

	private static DatabaseBridge instance = null;

	String serverName;
	String sid;
	String port;
	String service;
	String driverClass;
	String userName;
	String password;

	private DatabaseBridge() {
	}

	
	public static DatabaseBridge getInstance() {
		if (instance == null)
			instance = new DatabaseBridge();
		return instance;
	}
	
	
	
	
	
	public Connection getConnectionMYSQL(){
		service = "jdbc:mysql://115.146.86.163:3306/Transafe";
		//service = "jdbc:mysql://118.138.241.24:3306/transnetDB";
		driverClass = "com.mysql.jdbc.Driver";
		userName = "web_service";
		password = "wsusroll14cva4";
		//userName = "transnetdb";
	    //password = "transnetdb";
		Connection c = null;
		try {
			Class.forName(driverClass);
			c = DriverManager.getConnection (service,userName,password);
		} catch (ClassNotFoundException e) {
			System.out.println("==="+e.getMessage());
		} catch (SQLException e) {
			System.out.println("==="+e.getMessage());
		}
		
		return c;
	}

}
