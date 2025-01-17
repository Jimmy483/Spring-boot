package com.gmi.learn.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfo {

    private long id;

    private String firstName;

    private String lastName;

    private String username;

//    private String passwd;
}
