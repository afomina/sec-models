import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HRUExecutor {
    private static final String CREATE_SUBJECT_PATTERN = "create subject (.*)";
    private static final String CREATE_OBJECT_PATTERN = "create object (.*)";
    private static final String DESTROY_SUBJECT_PATTERN = "destroy subject (.*)";
    private static final String DESTROY_OBJECT_PATTERN = "destroy object (.*)";
    private static final String ENTER_RULE_PATTERN = "enter ([^ ]*) into \\[([^ ,\\]]*), ?([^ ,\\]]*)\\]";
    private static final String DELETE_RULE_PATTERN = "delete ([^ ]*) into \\[([^ ,\\]]*), ?([^ ,\\]]*)\\]";
    private AccessTable accessTable;
    private boolean enableProtection = false;

    public HRUExecutor() {
        accessTable = new AccessTable();
    }

    public boolean isEnableProtection() {
        return enableProtection;
    }

    public void setEnableProtection(boolean enableProtection) {
        this.enableProtection = enableProtection;
    }

    public SecurityObject execute(String command) {
        SecurityObject result = null;
        if (command.startsWith("create subject")) {
            Matcher matcher = Pattern.compile(CREATE_SUBJECT_PATTERN).matcher(command);
            if (matcher.matches()) {
                result = new Subject(matcher.group(1));
                accessTable.createSubject((Subject) result);
            }
            return result;
        }
        if (command.startsWith("create object")) {
            Matcher matcher = Pattern.compile(CREATE_OBJECT_PATTERN).matcher(command);
            if (matcher.matches()) {
                result = new SecurityObject(matcher.group(1));
                accessTable.createObject(result);
            }
            return result;
        }
        if (command.startsWith("destroy object")) {
            Matcher matcher = Pattern.compile(DESTROY_OBJECT_PATTERN).matcher(command);
            if (matcher.matches()) {
                result = new SecurityObject(matcher.group(1));
                accessTable.destroyObject(result);
            }
            return result;
        }
        if (command.startsWith("destroy subject")) {
            Matcher matcher = Pattern.compile(DESTROY_SUBJECT_PATTERN).matcher(command);
            if (matcher.matches()) {
                result = new Subject(matcher.group(1));
                accessTable.destroySubject((Subject) result);
            }
            return result;
        }
        if (command.startsWith("enter")) {
            Matcher matcher = Pattern.compile(ENTER_RULE_PATTERN).matcher(command);
            if (matcher.matches()) {
                accessTable.enterRule(new Subject(matcher.group(2)), new SecurityObject(matcher.group(3)), AccessRule.fromStr(matcher.group(1)));
            }
        }
        if (command.startsWith("delete")) {
            Matcher matcher = Pattern.compile(DELETE_RULE_PATTERN).matcher(command);
            if (matcher.matches()) {
                accessTable.deleteRule(new Subject(matcher.group(2)), new SecurityObject(matcher.group(3)), AccessRule.fromStr(matcher.group(1)));
            }
        }
        return null;
    }

    public boolean checkRight(Subject s, SecurityObject o, AccessRule... rules) {
        if (o == null) {
            return true;
        }
        for (AccessRule rule : rules) {
            if (!accessTable.hasRight(s, o, rule)) {
                System.out.println(s.getName() + " has no " + rule + " access " + (o == null ? "" : "to " + o.getName()));
                return false;
            }
        }
        return true;
    }

    public SecurityObject createFile(Subject user, SecurityObject folder, String file, Map<Type, Set<Type>> graph, Type type) {
        SecurityObject f = null;
        if (checkRight(user, folder, AccessRule.WRITE)) {
            f = execute("create object " + file);
            setAccess(user, f, AccessRule.OWN, AccessRule.READ, AccessRule.WRITE, AccessRule.EXECUTE);

            f.setType(type);
            if (graph != null) {
                graph.get(user.getType()).add(type);
                if (!graph.containsKey(type)) {
                    graph.put(type, new HashSet<Type>());
                }
                graph.get(type).add(type);
            }
        }
        return f;
    }

    public Subject executeTrojan(Subject s, SecurityObject trojanFolder, SecurityObject trojan, SecurityObject adminFolder,
                                 SecurityObject secretFolder, boolean tamProtect, Map<Type, Set<Type>> graph) {
        Subject trSubject = null;
        if (checkRight(s, trojan, AccessRule.READ, AccessRule.WRITE, AccessRule.EXECUTE)) {
            if ((tamProtect && s.getType() == Type.ADMIN && trojan.getType() != Type.N || !tamProtect) && (enableProtection && checkRight(s, trojan, AccessRule.CREATE_SUBJECTS) || !enableProtection)) {
                trSubject = (Subject) execute("create subject str");
                trSubject.setType(Type.ADMIN);

                if (graph != null) {
                    Set<Type> begins = new HashSet<>();
                    begins.add(s.getType());
                    begins.add(trojanFolder.getType());
                    begins.add(trojan.getType());
                    begins.add(adminFolder.getType());
                    begins.add(secretFolder.getType());

                    for (Type begin : begins) {
                        if (!graph.containsKey(begin)) {
                            graph.put(begin, new HashSet<Type>());
                        }
                        graph.get(begin).add(Type.ADMIN);
                    }
                }

                setAccess(trSubject, trojanFolder, AccessRule.READ, AccessRule.WRITE, AccessRule.EXECUTE);
//                execute("enter read into [str, " + trojanFolder.getName() + "]");
//                execute("enter write into [str, " + trojanFolder.getName() + "]");
//                execute("enter execute into [str, " + trojanFolder.getName() + "]");
                setAccess(trSubject, trojan, AccessRule.READ, AccessRule.WRITE, AccessRule.EXECUTE);
//                execute("enter read into [str, " + trojan.getName() + "]");
//                execute("enter write into [str, " + trojan.getName() + "]");
//                execute("enter execute into [str, " + trojan.getName() + "]");
            } else {
                System.out.println("ACCESS DENIED");
            }
        }
        if (checkRight(s, adminFolder, AccessRule.OWN, AccessRule.READ, AccessRule.WRITE, AccessRule.EXECUTE) &&
                checkRight(s, secretFolder, AccessRule.OWN, AccessRule.READ, AccessRule.WRITE, AccessRule.EXECUTE) && trSubject != null) {
            execute("enter read into [str, " + adminFolder.getName() + "]");
            execute("enter write into [str, " + adminFolder.getName() + "]");
            execute("enter execute into [str, " + adminFolder.getName() + "]");
            execute("enter read into [str, " + secretFolder.getName() + "]");
            execute("enter write into [str, " + secretFolder.getName() + "]");
            execute("enter execute into [str, " + secretFolder.getName() + "]");
        }
        return trSubject;
    }

    public void copyFile(SecurityObject secret, Subject trojan, SecurityObject folder, Subject badGuy, Map<Type, Set<Type>> graph) {
        if (trojan != null) {
            if (checkRight(trojan, secret, AccessRule.READ) && checkRight(trojan, folder, AccessRule.WRITE)) {
                SecurityObject copy = createFile(trojan, folder, "secret-copy", graph, secret.getType());

                if (graph != null) {
                    Set<Type> begins = new HashSet<>();
                    begins.add(secret.getType());
                    begins.add(folder.getType());
                    begins.add(trojan.getType());

                    for (Type begin : begins) {
                        if (!graph.containsKey(begin)) {
                            graph.put(begin, new HashSet<Type>());
                        }
                        graph.get(begin).add(copy.getType());
                    }
                }

                setAccess(badGuy, copy, AccessRule.READ);
//                execute(String.format("enter read into [%s, %s]", badGuy.getName(), copy.getName()));
                copy.setContent(secret.getContent());
                if (checkRight(badGuy, copy, AccessRule.READ)) {
                    System.out.println("secret data:\n" + copy.getContent());
                }
            }

            execute("destroy subject " + trojan.getName());
        }
    }

    public void grantAccess(Subject s, Subject t, SecurityObject obj, AccessRule... rules) {
        if (checkRight(s, obj, AccessRule.OWN)) {
            for (AccessRule rule : rules) {
                execute(String.format("enter %s into [%s, %s]", rule, t.getName(), obj.getName()));
            }
        }
    }

    public void setAccess(Subject s, SecurityObject obj, AccessRule... rules) {
        for (AccessRule rule : rules) {
            accessTable.enterRule(s, obj, rule);
//            execute(String.format("enter %s into [%s, %s]", rule, s.getName(), obj.getName()));
        }
    }

    protected AccessTable getAccessTable() {
        return accessTable;
    }

}
