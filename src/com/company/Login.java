package com.company;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.util.Map;
import java.util.TreeMap;
import java.io.*;

/**
 * Created by EdwinOrtega on 5/23/17.
 */
public class Login {
    public static JPasswordField passwordText = new JPasswordField(20);
    public static JTextField userText = new JTextField(20);
    public static JButton registerButton = new JButton("Register");
    public static JButton loginButton = new JButton("Login");
    private static TreeMap<String,String> userInfo;
    public static boolean connect = false;

    public static void start() {
        insert_users();
        //print_users();
        JFrame frame = new JFrame("ChatApp");
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        placeComponents(frame);
        frame.setVisible(true);
        while(!connect) {
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String user = userText.getText();
                    char[] pass1 = passwordText.getPassword();
                    String pass = new String(pass1);
                    connect = authorized(user, pass);
                    int num = 0;
                    if(connect){
                        frame.dispose();
                        num = 0;
                    }
                    else {
                        //JOptionPane.showMessageDialog(frame, "username or password is incorrect, try again.",
                          //      "Username/Password Incorrect", JOptionPane.WARNING_MESSAGE);
                        int exit = JOptionPane.showConfirmDialog(null, "Username or Password is incorrect, try again",
                                "Username/Password Incorrect", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                        if (exit == JOptionPane.YES_OPTION)
                        {
                            exit = 0;
                        }
                    }
                }
            });
            registerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String user = userText.getText();
                    char[] pass1 = passwordText.getPassword();
                    String pass = new String(pass1);
                    add_user(user, pass);
                }
            });
        }
    }
    public static void add_user(String username, String pass){
        for(Map.Entry<String, String> entry : userInfo.entrySet()) {
            String key = entry.getKey();
            if(username.equals(key)) {
                System.out.println("Username already in use, try again.");
                return;
            }
        }
        userInfo.put(username, pass);
        try{
            FileWriter out = new FileWriter("src/user_info", true);
            out.write("\n" + username + ":" + pass);
            out.close();             //close the file
        }
        catch(IOException e){       //throw exception if file fails
            System.out.println("Failed to write to file.\n");
        }
    }
    public static void insert_users(){
        try{
            BufferedReader in = new BufferedReader(new FileReader("src/user_info"));
            String [] info;
            String check;
            String username;
            String password;

            userInfo = new TreeMap();

            while((check = in.readLine()) != null){
                info = check.split(":");
                username = info[0];
                password = info[1];
                userInfo.put(username, password);
            }
            in.close();             //close the file
        }
        catch(IOException e){       //throw exception if file fails
            System.out.println("Failed to read file.\n");
        }
    }
    public static void print_users(){
        for(Map.Entry<String, String> entry : userInfo.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.printf("%s : %s\n", key, value);
        }
    }
    public static boolean authorized(String user, String pass){
        for(Map.Entry<String,String> entry : userInfo.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if(key.equals(user) && value.equals(pass)){
                return true;
            }
        }
        return false;
    }
    private static void placeComponents(JFrame frame) {
        frame.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 10, 80, 25);
        frame.add(userLabel);

        //JTextField userText = new JTextField(20);
        userText.setBounds(100, 10, 160, 25);
        frame.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 40, 80, 25);
        frame.add(passwordLabel);

        //JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 40, 160, 25);
        frame.add(passwordText);

        //JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        frame.add(loginButton);

//        JButton registerButton = new JButton("Register");
        registerButton.setBounds(180, 80, 80, 25);
        frame.add(registerButton);
    }
}
