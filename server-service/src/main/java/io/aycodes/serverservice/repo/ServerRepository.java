package io.aycodes.persistenceservice.repo;

import io.aycodes.persistenceservice.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {

    Optional<Server> findByIpAddress(String ipAddress);
}
