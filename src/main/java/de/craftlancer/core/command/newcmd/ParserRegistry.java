package de.craftlancer.core.command.newcmd;

import java.util.HashMap;
import java.util.Map;

public class ParserRegistry {
    private static final ParserRegistry PARSER_REGISTRY = new ParserRegistry();
    
    private static Map<String, CommandParser<?>> parserMap = new HashMap<>();
    private static Map<Class<?>, String> defaultParser = new HashMap<>();
    
    static {
        registerDefaultParser("int", new CommandParser<Integer>() {
            
            @Override
            public Class<Integer> getType() {
                return int.class;
            }
            
            @Override
            public ParserResult<Integer> parse(String input) {
                try {
                    int i = Integer.parseInt(input);
                    return new ParserResult<>(i);
                }
                catch (NumberFormatException e) {
                    return new ParserResult<>(0, false, "Could not parse integer.");
                }
            }
        });
        
        registerDefaultParser("string", new CommandParser<String>() {
            @Override
            public Class<String> getType() {
                return String.class;
            }
            
            @Override
            public ParserResult<String> parse(String input) {
                return new ParserResult<>(input);
            }
        });
        
    }
    
    public static void registerDefaultParser(String name, CommandParser<?> parser) {
        registerParser(name, parser);
        defaultParser.put(parser.getType(), name);
    }
    
    public static void registerParser(String name, CommandParser<?> parser) {
        parserMap.put(name, parser);
    }
    
    public static CommandParser<?> getParser(String name) {
        return parserMap.get(name);
    }
    
    public static CommandParser<?> getDefaultParser(Class<?> clazz) {
        return getParser(defaultParser.get(clazz));
    }
    
    public static ParserRegistry getParserRegistry() {
        return PARSER_REGISTRY;
    }
}
