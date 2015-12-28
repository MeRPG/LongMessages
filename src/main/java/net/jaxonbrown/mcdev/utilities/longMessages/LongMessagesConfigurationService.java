package net.jaxonbrown.mcdev.utilities.longMessages;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Jaxon A Brown
 */
public class LongMessagesConfigurationService {
    private JavaPlugin plugin;
    private FileConfiguration config;

    private String concatCharacter;
    private String concatPermission;
    private int maxLength;
    private boolean newlineEnabled;
    private String newlineCharacter;
    private String newlinePermission;
    private int newlineLimit;

    LongMessagesConfigurationService(LongMessagesPlugin plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
        config.options().copyDefaults(true);
        this.newlineEnabled = getBoolean("newline-enabled", true);
        this.concatCharacter = getString("extension-character", "&");
        this.newlineCharacter = getString("newline-character", "^");
        this.concatPermission = getString("permissions.extension-permission", "longmessages.use");
        this.newlinePermission = getString("permissions.newline-permission", "longmessages.newline");
        this.maxLength = getInt("maximum-message-length", 2048);
        this.newlineLimit = getInt("newline-limit", 10);
        plugin.saveConfig();
    }

    private String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, def);
    }

    private int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, def);
    }

    private boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, def);
    }

    public String getConcatCharacter() {
        return concatCharacter;
    }

    public String getConcatPermission() {
        return concatPermission;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public boolean isNewlineEnabled() {
        return newlineEnabled;
    }

    public String getNewlineCharacter() {
        return newlineCharacter;
    }

    public String getNewlinePermission() {
        return newlinePermission;
    }

    public int getNewlineLimit() {
        return newlineLimit;
    }
}
