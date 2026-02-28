package controller.customer;

import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.tm.CustomerTM;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {



    @FXML
    private TableColumn colAddress;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colTelephoneno;

    @FXML
    private TableView<CustomerTM> tblCustomer;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtTelephoneno;

    @FXML
    void btnAddCustmerOnAction(ActionEvent event) {
        String id= txtId.getText();
        String name=txtName.getText();
        String address= txtAddress.getText();
        String telephoneno=txtTelephoneno.getText();

        Customer customer= new Customer(id,name,address,telephoneno);
        System.out.println(customer);
        try {
            //Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/bookrental","root","1234");

            Connection connection= DBConnection.getInstance().getConnection();
            PreparedStatement psTm= connection.prepareStatement("INSERT INTO customer VALUES(?,?,?,?)");
            psTm.setString(1,customer.getId());
            psTm.setString(2,customer.getName());
            psTm.setString(3,customer.getAddress());
            psTm.setString(4,customer.getTelephoneno());

             if(psTm.executeUpdate()>0){
                new Alert(Alert.AlertType.INFORMATION,"Customer Added").show();
                loadTable();
            }else{
                new Alert(Alert.AlertType.ERROR,"Customer not Added").show();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnReloadCustomerOnAction(ActionEvent event) {
        loadTable();
    }
    public void loadTable(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTelephoneno.setCellValueFactory(new PropertyValueFactory<>("telephoneno"));

        try {
            //Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/bookrental","root","1234");
            Connection connection= DBConnection.getInstance().getConnection();
            System.out.println(connection);
            Statement statement=connection.createStatement();
            ResultSet resultSet= statement.executeQuery("SELECT * FROM customer");
            ArrayList<CustomerTM> customerTMS=new ArrayList<>();

            while (resultSet.next()){
                customerTMS.add(
                        new CustomerTM(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4)
                        )
                );

            }

            ObservableList<CustomerTM> observableList = FXCollections.observableArrayList(customerTMS);
            tblCustomer.setItems(observableList);
            //tblCustomer.setItems(observableList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadTable();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTelephoneno.setCellValueFactory(new PropertyValueFactory<>("telephoneno"));

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            System.out.println("New value"+newValue);
            assert newValue != null;
            //setTextToValues((Rental) newValue);
            setTextToValue((CustomerTM) newValue);
        });
    }

    public void btnDeleteCustomerOnAction(ActionEvent actionEvent) {

        try {
            Connection connection= DBConnection.getInstance().getConnection();
            PreparedStatement psTm= connection.prepareStatement("DELETE FROM customer WHERE id=?");
            psTm.setString(1,txtId.getText());
            if(psTm.executeUpdate()>0){
                new Alert(Alert.AlertType.INFORMATION,"Customer Deleted").show();
                loadTable();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void btnSearchCustomerOnAction(ActionEvent actionEvent) {
        try {
            //Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/bookrental","root","1234");
            Connection connection= DBConnection.getInstance().getConnection();
            PreparedStatement psTm= connection.prepareStatement("SELECT * FROM customer WHERE id=?");
            psTm.setString(1,txtId.getText());
            ResultSet resultSet= psTm.executeQuery();
            resultSet.next();
            Customer customer= new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );
            System.out.println(customer);
            setTextToValues(customer );


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void setTextToValues(Customer customer){
        txtId.setText(customer.getId());
        txtName.setText(customer.getName());
        txtAddress.setText(customer.getAddress());
        txtTelephoneno.setText(customer.getTelephoneno());
    }
    private void setTextToValue(CustomerTM customerTM){
        txtId.setText(customerTM.getId());
        txtName.setText(customerTM.getName());
        txtAddress.setText(customerTM.getAddress());
        txtTelephoneno.setText(customerTM.getTelephoneno());
    }

    public void btnCustomerReportOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design=JRXmlLoader.load("src/main/resources/report/customersreport.jrxml");
            JasperReport jasperReport= JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,null,DBConnection.getInstance().getConnection());
            JasperExportManager.exportReportToPdfFile(jasperPrint,"customersreport.pdf");
            JasperViewer.viewReport(jasperPrint,false);

        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
