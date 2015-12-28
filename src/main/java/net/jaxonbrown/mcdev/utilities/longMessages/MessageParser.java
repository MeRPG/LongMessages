package net.jaxonbrown.mcdev.utilities.longMessages;

import java.util.regex.Pattern;

/**
 * @author Jaxon A Brown
 */
public class MessageParser {
    private LongMessagesConfigurationService config;

    MessageParser(LongMessagesPlugin plugin) {
        this.config = plugin.getConfigurationService();
    }

    public boolean shouldConcat(String message) {
        return message.endsWith(config.getConcatCharacter());
    }

    public String parseMessageForConcat(String message) {
        return message.endsWith(config.getConcatCharacter()) ?
                message.substring(0, message.length() - config.getConcatCharacter().length()) :
                message;
    }

    public String[] parseMessageForNewlines(String message) {
        return message.split(Pattern.quote(config.getNewlineCharacter()), config.getNewlineLimit() + 1);
    }
}
