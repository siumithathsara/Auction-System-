package lk.ijse.realtimeauctionsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientViewController implements Initializable {

    @FXML
    private Label lblHighestBid;

    @FXML
    private Label lblItemName;

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtField;

    @FXML
    private TextField txtUsername;

    DataInputStream din;
    DataOutputStream dOut;
    String message ="";
    Socket socket;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        new Thread(() -> {
           try {
                socket = new Socket("127.0.0.1", 6000);
               din = new DataInputStream(socket.getInputStream());
                while (!message.equals("finished")) {
                   message = din.readUTF();

                    javafx.application.Platform.runLater(() -> {
                       txtArea.appendText( "me -"+ message + "\n");

            });
                }

            } catch (IOException e) {
                txtArea.appendText("IOException: " + e.getMessage() + "\n");
           }

        }).start();
    }

    @FXML
    void handleDisConnectBtn(ActionEvent event) {
     try {
         socket.close();
         din.close();
         dOut.close();
     }catch (Exception e){
         throw new RuntimeException(e);
     }
    }

    @FXML
    void placeBid(ActionEvent event) throws IOException {

        dOut = new DataOutputStream(socket.getOutputStream());
        dOut.writeUTF(txtField.getText());
        dOut.flush();
    }
    @FXML
    void handleUsername() throws IOException {

        din = new DataInputStream(socket.getInputStream());
        dOut = new DataOutputStream(socket.getOutputStream());
        String username = txtUsername.getText();
        dOut.writeUTF(username);
        dOut.flush();
    }

}
