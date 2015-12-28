package net.jaxonbrown.mcdev.utilities.longMessages;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

/**
 * @author Jaxon A Brown
 */
public class ChatListener implements Listener {
    private LongMessagesPlugin plugin;
    private MessageManager manager;
    private MessageParser parser;
    private LongMessagesConfigurationService config;

    ChatListener(LongMessagesPlugin plugin) {
        this.plugin = plugin;
        this.manager = plugin.getMessageManager();
        this.parser = plugin.getMessageParser();
        this.config = plugin.getConfigurationService();
    }

    void register() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        event.setCancelled(process(event.getPlayer(), event.getMessage()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        event.setCancelled(process(event.getPlayer(), event.getMessage()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        manager.clearMessageParts(event.getPlayer());
    }

    private boolean process(Player player, String message) {
        if(player.hasPermission(config.getConcatPermission())) {
            boolean hasCache = manager.hasMessageCache(player);
            boolean shouldConcat = parser.shouldConcat(message);

            if(hasCache && shouldConcat) {
                String parsedMessage = parser.parseMessageForConcat(message);
                String cachedMessage = manager.getMessage(player);
                if(cachedMessage.length() + parsedMessage.length() > config.getMaxLength()) {
                    manager.clearMessageParts(player);
                    if(config.isNewlineEnabled() && player.hasPermission(config.getNewlinePermission())) {
                        chat(player, parser.parseMessageForNewlines((cachedMessage + parsedMessage).substring(0, config.getMaxLength())));
                    } else {
                        chat(player, new String[] {(cachedMessage + parsedMessage).substring(0, config.getMaxLength())});
                    }
                } else {
                    manager.addMessagePart(player, parser.parseMessageForConcat(message));
                }
            } else if(hasCache) {
                manager.addMessagePart(player, message);
                String finalMessage = manager.getMessage(player);
                manager.clearMessageParts(player);
                if(config.isNewlineEnabled() && player.hasPermission(config.getNewlinePermission())) {
                    chat(player, parser.parseMessageForNewlines(finalMessage));
                } else {
                    chat(player, new String[] {finalMessage});
                }
            } else if(shouldConcat) {
                manager.addMessagePart(player, parser.parseMessageForConcat(message));
            } else {
                return false;
            }

            return true;
        }
        return false;
    }

    private void chat(Player player, String[] message) {
        if(message.length > 1) {
            String fullMessage = "";
            for(String part : message) {
                fullMessage += part + " ";
            }
            if(fullMessage.length() > 1) {
                fullMessage = fullMessage.substring(0, message.length - 1);
            }

            List<Player> recipients = Lists.newArrayList(Bukkit.getOnlinePlayers());
            AsyncPlayerChatEvent asyncPlayerChatEvent = new AsyncPlayerChatEvent(false, player, fullMessage, Sets.newHashSet(recipients));
            Bukkit.getPluginManager().callEvent(asyncPlayerChatEvent);
            if(asyncPlayerChatEvent.isCancelled()) {
                return;
            }
            List<Player> remove = Lists.newArrayList(recipients);
            remove.removeAll(asyncPlayerChatEvent.getRecipients());
            recipients.removeAll(remove);

            message[0] = String.format(asyncPlayerChatEvent.getFormat(), asyncPlayerChatEvent.getPlayer().getDisplayName(), message[0]);

            for(Player recipient : recipients) {
                recipient.sendMessage(message);
            }

            Bukkit.getConsoleSender().sendMessage(message);
        } else {
            player.chat(message[0]);
        }
    }
}
