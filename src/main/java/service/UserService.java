package service;

import entity.Role;
import entity.User;

import java.util.*;

public class UserService {
    private UserSessionService session = new UserSessionService();
    private RoleService roleService = new RoleService();

    private Map<String, User> userStore = new HashMap<>();

    public Map<String, User> getUserStore() {
        return this.userStore;
    }

    public User createUser(String id, String name) throws IllegalArgumentException {
        if (userStore.containsKey(id)) {
            throw new IllegalArgumentException("User already exists.");
        }
        User user = new User();
        user.setId(id);
        user.setName(name);
        userStore.put(id, user);
        return user;
    }

    public User getUserById(String id) {
        return this.getUserStore().get(id);
    }

    public boolean deleteUser(String id) {
        if (!userStore.containsKey(id)) {
            throw new IllegalArgumentException("User doesn't exist.");
        }
        userStore.remove(id);
        return true;
    }

    public void setPassword(String id, String pwd) {
        String encoded = Base64.getEncoder().encodeToString(pwd.getBytes());
        this.getUserStore().get(id).setPassword(encoded);
    }

    public String getEncryptedPassword(String id) {
        return this.getUserStore().get(id).getPassword();
    }

    public String decodePassword(String encoded) {
        return new String(Base64.getDecoder().decode(encoded.getBytes()));
    }

    public boolean addRole(String userId, Role role) {
        if(!this.getUserStore().containsKey(userId)){
            throw new IllegalArgumentException("User doesn't exist");
        }
        try {
            this.roleService.addNewRole(role);
        } catch (IllegalArgumentException e){
            //do nothing, role already exist
        }
        return this.getUserStore().get(userId).getRoles().add(role);
    }

    public boolean addRoles(String userId, Role... roles) {
        return this.getUserStore().get(userId).setRoles(Set.of(roles));
    }

    public void removeAllRolesForUser(String userId) {
        this.getUserStore().get(userId).removeRoles();
    }

    public String authenticate(String userId, String pwdPlain) throws IllegalStateException {
        if (this.getUserStore().containsKey(userId) && this.getUserStore().get(userId).getPassword().equals(pwdPlain)) {
            return session.validate(userId);
        } else {
            throw new IllegalStateException("Invalid userId or password");
        }
    }

    public String login(String userId, String pwdEncrypted) throws IllegalStateException {
        if (this.getUserStore().containsKey(userId) && this.getUserStore().get(userId).getPassword().equals(pwdEncrypted)) {
            return session.begin(userId);
        } else {
            throw new IllegalStateException("Invalid userId or password");
        }
    }

    public boolean checkRole(String authToken, Role role) {
        try {
            var roles = getAllRoles(authToken);
            var isValidRole = roles.contains(role) && roleService.isValidRole(role);

            if(!isValidRole) {
                this.getUserById(session.fetchUserId(authToken)).removeRole(role);
            }
            return isValidRole;
        } catch (Exception e) {
            return false;
        }
    }

    public Set<Role> fetchRoles(String authToken) throws IllegalStateException {
        return getAllRoles(authToken);
    }

    private Set<Role> getAllRoles(String authToken) {
        if (session.validateToken(authToken).equals(authToken)) {
            var user = this.getUserById(session.fetchUserId(authToken));
            var validRoles = user.getRoles().retainAll(getRoleService().getRoles());
            return user.getRoles();
        }
        return new HashSet<Role>();
    }
    public UserSessionService getSession() {
        return this.session;
    }

    public RoleService getRoleService() {
        return this.roleService;
    }
}
