package com.cts.pss.service;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Service;

import com.cts.pss.controller.Sender;
import com.cts.pss.entity.BookingRecord;
import com.cts.pss.entity.CoPassenger;
import com.cts.pss.entity.Fare;
import com.cts.pss.entity.Flight;
import com.cts.pss.entity.Passenger;
import com.cts.pss.model.CustomMessage;
import com.cts.pss.model.SearchQuery;
import com.cts.pss.repository.BookingRepository;
import com.cts.pss.repository.CoPassengerRepository;

@Service
@EnableFeignClients
public class BookingServiceImpl implements BookingService {

	// Need Price of Flight
	// Need Flight Details which is going to be booked

	//@Autowired
	//private RestTemplate rt;

	@Autowired
	private BookingRepository bookingDao;
	
	@Autowired
	private CoPassengerRepository coPassengerDao;

	@Autowired
	private Sender sender;

	//@Bean
	//public RestTemplate restTemplate() {
	//	return new RestTemplate();
	//}
	
	@Autowired
	private SearchServiceProxy searchService;
	@Autowired
	private FareServiceProxy fareService;

	//private String fareSericeUrl = "http://localhost:8081/api/pss/fare";
	//private String searchServiceUrl = "http://localhost:8082/api/pss/search";
	
	
	

	@Override
	public Object bookFlight(SearchQuery query) {

		Fare fare = fareService.getFare(query.getFlightNumber(), query.getOrigin(), query.getDestination(), query.getFlightDate());
		Flight flight = searchService.findFlight(query.getOrigin(), query.getDestination(), query.getFlightDate(), query.getFlightNumber());
			
	    BookingRecord bookingRecord = null;
		// booking process

		if (flight.getInventory().getAvailableSeats() < query.getTravellers()) {
			System.out.println(">>>>>>>> No Seats Aviable For Booking <<<<<<<");

			return new CustomMessage("error", "No Seats Aviable For Booking");
		}

		if (flight != null) {

			bookingRecord = new BookingRecord(LocalDateTime.now(), flight.getFlightNumber(), flight.getOrigin(),
					flight.getDestination(), flight.getFlightDate(), flight.getFlightTime(), "CONFIRMED",
					query.getTravellers(), flight.getFlightInfo(), query.getPassengers());

			// Calculate Fare based on number of Travellers

			bookingRecord.setFare(flight.getFare().getTicketFare() * query.getTravellers());

			if (query.getPassengers().getCoPassengers().size() == query.getTravellers() - 1) {
				bookingDao.save(bookingRecord);
				System.out.println(">>>>> BOOKING-SERVICE:::: Booking is Completed...");

			} else {
				System.out.println("Invalid Passenger Count::: Bookiong is Not Done");
				return new CustomMessage("error", "Invalid Passenger Count::: Bookiong is Not Done");
			}

		}

		// Send Booking Details to Search-Service
		Map<String, Object> bookingDetails = new HashMap<>();
		bookingDetails.put("ID", flight.getId());
		bookingDetails.put("SEATS_BOOKED", query.getTravellers());

		sender.sendBookingDetails(bookingDetails);

		return bookingRecord;
	}
	
	
	@Override
	public Object bookFlightV1(int flightId,int travellers,Passenger passenger) {
		System.out.println(">>>>>>>>>>> Service ");

		Fare fare = fareService.getFareByFlightId(flightId);
		Flight flight = searchService.getFlightById(flightId);
		BookingRecord bookingRecord = null;
		// booking process

		if (flight.getInventory().getAvailableSeats() < travellers) {
			System.out.println(">>>>>>>> No Seats Aviable For Booking <<<<<<<");

			return new CustomMessage("error", "No Seats Aviable For Booking");
		}

		if (flight != null) {

			bookingRecord = new BookingRecord(LocalDateTime.now(), flight.getFlightNumber(), flight.getOrigin(),
					flight.getDestination(), flight.getFlightDate(), flight.getFlightTime(), "CONFIRMED",
					travellers, flight.getFlightInfo(), passenger);

			// Calculate Fare based on number of Travellers

			bookingRecord.setFare(flight.getFare().getTicketFare() * travellers);

			if (passenger.getCoPassengers().size() == travellers - 1) {
				bookingDao.save(bookingRecord);
				System.out.println(">>>>> BOOKING-SERVICE:::: Booking is Completed...");

			} else {
				System.out.println("Invalid Passenger Count::: Bookiong is Not Done");
				return new CustomMessage("error", "Invalid Passenger Count::: Bookiong is Not Done");
			}

		}

		// Send Booking Details to Search-Service
		Map<String, Object> bookingDetails = new HashMap<>();
		bookingDetails.put("ID", flight.getId());
		bookingDetails.put("SEATS_BOOKED", travellers);

		sender.sendBookingDetails(bookingDetails);

		return bookingRecord;
	}

