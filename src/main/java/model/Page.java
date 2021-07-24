package model;

public class Page {
    private int id;
    private String path;
    private int code;
    private String content;

    public Page(int id, String path, int code, String content) {
        this.id = id;
        this.path = path;
        this.code = code;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
