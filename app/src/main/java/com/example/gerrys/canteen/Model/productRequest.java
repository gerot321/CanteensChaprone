package com.example.gerrys.canteen.Model;



public class productRequest {
    private String productname;
    private String requestid;
    private String merchantid;
    private String productid;
    private String quantity;
    private String totalprice;
    private String address;

    public productRequest(){

    }

    public productRequest(String merchantid, String productid, String productname,String quantity,String totalprice,String address) {
       this.merchantid=merchantid;
       this.productid=productid;
       this.productid=productname;
       this.quantity=quantity;
       this.totalprice=totalprice;
       this.address=address;
    }


    public String getAddress() {
        return address;
    }

    public String getMerchantid() {
        return merchantid;
    }

    public String getProductid() {
        return productid;
    }

    public String getProductname() {
        return productname;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMerchantid(String merchantid) {
        this.merchantid = merchantid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }
}
