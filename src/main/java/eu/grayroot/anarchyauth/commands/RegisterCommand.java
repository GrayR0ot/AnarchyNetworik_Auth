package eu.grayroot.anarchyauth.commands;

import eu.grayroot.anarchyauth.AnarchyAuth;
import eu.grayroot.anarchyauth.sql.PlayerData;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class RegisterCommand extends Command{
	
	public RegisterCommand() {
		super("register");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer){
			ProxiedPlayer player = (ProxiedPlayer) sender;
			if(!new PlayerData(AnarchyAuth.getInstance().getConnection()).isRegistered(player)) {
				if (args.length >= 2) {
					if (args[0].equalsIgnoreCase(args[1])) {
						new PlayerData(AnarchyAuth.getInstance().getConnection()).registerPlayer(player, args[0]);
						player.sendMessage("§7[§b!§7] §b» §fVotre compte à été créé avec succès !");
						AnarchyAuth.getLoggedPlayers().add(player);
						ServerInfo hub = ProxyServer.getInstance().getServerInfo("hub");
						player.connect(hub);
					} else {
						player.sendMessage("§7[§b!§7] §b» §fAide: §7/register (Mot de passe) (Confirmation du mot de passe)");
					}
				} else {
					player.sendMessage("§7[§b!§7] §b» §fAide: §7/register (Mot de passe) (Confirmation du mot de passe)");
				}
			} else {
				player.sendMessage("§7[§b!§7] §b» §cErreur: §7Vous avez déjà un compte !");
			}
					
		} else {
            AnarchyAuth.getInstance().getLogger().severe(String.format("You need to be a player to run this command!"));
		}
	}

}
