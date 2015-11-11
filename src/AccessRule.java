public enum AccessRule {
    READ, WRITE, EXECUTE, OWN;

    static AccessRule fromStr(String cmd) {
        for (AccessRule rule : values()) {
            if (rule.name().equalsIgnoreCase(cmd)) {
                return rule;
            }
        }
        return null;
    }
}
