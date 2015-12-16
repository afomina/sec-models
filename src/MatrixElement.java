public class MatrixElement {
    private Subject subject;
    private SecurityObject object;
    private AccessRule rule;

    public MatrixElement() {
    }

    public MatrixElement(Subject subject, SecurityObject object, AccessRule rule) {
        this.subject = subject;
        this.object = object;
        this.rule = rule;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public SecurityObject getObject() {
        return object;
    }

    public void setObject(SecurityObject object) {
        this.object = object;
    }

    public AccessRule getRule() {
        return rule;
    }

    public void setRule(AccessRule rule) {
        this.rule = rule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatrixElement that = (MatrixElement) o;

        if (object != null ? !object.equals(that.object) : that.object != null) return false;
        if (rule != that.rule) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subject != null ? subject.hashCode() : 0;
        result = 31 * result + (object != null ? object.hashCode() : 0);
        result = 31 * result + (rule != null ? rule.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "subject=" + subject +
                ", object=" + object +
                ", rule=" + rule;
    }
}
