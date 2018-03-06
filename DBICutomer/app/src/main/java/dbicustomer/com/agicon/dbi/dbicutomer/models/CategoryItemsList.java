package dbicustomer.com.agicon.dbi.dbicutomer.models;

/**
 * Created by agicon06 on 4/8/17.
 */

public class CategoryItemsList {

    private String product_id;
    private String product_name;
    private String product_price;
    private String product_discount;
    private String product_barcode;
    private String product_brand;
    private String cgst;
    private String sgst;
    private String cess;

    public CategoryItemsList() {
    }

    public CategoryItemsList(String product_id, String product_name, String product_price, String product_discount,
                             String product_barcode, String product_brand) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_discount = product_discount;
        this.product_barcode = product_barcode;
        this.product_brand = product_brand;
    }

    public CategoryItemsList(String product_id, String product_name, String product_price, String product_discount,
                             String product_barcode, String product_brand, String cgst, String sgst, String cess) {

        this.product_id = product_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_discount = product_discount;
        this.product_barcode = product_barcode;
        this.product_brand = product_brand;
        this.cgst = cgst;
        this.sgst = sgst;
        this.cess = cess;

    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getCess() {
        return cess;
    }

    public void setCess(String cess) {
        this.cess = cess;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_discount() {
        return product_discount;
    }

    public void setProduct_discount(String product_discount) {
        this.product_discount = product_discount;
    }

    public String getProduct_barcode() {
        return product_barcode;
    }

    public void setProduct_barcode(String product_barcode) {
        this.product_barcode = product_barcode;
    }

    public String getProduct_brand() {
        return product_brand;
    }

    public void setProduct_brand(String product_brand) {
        this.product_brand = product_brand;
    }
}
