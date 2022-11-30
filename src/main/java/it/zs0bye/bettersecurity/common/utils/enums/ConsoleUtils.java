package it.zs0bye.bettersecurity.common.utils.enums;

public enum ConsoleUtils {
    RESET("\u001B[0m"),
    BLACK("\u001B[30m)"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m");

    private final String code;

    ConsoleUtils(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
