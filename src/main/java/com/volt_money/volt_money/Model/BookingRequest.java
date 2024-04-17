package com.volt_money.volt_money.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    private int time;
    private String customerId;
    private String bookingId;
}
