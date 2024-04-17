package com.volt_money.volt_money.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Operator {
    private String operatorId;
    private String operatorName;
    private List<Booking> bookings;

}
