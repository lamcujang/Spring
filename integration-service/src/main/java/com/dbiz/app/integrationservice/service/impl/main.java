package com.dbiz.app.integrationservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class main {
    public static String customEncode(String url) {
        // Tạo một StringBuilder để chứa chuỗi đã mã hóa
        StringBuilder encoded = new StringBuilder();

        // Lặp qua từng ký tự của URL và thay thế theo quy tắc tùy chỉnh
        for (char c : url.toCharArray()) {
            if (Character.isDigit(c)) {
                encoded.append(c).append("ha");
            } else if (Character.isLetter(c)) {
                encoded.append(c).append("jo");
            } else {
                encoded.append(c);
            }
        }
        return encoded.toString();
    }
    public static void main(String[] args) throws JsonProcessingException {

         try {
            String url = "https://emenu.dbiz.com/?#/home/1000448/1000080/1000508/1000047/1000090/BTH1/TTH111/1000007";
            String encodedUrl1 = customEncode(url);
            System.out.println("Mã hóa tùy chỉnh: " + encodedUrl1);
            // Mã hóa URL
            String encodedUrl = URLEncoder.encode(url, "UTF-8");
            System.out.println("Mã hóa URL: " + encodedUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("key1", "value 1");
        requestParams.put("key2", "value@!$2");
        requestParams.put("key3", "value%3");
try {

    String encodedURL = requestParams.keySet().stream()
            .map(key -> {
                try {
                    return key + "=" + encodeValue(requestParams.get(key));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            })
            .collect(Collectors.joining("&", "http://www.baeldung.com?", ""));
    System.out.println("Mã hóa URL: " + encodedURL);
}catch (Exception e)
{
    e.printStackTrace();
}
//        Set<Integer> set = Set.of(1, 2, 3, 4, 5);
//        Set<Integer> set2= Set.of(1, 2, 3, 4, 5);
//
//        Set<Integer> set3 = new HashSet<>();
//        set3.addAll(set);
//        set3.addAll(set2);
//        System.out.println(set3);
//
//        System.out.println("Java TimeZone: " + ZoneId.systemDefault());
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonS = "{ \"isCustomer\": \"Y\", \"name\": \"Nhà cung cấp trong nước\", " +
//                "\"isActive\": \"Y\", \"value\": \"NCC01\", \"C_BP_Group_ID\": 1001411 , \"partner\": [" +
//                "{ \"SOCreditStatus\": \"X\", \"partnerName\": \"Nguyễn Thị Thuỳ Linh\", \"flatDiscount\": 0, " +
//                "\"userName\": \"Nguyễn Thị Thuỳ Linh\", \"isActive\": \"Y\", \"isCustomer\": \"N\", " +
//                "\"C_BPartner_ID\": 1003292, \"isPOSVIP\": \"N\", \"isVendor\": \"N\", \"value\": \"NVCT00070\", " +
//                "\"debt\": 0, \"C_BP_Group_ID\": 1001414, \"creaditLimit\": 0 }," +
//                "{ \"SOCreditStatus\": \"X\", \"partnerName\": \"Đinh Công Hoàng\", \"flatDiscount\": 0, " +
//                "\"userName\": \"Đinh Công Hoàng\", \"isActive\": \"Y\", \"isCustomer\": \"N\", " +
//                "\"C_BPartner_ID\": 1003381, \"isPOSVIP\": \"N\", \"isVendor\": \"N\", \"value\": \"NVCT00113\", " +
//                "\"debt\": 0, \"C_BP_Group_ID\": 1001414, \"creaditLimit\": 0 }," +
//                "{ \"SOCreditStatus\": \"X\", \"partnerName\": \"Nguyễn Thị Mai Nga\", \"flatDiscount\": 0, " +
//                "\"userName\": \"Nguyễn Thị Mai Nga\", \"isActive\": \"Y\", \"isCustomer\": \"N\", " +
//                "\"phone\": \"Nguyễn Thị Mai Nga\", \"C_BPartner_ID\": 1002904, \"isPOSVIP\": \"N\", \"isVendor\": \"N\", " +
//                "\"value\": \"NVCT00051\", \"debt\": 0, \"C_BP_Group_ID\": 1001414, \"creaditLimit\": 0 }] }";
//
//        JsonNode originalJson = objectMapper.readTree(jsonS);
//        List<JsonNode> separatedPartners = new ArrayList<>();
//
//        JsonNode partners = originalJson.get("partner");
//        System.out.println("Size partner: {}"+ partners.size());
//        if (partners.isArray()) {
//            for (JsonNode partner : partners) {
//                ObjectNode newJson = objectMapper.createObjectNode();
//                newJson.put("isCustomer", originalJson.get("isCustomer").asText());
//                newJson.put("name", originalJson.get("name").asText());
//                newJson.put("isActive", originalJson.get("isActive").asText());
//                newJson.put("value", originalJson.get("value").asText());
//                newJson.put("C_BP_Group_ID", originalJson.get("C_BP_Group_ID").asInt());
//                ArrayNode n = objectMapper.createArrayNode();
//                n.add(partner);
//                newJson.set("partner", n);
//                separatedPartners.add(newJson);
//            }
//        }
//
//        // In kết quả ra màn hình
//        for (JsonNode node : separatedPartners) {
//            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
//        }
//        System.out.println("2024-10-20T02:08:01.405619Z");
//
//
//        System.out.println(DateHelper.fromDateTimeToDate2("2024-10-20T02:08:01.405619Z"));
//
//        String jsonString ="{\"m_product_id\":1160844,\"ad_org_id\":1000399,\"m_warehouse_id\":1001151,\"isactive\":\"Y\",\"c_pos_id\":null}";
//
//        JSONObject item = new JSONObject(jsonString);
//        int code = item.optInt("c_pos_id", 0); // Nếu c_pos_id là null, trả về giá trị mặc định là -1
//        System.out.println("c_pos_id: " + code);
//        String dateStr = "19-10-2023";
//
//        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//
//        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        // Chuyển đổi chuỗi sang LocalDate
//        LocalDate date = LocalDate.parse(dateStr, inputFormatter);
//
//        // Định dạng lại thành yyyy-MM-dd
//        String formattedDate = date.format(outputFormatter);
//
//        // Hiển thị kết quả
//        System.out.println("Ngày sau khi đổi định dạng: " + formattedDate);
    }
    private static String encodeValue(String value) throws UnsupportedEncodingException{

        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

    private static final DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static Instant toInstantDateTime(String date) {
        LocalDate localDate = LocalDate.parse(date, formatterDateTime);
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }
}
