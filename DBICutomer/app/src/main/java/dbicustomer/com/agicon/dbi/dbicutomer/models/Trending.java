package dbicustomer.com.agicon.dbi.dbicutomer.models;

/**
 * Created by agicon06 on 31/7/17.
 */

public class Trending {

    private String store_name;
    private String store_discount;
    private String store_picture;

    public Trending(String store_name, String store_discount, String store_picture) {
        this.store_name = store_name;
        this.store_discount = store_discount;
        this.store_picture = store_picture;
    }

    public String getStore_picture() {
        return store_picture;
    }

    public void setStore_picture(String store_picture) {
        this.store_picture = store_picture;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_discount() {
        return store_discount;
    }

    public void setStore_discount(String store_discount) {
        this.store_discount = store_discount;
    }
}
