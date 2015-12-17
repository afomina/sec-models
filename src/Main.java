import java.util.*;

public class Main {
    public static void main(String[] args) {
//        tam1();
        tam2(false);
    }

    /*private static void hru() {
        HRUExecutor executor = new HRUExecutor();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter mode (1 or 2)");
        int mode = scanner.nextInt();
        System.out.println("Enable protection? (yes/no)");
        executor.setEnableProtection(scanner.next().equalsIgnoreCase("yes"));

        Subject admin = (Subject) executor.execute("create subject admin");
        Subject user = (Subject) executor.execute("create subject user");
        SecurityObject adminFolder = executor.createFile(admin, null, "o1");
        SecurityObject userFolder = executor.createFile(user, null, "o2");
        SecurityObject secretFolder = executor.createFile(admin, null, "o3");
        secretFolder.setContent("some secret info");
        executor.execute("enter read into [admin, o2]");

        if (mode == 1) {
            executor.grantAccess(user, admin, userFolder, AccessRule.WRITE, AccessRule.EXECUTE);
            Exercise.doEx(admin, user, adminFolder, userFolder, secretFolder, executor, false);
        } else if (mode == 2) {
            SecurityObject o4 = executor.createFile(user, null, "o4");
            executor.grantAccess(user, admin, o4, AccessRule.READ, AccessRule.WRITE, AccessRule.EXECUTE);
            Exercise.doEx(admin, user, adminFolder, o4, secretFolder, executor, false);
        }
    }*/

    private static void tam1() {

        HRUExecutor executor = new HRUExecutor();
        AccessTable table = executor.getAccessTable();
//        ListenableGraph g = graph();
        Type begin = Type.U;
        Subject s1 = createSubject("s1", begin, table);

        Map<Type, List<Type>> graph = tam1(executor, s1, Type.W, Type.V);
        for (Map.Entry<Type, List<Type>> entry : graph.entrySet()) {
            Type a = entry.getKey();
            for (Type type : graph.get(a)) {
                System.out.println(a + "->" + type);
            }
        }

    }

    private static Map<Type, List<Type>> tam1(HRUExecutor executor, Subject s1, Type ... types) {
        Map<Type, List<Type>> graph = new HashMap<>();
        AccessTable table = executor.getAccessTable();
        List<Type> ends  = new ArrayList<>();

        SecurityObject o1 = new SecurityObject("o1");
        Type end = Type.V;
        o1.setType(end);
        ends.add(end);
//        g.addEdge(begin, end);
        table.createObject(o1);
        executor.setAccess(s1, o1, AccessRule.READ);

        Subject s2 = createSubject("s2", Type.W, table);
        ends.add(Type.W);
//        g.addEdge(begin, Type.W);
        Subject s3 = createSubject("s3", Type.U, table);
        ends.add(Type.U);
//        g.addEdge(begin, Type.U);
        executor.setAccess(s2, o1, AccessRule.READ1);
        executor.setAccess(s3, o1, AccessRule.READ2);

        for (Type type : ends) {
            if (!graph.containsKey(s1.getType())) {
                graph.put(s1.getType(), new ArrayList<Type>());
            }
            graph.get(s1.getType()).add(type);
            for (Type begin : types) {
                if (!graph.containsKey(begin)) {
                    graph.put(begin, new ArrayList<Type>());
                }
                graph.get(begin).add(type);
            }
        }
//        System.out.println(table);
        return graph;
    }

    private static void tam2(boolean protect) {
        HRUExecutor executor = new HRUExecutor();
        AccessTable table = executor.getAccessTable();
        Type parentType = Type.ADMIN;
        Subject s1 = createSubject("s1", parentType, table);
        Subject s2 = createSubject("s2", Type.U, table);
        SecurityObject o1 = executor.createFile(s1, null, "o1",null, Type.V);
        SecurityObject o2 = executor.createFile(s2, null, "o2", null, Type.N);
        SecurityObject o3 = executor.createFile(s1, o1, "o3", null, Type.V);
        o3.setContent("SECRET");
        executor.setAccess(s1, o2, AccessRule.READ, AccessRule.WRITE, AccessRule.EXECUTE);

        Map<Type, Set<Type>> graph = Exercise.doEx(s1, s2, o1, o2, o3, executor, protect);

        for (Map.Entry<Type, Set<Type>> entry : graph.entrySet()) {
            Type a = entry.getKey();
            for (Type type : graph.get(a)) {
                System.out.println(a + "->" + type);
            }
        }
//        System.out.println(table);
//
//        ListenableGraph g = new ListenableDirectedGraph(DefaultEdge.class);
//        JGraphModelAdapter adapter = new JGraphModelAdapter(g);
//        JGraph graph = new JGraph(adapter);
//
//        Type[] types = {Type.ADMIN, Type.U, Type.V, Type.N};
//        for (Type type : types) {
//            g.addVertex(type);
//        }
//        positionVertexAt(Type.U, 130, 40, adapter);
//        positionVertexAt(Type.V, 60, 200, adapter);
//        positionVertexAt(Type.N, 310, 230, adapter);
//        positionVertexAt(Type.ADMIN, 410, 530, adapter);
//
//        for (Type a : new Type[]{Type.ADMIN, Type.U, Type.N}) {
//            for (Type b : types) {
//                g.addEdge(a, Type.N);
//            }
//        }
//        for (Type a : types) {
//            for (Type b : types) {
//                g.addEdge(a, Type.ADMIN);
//            }
//        }
//        for (Type a : new Type[]{Type.ADMIN, Type.V, Type.N}) {
//            for (Type b : types) {
//                g.addEdge(a, Type.V);
//            }
//        }
//
//        JFrame frame = new JFrame();
//        frame.setSize(700, 800);
//        frame.getContentPane().add(graph);
//        frame.setVisible(true);
    }

    /*private static ListenableGraph graph() {
        ListenableGraph g = new ListenableDirectedGraph(DefaultEdge.class);
        JGraphModelAdapter adapter = new JGraphModelAdapter(g);
        JGraph graph = new JGraph(adapter);

        Type[] types = {Type.U, Type.V, Type.W};
        for (Type type : types) {
            g.addVertex(type);
        }
        positionVertexAt(Type.U, 130, 40, adapter);
        positionVertexAt(Type.V, 60, 200, adapter);
        positionVertexAt(Type.W, 310, 230, adapter);
        for (Type a : types) {
            for (Type b : types) {
                g.addEdge(a, b);
            }
        }

        JFrame frame = new JFrame();
        frame.setSize(500, 400);
        frame.getContentPane().add(graph);
        frame.setVisible(true);

        return g;
    }

    private static void positionVertexAt(Object vertex, int x, int y, JGraphModelAdapter adapter) {
        DefaultGraphCell cell = adapter.getVertexCell(vertex);
        Map attr = cell.getAttributes();
        Rectangle2D b = GraphConstants.getBounds(attr);

        GraphConstants.setBounds(attr, new Rectangle(x, y, (int) b.getWidth(), (int) b.getHeight()));

        Map cellAttr = new HashMap();
        cellAttr.put(cell, attr);
        adapter.edit(cellAttr, null, null, null);
    }*/

    private static Subject createSubject(String name, Type type, AccessTable table) {
        Subject s1 = new Subject(name);
        s1.setType(type);
        table.createSubject(s1);
        return s1;
    }
}
