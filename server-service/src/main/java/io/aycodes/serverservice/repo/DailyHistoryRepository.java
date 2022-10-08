package io.aycodes.serverservice.repo;

import io.aycodes.serverservice.model.DailyPingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PingHistoryRepository extends JpaRepository<DailyPingHistory, Long> {
}
