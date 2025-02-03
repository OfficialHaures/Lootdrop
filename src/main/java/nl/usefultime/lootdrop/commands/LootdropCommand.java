package nl.usefultime.lootdrop.commands;

import nl.usefultime.lootdrop.Lootdrop;
import nl.usefultime.lootdrop.gui.MainGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LootdropCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("§cThis command can only be executed by players");
            return false;
        }

        Player player = (Player) commandSender;
        if(!player.hasPermission("lootdrop.admin")){
            player.sendMessage("§cYou do not have permission to use this command");
            return false;
        }
        MainGUI gui = new MainGUI(Lootdrop.getInstance());
        player.openInventory(gui.getInventory());
        return true;
    }
}
