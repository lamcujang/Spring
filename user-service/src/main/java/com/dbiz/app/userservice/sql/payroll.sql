

DO
$$
BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'd_employee_sq' AND relkind = 'S') THEN
CREATE SEQUENCE d_employee_sq
    MINVALUE 1000000;
END IF;
END
$$;



CREATE TABLE IF NOT EXISTS d_employee (
                                          d_employee_id NUMERIC(10) DEFAULT nextval('d_employee_sq') NOT NULL PRIMARY KEY,
    d_org_id            numeric(10)                                        not null
    constraint "FK_Org_DDEmployyee"
    references d_org,
    d_tenant_id         numeric(10)                                        not null
    constraint "FK_Tenant_DDEmployyee"
    references d_tenant,
    created             timestamp   default CURRENT_TIMESTAMP              not null,
    created_by          numeric(10)                                        not null,
    updated             timestamp   default CURRENT_TIMESTAMP              not null,
    updated_by          numeric(10)                                        not null,
    is_active           varchar(1)  default 'Y'::character varying         not null,
    name               varchar(255)                                     not null,
    code               varchar(255)                                    not null,
    gender varchar(20),
    birthday DATE,
    place_of_birth varchar(500),
    ethnicity  varchar(500),
    nationality  varchar(500),
    identification_number  varchar(30),
    date_of_issue DATE,
    place_of_issue varchar(30),
    permanent_address varchar(500),
    employee_type TEXT, -- Probation / Intern / Official / Part-time
    join_date DATE,
    work_status varchar(50), -- Active / On Leave / Resigned
    phone  varchar(50),
    personal_email  varchar(250),
    corporate_email varchar(250),
    contact_address varchar(250),
    contract_number varchar(250),
    contract_type varchar(250),
    contract_start_date DATE,
    contract_end_date DATE,
    base_salary NUMERIC,
    probation_salary NUMERIC,
    contract_signed_date DATE,
    contract_termination_date DATE,
    insurance_salary NUMERIC,
    original_contract_number TEXT,
    has_social_insurance VARCHAR(1) DEFAULT 'N'::character varying  ,
    social_insurance_number TEXT,
    has_health_insurance varchar(1) DEFAULT 'N'::character varying,
    health_insurance_number TEXT,
    has_accident_insurance varchar(1) DEFAULT 'N'::character varying,
    accident_insurance_number TEXT,
    --position_allowance varchar(1) DEFAULT 'N'::character varying,
   -- phone_allowance varchar(1) DEFAULT 'N'::character varying,
    meal_allowance numeric,
   --  travel_allowance varchar(1) DEFAULT 'N'::character varying,
    transport_allowance numeric,
    clothing_allowance numeric,
    night_shift_allowance numeric,
    --seniority_allowance varchar(1) DEFAULT 'N'::character varying,
  --  other_allowance varchar(1) DEFAULT 'N'::character varying,
    --kpi_bonus varchar(1) DEFAULT 'N'::character varying,
   -- sales_bonus varchar(1) DEFAULT 'N'::character varying,
    attendance_bonus numeric,
    holiday_bonus numeric,
   -- thirteenth_month_bonus varchar(1) DEFAULT 'N'::character varying,
    --tet_bonus varchar(1) DEFAULT 'N'::character varying,
    --birthday_bonus varchar(1) DEFAULT 'N'::character varying,
   -- project_completion_bonus varchar(1) DEFAULT 'N'::character varying,
    referral_bonus numeric,
  --  innovation_bonus varchar(1) DEFAULT 'N'::character varying,
   -- training_participation_bonus varchar(1) DEFAULT 'N'::character varying,
    --other_bonus varchar(1) DEFAULT 'N'::character varying,
    image_id numeric   constraint "FK_IMAGE_DEmployee"
    references d_image,
    d_employee_uu  varchar(36) default pos.uuid_generate_v4()         not null
    );

DO
$$
BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'd_employee_bank_sq' AND relkind = 'S') THEN
CREATE SEQUENCE d_employee_bank_sq
    MINVALUE 1000000;
END IF;
END
$$;
CREATE TABLE IF NOT EXISTS  d_employee_bank (
                                                d_employee_bank_id NUMERIC(10) DEFAULT nextval('d_employee_bank_sq') NOT NULL PRIMARY KEY,
    d_org_id            numeric(10)                                        not null
    constraint "FK_Org_DDEmployyee_Bank"
    references d_org,
    d_tenant_id         numeric(10)                                        not null
    constraint "FK_Tenant_DDEmployyee_Bank"
    references d_tenant,
    created             timestamp   default CURRENT_TIMESTAMP              not null,
    created_by          numeric(10)                                        not null,
    updated             timestamp   default CURRENT_TIMESTAMP              not null,
    updated_by          numeric(10)                                        not null,
    is_active           varchar(1)  default 'Y'::character varying         not null,
    d_employee_id numeric(10) constraint "FK_Employee_EmPloyeeBank"
    references d_employee,
    account_type varchar(20),
    d_bankaccount_id numeric(10) constraint "FK_Bank_DAccount"
    references d_bankaccount,
    is_default varchar(1) default 'N'::character varying,
    d_employee_bank_uu  varchar(36) default pos.uuid_generate_v4()         not null
    );
ALTER TABLE d_bankaccount
    ADD COLUMN IF NOT EXISTS branch VARCHAR(500);


DO
$$
BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'd_employee_contact_sq' AND relkind = 'S') THEN
CREATE SEQUENCE d_employee_contact_sq
    MINVALUE 1000000;
END IF;
END
$$;


