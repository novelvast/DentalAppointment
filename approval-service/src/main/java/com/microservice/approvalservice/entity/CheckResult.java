package com.microservice.approvalservice.entity;

import lombok.Data;

@Data
public class CheckResult {
    public String status;
    public String notes;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
