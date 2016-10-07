package com.example.bartv.mail.Database;

/**
 * Created by bartv on 24-9-2016.
 */

public class dbProperties {
    private int id;
    private String name;
    private String studentnumber;
    private String email;
    private String image;
    private String klas;
    private String zipcode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentnumber() {
        return studentnumber;
    }

    public void setStudentnumber(String studentnumber) {
        this.studentnumber = studentnumber;
    }

    public String getKlas() {
        return klas;
    }

    public void setKlas(String klas) {
        this.klas = klas;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
         this.email = email;
    }

    public dbProperties(int id, String name, String studentnumber, String email, String image, String klas, String zipcode) {
        this.id = id;
        this.name = name;
        this.studentnumber = studentnumber;
        this.email = email;
        this.image = image;
        this.klas = klas;
        this.zipcode = zipcode;
    }
}
