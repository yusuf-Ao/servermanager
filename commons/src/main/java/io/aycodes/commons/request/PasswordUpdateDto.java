package io.aycodes.commons.request;

import io.aycodes.commons.validation.constraint.PasswordConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PassUpdateDto {
    @PasswordConstraint(message = "Password must contain at least 1 uppercase, 1 lowercase and special character")
    private String          currentPassword;
    @NotNull
    @NotEmpty
    @PasswordConstraint(message = "Password must contain at least 1 uppercase, 1 lowercase and special character")
    private String          newPassword;
}
