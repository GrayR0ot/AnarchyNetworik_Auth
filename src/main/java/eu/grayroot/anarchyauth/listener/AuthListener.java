package eu.grayroot.anarchyauth.listener;

import eu.grayroot.anarchyauth.AnarchyAuth;
import eu.grayroot.anarchyauth.sql.PlayerData;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
public class AuthListener implements Listener {

    @EventHandler
    public void onJoin(PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();
        new PlayerData(AnarchyAuth.getInstance().getConnection()).updatePlayerData(player, false);

        //CHECK PLAYER ACCOUNT IN DATABASE
        if (new PlayerData(AnarchyAuth.getInstance().getConnection()).isRegistered(player)) {

            player.sendMessage("§7[§b!§7] §b» §fAide: §7/login (Mot de passe)");
            AnarchyAuth.getInstance().getProxy().getScheduler().schedule(AnarchyAuth.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (!AnarchyAuth.getLoggedPlayers().contains(player)) {
                        player.sendMessage("§7[§b!§7] §b» §fAide: §7/login (Mot de passe)");
                    }
                }
            }, 6, 10, TimeUnit.SECONDS);
            AnarchyAuth.getInstance().getProxy().getScheduler().schedule(AnarchyAuth.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (!AnarchyAuth.getLoggedPlayers().contains(player)) {
                        player.disconnect("Vous avez mis trop de temps pour vous connecter !");
                    }
                }
            }, 1, 1, TimeUnit.MINUTES);

        } else {

            player.sendMessage("§7[§b!§7] §b» §fAide: §7/register (Mot de passe) (Confirmation du mot de passe)");
            AnarchyAuth.getInstance().getProxy().getScheduler().schedule(AnarchyAuth.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (!AnarchyAuth.getLoggedPlayers().contains(player)) {
                        player.sendMessage("§7[§b!§7] §b» §fAide: §7/register (Mot de passe) (Confirmation du mot de passe)");
                    }
                }
            }, 6, 10, TimeUnit.SECONDS);
            AnarchyAuth.getInstance().getProxy().getScheduler().schedule(AnarchyAuth.getInstance(), new Runnable() {
                @Override
                public void run() {

                    if (!AnarchyAuth.getLoggedPlayers().contains(player)) {
                        player.disconnect("Vous avez mis trop de temps pour vous inscrire !");
                    }
                }
            }, 1, 1, TimeUnit.MINUTES);

        }

    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {
        ProxiedPlayer player = e.getPlayer();
        if (new PlayerData(AnarchyAuth.getInstance().getConnection()).isRegistered(player)) {
            new PlayerData(AnarchyAuth.getInstance().getConnection()).updatePlayerData(player, false);
        }
    }

}
