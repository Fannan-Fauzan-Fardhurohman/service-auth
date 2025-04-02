package com.fanxan.serviceauth.utils;

import com.fanxan.serviceauth.model.timezone.TimezoneJsonModel;
import com.fanxan.serviceauth.model.timezone.TimezoneModel;
import com.fanxan.serviceauth.utils.enumeration.OffsetBase;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private final static long SECOND_MILLIS = 1000;
    private final static long MINUTE_MILLIS = SECOND_MILLIS * 60;
    private final static long HOUR_MILLIS = MINUTE_MILLIS * 60;
    private final static long DAY_MILLIS = HOUR_MILLIS * 24;
    private final static long YEAR_MILLIS = DAY_MILLIS * 365;

    private static final Map<String, TimezoneJsonModel> TIMEZONE_REPOSITORY;

    private static final List<TimezoneJsonModel> TIMEZONE_LISTS;

    static {
        String timezoneJSON = ResourceUtils.readFileToString("timezone.json");
        TIMEZONE_LISTS = Collections.unmodifiableList(new Gson().fromJson(timezoneJSON, new TypeToken<List<TimezoneJsonModel>>() {
        }.getType()));

        Map<String, TimezoneJsonModel> mapper = new HashMap<>();
        TIMEZONE_LISTS.forEach(timezoneModel -> mapper.put(timezoneModel.getValue(), timezoneModel));
        TIMEZONE_REPOSITORY = Collections.unmodifiableMap(mapper);
    }

    private DateUtils() {
    }

    public static final DateTimeFormatter FULL_DATE = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss OOOO yyyy");

    /**
     * Convert {@link Date} to {@link LocalDate}.
     */
    public static LocalDate asLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Convert {@link LocalDate} to {@link Date}.
     */
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Convert {@link LocalDateTime} to {@link Date}.
     */
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date addHoursToDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static Date addMinuteToDate(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static Date addSecondsToDate(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    public static Date addDaysToDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static Date addWeeksToDate(Date date, int weeks) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_WEEK, weeks);
        return calendar.getTime();
    }

    public static Date addMonthToDate(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    public static Date addYearToDate(Date date, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

    public static Date addHoursAndMinuteToDate(Date date, int hours, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static Date addHoursAndMinuteAndSecondToDate(Date date, int hours, int minutes, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        calendar.add(Calendar.MINUTE, minutes);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    public static Timestamp toTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    public static Timestamp toTimestamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }

    public static Date getDaysAgoOrLater(int daysAgo) {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, daysAgo);
        return cal.getTime();
    }

    /**
     * Get the seconds difference
     */
    public static int secondsDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null)
            return 0;
        return (int) ((laterDate.getTime() / SECOND_MILLIS) - (earlierDate.getTime() / SECOND_MILLIS));
    }

    /**
     * Get the minutes difference
     */
    public static int minutesDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null)
            return 0;
        return (int) ((laterDate.getTime() / MINUTE_MILLIS) - (earlierDate.getTime() / MINUTE_MILLIS));
    }

    /**
     * Get the hours difference
     */
    public static int hoursDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null)
            return 0;
        return (int) ((laterDate.getTime() / HOUR_MILLIS) - (earlierDate.getTime() / HOUR_MILLIS));
    }

    /**
     * Get the days difference
     */

    public static boolean areDifferentDays(Date date1, Date date2, String timeZoneId) {
        ZoneId zoneId = ZoneId.of(timeZoneId);

        ZonedDateTime zdt1 = date1.toInstant().atZone(zoneId);
        ZonedDateTime zdt2 = date2.toInstant().atZone(zoneId);

        // Compare year, month, and day components in the specified timezone
        return zdt1.getYear() != zdt2.getYear() ||
                zdt1.getMonth() != zdt2.getMonth() ||
                zdt1.getDayOfMonth() != zdt2.getDayOfMonth();
    }

    public static int daysDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null)
            return 0;
        return (int) ((laterDate.getTime() / DAY_MILLIS) - (earlierDate.getTime() / DAY_MILLIS));
    }

    public static Date atStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    public static Date atStartOfDay(Date date, ZoneId zone) {
        LocalDateTime localDateTime = dateToLocalDateTime(date, zone);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay, zone);
    }

    public static Date atEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    public static Date atEndOfDay(Date date, ZoneId zone) {
        LocalDateTime localDateTime = dateToLocalDateTime(date, zone);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay, zone);
    }

    public static Date atStartOfWeek(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        LocalDateTime startOfCurrentWeek = localDateTime.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
        return localDateTimeToDate(startOfCurrentWeek);
    }

    public static Date atStartOfWeek(Date date, ZoneId zone) {
        LocalDateTime localDateTime = dateToLocalDateTime(date, zone);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        LocalDateTime startOfCurrentWeek = localDateTime.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
        return localDateTimeToDate(startOfCurrentWeek, zone);
    }

    public static Date atStartOfWeek(Date date, ZoneId zone, String regionId) {
        LocalDateTime localDateTime = dateToLocalDateTime(date, zone);
        DayOfWeek firstDayOfWeek = WeekFields.of(new Locale("en", regionId)).getFirstDayOfWeek();
        LocalDateTime startOfCurrentWeek = localDateTime.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
        return localDateTimeToDate(startOfCurrentWeek, zone);
    }

    public static Date atEndOfWeek(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        DayOfWeek lastDayOfWeek = firstDayOfWeek.plus(6);
        LocalDateTime endOfWeek = localDateTime.with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).with(LocalTime.MAX);
        return localDateTimeToDate(endOfWeek);
    }

    public static Date atEndOfWeek(Date date, ZoneId zone) {
        LocalDateTime localDateTime = dateToLocalDateTime(date, zone);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        DayOfWeek lastDayOfWeek = firstDayOfWeek.plus(6);
        LocalDateTime endOfWeek = localDateTime.with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).with(LocalTime.MAX);
        return localDateTimeToDate(endOfWeek, zone);
    }

    public static Date atEndOfWeek(Date date, ZoneId zone, String regionId) {
        LocalDateTime localDateTime = dateToLocalDateTime(date, zone);
        DayOfWeek firstDayOfWeek = WeekFields.of(new Locale("en", regionId)).getFirstDayOfWeek();
        DayOfWeek lastDayOfWeek = firstDayOfWeek.plus(6);
        LocalDateTime endOfWeek = localDateTime.with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).with(LocalTime.MAX);
        return localDateTimeToDate(endOfWeek, zone);
    }

    public static Date atStartOfMonth(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfMonth = localDateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        return localDateTimeToDate(startOfMonth);
    }

    public static Date atStartOfMonth(Date date, ZoneId zone) {
        LocalDateTime localDateTime = dateToLocalDateTime(date, zone);
        LocalDateTime startOfMonth = localDateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        return localDateTimeToDate(startOfMonth, zone);
    }

    public static Date atEndOfMonth(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfMonth = localDateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        return localDateTimeToDate(endOfMonth);
    }

    public static Date atEndOfMonth(Date date, ZoneId zone) {
        LocalDateTime localDateTime = dateToLocalDateTime(date, zone);
        LocalDateTime endOfMonth = localDateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        return localDateTimeToDate(endOfMonth, zone);
    }

    public static Date atStartOfYear(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfYear = localDateTime.with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
        return localDateTimeToDate(startOfYear);
    }

    public static Date atStartOfYear(Date date, ZoneId zone) {
        LocalDateTime localDateTime = dateToLocalDateTime(date, zone);
        LocalDateTime startOfYear = localDateTime.with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
        return localDateTimeToDate(startOfYear, zone);
    }

    public static Date atEndOfYear(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfYear = localDateTime.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
        return localDateTimeToDate(endOfYear);
    }

    public static Date atEndOfYear(Date date, ZoneId zone) {
        LocalDateTime localDateTime = dateToLocalDateTime(date, zone);
        LocalDateTime endOfYear = localDateTime.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
        return localDateTimeToDate(endOfYear, zone);
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static LocalDateTime dateToLocalDateTime(Date date, ZoneId zone) {
        return LocalDateTime.ofInstant(date.toInstant(), zone);
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime, ZoneId zone) {
        return Date.from(localDateTime.atZone(zone).toInstant());
    }

    public static Date stringToDate(String date, String pattern) throws Exception {
        return stringToDate(date, pattern, null);
    }

    public static Date stringToDate(String date, String pattern, TimeZone zone) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
        if (zone != null)
            formatter.setTimeZone(zone);
        return formatter.parse(date);
    }

    public static String toString(Date date, String pattern, ZoneId zone) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        df.setTimeZone(TimeZone.getTimeZone(zone));
        return df.format(date);
    }

    public static String toString(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        df.setTimeZone(TimeZone.getTimeZone("CET"));
        return df.format(date);
    }

    public static String toISO(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static String toISO(Date date, String timezone) {
        TimeZone tz = TimeZone.getTimeZone(timezone);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static Date currentTimezoneDate(String timezone) throws Exception {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        String dateString = formatter.format(date);
        return stringToDate(dateString, pattern);
    }

    public static String formatTimezoneDate(Date date, String timezone) throws Exception {
        String pattern = "dd MMM yyyy, HH:mm";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        return formatter.format(date);
    }

    public static String getCustomDate(LocalDate localDate) {
        String day = String.valueOf(localDate.getDayOfMonth());
        String month = String.valueOf(localDate.getMonthValue());
        if (day.length() == 1)
            day = "0" + day;
        if (month.length() == 1)
            month = "0" + month;
        return day + "/" + month;
    }

    public static String getInvoiceDate() {
        String pattern = "yyyyMMddhhmmss";
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        return formatter.format(date);
    }

    public static Long toMilliseconds(LocalDateTime localDateTime) {
        ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }

    public static List<TimezoneModel> getTimeZoneList(OffsetBase baseOffset) {
        return TIMEZONE_LISTS.stream().map(tz -> TimezoneModel.builder()
                        .code(tz.getValue())
                        .name(tz.getText())
                        .utc(tz.getUtc().toArray(new String[0]))
                        .offset(tz.getOffset())
                        .utcValue(tzUtcOffset(tz.getText()))
                        .build())
                .collect(Collectors.toList());
    }

    public static String tzUtcOffset(String utcOffset) {
        Pattern p = Pattern.compile("(?<=\\().+?(?=\\))");
        Matcher m = p.matcher(utcOffset);
        if (m.find()) {
            return m.group(0);
        }
        return utcOffset;
    }

    public static boolean timezoneValidation(String tz) {
        return TIMEZONE_REPOSITORY.containsKey(tz);
    }

    public static TimezoneJsonModel timezoneFind(String tz) {
        return TIMEZONE_REPOSITORY.get(tz);
    }

    public static String convertTimezoneToDate(String tz) {
        return null;
    }

    public static Date atStartOfTwoDaysAgo(Date date, ZoneId zone) {
        LocalDateTime localDateTime = dateToLocalDateTime(date, zone);
        LocalDateTime startOfTwoDaysAgo = localDateTime.minusDays(2);
        return localDateTimeToDate(startOfTwoDaysAgo, zone);
    }

    public static Date atStartOfYesterday(Date date, ZoneId zone) {
        LocalDateTime localDateTime = dateToLocalDateTime(date, zone);
        LocalDateTime startOfYesterday = localDateTime.minusDays(1);
        return localDateTimeToDate(startOfYesterday, zone);
    }

    public static Date convertTimeZone(Date date, ZoneId zone) {
        LocalDateTime localDateTime = dateToLocalDateTime(date, zone);
        return localDateTimeToDate(localDateTime, zone);
    }

    public static Date convertUtcToTimeZone(Date utcDate, String timeZoneId) {
        if (Objects.isNull(timeZoneId)) timeZoneId = "UTC";
        Instant instant = utcDate.toInstant();
        ZoneId zoneId = ZoneId.of(timeZoneId);
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date convertUtcToTimeZone(Date utcDate, ZoneId zone) {
        Instant instant = utcDate.toInstant();
        LocalDateTime localDateTime = instant.atZone(zone).toLocalDateTime();
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date atStartOfDay2(Date dateItemStart, ZoneId zoneId) {
        Date startAtZone = DateUtils.convertUtcToTimeZone(dateItemStart, zoneId);

        LocalDateTime localDateTime = dateToLocalDateTime(startAtZone);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay, zoneId);
    }

    public static Date atEndOfDay2(Date dateItemEnd, ZoneId zoneId) {
        Date endAtZone = DateUtils.convertUtcToTimeZone(dateItemEnd, zoneId);

        LocalDateTime localDateTime = dateToLocalDateTime(endAtZone);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay, zoneId);
    }

    public static Date atStartOfDay2(Long lDateItemStart, ZoneId zoneId) {
        Date dateItemStart = new Date(lDateItemStart);
        Date startAtZone = DateUtils.convertUtcToTimeZone(dateItemStart, zoneId);

        LocalDateTime localDateTime = dateToLocalDateTime(startAtZone);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay, zoneId);
    }

    public static Date atEndOfDay2(Long lDateItemEnd, ZoneId zoneId) {
        Date dateItemEnd = new Date(lDateItemEnd);
        Date endAtZone = DateUtils.convertUtcToTimeZone(dateItemEnd, zoneId);

        LocalDateTime localDateTime = dateToLocalDateTime(endAtZone);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay, zoneId);
    }

    public static String getHourInAmOrPm(Timestamp timestamp, String timezone) {
        return new SimpleDateFormat("hh:mm a").format(DateUtils.convertUtcToTimeZone(timestamp, timezone));
    }


    public static Date atStartOfDayExact(Date dateItemStart) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateItemStart);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date atEndOfDayExact(Date dateItemEnd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateItemEnd);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
}