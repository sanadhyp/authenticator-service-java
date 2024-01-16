import entity.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.RoleService;

import java.util.Set;

public class TestRoleService {
    RoleService service = new RoleService();
    private static final Role SALES = new Role("SALES");
    private static final Role TRADER = new Role("TRADING");

    @BeforeAll
    static void init(){

    }

    @Test
    public void addNewRole_shouldAddRoleIfNotPresent(){
        service.addNewRole(new Role("TRADER"));
        Assertions.assertEquals(1, service.getRoles().size());
    }

    @Test
    public void addDuplicateRole_shouldThrowException(){
        service.addNewRole(TRADER);
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.addNewRole(TRADER));
    }

    @Test
    public void deleteRole_shouldAddRoleIfPresent(){
        service.addNewRole(TRADER);
        Assertions.assertTrue( service.deleteRole(TRADER));
    }

    @Test
    public void addNonExistentRole_shouldThrowException(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.deleteRole(TRADER));
    }

    @Test
    public void getRoles_shouldReturnAllRoles(){
        service.addNewRole(SALES);
        service.addNewRole(TRADER);
        Assertions.assertEquals(Set.of(SALES, TRADER), service.getRoles());
    }
}
