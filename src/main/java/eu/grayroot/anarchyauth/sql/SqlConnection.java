package eu.grayroot.anarchyauth.sql;

import eu.grayroot.anarchyauth.AnarchyAuth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection {

	private Connection connection;
	private int port;
	private String urlbase,host,database,user,pass;

	public SqlConnection(String urlbase, String host, int port, String database, String user, String pass) {
		this.urlbase = urlbase;
		this.host = host;
		this.port = port;
		this.database = database;
		this.user = user;
		this.pass = pass;
	}

	public void connect() throws SQLException, ClassNotFoundException {
		if (connection != null && !connection.isClosed()) {
			return;
		}

		synchronized (this) {
			if (connection != null && !connection.isClosed()) {
				return;
			}
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + this.host+ ":" + this.port + "/" + this.database, this.user, this.pass);
		}
	}

	public void disconnect(){
		if(isConnected()){
			try {
				connection.close();
				AnarchyAuth.getInstance().log("Successfully disconnected to SQL Database!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isConnected(){
		return connection != null;
	}
	
	public Connection getConnection() {
		return connection;
	}
}