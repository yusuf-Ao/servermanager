package io.aycodes.apigateway.service;

import io.aycodes.apigateway.exception.AccessTokenException;
import io.aycodes.apigateway.exception.GatewayException;
import io.aycodes.apigateway.exception.RefreshTokenException;
import io.aycodes.commons.response.JwtResponse;
import io.aycodes.commons.enums.Roles;
import io.aycodes.commons.request.UserDetailDto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Slf4j
@NoArgsConstructor
@RequiredArgsConstructor
public class AuthenticationTokenService {
    @Autowired
    private AuthenticationTokenIssuer       authenticationTokenIssuer;
    private String                          tokenId;
    private Long                            userId;
    private String                          uniqueId;
    private Set<Roles>                      userRoles;
    private ZonedDateTime                   issuedDate;
    private AuthenticationTokenDetails      refreshAuthenticationDetails;


    private ZonedDateTime calculateAccessExpirationDate(final ZonedDateTime issuedDate) {
        return issuedDate.plusSeconds(AuthenticationTokenSettings.getAccessTokenExpiration());
    }

    private ZonedDateTime calculateRefreshExpirationDate(final ZonedDateTime issuedDate) {
        return issuedDate.plusSeconds(AuthenticationTokenSettings.getRefreshTokenExpiration());
    }

    private String generateTokenIdentifier() {
        return UUID.randomUUID().toString();
    }

    public JwtResponse issueToken(final UserDetailDto userDetailDto) {

        tokenId    = generateTokenIdentifier();
        userId     = userDetailDto.getId();
        uniqueId   = userDetailDto.getUniqueId();
        userRoles = userDetailDto.getRoles();
        issuedDate = ZonedDateTime.now();

        final ZonedDateTime accessExpirationDate  = calculateAccessExpirationDate(issuedDate);
        final ZonedDateTime refreshExpirationDate = calculateRefreshExpirationDate(issuedDate);

        return JwtResponse.builder()
                .refreshToken(authenticationTokenIssuer.issueRefreshToken(
                        AuthenticationTokenDetails.builder().tokenId(tokenId)
                                .userId(userId).uniqueId(uniqueId)
                                .userRoles(userRoles).issuedDate(issuedDate)
                                .expirationDate(refreshExpirationDate).build()))
                .accessToken(authenticationTokenIssuer.issueAccessToken(
                        AuthenticationTokenDetails.builder().tokenId(tokenId)
                        .userId(userId).uniqueId(uniqueId)
                        .userRoles(userRoles).issuedDate(issuedDate)
                        .expirationDate(accessExpirationDate).build()))
                .build();

    }

    public AuthenticationTokenDetails parseAccessToken(final String authenticationToken) {
        return new AuthenticationTokenParser().parseAccessToken(authenticationToken);
    }

    public AuthenticationTokenDetails parseRefreshToken(final String authenticationToken) {
        return new AuthenticationTokenParser().parseRefreshToken(authenticationToken);
    }

    public String refreshToken(final String refreshToken, final String currentAuthenticationToken){
        issuedDate = ZonedDateTime.now();
        final ZonedDateTime expirationDate = calculateAccessExpirationDate(issuedDate);
        boolean             tokenExpired   = false;

        try {
            parseAccessToken(currentAuthenticationToken);
        } catch(final AccessTokenException e) {
            tokenExpired = true;
        }

        if(!tokenExpired) {
            throw new GatewayException(HttpStatus.NOT_ACCEPTABLE,"Token has not expired.");
        }
        try {
            refreshAuthenticationDetails = parseRefreshToken(refreshToken);
        } catch(final RefreshTokenException e) {
            throw new GatewayException(HttpStatus.FORBIDDEN,"Refresh token expired.");
        }

        userId = refreshAuthenticationDetails.getUserId();
        uniqueId = refreshAuthenticationDetails.getUniqueId();
        tokenId  = refreshAuthenticationDetails.getTokenId();
        userRoles = refreshAuthenticationDetails.getUserRoles();
        final AuthenticationTokenDetails newTokenDetails = AuthenticationTokenDetails.builder()
                        .tokenId(tokenId).userId(userId)
                        .issuedDate(issuedDate)
                        .expirationDate(expirationDate)
                        .uniqueId(uniqueId)
                        .userRoles(userRoles).build();

        return new AuthenticationTokenIssuer().issueAccessToken(newTokenDetails);
    }
}
