import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.ListenableDirectedGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        tam2();
    }

    private static void hru() {
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
            Exercise.doEx(admin, user, adminFolder, userFolder, secretFolder, executor);
        } else if (mode == 2) {
            SecurityObject o4 = executor.createFile(user, null, "o4");
            executor.grantAccess(user, admin, o4, AccessRule.READ, AccessRule.WRITE, AccessRule.EXECUTE);
            Exercise.doEx(admin, user, adminFolder, o4, secretFolder, executor);
        }
    }

    private static void tam1() {
        HRUExecutor executor = new HRUExecutor();
        AccessTable table = executor.getAccessTable();
        ListenableGraph g = graph();
        Type begin = Type.U;
        Subject s1 = createSubject("s1", begin, table);

        SecurityObject o1 = new SecurityObject("o1");
        Type end = Type.V;
        o1.setType(end);
//        g.addEdge(begin, end);
        table.createObject(o1);
        executor.setAccess(s1, o1, AccessRule.READ);

        Subject s2 = createSubject("s2", Type.W, table);
//        g.addEdge(begin, Type.W);
        Subject s3 = createSubject("s3", Type.U, table);
//        g.addEdge(begin, Type.U);
        executor.setAccess(s2, o1, AccessRule.READ1);
        executor.setAccess(s3, o1, AccessRule.READ2);

        System.out.println(table);
    }

    private static void tam2() {
        HRUExecutor executor = new HRUExecutor();
        AccessTable table = executor.getAccessTable();
        Type parentType = Type.ADMIN;
        Subject s1 = createSubject("s1", parentType, table);
        Subject s2 = createSubject("s2", Type.U, table);
        SecurityObject o1 = executor.createFile(s1, null, "o1");
        o1.setType(Type.V);
        SecurityObject o2 = executor.createFile(s2, null, "o2");
        o2.setType(Type.N);
        SecurityObject o3 = executor.createFile(s1, o1, "o3");
        o3.setType(Type.V);
        executor.setAccess(s1, o2, AccessRule.READ, AccessRule.WRITE, AccessRule.EXECUTE);
        Exercise.doEx(s1, s2, o1, o2, o3, executor);

        System.out.println(table);
    }

    private static ListenableGraph graph() {
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
    }

    private static Subject createSubject(String name, Type type, AccessTable table) {
        Subject s1 = new Subject(name);
        s1.setType(type);
        table.createSubject(s1);
        return s1;
    }
}
