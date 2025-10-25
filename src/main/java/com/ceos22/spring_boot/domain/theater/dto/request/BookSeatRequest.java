package com.ceos22.spring_boot.domain.theater.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "좌석 예매 요청 DTO")
public class BookSeatRequest {

	@Schema(description = "상영시간 ID", example = "101")
	@NotNull(message = "상영시간 ID는 필수입니다.")
	@Min(value = 1, message = "유효한 상영시간 ID여야 합니다.")
	private Long movieTimeId;

	@Schema(description = "상영관 좌석 ID(ScreenSeat)", example = "5501")
	@NotNull(message = "좌석 ID는 필수입니다.")
	@Min(value = 1, message = "유효한 좌석 ID여야 합니다.")
	private Long screenSeatId;

}
