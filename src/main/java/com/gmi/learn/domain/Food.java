package com.gmi.learn.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Food {

    long id;

    String name;

    long price;

    String image;

    Date lastUpdated;
}
