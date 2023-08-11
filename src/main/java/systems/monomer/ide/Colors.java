package systems.monomer.ide;

import systems.monomer.tokenizer.Token;

import java.awt.*;

public enum Colors {
    RED("#e06c75"), GREEN("#98c379"), YELLOW("#e5c07b"), BLUE("#61afef"), PURPLE("#c678dd"), ORANGE("#d19a66"), GRAY("#abb2bf"), CYAN("#56b6c2");

    private final Color color;
    private Colors(String hex) {
        this.color = Color.decode(hex);
    }

    public static Color colorFor(Token.Usage usage) {
        if(usage == null) return GRAY.getColor();
        return (switch (usage) {
            case IDENTIFIER -> ORANGE;
            case OPERATOR -> PURPLE;
            case STRING, STRING_BUILDER -> GREEN;
            case CHARACTER, CHARACTER_FROM_INT -> YELLOW;
            case INTEGER, FLOAT -> RED;
            case GROUP -> BLUE;
        }).getColor();
    }

    public Color getColor() {
        return color;
    }
}
