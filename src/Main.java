import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HRUExecutor executor = new HRUExecutor();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            executor.execute(scanner.nextLine());
        }
    }
}
