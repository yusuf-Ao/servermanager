package service;

import dto.ServerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ServerServiceImpl implements ServerService{





    @Override
    public ServerDto create(ServerDto serverDto) {
        return null;
    }

    @Override
    public List<ServerDto> getAllSever() {
        return null;
    }

    @Override
    public ServerDto update(ServerDto serverDto) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public ServerDto getServer(Long id) {
        return null;
    }

    @Override
    public ServerDto ping(String ipAddress) {
        return null;
    }
}
