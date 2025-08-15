package org.common.dbiz.request.reportRequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxDeclarationIndividualQueryRequest extends BaseQueryRequest {
    private Integer id;
    private String isActive;

    private String taxPeriodType; // Kỳ Kê Khai //MON: Tháng | QTR: Quý
    private String taxMonthFrom; // Từ Tháng
    private String taxMonthTo; // Đến Tháng
    private Integer taxYear; // Năm
    private Integer taxQuarter; // Quý
    private Integer additionalSubmission; //Khai Bổ Sung lần thứ
    private String supplementaryDeclarationDate; //Ngày lập KHBS

}
