package de.craftlancer.core.conversation;

import java.util.UUID;

import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Player;

import net.md_5.bungee.chat.ComponentSerializer;

public class FormattedConversable implements Conversable {
    private Player p;

    public FormattedConversable(Player p) {
        this.p = p;
    }
    
    @Override
    public boolean isConversing() {
        return p.isConversing();
    }

    @Override
    public void acceptConversationInput(String input) {
        p.acceptConversationInput(input);
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        return p.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        p.abandonConversation(conversation);
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        p.abandonConversation(conversation, details);
    }

    @Override
    public void sendRawMessage(String message) {
        if(message.startsWith("[") || message.startsWith("{"))
            p.spigot().sendMessage(ComponentSerializer.parse(message));
        else
            p.sendRawMessage(message);
    }

    @Override
    public void sendRawMessage(UUID sender, String message) {
        if(message.startsWith("[") || message.startsWith("{"))
            p.spigot().sendMessage(ComponentSerializer.parse(message)); // FIXME 1.16.4, add sender
        else
            p.sendRawMessage(sender, message);
    }
}