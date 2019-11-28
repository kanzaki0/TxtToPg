package ModelToPg;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class UI {
	public static File[] files;
	public static ConToPg con1 = new ConToPg();
	public static void UI_start() {
		// 创建 JFrame 实例
        JFrame frame = new JFrame("模型导入工具V0.1");
        // Setting the width and height of frame
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();    
        // 添加面板
        frame.add(panel);
        /* 
         * 调用用户定义的方法并添加组件到面板
         */
        placeComponents(panel);

        // 设置界面可见
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {

        panel.setLayout(null);

        // 创建 JLabel
        JLabel IP1 = new JLabel("PG_IP：");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        IP1.setBounds(10,20,60,25);
        panel.add(IP1);   
        JTextField IP1_text = new JTextField("10.66.113.170");
        IP1_text.setBounds(70,20,150,25);
        panel.add(IP1_text);

        // 输入用户名的文本域
        JLabel usr1 = new JLabel("用户名:");
        usr1.setBounds(10,50,60,25);
        panel.add(usr1);
        JTextField user1_text = new JTextField("postgres");
        user1_text.setBounds(70,50,150,25);
        panel.add(user1_text);

        // 输入密码的文本域
        JLabel password1 = new JLabel("密码：");
        password1.setBounds(10,80,60,25);
        panel.add(password1);
        JTextField password1_text = new JTextField("vision12356HIK*");
        password1_text.setBounds(70,80,150,25);
        panel.add(password1_text);
        
        //输入版本的文本域
        JLabel version = new JLabel("version：");
        version.setBounds(240,20,60,25);
        panel.add(version);
        JTextField version_text = new JTextField("4.3.0");
        version_text.setBounds(300,20,150,25);
        panel.add(version_text);
        
        //输入表名的文本域
        JLabel table_name = new JLabel("表名：");
        table_name.setBounds(240,50,60,25);
        panel.add(table_name);
        JTextField table_name_text = new JTextField("model_base");
        table_name_text.setBounds(300,50,150,25);
        panel.add(table_name_text);
        
        //输入picurl的文本域
        JLabel picurl = new JLabel("URL：");
        picurl.setBounds(240,80,60,25);
        panel.add(picurl);
        JTextField picurl_text = new JTextField("http://10.66.71.91:8080/face-web/1.jpg");
        picurl_text.setBounds(300,80,150,25);
        panel.add(picurl_text);
               
        //查询当前版本的index的最大值最小值
        JTextField min_show = new JTextField();
        min_show.setBounds(120,110,250,15);
        min_show.setOpaque(false);
        min_show.setBorder(null);
        panel.add(min_show);
        JTextField max_show = new JTextField();
        max_show.setBounds(120,130,250,15);
        max_show.setOpaque(false);
        max_show.setBorder(null);
        panel.add(max_show);
        //选择文件后显示文件名或数量
		JLabel file1_name = new JLabel("");
		file1_name.setBounds(120,155,350,15);
        panel.add(file1_name);

        //输入起始index的文本域
        JLabel start_index = new JLabel("起始编号：");
        start_index.setBounds(10,180,80,25);
        panel.add(start_index);
        JTextField start_index_text = new JTextField();
        start_index_text.setBounds(100,180,200,25);
        panel.add(start_index_text);

        // 创建登录按钮
        JButton ConButton = new JButton("测试连接");
        ConButton.setBounds(10, 110, 100, 25);
        panel.add(ConButton);
        // 按钮触发事件
        ConButton.addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		con1.connection=con1.connect(IP1_text.getText(),user1_text.getText(),password1_text.getText());
        		String minandmax[]=con1.GetMax(version_text.getText(),table_name_text.getText());
        		con1.CloseConnect();
        		String min="当前index编码min为:"+minandmax[0];
        		String max="当前index编码max为:"+minandmax[1];
        		min_show.setText(min);
        		max_show.setText(max);
        		
        		start_index_text.setText(String.valueOf((Integer.parseInt(minandmax[1])+1)));
        		}
        		});
        
        //选取文件
        JButton SelectFiles = new JButton("选择文件");
        SelectFiles.setBounds(10,150, 100, 25);
        panel.add(SelectFiles);
        SelectFiles.addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		JFileChooser ChooseFiles=new JFileChooser("d:");
                ChooseFiles.setMultiSelectionEnabled(true);
                ChooseFiles.setFileSelectionMode(JFileChooser.FILES_ONLY);
                ChooseFiles.setFileFilter(new FileNameExtensionFilter("txt文件", "txt"));
                if(ChooseFiles.showSaveDialog(new JLabel()) == JFileChooser.APPROVE_OPTION) {
                	files = ChooseFiles.getSelectedFiles();
                	String FilePathShow=files[0].getPath();
                	if(files.length>1) {
                		FilePathShow="共"+files.length+"个文件";
                	}
                	 file1_name.setText(FilePathShow);
                }
        	}
        });
 
        //导入情况显示框
        JTextField ImportNum = new JTextField();
        ImportNum.setBounds(120,210,350,40);
        ImportNum.setOpaque(false);
        ImportNum.setBorder(null);
        ImportNum.setFont(new Font("微软雅黑", Font.PLAIN, 18) );
        ImportNum.setForeground(Color.RED);
        panel.add(ImportNum); 
        
        //导入模型
        JButton ImportData = new JButton("导入数据");
        ImportData.setBounds(10,210, 100, 25);
        panel.add(ImportData);
        ImportData.addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		if (con1!=null&&files!=null) {
        			ImportNum.setText("导入中...");
        		}else {
        			ImportNum.setText("连接失败或未找到文件！");
        		}
        			class MyThread extends Thread {
        				   public void run(){
        					 int num_imoprt=0;//导入条数
        					 long startTime=System.currentTimeMillis();
        				     con1.connection=con1.connect(IP1_text.getText(),user1_text.getText(),password1_text.getText());
        				     try {
							     num_imoprt=con1.InsertToModel(Integer.parseInt(start_index_text.getText()),picurl_text.getText(),version_text.getText(),table_name_text.getText(),files);//导入数据
						     } catch (NumberFormatException e) {
							     e.printStackTrace();
						     }
        				     con1.CloseConnect();
        				     long endTime=System.currentTimeMillis(); 
        				     ImportNum.setText("导入完成！"+"共计"+num_imoprt+"条，"+"耗时"+(endTime-startTime)/1000+"秒。");
        				  }
        				}
        			MyThread myThread = new MyThread();
        			myThread.start();
        	}
        });
        
        //底部说明
        JTextArea Illustration = new JTextArea("使用说明：\n1.输入PG数据库的IP、登录密码、模型表名、模型版本等参数\n"
        		+ "2.点击<测试连接>查看当前模型在表中最大和最小的index\n"
        		+ "3.点击<选择文件>选择一个或多个文件，右侧将显示文件名或文件数量\n"
        		+ "4.输入起始编号index，默认为当前最大值加1\n"
        		+ "5.点击<导入数据>，开始导入，直至右侧提示变为“导入完成！”");
        Illustration.setBounds(10,250,470,120);
        Illustration.setOpaque(false);
        panel.add(Illustration);
        ImageIcon i = new ImageIcon(UI.class.getResource("/jpg/timg.gif"));
        i.setImage(i.getImage().getScaledInstance(120,120,Image.SCALE_DEFAULT));
        JLabel gif1 = new JLabel(i);
        gif1.setBounds(360,340,120,120);
        panel.add(gif1);

        
    }
	}

