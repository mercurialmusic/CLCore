package de.craftlancer.core.resourcepack.emoji;

import de.craftlancer.core.CLCore;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiFontRegistry {
    
    private static final Pattern emojiPattern = Pattern.compile("(:([a-zA-Z_$?!.]+):)");
    private static EmojiFontRegistry instance;
    
    private List<Emoji> emojis;
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
            
            emojis = ((List<Emoji>) config.getList("emojis", new ArrayList<>()));
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
            
            config.set("emojis", emojis);
            
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean addEmoji(String name, String unicode, boolean endOfList) {
        if (emojis.stream().anyMatch(e -> e.match(name)))
            return false;
        
        Optional<Emoji> optional = emojis.stream().filter(e -> e.getUnicode().equals(unicode)).findFirst();
        
        if (!optional.isPresent())
            emojis.add(new Emoji(new ArrayList<>(Collections.singletonList(name)), unicode, endOfList));
        else
            optional.get().getAliases().add(name);
        
        return true;
    }
    
    public boolean removeEmoji(String name) {
        boolean removed = false;
        for (Emoji emoji : emojis)
            if (emoji.getAliases().removeIf(n -> n.equals(name)))
                removed = true;
        
        emojis.removeIf(e -> e.getAliases().isEmpty());
        
        return removed;
    }
    
    public Optional<Emoji> getEmoji(String name) {
        return emojis.stream().filter(e -> e.match(name)).findFirst();
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
        Optional<Emoji> emoji = getEmoji(emojiWord);
        
        return emoji.map(value -> match.replace(match, value.getUnicode())).orElse(match);
    }
    
    List<Emoji> getEmojis() {
        return emojis;
    }
    
    List<String> getAllAliases() {
        List<String> aliases = new ArrayList<>();
        
        for (Emoji emoji : emojis)
            aliases.addAll(emoji.getAliases());
        
        return aliases;
    }
    
    public static EmojiFontRegistry getInstance() {
        return instance;
    }
    
    public static class Emoji implements ConfigurationSerializable {
        
        private List<String> aliases;
        private String unicode;
        private boolean endOfList;
        
        public Emoji(List<String> aliases, String unicode, boolean endOfList) {
            this.aliases = aliases;
            this.unicode = unicode;
            this.endOfList = endOfList;
        }
        
        public Emoji(Map<String, Object> map) {
            this.aliases = (List<String>) map.getOrDefault("aliases", map.containsKey("name")
                    ? new ArrayList<>(Collections.singletonList(map.get("name").toString()))
                    : new ArrayList<>());
            this.unicode = (String) map.get("unicode");
            this.endOfList = (boolean) map.getOrDefault("endOfList", false);
        }
        
        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> map = new HashMap<>();
            
            map.put("aliases", aliases);
            map.put("unicode", unicode);
            map.put("endOfList", endOfList);
            
            return map;
        }
        
        public List<String> getAliases() {
            return aliases;
        }
        
        public String getUnicode() {
            return unicode;
        }
        
        public boolean match(String s) {
            return aliases.contains(s);
        }
        
        public boolean isEndOfList() {
            return endOfList;
        }
    }
}
