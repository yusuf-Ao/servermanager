package io.aycodes.persistenceservice.dto;

import io.aycodes.commons.validation.constraint.EmailConstraint;
import io.aycodes.commons.validation.constraint.NameConstraint;
import io.aycodes.commons.validation.constraint.PasswordConstraint;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@RequiredArgsConstructor
public class UserUpdateDto {

    @NotNull
    @NotEmpty(message = "Please provide firstname")
    @NameConstraint
    private String          firstName;
    @NotNull
    @NotEmpty(message = "Please provide lastname")
    @NameConstraint
    private String          lastName;
    @NotNull
    @NotEmpty
    @PasswordConstraint(message = "Password must contain at least 1 uppercase, 1 lowercase and special character")
    private String          password;
    private String          imageUrl;
}
