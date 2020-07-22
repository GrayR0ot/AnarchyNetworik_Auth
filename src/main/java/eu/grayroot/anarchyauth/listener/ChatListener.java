package eu.grayroot.anarchyauth.listener;

import eu.grayroot.anarchyauth.AnarchyAuth;
import eu.grayroot.anarchyauth.sql.PlayerData;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
public class ChatListener implements Listener {

    @EventHandler
    public void onChat(ChatEvent e) {
        ProxiedPlayer player = (ProxiedPlayer) e.getSender();
        //CHECK PLAYER ACCOUNT IN DATABASE
        if(!e.getMessage().toLowerCase().startsWith("/login") && !e.getMessage().toLowerCase().startsWith("/register")) {
            if (new PlayerData(AnarchyAuth.getInstance().getConnection()).isRegistered(player) &&
                    !new PlayerData(AnarchyAuth.getInstance().getConnection()).getPlayerData(player).getAuth()) {
                e.setCancelled(true);
            } else if (!new PlayerData(AnarchyAuth.getInstance().getConnection()).isRegistered(player)) {
                e.setCancelled(true);
            }
        }
    }

}
