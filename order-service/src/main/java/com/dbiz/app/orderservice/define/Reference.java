package com.dbiz.app.orderservice.define;

public class Reference {

    public enum DocStatus {
        DRA, COM, IPR,MOV,VOD // DRA: Draft, COM: Completed, IPR: In Progress
    }

    public enum KitChenStatus {
        WTP, DCP, BPR, NSK, KOS, PRD, DMC // WTP: Waiting To Prepare, DCP: Disinfecting Completed, BPR: Being Prepared, NSK: No Show Kitchen, KOS: Kitchen Order Served, PRD: Prepared, DMC: Disinfecting Missed
    }

    public enum TableStatus {
        TIU, TBD, ETB // TIU: Table In Use, TBD: Table Being Disinfected, ETB: Empty Table
    }

    public enum TableReservationStatus{
        CAN,PSB,NOS,TBL,TRC //CAN: Canceled, PSB: Pending, NOS: No Show, TBL: Table, TRC: Takeaway Reservation Confirmed
    }

}
