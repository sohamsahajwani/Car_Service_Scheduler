package com.volt_money.volt_money.Dao;

import com.volt_money.volt_money.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDao extends JpaRepository<Booking, String> {
}
