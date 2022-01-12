package de.craftlancer.core.resourcepack.emoji;

import de.craftlancer.core.CLCore;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EmojiFontRegistry {
    
    private static final Pattern emojiPattern = Pattern.compile("(:([a-zA-Z]+):)");
    private static EmojiFontRegistry instance;
    
    private Map<String, Emoji> emojis;
    private CLCore plugin;
    
    public EmojiFontRegistry(CLCore plugin) {
        instance = this;
        
        plugin.getCommand("emojiregistry").setExecutor(new EmojiCommandHandler(plugin, this));
        
        this.plugin = plugin;
        load();
    }
    
    private void load() {
        File file = new File(plugin.getDataFolder(), "emojis.yml");
        
        try {
            if (!file.exists())
                file.createNewFile();
            
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            
            List<Emoji> list = ((List<Emoji>) config.getList("emojis", new ArrayList<>()));
            emojis = list.stream().collect(Collectors.toMap(Emoji::getName, l -> l));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void save() {
        File file = new File(plugin.getDataFolder(), "emojis.yml");
        
        try {
            if (!file.exists())
                file.createNewFile();
            
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            
            config.set("emojis", new ArrayList<>(emojis.values()));
            
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean addEmoji(String name, String unicode) {
        if (emojis.containsKey(name))
            return false;
        
        emojis.put(name, new Emoji(name, unicode));
        
        return true;
    }
    
    public boolean removeEmoji(String name) {
        return emojis.remove(name) != null;
    }
    
    public Emoji getEmoji(String name) {
        return emojis.get(name);
    }
    
    public String replace(String message) {
        String messageOut = message;
        
        Matcher matcher = emojiPattern.matcher(message);
        while (matcher.find()) {
            String match = message.substring(matcher.start(), matcher.end());
            
            messageOut = messageOut.replace(match, replaceMatch(match));
        }
        
        return messageOut;
    }
    
    private String replaceMatch(String match) {
        if (match.length() == 2)
            return match;
        
        String emojiWord = match.substring(1, match.length() - 1);
        Emoji emoji = getEmoji(emojiWord);
        
        if (emoji == null)
            return match;
        
        return match.replace(match, emoji.getUnicode());
    }
    
    public static EmojiFontRegistry getInstance() {
        return instance;
    }
    
    public static class Emoji implements ConfigurationSerializable {
        
        private String name;
        private String unicode;
        
        public Emoji(String name, String unicode) {
            this.name = name;
            this.unicode = unicode;
        }
        
        public Emoji(Map<String, Object> map) {
            this.name = (String) map.get("name");
            this.unicode = (String) map.get("unicode");
        }
        
        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> map = new HashMap<>();
            
            map.put("name", name);
            map.put("unicode", unicode);
            
            return map;
        }
        
        public String getName() {
            return name;
        }
        
        public String getUnicode() {
            return unicode;
        }
    }
}
