package com.phonebook.model;

public class Validation {
    private boolean successful;
    private String reportMessage;

    public Validation(boolean successful, String reportMessage)
    {
        this.successful = successful;
        this.reportMessage = reportMessage;
    }

    public boolean isSuccessful() {
        return this.successful;
    }

    public String getReportMessage() {
        return this.reportMessage;
    }
}