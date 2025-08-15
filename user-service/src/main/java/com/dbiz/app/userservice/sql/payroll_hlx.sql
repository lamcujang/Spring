--d_employee_bonus_allowances
DO
$$
BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'd_employee_bonus_allowances_sq' AND relkind = 'S') THEN
CREATE SEQUENCE d_employee_bonus_allowances_sq
    MINVALUE 1000000;
END IF;
END
$$;


CREATE TABLE IF NOT EXISTS d_employee_bonus_allowances (
                                                           d_employee_bonus_allowances_id NUMERIC(10) DEFAULT nextval('d_employee_bonus_allowances_sq') NOT NULL PRIMARY KEY,
    d_org_id            numeric(10)                                        not null
    constraint "FK_Org_DEmployeeBonusAllowances"
    references d_org,
    d_tenant_id         numeric(10)                                        not null
    constraint "FK_Tenant_DEmployeeBonusAllowances"
    references d_tenant,
    created             timestamp   default CURRENT_TIMESTAMP              not null,
    created_by          numeric(10)                                        not null,
    updated             timestamp   default CURRENT_TIMESTAMP              not null,
    updated_by          numeric(10)                                        not null,
    is_active           varchar(1)  default 'Y'::character varying         not null,
    d_employee_bonus_allowances_uu varchar(36) default pos.uuid_generate_v4()      not null,
    d_bonus_id numeric(10)
    constraint "FK_Bonus_DEmployeeBonusAllowances"
    references d_bonus,
    d_allowance_id numeric(10)
    constraint "FK_Allowance_DEmployeeBonusAllowances"
    references d_allowance,
    d_employee_id numeric(10)
    constraint "FK_Employee_DEmployeeBonusAllowances"
    references d_employee
    );



--d_penalty_deductions
DO
$$
BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'd_penalty_deduction_sq' AND relkind = 'S') THEN
CREATE SEQUENCE d_penalty_deduction_sq
    MINVALUE 1000000;
END IF;
END
$$;



CREATE TABLE IF NOT EXISTS d_penalty_deduction (
                                                   d_penalty_deduction_id NUMERIC(10) DEFAULT nextval('d_penalty_deduction_sq') NOT NULL PRIMARY KEY,
    d_org_id            numeric(10)                                        not null
    constraint "FK_Org_DPenaltyDeduction"
    references d_org,
    d_tenant_id         numeric(10)                                        not null
    constraint "FK_Tenant_DPenaltyDeduction"
    references d_tenant,
    created             timestamp   default CURRENT_TIMESTAMP              not null,
    created_by          numeric(10)                                        not null,
    updated             timestamp   default CURRENT_TIMESTAMP              not null,
    updated_by          numeric(10)                                        not null,
    is_active           varchar(1)  default 'Y'::character varying         not null,
    d_penalty_deduction_uu varchar(36) default pos.uuid_generate_v4()      not null,
    name varchar(255) not null,
    code varchar(255) null,
    description varchar(255) null,
    penalty_amount numeric null,
    warning_count numeric null,
    value varchar(5) null,
    d_salary_config_id numeric(10)  not null
    constraint "FK_SalaryConfig_DPenaltyDeduction"
    references d_salary_config
    );

WITH inserted_ref AS (
INSERT INTO pos.d_reference (
    d_reference_id, d_tenant_id, "name", description,
    created, created_by, updated, updated_by,
    d_reference_uu, is_active
)
VALUES (
    nextval('pos.d_reference_sq'::regclass), 0,
    'Unauthorized Leave', 'Nghỉ không phép',
    CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0,
    pos.uuid_generate_v4(), 'Y'
    )
    RETURNING d_reference_id
    )

INSERT INTO pos.d_reference_list (
    d_reference_list_id, d_reference_id, value, "name",
    d_tenant_id, d_reference_list_uu, is_active,
    created, created_by, updated, updated_by, lineno
)
VALUES
    (nextval('pos.d_reference_list_sq'::regclass), (SELECT d_reference_id FROM inserted_ref), 'MIN', 'Vi phạm nhẹ', 0, pos.uuid_generate_v4(), 'Y', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, NULL),
    (nextval('pos.d_reference_list_sq'::regclass), (SELECT d_reference_id FROM inserted_ref), 'MAJ', 'Vi phạm nặng', 0, pos.uuid_generate_v4(), 'Y', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, NULL),
    (nextval('pos.d_reference_list_sq'::regclass), (SELECT d_reference_id FROM inserted_ref), 'SER', 'Vi phạm rất nặng', 0, pos.uuid_generate_v4(), 'Y', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, NULL);


WITH inserted_ref AS (
INSERT INTO pos.d_reference (
    d_reference_id, d_tenant_id, "name", description,
    created, created_by, updated, updated_by,
    d_reference_uu, is_active
)
VALUES (
    nextval('pos.d_reference_sq'::regclass), 0,
    'Break Internal Rule', 'Vi phạm nội quy',
    CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0,
    pos.uuid_generate_v4(), 'Y'
    )
    RETURNING d_reference_id
    )

-- Insert các item con sử dụng id mới vừa tạo
INSERT INTO pos.d_reference_list (
    d_reference_list_id, d_reference_id, value, "name",
    d_tenant_id, d_reference_list_uu, is_active,
    created, created_by, updated, updated_by, lineno
)
VALUES
    (nextval('pos.d_reference_list_sq'::regclass), (SELECT d_reference_id FROM inserted_ref), 'D01', 'Trừ 1 ngày lương', 0, pos.uuid_generate_v4(), 'Y', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, NULL),
    (nextval('pos.d_reference_list_sq'::regclass), (SELECT d_reference_id FROM inserted_ref), 'D02', 'Trừ 2 ngày lương', 0, pos.uuid_generate_v4(), 'Y', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, NULL),
    (nextval('pos.d_reference_list_sq'::regclass), (SELECT d_reference_id FROM inserted_ref), 'FIX', 'Theo mức cố định', 0, pos.uuid_generate_v4(), 'Y', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, NULL);



create or replace view d_penalty_deduction_v
as
select
    dpd.d_penalty_deduction_id,
    COALESCE(dpd.name, drgv.name_reference) as penalty_deduction_name,
    dpd.code,
    dpd.description,
    dpd.penalty_amount,
    dpd.warning_count,
    dpd.value,
    drgv.name,
    dpd.d_salary_config_id
from
    d_penalty_deduction  dpd
        left join d_reference_get_v drgv on dpd.value = drgv.value and
                                            (drgv.name_reference  = 'Unauthorized Leave'
                                                or  drgv.name_reference  = 'Break Internal Rule')
where dpd.is_active::text = 'Y'::text;

    