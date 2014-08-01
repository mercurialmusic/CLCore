package de.craftlancer.core.processing;

import org.bukkit.command.CommandSender;

public abstract class Question
{
    private CommandSender sender;
    private String answer = null;
    
    public CommandSender getSender()
    {
        return sender;
    }
    
    public boolean answer(String input)
    {
        if (!isValidInput(input) || answer != null)
            return false;
        
        this.answer = input;
        
        onAnswer();
        
        return true;
    }

    public boolean isAnswered()
    {
        return answer == null;
    }
    
    public String getAnswer()
    {
        return answer;
    }
    
    /**
     * Called when an Question is answered
     */
    protected abstract void onAnswer();
    
    /**
     * Notify asked
     */
    public abstract void ask();
    
    /**
     * Check if the input provided is a valid input
     * (e.g. a boolean questions will return true for "yes" and "no" and returns false for "asdf")
     * 
     * @param input
     * @return true if the input is valid, false if not
     */
    public abstract boolean isValidInput(String input);
    
}
