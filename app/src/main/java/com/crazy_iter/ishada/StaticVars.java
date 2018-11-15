package com.crazy_iter.ishada;

public interface StaticVars {

    String name = "ishada";
<<<<<<< HEAD
String hi = "HI";
=======

String hello = "Hello";
>>>>>>> refs/remotes/origin/master
    // region APIs

    String URL_BASE = "https://ishada.herokuapp.com/";
    String TASKS_BASE = URL_BASE + "tasks/";
    String ASSIGN_TASK = TASKS_BASE + "assignTask/";
    String CHANGE_STATUS = TASKS_BASE + "changeStatus/";
    String DELETE_TASK = TASKS_BASE + "deleteTask/";
    String NEW_TASK = TASKS_BASE + "newTask/";

    String USERS_BASE = URL_BASE + "users/";
    String SIGN_IN = USERS_BASE + "signin/";
    String SIGN_UP = USERS_BASE + "signup/";
    String USERS_BY_IDs = USERS_BASE + "getUsersByIDs/";

    // endregion

}
