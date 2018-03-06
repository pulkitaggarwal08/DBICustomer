package dbicustomer.com.agicon.dbi.dbicutomer.models;

/**
 * Created by agicon06 on 8/8/17.
 */

public class UserProduct {

    private String user_id;
    private String product_name;
    private String total_item_price;
    private String product_qty;
    private String unit_price;
    private String order_id;

    private String user_name;
    private String phone;
    private String order_date;
    private String subTotal;

    public UserProduct(String product_name, String total_item_price, String product_qty, String unit_price) {

        this.product_name = product_name;
        this.total_item_price = total_item_price;
        this.product_qty = product_qty;
        this.unit_price = unit_price;

    }

    public UserProduct(String product_name, String total_item_price, String product_qty, String unit_price, String order_id,
                       String order_date) {

        this.product_name = product_name;
        this.total_item_price = total_item_price;
        this.product_qty = product_qty;
        this.unit_price = unit_price;
        this.order_id = order_id;
        this.order_date = order_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getTotal_item_price() {
        return total_item_price;
    }

    public void setTotal_item_price(String total_item_price) {
        this.total_item_price = total_item_price;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }
}
