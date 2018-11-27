package com.crazy_iter.ishada;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;

import com.crazy_iter.ishada.Models.MeetingModel;
import com.crazy_iter.ishada.Models.TaskModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StaticMethods {

    public static ArrayList<TaskModel> tasks = null;
    public static ArrayList<MeetingModel> meetings = null;

    public static boolean isUserAdmin(Activity activity) {
        SharedPreferences perfs = activity.getSharedPreferences("Ishada", Context.MODE_PRIVATE);
        return perfs.getString("role", "").length() != 0
                && perfs.getString("role", "").contains("admin");
    }

    public static boolean logout(Activity activity) {
        try {
            SharedPreferences.Editor perfs = activity.getSharedPreferences("Ishada", Context.MODE_PRIVATE).edit();
            perfs.putString("userID", "");
            perfs.putString("role", "");
            perfs.putString("name", "");
            perfs.apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getMonthFromNumber(int num) {
        switch (num) {
            case 1: return "January";
            case 2: return "February";
            case 3: return "March";
            case 4: return "April";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "August";
            case 9: return "September";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
        }
        return "";
    }

    public static int getMonthFromName(String name) {
        switch (name) {
            case "January": return 1;
            case "February": return 2;
            case "March": return 3;
            case "April": return 4;
            case "May": return 5;
            case "June": return 6;
            case "July": return 7;
            case "August": return 8;
            case "September": return 9;
            case "October": return 10;
            case "November": return 11;
            case "December": return 12;
        }
        return 0;
    }

    public static DatePickerDialog createDialogWithoutDateField(Context context, int year, int month) {
        DatePickerDialog dpd = new DatePickerDialog(context, null, year, month, 1);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields =
                            datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return dpd;
    }

    public static void hideRightAnim(Context context, View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.hide_right));
            view.setVisibility(View.GONE);
        }
    }

    public static void showRightAnim(Context context, View view) {
        if (view.getVisibility() == View.GONE) {
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_right));
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void changeTaskStatus(@Nullable String id, @NotNull String status) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(id)) {
                tasks.get(i).setStatus(status);
            }
        }
    }

    public static void setTaskUser(@NotNull JSONObject json) throws JSONException {
        String taskID = json.getString("taskID");
        ArrayList<String> users = new ArrayList<>();
        for (int i = 0 ; i < json.getJSONArray("users").length(); i++) {
            users.add(json.getJSONArray("users").getJSONObject(i).getString("userID"));
        }

        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(taskID)) {
                tasks.get(i).setTaskUser(users);
            }
        }

    }

    public static boolean isEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
