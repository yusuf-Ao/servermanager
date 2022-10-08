package io.aycodes.apigateway.util;

import com.auth0.jwt.algorithms.Algorithm;
import io.aycodes.apigateway.service.AuthenticationTokenSettings;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlgorithmUtil {

    public Algorithm getAlgorithmForAccessToken() {
        return Algorithm.HMAC512(AuthenticationTokenSettings.getSecretKey());
    }
    public Algorithm getAlgorithmForRefreshToken() {
        return Algorithm.HMAC256(AuthenticationTokenSettings.getSecretKey());
    }
}
