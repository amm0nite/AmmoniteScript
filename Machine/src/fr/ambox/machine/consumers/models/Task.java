package fr.ambox.machine.consumers.models;

import fr.ambox.program.Program;

public class Task {
    public String type;
    public Program program;
    public int stopJobId;

    public Task(Program program) {
        this.type = "program";
        this.program = program;
    }
}
