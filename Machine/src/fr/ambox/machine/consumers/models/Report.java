package fr.ambox.machine.consumers.models;

public class Report extends Model {
    public String status;
    public String stdout;
    public String stderr;
    public String metadata;
    public String programState;
    public long processingTime;
    public int jobId;

    public Report(Job job) {
        super();
        this.jobId = job.id;
    }

    public Report(int jobId) {
        super();
        this.jobId = jobId;
    }
}
