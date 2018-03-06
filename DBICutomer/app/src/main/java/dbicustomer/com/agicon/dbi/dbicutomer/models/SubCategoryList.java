package dbicustomer.com.agicon.dbi.dbicutomer.models;

/**
 * Created by agicon06 on 31/7/17.
 */

public class SubCategoryList {

    private String subcategory_id;
    private String subcategory_name;
    private String category_id;

    public SubCategoryList(String subcategory_id,String subcategory_name) {
        this.subcategory_id = subcategory_id;
        this.subcategory_name = subcategory_name;
    }


    public String getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(String subcategory_id) {
        this.subcategory_id = subcategory_id;
    }

    public String getSubcategory_name() {
        return subcategory_name;
    }

    public void setSubcategory_name(String subcategory_name) {
        this.subcategory_name = subcategory_name;
    }

}
