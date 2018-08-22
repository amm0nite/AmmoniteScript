package fr.ambox.machine.consumers.models;

public class Job extends Model {
    public String name;
    public String hash;
    public String code;
    public String status;
    public int accountId;
    public int launch;

    public boolean shouldLaunch() {
        return this.launch > 0;
    }
}
