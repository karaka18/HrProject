package com.itwill.attendance.LeaveStatus;

public enum AbsenceType {
	
	SICK("병결"),
    HOLIDAY("공휴일"),
    UNAUTHORIZED("무단"),
    OTHER("그 외");

    private final String label;

    AbsenceType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
