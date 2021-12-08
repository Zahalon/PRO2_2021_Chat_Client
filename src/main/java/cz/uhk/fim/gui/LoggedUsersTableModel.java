package cz.uhk.fim.gui;

import cz.uhk.fim.models.ChatClient;

import javax.swing.table.AbstractTableModel;

public class LoggedUsersTableModel extends AbstractTableModel {
    ChatClient chatClient;
    public LoggedUsersTableModel(ChatClient client){
        this.chatClient = client;
    }

    @Override
    public int getRowCount() {
        return chatClient.getLoggedUsers().size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return chatClient.getLoggedUsers().get(rowIndex);
    }

    @Override
    public String getColumnName(int column) {
        switch (column){
            case 0:
                return "User name";
            default:
                return super.getColumnName(column);
        }
    }
}
