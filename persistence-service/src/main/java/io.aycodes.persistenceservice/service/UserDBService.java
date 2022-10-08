package io.aycodes.persistenceservice.service;


import io.aycodes.commons.enums.Roles;
import io.aycodes.commons.exception.CustomException;
import io.aycodes.commons.request.UserNotificationRequest;
import io.aycodes.commons.util.CopyUtils;
import io.aycodes.persistenceservice.dto.UserCreationDto;
import io.aycodes.persistenceservice.model.UserModel;
import io.aycodes.persistenceservice.repo.UserRepository;
import io.aycodes.persistenceservice.util.NotificationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static io.aycodes.commons.enums.AccountState.ACTIVE;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final NotificationUtil  notificationUtil;

    public void saveUser(final UserModel user) {
        userRepository.save(user);
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean userExistsByEmail(final String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public UserModel registerUser(UserCreationDto userCreationDto) throws CustomException {
        Collection<Roles> role = new ArrayList<>();
        role.add(Roles.USER);
        final LocalDateTime dateTime = LocalDateTime.now();
        final String email = userCreationDto.getEmail();
        if (userExistsByEmail(email)) {
            final String message = "User with the email: " + email +" already exists.";
            log.error(message);
            throw new CustomException(HttpStatus.NOT_MODIFIED, message);
        }
        UserModel user = UserModel.builder()
                .firstName(userCreationDto.getFirstName()).lastName(userCreationDto.getLastName())
                .email(email).password(passwordEncoder().encode(userCreationDto.getPassword()))
                .dateRegistered(dateTime).lastModified(dateTime)
                .isActive(true).accountState(ACTIVE)
                .imageUrl("noimagefornow")
                .roles(role)
                .build();
        saveUser(user);
        UserNotificationRequest newUserNotificationRequest = UserNotificationRequest.builder()
                .message("Hi " + user.getFirstName() + " welcome onboard!!!")
                .timeOfEvent(LocalDateTime.now().toString())
                .accountState(ACTIVE.getAccountState())
                .build();

        notificationUtil.publishNotificationToQueue(newUserNotificationRequest);
        return user;
    }

    public List<Server> getAllServers() {
        log.info("Fetching all servers");
        return serverRepository.findAll();
    }

    public void deleteServer(final Long id) throws CustomException {
        log.info("Deleting Server with id: {} ", id);
        if (getServerById(id).isEmpty()) {
            final String message = "Server with id: " + id + " does not exist";
            log.error(message);
            throw new CustomException(HttpStatus.NOT_MODIFIED, message);
        }
        serverRepository.deleteById(id);
    }

    public Optional<Server> getServerById(final Long id) {
        log.info("Fetching server with id: {}", id);
        return serverRepository.findById(id);
    }
    public Optional<Server> getServerByIpAddress(final String ipAddress) {
        log.info("Fetching server with address: {}", ipAddress);
        return serverRepository.findByIpAddress(ipAddress);
    }

    public UserModel updateUserInfo(final UserCreationDto userCreationDto, final Long id) throws CustomException {
        log.info("Updating user with id: {}", id);
        Optional<UserModel> oldUserModel = userRepository.findById(id);
        if (oldUserModel.isEmpty()) {
            final String message = "User does not exists in the database.";
            log.error(message);
            throw new CustomException(HttpStatus.NOT_MODIFIED, message);
        }
        UserModel updatedUserModel = oldUserModel.get();
        CopyUtils.copyProperties(userCreationDto,updatedUserModel);
        updatedUserModel.setLastModified(LocalDateTime.now());
        saveUser(updatedUserModel);
        log.info("User updated successfully: {}", id);
        return updatedUserModel;
    }

}
