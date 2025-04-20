package com.itwill.attendance.LeaveStatus;

public enum LeaveStatus {
	
	 ANNUAL("연차"),
	    HALF_DAY("반차"),
	    SICK("병가");

	    private final String label;

	    LeaveStatus(String label) {
	        this.label = label;
	    }

	    public String getLabel() {
	        return label;
	    }

}
