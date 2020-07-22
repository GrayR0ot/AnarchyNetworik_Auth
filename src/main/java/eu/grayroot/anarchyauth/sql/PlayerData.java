package eu.grayroot.anarchyauth.sql;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.google.common.hash.Hashing;
import eu.grayroot.anarchyauth.AnarchyAuth;
import eu.grayroot.anarchyauth.object.AnarchyPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerData {
	
	private Connection connection;
	
	public PlayerData(Connection connection) {
		this.connection = AnarchyAuth.getInstance().getConnection();
	}
	
	public boolean isRegistered(ProxiedPlayer player){
		try {
			PreparedStatement q = connection.prepareStatement("SELECT `name` FROM `players` WHERE `name` = ?");
			q.setString(1, player.getName());
			ResultSet resultat = q.executeQuery();
			boolean isRegistered = resultat.next();
			q.close();
			return isRegistered;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public void registerPlayer(ProxiedPlayer player, String password){
		try {
			PreparedStatement rs = connection.prepareStatement("INSERT INTO `players` (name, uuid, password, registration_ip) VALUES (?,?,?,?)");
			rs.setString(1, player.getName());
			rs.setString(2, player.getUniqueId().toString());
			rs.setString(3, Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString());
			rs.setString(4, player.getSocketAddress().toString().substring(0, player.getSocketAddress().toString().indexOf(":")));
			rs.executeUpdate();
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public AnarchyPlayer getPlayerData(ProxiedPlayer player) {
		AnarchyPlayer anarchyPlayer = null;
		try {
			PreparedStatement q = connection.prepareStatement("SELECT * FROM `players` WHERE `name` = ?");
			q.setString(1, player.getName());
			ResultSet rs = q.executeQuery();
			while (rs.next()) {
				anarchyPlayer = new AnarchyPlayer(rs.getInt("id"),
						rs.getString("name"),
						rs.getString("uuid"),
						rs.getString("password"),
						rs.getString("registration_ip"),
						rs.getBoolean("auth"),
						rs.getTimestamp("created_at"),
						rs.getTimestamp("updated_at"));
			}
			q.close();
			return anarchyPlayer;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return anarchyPlayer;
	}

	public void updatePlayerData(ProxiedPlayer player, boolean auth){
		try {
			PreparedStatement rs = connection.prepareStatement("UPDATE `players` SET `name`=?, `auth`=? WHERE `uuid`=?");
			rs.setString(1, player.getName());
			rs.setBoolean(2, auth);
			rs.setString(3, player.getUniqueId().toString());
			rs.executeUpdate();
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
