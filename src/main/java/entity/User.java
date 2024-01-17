package entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class User {
    private java.lang.String id;
    private java.lang.String name;
    private java.lang.String password;
    private Set<Role> roles = new HashSet<>();

    public java.lang.String getId() {
        return id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getPassword() {
        return password;
    }

    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public boolean setRoles(Set<Role> roles) {
        return this.roles.addAll(roles);
    }
    public void removeRoles() {
        this.roles = new HashSet<>();
    }
    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }

        var u = (User) obj;

        if (u.id.equals(this.id)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public java.lang.String toString() {
        return "User[" + id + ", " + name + "]";
    }
}
