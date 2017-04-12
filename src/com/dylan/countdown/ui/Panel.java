package com.dylan.countdown.ui;

import com.dylan.countdown.Clock;
import com.dylan.countdown.constants.button_enum;
import com.dylan.countdown.constants.label_enum;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Dylan于东海 on 2017/4/11.
 */
public class Panel extends JFrame {

    JPanel p_time, p_buttons; //面板

    JLabel[] labels; //标签

    JTextField[] textFields; //输入框

    JButton[] buttons; //按钮

    int xOld = 0;
    int yOld = 0;

    static final Font font = new Font("宋体", Font.BOLD, 14);
    static final Color bgColor = new Color(0, 191, 255);
    static final Color fontColor = new Color(22, 22, 255);
    Clock clock; //倒计时时钟

    public Panel(){
        //创建面板
        p_time = new JPanel();
        p_buttons = new JPanel();

        //初始化标签数组
        labels = new JLabel[label_enum.values().length];
        //初始化输入框数组
        textFields = new JTextField[label_enum.values().length];
        //初始化按钮数组
        buttons = new JButton[button_enum.values().length];

        //添加标签
        for (label_enum text: label_enum.values()){
            //标签
            JLabel label = new JLabel(text.toString());
            label.setFont(font);
            label.setForeground(fontColor);
            labels[text.ordinal()] = label;
            //文本框
            JTextField field = new JTextField(4);
            field.setBorder(new EmptyBorder(0,0,0,0));
            field.setHorizontalAlignment(JTextField.RIGHT);
            field.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    int keyChar = e.getKeyChar();
                    if (keyChar < KeyEvent.VK_0 || keyChar > KeyEvent.VK_9) {
                        e.consume();
                    }
                }
            });
            textFields[text.ordinal()] = field;
        }
        //添加按钮
        for (button_enum text: button_enum.values()){
            JButton jButton = new JButton(text.toString());
            jButton.setFont(font);
            jButton.setForeground(fontColor);
            jButton.setBorder(new EmptyBorder(8, 8, 8, 8));
            buttons[text.ordinal()] = jButton;
        }

        //设置布局管理
        this.setLayout(new GridLayout(2, 1));//网格式布局

        //加入各个组件
        for (label_enum l: label_enum.values()){
            p_time.add(textFields[l.ordinal()]);
            p_time.add(labels[l.ordinal()]);
        }

        for (button_enum b: button_enum.values()){
            p_buttons.add(buttons[b.ordinal()]);
        }

        //加入到JFrame
        this.add(p_time);
        this.add(p_buttons);

        //设置窗体
        this.setTitle("countdown");//窗体标签
        this.setSize(250, 80);//窗体大小
        this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//退出关闭JFrame
        this.setUndecorated(true);
        this.setVisible(true);//显示窗体

        //鼠标移动窗口
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                xOld = e.getX();//记录鼠标按下时的坐标
                yOld = e.getY();
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int xOnScreen = e.getXOnScreen();
                int yOnScreen = e.getYOnScreen();
                int xx = xOnScreen - xOld;
                int yy = yOnScreen - yOld;
                Panel.this.setLocation(xx, yy);//设置拖拽后，窗口的位置
            }
        });

        //关闭窗口
        buttons[button_enum.关闭.ordinal()].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttons[button_enum.开始.ordinal()].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startCountdown();
            }
        });

        buttons[button_enum.重置.ordinal()].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        p_buttons.setBackground(bgColor);
        p_time.setBackground(bgColor);

        //锁定窗体
        this.setResizable(false);
        clock = new Clock();
    }

    public void rePaint(int h, int m, int s){
        textFields[label_enum.小时.ordinal()].setText(h + "");
        textFields[label_enum.分钟.ordinal()].setText(m + "");
        textFields[label_enum.秒.ordinal()].setText(s + "");
    }

    public void alarm(){
        int x = Panel.this.getLocation().x;
        int y = Panel.this.getLocation().y;
        for (int i=0;i<100;i++) {
            if (i%2 == 0){
                Panel.this.setLocation(x+5, y);
            }else {
                Panel.this.setLocation(x-5, y);
            }
            try {
                Thread.sleep(20);
            }catch (InterruptedException e) {

            }
        }
    }

    private void startCountdown(){
        for (label_enum item : label_enum.values()){
            String text = textFields[item.ordinal()].getText().trim();
            int value = 0;
            if (!text.equals("")){
                value = Integer.parseInt(text);
            }
            switch (item){
                case 小时:
                    clock.setHours(value);
                    break;
                case 分钟:
                    clock.setMinutes(value);
                    break;
                case 秒:
                    clock.setSeconds(value);
                    break;
            }
        }

        clock.start(Panel.this);
    }

    public void reset(){
        clock.reset();
        for (JTextField field : textFields){
            field.setText("0");
        }
    }
}
