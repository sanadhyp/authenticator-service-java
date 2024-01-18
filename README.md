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
2. Role <br> 
~~3. Session : contains token and it's creation timestamp~~ it is not used in the current design


Future improvements:
1. Thread-safety
2. Bi-directional cache in UserSessionService for storing user-token,  token-user relationship
3. Integration tests