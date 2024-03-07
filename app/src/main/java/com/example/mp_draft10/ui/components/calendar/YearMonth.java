package com.example.mp_draft10.ui.components.calendar;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQuery;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;

public final class YearMonth implements Temporal, TemporalAdjuster, Comparable<java.time.YearMonth>, Serializable {
    YearMonth() {
        throw new RuntimeException("Stub!");
    }

    public static java.time.YearMonth now() {
        throw new RuntimeException("Stub!");
    }

    public static java.time.YearMonth now(ZoneId zone) {
        throw new RuntimeException("Stub!");
    }

    public static java.time.YearMonth now(Clock clock) {
        throw new RuntimeException("Stub!");
    }

    public static java.time.YearMonth of(int year, Month month) {
        throw new RuntimeException("Stub!");
    }

    public static java.time.YearMonth of(int year, int month) {
        throw new RuntimeException("Stub!");
    }

    public static java.time.YearMonth from(TemporalAccessor temporal) {
        throw new RuntimeException("Stub!");
    }

    public static java.time.YearMonth parse(CharSequence text) {
        throw new RuntimeException("Stub!");
    }

    public static java.time.YearMonth parse(CharSequence text, DateTimeFormatter formatter) {
        throw new RuntimeException("Stub!");
    }

    public boolean isSupported(TemporalField field) {
        throw new RuntimeException("Stub!");
    }

    public boolean isSupported(TemporalUnit unit) {
        throw new RuntimeException("Stub!");
    }

    public ValueRange range(TemporalField field) {
        throw new RuntimeException("Stub!");
    }

    public int get(TemporalField field) {
        throw new RuntimeException("Stub!");
    }

    public long getLong(TemporalField field) {
        throw new RuntimeException("Stub!");
    }

    public int getYear() {
        throw new RuntimeException("Stub!");
    }

    public int getMonthValue() {
        throw new RuntimeException("Stub!");
    }

    public Month getMonth() {
        throw new RuntimeException("Stub!");
    }

    public boolean isLeapYear() {
        throw new RuntimeException("Stub!");
    }

    public boolean isValidDay(int dayOfMonth) {
        throw new RuntimeException("Stub!");
    }

    public int lengthOfMonth() {
        throw new RuntimeException("Stub!");
    }

    public int lengthOfYear() {
        throw new RuntimeException("Stub!");
    }

    public java.time.YearMonth with(TemporalAdjuster adjuster) {
        throw new RuntimeException("Stub!");
    }

    public java.time.YearMonth with(TemporalField field, long newValue) {
        throw new RuntimeException("Stub!");
    }

    public java.time.YearMonth withYear(int year) {
        throw new RuntimeException("Stub!");
    }

    public java.time.YearMonth withMonth(int month) {
        throw new RuntimeException("Stub!");
    }

    public java.time.YearMonth plus(TemporalAmount amountToAdd) {
        throw new RuntimeException("Stub!");
    }

    public java.time.YearMonth plus(long amountToAdd, TemporalUnit unit) {
        throw new RuntimeException("Stub!");
    }

    public java.time.YearMonth plusYears(long yearsToAdd) {
        throw new RuntimeException("Stub!");
    }

    public java.time.YearMonth plusMonths(long monthsToAdd) {
        throw new RuntimeException("Stub!");
    }

    public java.time.YearMonth minus(TemporalAmount amountToSubtract) {
        throw new RuntimeException("Stub!");
    }

    public java.time.YearMonth minus(long amountToSubtract, TemporalUnit unit) {
        throw new RuntimeException("Stub!");
    }

    public java.time.YearMonth minusYears(long yearsToSubtract) {
        throw new RuntimeException("Stub!");
    }

    public java.time.YearMonth minusMonths(long monthsToSubtract) {
        throw new RuntimeException("Stub!");
    }

    public <R> R query(TemporalQuery<R> query) {
        throw new RuntimeException("Stub!");
    }

    public Temporal adjustInto(Temporal temporal) {
        throw new RuntimeException("Stub!");
    }

    public long until(Temporal endExclusive, TemporalUnit unit) {
        throw new RuntimeException("Stub!");
    }

    public String format(DateTimeFormatter formatter) {
        throw new RuntimeException("Stub!");
    }

    public LocalDate atDay(int dayOfMonth) {
        throw new RuntimeException("Stub!");
    }

    public LocalDate atEndOfMonth() {
        throw new RuntimeException("Stub!");
    }

    public int compareTo(java.time.YearMonth other) {
        throw new RuntimeException("Stub!");
    }

    public boolean isAfter(java.time.YearMonth other) {
        throw new RuntimeException("Stub!");
    }

    public boolean isBefore(java.time.YearMonth other) {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}

