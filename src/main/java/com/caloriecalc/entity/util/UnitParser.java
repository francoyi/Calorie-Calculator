package com.caloriecalc.entity.util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UnitParser {
    private static final Pattern AMOUNT_UNIT =
            Pattern.compile("(?i)(?:^|\\s)(\\d+(?:\\.\\d+)?)\\s*(g|ml)\\b");

    private UnitParser() {
    }

    public static Parsed parse(String input) {
        if (input == null) return null;
        String norm = input.trim().toLowerCase(Locale.ROOT).replaceAll(" +", " ");
        Matcher m = AMOUNT_UNIT.matcher(norm);
        double amount = Double.NaN;
        String unit = null;
        if (m.find()) {
            amount = Double.parseDouble(m.group(1));
            unit = m.group(2).toLowerCase(Locale.ROOT);
        }
        String name = norm.replaceAll(AMOUNT_UNIT.pattern(), "").trim();
        if (name.isEmpty()) name = norm;
        return new Parsed(name, amount, unit);
    }

    public record Parsed(String name, double amount, String unit) {
    }
}
