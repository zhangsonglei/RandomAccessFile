import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *<ul>
 *<li>Class: Test
 *<li>Description:  测试文件的随机读取，根据偏移量定位文件位置进行读取
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年7月6日
 *</ul>
 */
public class AccessFileRandom {	
	/**
	 *<li>Description: 计算每行偏移量，存到数组中索引为行索引
	 * @param index
	 * @param path
	 * @throws IOException
	 */
	public static void getfile(int[] index, List<String> list) throws IOException {
		int line = 0;
        index[line] = 0;

		for(int i = 1; i < list.size(); i++) {
			line++;
			index[line] = index[line - 1] + list.get(i).trim().getBytes("utf-8").length + 2;//计算每行偏移量/字节大小
		}
        System.out.println("init  end");
	}
	
	//RandomAccessFile读的是不排序数据--打印到控制台
	/**
	 *<li>Description: 输出每次读取的行内容和读取时间
	 * @param index 行偏移量
	 * @param line 读取的行数
	 * @param path 读取的文件路径
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void show(int[] index, int line, String path) throws FileNotFoundException, IOException {
		long beginTime = System.currentTimeMillis();
		RandomAccessFile raf = new RandomAccessFile(path,"r");//只读的打开随机访问文件
		raf.seek(index[line - 1]);//移动文件指针
		String str1 = new String(raf.readLine().getBytes("ISO-8859-1"),"utf-8");//读取的时候防止中文乱码
		long endTime = System.currentTimeMillis();
		System.out.println(str1+":"+(double)(endTime - beginTime)+"ms");
		raf.close();
	}

	/**
	 * <li>Description: 按行读取文件并存储到list中
	 * @param path 文件路径
	 * @param encoding 编码
	 * @return 以行为单位的list
	 * @throws IOException
	 */
	public static List<String> readFile(String path, String encoding) throws IOException {
		List<String> list = new ArrayList<String>();
		File file = new File(path);
		
		if(file.isFile() && file.exists()) {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			
			String line = "";
			while((line = reader.readLine()) != null) {
				line = line.trim();
				list.add(line);
			}
			reader.close();
		}else {
			System.err.println("File:\""+path+"\" read failed!");
		}
		
		System.out.println("end file read ");
		return list;	
	}
	
	public static void main(String[] args) throws IOException{
		String filePath = "getPredLabels.txt";
		List<String> list = readFile(filePath, "utf-8");
		int[] index = new int[list.size()];//索引=id+1
		
		getfile(index,list);

		while(true){
			System.out.print("Search line:");
			Scanner scanner = new Scanner(System.in);
			int line = scanner.nextInt();	
			if(line > 0 && line <= list.size())
				show(index,line++,filePath);	//显示索引的位置上数据
			else
				System.out.println("out of range of file: 1~"+list.size());
		}
    }
	
	
}