CREATE TABLE IF NOT EXISTS d_employee_contact (
                                                  d_employee_contact_id NUMERIC(10) DEFAULT nextval('d_employee_contact_sq') NOT NULL PRIMARY KEY,
    d_employee_id numeric(10) constraint "FK_Employyee_Contact"
    references d_employee,
    full_name varchar(500),
    phone varchar(50),
    relationship varchar(50),
    contact_type varchar(50),
    address varchar(500),
    d_org_id            numeric(10)                                        not null
    constraint "FK_Org_DDepartment"
    references d_org,
    d_tenant_id         numeric(10)                                        not null
    constraint "FK_Tenant_DDepartment"
    references d_tenant,
    created             timestamp   default CURRENT_TIMESTAMP              not null,
    created_by          numeric(10)                                        not null,
    updated             timestamp   default CURRENT_TIMESTAMP              not null,
    updated_by          numeric(10)                                        not null,
    is_active           varchar(1)  default 'Y'::character varying         not null,
    );


DO
$$
BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'd_department_sq' AND relkind = 'S') THEN
CREATE SEQUENCE d_department_sq
    MINVALUE 1000000;
END IF;
END
$$;

CREATE TABLE IF NOT EXISTS d_department
(
    d_department_id NUMERIC(10) DEFAULT nextval('d_department_sq') NOT NULL PRIMARY KEY,
    d_org_id            numeric(10)                                        not null
    constraint "FK_Org_DDepartment"
    references d_org,
    d_tenant_id         numeric(10)                                        not null
    constraint "FK_Tenant_DDepartment"
    references d_tenant,
    created             timestamp   default CURRENT_TIMESTAMP              not null,
    created_by          numeric(10)                                        not null,
    updated             timestamp   default CURRENT_TIMESTAMP              not null,
    updated_by          numeric(10)                                        not null,
    is_active           varchar(1)  default 'Y'::character varying         not null,
    name               varchar(255)                                     not null,
    code               varchar(255)                                    not null,
    description        text  ,
    established_date DATE not null ,
    department_head_id  numeric(10) not null
    constraint "FK_DepartmentHead_DDepartment"
    references d_employee,
    total_employees  INT,
    d_department_uu varchar(36) default pos.uuid_generate_v4()         not null
    );




DO
$$
BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'd_employee_grade_sq' AND relkind = 'S') THEN
CREATE SEQUENCE d_employee_grade_sq
    MINVALUE 1000000;
END IF;
END
$$;



CREATE TABLE IF NOT EXISTS d_employee_grade
(
    d_employee_grade_id NUMERIC(10) DEFAULT nextval('d_employee_grade_sq') NOT NULL PRIMARY KEY,
    d_org_id            numeric(10)                                        not null
    constraint "FK_Org_DEmployeeGrade"
    references d_org,
    d_tenant_id         numeric(10)                                        not null
    constraint "FK_Tenant_DEmployeeGrade"
    references d_tenant,
    created             timestamp   default CURRENT_TIMESTAMP              not null,
    created_by          numeric(10)                                        not null,
    updated             timestamp   default CURRENT_TIMESTAMP              not null,
    updated_by          numeric(10)                                        not null,
    is_active           varchar(1)  default 'Y'::character varying         not null,
    name               varchar(255)                                     not null,
    code               varchar(255)                                    not null,
    job_description        text  ,
    permission varchar(55),
    level numeric(10) ,
    experience_required TEXT,
    base_salary_min NUMERIC,
    d_employee_grade_uu varchar(36) default pos.uuid_generate_v4()         not null
    );


DO $$
BEGIN
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name='d_employee' AND column_name='d_department_id'
        ) THEN
ALTER TABLE d_employee
    ADD COLUMN d_department_id NUMERIC(10);
END IF;

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.table_constraints
            WHERE table_name='d_employee'
              AND constraint_type='FOREIGN KEY'
              AND constraint_name='FK_Department_DEmployee'
        ) THEN
ALTER TABLE d_employee
    ADD CONSTRAINT "FK_Department_DEmployee"
        FOREIGN KEY (d_department_id) REFERENCES d_department(d_department_id);
END IF;
    END$$;

DO $$

BEGIN
        -- Thêm cột d_grade_id nếu chưa tồn tại
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name='d_employee' AND column_name='d_employee_grade_id'
        ) THEN
ALTER TABLE d_employee
    ADD COLUMN d_employee_grade_id NUMERIC(10);
END IF;

        -- Thêm constraint FK_Grade_DEmployee nếu chưa tồn tại
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.table_constraints
            WHERE table_name='d_employee'
              AND constraint_type='FOREIGN KEY'
              AND constraint_name='FK_Grade_DEmployee'
        ) THEN
ALTER TABLE d_employee
    ADD CONSTRAINT "FK_Grade_DEmployee"
        FOREIGN KEY (d_employee_grade_id) REFERENCES d_employee_grade(d_employee_grade_id);
END IF;
    END$$;

DO
$$
BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'd_config_time_keeping_sq' AND relkind = 'S') THEN
CREATE SEQUENCE d_config_time_keeping_sq
    MINVALUE 1000000;
