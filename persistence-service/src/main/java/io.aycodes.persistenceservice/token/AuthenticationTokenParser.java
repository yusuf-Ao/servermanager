package io.aycodes.apigateway.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.aycodes.apigateway.exception.AccessTokenException;
import io.aycodes.apigateway.exception.RefreshTokenException;
import io.aycodes.apigateway.util.AlgorithmUtil;
import io.aycodes.commons.enums.Roles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.time.ZoneId;
import java.time.ZonedDateTime;
@Slf4j
@RequiredArgsConstructor
public class AuthenticationTokenParser {

    @Autowired
    private AlgorithmUtil algorithmUtil;

    private DecodedJWT decodeAndVerifyJwtToken(final String token) throws AccessTokenException {
        try {
            Algorithm   algorithm       = algorithmUtil.getAlgorithmForAccessToken();
            JWTVerifier verifier        = JWT.require(algorithm)
                    .withAudience(AuthenticationTokenSettings.getAudience()).build();
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
        return decodeAndVerifyJwtToken(token).getClaim(AuthenticationTokenSettings.getUserIdClaimName()).asLong();
    }

    private String extractUserUniqueIdFromClaims(@NotNull final String token) {
        return String.valueOf(decodeAndVerifyJwtToken(token).getClaim(AuthenticationTokenSettings.getUniqueIdClaimName()));
    }

    private Roles extractUserRoleFromClaims(@NotNull final String token) {
        return Roles.valueOf(String.valueOf(decodeAndVerifyJwtToken(token).getClaim(AuthenticationTokenSettings.getAuthorityClaimName())));
    }

    public AuthenticationTokenDetails parseAccessToken(final String token) throws AccessTokenException {
        return AuthenticationTokenDetails.builder()
                .tokenId(extractTokenIdFromClaims(token))
                .userId(extractUserIdFromClaims(token))
                .uniqueId(extractUserUniqueIdFromClaims(token))
                .issuedDate(extractIssuedDateFromClaims(token))
                .expirationDate(extractExpirationDateFromClaims(token))
                .userRoles(extractUserRoleFromClaims(token))
                .build();
    }

    public AuthenticationTokenDetails parseRefreshToken(final String refreshToken) throws RefreshTokenException {
        try {
            Algorithm   algorithm       = algorithmUtil.getAlgorithmForRefreshToken();
            JWTVerifier verifier        = JWT.require(algorithm)
                    .withAudience(AuthenticationTokenSettings.getAudience()).build();
            DecodedJWT decodedJWT = verifier.verify(refreshToken);
            return AuthenticationTokenDetails.builder()
                    .tokenId(decodedJWT.getId())
                    .userId(decodedJWT.getClaim(AuthenticationTokenSettings.getUserIdClaimName()).asLong())
                    .uniqueId(String.valueOf(decodedJWT.getClaim(AuthenticationTokenSettings.getUniqueIdClaimName())))
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