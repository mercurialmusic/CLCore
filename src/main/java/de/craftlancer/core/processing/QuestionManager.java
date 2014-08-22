package de.craftlancer.core.processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.scheduler.BukkitRunnable;

import de.craftlancer.core.processing.interfaces.IQuestionManager;

public final class QuestionManager implements Listener, IQuestionManager
{
    public static final int VERSION = 1;
    
    private Map<UUID, Queue<Question>> questions = new HashMap<>();
    
    public QuestionManager(final Plugin plugin)
    {
        final Listener instance = this;
        
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (instance == Bukkit.getServicesManager().getRegistration(IQuestionManager.class).getProvider())
                    Bukkit.getPluginManager().registerEvents(instance, plugin);
            }
        }.runTaskLater(plugin, 0);
    }
    
    @Override
    public void askQuestion(UUID uuid, Question question)
    {
        if (!questions.containsKey(uuid))
            questions.put(uuid, new LinkedList<Question>());
        
        questions.get(uuid).add(question);
        
        if (getActiveQuestion(uuid) == question)
            question.ask();
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event)
    {
        answerQuestion(event.getPlayer().getUniqueId(), event.getMessage());
    }
    
    @Override
    public synchronized boolean answerQuestion(UUID uuid, String input)
    {
        Question question = getActiveQuestion(uuid);
        
        if (question == null)
            return false;
        
        boolean success = question.answer(input);
        
        if (success)
            removeFirstQuestion(uuid);
        
        return success;
    }
    
    @Override
    public Question getActiveQuestion(UUID uuid)
    {
        return questions.containsKey(uuid) ? questions.get(uuid).peek() : null;
    }
    
    @Override
    public void removeFirstQuestion(UUID uuid)
    {
        questions.get(uuid).remove(getActiveQuestion(uuid));
        
        Question newQuestion = getActiveQuestion(uuid);
        if (newQuestion != null)
            newQuestion.ask();
    }
    
    @Override
    public void removeQuestion(UUID uuid, Question question)
    {
        Question oldActive = getActiveQuestion(uuid);
        questions.get(uuid).remove(question);
        
        Question newQuestion = getActiveQuestion(uuid);
        
        if (newQuestion != null && oldActive != newQuestion)
            newQuestion.ask();
    }
    
    @Override
    public void removeQuestions(UUID uuid, Collection<Question> unanswered)
    {
        Question oldActive = getActiveQuestion(uuid);
        
        for (Question q : unanswered)
            questions.get(uuid).remove(q);
        
        Question newQuestion = getActiveQuestion(uuid);
        
        if (newQuestion != null && oldActive != newQuestion)
            newQuestion.ask();
    }
    
    @Override
    public int getVersion()
    {
        return VERSION;
    }
    
    public static void load(Plugin plugin)
    {
        ServicesManager manager = Bukkit.getServicesManager();
        
        if (!manager.isProvidedFor(IQuestionManager.class))
            manager.register(IQuestionManager.class, new QuestionManager(plugin), plugin, ServicePriority.Normal);
        else
        {
            Collection<RegisteredServiceProvider<IQuestionManager>> handlers = manager.getRegistrations(IQuestionManager.class);
            List<IQuestionManager> remove = new ArrayList<>();
            IQuestionManager newest = null;
            for (RegisteredServiceProvider<IQuestionManager> handler : handlers)
            {
                IQuestionManager provider = handler.getProvider();
                if (newest == null || provider.getVersion() > newest.getVersion())
                {
                    remove.add(newest);
                    newest = provider;
                }
                else
                    remove.add(provider);
            }
            
            if (newest == null || VERSION > newest.getVersion())
            {
                remove.add(newest);
                manager.register(IQuestionManager.class, new QuestionManager(plugin), plugin, ServicePriority.Normal);
            }
            
            for (IQuestionManager provider : remove)
                manager.unregister(provider);
        }
    }
}
