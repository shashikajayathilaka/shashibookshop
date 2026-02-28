package controller.rental;

import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Rental;
import model.tm.RentalTM;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RentalFormController implements Initializable {

    @FXML
    private TableColumn colBookId;

    @FXML
    private TableColumn colCustomerId;

    @FXML
    private TableColumn colIssueDate;

    @FXML
    private TableColumn colReturnDate;

    @FXML
    private DatePicker dateIssueDate;

    @FXML
    private DatePicker dateReturnDate;

    @FXML
    private TableView tblRental;

    @FXML
    private JFXTextField txtBookId;

    @FXML
    private JFXTextField txtCustomerId;

    @FXML
    void btnAddRentalOnAction(ActionEvent event) {
        String bid= txtBookId.getText();
        String id=txtCustomerId.getText();
        LocalDate issuedate = dateIssueDate.getValue();
        LocalDate returndate =dateReturnDate.getValue();

       Rental rental=new Rental(bid,id,issuedate,returndate);
        System.out.println(rental);
        try {
            //Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/bookrental","root","1234");

            Connection connection= DBConnection.getInstance().getConnection();
            PreparedStatement psTm= connection.prepareStatement("INSERT INTO rental VALUES(?,?,?,?)");
            psTm.setString(1,rental.getBid());
            psTm.setString(2, rental.getId());
            psTm.setDate(3, Date.valueOf(rental.getIssuedate()));
            psTm.setDate(4, Date.valueOf(rental.getReturndate()));


            if(psTm.executeUpdate()>0){
                new Alert(Alert.AlertType.INFORMATION,"rental Added").show();
                loadTable();
            }else{
                new Alert(Alert.AlertType.ERROR,"rental not Added").show();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnDeleteRentalOnAction(ActionEvent event) {
        try {
            Connection connection= DBConnection.getInstance().getConnection();
            PreparedStatement psTm= connection.prepareStatement("DELETE FROM rental WHERE bid=?");
            psTm.setString(1,txtBookId.getText());
            if(psTm.executeUpdate()>0){
                new Alert(Alert.AlertType.INFORMATION,"rental Deleted").show();
                loadTable();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnReloadRentalOnAction(ActionEvent event) {
        loadTable();
    }

    @FXML
    void btnSearchRentalOnAction(ActionEvent event) {
        try {
            //Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/bookrental","root","1234");
            Connection connection= DBConnection.getInstance().getConnection();
            PreparedStatement psTm= connection.prepareStatement("SELECT * FROM rental WHERE bid=?");
            psTm.setString(1,txtBookId.getText());
            ResultSet resultSet= psTm.executeQuery();
            resultSet.next();

            Rental rental= new Rental(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDate(3).toLocalDate(),
                    resultSet.getDate(4).toLocalDate()
            );


            System.out.println(rental);
            setTextToValues(rental);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private void setTextToValues(Rental rental){

        txtBookId.setText(rental.getBid());
        txtCustomerId.setText(rental.getId());
        dateIssueDate.setValue(rental.getIssuedate());
        dateReturnDate.setValue(rental.getReturndate());


    }
    public void loadTable(){
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bid"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIssueDate.setCellValueFactory(new PropertyValueFactory<>("issuedate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returndate"));

        try {
            //Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/bookrental","root","1234");
            Connection connection= DBConnection.getInstance().getConnection();
            System.out.println(connection);
            Statement statement=connection.createStatement();
            ResultSet resultSet= statement.executeQuery("SELECT * FROM rental");
            ArrayList<RentalTM> rentalTMS=new ArrayList<>();

            while (resultSet.next()){
                rentalTMS.add(
                        new RentalTM(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getDate(3).toLocalDate(),
                                resultSet.getDate(4).toLocalDate()
                        )
                );

            }

            ObservableList<RentalTM> observableList = FXCollections.observableArrayList(rentalTMS);
            tblRental.setItems(observableList);
            //tblCustomer.setItems(observableList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bid"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIssueDate.setCellValueFactory(new PropertyValueFactory<>("issuedate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returndate"));

        tblRental.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            System.out.println("New value"+newValue);
            assert newValue != null;
            //setTextToValues((Rental) newValue);
            setTextToValue((RentalTM) newValue);
        });

    }
    private void setTextToValue(RentalTM rentalTM){

        txtBookId.setText(rentalTM.getBid());
        txtCustomerId.setText(rentalTM.getId());
        dateIssueDate.setValue(rentalTM.getIssuedate());
        dateReturnDate.setValue(rentalTM.getReturndate());


    }
}
