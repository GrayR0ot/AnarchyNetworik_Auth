package eu.grayroot.anarchyauth.commands;

import com.google.common.hash.Hashing;
import eu.grayroot.anarchyauth.AnarchyAuth;
import eu.grayroot.anarchyauth.object.AnarchyPlayer;
import eu.grayroot.anarchyauth.sql.PlayerData;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.nio.charset.StandardCharsets;

public class LoginCommand extends Command{

	public LoginCommand() {
		super("login");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer){
			ProxiedPlayer player = (ProxiedPlayer) sender;
			if(new PlayerData(AnarchyAuth.getInstance().getConnection()).isRegistered(player)) {
				if (args.length >= 1) {
					String password = new PlayerData(AnarchyAuth.getInstance().getConnection()).getPlayerData(player).getPassword();
					if (Hashing.sha512().hashString(args[0], StandardCharsets.UTF_8).toString().equalsIgnoreCase(password)) {
						new PlayerData(AnarchyAuth.getInstance().getConnection()).updatePlayerData(player, true);
						player.sendMessage("§7[§b!§7] §b» §fConnexion effectuée avec succès !");
						AnarchyAuth.getLoggedPlayers().add(player);
						ServerInfo hub = ProxyServer.getInstance().getServerInfo("hub");
						player.connect(hub);
					} else {
						player.sendMessage("§7[§b!§7] §b» §cEreur: §7Mot de passe incorrect !");
					}
				} else {
					player.sendMessage("§7[§b!§7] §b» §fAide: §7/login (Mot de passe)");
				}
			} else {
				player.sendMessage("§7[§b!§7] §b» §cVous devez vous créer un compte !");
			}
					
		} else {
            AnarchyAuth.getInstance().getLogger().severe(String.format("You need to be a player to run this command!"));
		}
	}

}
