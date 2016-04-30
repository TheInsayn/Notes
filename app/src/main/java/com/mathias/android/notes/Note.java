package com.mathias.android.notes;

/**
 * Created by Mathias.
 */
public class Note {
    private String mTitle;
    private String mText;
    private String mTimestamp;

    public Note(String title, String text, String timestamp) {
        mTitle = title;
        mText = text;
        mTimestamp = timestamp;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getText() {
        return mText;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setText(String text) {
        mText = text;
    }

    public void setTimestamp(String timestamp) {
        mTimestamp = timestamp;
    }
}
