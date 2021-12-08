package cz.uhk.fim.models.chatFileOperations;

import cz.uhk.fim.models.Message;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvChatFileOperations implements ChatFileOperations {
    private static final String MESSAGE_FILE = "./messages.csv";
    private static final String LOGGED_USERS_FILE = "./loggedUsers.csv";

    @Override
    public List<Message> loadMessages() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(MESSAGE_FILE));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            csvParser.getRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void writeMessagesToFile(List<Message> messages) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(MESSAGE_FILE));
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            for (Message message : messages) {
                csvPrinter.printRecord(message.getAuthor(),message.getText(),message.getCreated());



            }
            writer.write(csvPrinter.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> loadLoggedUsers() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(LOGGED_USERS_FILE));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            csvParser.getRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void writeLoggedUsersToFile(List<String> users) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(LOGGED_USERS_FILE));
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            for (String user : users) {
                csvPrinter.printRecord(user);
            }
            writer.write(csvPrinter.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
