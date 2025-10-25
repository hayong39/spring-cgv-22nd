package com.ceos22.spring_boot.domain.theater.dto.response;

import com.ceos22.spring_boot.entity.Reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "좌석 예매 결과 DTO")
public class BookSeatResponse {
	@Schema(description = "예약 ID", example = "90001")
	private Long reservationId;

	@Schema(description = "메시지", example = "예약 완료")
	private String message;

	public static BookSeatResponse of(Reservation reservation, String message) {
		return BookSeatResponse.builder()
			.reservationId(reservation.getId())
			.message(message)
			.build();
	}
}
