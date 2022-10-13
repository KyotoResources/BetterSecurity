package it.zs0bye.bettersecurity.warnings.enums;

public enum TypeWarning {
    COMMANDS("commands"),
    PORT_BYPASS_PREVENTION("port_bypass_prevention");

    private final String type;

    TypeWarning(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
