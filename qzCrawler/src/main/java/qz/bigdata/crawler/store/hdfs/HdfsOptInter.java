package qz.bigdata.crawler.store.hdfs;

import java.io.IOException;
import java.util.List;


public interface HdfsOptInter {
	
	public boolean createDir(String path);
	
	public String upLoadFile(String localPath);
	
	public boolean downLoadFile(String hdfsPath, String localPath);
	
	public boolean deleteFile(String hdfsPath);
	
	public boolean appendFile(String hdfsPath, String appendContent, boolean isFile);
	
	public String upLoadPage(Object pageObj, String fileName) throws IOException;
	
	public Object downLoadPage(String filePath);
	
	public boolean renameFile(String oldFileName, String newFileName);
	
	public List<String> searchFile(String searchName, String fileType, String crawlerName);
	
	public List<String> getFileList(String dirPath);
	
	public void close();
	
	
}
