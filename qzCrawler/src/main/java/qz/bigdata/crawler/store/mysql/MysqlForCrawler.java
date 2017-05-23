package qz.bigdata.crawler.store.mysql;

import com.thoughtworks.xstream.XStream;
import qz.bigdata.crawler.core.UrlInfo;
import qz.bigdata.crawler.store.ISaveData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MysqlForCrawler implements ISaveData {
	
	private String crawlerName;
	public MysqlForCrawler (String crawlerName){
		this.crawlerName = crawlerName;
	}
	
	/**
	 * 根据构建当前对象时传入的爬虫名称构建数据表
	 * @return 创建表是否成功
	 */
	public boolean createPageDataTable(){
		Connection conn=null;
		Statement st=null;
		String tableName ="pagedata_"+crawlerName;
		String creatSql = "CREATE TABLE IF NOT EXISTS "+tableName+" (id INT PRIMARY KEY AUTO_INCREMENT ,url VARCHAR(10240) not null,parentUrl VARCHAR(10240),data text, updateDate datetime,updateVersion int)";
		
		try {
			conn = MysqlClient.getConnection();
			st = conn.createStatement();
			st.executeUpdate(creatSql);
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}finally{
			close(conn,st,null);
		}
		
	}
    /**
     * 删除表
     * @return 是否删除成功
     */
	public boolean dropPageDataTable(){
		Connection conn=null;
		Statement st=null;
		String tableName ="pagedata_"+crawlerName;
		String creatSql = "DROP TABLE IF EXISTS "+tableName+" ";
		
		try {
			conn = MysqlClient.getConnection();
			st = conn.createStatement();
			st.executeUpdate(creatSql);
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}finally{
			close(conn,st,null);
		}
		
	}

    /**
     * 储存页面信息
     * @param url 页面url地址
     * @param parentUrl  父页面url地址
     * @param data  页面内容
     * @return 数据储存成功与否
     */
	public boolean storePageInfo(String url, String parentUrl, String data){
		boolean isSuccess = false;
		boolean isExsit = checkPageExsit(url);
		if(isExsit){
			isSuccess = updatePageInfo(url,parentUrl,data);
		}else{
			isSuccess = insertPageInfo(url,parentUrl,data);
		}
		return isSuccess;
	}

    /**
     * 插入新的页面信息
     * @param url 页面url地址
     * @param parentUrl  父页面url地址
     * @param data  页面内容
     * @return 插入成功与否
     */
	public boolean insertPageInfo(String url, String parentUrl, String data){
		Connection conn=null;
		Statement st=null;
		String tableName ="pagedata_"+crawlerName;
		parentUrl = parentUrl==null?"":parentUrl;
		String insertSql = "INSERT INTO "+tableName+"(url,parentUrl,data,updateDate,updateVersion) value('"+url + "','" +parentUrl+"','" + data + "',now(),1)";
		try {
			conn = MysqlClient.getConnection();
			st = conn.createStatement();
			st.executeUpdate(insertSql);
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}finally{
			close(conn,st,null);
		}
	}

    /**
     * 根据url地址更新其内容
     * @param url 页面url地址
     * @param parentUrl  父页面url地址
     * @param data  页面内容
     * @return 是否更新成功
     */
	public boolean updatePageInfo(String url, String parentUrl, String data){
		Connection conn=null;
		Statement st=null;
		String tableName ="pagedata_"+crawlerName;
		String insertSql = "UPDATE "+tableName+" SET parentUrl='"+parentUrl+"',data='"+data+"',updateDate=now(),updateVersion=updateVersion+1 where url='"+url+"'";
		
		try {
			conn = MysqlClient.getConnection();
			st = conn.createStatement();
			st.executeUpdate(insertSql);
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}finally{
			close(conn,st,null);
		}
	}

    /**
     * 根据条件查询表中数据并返回页面内容对象
     * @param queryFactor 查询条件的map集合
     * @return 页面内容对象的集合
     */
	public List<PageInfo> queryPageInfo(Map<String,String> queryFactor){
		Connection conn=null;
		Statement st=null;
		ResultSet rs=null;
		List<PageInfo> pageList = new ArrayList<PageInfo>();
		StringBuffer whereSql = new StringBuffer();
		if(queryFactor.size()>0){
			Set<String> columns = queryFactor.keySet();
			for(String column : columns){
				String value = queryFactor.get(column);
				whereSql.append(" and ").append(column).append("='").append(value).append("'");
			}
		}
		try {
			conn = MysqlClient.getConnection();
			st = conn.createStatement();
			rs = queryUrl(conn,st,whereSql.toString());
			if(rs.next()){
				PageInfo pageInfo = new PageInfo();
				pageInfo.setUrl(rs.getString("url"));
				pageInfo.setParentUrl(rs.getString("parentUrl"));
				pageInfo.setData(rs.getString("data"));
				pageInfo.setUpdateDate(rs.getTimestamp("updateDate"));
				pageInfo.setUpdateVersion(rs.getInt("updateVersion"));
				pageList.add(pageInfo);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
			close(conn,st,rs);
		}
		
		return pageList;
	}

    /**
     * 根据条件删除表中数据
     * @param queryFactor 删除条件的map集合
     * @return 删除的条数
     */
	public int deletePageInfo(Map<String,String> queryFactor){
		Connection conn=null;
		Statement st=null;
		ResultSet rs=null;
		int delRow = 0;
		String tableName ="pagedata_"+crawlerName;
		String deleteSql = "delete FROM "+tableName+" where 1=1 ";
		StringBuffer whereSql = new StringBuffer();
		if(queryFactor.size()>0){
			Set<String> columns = queryFactor.keySet();
			for(String column : columns){
				String value = queryFactor.get(column);
				whereSql.append(" and ").append(column).append("='").append(value).append("'");
			}
		}
		try {
			conn = MysqlClient.getConnection();
			st = conn.createStatement();
			delRow = st.executeUpdate(deleteSql+whereSql.toString());
		}catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
			close(conn,st,rs);
		}
		
		return delRow;
	}

    /**
     * 根据url查询表中是否存在该条记录
     * @param url 页面url地址
     * @return 查询结果集
     */
	private boolean checkPageExsit(String url){
		Connection conn=null;
		Statement st=null;
		ResultSet rs=null;
		String whereSql = "";
		if(url!=null && !"".equals(url)){
			whereSql += " and url='"+url+"'";
		}
		boolean isExsit =false;
		try {
			conn = MysqlClient.getConnection();
			st = conn.createStatement();
			rs = queryUrl(conn,st,whereSql);
			if(rs.next()){
				isExsit = true;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
			close(conn,st,rs);
		}
		
		return isExsit;
	}
	
	private ResultSet queryUrl(Connection conn, Statement st,String whereSql){
		ResultSet rs =null;
		String tableName ="pagedata_"+crawlerName;
		String querySql = "SELECT * FROM "+tableName+" where 1=1 "+whereSql;
		try {
			rs = st.executeQuery(querySql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	} 
	
	
	private void close(Connection conn,Statement st,ResultSet rs){
		if(st != null){
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


    @Override
    public void saveData(UrlInfo urlInfo) throws Exception {
        XStream xstream = new XStream();
        String pageXml = xstream.toXML(urlInfo.data);
        this.storePageInfo(urlInfo.url.toString(), urlInfo.parentUrl == null?"":urlInfo.parentUrl.toString(), pageXml);
    }
}
