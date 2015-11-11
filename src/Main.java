import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HRUExecutor executor = new HRUExecutor();
//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            executor.execute(scanner.nextLine());
//        }
        Subject admin = (Subject) executor.execute("create subject admin");
        Subject user = (Subject) executor.execute("create subject user");
        SecurityObject adminFolder = executor.createFile(admin, null, "o1");
        SecurityObject userFolder = executor.createFile(user, null, "o2");
        SecurityObject secretFolder = executor.createFile(admin, null, "o3");
        secretFolder.setContent("some secret info");
        executor.execute("enter read into [admin, o2]");
        Exercise.doEx(admin, user, adminFolder, userFolder, secretFolder, executor);
    }
}
