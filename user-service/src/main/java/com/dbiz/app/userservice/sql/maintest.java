package com.dbiz.app.userservice.sql;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

public class maintest {

        public static void main(String args[]) throws IOException, ParseException {
            ObjectMapper mapper = new ObjectMapper();
            EmployeeDto dto  = new EmployeeDto();
            dto.setCode("EMP001");
            dto.setName("Empl oye");
            dto.setBirthday("1990-01-01");
            dto.setContactAddress("Sài gòn");
            DeparmentDto dept1 = new DeparmentDto();
            dept1.setId(1);
            dept1.setName("Phòng Kế toán");
            dept1.setCode("PT001");
            dto.setDept1(dept1);
            dto.setDept2(dept1);


            FilterProvider filters = new SimpleFilterProvider() .addFilter(
                    "employeeFilter", SimpleBeanPropertyFilter.filterOutAllExcept("name","code"));

            String jsonString = mapper.writer(filters)
                    .withDefaultPrettyPrinter()
                    .writeValueAsString(dto );
            GlobalReponse response = new GlobalReponse();
            response.setData(jsonString);
            System.out.println(response.getData());
        }
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GlobalReponse  {
        private Integer status =200;
        private String message ="Success";
        private Object data ;
        private String errors = "";

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonFilter("employeeFilter")
    public static class EmployeeDto implements Serializable {
        Integer id;
        private Integer orgId;
        @Size(max = 1)
        String isActive;
        @Size(max = 255)
        String name;
        @Size(max = 255)
        String code;
        @Size(max = 20)
        String gender;
        String birthday;
        @Size(max = 500)
        String placeOfBirth;
        @Size(max = 500)
        String ethnicity;
        @Size(max = 500)
        String nationality;
        @Size(max = 30)
        String identificationNumber;
        String dateOfIssue;
        @Size(max = 30)
        String placeOfIssue;
        @Size(max = 500)
        String permanentAddress;
        String employeeType;
        String joinDate;
        @Size(max = 50)
        String workStatus;
        @Size(max = 50)
        String phone;
        @Size(max = 250)
        String personalEmail;
        @Size(max = 250)
        String corporateEmail;
        @Size(max = 250)
        String contactAddress;
        @Size(max = 250)
        String contractNumber;
        @Size(max = 250)
        String contractType;
        String contractStartDate;
        String contractEndDate;
        BigDecimal baseSalary;
        BigDecimal probationSalary;
        String contractSignedDate;
        String contractTerminationDate;
        BigDecimal insuranceSalary;
        String originalContractNumber;
        @Size(max = 1)
        String hasSocialInsurance;
        String socialInsuranceNumber;
        @Size(max = 1)
        String hasHealthInsurance;
        String healthInsuranceNumber;
        @Size(max = 1)
        String hasAccidentInsurance;
        String accidentInsuranceNumber;
        @Size(max = 1)
        String positionAllowance;
        @Size(max = 1)
        String phoneAllowance;
        @Size(max = 1)
        String mealAllowance;
        @Size(max = 1)
        String travelAllowance;
        @Size(max = 1)
        String transportAllowance;
        @Size(max = 1)
        String clothingAllowance;
        @Size(max = 1)
        String seniorityAllowance;
        @Size(max = 1)
        String otherAllowance;
        @Size(max = 1)
        String kpiBonus;
        @Size(max = 1)
        String salesBonus;
        @Size(max = 1)
        String attendanceBonus;
        @Size(max = 1)
        String thirteenthMonthBonus;
        @Size(max = 1)
        String tetBonus;
        @Size(max = 1)
        String birthdayBonus;
        @Size(max = 1)
        String projectCompletionBonus;
        @Size(max = 1)
        String referralBonus;
        @Size(max = 1)
        String innovationBonus;
        @Size(max = 1)
        String trainingParticipationBonus;
        @Size(max = 1)
        String otherBonus;
        private Integer imageId;
        Integer departmentId;
        Integer employeeGradeId;

        String departmentName;
        String employeeGradeName;
        String orgName;
        String workStatusName;
        String employeeTypeName;
        String contractTypeName;

        DeparmentDto dept1;

        DeparmentDto dept2;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonFilter("employeeFilter")
    public static class DeparmentDto implements Serializable {

        Integer id;
        private Integer orgId;
        @Size(max = 1)
        String isActive;
        @Size(max = 255)
        String name;
        @Size(max = 255)
        String code;
        @Size(max = 500)
        String description;
        @Size(max = 500)
        String contactAddress;
        @Size(max = 50)
        String phone;
        @Size(max = 250)
        String email;
    }

    }