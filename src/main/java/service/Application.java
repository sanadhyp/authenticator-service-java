package service;

import entity.Role;
import entity.User;

import java.util.Base64;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        System.out.println("Enter your choice...");
        terminal();
    }

    private static void terminal() {
        String userId, userName, userPassword, encyptedPwd, token;
        Role role;

        UserService userService = new UserService();
        RoleService roleService = userService.getRoleService();
        UserSessionService userSessionService = userService.getSession();

        while(true) {
            System.out.println("---------------------------");
            System.out.println("1  # Create a new user.");
            System.out.println("2  # Delete a user.");
            System.out.println("3  # Create a new role.");
            System.out.println("4  # Delete role.");
            System.out.println("5  # Assign role to user.");
            System.out.println("6  # User login.");
            System.out.println("7  # Authenticate user.");
            System.out.println("8  # Validate token.");
            System.out.println("9  # Invalidate token.");
            System.out.println("10 # Check role on token.");
            System.out.println("11 # Fetch all roles for token.");
            System.out.println("0  # Exit.");
            System.out.println("---------------------------");
            Scanner scanner = new Scanner(System.in);

            try {
                switch (scanner.nextInt()) {
                    case 0:
                        System.out.println("---------------------------");
                        return;
                    case 1:
                        System.out.println("User id:");
                        userId = scanner.next();
                        System.out.println("User name");
                        userName = scanner.next();
                        System.out.println("Password:");
                        userPassword = scanner.next();
                        userService.createUser(userId, userName);
                        System.out.println("user created");
                        userService.setPassword(userId, userPassword);
                        System.out.println("password set");
                        break;
                    case 2:
                        System.out.println("User id:");
                        userId = scanner.next();
                        userService.deleteUser(userId);
                        System.out.println("user deleted");
                        break;
                    case 3:
                        System.out.println("Role id:");
                        role = new Role(scanner.next());
                        roleService.addNewRole(role);
                        System.out.println("role added.");
                        break;
                    case 4:
                        System.out.println("Role id:");
                        role = new Role(scanner.next());
                        roleService.deleteRole(role);
                        System.out.println("role deleted.");
                        break;
                    case 5:
                        System.out.println("User id:");
                        userId = scanner.next();
                        System.out.println("Role id:");
                        role = new Role(scanner.next());
                        userService.addRole(userId, role);
                        System.out.println("role added to user.");
                        break;
                    case 6:
                        System.out.println("User id:");
                        userId = scanner.next();
                        System.out.println("Password:");
                        userPassword = scanner.next();
                        encyptedPwd = Base64.getEncoder().encodeToString(userPassword.getBytes());
                        token = userService.login(userId, encyptedPwd);
                        System.out.println("User logged in. Token: " + token);
                        break;
                    case 7:
                        System.out.println("User id:");
                        userId = scanner.next();
                        System.out.println("Password:");
                        userPassword = scanner.next();
                        encyptedPwd = Base64.getEncoder().encodeToString(userPassword.getBytes());
                        token = userService.authenticate(userId, encyptedPwd);
                        System.out.println("User authenticated. Token: " + token);
                        break;
                    case 8:
                        System.out.println("Token:");
                        token = scanner.next();
                        userSessionService.validateToken(token);
                        System.out.println("Token " + token + " validated.");
                        break;
                    case 9:
                        System.out.println("Token:");
                        token = scanner.next();
                        userSessionService.inValidate(token);
                        System.out.println("Token " + token + " in-validated.");
                        break;
                    case 10:
                        System.out.println("Token:");
                        token = scanner.next();
                        System.out.println("Role:");
                        String r = scanner.next();
                        role = new Role(r);
                        System.out.println("Token " + token + " has role "+ r + " : " + userService.checkRole(token, role));
                        break;
                    case 11:
                        System.out.println("Token:");
                        token = scanner.next();
                        System.out.println("Token " + token + " has these roles "+ userService.fetchRoles(token));
                        break;
                    default:
                        System.out.println("Invalid choice...");
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