END IF;
END
$$;
CREATE TABLE IF NOT EXISTS  d_config_time_keeping (
                                                      d_config_time_keeping_id NUMERIC(10) DEFAULT nextval('d_config_time_keeping_sq') NOT NULL PRIMARY KEY,
    d_org_id            numeric(10)                                        not null
    constraint "FK_Org_DTimeKeeping"
    references d_org,
    d_tenant_id         numeric(10)                                        not null
    constraint "FK_Tenant_DTimeKeeping"
    references d_tenant,
    created             timestamp   default CURRENT_TIMESTAMP              not null,
    created_by          numeric(10)                                        not null,
    updated             timestamp   default CURRENT_TIMESTAMP              not null,
    updated_by          numeric(10)                                        not null,
    is_active           varchar(1)  default 'Y'::character varying         not null,
    standard_work_hours numeric, --	Số giờ của ngày công chuẩn
    max_lunch_break_minutes numeric, -- số giờ nghỉ trưa tối đa (đơn vị phút)
    max_late_minutes numeric, --	Cho phép đi muộn tối đa
    max_early_leave_minutes numeric, --Cho phép về sớm tối đa
    auto_mark_absent_if_no_checkin varchar(1) DEFAULT 'Y'::character varying  , --Tự động đánh dấu nghỉ nếu không chấm công vào
    allow_checkout_after_work_hours varchar(1) DEFAULT 'N'::character varying  , -- 	Cho phép chấm công ra sau giờ hành chính
    require_reason_for_late_checkin varchar(1) DEFAULT 'Y'::character varying  ,--  	Yêu cầu lý do khi chấm công vào muộn
    allow_supplement_request varchar(1) DEFAULT 'Y'::character varying  ,-- Cho phép tạo đơn xin bổ sung công
    request_deadline_days numeric, -- 	Thời hạn gửi đơn (ngày)
    approver_id  numeric (10) constraint "FK_Approver_DTimeKeeping" --  	Người phê duyệt (d_employee)
    references d_employee,-- Employee who approves timekeeping requests,
    max_checkin_attempts_per_day numeric, -- Số lần thử chấm công tối đa/ngày
    mark_unpaid_leave_if_no_checkin_and_no_request varchar(1) DEFAULT 'Y'::character varying  , --	Không có chấm công vào và không có đơn thì tính nghỉ không phép
    no_fault_if_leave_request_exists varchar(1) DEFAULT 'Y'::character varying  , -- Có đơn xin nghỉ thì không tính lỗi
    allow_single_checkin_for_multiple_shifts varchar(1) DEFAULT 'N'::character varying  , -- Cho phép chấm 1 lượt vào - ra khi làm nhiều ca liên tục trong ngày
    auto_sum_working_hours_between_shifts varchar(1) DEFAULT 'Y'::character varying, -- Hệ thống tự cộng dồn thời gian làm giữa các ca
    merge_shifts_within_minutes numeric, -- 	Cộng dồn nếu ca kế tiếp bắt đầu trong vòng (phút)
    max_shifts_per_day numeric, -- 	Số ca tối đa trong ngày
    min_rest_between_shifts_minutes numeric, -- 	Thời gian nghỉ tối thiểu giữa các ca (phút)
    notify_if_no_checkin_2_days varchar(1) DEFAULT 'Y'::character varying  , --Gửi nhắc nhở nếu không chấm công đủ 2 ngày liên tiếp
    warn_if_late_3_times_per_week varchar(1) DEFAULT 'Y'::character varying  , -- Cảnh báo khi đi muộn quá 3 lần/tuần
    warn_if_early_leave_3_times_per_week varchar(1) DEFAULT 'Y'::character varying  , -- Cảnh báo khi về sớm quá 3 lần/tuần
    notify_at TIME, -- 	Gửi nhắc nhở lúc (hh:mm:ss)
    warning_recipient_id  numeric(10) constraint "FK_WarningRecipient_DTimeKeeping"
    references d_employee,-- Employee who receives warnings about timekeeping issues 	Người nhận cảnh báo (d_employee)
    notify_by_email varchar(1) DEFAULT 'Y'::character varying , -- Whether to notify by email,
    notify_by_sms varchar(1) DEFAULT 'N'::character varying , -- Whether to notify by SMS,
    notify_in_app varchar(1) DEFAULT 'Y'::character varying,  -- Whether to notify in the app,
    d_config_time_keeping_uu varchar(36) default pos.uuid_generate_v4()         not null
    );


DO
$$
BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'd_time_keeping_sq' AND relkind = 'S') THEN
CREATE SEQUENCE d_time_keeping_sq
    MINVALUE 1000000;
END IF;
END
$$;
CREATE TABLE IF NOT EXISTS  d_time_keeping (
                                               d_time_keeping_id NUMERIC(10) DEFAULT nextval('d_time_keeping_sq') NOT NULL PRIMARY KEY,
    d_org_id            numeric(10)                                        not null
    constraint "FK_Org_DTimeKeeping"
    references d_org,
    d_tenant_id         numeric(10)                                        not null
    constraint "FK_Tenant_DTimeKeeping"
    references d_tenant,
    created             timestamp   default CURRENT_TIMESTAMP              not null,
    created_by          numeric(10)                                        not null,
    updated             timestamp   default CURRENT_TIMESTAMP              not null,
    updated_by          numeric(10)                                        not null,
    is_active           varchar(1)  default 'Y'::character varying         not null,
    checkin_address varchar(500),
    gps_coordinates varchar(500),
    checkin_radius_meters INT,
    status varchar(50),
    updated_qr timestamp,
    employee_created_by numeric(10)  constraint  "FK_Employee_DTimeKeeping"  references d_employee,
    d_time_keeping_uu varchar(36) default pos.uuid_generate_v4()         not null
    );



DO
$$
BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'd_config_shift_sq' AND relkind = 'S') THEN
CREATE SEQUENCE d_config_shift_sq
    MINVALUE 1000000;
