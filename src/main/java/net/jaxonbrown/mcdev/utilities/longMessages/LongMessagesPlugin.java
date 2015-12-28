package net.jaxonbrown.mcdev.utilities.longMessages;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Jaxon A Brown
 */
public class LongMessagesPlugin extends JavaPlugin {
    private LongMessagesConfigurationService longMessagesConfigurationService;
    private MessageManager messageManager;
    private MessageParser messageParser;
    private ChatListener chatListener;
    private LongMessagesCommand longMessagesCommand;


    @Override
    public void onEnable() {
        (longMessagesConfigurationService = new LongMessagesConfigurationService(this)).reload();
        messageManager = new MessageManager();
        messageParser = new MessageParser(this);
        (chatListener = new ChatListener(this)).register();
        (longMessagesCommand = new LongMessagesCommand(this)).register();
    }

    public LongMessagesConfigurationService getConfigurationService() {
        return longMessagesConfigurationService;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public MessageParser getMessageParser() {
        return messageParser;
    }

    public ChatListener getChatListener() {
        return chatListener;
    }

    public LongMessagesCommand getLongMessagesCommand() {
        return longMessagesCommand;
    }
}
