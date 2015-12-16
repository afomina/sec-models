/**
 * Created by alexa on 08.11.2015.
 */
public class Exercise {
    public static void doEx(Subject admin, Subject user, SecurityObject adminFolder, SecurityObject userFolder, SecurityObject secret, HRUExecutor executor,
                            boolean tamProtect) {
        SecurityObject trojan = executor.createFile(user, userFolder, "trojan");
        trojan.setType(Type.N);
        if (executor.checkRight(admin, userFolder, AccessRule.READ, AccessRule.WRITE)) {
            executor.setAccess(admin, trojan, AccessRule.READ, AccessRule.WRITE, AccessRule.EXECUTE);
//            executor.execute("enter read into [" + admin.getName() + ", trojan]");
//            executor.execute("enter write into [" + admin.getName() + ", trojan]");
//            executor.execute("enter execute into [" + admin.getName() + ", trojan]");
        }
        Subject trojanSubject = executor.executeTrojan(admin, userFolder, trojan, adminFolder, secret, tamProtect);

        executor.copyFile(secret, trojanSubject, userFolder, user);
    }
}
