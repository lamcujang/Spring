package org.common.dbiz.helper;

import org.common.dbiz.model.QRCodeDecoded;

import java.math.BigInteger;
import java.nio.ByteBuffer;


public class Encoder {

    // Characters for Base62 encoding
    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE62 = BASE62_CHARS.length();

    // Custom limits
    private static final int MAX_TENANT_ID = 4095;     // 12 bits
    private static final int MAX_ORDER_ID = 16383;     // 14 bits
    private static final int MAX_TIMESTAMP_OFFSET = 32767; // 15 bits

    private static final long BASE_TIMESTAMP = 1700000000L; // Optional: UNIX seconds base (e.g., Nov 2023)


    public static String encode(int tenantId, int orderId, long timestamp) {
        // Combine the inputs into a single byte array
        ByteBuffer buffer = ByteBuffer.allocate(16); // 4 bytes for tenantId, 4 bytes for orderId, 8 bytes for timestamp
        buffer.putInt(tenantId);
        buffer.putInt(orderId);
        buffer.putLong(timestamp);
        byte[] combinedBytes = buffer.array();

        // Convert the byte array to a BigInteger for Base62 encoding
        BigInteger bigInt = new BigInteger(1, combinedBytes);

        // Encode BigInteger to a base62 string
        StringBuilder base62 = new StringBuilder();
        while (bigInt.compareTo(BigInteger.ZERO) > 0) {
            int remainder = bigInt.mod(BigInteger.valueOf(BASE62)).intValue();
            base62.append(BASE62_CHARS.charAt(remainder));
            bigInt = bigInt.divide(BigInteger.valueOf(BASE62));
        }

        // Ensure the string is exactly 12 characters long
        while (base62.length() < 12) {
            base62.insert(0, '0');
        }

        return base62.reverse().toString(); // Reverse to get the correct order
    }

    public static String encodeLength7(int tenantId, int orderId, long timestamp) {
        if (tenantId > MAX_TENANT_ID || orderId > MAX_ORDER_ID) {
            throw new IllegalArgumentException("Value exceeds size limits.");
        }

        int tsOffset = (int)(timestamp - BASE_TIMESTAMP);
        if (tsOffset > MAX_TIMESTAMP_OFFSET || tsOffset < 0) {
            throw new IllegalArgumentException("Timestamp offset out of range.");
        }

        long packed = (tenantId & 0xFFF);            // 12 bits
        packed = (packed << 14) | (orderId & 0x3FFF); // +14 bits = 26 bits
        packed = (packed << 15) | (tsOffset & 0x7FFF); // +15 bits = 41 bits

        return toBase62(packed, 7);
    }

    // Base62 conversion helpers
    private static String toBase62(long value, int length) {
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            int index = (int)(value % BASE62);
            sb.append(BASE62_CHARS.charAt(index));
            value /= BASE62;
        }
        while (sb.length() < length) {
            sb.append('0');
        }
        return sb.reverse().toString();
    }

    public static QRCodeDecoded decodeLength7(String encoded) {
        long packed = fromBase62(encoded);

        int tsOffset = (int)(packed & 0x7FFF);       // last 15 bits
        packed >>= 15;
        int orderId = (int)(packed & 0x3FFF);        // next 14 bits
        packed >>= 14;
        int tenantId = (int)(packed & 0xFFF);        // first 12 bits

        long timestamp = BASE_TIMESTAMP + tsOffset;


        // Extract tenantId, orderId, and timestamp

        return QRCodeDecoded.builder()
                .tenantId(tenantId)
                .posOrderId(orderId)
                .timestamp(timestamp)
                .build();
    }

    private static long fromBase62(String base62) {
        long result = 0;
        for (int i = 0; i < base62.length(); i++) {
            result = result * BASE62 + BASE62_CHARS.indexOf(base62.charAt(i));
        }
        return result;
    }

    public static QRCodeDecoded decode(String encoded) {
        // Convert the base62 encoded string back to BigInteger
        BigInteger bigInt = BigInteger.ZERO;
        for (char ch : encoded.toCharArray()) {
            bigInt = bigInt.multiply(BigInteger.valueOf(BASE62));
            bigInt = bigInt.add(BigInteger.valueOf(BASE62_CHARS.indexOf(ch)));
        }

        // Convert BigInteger back to byte array and pad to ensure exactly 16 bytes
        byte[] combinedBytes = bigInt.toByteArray();
        byte[] paddedBytes = new byte[16];
        System.arraycopy(combinedBytes, 0, paddedBytes, 16 - combinedBytes.length, combinedBytes.length);

        // Wrap in ByteBuffer for decoding
        ByteBuffer buffer = ByteBuffer.wrap(paddedBytes);

        // Extract tenantId, orderId, and timestamp
        int tenantId = buffer.getInt();
        int orderId = buffer.getInt();
        long timestamp = buffer.getLong();

        return QRCodeDecoded.builder()
                .tenantId(tenantId)
                .posOrderId(orderId)
                .timestamp(timestamp)
                .build();
    }



}

