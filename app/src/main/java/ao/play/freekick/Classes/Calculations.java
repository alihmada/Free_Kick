package ao.play.freekick.Classes;

import java.time.Duration;

import ao.play.freekick.Models.Common;

public class Calculations {

    public static double sum(double firstNumber, double secondNumber) {
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

    public static String priceCalculator(boolean isSolo, Duration timeElapsed) {
        if (isSolo) {
            return String.valueOf(Calculations.mul(Calculations.div(timeElapsed.toMinutes(), Common.HOUR), Common.HOUR_PRICE));
        } else {
            return String.valueOf(Calculations.mul(Calculations.div(timeElapsed.toMinutes(), Common.HOUR), mul(Common.HOUR_PRICE, 2)));
        }
    }

}
