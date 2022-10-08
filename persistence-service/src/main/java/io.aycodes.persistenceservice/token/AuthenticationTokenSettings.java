package io.aycodes.apigateway.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@NoArgsConstructor
@Configuration
public class AuthenticationTokenSettings {

    protected static final String ALIAS    = "spring";
    private static final String   PASSWORD = "secret";

    @Value("${security.jwt.issuer}")
    private static String      issuer;
    @Value("${security.jwt.audience}")
    private static String     audience;
    @Value("${security.jwt.secret-key}")
    private static String     secretKey;
    @Value("${security.jwt.refresh-token.validFor-seconds}")
    private static Long      refreshTokenExpiration;
    @Value("${security.jwt.access-token.validFor-seconds}")
    private static Long      accessTokenExpiration;
    @Value("${security.jwt.claim-names.authority}")
    private static String      authorityClaimName;
    @Value("${security.jwt.claim-names.userId}")
    private static String      userIdClaimName;


    @Value("${security.jwt.claim-names.uniqueId}")
    private static String      uniqueIdClaimName;
    @Value("${security.jwt.claim-names.subject}")
    private static String      subjectClaimName;


    public static String getSecretKey(){
        //todo: to make this external and encrypt
        return secretKey;
    }

    public static String getIssuer() {
        return issuer;
    }

    public static String getAudience() {
        return audience;
    }

    public static Long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public static Long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public static String getAuthorityClaimName() {
        return authorityClaimName;
    }

    public static String getUserIdClaimName() {
        return userIdClaimName;
    }

    public static String getSubjectClaimName() {
        return subjectClaimName;
    }

    public static String getUniqueIdClaimName() {
        return uniqueIdClaimName;
    }

}
