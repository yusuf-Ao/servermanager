package io.aycodes.apigateway.service;

import com.auth0.jwt.JWT;
import io.aycodes.apigateway.util.AlgorithmUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
@RequiredArgsConstructor
public class AuthenticationTokenIssuer {
    @Autowired
    private AlgorithmUtil   algorithmUtil;

    public String issueAccessToken(final AuthenticationTokenDetails authenticationTokenDetails) {

        return JWT.create()
                .withJWTId(authenticationTokenDetails.getTokenId())
                .withIssuer(AuthenticationTokenSettings.getIssuer())
                .withAudience(AuthenticationTokenSettings.getAudience())
                .withSubject(String.valueOf(authenticationTokenDetails.getUserId()))
                .withIssuedAt(Date.from(authenticationTokenDetails.getIssuedDate().toInstant()))
                .withExpiresAt(Date.from(authenticationTokenDetails.getExpirationDate().toInstant()))
                .withClaim(AuthenticationTokenSettings.getUserIdClaimName(), authenticationTokenDetails.getUserId())
                .withClaim(AuthenticationTokenSettings.getAuthorityClaimName(), authenticationTokenDetails.getUserRoles().ordinal())
                .withClaim(AuthenticationTokenSettings.getSubjectClaimName(), AuthenticationTokenSettings.ALIAS)
                .sign(algorithmUtil.getAlgorithmForAccessToken());

    }

    public String issueRefreshToken(final AuthenticationTokenDetails authenticationTokenDetails) {
        return JWT.create()
                .withJWTId(authenticationTokenDetails.getTokenId())
                .withIssuer(AuthenticationTokenSettings.getIssuer())
                .withAudience(AuthenticationTokenSettings.getAudience())
                .withIssuedAt(Date.from(authenticationTokenDetails.getIssuedDate().toInstant()))
                .withExpiresAt(Date.from(authenticationTokenDetails.getExpirationDate().toInstant()))
                .withClaim(AuthenticationTokenSettings.getUserIdClaimName(), authenticationTokenDetails.getUserId())
                .sign(algorithmUtil.getAlgorithmForRefreshToken());
    }
}

