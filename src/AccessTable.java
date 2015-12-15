import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AccessTable {
    private Set<MatrixElement> matrix = new HashSet<MatrixElement>();
    private Set<Subject> avaiableSubjects = new HashSet<Subject>();
    private Set<SecurityObject> avaiableObjects = new HashSet<SecurityObject>();

    public void enterRule(Subject s, SecurityObject obj, AccessRule r){
        if (r == null) {
            System.out.println("no such rule");
            return;
        }
        if (exists(s, obj)) {
            matrix.add(new MatrixElement(s, obj, r));
        }
//        System.out.println(matrix);
    }

    public void deleteRule(Subject s, SecurityObject obj, AccessRule r) {
        if (r == null) {
            System.out.println("no such rule");
            return;
        }
        if (exists(s, obj)) {
            matrix.remove(new MatrixElement(s, obj, r));
        }
//        System.out.println(matrix);
    }

    public void createSubject(Subject s) {
        if (!avaiableSubjects.add(s)) {
            System.out.println("Subject " + s + "already exists");
        }
//        System.out.println(avaiableSubjects);
    }

    public void createObject(SecurityObject obj) {
        if (!avaiableObjects.add(obj)) {
            System.out.println("Object " + obj + "already exists");
        }
//        System.out.println(avaiableObjects);
    }

    public void destroySubject(Subject s) {
        if (check(s)) {
            avaiableSubjects.remove(s);
            for (Iterator<MatrixElement> iterator = matrix.iterator(); iterator.hasNext(); ) {
                MatrixElement matrixElement = iterator.next();
                if (s.equals(matrixElement.getSubject())) {
                    iterator.remove();
                }
            }
        }
//        System.out.println(avaiableSubjects);
    }

    public void destroyObject(SecurityObject obj) {
        if (!check(obj)) {
            return;
        }
        if (avaiableSubjects.contains(obj)) {
            System.out.println("Object " + obj + " is in subjects set, can't destroy");
            return;
        }
        avaiableObjects.remove(obj);
        for (Iterator<MatrixElement> iterator = matrix.iterator(); iterator.hasNext(); ) {
            MatrixElement matrixElement = iterator.next();
            if (obj.equals(matrixElement.getObject())) {
                iterator.remove();
            }
        }
//        System.out.println(avaiableObjects);
    }

    protected boolean exists(Subject s, SecurityObject o) {
        return check(s) && check(o);
    }

    protected boolean check(Subject s) {
        if (!avaiableSubjects.contains(s)) {
            System.out.println("Subject " + s + " not in subjects set");
            return false;
        }
        return true;
    }

    protected boolean check(SecurityObject obj) {
        if (!avaiableObjects.contains(obj)) {
            System.out.println("Object " + obj + " not in objects set");
            return false;
        }
        return true;
    }

    public boolean hasRight(Subject s, SecurityObject obj, AccessRule r) {
        return matrix.contains(new MatrixElement(s, obj, r));
    }

    @Override
    public String toString() {
        String res = "";
        for (MatrixElement matrixElement : matrix) {
            res += matrixElement + "\n";
        }
        return res;
    }
}
