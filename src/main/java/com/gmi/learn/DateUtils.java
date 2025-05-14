package com.gmi.learn;

import java.time.LocalDate;
import java.time.Period;

public class DateUtils {

    public static Period getDateDifference(LocalDate startDate, LocalDate endDate){
        return Period.between(startDate, endDate);
    }
}
