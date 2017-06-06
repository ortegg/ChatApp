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

    /**
     * Constructs the user by laying out the GUI and registering a
     * listener with the textfield so that pressing Return in the
     * listener sends the textfield contents to the server.  Note
     * however that the textfield is initially NOT editable, and
     * only becomes editable AFTER the user receives the NAMEACCEPTED
     * message from the server.
     */
    public EClient() {

        //chat window GUI
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "South");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();

        // Add Listeners
        textField.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the textfield by sending
             * the contents of the text field to the server.    Then clear
             * the text area in preparation for the next message.
             */
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
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
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
        //Login client = new Login();
        //client.start();
        //System.out.println("left login button 1");
        //if(client.connect) {

        EClient user = new EClient();
        user.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        user.frame.setVisible(true);
        user.run();
        //}
    }
}
