package dbicustomer.com.agicon.dbi.dbicutomer.models;

/**
 * Created by agicon06 on 31/7/17.
 */

public class InvoiceList {

    private String invoice_id;
    private String shop_name;
    private String order_date;
    private String order_status;
    private String order_amount;
    private String store_id;

    public InvoiceList(String invoice_id, String shop_name, String order_date, String order_status, String order_amount, String store_id) {

        this.invoice_id = invoice_id;
        this.shop_name = shop_name;
        this.order_date = order_date;
        this.order_status = order_status;
        this.order_amount = order_amount;
        this.store_id = store_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }
}
