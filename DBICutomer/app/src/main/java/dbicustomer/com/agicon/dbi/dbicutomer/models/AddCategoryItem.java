package dbicustomer.com.agicon.dbi.dbicutomer.models;

import java.io.Serializable;

/**
 * Created by agicon06 on 17/8/17.
 */

public class AddCategoryItem implements Serializable {

    private String new_item_name;
    private String new_item_price;
    private String new_item_quantity;
    private String new_item_total_price;
    private String new_item_key_id;
    private String new_item_total_price_included_tax;

    public AddCategoryItem() {
    }

    public AddCategoryItem(String new_item_name, String new_item_price, String new_item_quantity, String new_item_total_price,
                           String new_item_key_id) {
        this.new_item_name = new_item_name;
        this.new_item_price = new_item_price;
        this.new_item_quantity = new_item_quantity;
        this.new_item_total_price = new_item_total_price;
        this.new_item_key_id = new_item_key_id;
    }

    public AddCategoryItem(String new_item_name, String new_item_price, String new_item_quantity, String new_item_total_price,
                           String new_item_key_id, String new_item_total_price_included_tax) {
        this.new_item_name = new_item_name;
        this.new_item_price = new_item_price;
        this.new_item_quantity = new_item_quantity;
        this.new_item_total_price = new_item_total_price;
        this.new_item_key_id = new_item_key_id;
        this.new_item_total_price_included_tax = new_item_total_price_included_tax;
    }

    public String getNew_item_total_price_included_tax() {
        return new_item_total_price_included_tax;
    }

    public void setNew_item_total_price_included_tax(String new_item_total_price_included_tax) {
        this.new_item_total_price_included_tax = new_item_total_price_included_tax;
    }

    public String getNew_item_name() {
        return new_item_name;
    }

    public void setNew_item_name(String new_item_name) {
        this.new_item_name = new_item_name;
    }

    public String getNew_item_price() {
        return new_item_price;
    }

    public void setNew_item_price(String new_item_price) {
        this.new_item_price = new_item_price;
    }

    public String getNew_item_quantity() {
        return new_item_quantity;
    }

    public void setNew_item_quantity(String new_item_quantity) {
        this.new_item_quantity = new_item_quantity;
    }

    public String getNew_item_total_price() {
        return new_item_total_price;
    }

    public void setNew_item_total_price(String new_item_total_price) {
        this.new_item_total_price = new_item_total_price;
    }

    public String getNew_item_key_id() {
        return new_item_key_id;
    }

    public void setNew_item_key_id(String new_item_key_id) {
        this.new_item_key_id = new_item_key_id;
    }
}