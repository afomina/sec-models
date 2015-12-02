import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

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

    private static void tam() {
        HRUExecutor executor = new HRUExecutor();
        AccessTable table = executor.getAccessTable();
        Subject s1 = createSubject("s1", Type.U, table);

        SecurityObject o1 = new SecurityObject("o1");
        o1.setType(Type.V);
        table.createObject(o1);
        executor.setAccess(s1, o1, AccessRule.READ);

        Subject s2 = createSubject("s2", Type.W, table);
        Subject s3 = createSubject("s3", Type.U, table);
        executor.setAccess(s2, o1, AccessRule.READ1);
        executor.setAccess(s3, o1, AccessRule.READ2);

    }

    private static Subject createSubject(String name, Type type, AccessTable table) {
        Subject s1 = new Subject(name);
        s1.setType(type);
        table.createSubject(s1);
        return s1;
    }
}
