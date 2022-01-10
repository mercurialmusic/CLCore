package de.craftlancer.core.resourcepack;

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
    AETHER("\uEE10");
    
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
        StringBuilder ret = new StringBuilder();
        String s = String.valueOf(bal);
        
        for (int i = 0; i < s.length(); i++) {
            int v = Integer.parseInt(s.substring(i, i + 1));
            ret.append(EconomyFont.values()[v]);
        }
        
        return ret.toString();
    }
}
