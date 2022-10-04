package it.zs0bye.bettersecurity.utils.enums;

public enum ConsoleUtils {
    RESET("\u001B[0m"),
    YELLOW("\u001B[33m");

    private final String code;

    ConsoleUtils(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