END IF;
END
$$;
CREATE TABLE IF NOT EXISTS  d_config_shift (
                                               d_config_shift_id NUMERIC(10) DEFAULT nextval('d_config_shift_sq') NOT NULL PRIMARY KEY,
    d_org_id            numeric(10)                                        not null
    constraint "FK_Org_DTimeKeeping"
    references d_org,
    d_tenant_id         numeric(10)                                        not null
    constraint "FK_Tenant_DTimeKeeping"
    references d_tenant,
    created             timestamp   default CURRENT_TIMESTAMP              not null,
    created_by          numeric(10)                                        not null,
    updated             timestamp   default CURRENT_TIMESTAMP              not null,
    updated_by          numeric(10)                                        not null,
    is_active           varchar(1)  default 'Y'::character varying         not null,
    code varchar(255),
    name varchar(255),
    shift_type varchar(50), -- Part-time / Full-time
    start_time TIME,
    break_duration_minutes INT,
    end_time TIME,
    checkin_time TIME,
    checkout_time TIME,
    valid_from DATE,
    valid_to DATE,
    working_days varchar(50), -- Comma-separated list of days (e.g., "Monday,Tuesday,Wednesday"

    is_valid_to varchar(1) DEFAULT 'Y'::character varying -- Whether the shift is currently valid
    ,d_employee_created_by numeric(10)  constraint  "FK_Employee_DConfigShift"
    references d_employee, -- Employee who created the shift
    d_config_shift_uu varchar(36) default pos.uuid_generate_v4()         not null
    );



DO
$$
BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'd_config_shift_employee_sq' AND relkind = 'S') THEN
CREATE SEQUENCE d_config_shift_employee_sq
    MINVALUE 1000000;
END IF;
END
$$;

CREATE TABLE IF NOT EXISTS  d_config_shift_employee (
                                                        d_config_shift_employee_id NUMERIC(10) DEFAULT nextval('d_config_shift_employee_sq') NOT NULL PRIMARY KEY,
    d_org_id            numeric(10)                                        not null
    constraint "FK_Org_DTimeKeeping"
    references d_org,
    d_tenant_id         numeric(10)                                        not null
    constraint "FK_Tenant_DTimeKeeping"
    references d_tenant,
    created             timestamp   default CURRENT_TIMESTAMP              not null,
    created_by          numeric(10)                                        not null,
    updated             timestamp   default CURRENT_TIMESTAMP              not null,
    updated_by          numeric(10)                                        not null,
    is_active           varchar(1)  default 'Y'::character varying         not null,
    d_config_shift_id numeric(10) constraint "FK_Shift_DConfigShift"
    references d_config_shift,
    d_employee_id numeric(10) constraint "FK_Employee_DConfigShiftEmployee"
    references d_employee,
    d_config_shift_employee_uu varchar(36) default pos.uuid_generate_v4()         not null
    );



DO
$$
BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'd_reason_sq' AND relkind = 'S') THEN
CREATE SEQUENCE d_reason_sq
    MINVALUE 1000000;
END IF;
END
$$;
CREATE TABLE IF NOT EXISTS  d_reason (
                                         d_reason_id NUMERIC(10) DEFAULT nextval('d_reason_sq') NOT NULL PRIMARY KEY,
    d_org_id            numeric(10)                                        not null
    constraint "FK_Org_DTimeKeeping"
    references d_org,
    d_tenant_id         numeric(10)                                        not null
    constraint "FK_Tenant_DTimeKeeping"
    references d_tenant,
    created             timestamp   default CURRENT_TIMESTAMP              not null,
    created_by          numeric(10)                                        not null,
    updated             timestamp   default CURRENT_TIMESTAMP              not null,
    updated_by          numeric(10)                                        not null,
    is_active           varchar(1)  default 'Y'::character varying         not null,
    name varchar(500),
    type varchar(500),
    d_reason_uu varchar(36) default pos.uuid_generate_v4()         not null
    );


DO
$$
BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'd_employee_attendance_sq' AND relkind = 'S') THEN
CREATE SEQUENCE d_employee_attendance_sq
    MINVALUE 1000000;
END IF;
END
$$;
CREATE TABLE IF NOT EXISTS  d_employee_attendance (
                                                      d_employee_attendance_id NUMERIC(10) DEFAULT nextval('d_employee_attendance_sq') NOT NULL PRIMARY KEY,
    d_org_id            numeric(10)                                        not null
    constraint "FK_Org_DTimeKeeping"
    references d_org,
    d_tenant_id         numeric(10)                                        not null
    constraint "FK_Tenant_DTimeKeeping"
    references d_tenant,
    created             timestamp   default CURRENT_TIMESTAMP              not null,
    created_by          numeric(10)                                        not null,
    updated             timestamp   default CURRENT_TIMESTAMP              not null,
    updated_by          numeric(10)                                        not null,
    is_active           varchar(1)  default 'Y'::character varying         not null,
    d_employee_id  numeric(10) constraint "FK_Employee_DAttendance"
    references d_employee, -- Employee ID
    attendance_date DATE,
    checkin_time TIME,
    checkin_reason TEXT,
    d_config_shift_id numeric(10) constraint "FK_Shift_DAttendance"
    references d_config_shift, -- Shift ID
    attendance_type TEXT,
    approved_by numeric(10) constraint "FK_ApprovedBy_DAttendance"
    references d_employee, -- Employee who approved the attendance record
    d_employee_attendance_uu varchar(36) default pos.uuid_generate_v4()         not null
    );



create or replace  view d_config_time_keeping_get_v as
select dctk.d_config_time_keeping_id,
       dctk.d_org_id,
       dctk.d_tenant_id,
       dctk.created,
       dctk.created_by,
       dctk.updated,
       dctk.updated_by,
       dctk.is_active,
       dctk.standard_work_hours,
       dctk.max_lunch_break_minutes,
       dctk.max_late_minutes,
       dctk.max_early_leave_minutes,
       dctk.auto_mark_absent_if_no_checkin,
       dctk.allow_checkout_after_work_hours,
       dctk.require_reason_for_late_checkin,
       dctk.allow_supplement_request,
       dctk.request_deadline_days,
       dctk.approver_id,
       dctk.max_checkin_attempts_per_day,
       dctk.mark_unpaid_leave_if_no_checkin_and_no_request,
       dctk.no_fault_if_leave_request_exists,
       dctk.allow_single_checkin_for_multiple_shifts,
       dctk.auto_sum_working_hours_between_shifts,
       dctk.merge_shifts_within_minutes,
       dctk.max_shifts_per_day,
       dctk.min_rest_between_shifts_minutes,
       dctk.notify_if_no_checkin_2_days,
       dctk.warn_if_late_3_times_per_week,
       dctk.warn_if_early_leave_3_times_per_week,
       dctk.notify_at,
       dctk.warning_recipient_id,
       dctk.notify_by_email,
       dctk.notify_by_sms,
       dctk.notify_in_app,
       de.name as approver_name,
       de.code as approver_code,
       de2.code as warning_recipient_code,
       de2.name as warning_recipient_name,
       dog.code as org_code,
       dog.name as org_name
