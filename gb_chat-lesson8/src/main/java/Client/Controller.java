package Client;

import Server.DBAuthService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import jdk.internal.access.JavaIOFileDescriptorAccess;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String nick;


    @FXML
    private AnchorPane clientPanel;
    @FXML
    private AnchorPane msgPanel;
    @FXML
    private AnchorPane regPanel;
    @FXML
    private TextField textField;
    @FXML
    private Button btnSend;
    @FXML
    private ListView<String> clientList;
    @FXML
    private TextArea textArea;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField loginField;
    @FXML
    private AnchorPane authPanel;
    private boolean isAuthSuccess;

    @FXML
    TextField regLogin;

    @FXML
    PasswordField regPassword;

    @FXML
    TextField regNick;

    private void connect() {
        try {
            this.socket = new Socket("localhost", 8189);
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            setAuth(false);
            new Thread(this::closeClient).start();
            new Thread(() -> {
                try {
                    while (true) {
                        final String msgAuth = in.readUTF();
                        System.out.println("CLIENT: Received message: " + msgAuth);
                        if (msgAuth.startsWith("/authok")) {
                            setAuth(true);
                            loadHistory();

                            nick = msgAuth.split("\\s")[1];
                            textArea.appendText("Успешная авторизация под ником " + nick + "\n");
                            break;
                        }
                        textArea.appendText(msgAuth + "\n");
                    }
                    while (true) {
                        String msgFromServer = in.readUTF();
                        System.out.println("CLIENT: Received message: " + msgFromServer);
                        if (msgFromServer.startsWith(nick)) {
                            msgFromServer = "[You] " + msgFromServer;
                        }
                        if ("/end".equalsIgnoreCase(msgFromServer)) {
                            break;
                        }
                        if (msgFromServer.startsWith("/clients")) {
                            final List<String> clients = new ArrayList<>(Arrays.asList(msgFromServer.split("\\s")));
                            clients.remove(0);
                            clientList.getItems().clear();
                            clientList.getItems().addAll(clients);
                            continue;
                        }
                        textArea.appendText(msgFromServer + "\n");
                        SaveHistory();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } finally {
                    try {
                        setAuth(false);

                        socket.close();
                        nick = "";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public void registration() {

        DBAuthService.setNewUsers(regLogin.getText(), regPassword.getText(), regNick.getText());
        System.out.println("Поздравляем с успешной регистрацией.");
        regLogin.clear();
        regPassword.clear();
        regNick.clear();
    }

    private void setAuth(boolean isAuthSuccess) {
        this.isAuthSuccess = isAuthSuccess;
        authPanel.setVisible(!isAuthSuccess);
        authPanel.setManaged(!isAuthSuccess);

        msgPanel.setVisible(isAuthSuccess);
        msgPanel.setManaged(isAuthSuccess);

        clientPanel.setVisible(isAuthSuccess);
        clientPanel.setManaged(isAuthSuccess);
    }

    public void sendAuth(ActionEvent actionEvent) {
        if (socket == null || socket.isClosed()) {
            connect();
        }

        try {
            System.out.println("CLIENT: Send auth message");
            out.writeUTF("/auth " + loginField.getText() + " " + passwordField.getText());
            loginField.clear();
            passwordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(ActionEvent actionEvent) {
        try {
            final String msg = textField.getText();
            System.out.println("CLIENT: Send message to server: " + msg);
            out.writeUTF(msg);
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connect();
    }

    public void selectClient(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            final String msg = textField.getText();
            String nickname = clientList.getSelectionModel().getSelectedItem();
            textField.setText("/w " + nickname + " " + msg);
            textField.requestFocus();
            textField.selectEnd();
        }
    }

    public void closeClient() {
        try {
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!isAuthSuccess) {
            try {
                socket.close();
                Platform.exit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void SaveHistory() throws IOException {
        try {
            File history = new File("history.txt");
            if (!history.exists()) {
                System.out.println("Файла истории нет,создадим его");
                history.createNewFile();
            }
            PrintWriter fileWriter = new PrintWriter(new FileWriter(history, false));

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(textArea.getText());
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadHistory() throws IOException {
        int posHistory = 100;
        File history = new File("history.txt");
        List<String> historyList = new ArrayList<>();
        FileInputStream in = new FileInputStream(history);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        String temp;
        while ((temp = bufferedReader.readLine()) != null) {
            historyList.add(temp);
        }

        if (historyList.size() > posHistory) {
            for (int i = historyList.size() - posHistory; i <= (historyList.size() - 1); i++) {
                textArea.appendText(historyList.get(i) + "\n");
            }
        } else {
            for (int i = 0; i < posHistory; i++) {
                System.out.println(historyList.get(i));
            }
        }
    }
}