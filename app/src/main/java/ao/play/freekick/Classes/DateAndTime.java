package ao.play.freekick.Classes;

import android.annotation.SuppressLint;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateAndTime {
    @SuppressLint("SimpleDateFormat")
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
    static Calendar calendar = Calendar.getInstance();

    public static LocalDateTime getLocalTime() {
        return LocalDateTime.now();
    }

    public static String getCurrentTime() {
        return DateAndTime.timeFormatter(DateAndTime.getLocalTime());
    }


    public static LocalDateTime timeConverter(String timeWithFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
        return LocalDateTime.parse(timeWithFormat, formatter);
    }

    public static String setACustomTime(String startingTime, String customTime) {
        return timeFormatter(timeConverter(startingTime).plusMinutes(timeToMinutes(customTime)));
    }

    public static String setACustomTime(String startingTime, long customTime) {
        return timeFormatter(timeConverter(startingTime).plusMinutes(timeToMinutes(customTime)));
    }

    public static String priceDependentTiming(String startingTime, double minutes) {
        return timeFormatter(timeConverter(startingTime).plusSeconds((long) Calculations.mul(minutes, 60)));
    }

    public static long timeToMinutes(String time) {
        LocalTime parsingTime;
        try {
            parsingTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception parseException) {
            parsingTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("H:m"));
        }

        return (long) parsingTime.getHour() * 60 + parsingTime.getMinute();
    }

    private static long timeToMinutes(long time) {
        return time;
    }

    public static String timeFormatter(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a"));
    }

    public static String timeFormatter(int hours, int minutes) {
        return timeFormatter(LocalDateTime.of(LocalDate.now(), LocalTime.of(hours, minutes, 0)));
    }

    public static Duration timeDifference(String timeWithFormat) {
        return Duration.between(timeConverter(timeWithFormat), getLocalTime());
    }

    public static Duration timeDifference(String startingTimeWithFormat, String endingTimeWithFormat) {
        return Duration.between(timeConverter(startingTimeWithFormat), timeConverter(endingTimeWithFormat));
    }

    public static String convertToDuration(String timeString) {
        LocalTime localTime = LocalTime.parse(timeString);
        long seconds = localTime.toSecondOfDay();
        return Duration.ofSeconds(seconds).toString();
    }

    public static String durationToClockFormat(Duration timeElapsed) {
        return String.format("%s:%s:%s", String.format(timeElapsed.toHours() < 10 ? "0%s" : "%s", timeElapsed.toHours()), String.format((long) (Calculations.sub(timeElapsed.toMinutes(), Calculations.mul(timeElapsed.toHours(), 60))) < 10 ? "0%s" : "%s", (long) (Calculations.sub(timeElapsed.toMinutes(), Calculations.mul(timeElapsed.toHours(), 60)))), String.format((long) (Calculations.sub(timeElapsed.getSeconds(), Calculations.mul(timeElapsed.toMinutes(), 60))) < 10 ? "0%s" : "%s", (long) (Calculations.sub(timeElapsed.getSeconds(), Calculations.mul(timeElapsed.toMinutes(), 60)))));
    }

    public static String durationToClockFormat(String duration) {
        Duration timeElapsed = Duration.parse(duration);
        return String.format("%s:%s:%s", String.format(timeElapsed.toHours() < 10 ? "0%s" : "%s", timeElapsed.toHours()), String.format((long) (Calculations.sub(timeElapsed.toMinutes(), Calculations.mul(timeElapsed.toHours(), 60))) < 10 ? "0%s" : "%s", (long) (Calculations.sub(timeElapsed.toMinutes(), Calculations.mul(timeElapsed.toHours(), 60)))), String.format((long) (Calculations.sub(timeElapsed.getSeconds(), Calculations.mul(timeElapsed.toMinutes(), 60))) < 10 ? "0%s" : "%s", (long) (Calculations.sub(timeElapsed.getSeconds(), Calculations.mul(timeElapsed.toMinutes(), 60)))));
    }

    public static String durationPlus(String duration1, String duration2) {
        return String.valueOf(Duration.parse(duration1).plus(Duration.parse(duration2)));
    }

    public static String durationMinus(String duration1, String duration2) {
        return String.valueOf(Duration.parse(duration1).minus(Duration.parse(duration2)));
    }

    public static boolean isSpanningMultipleDays(String startingDateTime, String endingDateTime) {

        try {
            // Parse the starting and ending date/time strings
            Date startingDate = dateFormat.parse(startingDateTime);
            Date endingDate = dateFormat.parse(endingDateTime);

            // Get the date components of the starting and ending dates
            Calendar calendar = Calendar.getInstance();

            assert startingDate != null;
            calendar.setTime(startingDate);
            int startingDay = calendar.get(Calendar.DAY_OF_MONTH);

            assert endingDate != null;
            calendar.setTime(endingDate);
            int endingDay = calendar.get(Calendar.DAY_OF_MONTH);

            // Compare the days
            return startingDay != endingDay;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String[] getYesterday() {
        String[] date = new String[3];

        // Get the date of yesterday
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        date[0] = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        date[1] = String.valueOf(calendar.get(Calendar.MONTH) + 1); // Months are zero-based, so add 1
        date[2] = String.valueOf(calendar.get(Calendar.YEAR));

        return date;
    }

    public static String getYear() {
        return Integer.toString(LocalDate.now().getYear());
    }

    public static String getMonth() {
        return Integer.toString(LocalDate.now().getMonthValue());
    }

    public static String getDay() {
        return Integer.toString(LocalDate.now().getDayOfMonth());
    }

    public static int[] timeExtractor(String dateTime) {
        Date date;
        try {
            date = dateFormat.parse(dateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar cal = Calendar.getInstance();
        assert date != null;
        cal.setTime(date);
        return new int[]{cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)};
    }

    public static String getEnglishNameOfMonth() {
        return new DateFormatSymbols(new Locale("en")).getMonths()[getMonthNumber()];
    }

    public static String getEnglishNameOfDay() {
        return new DateFormatSymbols(new Locale("en")).getWeekdays()[getDayNumber()];
    }

    public static String getArabicNameOfMonth() {
        return new DateFormatSymbols(new Locale("ar")).getMonths()[getMonthNumber()];
    }

    public static String getArabicNameOfDay() {
        return new DateFormatSymbols(new Locale("ar")).getWeekdays()[getDayNumber()];
    }

    public static int getMonthNumber() {
        return calendar.get(Calendar.MONTH);
    }

    public static int getDayNumber() {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