from pos.d_config_time_keeping dctk
         join d_org dog on dog.d_org_id = dctk.d_org_id
         left join d_employee de on dctk.approver_id = de.d_employee_id
         left join d_employee de2 on dctk.warning_recipient_id = de2.d_employee_id ;




CALL pos.d_gen_reference('Contract Type', 'Loại hợp đồng',
                         '[
                           {
                             "referenceListValue": "UDC",
                             "referenceListName": "Hợp đồng không xác định thời hạn",
                             "lineNo": 0
                           },
                           {
                             "referenceListValue": "FTC",
                             "referenceListName": "Hợp đồng có thời hạn 1 năm",
                             "lineNo": 1
                           },
                           {
                             "referenceListValue": "F2Y",
                             "referenceListName": "Hợp đồng có thời hạn 2 năm",
                             "lineNo": 2
                           },
                           {
                             "referenceListValue": "F3Y",
                             "referenceListName": "Hợp đồng có thời hạn 3 năm",
                             "lineNo": 3
                           },
                           {
                             "referenceListValue": "P2M",
                             "referenceListName": "Hợp đồng thử việc 2 tháng",
                             "lineNo": 4
                           }
                         ]'::jsonb);




CALL pos.d_gen_reference('Employee Type', 'Loại nhân viên',
                         '[
                           {
                             "referenceListValue": "PRO",
                             "referenceListName": "Thử việc",
                             "lineNo": 0
                           },
                           {
                             "referenceListValue": "INT",
                             "referenceListName": "Thực tập",
                             "lineNo": 1
                           },
                           {
                             "referenceListValue": "OFF",
                             "referenceListName": "Chính thức",
                             "lineNo": 2
                           },
                           {
                             "referenceListValue": "PTT",
                             "referenceListName": "Bán tời gian",
                             "lineNo": 3
                           }
                         ]'::jsonb);





CALL pos.d_gen_reference('Working Status', 'Trạng thái làm việc',
                         '[
                           {
                             "referenceListValue": "ACT",
                             "referenceListName": "Đang làm",
                             "lineNo": 0
                           },
                           {
                             "referenceListValue": "LEA",
                             "referenceListName": "Nghỉ phép",
                             "lineNo": 1
                           },
                           {
                             "referenceListValue": "RES",
                             "referenceListName": "Đã nghỉ",
                             "lineNo": 2
                           }
                         ]'::jsonb);







CALL pos.d_gen_reference('Employee BankAccount Type', 'Loại tài khoản ngân hàng',
                         '[
                           {
                             "referenceListValue": "PER",
                             "referenceListName": "Cá nhân",
                             "lineNo": 0
                           },
                           {
                             "referenceListValue": "BUS",
                             "referenceListName": "Công ty",
                             "lineNo": 1
                           }
                         ]'::jsonb);




CALL pos.d_gen_reference('Employee Contact Type', 'Loại liên hệ',
                         '[
                           {
                             "referenceListValue": "FAM",
                             "referenceListName": "Người thân",
                             "lineNo": 0
                           },
                           {
                             "referenceListValue": "EMC",
                             "referenceListName": "Liên hệ khẩn cấp",
                             "lineNo": 1
                           }
                         ]'::jsonb);




CALL pos.d_gen_reference('Grade Experience Required', 'Yêu cầu Kinh nghiệm',
                         '[
                           {
                             "referenceListValue": "1MO",
                             "referenceListName": "1 Tháng",
                             "lineNo": 0
                           },
                           {
                             "referenceListValue": "2MO",
                             "referenceListName": "2 Tháng",
                             "lineNo": 1
                           },
                           {
                             "referenceListValue": "3MO",
                             "referenceListName": "3 Tháng",
                             "lineNo": 2
                           },
                           {
                             "referenceListValue": "6MO",
                             "referenceListName": "6 Tháng",
                             "lineNo": 3
                           },
                           {
                             "referenceListValue": "1YR",
                             "referenceListName": "1 Năm",
                             "lineNo": 4
                           },
                           {
                             "referenceListValue": "2YR",
                             "referenceListName": "2 Năm",
                             "lineNo": 5
                           }
                         ,
                           {
                             "referenceListValue": "3YR",
                             "referenceListName": "2 Năm",
                             "lineNo": 6
                           }
                         ,
                           {
                             "referenceListValue": "5PL",
                             "referenceListName": "Trên 5 Năm",
                             "lineNo": 7
                           }
                         ]'::jsonb);






