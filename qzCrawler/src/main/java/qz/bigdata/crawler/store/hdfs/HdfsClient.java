package qz.bigdata.crawler.store.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import qz.bigdata.crawler.configuration.GlobalOption;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class HdfsClient {
	
	private static String hdfsURL;
	private static String port;
	private static FileSystem fs;
	private static Configuration conf;
	static{
		try {
//			Properties prop = new Properties();
//			InputStream is = HdfsClient.class.getResourceAsStream("/conf/hdfs.properties");
//			prop.load(is);
//			hdfsURL = prop.getProperty(GlobalOption.hdfsIP);
//			port = prop.getProperty(String.valueOf(GlobalOption.hdfsPort));
			conf = new Configuration();
			conf.setBoolean("dfs.support.append", true);
            System.setProperty("hadoop.home.dir", "E:/hadoop-1.2.1");
			fs = FileSystem.get(URI.create(GlobalOption.hdfsIP+":"+GlobalOption.hdfsPort), conf);
//            fs = FileSystem.get(URI.create("192.168.13.25:9030"), conf);
			System.out.println("�½�һ��fs...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	//����Ŀ¼
	public boolean makeDir(String dir) throws IOException {
		Path path = new Path(dir);
		return fs.mkdirs(path);
	}  
	
	//���HDFS��ĳ���ļ��л��ļ��Ƿ����
	public boolean checkDir(String dir) throws IOException {
		Path path = new Path(dir);
		return fs.exists(path);
	}
	
	
	//�ϴ��ļ�
	 public void uploadFile(String filePath,String hdfsPath) throws IOException{
		 Path srcPath = new Path(filePath);
		 Path dstPath = new Path(hdfsPath);
		 fs.copyFromLocalFile(srcPath,dstPath);
	 }
	 
	//�����ļ�
	 public void downloadFile(String hdfsPath,String filePath) throws IOException{
		 Path srcPath = new Path(hdfsPath);
		 File file = new File(filePath);
	//	 fs.copyToLocalFile(srcPath, dstPath);  ������������������ļ�����һ��crc��׺���ļ�
		 FSDataInputStream is = fs.open(srcPath);
		 IOUtils.copyBytes(is, new FileOutputStream(file),4096, true);
	 }
	
	//�����ļ�
	 public void createFile(String filePath,String fileContent) throws IOException{
		 Path dst = new Path(filePath);
		 byte[] bytes = fileContent.getBytes("UTF-8");
		 FSDataOutputStream output = fs.create(dst);
		 output.write(bytes);
		 output.close();
	 }
	
	//ɾ���ļ�
	 public boolean deleteFile(String filePath) throws IOException{
		 Path dst = new Path(filePath);
		 boolean isExists = fs.exists(dst);
		 if(isExists){
			 boolean isdel = fs.delete(dst, true);
			 System.out.println(filePath+" has deleted,result is: "+isdel);
			 return isdel;
		 }else{
			 System.out.println(filePath+" is not exist!!");
			 return false;
		 }
	 }
	 
	 //��ȡ�ļ�
	 public byte[] readFile(String filePath) throws IOException{
		 Path dst = new Path(filePath);
		 FSDataInputStream fsIn = fs.open(dst);
		 FileStatus statu = fs.getFileStatus(dst);
		 byte [] bytes = new byte [Integer.parseInt(String.valueOf(statu.getLen()))];
		 fsIn.readFully(0, bytes);
		 fsIn.close();
		 return bytes;
	 }
	
	 //����HDFS��ĳ��·�����ļ��嵥
	 public FileStatus[] listFiles(String dirPath) throws IOException {
		 Path dst = new Path(dirPath);
		 FileStatus[] status = fs.listStatus(dst);
		 return status;
	 }
	
	 //����HDFS��ĳ��·�����ļ�������ַ���ļ�·��
	 public List<String> searchFile(List<String> list,String dirPath,String searchName) throws IOException {
		 Path dst = new Path(dirPath);
		 FileStatus[] status = fs.listStatus(dst);
		 for(FileStatus file : status){
			 if(file.isDir()){
				 String filePath = file.getPath().toString();
				 searchFile(list,filePath,searchName);
			 }else{
				 String fileName = file.getPath().getName();
				 if(fileName.contains(searchName)){
					 list.add(file.getPath().toUri().getPath());
				 }
			 }
		 }
		 return list;
	 }
	 
	 //����HDFS��ĳ��·�����ļ��б�
	 public List<String> getFileList(List<String> list,String dirPath) throws IOException {
		 Path dst = new Path(dirPath);
		 FileStatus[] status = fs.listStatus(dst);
		 for(FileStatus file : status){
			 if(file.isDir()){
				 String filePath = file.getPath().toString();
				 getFileList(list,filePath);
			 }else{
				 list.add(file.getPath().toUri().getPath());
			 }
		 }
		 return list;
	 }
	 
	//׷���ļ�������(��д�ļ����ݣ���ɺ���д��׷������)
	 public void appendFile(String filePath,String appendContent) throws IOException{
		 Path dst = new Path(filePath);
		 FSDataInputStream fsIn = fs.open(dst);
		 FSDataOutputStream fsOut = fs.create(dst);
		 int readbytes =0;
		 byte[] bytes = new byte[1024];
		 while((readbytes = fsIn.read(bytes))>0){
			 fsOut.write(bytes, 0, readbytes);
		 }
		 byte[] appendBytes = appendContent.getBytes();
		 fsOut.write(appendBytes);
		 fsIn.close();
		 fsOut.close();
		
	 }
	
	 //�޸��ļ�������Ϊ�´��������(ɾ���ļ����ٴ����ļ�)
	 public boolean updateFile(String filePath,String appendContent) throws IOException{
		 boolean isdel = deleteFile(filePath);
		 if(isdel){
			 createFile(filePath,appendContent);
			 return true;
		 }else{
			 System.out.println("�����ļ�ǰ��ɾ���ļ�ʧ��..");
			 return false;
		 }
	 }
	 
	 //�ļ�������(�����������Ϊhdfs��ȫ·��)
	 public boolean renameFile(String oldFileName,String newFileName) throws IOException{
		 Path oldName = new Path(oldFileName);
		 Path newName = new Path(newFileName);
		 boolean isRe = fs.rename(oldName, newName);
		 return isRe;
	 }
	 
	 //�ر�fileSystem
	 public void close() throws IOException{
		 fs.close();
	 }
	
}
