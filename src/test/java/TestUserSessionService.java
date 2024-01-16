import entity.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import service.UserService;
import service.UserSessionService;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class TestUserSessionService {

    UserSessionService service = new UserSessionService(5);

    @Test
    public void getSessionToken_shouldCreateANewToken() {
        Assertions.assertNotNull(service.begin("bob"));
    }

    @ParameterizedTest
    @MethodSource("tokenExpiry")
    public void validateToken_shouldThrowExceptionIfTokenExpired(String userId, int executionTimeSeconds) {
        var aToken = service.begin(userId);
        try {
            TimeUnit.SECONDS.sleep(executionTimeSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertThrows(IllegalStateException.class, () -> service.validate(userId));
    }

    private static Stream<Arguments> tokenExpiry() {
        return Stream.of(
                //Arguments.of("tom", 2),
                Arguments.of("tom", 10),
                Arguments.of("bob", 5)
        );
    }

    @Test
    public void validateToken_shouldReturnTokenIfValid() {
        var aToken = service.begin("kevin");
        Assertions.assertEquals(aToken, service.validateToken(aToken));
    }

    @Test
    public void validateToken_shouldThrowExceptionForInvalidToken() {
        Assertions.assertThrows(IllegalStateException.class, () -> service.validateToken("random_token"));
    }

    @Test
    public void invalidateToken_shouldThrowExceptionWhenInvalidTokenSupplied() {
        Assertions.assertThrows(IllegalStateException.class, () -> service.inValidate("random_token"));
    }

    @Test
    public void invalidateToken_shouldInvalidateToken() {
        var aToken = service.begin("kevin");
        service.inValidate(aToken);
        Assertions.assertThrows(IllegalStateException.class, () -> service.inValidate(aToken));
    }

}
