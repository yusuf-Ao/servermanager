package io.aycodes.persistenceservice.token.data;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@RequiredArgsConstructor
@SuperBuilder
@Entity
public class TokenStore {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(
            name = "token_id_sequence",
            sequenceName = "token_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "token_id_sequence"
    )
    private Long                    id;
    private String                  token;
}
