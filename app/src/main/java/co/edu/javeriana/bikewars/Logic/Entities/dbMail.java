package co.edu.javeriana.bikewars.Logic.Entities;

/**
 * Created by Todesser on 03/11/2017.
 */

public class dbMail {
    private String from;
    private String to;
    private String message;
    private long date;
    private boolean processed;

    public dbMail() {
    }

    public dbMail(String from, String to, String message, long date, boolean processed) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.processed = processed;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
