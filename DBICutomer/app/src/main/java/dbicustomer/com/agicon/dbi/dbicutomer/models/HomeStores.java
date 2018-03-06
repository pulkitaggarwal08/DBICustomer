package dbicustomer.com.agicon.dbi.dbicutomer.models;

public class HomeStores {

    private String store_id;
    private String store_name;
    private String store_address;
    private String store_city;
    private String store_state;
    private String store_country;
    private String store_zip;
    private String store_landline;
    private String store_mobile;
    private String store_image;

    public HomeStores(String store_id, String store_name, String store_address, String store_city, String store_state, String store_country
            , String store_zip, String store_landline, String store_mobile, String store_image) {

        this.store_id = store_id;
        this.store_name = store_name;
        this.store_address = store_address;
        this.store_city = store_city;
        this.store_state = store_state;
        this.store_country = store_country;
        this.store_zip = store_zip;
        this.store_landline = store_landline;
        this.store_mobile = store_mobile;
        this.store_image = store_image;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getStore_city() {
        return store_city;
    }

    public void setStore_city(String store_city) {
        this.store_city = store_city;
    }

    public String getStore_state() {
        return store_state;
    }

    public void setStore_state(String store_state) {
        this.store_state = store_state;
    }

    public String getStore_country() {
        return store_country;
    }

    public void setStore_country(String store_country) {
        this.store_country = store_country;
    }

    public String getStore_zip() {
        return store_zip;
    }

    public void setStore_zip(String store_zip) {
        this.store_zip = store_zip;
    }

    public String getStore_landline() {
        return store_landline;
    }

    public void setStore_landline(String store_landline) {
        this.store_landline = store_landline;
    }

    public String getStore_mobile() {
        return store_mobile;
    }

    public void setStore_mobile(String store_mobile) {
        this.store_mobile = store_mobile;
    }

    public String getStore_image() {
        return store_image;
    }

    public void setStore_image(String store_image) {
        this.store_image = store_image;
    }
}
