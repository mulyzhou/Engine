package com.flying.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;

import com.flying.util.DateUtil;

public class MyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//		ApplicationContext context = new FileSystemXmlApplicationContext("src/applicationContext*.xml");
//		T_BASE_LOGINPERSONServiceIface tofficearchiveService = (T_BASE_LOGINPERSONServiceIface)context.getBean("T_BASE_LOGINPERSONService");
//		try {
//			String pwd=MD5.getMD5("123456");
//			HashMap hp=new HashMap();
//			hp.put("loginname", "zhengguofeng");
//			hp.put("loginpwd", pwd);
//			T_BASE_LOGINPERSON tbaseloginperson=(T_BASE_LOGINPERSON)tofficearchiveService.loginValidata(hp);
//            System.out.print(tbaseloginperson.getLoginname());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		try{
			String url="This 我的yi在";
			//byte[] buffer = url.getBytes();
		    BufferedOutputStream fw = new BufferedOutputStream(new FileOutputStream(new File("D:\\Dir\\test.txt")));
		    PrintWriter pw = new PrintWriter(fw);
			
			pw.write(url);
			pw.flush();
			//File file = new File("D:\\test2.txt");
			System.out.print("ok");
		}catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
    @Test
	public void testPost() throws UnsupportedEncodingException{
	   System.out.println(java.net.URLEncoder.encode("楼主"));
	   System.out.println(java.net.URLDecoder.decode("%E9%83%91","UTF-8"));//%E9%83%91
	   System.out.println(java.net.URLDecoder.decode("http://dev.sencha.com/deploy/ext-4.0.0/examples/tree/get-nodes.php?_dc=1312768476801&node=src&sort=%5B%7B%22property%22%3A%22leaf%22%2C%22direction%22%3A%22ASC%22%7D%2C%7B%22property%22%3A%22text%22%2C%22direction%22%3A%22ASC%22%7D%5D"));
	   System.out.println(java.net.URLDecoder.decode("http://dev.sencha.com/deploy/ext-4.0.0/examples/tree/get-nodes.php?_dc=1312768508804&node=src%2Fapp&sort=%5B%7B%22property%22%3A%22leaf%22%2C%22direction%22%3A%22ASC%22%7D%2C%7B%22property%22%3A%22text%22%2C%22direction%22%3A%22ASC%22%7D%5D"));
    }
    
    /**
     *  对于ArrayList,Vector 其核心是一个数组， 如果明确知道List的实例是ArrayList,Vector，当然用 for(int i=0; i<lst.size();i++){} 这种方式是最快的. 当然用Iterator 的话，其实是相关无几，Iterator 基本上都是指针操作, Iterator本身的处理会增加一点点的开销,跟踪一下源码就可以知道.

		Iterator 好处：通用，对于所有集合，使用Iterator性能都一样, 客户端自身不维护遍历集合的"指针"，所有的内部状态（如当前元素位置，是否有下一个元素）都由Iterator来维护，而这个Iterator由集合类通过工厂方法生成，因此，它知道如何遍历整个集合。
		客户端从不直接和集合类打交道，它总是控制Iterator，向它发送"向前"，"向后"，"取当前元素"的命令，就可以间接遍历整个集合。

		for(i=0;...) 方法有一个缺点: 如果List的实例是LinkedList等非"数组"存储方式的时候，遍历集合的开销会差别很大! 就以LinkedList来说,以下是get(i)方法来取元素的主要代码, 我们可以看到，LinkedList 内的get(i)方法，用了循环方式来返回元素，性能肯定会差.
     */
    @Test
    public void testForeach(){
    	List <Integer> list = new ArrayList <Integer>(); 
        for(int i=0;i <5000000 ;i++){ 
            list.add(11); 
        }
         
        int size= list.size(); 
        int c1=1; 
        long start = System.currentTimeMillis(); 
        for(int i=0;i <size;i++){ 
            c1 = list.get(i); 
        } 
        long end = System.currentTimeMillis()-start; 
        System.out.println("for + get(i)方法: " + end); 
         
         
        start = System.currentTimeMillis(); 
        for(int c2:list){ 
        } 
        end = System.currentTimeMillis()-start; 
        System.out.println("Iterator(foreach)方法:" + end); 
         
         
        int i = 0; 
        size = list.size(); 
        start = System.currentTimeMillis(); 
        while(i <size){ 
        	c1 = list.get(i);  
        	i++; 
        } 
        end = System.currentTimeMillis()-start; 
        System.out.println("While + get(i)方法:" + end); 
         
        start = System.currentTimeMillis(); 
        for(Iterator iterator = list.iterator(); iterator.hasNext();) { 
        	c1 = (Integer) iterator.next(); 
        } 
        end = System.currentTimeMillis()-start; 
        System.out.println("for iterator方法:" + end); 
    }
    
    @Test
    public void testMacth(){
    	System.out.println("434sdf".matches("\\d*"));
    }
    
    class Tree{
    	public String id;
    	public String text;
    	public String pid;
    	
    	public List<Tree> children = new ArrayList<Tree>();
    	
    	public Tree(String id,String text,String pid){
    		this.id = id;
    		this.text = text;
    		this.pid = pid;
    	}
    	
    	public String toString(){
    		String json = "{id:"+id+",text:"+text+",pid:"+pid;
    		json += ",children:[";
    		for(int i = 0;i<children.size();i++){
    			json += children.get(i).toString()+",";
    		}
    		if(children.size()>0){
    			json = json.substring(0,json.length()-1);
    		}
    		json += "]}";
    		return json;
    	}
    }
    @Test
    public void testArrayTree(){
    	//初始化
    	List<Tree> arr = new ArrayList();
    	arr.add(new MyTest.Tree("1", "t1", "0"));
    	arr.add(new MyTest.Tree("2", "t1", "1"));
    	arr.add(new MyTest.Tree("3", "t1", "1"));
    	arr.add(new MyTest.Tree("4", "t1", "1"));
    	arr.add(new MyTest.Tree("5", "t1", "2"));
    	arr.add(new MyTest.Tree("6", "t1", "2"));
    	arr.add(new MyTest.Tree("7", "t1", "3"));
    	
    	Map<String,Tree> map = new HashMap<String,Tree>();
    	Tree root = null;
    	for(int i = 0;i<arr.size();i++){
    		if(root == null){
    			root = arr.get(i);
    		}
    		if(Integer.parseInt(arr.get(i).id)<Integer.parseInt(root.id)){
    			root = arr.get(i);
    		}
    		map.put("z"+arr.get(i).id, arr.get(i));
    	}
    	
    	for(int i = 0;i<arr.size();i++){
    		String ppid = "z"+arr.get(i).pid;
    		if(map.get(ppid) == null){
    			continue;
    		}
    		map.get(ppid).children.add(arr.get(i));
    	}
    	
    	System.out.println(root);
    }
    
    @Test
    public void testTableXml() throws DocumentException{
    	String tablesXml = "<?xml version='1.0' encoding='UTF-8'?><tables><table id='1' name='student' desc='学生' x='439' y='108' width='100' height='100'>" +
								"<column name='id' desc='编号' type='number' length='' isNull='false' isPk='true'/>" +
								"<column name='name' desc='名称' type='varchar' length='100' isNull='true' isPk='false'/>" +
								"<column name='classid' desc='班级编号' type='number' length='' isNull='false' isPk='false'/>" +
							"</table></tables>";
		Document tableDoc = DocumentHelper.parseText(tablesXml); 
		List tables = tableDoc.selectNodes("/tables/table");
		Iterator iterTable = tables.iterator(); 
		while(iterTable.hasNext()){
			Element table = (Element)iterTable.next(); 
			String bmc = table.attributeValue("name");
			String bzs = table.attributeValue("desc");
			
			System.out.println(bmc+":"+bzs);
			List fields = table.selectNodes("column");
			Iterator iterField = fields.iterator(); 
			while(iterField.hasNext()){
				Element field = (Element)iterField.next();
				String zdmc = field.attributeValue("name");
				String zdzs = field.attributeValue("desc");
				System.out.println(zdmc+":"+zdzs);
			}
		}
    }
    
    @Test
    public void testReplaceAll(){
    	String sqlName = "T_BG_FJ.selectXw";
    	System.out.println(sqlName.substring(0,sqlName.indexOf(".")));
    	System.out.println(sqlName.substring(sqlName.indexOf("_")+1,sqlName.length()));
    	System.out.println(sqlName.substring(sqlName.lastIndexOf("_")+1));
    	//String path = "D:/src/config/tablename*.xml";
    	//System.out.println(path.substring(0,path.lastIndexOf("/")+1));
    	//System.out.println(path.substring(path.lastIndexOf("/")+1));
    }
    
    @Test
    public void testFiles(){
    	 String s = "sql-map-*.xml";
         s = s.replace('.', '#');
         s = s.replaceAll("#", "\\\\.");
         s = s.replace('*', '#');
         s = s.replaceAll("#", ".*");
         s = s.replace('?', '#');
         s = s.replaceAll("#", ".?");
         s = "^" + s + "$";
       
         System.out.println(s);
         Pattern p = Pattern.compile(s);
       
         ArrayList list = new ArrayList();
         list.add("sql-map-import.xml");
         list.add("sql-map-xg.xml");
         list.add("sql-map---.xml");
         list.add("1sql-map-import");
         list.add("abc.txt");
       
         Matcher fMatcher = null;
         String s1 = null;
         int size = list.size();
         for(int i=0;i<size;i++)
         {
           s1 = (String)list.get(i);
           fMatcher = p.matcher(s1);
           if(fMatcher.matches())
           {
             System.out.println(s1);
           }
         }
    }
    
    @Test
    public void createFile(){
    	//String path = "c:/zdf/zdf1/zdf2/zdf.txt";
    	//File file = FileUtil.createFile(path);
    	
    	List list = new ArrayList();
		List trueList = new ArrayList();
		trueList.add(true);
		trueList.add("是");
		
		List falseList = new ArrayList();
		falseList.add(false);
		falseList.add("否");
		
		//list.addAll(trueList);
		//list.addAll(falseList);
		//list.add(trueList);
		//list.add(falseList);
		
		System.out.println( JSONArray.fromObject(list));
    }
    
    @Test
    public void testDateregx(){
    	String[] str = {
                "2001-0-0", "2001-1-1", "2001-1-30", "2001-1-31",
                "2001-1-32", "2001-2-1", "2001-2-27", "2001-2-28",
                "2004-2-29", "2001-2-29", "2001-2-30", "2001-2-31",
                "2001-2-32", "2001-3-1", "2001-3-10", "2001-3-29",
                "2001-3-30", "2001-3-31", "2001-3-32", "2001-4-1",
                "2001-4-10", "2001-4-29", "2001-4-30", "2001-4-31",
                "2001-4-32", "2001-5-1", "2001-5-10", "2001-5-29",
                "2001-5-30", "2001-5-31", "2001-5-32", "2001-6-1",
                "2001-6-10", "2001-6-29", "2001-6-30", "2001-6-31",
                "2001-6-32", "2001-7-1", "2001-7-10", "2001-7-29",
                "2001-7-30", "2001-7-31", "2001-7-32", "2001-8-1",
                "2001-8-10", "2001-8-29", "2001-8-30", "2001-8-31",
                "2001-8-32", "2001-9-1", "2001-9-10", "2001-9-29",
                "2001-9-30", "2001-9-31", "2001-9-32", "2001-10-1",
                "2001-10-10", "2001-10-29", "2001-10-30", "2001-10-31",
                "2001-10-32", "2001-11-1", "2001-11-10", "2001-11-29",
                "2001-11-30", "2001-11-31", "2001-11-32", "2001-12-1",
                "2001-12-10", "2001-12-29", "2001-12-30", "2001-12-31",
                "2001-12-32", "2001-13-1", "2001-13-10", "2001-13-29",
                "2001-13-30", "2001-13-31", "2001-13-32", "245-12-4",
                "100-2-29" , "200-2-29" , "300-2-29" , "400-2-29",
                "500-2-29" , "800-2-29" , "900-2-29" , "2008-2-29",
                "1900-2-29", "2000-2-29", "1-1-1"   , "1-2-28",
                "0-1-1"   , "1-12-31"   , "351-2-29" , "352-2-29",
                "353-2-29" , "354-2-29" , "355-2-29" , "356-2-29",
                "357-2-29" , "358-2-29" , "350-2-29" , "1-2-29",
                "2-2-29"   , "3-2-29"   , "4-2-29"   , "5-2-29",
                "6-2-29"   , "7-2-29"   , "8-2-29"   , "9-2-29",
                "10-2-29"  , "11-2-29"  , "12-2-29"  , "13-2-29",
                "14-2-29"  , "15-2-29"  , "16-2-29"  , "17-2-29",
                "18-2-29"  , "19-2-29"  , "20-2-29"  , "21-2-29",
                "22-2-29"  , "23-2-29"  , "24-2-29"  , "25-2-29",
                "26-2-29"  , "27-2-29"  , "28-2-29"  , "29-2-29",
                "0-1-12"   , "00-1-12"  , "000-1-12" , "0000-1-12",
                "0028-2-29", "2007-1-31", "2007-11-31"
        };
    	
        for(String s : str) {
            System.out.println(s + " " + DateUtil.dateVerify(s));
        }
    }
    
    @Test
    public void testDateChange(){
    	String dateStr = "2012-02-07";
    	
    	System.out.println(DateUtil.stringToDate(dateStr,null));
    }
    
    @Test
    public void testSubChange(){
    	String str = "100___";
    	
    	System.out.println(str.substring(0,str.length()-3)+"100");
    }
}