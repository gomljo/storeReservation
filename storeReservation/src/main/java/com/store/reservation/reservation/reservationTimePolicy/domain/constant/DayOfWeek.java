package com.store.reservation.reservation.reservationTimePolicy.domain.constant;

public enum DayOfWeek {
    MONDAY(1, "월요일", "monday"),
    TUESDAY(2, "화요일", "tuesday"),
    WEDNESDAY(3,"수요일","wednesday"),
    THURSDAY(4,"목요일", "thursday"),
    FRIDAY(5, "금요일", "friday"),
    SATURDAY(6,"토요일", "saturday"),
    SUNDAY(7,"일요일", "sunday");

    private final int order;
    private final String koreanDayOfWeek;
    private final String englishDayOfWeek;

    DayOfWeek(int order, String koreanDayOfWeek, String englishDayOfWeek){
        this.order = order;
        this.koreanDayOfWeek = koreanDayOfWeek;
        this.englishDayOfWeek = englishDayOfWeek;
    }

    public int getOrder() {
        return order;
    }

    public String getKoreanDayOfWeek() {
        return koreanDayOfWeek;
    }

    public String getEnglishDayOfWeek() {
        return englishDayOfWeek;
    }
}
