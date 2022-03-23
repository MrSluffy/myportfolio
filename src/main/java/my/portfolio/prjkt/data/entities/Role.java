package my.portfolio.prjkt.data.entities;

public enum Role {

    USER("user"), ADMIN("admin"), GUEST("guest");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
