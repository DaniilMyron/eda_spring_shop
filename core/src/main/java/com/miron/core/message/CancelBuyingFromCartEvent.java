package com.miron.core.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelBuyingFromCartEvent implements EventMessage{
    private Map<Integer, Integer> json;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime timestamp;
}
