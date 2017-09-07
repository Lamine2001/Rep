package com.siemens.windpower.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DB_Connection {

	public List<Map<String, Object>> getData(String query) 
	{
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try
		{  
			String url = "jdbc:sqlserver://DKBDKB7QKA;databaseName=ARM;integratedSecurity=true";
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			Connection con = DriverManager.getConnection(url);
			
			Statement stmt=con.createStatement();  
			//query = "SELECT TOP 10 Measure,Year,Value FROM dbo.FailureRates where Measure ='Major crane hours' or Measure = 'Major non-crane hours'";
			ResultSet rs=stmt.executeQuery(query);  
			ResultSetMetaData meta = rs.getMetaData();
			Map<String, Object> map = new HashMap<String, Object>();
			while (rs.next()) {
				map = new HashMap<String, Object>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					String key = null;
					Object value = null;
					key = meta.getColumnName(i);
					value = rs.getObject(key);
					map.put(key, value);
					
				}
				result.add(map);
				
			}
			con.close();  
			}
		catch(Exception e)
		{ 
			e.printStackTrace();
		}
		return result;  
		
	}
}