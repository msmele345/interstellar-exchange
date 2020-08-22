package com.mitchmele.interstellarexchange.helpers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradesForDatesRequest {

    private String startDate;
    private String endDate;
}
