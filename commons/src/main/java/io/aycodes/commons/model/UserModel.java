package io.aycodes.persistenceservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.aycodes.commons.enums.AccountState;
import io.aycodes.commons.enums.Roles;
import io.aycodes.commons.validation.constraint.EmailConstraint;
import io.aycodes.commons.validation.constraint.NameConstraint;
import io.aycodes.commons.validation.constraint.PasswordConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("password")
public class UserModel {
    @Id
    @SequenceGenerator(
            name = "user_id_sequence",
            sequenceName = "user_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_sequence"
    )
    private Long            id;
    @NotEmpty
    @Column(nullable = false, unique = true)
    private String          uniqueId;
    @NotNull
    @NotEmpty(message = "Please provide firstname")
    @NameConstraint
    private String          firstName;
    @NotNull
    @NotEmpty(message = "Please provide lastname")
    @NameConstraint
    private String          lastName;
    @Column(unique = true)
    @NotNull
    @NotEmpty(message = "Username cannot be empty")
    @EmailConstraint(message = "Please enter a valid email")
    private String          email;
    @NotNull
    @NotEmpty
    @PasswordConstraint(message = "Password must contain at least 1 uppercase, 1 lowercase and special character")
    private String          password;
    private LocalDateTime   dateRegistered;
    private LocalDateTime   lastModified;
    private LocalDateTime   passwordUpdateDate;
    private String          imageUrl;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean         isActive;
    @Enumerated(EnumType.STRING)
    private AccountState    accountState;
    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<Roles> roles = new ArrayList<>();

}
