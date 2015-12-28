package net.jaxonbrown.mcdev.utilities.longMessages;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author Jaxon A Brown
 */
public class LongMessagesCommand implements CommandExecutor {
    private LongMessagesPlugin plugin;

    LongMessagesCommand(LongMessagesPlugin plugin) {
        this.plugin = plugin;
    }

    void register() {
        plugin.getCommand("longMessages").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.isOp()) {
            plugin.getConfigurationService().reload();
            sender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");
            return true;
        }

        List<String> message = Lists.newArrayList();
        message.add(ChatColor.GRAY + "---------------------------------");
        message.add(ChatColor.RED + "LongMessages " + plugin.getDescription().getVersion());
        message.add(ChatColor.RED + "Written by " + plugin.getDescription().getAuthors().get(0));
        message.add(ChatColor.GRAY + "---------------------------------");
        message.add(ChatColor.AQUA + "Place a \"" + plugin.getConfigurationService().getConcatCharacter() + "\" at the end ");
        message.add(ChatColor.AQUA + "of a message to begin a long message.");
        message.add(ChatColor.AQUA + "When you send a message without");
        message.add(ChatColor.AQUA + "a \"" + plugin.getConfigurationService().getConcatCharacter() + "\", your message will send.");
        message.add(ChatColor.AQUA + "You have a limit of " + plugin.getConfigurationService().getMaxLength() + " characters.");
        if(plugin.getConfigurationService().isNewlineEnabled()) {
            message.add(ChatColor.AQUA + "You can also place \"" + plugin.getConfigurationService().getNewlineCharacter() + "\" in your");
            message.add(ChatColor.AQUA + "long message to move to a new line.");
            message.add(ChatColor.AQUA + "You have a limit of " + plugin.getConfigurationService().getNewlineLimit() + " newlines.");
        }
        message.add(ChatColor.GRAY + "---------------------------------");

        sender.sendMessage(message.toArray(new String[message.size()]));
        return true;
    }
}
