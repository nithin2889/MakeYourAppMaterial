package com.learnwithme.xyzreader.utils;

import android.text.Html;
import android.text.Spanned;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringUtils {

    private final static SimpleDateFormat oldFormat
            = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);

    private final static SimpleDateFormat newFormat
            = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

    public static Spanned getFormattedBookText(String text) {

        String cleanedText = text
                .replaceAll("(?<!\\r\\n)(\\r\\n)(?!\\r\\n)", " ")
                .replaceAll("(\\r\\n|\\n)", "<br />");

        Spanned result;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(cleanedText, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(cleanedText);
        }
        return result;
    }

    public static String getFormattedDetailsSubtitle(String date, String author) {

        return " by " + author + " published on: "
                + getFormattedDate(date);
    }

    public static Spanned getFormattedSubtitle(String date, String author) {

        String html = " by " + author + "<br/>" + getFormattedDate(date);

        Spanned result;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    private static String getFormattedDate(String rawDate) {

        Date releaseDate = new Date();

        try {
            releaseDate = oldFormat.parse(rawDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newFormat.format(releaseDate);
    }
}