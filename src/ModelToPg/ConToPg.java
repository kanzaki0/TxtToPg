package ModelToPg;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets; 

public class ConToPg {
	public Connection connection = null;
	//连接数据库
	public Connection connect (String IP,String user,String password) {
		 Connection connection = null;
	     try {
	    	 String url = "jdbc:postgresql://"+IP+":5432/xspring";
	    	 Class.forName("org.postgresql.Driver");
	    	 connection= DriverManager.getConnection(url, user, password);
	    	 return connection;
	     } catch (Exception e) {
	            throw new RuntimeException(e);
	        }	    	
	}
	
	//关闭连接
	public void CloseConnect () {
		try {
			connection.close();
		}catch (Exception e) {
				e.printStackTrace();
		}
	}
	
	//获取最大值
	public String[] GetMax (String version,String table_name) {
		Statement statement = null;
        try {
            String sql = "select min(index),max(index) from "+table_name+" where version='"+version+"' limit 1";
            statement = connection.createStatement();
 
            ResultSet resultSet = statement.executeQuery(sql);
                 //取出列值
        	String[] minandmax = new String[2];
            while(resultSet.next()){
            	minandmax[0] = resultSet.getString(1);
            	minandmax[1] = resultSet.getString(2);
            	if(minandmax[0]==null) {minandmax[0]="0";}
            	if(minandmax[1]==null) {minandmax[1]="0";}
    			return minandmax;
                }
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException(e);
        }finally{
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }finally{
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }

        }
		return null;

	}

	//数据插入PG表
	public int InsertToModel (int start_index,String picurl,String version,String table_name,File[] files) {
//		for(File file:files) {
//			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
//			String lineTxt = null;
//			while((lineTxt = br.readLine()) != null) {
//				//insert方式插入PG表，效率低，废弃
//				lineTxt=lineTxt.trim();
//				String sql = "INSERT INTO "+table_name+"(\"index\",\"picurl\",\"picmode\",\"version\") values ("+start_index+","+"'"+picurl+"'"+","+"'"+lineTxt+"'"+","+"'"+version+"'"+")";
//				try {
//					Statement statement=connection.createStatement();
//					statement.executeUpdate(sql);
//					start_index++;
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			}
//			br.close();
		CopyManager copyManager = null;
		File csv_pg = ConToPg.ModelToCsv(start_index,picurl,version,table_name,files);
		try {
			copyManager = new CopyManager((BaseConnection)connection);
			FileReader reader = new FileReader(csv_pg);
			copyManager.copyIn("copy "+table_name+" from stdin delimiter as ',' NULL as 'null'",reader);
		}catch (Exception e) {
			e.printStackTrace();
		}
		//获取文件行数，进而得知导入行数
		try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(csv_pg))){
			long fileLength = csv_pg.length();
	        lineNumberReader.skip(fileLength);
	        int lineNumber = lineNumberReader.getLineNumber();
	        return lineNumber;
	    } catch (IOException e) {
	        return -1;
	    }
		
	}
	
	//生成csv文件
	public static File ModelToCsv (int start_index,String picurl,String version,String table_name,File[] files) {
		File csv =null;
		BufferedWriter csvwrite = null;
		try {
			csv=File.createTempFile(table_name+"_"+start_index+"_"+System.currentTimeMillis(), ".csv", new File(files[0].getParent()));
			csvwrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv), StandardCharsets.UTF_8), 1024);
			for(File file:files) {
				//逐个文件
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
				String lineTxt = null;
				while((lineTxt = br.readLine()) != null) {
					//逐行读取，数据插入csv文件
					lineTxt=lineTxt.trim();
					csvwrite.write(start_index+","+picurl+","+lineTxt+","+version);
					csvwrite.newLine();
					csvwrite.flush();
					start_index++;
				}
				br.close();
			}
			csvwrite.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return csv;
	}
	
	
}
