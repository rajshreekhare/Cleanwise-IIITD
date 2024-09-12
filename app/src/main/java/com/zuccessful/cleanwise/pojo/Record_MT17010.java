package com.zuccessful.cleanwise.pojo;


import com.zuccessful.cleanwise.utilities.Utilities_MT17010;

public class Record_MT17010 {
    private String jobId;
    private String supervisorId;
    private Utilities_MT17010.Status status;

    public Record_MT17010(String jobId, String supervisorId, Utilities_MT17010.Status status) {
        this.jobId = jobId;
        this.supervisorId = supervisorId;
        this.status = status;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public Utilities_MT17010.Status getStatus() {
        return status;
    }

    public void setStatus(Utilities_MT17010.Status status) {
        this.status = status;
    }

    public Record_MT17010() {

    }
}
