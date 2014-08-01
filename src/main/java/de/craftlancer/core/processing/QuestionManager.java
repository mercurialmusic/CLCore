package de.craftlancer.core.processing;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class QuestionManager implements Listener
{
    private Map<UUID, Queue<Question>> questions = new HashMap<>();
    
    public void askQuestion(UUID uuid, Question question)
    {
        if (!questions.containsKey(uuid))
            questions.put(uuid, new LinkedList<Question>());
        
        questions.get(uuid).add(question);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event)
    {
        answerQuestion(event.getPlayer().getUniqueId(), event.getMessage());
    }
    
    public synchronized boolean answerQuestion(UUID uuid, String input)
    {
        Question question = getActiveQuestion(uuid);
        
        if (question == null)
            return false;
        
        boolean success = question.answer(input);
        questions.remove(question);
        return success;
    }
    
    public Question getActiveQuestion(UUID uuid)
    {
        return questions.containsKey(uuid) ? questions.get(uuid).peek() : null;
    }
}
