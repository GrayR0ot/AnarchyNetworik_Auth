package eu.grayroot.anarchyauth;

import com.google.common.hash.Hashing;
import eu.grayroot.anarchyauth.commands.LoginCommand;
import eu.grayroot.anarchyauth.commands.RegisterCommand;
import eu.grayroot.anarchyauth.listener.AuthListener;
import eu.grayroot.anarchyauth.listener.ChatListener;
import eu.grayroot.anarchyauth.sql.SqlConnection;
import gnu.trove.map.hash.THashMap;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.protocol.packet.Chat;
import net.md_5.bungee.protocol.packet.Login;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnarchyAuth extends Plugin {

    private static AnarchyAuth instance;
    private static SqlConnection sql;
    private static Connection sqlConnection;
    private static List<ProxiedPlayer> loggedPlayers;

    @Override
    public void onEnable() {
        instance = this;
        sql = new SqlConnection("jdbc:mysql://", "127.0.0.1", 3306, "dbName", "dbUser", "dbPassword");
        try {
            sql.connect();
            log("Successfully connected to SQL Database!");
        } catch (SQLException | ClassNotFoundException e) {
            log("Unable to connect to Database! Closing the server");
            ProxyServer.getInstance().stop();
            e.printStackTrace();
        }
        sqlConnection = sql.getConnection();
        registerListeners();
        registerCommands();
        loggedPlayers = new ArrayList<>();
    }

    @Override
    public void onDisable() {
        sql.disconnect();
    }

    private void registerListeners() {
        PluginManager pm = getProxy().getPluginManager();
        //pm.registerListener(this, new PacketListener());
        pm.registerListener(this, new AuthListener());
        pm.registerListener(this, new ChatListener());
    }

    private void registerCommands() {
        PluginManager pm = getProxy().getPluginManager();
        pm.registerCommand(this, new RegisterCommand());
        pm.registerCommand(this, new LoginCommand());
    }

    public void log(String log) {
        getLogger().info(log);
    }

    public static AnarchyAuth getInstance() {
        return instance;
    }

    public static List<ProxiedPlayer>getLoggedPlayers() {
        return loggedPlayers;
    }

    public Connection getConnection() {
        return sqlConnection;
    }
}
