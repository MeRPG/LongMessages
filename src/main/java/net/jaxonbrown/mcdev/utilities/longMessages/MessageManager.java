package net.jaxonbrown.mcdev.utilities.longMessages;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Jaxon A Brown
 */
public class MessageManager {
    private Map<UUID, List<String>> messageCacheMap;

    MessageManager() {
        this.messageCacheMap = Maps.newHashMap();
    }

    public void addMessagePart(Player player, String message) {
        addMessagePart(player.getUniqueId(), message);
    }

    public void addMessagePart(UUID uuid, String message) {
        List<String> messageCache = this.messageCacheMap.getOrDefault(uuid, new ArrayList<String>());
        messageCache.add(message);
        this.messageCacheMap.put(uuid, messageCache);
    }

    public boolean hasMessageCache(Player player) {
        return hasMessageCache(player.getUniqueId());
    }

    public boolean hasMessageCache(UUID uuid) {
        if(!messageCacheMap.containsKey(uuid)) {
            return false;
        }

        List<String> messageCache = messageCacheMap.get(uuid);

        return messageCache.size() != 0;
    }

    public String getMessage(Player player) {
        return getMessage(player.getUniqueId());
    }

    public String getMessage(UUID uuid) {
        List<String> messageCache = messageCacheMap.get(uuid);
        String message = "";

        for(String messagePart : messageCache) {
            message += messagePart;
        }

        return message;
    }

    public void clearMessageParts(Player player) {
        clearMessageParts(player.getUniqueId());
    }

    public void clearMessageParts(UUID uuid) {
        messageCacheMap.remove(uuid);
    }
}
