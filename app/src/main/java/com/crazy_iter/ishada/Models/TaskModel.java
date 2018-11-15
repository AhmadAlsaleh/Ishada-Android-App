package com.crazy_iter.ishada.Models;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.crazy_iter.ishada.R;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskModel implements Serializable {

    private String id;
    private String title;
    private String description;
    private String status;
    private String data;
    private ArrayList<String> taskUser = null;

    public TaskModel(String id, String title, String description, String status, String data, ArrayList<String> taskUser) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.data = data;
        this.taskUser = taskUser;
    }

    public ArrayList<String> getTaskUser() {
        return taskUser;
    }

    public void setTaskUser(ArrayList<String> taskUser) {
        this.taskUser = taskUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setTaskAsDone(@NotNull ImageView taskStatusIV) {
        taskStatusIV.setImageResource(R.drawable.ic_done);
        this.setStatus("Done");
    }

    public void setTaskAsFinished(@NotNull ImageView taskStatusIV) {
        taskStatusIV.setImageResource(R.drawable.ic_done_all);
        this.setStatus("Finish");
    }
}
