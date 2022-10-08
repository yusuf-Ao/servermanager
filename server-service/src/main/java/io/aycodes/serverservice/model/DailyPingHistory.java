package io.aycodes.serverservice.model;

import io.aycodes.commons.enums.ServerStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@SuperBuilder
@Entity
public class ServerPingHistory implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(
            name = "pinghistory_id_sequence",
            sequenceName = "pinghistory_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "pinghistory_id_sequence"
    )
    private Long                    id;
    private LocalDateTime           pingTimeStamp;
    @Enumerated(EnumType.STRING)
    private ServerStatus            status;

}
