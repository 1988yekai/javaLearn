package qz.bigdata.crawler.store.mysql;


import java.sql.Timestamp;

public class PageInfo {
	private String url;
	private String parentUrl;
	private String data;
	private Timestamp updateDate;
	private int updateVersion;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getParentUrl() {
		return parentUrl;
	}
	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public int getUpdateVersion() {
		return updateVersion;
	}
	public void setUpdateVersion(int updateVersion) {
		this.updateVersion = updateVersion;
	}
	public Timestamp getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
}
