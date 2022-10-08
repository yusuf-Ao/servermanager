package io.aycodes.apigateway.token;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@RequiredArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private String refreshToken;

}
