# authenticator-service-java

Run Application.java to launch app on console / terminal.

For individual services, run Junits to verify operations.

Main services includes:
1. UserService : all user operations must be performed through it.
2. RoleService : for maintaining roles
3. UserSessionService: all operations related to session-token

Default token-timeout: Configured in ApplicationConfiguration.java

Entities:
1. User
2. Role 
3. Session : contains token and it's creation timestamp