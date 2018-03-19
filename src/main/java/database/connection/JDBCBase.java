package database.connection;

import java.sql.*;

public class JDBCBase {
	public static Connection createConnection(String username, String passWd, String className, String url)
			throws Exception {
		try {
			// Class.forName("oracle.jdbc.driver.OracleDriver");
			Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, passWd);

		} catch (SQLException e) {
			throw new Exception(
					"Connection cannot be established. Please check connection settings. " + e.getMessage());
		}

		if (connection == null) {
			throw new Exception("Connection cannot be established.");
		}
		return connection;
	}

	public static ResultSet getResult(Connection conn, String sqlStr) throws SQLException {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sqlStr);
		return rs;
	}	
	

}
