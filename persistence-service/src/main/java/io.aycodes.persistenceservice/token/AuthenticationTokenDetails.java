package io.aycodes.apigateway.service;


import io.aycodes.commons.enums.Roles;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import java.util.Set;

@SuperBuilder
@Data
@RequiredArgsConstructor
public class AuthenticationTokenDetails {

    private final String        tokenId;
    private final Long          userId;
    private final String        uniqueId;
    private final ZonedDateTime issuedDate;
    private final ZonedDateTime expirationDate;
    private final Set<Roles>    userRoles;


}

