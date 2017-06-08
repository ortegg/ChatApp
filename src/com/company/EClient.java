package com.company;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

/**
 * Created by EdwinOrtega on 5/23/17.
 */
public class EClient {
    BufferedReader in;
    PrintWriter out;
    JFrame frame = new JFrame("ChatApp");
    //JSplitPane frame2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);
    private javax.swing.JList online_list;

    //client constructor
    public EClient() {
        //chat window GUI
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "South");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();
        frame.setResizable(true);
        online_list = new javax.swing.JList();

        online_list.setModel(new javax.swing.AbstractListModel() {
        String[] online_users = {"user1", "user2"};
        public int getSize() { return online_users.length; }
        public Object getElementAt(int i) { return online_users[i]; }
        });

        online_list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        online_list.setFixedCellWidth(80);
        frame.getContentPane().add(online_list, "East");
        // Add Listeners
        textField.addActionListener(new ActionListener() {
            //when hit enter it sends the message to screen.
            public void actionPerformed(ActionEvent e){
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }
    //prompt for log in or register.
    private String getName() {
        return JOptionPane.showInputDialog(frame,"Username:",
                "Login/Register to ChatApp", JOptionPane.OK_CANCEL_OPTION);
    }
    //connect to server then enters the processing loop.
    public void run() throws IOException{
        // Make connection and initialize streams
        Socket socket = new Socket("127.0.0.1", 1955);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Process all messages from server, according to the protocol.
        while (true) {
            String line = in.readLine();
            if(line.startsWith("SUBMITNAME")) {
                out.println(getName());
            }
            if(line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            }
            else if(line.startsWith("MESSAGE")) {
                messageArea.append(line.substring(8) + "\n");
            }
        }
    }
    //runs the user and allows user to leave with a closeable frame.
    public static void main(String[] args) throws Exception {
        Login client = new Login();
        client.start();
        if(client.connect == 1) {
            client.connect = 0;
            EClient user = new EClient();
            user.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            user.frame.setVisible(true);
            user.run();
        }
    }
}
