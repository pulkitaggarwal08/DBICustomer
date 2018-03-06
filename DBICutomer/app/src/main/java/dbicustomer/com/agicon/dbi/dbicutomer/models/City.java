package dbicustomer.com.agicon.dbi.dbicutomer.models;

/**
 * Created by agicon06 on 28/9/17.
 */

public class City {

    private String id;
    private String name;


    public City(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

