package io.aycodes.commons.clients.persistenceservice;


import io.aycodes.commons.request.UserCreationDto;
import io.aycodes.commons.request.UserUpdateDto;
import io.aycodes.commons.response.CustomResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@FeignClient(
        value = "persistence-service",
        path = "/api/v1/user"
)
public interface UserGateway {

    @GetMapping("/health-check")
    CustomResponse healthCheck();

    @PostMapping("/register")
    CustomResponse addUser(@RequestBody @Valid final UserCreationDto userCreationDto);

    @GetMapping("/{userId}")
    CustomResponse getServer(@NotNull @PathVariable("userId") final Long userId);

    @PutMapping("/modify/{userId}")
    CustomResponse updateUser(@RequestBody final UserUpdateDto userUpdateDto,
                              @NotNull @PathVariable("userId") final Long userId);

    @DeleteMapping("/delete/{userId}")
    CustomResponse deleteUser(@NotNull @PathVariable("userId") final Long userId);

    @GetMapping("/fetch")
    CustomResponse getAllUsers();

}
