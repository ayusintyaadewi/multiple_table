package com.example.asus.tugas_3.Features.BukuCRUD.CreateBuku;

public class Buku {
    private long id;
    private String name;
    private int code;
    private int credit;

    public Buku(long id, String name, int code, int credit) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.credit = credit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