CALL pos.d_gen_reference('Employee Permission', 'Quyền hạn nhân viên',
                         '[
                           {
                             "referenceListValue": "SML",
                             "referenceListName": "Quản lý cấp cao",
                             "lineNo": 0
                           },
                           {
                             "referenceListValue": "DPM",
                             "referenceListName": "Quản lý phòng ban",
                             "lineNo": 1
                           },
                           {
                             "referenceListValue": "PMT",
                             "referenceListName": "Quản lý nhóm dự án",
                             "lineNo": 2
                           },
                           {
                             "referenceListValue": "REG",
                             "referenceListName": "Nhân viên chính thức",
                             "lineNo": 3
                           },
                           {
                             "referenceListValue": "INT",
                             "referenceListName": "Thực tập sinh",
                             "lineNo": 4
                           },
                           {
                             "referenceListValue": "ADM",
                             "referenceListName": "Hỗ trợ hành chính",
                             "lineNo": 5
                           }
                         ,
                           {
                             "referenceListValue": "ROD",
                             "referenceListName": "Chỉ xem dữ liệu (read-only)",
                             "lineNo": 6
                           }
                         ,
                           {
                             "referenceListValue": "NRW",
                             "referenceListName": "Không có quyền",
                             "lineNo": 7
                           }
                         ]'::jsonb);






CALL pos.d_gen_reference('Employee Shift Type', 'Loại ca làm việc',
                         '[
                           {
                             "referenceListValue": "FUL",
                             "referenceListName": "Toàn thời gian",
                             "lineNo": 0
                           },
                           {
                             "referenceListValue": "PAR",
                             "referenceListName": "Bán thời gian",
                             "lineNo": 1
                           }
                         ]'::jsonb);






DO
$$
BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'd_salary_config_sq' AND relkind = 'S') THEN
CREATE SEQUENCE d_salary_config_sq
    MINVALUE 1000000;
END IF;
END
$$;

CREATE TABLE IF NOT EXISTS d_salary_config (
                                               d_salary_config_id NUMERIC(10) DEFAULT nextval('d_salary_config_sq') NOT NULL PRIMARY KEY,
    d_org_id            numeric(10)                                        not null
    constraint "FK_Org_DSalaryConfig"
    references d_org,
    d_tenant_id         numeric(10)                                        not null
    constraint "FK_Tenant_DSalaryConfig"
    references d_tenant,
    created             timestamp   default CURRENT_TIMESTAMP              not null,
    created_by          numeric(10)                                        not null,
    updated             timestamp   default CURRENT_TIMESTAMP              not null,
    updated_by          numeric(10)                                        not null,
    is_active           varchar(1)  default 'Y'::character varying         not null,
    ot_limit_per_day NUMERIC,
    require_ot_approval_before_work VARCHAR(1),
    ot_coefficient_weekday NUMERIC,
    ot_coefficient_weekend NUMERIC,
    auto_ot_weekend VARCHAR(1),
    auto_ot_holiday VARCHAR(1),
    ot_coefficient_holiday NUMERIC,
    apply_from NUMERIC,
    apply_to NUMERIC,
    ot_coefficient_night NUMERIC,
    ot_approver NUMERIC(10) constraint "FK_EmployeeOT_DSalaryConfig"
    references pos.d_employee,
    notify_ot_request VARCHAR(1),
    auto_reject_unapproved_ot VARCHAR(1),
    total_work_hours_lt NUMERIC,
    underwork_coefficient_lt NUMERIC,
    total_work_hours_from NUMERIC,
    total_work_hours_to NUMERIC,
    underwork_coefficient_to NUMERIC,
    exclude_lunch_break_from_underwork VARCHAR(1),
    round_underwork_time_15min VARCHAR(1),
    allow_underwork_offset_by_ot VARCHAR(1),
    social_insurance_employee_rate NUMERIC,
    health_insurance_employee_rate NUMERIC,
    accident_insurance_employee_rate NUMERIC,
    social_insurance_company_rate NUMERIC,
    health_insurance_company_rate NUMERIC,
    accident_insurance_company_rate NUMERIC,
    labor_accident_insurance NUMERIC,
    min_insurance_contribution NUMERIC,
    max_insurance_contribution NUMERIC,
    auto_calculate_end_month_salary VARCHAR(1),
    auto_send_payslip_email VARCHAR(1),
    auto_backup_data VARCHAR(1),
    remind_closing_before_salary_calc VARCHAR(1),
    alert_exceed_ot_limit VARCHAR(1),
    notify_absence VARCHAR(1),
    alert_salary_not_calculated VARCHAR(1),
    alert_attendance_not_closed VARCHAR(1),
    salary_cycle VARCHAR(25),
    salary_calc_day VARCHAR(25),
    notification_email VARCHAR(25),
    reward_approver NUMERIC(10)  constraint "FK_Employee_DSalaryConfig"
    references pos.d_employee,
    auto_approval_limit NUMERIC,
    notify_email_on_reward VARCHAR(1),
    log_reward_history VARCHAR(1),
    daily_approval_report VARCHAR(1),
    d_salary_config_uu varchar(36) default pos.uuid_generate_v4()         not null
    );



create or replace view d_department_get_v as
select dd.created_by,
       dd.established_date,
       dd.department_head_id,
       dd.total_employees,
       dd.d_department_id,
       dd.updated,
       dd.updated_by,
       dd.d_org_id,
       dd.d_tenant_id,
       dd.created,
       dd.d_department_uu,
       dd.is_active,
       dd.name,
       dd.code,
       dd.description,
       de.code as department_head_code,
       de.name as department_head_name
from d_department dd
         join d_employee de on dd.department_head_id = de.d_employee_id;


create or replace view d_employee_grade_get_v as
select deg.d_employee_grade_id,
       deg.d_org_id,
       deg.d_tenant_id,
       deg.created,
       deg.created_by,
       deg.updated,
       deg.updated_by,
       deg.is_active,
       deg.name,
       deg.code,
       deg.job_description,
       deg.permission,
       deg.level,
       deg.experience_required,
       deg.base_salary_min,
       drl.name as permission_name,
       drl2.name as experience_required_name
