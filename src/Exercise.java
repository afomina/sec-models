import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alexa on 08.11.2015.
 */
public class Exercise {
    public static Map<Type, List<Type>> doEx(Subject admin, Subject user, SecurityObject adminFolder, SecurityObject userFolder, SecurityObject secret, HRUExecutor executor,
                            boolean tamProtect) {
        Map<Type, List<Type>> graph = new HashMap<>();
//        List<Type> begins = new ArrayList<>();
//        List<Type> ends = new ArrayList<>();
//        begins.add(admin.getType());
//        begins.add(user.getType());
//        begins.add(Type.N);

        graph.put(user.getType(), new ArrayList<>());
        SecurityObject trojan = executor.createFile(user, userFolder, "trojan", graph, Type.N);
//        ends.add(trojan.getType());
        if (executor.checkRight(admin, userFolder, AccessRule.READ, AccessRule.WRITE)) {
            executor.setAccess(admin, trojan, AccessRule.READ, AccessRule.WRITE, AccessRule.EXECUTE);
        }
//        begins.add(userFolder.getType());
//        begins.add(trojan.getType());
        Subject trojanSubject = executor.executeTrojan(admin, userFolder, trojan, adminFolder, secret, tamProtect , graph);

        executor.copyFile(secret, trojanSubject, userFolder, user, graph);

        return graph;
    }
}
