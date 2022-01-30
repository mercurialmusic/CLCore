package de.craftlancer.core.resourcepack;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public enum NameTagFont {
    
    NAMEPLATE("\uC100"),
    NAMEPLATE_END("\uC101"),
    A("\uC102"),
    B("\uC103"),
    C("\uC104"),
    D("\uC105"),
    E("\uC106"),
    F("\uC107"),
    G("\uC108"),
    H("\uC109"),
    I("\uC110"),
    J("\uC111"),
    K("\uC112"),
    L("\uC113"),
    M("\uC114"),
    N("\uC115"),
    O("\uC116"),
    P("\uC117"),
    Q("\uC118"),
    R("\uC119"),
    S("\uC120"),
    T("\uC121"),
    U("\uC122"),
    V("\uC123"),
    W("\uC124"),
    X("\uC125"),
    Y("\uC126"),
    Z("\uC127");
    
    
    private static final Map<String, Map<ChatColor, String>> nameCache = new HashMap<>();
    
    private final String unicode;
    
    NameTagFont(String unicode) {
        this.unicode = unicode;
    }
    
    public String getUnicode() {
        return unicode;
    }
    
    @Override
    public String toString() {
        return unicode;
    }
    
    public static String getTag(String input, ChatColor color) {
        
        Map<ChatColor, String> map = nameCache.compute(input, (i, m) -> {
            if (m == null)
                m = new HashMap<>();
            
            return m;
        });
        
        return map.compute(color, (c, tag) -> {
            if (tag == null)
                tag = buildTag(input.toUpperCase().replaceAll("[^a-zA-Z]", " "), color);
            
            return tag;
        });
    }
    
    private static String buildTag(String input, ChatColor color) {
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < input.length(); i++) {
            String character = input.substring(i, i + 1);
            String font = (character.equals(" ") ? " " : NameTagFont.valueOf(character).toString());
            boolean last = i == input.length() - 1;
            boolean first = i == 0;
            
            if (!first)
                builder.append(TranslateSpaceFont.TRANSLATE_NEGATIVE_1);
            
            builder.append(color);
            builder.append(last ? NAMEPLATE_END : NAMEPLATE);
            builder.append(ChatColor.WHITE);
            builder.append(last ? TranslateSpaceFont.TRANSLATE_NEGATIVE_8 : TranslateSpaceFont.TRANSLATE_NEGATIVE_7);
            builder.append(font);
        }
        
        return builder.toString();
    }
}
