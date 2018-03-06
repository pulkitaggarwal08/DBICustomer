package dbicustomer.com.agicon.dbi.dbicutomer.models;

public class CategoryList {

    private String store_id;
    private String category_id;
    private String category_name;

    public CategoryList(String store_id, String category_id, String category_name) {
        this.store_id=store_id;
        this.category_id = category_id;
        this.category_name = category_name;
    }

    public CategoryList(String category_id, String category_name) {
        this.store_id=store_id;
        this.category_id = category_id;
        this.category_name = category_name;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
