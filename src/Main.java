import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
}
