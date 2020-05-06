package de.craftlancer.core.command.newcmd;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.craftlancer.core.util.ReflectionUtils;

public class NewCommandExecutor implements CLCommand {
    private String permission = "";
    
    @Override
    public void handleCommand(CommandSender sender, String[] args) throws CommandHandlerException {
        Method m = ReflectionUtils.getMethod(this.getClass(), "performCommand");
        
        if (m == null) {
            throw new CommandHandlerException("CommandHandler was unable to find performCommand method signature.");
        }
        
        Parameter[] para = m.getParameters();
        Object[] commandArgs = new Object[para.length];
        
        if (para.length <= 0 || !CommandSender.class.isAssignableFrom(para[0].getType()))
            throw new CommandHandlerException("CommandHandler found performCommand, but it's lacking a CommandSender parameter.");
        if (!para[0].getType().isAssignableFrom(sender.getClass())) {
            // at sender: wrong sender type
            sender.sendMessage("wrong sender type");
            return;
        }
        if (!sender.hasPermission(getPermission())) {
            // at sender: no permission
            sender.sendMessage("no permission");
            return;
        }
        
        commandArgs[0] = sender;
        
        for (int i = 1; i < para.length; i++) {
            Parameter localPara = para[i];
            CommandArgument annotation = localPara.getAnnotation(CommandArgument.class);
            CommandParser<?> parser = annotation != null ? ParserRegistry.getParser(annotation.name()) : ParserRegistry.getDefaultParser(localPara.getType());
            
            if (parser == null)
                throw new CommandHandlerException(
                        String.format("Couldn't find specifed CommandParser %s or default parser at args %d.", annotation == null ? "null" : annotation.name(), i));
            if (!localPara.getType().isAssignableFrom(parser.getType()))
                throw new CommandHandlerException("Specified CommandParser doesn't match type of parameter.");
            
            if (args.length < i) {
                // at sender: no arguments left to parse
                sender.sendMessage("no arguments left to parse");
                return;
            }
            
            ParserResult<?> result = parser.parse(args[i - 1]);
            if (!result.isSuccess()) {
                // at sender: argument wrong
                sender.sendMessage("argument wrong");
                return;
            }
            
            commandArgs[i] = result.getResult();
        }
        
        try {
            m.invoke(this, commandArgs);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new CommandHandlerException(e.getMessage());
        }
    }
    
    @Override
    public List<String> handleTabComplete(CommandSender sender, String[] args) throws CommandHandlerException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getPermission() {
        return permission;
    }
    
}
