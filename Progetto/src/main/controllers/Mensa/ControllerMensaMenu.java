package main.controllers.Mensa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import main.NormalClasses.Mensa.Dish;
import main.NormalClasses.Mensa.Menu;
import main.StringPropertyClasses.Mensa.StringPropertyDish;
import main.controllers.AbstractController;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerMensaMenu extends AbstractController implements Initializable {

    /*
        Add & Remove Buttons and close button
    */

    @FXML private ImageView addAntipastoIV;
    @FXML private ImageView addPrimoIV;
    @FXML private ImageView addSecondoIV;
    @FXML private ImageView addDolceIV;
    @FXML private ImageView removeAntipastoIV;
    @FXML private ImageView removePrimoIV;
    @FXML private ImageView removeSecondoIV;
    @FXML private ImageView removeDolceIV;
    @FXML private ImageView goHomeIV;

    /*
        Tables & Columns
    */

    @FXML private TableView<StringPropertyDish> antipastoTable;
    @FXML private TableView<StringPropertyDish> primoTable;
    @FXML private TableView<StringPropertyDish> secondoTable;
    @FXML private TableView<StringPropertyDish> dolceTable;

    @FXML private TableColumn <StringPropertyDish,String> antipastoColumn;
    @FXML private TableColumn <StringPropertyDish,String> primoColumn;
    @FXML private TableColumn <StringPropertyDish,String> secondoColumn;
    @FXML private TableColumn <StringPropertyDish,String> dolceColumn;


    /*
        Setup
    */

    private ObservableList<StringPropertyDish> dishList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyDish> antipastiList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyDish> primiList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyDish> secondiList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyDish> dolciList = FXCollections.observableArrayList();

    /*
        Utilities
    */

    @FXML private ChoiceBox giorniChoiceBox;
    private final String addDishFxmlPath = "../../resources/fxml/mensa_addDish.fxml";
    private final Menu.MenuTypeFlag defaultDay = Menu.MenuTypeFlag.MONDAY;
    private final int addDishW = 380;
    private final int addDishH = 380;


    /*
        Initialization
    */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnAssociation();
        selectionMenuSetup();
        refreshDishes(defaultDay);
    }

    private void columnAssociation() {
        antipastoColumn.setCellValueFactory(cellData -> cellData.getValue().nomePProperty());
        primoColumn.setCellValueFactory(cellData -> cellData.getValue().nomePProperty());
        secondoColumn.setCellValueFactory(cellData -> cellData.getValue().nomePProperty());
        dolceColumn.setCellValueFactory(cellData -> cellData.getValue().nomePProperty());
    }

    private void selectionMenuSetup() {
        ObservableList al = FXCollections.observableArrayList(Arrays.asList(Menu.MenuTypeFlag.values()));
        giorniChoiceBox.setItems(al);
    }

    private void refreshDishes(Menu.MenuTypeFlag day) {

        clearlists();

        List<Dish> dishArrayList = null;//TODO DISH RETRIEVAL
        if(dishArrayList != null){
            for(Dish dish : dishArrayList){
                if(dish.getTipo() == Dish.DishTypeFlag.ANTIPASTO)  antipastiList.add(new StringPropertyDish(dish));
                if(dish.getTipo() == Dish.DishTypeFlag.PRIMO)      primiList.add(new StringPropertyDish(dish));
                if(dish.getTipo() == Dish.DishTypeFlag.SECONDO)    secondiList.add(new StringPropertyDish(dish));
                if(dish.getTipo() == Dish.DishTypeFlag.DOLCE)      dolciList.add(new StringPropertyDish(dish));
            }
        }

        setTables();
    }

    private void setTables() {
        antipastoTable.setItems(antipastiList);
        primoTable.setItems(primiList);
        secondoTable.setItems(secondiList);
        dolceTable.setItems(dolciList);
    }

    private void clearlists() {
        dishList.clear();
        antipastiList.clear();
        primiList.clear();
        secondiList.clear();
        dolciList.clear();
    }


    public void goHome() {
        closePopup(goHomeIV);
    }

    private void addDish(Dish.DishTypeFlag tipo, Node trigger) {

        try {
            ControllerMensaViewDishes.setDishType(tipo);
            openPopup(trigger,addDishFxmlPath,addDishW,addDishH);
            refreshDishes(Menu.MenuTypeFlag.valueOf(giorniChoiceBox.getValue().toString()));
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public void addAntipasto() {
        addDish(Dish.DishTypeFlag.ANTIPASTO, addAntipastoIV);
    }

    public void addPrimo() {
        addDish(Dish.DishTypeFlag.PRIMO, addPrimoIV);
    }

    public void addSecondo() {
        addDish(Dish.DishTypeFlag.SECONDO, addSecondoIV);
    }

    public void addDolce() {
        addDish(Dish.DishTypeFlag.DOLCE, addDolceIV);
    }

    public void removeAntipasto() {
        removeDish(Dish.DishTypeFlag.ANTIPASTO);
    }

    public void removePrimo() {
        removeDish(Dish.DishTypeFlag.PRIMO);
    }

    public void removeSecondo() {
        removeDish(Dish.DishTypeFlag.SECONDO);
    }

    public void removeDolce() {
        removeDish(Dish.DishTypeFlag.DOLCE);
    }

    private void removeDish(Dish.DishTypeFlag tipo) {
        if(isUserSure()) {
            StringPropertyDish selectedDish = getSelectedDish(tipo);
            if(selectedDish != null){
                //TODO DISH DISASSOCIATION
                refreshDishes(Menu.MenuTypeFlag.valueOf(giorniChoiceBox.getValue().toString()));
            }
            else { createErrorPopup("Errore", "Non hai selezionato un piatto"); }
        }
    }

    private StringPropertyDish getSelectedDish(Dish.DishTypeFlag tipo) {
        StringPropertyDish selectedDish = null;
        if (tipo == Dish.DishTypeFlag.ANTIPASTO) {
            if(antipastoTable.getSelectionModel().selectedItemProperty().get() != null)
                selectedDish = antipastoTable.getSelectionModel().selectedItemProperty().get();
        }
        if (tipo == Dish.DishTypeFlag.PRIMO) {
            if(primoTable.getSelectionModel().selectedItemProperty().get() != null)
                selectedDish = primoTable.getSelectionModel().selectedItemProperty().get();
        }
        if (tipo == Dish.DishTypeFlag.SECONDO) {
            if(secondoTable.getSelectionModel().selectedItemProperty().get() != null)
                selectedDish = secondoTable.getSelectionModel().selectedItemProperty().get();
        }
        if (tipo == Dish.DishTypeFlag.DOLCE) {
            if(dolceTable.getSelectionModel().selectedItemProperty().get() != null)
                selectedDish = dolceTable.getSelectionModel().selectedItemProperty().get();
        }
        return selectedDish;
    }


}
