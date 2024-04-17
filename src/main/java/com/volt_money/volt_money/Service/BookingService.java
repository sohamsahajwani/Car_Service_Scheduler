package com.volt_money.volt_money.Service;

import com.volt_money.volt_money.Model.Booking;
import com.volt_money.volt_money.Model.BookingRequest;

import java.util.List;

public interface BookingService {

    public List<Booking> getAllBookings();

    Booking bookSlot(BookingRequest bookingRequest);

    List<String> getAvailableSlots();

    List<Booking> getAllBookingsByOperatorId(String operatorId);

    Booking rescheduleBooking(BookingRequest bookingRequest);

    void deleteBooking(String id);
}
