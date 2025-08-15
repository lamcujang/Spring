update pos.d_table
set table_status  = 'ETB'
where 1 = 1;

update pos.d_pos_order
set d_kitchen_order_id = null,d_request_order_id = null
where 1 = 1;

update pos.d_pos_orderline
set d_kitchen_orderline_id = null
where 1 = 1;

update pos.d_rq_orderline_detail
set d_request_orderline_id = null
where 1 = 1;

--TRANSACTION
delete from pos.d_transaction where 1 = 1;

--KDS
delete from pos.d_kitchen_orderline  where 1 = 1;
delete from pos.d_kitchen_order where 1 = 1;

--PRODUCTION
delete from pos.d_productionline where 1 = 1;
delete from pos.d_production where 1 = 1;

--INTEGRATION
delete from pos.d_integration_history where 1 = 1;
delete from pos.d_erp_integration  where 1 = 1;

--SHIFT CONTROL
delete from pos.d_shift_control where 1 = 1;


--NHAP HANG
delete from pos.d_purchase_orderline  where 1 = 1;
delete from pos.d_purchase_order  where 1 = 1;


--DASHBOARD
delete from pos.d_temp_sales_revenue where 1 = 1;
delete from pos.d_temp_sales_summary where 1 = 1;

--QRCODE TRANCSACTION
delete from pos.d_reconciledetail where 1 = 1;

--VOUCHER TRANSACTION
delete from pos.d_voucher_transaction where  1 = 1;

--POS ORDER
delete from pos.d_pos_receipt_other where 1 = 1;

--Payment
delete from pos.d_pos_payment_dt where 1 = 1;
delete from pos.d_pos_payment where 1 = 1;

--Tax
delete from pos.d_pos_taxline  where 1 = 1;

--Payment
delete from pos.d_payment where 1 = 1;

--Invoice
delete from pos.d_invoiceline where 1 = 1;
delete from pos.d_invoice where 1 = 1;

--Reservation
delete from pos.d_reservation_line where  1 = 1;
delete from pos.d_reservation_order where 1 = 1;

--Request Order
delete from pos.d_rq_orderline_detail where 1 = 1;
delete from pos.d_request_orderline where 1 = 1;
delete from pos.d_request_order where 1 = 1;

--POS Order
delete from pos.d_pos_lot where 1 = 1;
delete from pos.d_pos_orderline_detail where 1 = 1;
delete from pos.d_pos_orderline where 1 = 1;
delete from pos.d_pos_order where 1 = 1;