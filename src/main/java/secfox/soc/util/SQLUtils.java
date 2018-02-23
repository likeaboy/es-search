package secfox.soc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;

public class SQLUtils {

	public static Connection createConn(String host,String port,String user,String pwd) throws Exception{
		Connection conn = null; 
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); 
        conn = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/soc", user, pwd); //链接本地MYSQL
        
        return conn;
	}
	
	public static long executeDelSQL(Connection conn,String sql) throws Exception{
		 Statement stmt = conn.createStatement();
	     return stmt.executeUpdate(sql);
	}
	
	public static ResultSet executeQuerySQL(Connection conn,String sql) throws Exception{
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sql);
	}
	
}
