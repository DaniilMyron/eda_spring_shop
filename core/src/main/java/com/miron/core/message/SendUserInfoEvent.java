package com.miron.core.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miron.core.models.UserInfoForCheck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendUserInfoEvent implements EventMessage{
    private UserInfoForCheck userInfo;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime timestamp;
    private SendUserInfoStatusEnum status;
}
