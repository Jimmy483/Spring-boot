package com.gmi.learn.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Food {

//    used Entity cause new interface foodRepository extends PagingAndSortingRepository which is part of JPA and thus spring tries to process FoodRepository
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;

    long price;

    String image;

    @Column(name="lastUpdated")
    String lastUpdated;
}
