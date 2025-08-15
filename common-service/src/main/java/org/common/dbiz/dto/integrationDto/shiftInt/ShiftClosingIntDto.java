package org.common.dbiz.dto.integrationDto.shiftInt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Data
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ShiftClosingIntDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@JsonProperty("period_start_date")
	private String periodStartDate;
	@JsonProperty("posting_date")
	private String postingDate;
	@JsonProperty("company")
	private String company;
	@JsonProperty("pos_profile")
	private String posProfile;
	@JsonProperty("user")
	private String user;
	@JsonProperty("period_end_date")
	private String periodEndDate;
	@JsonProperty("posting_time")
	private String postingTime;
	@JsonProperty("document_no")
	private String documentNo;
	@JsonProperty("balance_details")
	private List<BalanceDetailDto> balanceDetails;


	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Data
	public static class BalanceDetailDto {
		@JsonProperty("mode_of_payment")
		private String modeOfPayment;
		@JsonProperty("opening_amount")
		private BigDecimal openingAmount;
	}

}










