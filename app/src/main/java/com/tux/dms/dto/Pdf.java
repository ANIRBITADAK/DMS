package com.tux.dms.dto;

public class Pdf {

    private String pdf_path;
    private int pdf_id;


    public Pdf(String pdf_path, int pdf_id) {
        this.pdf_path = pdf_path;
        this.pdf_id = pdf_id;
    }

    public String getPdf_path() {
        return pdf_path;
    }

    public void setPdf_path(String pdf_path) {
        this.pdf_path = pdf_path;
    }

    public int getPdf_id() {
        return pdf_id;
    }

    public void setPdf_id(int pdf_id) {
        this.pdf_id = pdf_id;
    }







}
