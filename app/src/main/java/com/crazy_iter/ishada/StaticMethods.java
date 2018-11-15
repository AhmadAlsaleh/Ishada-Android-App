package com.crazy_iter.ishada;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.crazy_iter.ishada.Models.TaskModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StaticMethods {

    public static ArrayList<TaskModel> tasks = null;

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
}
