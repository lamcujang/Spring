package com.dbiz.app.tenantservice.exception.payload;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.dbiz.app.tenantservice.constant.AppConstant;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
public final class ExceptionMsg  implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = ZonedDateTimeSerializer.class)
	@JsonFormat(shape = Shape.STRING, pattern = AppConstant.ZONED_DATE_TIME_FORMAT)
	private final ZonedDateTime timestamp;

	@JsonInclude(value = Include.NON_NULL)
	private Throwable throwable;
	private final Integer status;
	private final String error;
	private final String msg;
	
}










