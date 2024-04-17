package com.volt_money.volt_money.Controller;

import com.volt_money.volt_money.Model.Booking;
import com.volt_money.volt_money.Model.BookingRequest;
import com.volt_money.volt_money.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController   //provides RESTful API
public class BookingController {

    @Autowired
    private BookingService bookingService;

    private void validateAppointmentRequest(BookingRequest bookingRequest) {
        if(bookingRequest == null || StringUtils.isEmpty(bookingRequest.getCustomerId()) || bookingRequest.getTime() < 0 || bookingRequest.getTime() > 23) {
            throw new IllegalArgumentException("Invalid appointment request");
        }
    }

    // get all bookings.
    @GetMapping(value = "/getAllBookings")
    public List<Booking> getAllBookings(@RequestParam(required = false) String operatorId) {
        if(StringUtils.hasText(operatorId)) {
            return bookingService.getAllBookingsByOperatorId(operatorId);
        }
        return bookingService.getAllBookings();
    }

    // book a slot.
    @PostMapping(value = "/bookSlot")
    public Booking bookSlot(@RequestBody BookingRequest bookingRequest) {


        validateAppointmentRequest(bookingRequest);

        return bookingService.bookSlot(bookingRequest);
    }


    // get available slots.
    @GetMapping(value = "/getAvailableSlots")
    public List<String> getAvailableSlots() {
        return bookingService.getAvailableSlots();
    }

    // reschedule a booking.
    @PutMapping(value = "/rescheduleBooking")
    public Booking rescheduleBooking(@RequestBody BookingRequest bookingRequest) {
        validateAppointmentRequest(bookingRequest);
        return bookingService.rescheduleBooking(bookingRequest);
    }

    // delete a booking by ID.
    @DeleteMapping(value = "/deleteBooking/{id}")
    public void deleteBooking(@PathVariable String id) {
        bookingService.deleteBooking(id);
    }

}
