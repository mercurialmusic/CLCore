package de.craftlancer.core.processing.interfaces;

import java.util.Collection;
import java.util.UUID;

import de.craftlancer.core.processing.Question;

public interface IQuestionManager
{
    public int getVersion();
    
    public void askQuestion(UUID uuid, Question question);
    
    public boolean answerQuestion(UUID uuid, String input);
    
    public Question getActiveQuestion(UUID uuid);
    
    public void removeFirstQuestion(UUID uuid);

    public void removeQuestions(UUID uuid, Collection<Question> unanswered);

    public void removeQuestion(UUID uuid, Question question);
}
