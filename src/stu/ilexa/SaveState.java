package stu.ilexa;

import java.util.ArrayList;

public class SaveState {
    private ArrayList<Interpreter> interpreters;
    private ArrayList<String> parameters;

    public SaveState(ArrayList<Interpreter> interpreters, ArrayList<String> parameters) {
        this.interpreters = interpreters;
        this.parameters = parameters;
    }

    public ArrayList<Interpreter> getInterpreters() {
        return interpreters;
    }

    public void setInterpreters(ArrayList<Interpreter> interpreters) {
        this.interpreters = interpreters;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }
}
