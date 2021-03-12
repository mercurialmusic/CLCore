package de.craftlancer.core.resourcepack;

import java.util.HashMap;
import java.util.Map;

/**
 * Special unicodes working together with the custom CC resource pack to horizontally
 * move text to the left. DOES NOT work without the texture pack enabled.
 */
public enum NegativeSpaceFont {
    
    TRANSLATE_1024(1024, "\uF80F"),
    TRANSLATE_512(512, "\uF80E"),
    TRANSLATE_256(256, "\uF80D"),
    TRANSLATE_128(128, "\uF80C"),
    TRANSLATE_64(64, "\uF80B"),
    TRANSLATE_32(32, "\uF80A"),
    TRANSLATE_16(16, "\uF809"),
    TRANSLATE_8(8, "\uF808"),
    TRANSLATE_7(7, "\uF807"),
    TRANSLATE_6(6, "\uF806"),
    TRANSLATE_5(5, "\uF805"),
    TRANSLATE_4(4, "\uF804"),
    TRANSLATE_3(3, "\uF803"),
    TRANSLATE_2(2, "\uF802"),
    TRANSLATE_1(1, "\uF801");
    
    
    private String unicode;
    private int pixelSize;
    
    NegativeSpaceFont(int pixelSize, String unicode) {
        this.unicode = unicode;
        this.pixelSize = pixelSize;
    }
    
    private int getPixelSize() {
        return pixelSize;
    }
    
    @Override
    public String toString() {
        return unicode;
    }
    
    private static Map<Integer, String> specificAmounts = new HashMap<>();
    
    static {
        for (NegativeSpaceFont font : NegativeSpaceFont.values())
            specificAmounts.put(font.getPixelSize(), font.toString());
    }
    
    public static String getSpecificAmount(int negative) {
        if (specificAmounts.containsKey(negative))
            return specificAmounts.get(negative);
        
        int original = negative;
        StringBuilder builder = new StringBuilder();
        
        for (NegativeSpaceFont font : NegativeSpaceFont.values()) {
            if (negative <= 0)
                break;
            int amount = negative / font.getPixelSize();
            for (int i = 0; i < amount; i++)
                builder.append(font);
            
            negative = negative - (amount * font.getPixelSize());
        }
        
        specificAmounts.put(original, builder.toString());
        return builder.toString();
    }
}