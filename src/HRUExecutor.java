import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HRUExecutor {
    private static final String CREATE_SUBJECT_PATTERN = "create subject (.*)";
    private static final String CREATE_OBJECT_PATTERN = "create object (.*)";
    private static final String DESTROY_SUBJECT_PATTERN = "destroy subject (.*)";
    private static final String DESTROY_OBJECT_PATTERN = "destroy object (.*)";
    private static final String ENTER_RULE_PATTERN = "enter ([^ ]*) into A\\[([^ ,\\]]*), ?([^ ,\\]]*)\\]";
    private static final String DELETE_RULE_PATTERN = "delete ([^ ]*) into A\\[([^ ,\\]]*), ?([^ ,\\]]*)\\]";
    private AccessTable accessTable;

    public HRUExecutor() {
        accessTable = new AccessTable();
    }

    public void execute(String command) {
        if (command.startsWith("create subject")) {
            Matcher matcher = Pattern.compile(CREATE_SUBJECT_PATTERN).matcher(command);
            if (matcher.matches()) {
                accessTable.createSubject(new Subject(matcher.group(1)));
            }
            return;
        }
        if (command.startsWith("create object")) {
            Matcher matcher = Pattern.compile(CREATE_OBJECT_PATTERN).matcher(command);
            if (matcher.matches()) {
                accessTable.createObject(new SecurityObject(matcher.group(1)));
            }
            return;
        }
        if (command.startsWith("destroy object")) {
            Matcher matcher = Pattern.compile(DESTROY_OBJECT_PATTERN).matcher(command);
            if (matcher.matches()) {
                accessTable.destroyObject(new SecurityObject(matcher.group(1)));
            }
            return;
        }
        if (command.startsWith("destroy subject")) {
            Matcher matcher = Pattern.compile(DESTROY_SUBJECT_PATTERN).matcher(command);
            if (matcher.matches()) {
                accessTable.destroySubject(new Subject(matcher.group(1)));
            }
            return;
        }
        if (command.startsWith("enter")) {
            Matcher matcher = Pattern.compile(ENTER_RULE_PATTERN).matcher(command);
            if (matcher.matches()) {
                accessTable.enterRule(new Subject(matcher.group(2)), new SecurityObject(matcher.group(3)), AccessRule.fromStr(matcher.group(1)));
            }
            return;
        }
        if (command.startsWith("delete")) {
            Matcher matcher = Pattern.compile(DELETE_RULE_PATTERN).matcher(command);
            if (matcher.matches()) {
                accessTable.deleteRule(new Subject(matcher.group(2)), new SecurityObject(matcher.group(3)), AccessRule.fromStr(matcher.group(1)));
            }
            return;
        }
    }
}
