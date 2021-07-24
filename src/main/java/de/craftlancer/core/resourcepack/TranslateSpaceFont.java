package de.craftlancer.core.resourcepack;

import java.util.HashMap;
import java.util.Map;

/**
 * Special unicodes working together with the custom CC resource pack to horizontally
 * move text to the left. DOES NOT work without the texture pack enabled.
 */
public enum TranslateSpaceFont {
    
    TRANSLATE_NEGATIVE_1024(-1024, "\uF80F"),
    TRANSLATE_NEGATIVE_512(-512, "\uF80E"),
    TRANSLATE_NEGATIVE_256(-256, "\uF80D"),
    TRANSLATE_NEGATIVE_128(-128, "\uF80C"),
    TRANSLATE_NEGATIVE_64(-64, "\uF80B"),
    TRANSLATE_NEGATIVE_32(-32, "\uF80A"),
    TRANSLATE_NEGATIVE_16(-16, "\uF809"),
    TRANSLATE_NEGATIVE_8(-8, "\uF808"),
    TRANSLATE_NEGATIVE_7(-7, "\uF807"),
    TRANSLATE_NEGATIVE_6(-6, "\uF806"),
    TRANSLATE_NEGATIVE_5(-5, "\uF805"),
    TRANSLATE_NEGATIVE_4(-4, "\uF804"),
    TRANSLATE_NEGATIVE_3(-3, "\uF803"),
    TRANSLATE_NEGATIVE_2(-2, "\uF802"),
    TRANSLATE_NEGATIVE_1(-1, "\uF801"),
    TRANSLATE_POSITIVE_1024(1024, "\uF82F"),
    TRANSLATE_POSITIVE_512(512, "\uF82E"),
    TRANSLATE_POSITIVE_256(256, "\uF82D"),
    TRANSLATE_POSITIVE_128(128, "\uF82C"),
    TRANSLATE_POSITIVE_64(64, "\uF82B"),
    TRANSLATE_POSITIVE_32(32, "\uF82A"),
    TRANSLATE_POSITIVE_16(16, "\uF829"),
    TRANSLATE_POSITIVE_8(8, "\uF828"),
    TRANSLATE_POSITIVE_7(7, "\uF827"),
    TRANSLATE_POSITIVE_6(6, "\uF826"),
    TRANSLATE_POSITIVE_5(5, "\uF825"),
    TRANSLATE_POSITIVE_4(4, "\uF824"),
    TRANSLATE_POSITIVE_3(3, "\uF823"),
    TRANSLATE_POSITIVE_2(2, "\uF822"),
    TRANSLATE_POSITIVE_1(1, "\uF821");
    
    private static Map<Integer, String> specificAmounts = new HashMap<>();
    
    static {
        for (TranslateSpaceFont font : TranslateSpaceFont.values())
            specificAmounts.put(font.getPixelSize(), font.toString());
    }
    
    public static String getSpecificAmount(int pixels) {
        if (specificAmounts.containsKey(pixels))
            return specificAmounts.get(pixels);
        
        if (pixels == 0)
            return "";
        
        int original = pixels;
        boolean isPositive = pixels > 0;
        StringBuilder builder = new StringBuilder();
        
        for (TranslateSpaceFont font : TranslateSpaceFont.values()) {
            if ((isPositive && font.getPixelSize() < 0) || (!isPositive && font.getPixelSize() > 0))
                continue;
            if ((isPositive && pixels <= 0) || (!isPositive && pixels >= 0))
                break;
            int amount = Math.abs(pixels / font.getPixelSize());
            for (int i = 0; i < amount; i++)
                builder.append(font);
            
            pixels = pixels - (amount * font.getPixelSize());
        }
        
        specificAmounts.put(original, builder.toString());
        return builder.toString();
    }
    
    private String unicode;
    private int pixelSize;
    
    TranslateSpaceFont(int pixelSize, String unicode) {
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
}