package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class DashBoardController {

    @FXML
    private AnchorPane dashRoot;

    @FXML
    void btnBookFormOnAction(ActionEvent event) {
        try {
            URL resource = this.getClass().getResource("/view/book_form.fxml");
            assert resource != null;

            Parent parent = FXMLLoader.load(resource);
            dashRoot.getChildren().clear();
            dashRoot.getChildren().add(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnCustomerFormOnAction(ActionEvent event) {
        try {
            URL resource = this.getClass().getResource("/view/customer_form.fxml");
            assert resource != null;

            Parent parent = FXMLLoader.load(resource);
            dashRoot.getChildren().clear();
            dashRoot.getChildren().add(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }





    }


    public void btnRentalFormOnAction(ActionEvent actionEvent) {

        URL resource =this.getClass().getResource("/view/rental_form.fxml");
        assert  resource!=null;
        Parent parent=null;
        try {
            parent=FXMLLoader.load(resource);
            dashRoot.getChildren().clear();
            dashRoot.getChildren().add(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
