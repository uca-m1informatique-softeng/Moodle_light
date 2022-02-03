package Model.user;

import Model.ActionsManager;

public class Student {
    String name;
    String id;
    ActionsManager actionManager;

    public Student(String name, String id, ActionsManager actionManager) {
        this.name = name;
        this.id = id;
        this.actionManager = actionManager;
    }
}
