package qz.bigdata.crawler.store.hdfs;

import com.thoughtworks.xstream.XStream;
import qz.bigdata.crawler.core.UrlInfo;
import qz.bigdata.crawler.store.ISaveData;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HdfsOptImp implements HdfsOptInter, ISaveData {
//    public static void main(String[] args)
//    {
//        HdfsOptImp.getInstance("testCrawler").createDir("test1");
//    }
	private HdfsClient client = null;
	
	private static HdfsOptImp hdfsOpt = null;
	
	private final String FIRST_LEVLE_DIR = "dataInfo";
	
	private final String SECOND_LEVLE_PAGE = "page";
	
	private final String SECOND_LEVLE_FILE = "file";

	private String crawlerPath;

	private HdfsOptImp(String crawlerName) throws IOException {
		this.client = new HdfsClient();
		this.initFolder(crawlerName);
	}

	private void initFolder(String crawlerName) throws IOException {
		StringBuffer buffer = new StringBuffer();
		String firstLevelFolder = "/" + FIRST_LEVLE_DIR;

		if(!this.client.checkDir(firstLevelFolder)){
			this.client.makeDir(firstLevelFolder);
		}
		String secondLevelFileFolder = firstLevelFolder + "/" + SECOND_LEVLE_FILE;
		if(!this.client.checkDir(secondLevelFileFolder)){
			this.client.makeDir(secondLevelFileFolder);
		}
		String secondLevelPageFolder = firstLevelFolder + "/" + SECOND_LEVLE_PAGE;
		if(!this.client.checkDir(secondLevelPageFolder)){
			this.client.makeDir(secondLevelPageFolder);
		}
		this.crawlerPath = secondLevelPageFolder + "/" + crawlerName;
		if(!this.client.checkDir(this.crawlerPath)){
			this.client.makeDir(this.crawlerPath);
		}
	}
	synchronized public static HdfsOptImp getInstance(String crawlerName) throws IOException {
		if(hdfsOpt == null){
			hdfsOpt = new HdfsOptImp(crawlerName);
		}
		return hdfsOpt;
	}
	/**
	 * ����hdfsĿ¼
	 * @param path  Ŀ¼·��
	 * @return �����ɹ����
	 */

	public boolean createDir(String path){
		try {
			return client.makeDir(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//�ϴ������ļ���HDFS�ļ�ϵͳ��ʱ��Ŀ¼��
/*	@Override
	public String upLoadFile(String localPath ){
		int maxNum = 1;
		String newHdfsPath = "";
		if(!isFile(localPath)){
			System.out.println("�����ļ�������..");
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		String rootPath = "dataInfo/file";
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String hdfsPath = buffer.append("/").append(rootPath).append("/").append(today).toString();
		String maxNumFileName = "maxNum.txt";
		String maxNumFilePath = buffer.append("/").append(maxNumFileName).toString();
		boolean isExsits;
		try {
			synchronized(this){
				System.out.println("����ͬ����ģ��..");
				isExsits = client.checkDir(hdfsPath);
				if(!isExsits){
					client.makeDir(hdfsPath);
					createMaxNumFile(maxNumFilePath);
					newHdfsPath = changeFileName(localPath,"1",hdfsPath);
				}else{
					byte[] bytes = client.readFile(maxNumFilePath);
					if(bytes.length>0){
						maxNum = Integer.parseInt(new String(bytes));
						maxNum++;
						updateMaxNumFile(maxNumFilePath,String.valueOf(maxNum));
						newHdfsPath = changeFileName(localPath,String.valueOf(maxNum),hdfsPath);
					}
				}
				System.out.println("ͬ����ģ�����..");
			}
			client.uploadFile(localPath, newHdfsPath);
			System.out.println("�ϴ��ļ�����..");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return String.valueOf(maxNum);
	}*/
	
	
	/**
	 * �ϴ������ļ���HDFS�ļ�ϵͳ��ʱ��Ŀ¼��
	 * @param localPath �����ļ�·��  
	 * @return �ϴ���hdfsϵͳ���·��
	 */
	public String upLoadFile(String localPath ){
		String newHdfsPath = "";
		if(!isFile(localPath)){
			System.out.println("�����ļ�������..");
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String fileName = new File(localPath).getName();
		try {
			synchronized(this){
				fileName = getRelName(fileName);
			}
			newHdfsPath = buffer.append("/").append(FIRST_LEVLE_DIR).append("/").append(SECOND_LEVLE_FILE)
					.append("/").append(today).append("/").append(fileName).toString();
			client.uploadFile(localPath, newHdfsPath);
			System.out.println("�ϴ��ļ�����..");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return String.valueOf(newHdfsPath);
	}
	
	
	/**
	 * �������ȡ����ҳ�������л���浽hdfs�ϣ����ļ���ʽ����
	 * @param pageObj ��ҳ���ݶ���
	 * @param fileName hdfs�б�����ļ����
	 * @return ���浽hdfsϵͳ����ļ�·��
	 */
	public String upLoadPage(Object pageObj, String fileName) throws IOException{

		StringBuffer buffer = new StringBuffer();
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		synchronized(this){
			fileName = getRelName(fileName);
		}
		String dateFolder = this.crawlerPath + "/" + today;
		this.createDateFolder(dateFolder);

		String newHdfsPath = dateFolder +  "/" + fileName;

		XStream xstream = new XStream();  
		String pageXml = xstream.toXML(pageObj);
		client.createFile(newHdfsPath, pageXml);
		return String.valueOf(newHdfsPath);
	}

	private void createDateFolder(String path) throws IOException {
		if(!this.client.checkDir(path)){
			this.client.makeDir(path);
		}
	}
	
	
	
	/**
	 * ����ҳ���ݵ��ļ����ص����ز����з����л��󣬵õ�ҳ�����
	 * @param filePath  hdfsϵͳ���ļ���·��  
	 * @return  �����л����ҳ�����ݶ���
	 */
	public Object downLoadPage(String filePath){
		Object pageObject = null;
		String pageStirng;
		try {
			pageStirng = new String(client.readFile(filePath),"UTF-8");
			XStream xstream = new XStream();  
			pageObject = xstream.fromXML(pageStirng);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pageObject;
	}
	
	
	/**
	 * ��HDFS�ļ�ϵͳ�������ļ�������
	 * @param hdfsPath  hdfsϵͳ���ļ���·��  
	 * @param localPath ���ر���·��
	 * @return ���سɹ����
	 */
	public boolean downLoadFile(String hdfsPath, String localPath) {
		try {
			client.downloadFile(hdfsPath, localPath);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * ����ɾ���ļ��л��ļ�
	 * @param hdfsPath hdfsϵͳ��Ҫɾ����ļ�·��  
	 * @return ɾ��ɹ����
	 */
	public boolean deleteFile(String hdfsPath) {
		try {
			return client.deleteFile(hdfsPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	
	/**
	 * ׷���ļ����ַ�
	 * @param hdfsPath hdfsϵͳ���ļ�·��  
	 * @param appendContent  ׷�ӵ��ַ�����
	 * @param isFile ׷�ӵ������Ƿ��Ǳ��ص��ļ����������appendContentΪ�����ļ���·�����������appendContentֱ��������
	 * @return  ׷�ӳɹ����s
	 */
	public boolean appendFile(String hdfsPath, String appendContent,boolean isFile) {
		int readbytes = 0;
		StringBuffer content = new StringBuffer();
		try {
			if(isFile){
				if(isFile(appendContent)){
					byte[] bytes = new byte[4096];
					InputStream fileIn = new BufferedInputStream(new FileInputStream(new File(appendContent)));
					while((readbytes = fileIn.read(bytes)) != -1){
						content.append(new String(bytes,"UTF-8"));
					}
					fileIn.close();
				}else{
					System.out.println("׷�ӵ��ļ�������..");
				}
			}else{
				content.append(appendContent);
			}
			client.appendFile(hdfsPath, content.toString());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	 
	
	/**
	 * �ļ�������
	 * @param oldFileName  hdfsϵͳ���ļ�������ȫ·��
	 * @param newFileName  hdfsϵͳ���ļ��޸ĺ��ȫ·
	 * @return ������ɹ����
	 */
	 public boolean renameFile(String oldFileName,String newFileName){
		try {
			return client.renameFile(oldFileName, newFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	 }
	
	/**
	 * ��������ļ���ƣ�ģ���ѯhdfs�ļ�ϵͳ��ĳ·���µ�����ƥ����ļ���������Щ�ļ���ȫ·��
	 * @param searchName  Ҫ��ѯ���ļ�����ƣ������ǲ������
	 * @param fileType  �Ǹ����ļ�����ҳ���ļ���ֵΪ: file ����  page
	 * @param crawlerName ����ѯҳ���ļ�������Խ�·����ȷ��ĳ�������ļ����µ��ļ���ֵ����Ϊ��
	 * @return ��ӦĿ¼�£�����ģ��ƥ�����Ƶ��ļ���ȫ·��
	 */
	public List<String> searchFile(String searchName,String fileType,String crawlerName){
		String filePath="";
		StringBuffer sb = new StringBuffer();
		List<String> list = new ArrayList<String>();
		if(fileType == null || "".equals(fileType)){
			fileType = "file";
		}
		if("file".equalsIgnoreCase(fileType)){
			filePath = sb.append("/").append(FIRST_LEVLE_DIR).append("/").append(SECOND_LEVLE_FILE).toString();
		}else if("page".equalsIgnoreCase(fileType)){
			if(crawlerName != null && !"".equals(crawlerName)){
				filePath = sb.append("/").append(FIRST_LEVLE_DIR).append("/").append(SECOND_LEVLE_PAGE)
						.append("/").append(crawlerName).toString();
			}else{
				filePath = sb.append("/").append(FIRST_LEVLE_DIR).append("/").append(SECOND_LEVLE_PAGE).toString();
			}
		}
		try {
			list = client.searchFile(list,filePath, searchName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * ��ȡĳ��·���������ļ���·��
	 * @param dirPath hdfs��ѯ·��
	 * @return ��·���������ļ���hdfs·������
	 */
	public List<String> getFileList(String dirPath){
		List<String> list = new ArrayList<String>();
		try {
			list = client.getFileList(list,dirPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * �ر�FileSystem
	 */
	public void close(){
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * �жϱ��ص��ļ��Ƿ����
	 * @param localFilePath   �����ļ�·��
	 * @return �ļ��Ƿ����
	 */
	private boolean isFile(String localFilePath){
		File file = new File(localFilePath);
		if(!file.exists() || file.isDirectory()){
			return false;
		}
		return true;
	}
	
	
	/**
	 * �½�����Ŀ¼�󣬴���һ���洢�ļ��������ֵ���ļ������ڴ����µ��ļ����ļ���
	 * @param hdfsPath  hdfsϵͳ�е��ļ�Ŀ¼
	 */
	private synchronized void createMaxNumFile(String hdfsPath){
		try {
			client.createFile(hdfsPath, "1");   //��ʼֵ��1
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * �޸Ĵ����ļ������е�ֵ
	 * @param hdfsPath  hdfsϵͳ���ļ���·��
	 * @param newNum  �µ����ֵ
	 */
	private synchronized void updateMaxNumFile(String hdfsPath,String newNum){
		try {
			client.updateFile(hdfsPath, newNum);   
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �������ļ�����ƣ���Ϊ�����ֵ�Ӻ�׺����Ʒ�ʽ����������hdfs�е��µ��ļ�·��
	 * @param filePath  �����ļ�·��
	 * @param newName   �µ��ļ����
	 * @param hdfsPath  hdfsϵͳ���ļ���·��
	 * @return hdfsϵͳ���µ��ļ�·��
	 */
	private String changeFileName(String filePath,String newName,String hdfsPath){
		StringBuffer newFileName= new StringBuffer();
		newFileName.append(hdfsPath).append("/");
		String suffix = ""; //��׺��
		File file = new File(filePath);
		String fileName = file.getName();
		String [] fullName = fileName.split("\\.");
		if(fullName.length>1){
			suffix = fullName[fullName.length-1];
			newFileName.append(newName).append(".").append(suffix);
		}
		return newFileName.toString();
	}

	
	/**
	 * ��������ҳ���ݵ��ļ������ʱ���
	 * @param name  �ļ����
	 * @return ����ʱ�������ļ����
	 */
	private synchronized String getRelName(String name){
		name = name.replace('/', '~');
        name = name.replace(':', '`');
		if (name.lastIndexOf(".") != -1){
			return (new StringBuilder()).append(name.substring(0, name.lastIndexOf("."))).append("_").append(System.currentTimeMillis()).append(name.substring(name.lastIndexOf("."))).toString();
		}else{
			return (new StringBuilder()).append(name).append("_").append(System.currentTimeMillis()).toString();
		}
	}


	@Override
	public void saveData(UrlInfo urlInfo) throws Exception {
		this.upLoadPage(urlInfo.data, urlInfo.url.toString());
	}
}
