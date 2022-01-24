package de.craftlancer.core.resourcepack;

import java.util.HashMap;
import java.util.Map;

public enum EconomyFont {
    
    ZERO("\uEE00"),
    ONE("\uEE01"),
    TWO("\uEE02"),
    THREE("\uEE03"),
    FOUR("\uEE04"),
    FIVE("\uEE05"),
    SIX("\uEE06"),
    SEVEN("\uEE07"),
    EIGHT("\uEE08"),
    NINE("\uEE09"),
    AETHER("\uEE10"),
    COMMA("\uEE11");
    
    private static final Map<Integer, String> balanceCache = new HashMap<>();
    
    private final String unicode;
    
    EconomyFont(String unicode) {
        this.unicode = unicode;
    }
    
    public String getUnicode() {
        return unicode;
    }
    
    @Override
    public String toString() {
        return unicode;
    }
    
    public static String getBalance(int bal) {
        if (balanceCache.containsKey(bal))
            return balanceCache.get(bal);
        
        StringBuilder ret = new StringBuilder();
        String s = String.valueOf(bal);
        
        int commaCount = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (commaCount != 0 && commaCount % 3 == 0)
                ret.insert(0, COMMA + "" + TranslateSpaceFont.TRANSLATE_NEGATIVE_1);
            
            int v = Integer.parseInt(String.valueOf(s.charAt(i)));
            ret.insert(0, EconomyFont.values()[v] + "" + TranslateSpaceFont.TRANSLATE_NEGATIVE_1);
            
            commaCount++;
        }
        
        ret.append(TranslateSpaceFont.TRANSLATE_POSITIVE_1);
        
        balanceCache.put(bal, ret.toString());
        
        return ret.toString();
    }
}
