package com.crazy_iter.ishada;

public interface StaticVars {

    String name = "ishada";
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

    String MEETINGS = URL_BASE + "meetings/";
    String NEW_MEETING = MEETINGS + "newMeeting";
    String EDIT_MEETING = MEETINGS + "editMeeting";
    String DELETE_MEETING = MEETINGS + "deleteMeeting";

    // endregion

    /*
	"items" : [{
		"num" : 1,
		"subject" : "S1",
		"extra" : "E1",
	}]
     */
}
