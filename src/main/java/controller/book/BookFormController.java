package controller.book;

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
import model.Book;
import model.tm.BookTM;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BookFormController implements Initializable {

    @FXML
    private TableColumn colAuthor;

    @FXML
    private TableColumn colCategory;

    @FXML
    private TableColumn colID;

    @FXML
    private TableColumn colQuantity;

    @FXML
    private TableColumn colTitle;

    @FXML
    private TableView<BookTM> tblBook;

    @FXML
    private JFXTextField txtAuthor;

    @FXML
    private JFXTextField txtBookId;

    @FXML
    private JFXTextField txtBookTitle;

    @FXML
    private JFXTextField txtCategory;

    @FXML
    private JFXTextField txtQuantity;

    @FXML
    void btnAddBookOnAction(ActionEvent event) {

        String bid=txtBookId.getText();
        String btitle=txtBookTitle.getText();
        String author=txtAuthor.getText();
        String category=txtCategory.getText();
        Integer quantity= Integer.valueOf(txtQuantity.getText());

        Book book=new Book(bid,btitle,author,category,quantity);
        System.out.println(book);

        try {
            Connection connection=DBConnection.getInstance().getConnection();
            PreparedStatement psTm=connection.prepareStatement("INSERT INTO book VALUES(?,?,?,?,?)");
            psTm.setString(1, book.getBid());
            psTm.setString(2, book.getBtitle());
            psTm.setString(3, book.getAuthor());
            psTm.setString(4, book.getCategory());
            psTm.setString(5,book.getQuantity().toString());

            if(psTm.executeUpdate()>0){
                new Alert(Alert.AlertType.INFORMATION,"book Added").show();
                loadTable();
            }else{
                new Alert(Alert.AlertType.ERROR,"book not Added").show();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
      
    }

    @FXML
    void btnDeleteBookOnAction(ActionEvent event) {
        try {
            Connection connection= DBConnection.getInstance().getConnection();
            PreparedStatement psTm= connection.prepareStatement("DELETE FROM book WHERE bid=?");
            psTm.setString(1,txtBookId.getText());
            if(psTm.executeUpdate()>0){
                new Alert(Alert.AlertType.INFORMATION,"book Deleted").show();
                loadTable();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void btnReloadBookOnAction(ActionEvent event) {
        loadTable();

    }
    public void loadTable() {
        colID.setCellValueFactory(new PropertyValueFactory<>("bid"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("btitle"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        try {
            Connection connection=DBConnection.getInstance().getConnection();
            System.out.println(connection);
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT * FROM book");
            ArrayList<BookTM> bookTMS=new ArrayList<>();

            while (resultSet.next()){
                bookTMS.add( new BookTM(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getInt(5)
                ));

            }
            ObservableList<BookTM> observableList = FXCollections.observableArrayList(bookTMS);
            tblBook.setItems(observableList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }




    }


                @FXML
    void btnSearchBookOnAction(ActionEvent event) {
                    try {
                        //Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/bookrental","root","1234");
                        Connection connection= DBConnection.getInstance().getConnection();
                        PreparedStatement psTm= connection.prepareStatement("SELECT * FROM book WHERE bid=?");
                        psTm.setString(1,txtBookId.getText());
                        ResultSet resultSet= psTm.executeQuery();
                        resultSet.next();
                        Book book = new Book(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getInt(5)
                        );
                        System.out.println(book);
                        setTextToValues(book);


                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
    }



    private void setTextToValues(Book book){


        txtBookId.setText(book.getBid());
        txtBookTitle.setText(book.getBtitle());
        txtAuthor.setText(book.getAuthor());
        txtCategory.setText(book.getCategory());
        txtQuantity.setText(book.getQuantity().toString());

        //String title = cmbTitle.getValue().toString();
        //LocalDate dobValue = dateDob.getValue();
        //Double salary = Double.parseDouble(txtSalary.getText());
        //dateDob.setValue(customer.getDobValue());
        //txtSalary.setText(customer.getSalary().toString());
        //txtAddress.setText(customer.getAddress());
        //String title = cmbTitle.getValue().toString();
        //cmbTitle.setValue(customer.getTitle());



    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
        colID.setCellValueFactory(new PropertyValueFactory<>("bid"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("btitle"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        tblBook.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            System.out.println("New value"+newValue);
            assert newValue != null;
            //setTextToValues((Rental) newValue);
            setTextToValue((BookTM) newValue);
        });

        //tblBook.getSelectionModel().selectedItemProperty().addListener((ObservableValue observableValue,o,t1));

    }
    private void setTextToValue(BookTM bookTM){


        txtBookId.setText(bookTM.getBid());
        txtBookTitle.setText(bookTM.getBtitle());
        txtAuthor.setText(bookTM.getAuthor());
        txtCategory.setText(bookTM.getCategory());
        txtQuantity.setText(bookTM.getQuantity().toString());

        //String title = cmbTitle.getValue().toString();
        //LocalDate dobValue = dateDob.getValue();
        //Double salary = Double.parseDouble(txtSalary.getText());
        //dateDob.setValue(customer.getDobValue());
        //txtSalary.setText(customer.getSalary().toString());
        //txtAddress.setText(customer.getAddress());
        //String title = cmbTitle.getValue().toString();
        //cmbTitle.setValue(customer.getTitle());



    }
}