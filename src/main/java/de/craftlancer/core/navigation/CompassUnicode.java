package de.craftlancer.core.navigation;

public enum CompassUnicode {
    ZERO("\uEC00"),
    ONE("\uEC01"),
    TWO("\uEC02"),
    THREE("\uEC03"),
    FOUR("\uEC04"),
    FIVE("\uEC05"),
    SIX("\uEC06"),
    SEVEN("\uEC07"),
    EIGHT("\uEC08"),
    NINE("\uEC09"),
    TEN("\uEC10"),
    ELEVEN("\uEC11"),
    TWELVE("\uEC12"),
    THIRTEEN("\uEC13"),
    FOURTEEN("\uEC14"),
    FIFTEEN("\uEC15"),
    SIXTEEN("\uEC16"),
    SEVENTEEN("\uEC17"),
    EIGHTEEN("\uEC18"),
    NINETEEN("\uEC19"),
    TWENTY("\uEC20"),
    TWENTY_ONE("\uEC21"),
    TWENTY_TWO("\uEC22"),
    TWENTY_THREE("\uEC23"),
    TWENTY_FOUR("\uEC24"),
    TWENTY_FIVE("\uEC25"),
    TWENTY_SIX("\uEC26"),
    TWENTY_SEVEN("\uEC27"),
    TWENTY_EIGHT("\uEC28"),
    TWENTY_NINE("\uEC29"),
    THIRTY("\uEC30"),
    THIRTY_ONE("\uEC31");
    
    private String unicode;
    
    CompassUnicode(String unicode) {
        this.unicode = unicode;
    }
    
    @Override
    public String toString() {
        return unicode;
    }
}
