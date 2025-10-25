package com.ceos22.spring_boot.domain.theater.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.ceos22.spring_boot.domain.theater.dto.request.BookSeatRequest;
import com.ceos22.spring_boot.domain.theater.dto.response.BookSeatResponse;
import com.ceos22.spring_boot.domain.theater.exception.TheaterErrorCode;
import com.ceos22.spring_boot.domain.theater.repository.MovieTimeRepository;
import com.ceos22.spring_boot.domain.theater.repository.ReservationRepository;
import com.ceos22.spring_boot.domain.theater.repository.ReservationSeatRepository;
import com.ceos22.spring_boot.domain.theater.repository.ScreenSeatRepository;
import com.ceos22.spring_boot.domain.user.repository.UserRepository;
import com.ceos22.spring_boot.entity.MovieTime;
import com.ceos22.spring_boot.entity.Reservation;
import com.ceos22.spring_boot.entity.ReservationSeat;
import com.ceos22.spring_boot.entity.ScreenSeat;
import com.ceos22.spring_boot.entity.User;
import com.ceos22.spring_boot.entity.enums.ReservationStatus;
import com.ceos22.spring_boot.global.exception.GlobalException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatBookingService {

	private final ScreenSeatRepository screenSeatRepository;
	private final MovieTimeRepository movieTimeRepository;
	private final UserRepository userRepository;
	private final ReservationRepository reservationRepository;
	private final ReservationSeatRepository reservationSeatRepository;

	@Transactional
	public BookSeatResponse bookSeat(User user, BookSeatRequest req){
		Long mtId = req.getMovieTimeId();
		Long seatId = req.getScreenSeatId();

		ScreenSeat seat = screenSeatRepository.findForUpdate(seatId)
			.orElseThrow(() -> new GlobalException(TheaterErrorCode.SCREEN_SEAT_NOT_FOUND));
		MovieTime mt = movieTimeRepository.findById(mtId)
			.orElseThrow(() -> new GlobalException(TheaterErrorCode.MOVIE_TIME_NOT_FOUND));

		if(!screenSeatRepository.existsByIdAndScreenId(seatId, mt.getScreen().getId())){
			throw new GlobalException(TheaterErrorCode.SEAT_NOT_IN_SCREEN);
		}

		if(reservationSeatRepository.existsByMovieTimeIdAndScreenSeatId(mtId, seatId)){
			throw new GlobalException(TheaterErrorCode.SEAT_ALREADY_BOOKED);
		}

		Reservation reservation = reservationRepository.save(
			Reservation.create(user, mt, ReservationStatus.CONFIRMED)
		);

		ReservationSeat rs = ReservationSeat.assign(reservation, mt, seat);

		try{
			reservationSeatRepository.save(rs);
		}catch(DataIntegrityViolationException e){
			throw new GlobalException(TheaterErrorCode.RESERVATION_CONFLICT);
		}

		return BookSeatResponse.of(reservation, "예약 완료");
	}
}
