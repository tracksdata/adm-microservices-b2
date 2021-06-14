package com.cts.pss.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.pss.dao.BookingRepository;
import com.cts.pss.dao.CoPassengerRepository;
import com.cts.pss.entity.BookingRecord;
import com.cts.pss.entity.CoPassenger;
import com.cts.pss.entity.Flight;
import com.cts.pss.entity.Passenger;
import com.cts.pss.model.SearchQuery;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BookingRepository bookingDao;
	@Autowired
	private CoPassengerRepository coPassengerDao;
	@Autowired
	private FlightService flightService;

	@Override
	public BookingRecord bookFlight(SearchQuery query) {

		BookingRecord bookingRecord = null;

		Flight flight = flightService.findFlightByFlightNumberAndOriginAndDestinationAndFlightDate(
				query.getFlightNumber(), query.getOrigin(), query.getDestination(), query.getFlightDate());

		if (flight.getInventory().getCount() < query.getTravellers()) {
			System.out.println(">>>>>> No more Seats Avaiable for Book <<<<<<");
			return null;
		}

		if (flight != null) {
			bookingRecord = new BookingRecord(LocalDateTime.now(), flight.getFlightDate(), flight.getFlightTime(),
					flight.getFlightNumber(), flight.getOrigin(), flight.getDestination(), "CONFIRMED",
					query.getTravellers(), query.getPassenger(), flight.getFlightInfo());

			bookingRecord.setFare(flight.getFare().getFare() * query.getTravellers());

			if (query.getPassenger().getCoPassengers().size() == query.getTravellers() - 1) {
				// Update Inventory
				flight.getInventory().setCount(flight.getInventory().getCount() - query.getTravellers());
				flightService.scheduleFlight(flight);
				bookingDao.save(bookingRecord);
			} else {
				System.out.println(">>>>> Passenger count Wrongly Provided. Booking not Done...");
			}

		}

		return bookingRecord;
	}

	@Override
	public BookingRecord getBookingData(int id) {
		return bookingDao.findById(id).orElse(null);
	}

	// Cancel All the passengers
	@Override
	public void cancelBooking(int bookingId) {
		bookingDao.deleteById(bookingId);
	}

	// Cancel booking for selected CO-Passengers only
	@Override
	public BookingRecord customCancelBooking(int bookingId, List<CoPassenger> coPassengers) {
		// against to whose booking id CO-Passengers  have to be deleted?
		BookingRecord bookingData = getBookingData(bookingId);
		
		Flight flight = flightService.findFlightByFlightNumberAndOriginAndDestinationAndFlightDate(
				bookingData.getFlightNumber(), bookingData.getOrigin(), bookingData.getDestination(),
				bookingData.getFlightDate());
		

		// Get All CO-Passengers to be deleted from UI
		//Remove CO-Passenger in copassengers table and co_passenger table separately using native sql query
		for (CoPassenger cp : coPassengers) {
			coPassengerDao.deleteCopassengersById(cp.getCopassengerId());
			coPassengerDao.deleteCopassenger(cp.getCopassengerId());
		}
		
		Passenger passenger=bookingData.getPassenger();
		
		// copy only copassengerIds from coPassengers collection who suppose to be deleted
		Set<Integer> ids = coPassengers.stream()
		        .map(CoPassenger::getCopassengerId)
		        .collect(Collectors.toSet());
		
		List<CoPassenger> updatedCoPassengers = passenger.getCoPassengers().stream()
		        .filter(cp -> !ids.contains(cp.getCopassengerId()))
		        .collect(Collectors.toList());
		
		passenger.setCoPassengers(updatedCoPassengers);
		
		// Update number of Travellers after deleting the CO-Passengers
		bookingData.setTravellers(bookingData.getTravellers()- coPassengers.size());
		bookingData.setPassenger(passenger);
		//Update Deleted Booking to the flight Inventory
		flight.getInventory().setCount(flight.getInventory().getCount()+coPassengers.size());
		
		bookingDao.save(bookingData);
		flightService.scheduleFlight(flight);
		
		return bookingData;
	}

	// Reschedule Booked Flight
	@Override
	public BookingRecord rescheduleBooking(int bookingId, int flightId) {
		BookingRecord bookingRecord = getBookingData(bookingId);
		Flight flight = flightService.findFlightByFlightNumberAndOriginAndDestinationAndFlightDate(
				bookingRecord.getFlightNumber(), bookingRecord.getOrigin(), bookingRecord.getDestination(),
				bookingRecord.getFlightDate());

		// Restore Inventory booked travellers count
		flight.getInventory().setCount(flight.getInventory().getCount() + bookingRecord.getTravellers());

		// Get New Selected Flight Information
		Flight newFlight = flightService.findFlightById(flightId);

		// New Flight Booking Information
		bookingRecord.setFlightNumber(newFlight.getFlightNumber());
		bookingRecord.setOrigin(newFlight.getOrigin());
		bookingRecord.setDestination(newFlight.getDestination());
		bookingRecord.setFlightDate(newFlight.getFlightDate());
		bookingRecord.setFlightTime(newFlight.getFlightTime());
		bookingRecord.setFlightInfo(newFlight.getFlightInfo());
		bookingRecord.setFare(newFlight.getFare().getFare());

		bookingDao.save(bookingRecord);
		// Update Inventory for newly selected flight
		newFlight.getInventory().setCount(newFlight.getInventory().getCount() - bookingRecord.getTravellers());
		flightService.scheduleFlight(newFlight);

		return bookingRecord;
	}

}
