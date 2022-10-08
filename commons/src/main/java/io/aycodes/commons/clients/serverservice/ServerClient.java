package io.aycodes.commons.clients.serverservice;


import io.aycodes.commons.request.ServerDto;
import io.aycodes.commons.response.CustomResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(
        value = "server-service",
        path = "/api/v1/server"
)
public interface ServerGateway {

    @GetMapping("/health-check")
    CustomResponse healthCheck();

    @PostMapping("/save")
    CustomResponse addServer(@RequestBody @Valid final ServerDto serverDto);

    @GetMapping("/{serverId}")
    CustomResponse getServer(@NotNull @PathVariable("serverId") final Long serverId);

    @PutMapping("/modify/{serverId}")
    CustomResponse updateServer(@RequestBody final ServerDto serverDto,
                                @NotNull @PathVariable("serverId") final Long serverId);

    @DeleteMapping("/delete/{serverId}")
    CustomResponse deleteServer(@NotNull @PathVariable("serverId") final Long serverId);

    @DeleteMapping("/batch-delete")
    CustomResponse batchDeleteServers(@RequestBody final List<Long> serverIdList);

    @GetMapping("/ping")
    CustomResponse pingServer(@NotNull @RequestParam("ipAddress") final String ipAddress);

    @GetMapping("/fetch")
    CustomResponse getAllServers();

}
