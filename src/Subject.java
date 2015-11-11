public class Subject extends SecurityObject {
    public Subject(String name) {
        super(name);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Subject)) {
            return false;
        }
        return getName().equals(((Subject)obj).getName());
    }
}
