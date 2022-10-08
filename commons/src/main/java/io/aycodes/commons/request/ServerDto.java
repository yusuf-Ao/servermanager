package io.aycodes.serverservice.dto;

import io.aycodes.serverservice.constant.ServerStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class ServerDto {
    @NotBlank(message = "IPAddress cannot be blank")
    @NotEmpty(message = "IPAddress cannot be empty")
    private String              ipAddress;
    private String              serverName;
    private String              serverType;
    private String              memory;
    private String              hostName;
    private ServerStatus        status;
}
