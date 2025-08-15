package org.common.dbiz.dto.paymentDto.napas;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HeaderPayloadNapasDto {

    String messageIdentifier;
    String creationDateTime;
    String senderReference;
    String senderId;
    String receiverId;
    String signature;
}
