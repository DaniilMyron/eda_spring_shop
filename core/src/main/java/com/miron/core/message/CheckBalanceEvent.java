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
public class CheckBalanceEvent implements EventMessage {
    private int productId;
    private String authenticatedUsername;
    private int sum;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime timestamp;
    private CheckBalanceStatusEnum status;
}
