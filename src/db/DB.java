package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Properties;

import org.h2.tools.Script;

public class DB {

	private static Connection conn = null;

	public static Connection getConnection() {
		if (conn == null)
			try {
				Properties props = loadProperties();
				String driver = props.getProperty("jdbc_driver");
				String url = props.getProperty("dburl");
				String usuario = props.getProperty("user");
				String senha = props.getProperty("pass");
				Class.forName(driver);
				conn = DriverManager.getConnection(url, usuario, senha);

			} catch (Exception e) {
				throw new DBException(e.getMessage());
			}
		return conn;
	}
	
	public static void backupData() {
		if (conn == null) {
			throw new DBException("Não há conexão com o banco de dados");
		} else {
			try {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				String usuario = props.getProperty("user");
				String senha = props.getProperty("pass");
				Script.process(url, usuario, senha, "backups\\backup"+LocalDate.now(),"","");
			} catch (Exception e) {
				throw new DBException(e.getMessage());
			}
		}
	}
	
	public static void lerBackup(String filepath) {
		if (conn == null) {
			throw new DBException("Não há conexão com o banco de dados");
		} else {
			try {
				PreparedStatement st = null;
				st = conn.prepareStatement("RUNSCRIPT FROM '" + filepath+"'");
				st.executeUpdate();
			} catch (SQLException e) {
				throw new DBException(e.getMessage());
			}
		}
	}

	public static void closeConnection() {

		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}

	}

	private static Properties loadProperties() {
		try (FileInputStream fs = new FileInputStream("cfg\\db.properties")) {
			Properties props = new Properties();
			props.load(fs);
			return props;
		} catch (IOException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new DBException(e.getMessage());
			}
		}
	}

	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DBException(e.getMessage());
			}
		}
	}

}
