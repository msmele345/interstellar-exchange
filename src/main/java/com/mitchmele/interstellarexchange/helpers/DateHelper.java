package com.mitchmele.interstellarexchange.helpers;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateHelper {

    public static final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

    public Date createDateFromString(String stringDate) throws ParseException {
        return formatter.parse(stringDate);
    }
}
