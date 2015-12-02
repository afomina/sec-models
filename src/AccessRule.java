public enum AccessRule {
    READ, WRITE, EXECUTE, OWN, CREATE_SUBJECTS, READ1, READ2;

    static AccessRule fromStr(String cmd) {
        for (AccessRule rule : values()) {
            if (rule.name().equalsIgnoreCase(cmd)) {
                return rule;
            }
        }
        return null;
    }
}
