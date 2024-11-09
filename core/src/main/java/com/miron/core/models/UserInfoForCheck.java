package com.miron.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoForCheck {
    //MIGHT BE USER REAL NAME, ADDRESS, ETC.
    private int payingSum;
    private String authenticatedUsername;
}
