package cz.uhk.fim;

import org.apache.derby.tools.ij;

public class RunDbConsole {
    public static void main(String[] args) {
        try {
            //connect 'jdbc:derby:ClientChatDb;create=true';
            ij.main(args);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
