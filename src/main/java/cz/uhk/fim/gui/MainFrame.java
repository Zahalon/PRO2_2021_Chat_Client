package cz.uhk.fim.gui;

import cz.uhk.fim.models.ChatClient;
import cz.uhk.fim.models.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    JTextField txtInputName, txtInputMessage;
    JTextArea txtAreaChat;
    JTable tblLoggedUsers;
    JButton btnLogin, btnSendMessage;

    LoggedUsersTableModel loggedUsersTableModel;

    ChatClient chatClient;

    public MainFrame(int width, int height, ChatClient chatClient){
        super("PRO2 Chat client");
        this.chatClient = chatClient;
        setSize(width, height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initGui();
    }

    private void initGui(){
        JPanel panelMain = new JPanel(new BorderLayout());

        JPanel panelLogin = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelChat = new JPanel();
        JPanel panelLoggedUsers = new JPanel();
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.LEFT));

        initLoginPanel(panelLogin);
        initChatPanel(panelChat);
        initLoggedUsersPanel(panelLoggedUsers);
        initFooterPanel(panelFooter);

        panelMain.add(panelLogin, BorderLayout.NORTH);
        panelMain.add(panelChat, BorderLayout.CENTER);
        panelMain.add(panelLoggedUsers, BorderLayout.EAST);
        panelMain.add(panelFooter, BorderLayout.SOUTH);
        add(panelMain);
    }

    private void initLoginPanel(JPanel panel){
        panel.add(new JLabel("Jméno"));
        txtInputName = new JTextField("", 30);
        panel.add(txtInputName);
        btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("login clicked - " + txtInputName.getText());

                if(chatClient.isAuthenticated()){
                    chatClient.logout();
                    btnLogin.setText("Login");
                    txtInputName.setEditable(true);
                    txtAreaChat.setEnabled(false);
                }
                else{
                    String userName = txtInputName.getText();
                    if(userName.length()<1){
                        JOptionPane.showMessageDialog(null,
                                "Enter your username",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    chatClient.login(userName);
                    btnLogin.setText("Logout");
                    txtInputName.setEditable(false);
                    txtAreaChat.setEnabled(true);
                }
            }
        });
        panel.add(btnLogin);
    }

    private void initChatPanel(JPanel panel){
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        txtAreaChat = new JTextArea();
        txtAreaChat.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtAreaChat);
        /*for (int i = 0; i < 50; i++) {
            txtAreaChat.append("Message " + i + "\n");
        }*/
        chatClient.addActionListenerMessagesChanged(e -> refreshMessages());
        panel.add(scrollPane);
    }

    private void initLoggedUsersPanel(JPanel panel){
        Object[][] data = new Object[][]{
                {"1,1", "1,2"},
                {"2,1", "2,2"},
                {"xxxx", "yyyyy"}
        };
        String[] colNames = new String[] {"Column 1 X", "Column 2 Y"};
        //tblLoggedUsers = new JTable(data, colNames);

        loggedUsersTableModel = new LoggedUsersTableModel(chatClient);
        tblLoggedUsers = new JTable();
        tblLoggedUsers.setModel(loggedUsersTableModel);

        chatClient.addActionListenerLoggedUsersChanged(e -> {
            loggedUsersTableModel.fireTableDataChanged();
        });

        JScrollPane scrollPane = new JScrollPane(tblLoggedUsers);
        scrollPane.setPreferredSize(new Dimension(250,500));
        panel.add(scrollPane);
    }

    private void initFooterPanel(JPanel panel){
        txtInputMessage = new JTextField("", 50);
        panel.add(txtInputMessage);

        btnSendMessage = new JButton("Send");
        btnSendMessage.addActionListener(e -> {
            String text = txtInputMessage.getText();
            if(text.length() == 0)
                return;
            if(!chatClient.isAuthenticated())
                return;
            chatClient.sendMessage(text);
            txtInputMessage.setText("");
        });
        panel.add(btnSendMessage);
    }

    private void refreshMessages(){
        if(!chatClient.isAuthenticated())
            return;

        txtAreaChat.setText("");
        for (Message msg: chatClient.getMessages()) {
            txtAreaChat.append(msg.toString());
            txtAreaChat.append("\n");
        }
    }
}
