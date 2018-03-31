package com.example.gerrys.canteen.Model;

public class User {
    private String name;
    private String Password;
    private String Phone;
    private String Status;
    private String saldo;
    private String address;
    private String gender;
    private String tanggalLahir;
    private String hobi;
    private String profesi;
    private String image;
    public User(){

    }

    public User(String name, String password,String status,String saldo,String address,String gender,String tanggalLahir,String hobi, String profesi,String image) {
        this.name = name;
        Password = password;
        Status = status;
        this.saldo=saldo;
        this.address=address;
        this.gender=gender;
        this.tanggalLahir=tanggalLahir;
        this.hobi=hobi;
        this.profesi=profesi;
        this.image=image;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public String getImage() {
        return image;
    }

    public String getHobi() {
        return hobi;
    }

    public String getProfesi() {
        return profesi;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHobi(String hobi) {
        this.hobi = hobi;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setProfesi(String profesi) {
        this.profesi = profesi;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }
    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getSaldo() {
        return saldo;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
    public String getUserStatus() {
        return Status;
    }

    public void setUserStatus(String status) {
        Status = status;
    }
}
