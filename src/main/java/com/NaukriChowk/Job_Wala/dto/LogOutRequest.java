package com.NaukriChowk.Job_Wala.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogOutRequest {

    @Valid
    @NotNull(message = "Device info cannot be null")
    private DeviceInfo deviceInfo;

    @Valid
    @NotNull(message = "Existing Token needs to be passed")
    private String token;
}
