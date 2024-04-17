package com.volt_money.volt_money.Service;

import com.volt_money.volt_money.Dao.BookingDao;
import com.volt_money.volt_money.Model.ServiceOperator;
import com.volt_money.volt_money.Model.Booking;
import com.volt_money.volt_money.Model.BookingRequest;
import com.volt_money.volt_money.Model.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BookingServiceImplementation implements BookingService {
    @Autowired
    private BookingDao schedulerDao;


    // Retrieves all bookings.
    @Override
    public List<Booking> getAllBookings() {
        return schedulerDao.findAll();
    }

    private String generateShortBookingId() {
        String timestamp = String.valueOf(Instant.now().toEpochMilli()); // Current timestamp
        String randomNumber = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000)); // Random 4-digit number
        return timestamp.substring(7) + randomNumber; // Combine timestamp and random number
    }

    // Books a slot based on the provided booking request.
    @Override
    public Booking bookSlot(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        //booking.setBookingId(UUID.randomUUID().toString());
        String bookingId = generateShortBookingId();
        booking.setBookingId(bookingId);
        booking.setCustomerId(bookingRequest.getCustomerId());
        List<Booking> bookings =  schedulerDao.findAll();
        if (CollectionUtils.isEmpty(bookings)) {
            booking.setOperatorId("1");
            booking.setTime(bookingRequest.getTime());
            schedulerDao.save(booking);
            return booking;
        }
        int availableOperator = getAvailableOperator(bookings, bookingRequest.getTime());
        if(availableOperator < 3) {
            Operator operator = ServiceOperator.getOperators().get(availableOperator);
            booking.setOperatorId(operator.getOperatorId());
            booking.setTime(bookingRequest.getTime());
            schedulerDao.save(booking);
            return booking;
        }

        throw new RuntimeException("No available operator for the given time");
    }



    // Retrieves available slots.
    @Override
    public List<String> getAvailableSlots() {
        List<Booking> bookings =  schedulerDao.findAll();
        int[] myArray = new int[25];
        for (Booking booking : bookings) {
            myArray[booking.getTime()] = myArray[booking.getTime()] + 1;
        }
        List<String> availableSlots = new ArrayList<>();
        int start =-1;

        for (int i = 0; i < myArray.length; i++) {
            if(myArray[i] < 3) {
                if(start == -1) {
                    start = i;
                }
                if (i == myArray.length -1) {
                    availableSlots.add(start + " - " + i);
                }
            } else {
                if(start != -1 || i == myArray.length -1) {

                    availableSlots.add(start + " - " + i);
                    start = -1;
                }
            }

        }
        return availableSlots;


    }

    // Retrieves all bookings for a specific operator.
    @Override
    public List<Booking> getAllBookingsByOperatorId(String operatorId) {
        List<Booking> bookings =  schedulerDao.findAll();
        List<Booking> operatorBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if(booking.getOperatorId().equals(operatorId)) {
                operatorBookings.add(booking);
            }
        }
        return operatorBookings;

    }

    // Reschedules a booking based on the provided booking request.
    @Override
    public Booking rescheduleBooking(BookingRequest bookingRequest) {
        if (!StringUtils.isEmpty(bookingRequest.getBookingId())) {
            Optional<Booking> optionalBooking = schedulerDao.findById(bookingRequest.getBookingId());
            if (optionalBooking.isPresent()) {
                Booking booking = optionalBooking.get();
                booking.setTime(bookingRequest.getTime());
                schedulerDao.save(booking);
                return booking; // Return the updated booking
            } else {
                throw new RuntimeException("Booking not found");
            }
        }
        throw new IllegalArgumentException("Invalid booking ID");
    }


    // Deletes a booking based on the provided booking ID.
    @Override
    public void deleteBooking(String id) {
        try {
            schedulerDao.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Appointment not found");
        }
    }

    // Calculates the number of available operators for a given time.
    private int getAvailableOperator(List<Booking> bookings, int time) {
        int count = 0;
        for (Booking booking : bookings) {
            if(time == booking.getTime()) {
                count++;

            }

        }
        return count;
    }
}
