package com.miron.core.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miron.core.models.PublishedProduct;
import com.miron.core.models.PublishedUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisteredEvent implements EventMessage{
    private PublishedUser publishedUser;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime timestamp;
}
