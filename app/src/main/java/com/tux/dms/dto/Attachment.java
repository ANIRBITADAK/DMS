package com.tux.dms.dto;

public class Attachment {

    private String attachmentPath;
    private int attachmentId;
    private String type;

    public Attachment(String attachmentPath, int attachmentId, String type) {
        this.attachmentPath = attachmentPath;
        this.attachmentId = attachmentId;
        this.type = type;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachment_path) {
        this.attachmentPath = attachment_path;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachment_id) {
        this.attachmentId = attachment_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}
