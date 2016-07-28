package mapQuchongTest;

/**
 * Created by yek on 2016-7-26.
 */
public class PageInfo {
    private String url;
    private String title;

    public PageInfo() {
    }

    public PageInfo(String url, String title) {

        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
