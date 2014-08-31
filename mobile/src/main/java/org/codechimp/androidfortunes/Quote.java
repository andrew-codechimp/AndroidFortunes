package org.codechimp.androidfortunes;

import com.google.gson.annotations.Expose;

public class Quote {
    @Expose
    private String id;

    @Expose
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content= content;
    }

}