	// delete booking by booking ID

	@Override
	public boolean deleteBookingById(int bookingId) {
		if (bookingDao.existsById(bookingId)) {
			bookingDao.deleteById(bookingId);
			return true;
		}
		return false;
	}
	
	
	//cancel booking for selected passengers only
	@Override
	public BookingRecord customCancelBooking(int booingId,List<CoPassenger> coPassengers) {
		
		//against to whose booking id Co-Passengers have to delete ?
		
		BookingRecord bookingRecord = getBookingDetails(booingId); 
		
		System.out.println(bookingRecord);
		
		Flight flight = searchService.findFlight(bookingRecord.getOrigin(), bookingRecord.getDestination(), bookingRecord.getFlightDate(), bookingRecord.getFlightNumber());
						
		// get all co-passengers ids suppose to be deleted from UI as Collection
		for(CoPassenger cp:coPassengers) {
			coPassengerDao.deleteCopassengersById(cp.getCopassengerId());
			coPassengerDao.deleteCopassenger(cp.getCopassengerId());			
		}
		
		Passenger passenger = bookingRecord.getPassenger();
		
		// copy only copassengerIds from copassenger collection
		
		Set<Integer> ids = coPassengers.stream().map(CoPassenger::getCopassengerId).collect(Collectors.toSet());
		
		List<CoPassenger> updatedPassengers = passenger.getCoPassengers().stream().filter(cp->!ids.contains(cp.getCopassengerId())).collect(Collectors.toList());
		
		passenger.setCoPassengers(updatedPassengers);
		
		//update number of Travellers after deleting the Copassengers
		bookingRecord.setTravellers(bookingRecord.getTravellers()-coPassengers.size());
		bookingRecord.setPassenger(passenger);
		
		
		// Send New Flight Details to Search-Service
				Map<String, Object> newFlightDetails = new HashMap<>();
				newFlightDetails.put("ID", flight.getId());
				newFlightDetails.put("SEATS_UPDATED", coPassengers.size());
				sender.rescheduleFlightInformation(newFlightDetails); // adding

				
		
		//flight.getInventory().setAvailableSeats(flight.getInventory().getAvailableSeats()+coPassengers.size());
		
		bookingDao.save(bookingRecord);
		searchService.scheduleNewFlight(flight);
		
		return bookingRecord;
	}
	
	

	// get Booking Data by ID

	@Override
	public BookingRecord getBookingDetails(int bookingId) {
		return bookingDao.findById(bookingId).orElse(null);
	}

	// Reschedule flight

	@Override
	public BookingRecord rescheduleBooking(int bookingId, int flightId) {

		BookingRecord bookingRecord = getBookingDetails(bookingId);

		Flight flight = searchService.findFlight(bookingRecord.getOrigin(), bookingRecord.getDestination(), bookingRecord.getFlightDate(), bookingRecord.getFlightNumber());
				
				

		// get new selected flight information

		Flight newFlight = searchService.getFlightById(flightId);

		// Update Changed Flight Information in Booking_Record table
		bookingRecord.setBookingId(bookingId);
		bookingRecord.setFlightNumber(newFlight.getFlightNumber());
		bookingRecord.setOrigin(newFlight.getOrigin());
		bookingRecord.setDestination(newFlight.getDestination());
		bookingRecord.setFlightDate(newFlight.getFlightDate());
		bookingRecord.setFlightTime(newFlight.getFlightTime());
		bookingRecord.setFlightInfo(newFlight.getFlightInfo());
		bookingRecord.setFare(newFlight.getFare().getTicketFare() * bookingRecord.getTravellers());

		bookingDao.save(bookingRecord);

		// Update new flight booking seats information in Inventory

		// Send Old Flight Details to Search-Service
		Map<String, Object> oldFlightDetails = new HashMap<>();
		oldFlightDetails.put("ID", flight.getId());
		oldFlightDetails.put("SEATS_UPDATED", bookingRecord.getTravellers());

		// Send New Flight Details to Search-Service
		Map<String, Object> newFlightDetails = new HashMap<>();
		newFlightDetails.put("ID", newFlight.getId());
		newFlightDetails.put("SEATS_BOOKED", bookingRecord.getTravellers());

		sender.rescheduleFlightInformation(oldFlightDetails); // adding
		sender.sendBookingDetails(newFlightDetails); // minus

		return bookingRecord;
	}

}
