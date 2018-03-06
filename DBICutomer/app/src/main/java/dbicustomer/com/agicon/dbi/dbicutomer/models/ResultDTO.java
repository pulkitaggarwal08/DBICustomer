package dbicustomer.com.agicon.dbi.dbicutomer.models;

/**
 * Created by pulkit on 9/7/17.
 */

public class ResultDTO {

    private String contentResult;
    private String content_name;
    private String content_id;

    public ResultDTO(String content_name, String content_result) {
        this.content_name = content_name;
        this.contentResult = content_result;
    }

    public ResultDTO() {

    }

    public ResultDTO(String content_name, String content_result, String content_id) {
        this.content_name = content_name;
        this.contentResult = content_result;
        this.content_id = content_id;

    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getContent_name() {
        return content_name;
    }

    public void setContent_name(String content_name) {
        this.content_name = content_name;
    }

    public String getContentResult() {
        return contentResult;
    }

    public void setContentResult(String contentResult) {
        this.contentResult = contentResult;
    }
}
