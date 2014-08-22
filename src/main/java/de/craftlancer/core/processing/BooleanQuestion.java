package de.craftlancer.core.processing;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.command.CommandSender;

public abstract class BooleanQuestion extends Question
{
    public BooleanQuestion(CommandSender sender)
    {
        super(sender);
    }

    private static final Set<String> no = new HashSet<String>();
    private static final Set<String> yes = new HashSet<String>();
    
    static
    {
        no.add("no");
        no.add("nein");
        no.add("false");
        
        yes.add("yes");
        yes.add("ja");
        yes.add("true");
    }
    
    @Override
    protected void onAnswer()
    {
        boolean answer = checkAnswer(getAnswer());
        
        if (answer)
            onYes();
        else
            onNo();
    }
    
    protected abstract void onNo();
    
    protected abstract void onYes();
    
    @Override
    public abstract void ask();
    
    @Override
    public boolean isValidInput(String input)
    {
        input = input.toLowerCase();
        return no.contains(input) || yes.contains(input);
    }
    
    private static boolean checkAnswer(String answer)
    {
        answer = answer.toLowerCase();
        if (yes.contains(answer))
            return true;
        else if (no.contains(answer))
            return false;
        else
            throw new IllegalStateException("The given answer '" + answer + "' is an illegal state.");
    }
}
