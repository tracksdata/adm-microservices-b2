package com.cts.pss.service;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.aspectj.weaver.NewFieldTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cts.pss.controller.Sender;
import com.cts.pss.entity.BookingRecord;
import com.cts.pss.entity.Co_Passenger;
import com.cts.pss.entity.Fare;
import com.cts.pss.entity.Flight;
import com.cts.pss.entity.Passenger;
import com.cts.pss.model.CustomMessage;
import com.cts.pss.model.SearchQuery;
import com.cts.pss.repository.BookingRepository;
import com.cts.pss.repository.CoPassengerRepository;

@Service
public class BookingServiceImpl {

	// Need Price of Flight
	// Need Flight Details which is going to be booked

	@Autowired
	private RestTemplate rt;

	@Autowired
	private BookingRepository bookingDao;
	
	@Autowired
	private CoPassengerRepository coPassengerDao;

	@Autowired
	private Sender sender;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	private String fareSericeUrl = "http://localhost:8081/api/pss/fare";
	private String searchServiceUrl = "http://localhost:8082/api/pss/search";

	public Object bookFlight(SearchQuery query) {

		Fare fare = rt.getForObject(fareSericeUrl + "/" + query.getFlightNumber() + "/" + query.getOrigin() + "/"
				+ query.getDestination() + "/" + query.getFlightDate(), Fare.class);
		Flight flight = rt.getForObject(searchServiceUrl + "/find/" + query.getOrigin() + "/" + query.getDestination()
				+ "/" + query.getFlightDate() + "/" + query.getFlightNumber(), Flight.class);
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

			if (query.getPassengers().getCo_Passengers().size() == query.getTravellers() - 1) {
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

	// delete booking by booking ID

	public boolean deleteBookingById(int bookingId) {
		if (bookingDao.existsById(bookingId)) {
			bookingDao.deleteById(bookingId);
			return true;
		}
		return false;
	}
	
	
	//cancel booking for selected passengers only
	public BookingRecord customCancelBooking(int booingId,List<Co_Passenger> coPassengers) {
		
		//against to whose booking id Co-Passengers have to delete ?
		
		BookingRecord bookingRecord = getBookingDetails(booingId); 
		
		System.out.println(bookingRecord);
		
		Flight flight = rt.getForObject(
						searchServiceUrl + "/find/" + bookingRecord.getOrigin() + "/" + bookingRecord.getDestination()
								+ "/" + bookingRecord.getFlightDate() + "/" + bookingRecord.getFlightNumber(),Flight.class);
		// get all co-passengers ids suppose to be deleted from UI as Collection
		for(Co_Passenger cp:coPassengers) {
			coPassengerDao.deleteCopassengersById(cp.getCopassengerId());
			coPassengerDao.deleteCopassenger(cp.getCopassengerId());			
		}
		
		Passenger passenger = bookingRecord.getPassenger();
		
		// copy only copassengerIds from copassenger collection
		
		Set<Integer> ids = coPassengers.stream().map(Co_Passenger::getCopassengerId).collect(Collectors.toSet());
		
		List<Co_Passenger> updatedPassengers = passenger.getCo_Passengers().stream().filter(cp->!ids.contains(cp.getCopassengerId())).collect(Collectors.toList());
		
		passenger.setCo_Passengers(updatedPassengers);
		
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
		rt.postForObject(searchServiceUrl+"/newFlight", flight, Flight.class);
		//rt.getForObject(searchServiceUrl+"/newFlight", Flight.class);
		
		return bookingRecord;
	}
	
	

	// get Booking Data by ID

	public BookingRecord getBookingDetails(int bookingId) {
		return bookingDao.findById(bookingId).orElse(null);
	}

	// Reschedule flight

	public BookingRecord rescheduleBooking(int bookingId, int flightId) {

		BookingRecord bookingRecord = getBookingDetails(bookingId);

		Flight flight = rt
				.getForObject(
						searchServiceUrl + "/find/" + bookingRecord.getOrigin() + "/" + bookingRecord.getDestination()
								+ "/" + bookingRecord.getFlightDate() + "/" + bookingRecord.getFlightNumber(),
						Flight.class);

		// get new selected flight information

		Flight newFlight = rt.getForObject(searchServiceUrl + "/" + flightId, Flight.class);

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
