package com.dbiz.app.systemservice.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.model.EMenuDecoded;
import org.springframework.stereotype.Service;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class EncoderUrl {

    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE62 = BASE62_CHARS.length();

    public static String encodeEmenu(int tenantId, int orgId, int tableId, int floorId,
                                     int terminalId, String tableNo, String floorNo,String orgName, String address, int priceListId) {
        byte[] tableNoBytes = tableNo.getBytes(StandardCharsets.UTF_8);
        byte[] floorNoBytes = floorNo.getBytes(StandardCharsets.UTF_8);
        byte[] orgNameBytes = orgName.getBytes(StandardCharsets.UTF_8);
        byte[] addressBytes = address.getBytes(StandardCharsets.UTF_8);
        int totalSize =
                4 * 5 +
                        4 + tableNoBytes.length +
                        4 + floorNoBytes.length +
                        4 + orgNameBytes.length +
                        4 + addressBytes.length +
                        4;
        ByteBuffer buffer = ByteBuffer.allocate(
                totalSize
        );

        buffer.putInt(tenantId);
        buffer.putInt(orgId);
        buffer.putInt(tableId);
        buffer.putInt(floorId);
        buffer.putInt(terminalId);

        buffer.putInt(tableNoBytes.length);
        buffer.put(tableNoBytes);

        buffer.putInt(floorNoBytes.length);
        buffer.put(floorNoBytes);

        buffer.putInt(orgNameBytes.length);
        buffer.put(orgNameBytes);

        buffer.putInt(addressBytes.length);
        buffer.put(addressBytes);

        buffer.putInt(priceListId);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(buffer.array());
    }
    public static EMenuDecoded decodeEmenu(String encoded) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encoded);
        ByteBuffer buffer = ByteBuffer.wrap(decodedBytes);

        int tenantId = buffer.getInt();
        int orgId = buffer.getInt();
        int tableId = buffer.getInt();
        int floorId = buffer.getInt();
        int terminalId = buffer.getInt();

        int tableNoLength = buffer.getInt();
        if (buffer.remaining() < tableNoLength) throw new BufferUnderflowException();
        byte[] tableNoBytes = new byte[tableNoLength];
        buffer.get(tableNoBytes);
        String tableNo = new String(tableNoBytes, StandardCharsets.UTF_8);

        int floorNoLength = buffer.getInt();
        if (buffer.remaining() < floorNoLength + 4) throw new BufferUnderflowException();
        byte[] floorNoBytes = new byte[floorNoLength];
        buffer.get(floorNoBytes);
        String floorNo = new String(floorNoBytes, StandardCharsets.UTF_8);

        int orgNameLength=buffer.getInt();
        if(buffer.remaining() < orgNameLength + 4) throw new BufferUnderflowException();
        byte[] orgNameBytes  = new byte[orgNameLength];
        buffer.get(orgNameBytes);
        String orgName = new String(orgNameBytes,StandardCharsets.UTF_8);

        int addressLength=buffer.getInt();
        if(buffer.remaining() < addressLength + 4) throw new BufferUnderflowException();
        byte[] addressBytes  = new byte[addressLength];
        buffer.get(addressBytes);
        String address = new String(addressBytes,StandardCharsets.UTF_8);

        int priceListId = buffer.getInt();

        return EMenuDecoded.builder()
                .tenantId(tenantId)
                .orgId(orgId)
                .tableId(tableId)
                .floorId(floorId)
                .terminalId(terminalId)
                .tableNo(tableNo)
                .floorNo(floorNo)
                .priceListId(priceListId)
                .orgName(orgName)
                .address(address)
                .build();
    }

}
