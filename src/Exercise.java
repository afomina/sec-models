import java.util.*;

/**
 * Created by alexa on 08.11.2015.
 */
public class Exercise {
    public static Map<Type, Set<Type>> doEx(Subject admin, Subject user, SecurityObject adminFolder, SecurityObject userFolder, SecurityObject secret, HRUExecutor executor,
                            boolean tamProtect) {
        Map<Type, Set<Type>> graph = new HashMap<>();

        graph.put(user.getType(), new HashSet<Type>());
        graph.put(admin.getType(), new HashSet<Type>());
        SecurityObject trojan = executor.createFile(user, userFolder, "trojan", graph, Type.N);
        if ((tamProtect && admin.getType() == Type.ADMIN && trojan.getType() != Type.N || !tamProtect) && executor.checkRight(admin, userFolder, AccessRule.READ, AccessRule.WRITE)) {
            executor.setAccess(admin, trojan, AccessRule.READ, AccessRule.WRITE, AccessRule.EXECUTE);
            graph.get(admin.getType()).add(trojan.getType());
        }
//        begins.add(userFolder.getType());
//        begins.add(trojan.getType());
        Subject trojanSubject = executor.executeTrojan(admin, userFolder, trojan, adminFolder, secret, tamProtect , graph);

        executor.copyFile(secret, trojanSubject, userFolder, user, graph);

        return graph;
    }
}
