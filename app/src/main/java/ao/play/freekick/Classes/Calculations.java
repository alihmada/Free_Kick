package ao.play.freekick.Classes;

import java.time.Duration;

import ao.play.freekick.Models.Common;

public class Calculations {

    public static int sum(int firstNumber, int secondNumber) {
        return firstNumber + secondNumber;
    }

    public static double sub(double firstNumber, double secondNumber) {
        return firstNumber - secondNumber;
    }

    public static double mul(double firstNumber, double secondNumber) {
        return firstNumber * secondNumber;
    }

    public static double div(double firstNumber, double secondNumber) {
        return firstNumber / secondNumber;
    }

    public static double mod(double firstNumber, double secondNumber) {
        return firstNumber % secondNumber;
    }

    public static boolean isEven(int number) {
        return mod(number, 2) == 0;
    }

    public static String round(String number) {
        return String.valueOf((double) Math.round(Double.parseDouble(number)));
    }

    public static String priceCalculator(boolean isSolo, Duration timeElapsed) {
        double minutes = timeElapsed.toMinutes();
        double hourPrice = isSolo ? Common.HOUR_PRICE : Common.HOUR_PRICE * 2;
        return String.valueOf(mul(div(minutes, Common.HOUR), hourPrice));
    }
}