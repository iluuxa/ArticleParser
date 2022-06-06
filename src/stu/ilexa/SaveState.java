package stu.ilexa;

import java.util.ArrayList;
import java.util.TreeSet;

public class SaveState {
    private ArrayList<Interpreter> interpreters;
    private ArrayList<String> parameters;

    private TreeSet<String> deletedParameters;

    public SaveState(ArrayList<Interpreter> interpreters, ArrayList<String> parameters,TreeSet<String> deletedParameters) {
        this.interpreters = interpreters;
        this.parameters = parameters;
        this.deletedParameters=deletedParameters;
    }

    public TreeSet<String> getDeletedParameters() {
        return deletedParameters;
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
