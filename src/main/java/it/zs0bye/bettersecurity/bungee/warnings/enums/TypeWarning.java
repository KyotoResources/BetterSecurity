package it.zs0bye.bettersecurity.bungee.warnings.enums;

public enum TypeWarning {
    COMMANDS("commands"),
    PREVENT_COMMAND_SPAM("prevent_command_spam");

    private final String type;

    TypeWarning(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
