package org.common.dbiz.dto.paymentDto;

import lombok.*;
import org.common.dbiz.dto.orderDto.FloorDto;
import org.common.dbiz.dto.orderDto.PosOrderLineVAllDto;
import org.common.dbiz.dto.orderDto.PurchaseOrderLineDto;
import org.common.dbiz.dto.orderDto.TableDto;
import org.common.dbiz.dto.orderDto.response.UserDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosReceiptOtherDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosTaxLineDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.userDto.VendorDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PurchaseInvoiceViewDto extends BaseDto implements Serializable {
    String accountingDate;
    String dateInvoiced; //Ngay hoa don
    String documentNo; // Ma hoa don
    BigDecimal totalAmount; // tong tien phai tra
    String invoiceStatus; // Trang thai           -- COM
    Integer referencePurchaseInvoiceId;
    String invoiceForm; // Mau so hoa don
    String invoiceSign; // Ky hieu hoa don
    String invoiceNo; // So hoa don
    String searchCode; // ma tra cuu
    String searchLink; // link
    String invoiceError;
    BigDecimal paid;
    String description;

    private String einvoiceValueStatus;
    private String einvoiceStatus;
    private String einvoiceTaxCode;
    private String issuedDate; // ngay phat hanh

    BigDecimal discountAmount; // Giam gia
    BigDecimal discountPercent;
    String discountType;
    BigDecimal taxAmount; // tien thue

    PosTerminalDto posTerminal;
    VendorDto vendor; // nha cung cap : ma so thue, dia chi , ten
    UserDto user; // Nguoi mua
    OrgDto org;

    List<PaymentViewDto> paymentDtos; //Thong tin thanh toan -- hien tai chua lam
    List<PurchaseInvoiceLineDto> invoiceLines; // Chua phieu nhap
    List<PurchaseInvoiceLineDetailVDto> invoiceLineDetails; // Thong tin cac mat hang
}
