import entity.Role;
import entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.util.Set;

public class TestUserService {
    public static final String BOB_ID = "bob";
    public static final String BOB_NAME = "Bob M";
    public static final String BOB_PWD_PLAIN = "bob_p";
    public static final String BOB_PWD_ENCRYPTED = "Ym9iX3A=";
    public static final Role SALES = new Role("SALES");
    public static final Role ADMIN = new Role("ADMIN");
    public static final Role TRADER = new Role("TRADER");
    private static UserService service;

    @BeforeAll
    public static void init() {
        service = new UserService();
        service.createUser(BOB_ID, BOB_NAME);
    }

    @Test
    public void createUser_shouldAddNewUser() {
        User u = service.createUser("tom", "Tom B");
        Assertions.assertEquals("tom", u.getId());
    }

    @Test
    public void duplicateUserAttempt_shouldFailWithException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.createUser(BOB_ID, "New Bob"));
    }

    @Test
    public void deleteUser_shouldRemoveTheUser() {
        service.createUser("kevin", "Kevin P");
        Assertions.assertTrue(service.deleteUser("kevin"));
    }

    @Test
    public void deleteNonExistentUser_shouldFailWithException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.deleteUser("kevin"));
    }

    @Test
    public void getUserById_shouldReturnUserForInputId() {
        Assertions.assertEquals(service.getUserById("tom").getId(), "tom");
    }

    @Test
    public void setPassword_shouldAllowToSetAPasswordForUserId() {
        service.setPassword(BOB_ID, BOB_PWD_PLAIN);
        Assertions.assertEquals("Ym9iX3A=", service.getUserById(BOB_ID).getPassword());
    }

    @Test
    public void getPasswordForUserId_shouldShowEncodedPassword() {
        service.setPassword(BOB_ID, BOB_PWD_PLAIN);
        Assertions.assertEquals(BOB_PWD_ENCRYPTED, service.getEncryptedPassword(BOB_ID));
    }

    @Test
    public void getPasswordForUserId_shouldDecodePassword() {
        service.setPassword(BOB_ID, BOB_PWD_PLAIN);
        Assertions.assertEquals(BOB_PWD_PLAIN, service.decodePassword(service.getEncryptedPassword(BOB_ID)));
    }

    @Test
    public void addSingleRoleForUserId_shouldAddRoleForUser() {
        String userId = BOB_ID;
        var aRole = new Role("IT_READONLY");
        service.addRole(userId, aRole);

        Assertions.assertTrue(service.getUserById(userId).getRoles().contains(aRole));
    }

    @Test
    public void addMultipleRolesForUserId_shouldAddAllRolesForUser() {
        String userId = BOB_ID;
        service.addRole(userId, new Role("IT_READONLY"));
        service.addRole(userId, new Role("ADMIN"));
        Assertions.assertEquals(2, service.getUserById(userId).getRoles().size());
    }

    @Test
    public void displayAllRolesForUserId_shouldPrintAllRolesForUser() {
        String userId = BOB_ID;
        service.removeAllRolesForUser(userId);
        service.addRoles(userId, SALES, ADMIN);
        System.out.println(service.getUserById(userId).getRoles());
        Assertions.assertEquals(Set.of(SALES, ADMIN), service.getUserById(userId).getRoles());
    }

    @Test
    public void removeAllRolesForUserId_shouldRemoveAllRolesForUser() {
        String userId = BOB_ID;
        service.addRoles(userId, SALES, ADMIN);
        service.removeAllRolesForUser(userId);
        Assertions.assertEquals(0, service.getUserById(userId).getRoles().size());
    }

    @Test
    public void authenticateUser_shouldReturnAValidToken() {
        Assertions.assertNotNull(service.authenticate(BOB_ID, BOB_PWD_ENCRYPTED));
    }


    @Test
    public void roleValidForUser_shouldReturnTrue() {
        String userId = BOB_ID;
        service.setPassword(BOB_ID, BOB_PWD_PLAIN);
        service.addRoles(userId, ADMIN);
        var authToken = service.login(userId, BOB_PWD_ENCRYPTED);

        Assertions.assertTrue(service.checkRole(authToken, ADMIN));
    }

    @Test
    public void roleInValidForUser_shouldReturnFalse() {
        String userId = BOB_ID;
        service.setPassword(BOB_ID, BOB_PWD_PLAIN);
        service.addRoles(userId, ADMIN);
        var authToken = service.login(userId, BOB_PWD_ENCRYPTED);

        Assertions.assertFalse(service.checkRole(authToken, SALES));
    }

    @Test
    public void allRolesForValidToken_shouldReturnAllRoles() {
        String userId = BOB_ID;
        service.setPassword(BOB_ID, BOB_PWD_PLAIN);
        service.removeAllRolesForUser(BOB_ID);
        service.addRoles(userId, ADMIN, SALES, TRADER);
        var authToken = service.login(userId, BOB_PWD_ENCRYPTED);

        Assertions.assertEquals(3, service.fetchRoles(authToken).size());
    }

    @Test
    public void allRolesForInValidToken_shouldThrowException() {
        Assertions.assertThrows(IllegalStateException.class, () -> service.fetchRoles("random_token"));
    }


}
