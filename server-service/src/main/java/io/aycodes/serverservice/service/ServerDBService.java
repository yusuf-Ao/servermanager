package io.aycodes.persistenceservice.dbservice;

import io.aycodes.persistenceservice.dto.ServerDto;
import io.aycodes.commons.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.aycodes.persistenceservice.model.Server;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import io.aycodes.persistenceservice.repo.ServerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component("serverdbservice")
@Scope("singleton")
@RequiredArgsConstructor
@Slf4j
public class ServerDBService {

    @Autowired
    private ServerRepository            serverRepository;

    //todo: get servers by pagination and sorting

    public boolean serverExists(final String ipAddress) {
        log.info("Searching server by ipAddress: {}", ipAddress);
        return serverRepository.findByIpAddress(ipAddress).isPresent();
    }

    public Server addServer(final ServerDto serverDto) throws CustomException {
        final String ipAddress = serverDto.getIpAddress();
        log.info("Adding new server: {}", ipAddress);
        if (serverExists(ipAddress)) {
            final String message = "Server with the IPAddress: " + ipAddress +" already exists.";
            log.error(message);
            throw new CustomException(HttpStatus.NOT_MODIFIED, message);
        }
        Server server = new Server();
        final LocalDateTime dateTime = LocalDateTime.now();
        BeanUtils.copyProperties(serverDto, server);
        server.setDateCreated(dateTime);
        server.setDateModified(dateTime);
        serverRepository.save(server);
        log.info("server added successfully");
        return server;
    }

    public List<Server> getAllServers() {
        log.info("Fetching all servers");
        return serverRepository.findAll();
    }

    public boolean deleteServer(final Long id) {
        log.info("Deleting Server with id: {} ", id);
        serverRepository.deleteById(id);
        return true;
    }

    public boolean deleteSelectedServers(final List<Long> ids) {
        log.info("Deleting selected server");
        serverRepository.deleteAllByIdInBatch(ids);
        return true;
    }

    public Optional<Server> getServer(final Long id) {
        log.info("Fetching server with id: {}", id);
        return serverRepository.findById(id);
    }

    public Optional<Server> updateServer(final ServerDto serverDto, final Long id) throws CustomException {
        log.info("Updating server with id: {}", id);
        Optional<Server> server = serverRepository.findById(id);
        if (server.isEmpty()) {
            final String message = "Server does not exists in the database.";
            log.error(message);
            throw new CustomException(HttpStatus.NOT_MODIFIED, message);
        }
        BeanUtils.copyProperties(serverDto, server);
        server.get().setDateModified(LocalDateTime.now());
        log.info("server updated successfully: {}", id);
        return server;
    }


}
