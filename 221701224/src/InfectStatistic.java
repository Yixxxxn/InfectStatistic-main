/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

class InfectStatistic {
	public static String log_route="";//��־�ļ�·��
	public static String out_route;//����ļ�·��
	public static String log_need;//��Ҫ����������־�ļ�·��
	public static String out_name;//
	public static String[] log_list;//��ȡ������־�ļ��б�,���Ұ������ڴ�С��������
	public static int[] type_num= {1,2,3,4};
	/*
	 * ip�����Ⱦ���ߣ�sp�������ƻ��ߣ�cure����������dead��������
	 */
	public static String[] type_symbol= {"ip","sp","cure","dead"};
	public static String[] type_name= {"��Ⱦ����","���ƻ���","����","����"};
	
	public static int[] province_select = new int[35];
	public static int[][] province_num = new int[35][4];
	public static String[] province_name = {"ȫ��", "����", "����" ,"����", "����", "����","����",
			"�㶫", "����", "����", "����", "�ӱ�", "����", "������", "����", "����", "����",
			"����", "����", "����", "���ɹ�", "����", "�ຣ", "ɽ��", "ɽ��", "����", "�Ϻ�",
			"�Ĵ�", "̨��", "���", "����", "���", "�½�", "����", "�㽭"};
    
public static void judgeCommandLine(String[] str) {
	if(!str[0].equals("list"))
	{
		System.out.println("�����д��󣬿�ͷ��list����");
		System.exit(0);
	}
	for(int i=1;i<str.length;i++)
	{
		if(str[i].equals("-log"))
		{
			if(str[++i].matches("^[A-z]:\\\\(.+?\\\\)*$"))
			{
				log_route=str[i];
			}
			else
			{
				System.out.println("�����д����ļ�·��δ��д�����");
				System.exit(0);
			}
		}
		if(str[i].equals("-out"))
		{
			if(str[++i].matches("^[A-z]:\\\\(\\S+)+(\\.txt)$"))
			{
				out_route=str[i];
			}
			else
			{
				System.out.println("�����д����ļ����·��δ��д�����");
				System.exit(0);
			}
		}
		if(str[i].equals("-date"))
		{
			//-date��������һ��ָ�ֱ��
			if((i+1)==str.length)
			{
				getLogList(log_route);
				log_need=log_list[0];
			}
			else if(str[i+1].equals("-log")||str[i+1].equals("-out")
			   ||str[i+1].equals("-type")||str[i+1].equals("-province"))
			{
				getLogList(log_route);
				log_need=log_list[0];
			}
			else
			{
				if(isLegalDate(str[++i]))
				{
					getLogList(log_route);
					log_need=str[i];
							
				}
				else
				{
					System.out.println("�����д������ڸ�ʽ����");
					System.exit(0);
				}
			}
			
		}
		if(str[i].equals("-type"))
		{
			for(int j=1;j<5;j++)
			{
				//-date��������һ��ָ�ֱ��
				if((i+j)<str.length)
				{
					if(str[i+j].equals("-log")||str[i+j].equals("-out")
						||str[i+j].equals("-date")||str[i+j].equals("-province"))
					{
						break;
					}
					else if(str[i+j].equals(type_symbol[0]))
					{
						type_num[0]=j;
					}
					else if(str[i+j].equals(type_symbol[1]))
					{
						type_num[1]=j;
					}
					else if(str[i+j].equals(type_symbol[2]))
					{
						type_num[2]=j;
					}
					else if(str[i+j].equals(type_symbol[3]))
					{
						type_num[3]=j;
					}
					else
					{
						System.out.println("�����д���-type��ʽ����");
						System.exit(0);
					}
				}
				else break;
			}
		}
		if(str[i].equals("-province"))
		{
			for(int j=1;j<=province_name.length;j++)
			{
				//-province��������һ��ָ�ֱ��
				if((i+j)<str.length)
				{
					if(str[i+j].equals("-log")||str[i+j].equals("-out")
						||str[i+j].equals("-date")||str[i+j].equals("-province"))
					{
						break;
					}
					else if(str[i+j].equals(province_name[j-1]))
					{
						province_select[0]=1;
						province_select[j-1]=1;
					}
					else
					{
						System.out.println("�����д���-province��ʽ����");
						System.exit(0);
					}
				}
				else break;
			}
			
		}
	}
}
public static boolean isLegalDate(String str) {
	SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
	 try {
		 date_format.setLenient(false);
		 date_format.parse(str);
         String[] date_str = str.split("-");
         for (String s : date_str) 
         {
             boolean isNum = s.matches("[0-9]+");
             if (!isNum)
                 return false;
         }
	 	 } catch (Exception e) {
         return false;
     }
     return true;
}

public static void getLogList(String str) {
	log_list=new File(str).list();
	for (int i=0;i<log_list.length-1;i++){
        for (int j=0;j<log_list.length-i-1;j++) {
        	if(log_list[j].compareTo(log_list[j+1])>0){
                String temp=log_list[j];
                log_list[j]=log_list[j+1];
                log_list[j+1]=temp;
            }
        	
        }
    }

}
/*
 * ��ȡ�ı�����
 */
public static void getTextContent(String str) {
	try {
		BufferedReader br = new BufferedReader(new InputStreamReader
				(new FileInputStream(new File(str)), "UTF-8"));
		 String text_content = null;
         
         while ((text_content = br.readLine()) != null) { //���ж�ȡ�ı�����
         	if(!text_content.startsWith("//")) //������//������ȡ
         	processingText(text_content);
         }
         br.close();
	} catch (Exception e) {
		// TODO: handle exception
	}
}

    
    
    
public static void processingText(String str) {
	String situation1 = "(\\S+) ���� ��Ⱦ���� (\\d+)��";
	String situation2 = "(\\S+) ���� ���ƻ��� (\\d+)��";
	String situation5 = "(\\S+) ��Ⱦ���� ���� (\\S+) (\\d+)��";
	String situation6 = "(\\S+) ���ƻ��� ���� (\\S+) (\\d+)��";
	String situation4 = "(\\S+) ���� (\\d+)��";
	String situation3 = "(\\S+) ���� (\\d+)��";
	String situation7 = "(\\S+) ���ƻ��� ȷ���Ⱦ (\\d+)��";
	String situation8 = "(\\S+) �ų� ���ƻ��� (\\d+)��";
	boolean is_situation1 = Pattern.matches(situation1, str);
	boolean is_situation2 = Pattern.matches(situation2, str);
	boolean is_situation3 = Pattern.matches(situation3, str);
	boolean is_situation4 = Pattern.matches(situation4, str);
	boolean is_situation5 = Pattern.matches(situation5, str);
	boolean is_situation6 = Pattern.matches(situation6, str);
	boolean is_situation7 = Pattern.matches(situation7, str);
	boolean is_situation8 = Pattern.matches(situation8, str);
	
	if(is_situation1) //���� ��Ⱦ���ߴ���
	addIP(str);
	else if(is_situation2) //���� ���ƻ��ߴ���
	addSP(str);
	else if(is_situation5) //��Ⱦ���� ���봦��
	influxIP(str);
	else if(is_situation6) //���ƻ��� ���봦��
	influxSP(str);
	else if(is_situation4) //���� �������ߴ���
	addDead(str);
	else if(is_situation3) //���� �������ߴ���
	addCure(str);
	else if(is_situation7) //���ƻ��� ȷ���Ⱦ����
	diagnoseSP(str);
	else if(is_situation8) //�ų� ���ƻ��ߴ���
	excludeSP(str);
	
}

public static void addIP(String str) {
	//
	String[] str_arr = str.split(" "); //���ַ����Կո�ָ�Ϊ����ַ���
	int i;
	int n = Integer.valueOf(str_arr[3].replace("��", ""));//����ǰ�����ִ��ַ�������ת��Ϊint����
	for(i = 0; i < province_name.length; i++)
	{
		if(str_arr[0].equals(province_name[i])) 
		{ //��һ���ַ���Ϊʡ��
			province_num[0][0] += n; //ȫ����Ⱦ������������
			province_num[i][0] += n; //��ʡ�ݸ�Ⱦ������������
			if(province_select[0] == -1) //ʡ�ݴ���δָ��״̬
			province_select[i] = 1; //��Ҫ����ʡ���г�
			break;
		}
	}
	
}

public static void addSP(String str) {
	// 
	String[] str_arr = str.split(" "); //���ַ����Կո�ָ�Ϊ����ַ���
	int i;
	int n = Integer.valueOf(str_arr[3].replace("��", ""));//����ǰ�����ִ��ַ�������ת��Ϊint����
	for(i = 0; i < province_name.length; i++)
	{
		if(str_arr[0].equals(province_name[i])) 
		{ //��һ���ַ���Ϊʡ��
			province_num[0][1] += n; //ȫ�����ƻ�����������
			province_num[i][1] += n; //��ʡ�����ƻ�����������
			if(province_select[0] == -1) //ʡ�ݴ���δָ��״̬
			province_select[i] = 1; //��Ҫ����ʡ���г�
			break;
		}
	}
	
}
public static void influxIP(String str) {
	// 
	String[] str_arr = str.split(" "); //���ַ����Կո�ָ�Ϊ����ַ���
	int i;
	int n = Integer.valueOf(str_arr[4].replace("��", ""));
	for(i = 0; i < province_name.length; i++)
	{
		if(str_arr[0].equals(province_name[i])) 
		{ //��һ���ַ���Ϊ����ʡ��
			province_num[i][0] -= n; //��ʡ�ݸ�Ⱦ������������
			if(province_select[0] == -1) //ʡ�ݴ���δָ��״̬
				province_select[i] = 1; //��Ҫ����ʡ���г�
		}
		if(str_arr[3].equals(province_name[i])) 
		{ //���ĸ��ַ���Ϊ����ʡ��
			province_num[i][0] += n; //��ʡ�ݸ�Ⱦ������������
			if(province_select[0] == -1) //ʡ�ݴ���δָ��״̬
				province_select[i] = 1; //��Ҫ����ʡ���г�
		}
	}
	
}
public static void influxSP(String str) {
	// TODO �Զ����ɵķ������
	String[] str_arr = str.split(" "); //���ַ����Կո�ָ�Ϊ����ַ���
	int i;
	int n = Integer.valueOf(str_arr[4].replace("��", ""));
	for(i = 0; i < province_name.length; i++)
	{
		if(str_arr[0].equals(province_name[i])) 
		{ //��һ���ַ���Ϊ����ʡ��
			province_num[i][1] -= n; //��ʡ�ݸ�Ⱦ������������
			if(province_select[0] == -1) //ʡ�ݴ���δָ��״̬
				province_select[i] = 1; //��Ҫ����ʡ���г�
		}
		if(str_arr[3].equals(province_name[i])) 
		{ //���ĸ��ַ���Ϊ����ʡ��
			province_num[i][1] += n; //��ʡ�ݸ�Ⱦ������������
			if(province_select[0] == -1) //ʡ�ݴ���δָ��״̬
				province_select[i] = 1; //��Ҫ����ʡ���г�
		}
	}
}
public static void addDead(String str) {
	// 
	String[] str_arr = str.split(" "); //���ַ����Կո�ָ�Ϊ����ַ���
	int i;
	int n = Integer.valueOf(str_arr[2].replace("��", ""));
	for(i = 0; i < province_name.length; i++) 
	{
		if(str_arr[0].equals(province_name[i])) 
		{ //��һ���ַ���Ϊʡ��
			province_num[0][3] += n; //ȫ��������������
			province_num[0][0] -= n; //ȫ����Ⱦ������������
			province_num[i][3] += n; //��ʡ��������������
			province_num[i][0] -= n; //��ʡ�ݸ�Ⱦ������������
			if(province_select[0] == -1) //ʡ�ݴ���δָ��״̬
			province_select[i] = 1; //��Ҫ����ʡ���г�
			break;
		}
	}
}
public static void addCure(String str) {
	// 
	String[] str_arr = str.split(" "); //���ַ����Կո�ָ�Ϊ����ַ���
	int i;
	int n = Integer.valueOf(str_arr[2].replace("��", ""));
	for(i = 0; i < province_name.length; i++) 
	{
		if(str_arr[0].equals(province_name[i])) 
		{ //��һ���ַ���Ϊʡ��
			province_num[0][2] += n; //ȫ��������������
			province_num[0][0] -= n; //ȫ����Ⱦ������������
			province_num[i][2] += n; //��ʡ��������������
			province_num[i][0] -= n; //��ʡ�ݸ�Ⱦ������������
			if(province_select[0] == -1) //ʡ�ݴ���δָ��״̬
			province_select[i] = 1; //��Ҫ����ʡ���г�
			break;
		}
	}
	
}
public static void diagnoseSP(String str) {
	// TODO �Զ����ɵķ������
	String[] str_arr=str.split(" "); //���ַ����Կո�ָ�Ϊ����ַ���
	int i;
	int n=Integer.valueOf(str_arr[3].replace("��", ""));//����ǰ�����ִ��ַ�������ת��Ϊint����
	
	for(i=0;i< province_name.length;i++)
	{
		if(str_arr[0].equals(province_name[i])) 
		{ //��һ���ַ���Ϊʡ��
			province_num[0][1]-=n; //ȫ�����ƻ�����������
			province_num[0][0]+=n; //ȫ����Ⱦ������������
			province_num[i][1]-=n; //��ʡ�����ƻ�����������
			province_num[i][0]+=n; //��ʡ�ݸ�Ⱦ������������
			if(province_select[0]==-1) //ʡ�ݴ���δָ��״̬
			province_select[i]=1; //��Ҫ����ʡ���г�
			break;
		}
	}
}
public static void excludeSP(String str) {
	// TODO �Զ����ɵķ������
	String[] str_arr=str.split(" "); //���ַ����Կո�ָ�Ϊ����ַ���
	int i;
	int n=Integer.valueOf(str_arr[3].replace("��", ""));//����ǰ�����ִ��ַ�������ת��Ϊint����
	
	for(i=0;i< province_name.length;i++)
	{
		if(str_arr[0].equals(province_name[i])) 
		{ //��һ���ַ���Ϊʡ��
			province_num[0][1]-=n; //ȫ�����ƻ�����������
			province_num[i][1]-=n; //��ʡ�����ƻ�����������
			if(province_select[0]==-1) //ʡ�ݴ���δָ��״̬
			province_select[i]=1; //��Ҫ����ʡ���г�
			break;
		}
	}
}

public static void outputFile(String str) {
	FileWriter fwriter = null;
	try {
		fwriter=new FileWriter(str);
		int i,j,k;
		if(province_select[0]==-1)//��ʾδָ��ʡ��
		province_select[0]=1;
			for(i=0;i<province_name.length;i++)
			{
				if(province_select[i]==1)
				{
					fwriter.write(province_name[i] + " ");
					for(j=0;j<type_num.length;j++)
					{
						for(k=0;k>type_num.length;k++)
						{
							if(type_num[k]==j+1)
							{
								fwriter.write(type_name[k] + province_num[i][k] + "�� ");
        						break;
							}
						}
					}
					fwriter.write("\n");
				}

			}
			fwriter.write("// ���ĵ�������ʵ���ݣ���������ʹ��");	
	} catch (IOException e) {
		// TODO �Զ����ɵ� catch ��
		e.printStackTrace();
	}finally {
        try {
            fwriter.flush();
            fwriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}







/*
 * 
 * public static List<String> getFiles(String path) {
    List<String> files = new ArrayList<String>();
    File file = new File(path);
    File[] tempList = file.listFiles();

    for (int i = 0; i < tempList.length; i++) {
        if (tempList[i].isFile()) {
            files.add(tempList[i].toString());
            //�ļ�����������·��
            //String fileName = tempList[i].getName();
        }
        if (tempList[i].isDirectory()) {
            //����Ͳ��ݹ��ˣ�
        }
    }
    return files;
}
*/
public static void main(String[] args) {
    /*    if(args[0].equals("list"))
        {
        	for(int i=1;i<args.length;i++)
        	{
        		if(args[i].equals("-log"))
        		{
        			File file=new File(args[i+1]);
        		}
        		if(args[i].equals("-out"))
        		{
        			
        		}	
        		    
        	}
        }
        else
        {
        	
        }
    	*/
	province_select[0]=-1;
    judgeCommandLine(args);
    for(int i=0;i<log_list.length;i++)
    {
    	if(log_need.compareTo(log_list[i])>=0)
    	{
    		getTextContent(log_list[i]);
    	}
    	else break;
    }
    //System.out.println(out_route);
    outputFile(out_route);
    	
    }

}
