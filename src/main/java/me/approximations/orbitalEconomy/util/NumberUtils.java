package me.approximations.orbitalEconomy.util;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NumberUtils {

    private static final Pattern PATTERN = Pattern.compile("^(\\d+\\.?\\d*)(\\D+)");

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###");
    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#.##");
    private static final List<String> formatos = Arrays.asList("", "k", "M", "B", "T", "Q", "QQ", "S", "SS", "OC", "N", "D", "UN", "DD", "TR", "SD", "SPD", "OD", "ND", "VG", "UVG", "DVG", "TVG", "QTV", "QNV", "SEV", "SPV", "OVG", "NVG", "TG");

    public static String format(double value, boolean arredondar) {
        if (isInvalid(value)) return "0";

        if(value < 1000000.0) {
            return DECIMAL_FORMAT.format(value);
        }
//        if (MessageValue.get(MessageValue::formatType).equalsIgnoreCase("DECIMAL")) {
//            return DECIMAL_FORMAT.format(value);
//        }

        int index = 0;
        List<String> format = formatos;

        double tmp;
        while ((tmp = value / 1000) >= 1) {
            if (index + 1 == format.size()) break;
            value = tmp;
            ++index;
        }

        return arredondar ? NUMBER_FORMAT.format(Math.floor(value)) + format.get(index) : NUMBER_FORMAT.format(value) + format.get(index);
    }

    public static String format(double value) {
        return format(value, false);
    }

    public static double parse(String string) {
        try {

            double value = Double.parseDouble(string);
            return isInvalid(value) ? -1 : value;

        } catch (Exception ignored) {
        }

//        if (MessageValue.get(MessageValue::formatType).equalsIgnoreCase("DECIMAL")) return 0;

        Matcher matcher = PATTERN.matcher(string);
        if (!matcher.find()) return 0;

        double amount = Double.parseDouble(matcher.group(1));
        String suffix = matcher.group(2);
        String fixedSuffix = suffix.equalsIgnoreCase("k") ? suffix.toLowerCase() : suffix.toUpperCase();

        int index = formatos.indexOf(fixedSuffix);

        double value = amount * Math.pow(1000, index);
        return isInvalid(value) ? -1 : value;
    }

    public static boolean isInvalid(double value) {
        return value < 0 || Double.isNaN(value) || Double.isInfinite(value);
    }

}