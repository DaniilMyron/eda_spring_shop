package com.miron.core.models;

import lombok.*;

@Data
@Builder
@Setter(AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoForCheck {
    //MIGHT BE USER REAL NAME, ADDRESS, ETC.
    private int payingSum;
    private String authenticatedUsername;
}
