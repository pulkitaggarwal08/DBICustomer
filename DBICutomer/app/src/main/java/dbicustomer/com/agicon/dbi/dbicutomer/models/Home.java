package dbicustomer.com.agicon.dbi.dbicutomer.models;

public class Home {

    private String store_type_id;
    private String store_type_name;
    private String city_name;
    private String bills_uploaded;
    private String total_spending;
    private String total_orders;
    private String offer_date;
    private String image;

    public Home(String store_type_id, String store_type_name, String city_name
            , String bills_uploaded, String total_spending, String total_orders, String offer_date) {

        this.store_type_id = store_type_id;
        this.store_type_name = store_type_name;
        this.city_name = city_name;
        this.bills_uploaded = bills_uploaded;
        this.total_spending = total_spending;
        this.total_orders = total_orders;
        this.offer_date = offer_date;

    }

    public Home(String image) {

        this.image = image;
    }

    public String getStore_type_id() {
        return store_type_id;
    }

    public void setStore_type_id(String store_type_id) {
        this.store_type_id = store_type_id;
    }

    public String getStore_type_name() {
        return store_type_name;
    }

    public void setStore_type_name(String store_type_name) {
        this.store_type_name = store_type_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getBills_uploaded() {
        return bills_uploaded;
    }

    public void setBills_uploaded(String bills_uploaded) {
        this.bills_uploaded = bills_uploaded;
    }

    public String getTotal_spending() {
        return total_spending;
    }

    public void setTotal_spending(String total_spending) {
        this.total_spending = total_spending;
    }

    public String getTotal_orders() {
        return total_orders;
    }

    public void setTotal_orders(String total_orders) {
        this.total_orders = total_orders;
    }

    public String getOffer_date() {
        return offer_date;
    }

    public void setOffer_date(String offer_date) {
        this.offer_date = offer_date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
