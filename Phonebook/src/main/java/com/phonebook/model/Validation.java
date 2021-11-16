package com.phonebook.model;

public record Validation(boolean successful, String reportMessage) {

    public boolean isSuccessful() {
        return this.successful;
    }

    public String getReportMessage() {
        return this.reportMessage;
    }
}