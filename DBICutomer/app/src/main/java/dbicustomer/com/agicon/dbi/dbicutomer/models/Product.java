package dbicustomer.com.agicon.dbi.dbicutomer.models;

import java.io.Serializable;

public class Product implements Serializable {

    private String product_id;
    private String product_name;
    private String product_price;
    private String product_discount;
    private String product_brand;
    private String product_quantity;
    private String product_total_price;
    private String product_barcode;
    private String cgst;
    private String sgst;
    private String cess;
    private String new_item_total_price_included_tax;

    public Product() {

    }

    public Product(String product_id, String product_name, String product_price, String product_discount,
                   String product_brand, String product_quantity, String product_total_price, String product_barcode,
                   String cgst, String sgst, String cess, String new_item_total_price_included_tax) {

        this.product_id = product_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_discount = product_discount;
        this.product_brand = product_brand;
        this.product_quantity = product_quantity;
        this.product_total_price = product_total_price;
        this.product_barcode = product_barcode;
        this.cgst = cgst;
        this.sgst = sgst;
        this.cess = cess;
        this.new_item_total_price_included_tax = new_item_total_price_included_tax;

    }

    public Product(String product_id, String product_name, String product_price, String product_brand,
                   String product_quantity, String product_price1, String product_id1, String cess) {

        this.product_id = product_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_brand = product_brand;
        this.product_quantity = product_quantity;
        this.product_price = product_price1;
        this.product_id = product_id1;
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

    public String getNew_item_total_price_included_tax() {
        return new_item_total_price_included_tax;
    }

    public void setNew_item_total_price_included_tax(String new_item_total_price_included_tax) {
        this.new_item_total_price_included_tax = new_item_total_price_included_tax;
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

    public String getProduct_brand() {
        return product_brand;
    }

    public void setProduct_brand(String product_brand) {
        this.product_brand = product_brand;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_total_price() {
        return product_total_price;
    }

    public void setProduct_total_price(String product_total_price) {
        this.product_total_price = product_total_price;
    }

    public String getProduct_barcode() {
        return product_barcode;
    }

    public void setProduct_barcode(String product_barcode) {
        this.product_barcode = product_barcode;
    }
}