from d_employee_grade deg
         left join d_reference_list drl on deg.permission = drl.value join d_reference dr on drl.d_reference_id = dr.d_reference_id and dr.name = 'Grade Permission'
         left join d_reference_list drl2 on deg.experience_required = drl2.value
         left join d_reference dr2 on drl2.d_reference_id = dr2.d_reference_id
    and dr2.name = 'Grade Experience Required';




CREATE OR REPLACE VIEW pos.d_employee_get_v
AS SELECT de.d_employee_id,
          de.d_org_id,
          de.d_tenant_id,
          de.created,
          de.created_by,
          de.updated,
          de.updated_by,
          de.is_active,
          de.name,
          de.code,
          de.gender,
          de.birthday,
          de.place_of_birth,
          de.ethnicity,
          de.nationality,
          de.identification_number,
          de.date_of_issue,
          de.place_of_issue,
          de.permanent_address,
          de.employee_type,
          de.join_date,
          de.work_status,
          de.phone,
          de.personal_email,
          de.corporate_email,
          de.contact_address,
          de.contract_number,
          de.contract_type,
          de.contract_start_date,
          de.contract_end_date,
          de.base_salary,
          de.probation_salary,
          de.contract_signed_date,
          de.contract_termination_date,
          de.insurance_salary,
          de.original_contract_number,
          de.has_social_insurance,
          de.social_insurance_number,
          de.has_health_insurance,
          de.health_insurance_number,
          de.has_accident_insurance,
          de.accident_insurance_number,
          de.position_allowance,
          de.phone_allowance,
          de.meal_allowance,
          de.transport_allowance,
          de.clothing_allowance,
          de.attendance_bonus,
          de.referral_bonus,
          de.holiday_bonus,
          de.image_id,
          de.d_employee_uu,
          de.d_department_id,
          de.d_employee_grade_id,
          drl.name AS work_status_name,
          drl2.name AS employee_type_name,
          drl3.name AS contract_type_name,
          dog.name AS org_name,
          dp.name AS department_name,
          deg.name AS employee_grade_name,
          de.night_shift_allowance
   FROM pos.d_employee de
            JOIN pos.d_org dog ON de.d_org_id = dog.d_org_id
            JOIN pos.d_department dp ON de.d_department_id = dp.d_department_id
            JOIN pos.d_employee_grade deg ON de.d_employee_grade_id = deg.d_employee_grade_id
            JOIN pos.d_reference_list drl ON drl.value::text = de.work_status::text
     JOIN pos.d_reference dr ON dr.d_reference_id = drl.d_reference_id AND dr.name::text = 'Working Status'::text
              JOIN pos.d_reference_list drl2 ON drl2.value::text = de.employee_type
              JOIN pos.d_reference dr2 ON drl2.d_reference_id = dr2.d_reference_id AND dr2.name::text = 'Employee Type'::text
              JOIN pos.d_reference_list drl3 ON drl3.value::text = de.contract_type::text
              JOIN pos.d_reference dr3 ON dr3.d_reference_id = drl3.d_reference_id AND dr3.name::text = 'Contract Type'::text;



select contract_type,employee_type,work_status,d_employee_id,d_department_id,d_employee_grade_id from d_employee;

create or replace  view d_config_shift_get_v as
select dcs.*,dog.name as org_name,de.name as employee_created_name ,
       drl.name as shift_type_name
from d_config_shift dcs  join d_org dog on dcs.d_org_id = dog.d_org_id
                         join d_employee de on de.d_employee_id = dcs.d_employee_created_by
                         left join d_reference_list drl on drl.value = dcs.shift_type
                         join d_reference dr on drl.d_reference_id = dr.d_reference_id
    and dr.name = 'Employee Shift Type';


drop view if exists d_config_shift_employee_get_v;

create or replace view d_config_shift_employee_get_v as
select dcse.d_config_shift_employee_id,
       de.d_org_id,
       dcse.d_tenant_id,
       dcse.created,
       dcse.created_by,
       dcse.updated,
       dcse.updated_by,
       coalesce(dcse.is_active,'N') as is_active,
       dcse.d_config_shift_id,
       coalesce(dcse.d_employee_id,de.d_employee_id) as d_employee_id,
       dcse.d_config_shift_employee_uu,
       de.name as employee_name,
       de.code as employee_code,
       de.d_employee_grade_id,
       de.d_department_id,
       dd.name as department_name,
       dog.name as org_name,
       drl.name as work_status_name,
       drl.value as work_status
from d_employee de
         left join  d_config_shift_employee dcse on dcse.d_employee_id = de.d_employee_id
         join d_department dd on de.d_department_id = dd.d_department_id
         join d_org dog on dog.d_org_id = de.d_org_id
         join d_reference_list drl on drl.value = de.work_status
         join d_reference dr on dr.d_reference_id = drl.d_reference_id and dr.name = 'Working Status'
where de.work_status != 'RES';



create or replace view d_config_shift_employee_get_v as
select dcse.d_config_shift_employee_id,
       de.d_org_id,
       dcse.d_tenant_id,
       dcse.created,
       dcse.created_by,
       dcse.updated,
       dcse.updated_by,
       coalesce(dcse.is_active,'N') as is_active,
       dcse.d_config_shift_id,
       coalesce(dcse.d_employee_id,de.d_employee_id) as d_employee_id,
       dcse.d_config_shift_employee_uu,
       de.name as employee_name,
       de.code as employee_code,
       de.d_employee_grade_id,
       de.d_department_id,
       dd.name as department_name,
       dog.name as org_name,
       drl.name as work_status_name,
       drl.value as work_status
from d_employee de
         join  d_config_shift_employee dcse on dcse.d_employee_id = de.d_employee_id
         join d_department dd on de.d_department_id = dd.d_department_id
         join d_org dog on dog.d_org_id = de.d_org_id
         join d_reference_list drl on drl.value = de.work_status
         join d_reference dr on dr.d_reference_id = drl.d_reference_id and dr.name = 'Working Status'
