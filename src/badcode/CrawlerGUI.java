package badcode;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Timer;

public class CrawlerGUI extends JFrame{
    public JTextField t1,t2,t3,t4,t5;
    public JButton b1,b2,b3,b4,b5;
    public JButton bn1,bn2;
    public JPanel jp;
    public JTextField screen = new JTextField("0");

    public CrawlerGUI(){
        jp = new JPanel();
        jp.setVisible(true);
        setSize(530,530);

        t1 = new JTextField(50);
        t2 = new JTextField(50);
        t3 = new JTextField(50);
        t4 = new JTextField(50);
        t5 = new JTextField(50);

        t1.setBounds(130, 50, 250, 30);
        t2.setBounds(130, 100, 250, 30);
        t3.setBounds(130, 150, 250, 30);
        t4.setBounds(130, 200, 250, 30);
        t5.setBounds(130, 250, 250, 30);

        JLabel l1 = new JLabel("起始地址:");
        JLabel l2 = new JLabel("网页保存地址:");
        JLabel l3 = new JLabel("正文保存地址:");
        JLabel l4 = new JLabel("正则表达式:");
        JLabel l5 = new JLabel("线程数:");

        l1.setBounds(30, 50, 90, 30);
        l2.setBounds(30, 100, 90, 30);
        l3.setBounds(30, 150, 90, 30);
        l4.setBounds(30, 200, 90, 30);
        l5.setBounds(30, 250, 90, 30);

        b1 = new JButton("添加");
        b2 = new JButton("确认");
        b3 = new JButton("确认");
        b4 = new JButton("添加");
        b5 = new JButton("确认");

        b1.setBounds(400, 50, 90, 30);
        b2.setBounds(400, 100, 90, 30);
        b3.setBounds(400, 150, 90, 30);
        b4.setBounds(400, 200, 90, 30);
        b5.setBounds(400, 250, 90, 30);


        b1.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Main.crawler.setRootURL(t1.getText());
                t1.setText("");
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Fetcher.pagePath = t2.getText();
            }
        });

        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Fetcher.infoPath = t3.getText();
            }
        });

        b4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.crawler.addRegex(t4.getText());
                t4.setText("");
            }
        });

        b5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Main.crawler.setThreadsNum(Integer.parseInt(t5.getText()));
                } catch (Exception ex) {
                    t5.setText("");
                }
            }
        });

        JRadioButton rb1 = new JRadioButton("是否为主节点",false);
        JRadioButton rb2 = new JRadioButton("是否进行正文分析",false);

        rb1.setBounds(120, 300, 150, 30);
        rb2.setBounds(300, 300, 150, 30);

        screen.setEditable(false);
        screen.setBounds(180, 400, 150, 60);
        screen.setFont(new Font("幼圆", Font.BOLD, 60));

        JLabel l21 = new JLabel("已爬取了");
        JLabel l22 = new JLabel("条URL");
        l21.setBounds(120, 410, 60, 30);
        l22.setBounds(335, 410, 60, 30);

        bn1 = new JButton("开始");
        bn2 = new JButton("结束");
        bn1.setBounds(120, 350, 90, 30);
        bn2.setBounds(300, 350, 90, 30);

        bn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Timer timer = new Timer();
                timer.schedule(new MyTask(),100);

                if(rb1.isSelected()){
                    Main.crawler.setMaster();
                }
                if(rb2.isSelected()){
                    Main.crawler.setNews();
                }
                Main.crawler.start();

                timer.cancel();
            }
        });

        bn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.crawler = null;
            }
        });

        Container cont = getContentPane();
        cont.add(jp,BorderLayout.CENTER);

        jp.setLayout(null);
        jp.add(b1);
        jp.add(b2);
        jp.add(b3);
        jp.add(b4);
        jp.add(b5);

        jp.add(t1);
        jp.add(t2);
        jp.add(t3);
        jp.add(t4);
        jp.add(t5);

        jp.add(l1);
        jp.add(l2);
        jp.add(l3);
        jp.add(l4);
        jp.add(l5);

        jp.add(bn1);
        jp.add(bn2);

        jp.add(rb1);
        jp.add(rb2);

        jp.add(screen);
        jp.add(l21);
        jp.add(l22);

    }

    public static void main(String[] args){
        CrawlerGUI app=new CrawlerGUI();
        app.setVisible(true);

        app.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }

    class MyTask extends java.util.TimerTask{
        public void run(){

        }
    }

}


