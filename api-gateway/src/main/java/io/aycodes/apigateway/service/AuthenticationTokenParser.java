package io.aycodes.persistenceservice.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.aycodes.commons.enums.Roles;
import io.aycodes.commons.exception.AccessTokenException;
import io.aycodes.commons.exception.RefreshTokenException;
import io.aycodes.persistenceservice.util.AlgorithmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationTokenParser {

    private AlgorithmUtil algorithmUtil;
    @Autowired
    private final AuthenticationTokenSettings authenticationTokenSettings;
    private DecodedJWT decodeAndVerifyJwtToken(final String token) throws AccessTokenException {
        try {
            Algorithm   algorithm       = algorithmUtil.getAlgorithmForAccessToken();
            JWTVerifier verifier        = JWT.require(algorithm)
                    .withAudience(authenticationTokenSettings.getAudience()).build();
            return verifier.verify(token);
        } catch (final Exception e) {
            final String message = "Invalid access token";
            log.error(message,e);
            throw new AccessTokenException(HttpStatus.FORBIDDEN, message, e);
        }
    }

    private ZonedDateTime extractExpirationDateFromClaims(@NotNull final String token) {
        return ZonedDateTime.ofInstant(decodeAndVerifyJwtToken(token).getExpiresAtAsInstant(), ZoneId.systemDefault());
    }

    private ZonedDateTime extractIssuedDateFromClaims(@NotNull final String token) {
        return ZonedDateTime.ofInstant(decodeAndVerifyJwtToken(token).getIssuedAtAsInstant(), ZoneId.systemDefault());
    }

    private String extractTokenIdFromClaims(@NotNull final String token) {
        return decodeAndVerifyJwtToken(token).getId();
    }

    private Long extractUserIdFromClaims(@NotNull final String token) {
        return decodeAndVerifyJwtToken(token).getClaim(authenticationTokenSettings.getUserIdClaimName()).asLong();
    }

    private String extractUserUniqueIdFromClaims(@NotNull final String token) {
        return String.valueOf(decodeAndVerifyJwtToken(token).getClaim(authenticationTokenSettings.getUniqueIdClaimName()));
    }

    private Roles extractUserRoleFromClaims(@NotNull final String token) {
        return Roles.valueOf(String.valueOf(decodeAndVerifyJwtToken(token).getClaim(authenticationTokenSettings.getAuthorityClaimName())));
    }

    public AuthenticationTokenDetails parseAccessToken(final String token) throws AccessTokenException {
        return AuthenticationTokenDetails.builder()
                .tokenId(extractTokenIdFromClaims(token))
                .userId(extractUserIdFromClaims(token))
                .uniqueId(extractUserUniqueIdFromClaims(token))
                .issuedDate(extractIssuedDateFromClaims(token))
                .expirationDate(extractExpirationDateFromClaims(token))
                .userRoles(Collections.singleton(extractUserRoleFromClaims(token)))
                .build();
    }

    public AuthenticationTokenDetails parseRefreshToken(final String refreshToken) throws RefreshTokenException {
        try {
            Algorithm   algorithm       = algorithmUtil.getAlgorithmForRefreshToken();
            JWTVerifier verifier        = JWT.require(algorithm)
                    .withAudience(authenticationTokenSettings.getAudience()).build();
            DecodedJWT decodedJWT = verifier.verify(refreshToken);
            return AuthenticationTokenDetails.builder()
                    .tokenId(decodedJWT.getId())
                    .userId(decodedJWT.getClaim(authenticationTokenSettings.getUserIdClaimName()).asLong())
                    .uniqueId(String.valueOf(decodedJWT.getClaim(authenticationTokenSettings.getUniqueIdClaimName())))
                    .issuedDate(ZonedDateTime.ofInstant(decodedJWT.getIssuedAtAsInstant(), ZoneId.systemDefault()))
                    .expirationDate(ZonedDateTime.ofInstant(decodedJWT.getExpiresAtAsInstant(), ZoneId.systemDefault()))
                    .build();
        } catch (final Exception e) {
            final String message = "Invalid refresh token";
            log.error(message,e);
            throw new RefreshTokenException(HttpStatus.FORBIDDEN, message, e);
        }
    }
}