where de.work_status != 'RES';

--Time Sheet
DO
$$
BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'd_timesheet_summary_sq' AND relkind = 'S') THEN
CREATE SEQUENCE d_timesheet_summary_sq
    MINVALUE 1000000;
END IF;
END
$$;

CREATE TABLE IF NOT EXISTS d_timesheet_summary (
                                                   d_timesheet_summary_id NUMERIC(10) DEFAULT nextval('d_timesheet_summary_sq') NOT NULL PRIMARY KEY,
    d_org_id            numeric(10)                                        not null
    constraint "FK_Org_DTimeSheetSummary"
    references d_org,
    d_tenant_id         numeric(10)                                        not null
    constraint "FK_Tenant_DTimeSheetSummary"
    references d_tenant,
    created             timestamp   default CURRENT_TIMESTAMP              not null,
    created_by          numeric(10)                                        not null,
    updated             timestamp   default CURRENT_TIMESTAMP              not null,
    updated_by          numeric(10)                                        not null,
    is_active           varchar(1)  default 'Y'::character varying         not null,
    d_timesheet_summary_uu varchar(36) default pos.uuid_generate_v4()      not null,
    standard_working_hours numeric(6,2) NULL,
    total_working_hours numeric(6,2) NULL,
    actual_working_hours numeric(6,2) null,
    overtime_hours numeric(6,2) null,
    late_early_hours numeric(6,2) null,
    monthly_overtime_hours  numeric(6,2) null,
    annual_leave_used numeric null,
    monthly_late_early_hours numeric(6,2) null,
    d_employee_id numeric(10)                                        not null
    constraint "FK_Employee_DTimeSheetSummary"
    references d_employee
    );

--Đơn nghỉ phép
DO
$$
BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'd_leave_application_sq' AND relkind = 'S') THEN
CREATE SEQUENCE d_leave_application_sq
    MINVALUE 1000000;
END IF;
END
$$;


CREATE TABLE IF NOT EXISTS d_leave_application (
                                                   d_leave_application_id NUMERIC(10) DEFAULT nextval('d_leave_application_sq') NOT NULL PRIMARY KEY,
    d_org_id            numeric(10)                                        not null
    constraint "FK_Org_DLeaveApplication"
    references d_org,
    d_tenant_id         numeric(10)                                        not null
    constraint "FK_Tenant_DLeaveApplication"
    references d_tenant,
    created             timestamp   default CURRENT_TIMESTAMP              not null,
    created_by          numeric(10)                                        not null,
    updated             timestamp   default CURRENT_TIMESTAMP              not null,
    updated_by          numeric(10)                                        not null,
    is_active           varchar(1)  default 'Y'::character varying         not null,
    d_leave_application_uu varchar(36) default pos.uuid_generate_v4()      not null,
    leave_type varchar(100) not null,
    from_date date not null,
    to_date date not null,
    approver_id numeric(10) not null,
    reason varchar(255) not null,
    d_timesheet_summary_id     numeric(10)                                         not null
    constraint "FK_TimeSheet_DLeaveApplication"
    references d_timesheet_summary
    );


--Tăng ca
DO
$$
BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'd_overtime_log_sq' AND relkind = 'S') THEN
CREATE SEQUENCE d_overtime_log_sq
    MINVALUE 1000000;
END IF;
END
$$;


CREATE TABLE IF NOT EXISTS d_overtime_log (
                                              d_overtime_log_id NUMERIC(10) DEFAULT nextval('d_overtime_log_sq') NOT NULL PRIMARY KEY,
    d_org_id            numeric(10)                                        not null
    constraint "FK_Org_DOverTimeLog"
    references d_org,
    d_tenant_id         numeric(10)                                        not null
    constraint "FK_Tenant_DOverTimeLog"
    references d_tenant,
    created             timestamp   default CURRENT_TIMESTAMP              not null,
    created_by          numeric(10)                                        not null,
    updated             timestamp   default CURRENT_TIMESTAMP              not null,
    updated_by          numeric(10)                                        not null,
    is_active           varchar(1)  default 'Y'::character varying         not null,
    d_overtime_log_uu varchar(36) default pos.uuid_generate_v4()      not null,
    overtime_date date null,
    overtime_hours numeric(5,2) null,
    overtime_type varchar(100) null,
    d_timesheet_summary_id     numeric(10)                                         not null
    constraint "FK_OverTime_DLeaveTimeSheetSummary"
    references d_timesheet_summary
    );


create or replace view d_h_timesheet_summary_v
AS select
              dt.d_employee_id,
              de.name as employee_name,
              de.code as employee_code,
              dt.d_org_id,
              dor.name as org_name,
              de.d_department_id,
              dd.name as department_name,
              de.d_employee_grade_id,
              deg.name as grade_name,
              de.employee_type,
              dt.standard_working_hours,
              dt.total_working_hours,
              dt.actual_working_hours,
              dt.overtime_hours,
              dt.late_early_hours,
              dt.monthly_overtime_hours,
              dt.annual_leave_used,
              dt.monthly_late_early_hours,
              dt.created,
              dt.d_timesheet_summary_id
   FROM
              d_timesheet_summary dt
                  LEFT JOIN d_employee de ON dt.d_employee_id = de.d_employee_id
                  LEFT JOIN d_org dor ON dor.d_org_id = dt.d_org_id
                  LEFT JOIN d_department dd ON dd.d_department_id = de.d_department_id
                  LEFT JOIN d_employee_grade deg ON de.d_employee_grade_id = deg.d_employee_grade_id
   WHERE
              dt.is_active = 'Y'
     AND de.is_active = 'Y';

--Add column night_shift_allowance in Employee
alter table d_employee
    add column night_shift_allowance numeric null;































