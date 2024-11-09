package com.miron.core.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeBalanceEvent implements EventMessage{
    private String authenticatedUsername;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime timestamp;
    private ChangeBalanceStatusEnum status;
}
