package io.aycodes.persistenceservice.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Server {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long                id;
    @Column(unique = true)
    @NotNull
    @NotEmpty(message = "IPAddress cannot be empty")
    private String              ipAddress;
    private String              serverName;
    private String              serverType;
    private LocalDateTime       dateCreated;
    private LocalDateTime       dateModified;
    private Long                memory;
    private String              hostName;
    private String              imageUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Server server = (Server) o;
        return id != null && Objects.equals(id, server.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
