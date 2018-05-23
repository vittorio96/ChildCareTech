package main.controllers.Mensa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import main.Classes.NormalClasses.Mensa.Dish;
import main.Classes.NormalClasses.Mensa.Menu;
import main.Classes.StringPropertyClasses.Mensa.StringPropertyDish;
import main.controllers.AbstractController;
import main.controllers.PopupController;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerMensaManageMenu extends AbstractController implements Initializable {

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
        Lists
    */

    private ObservableList<StringPropertyDish> dishList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyDish> antipastiList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyDish> primiList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyDish> secondiList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyDish> dolciList = FXCollections.observableArrayList();

    /*
        Utilities
    */

    @FXML private ComboBox giorniComboBox;
    private final String viewIngredientsFxmlPath = "../../resources/fxml/mensa_viewIngredientsPopOver.fxml";
    private final String viewDishesFxmlPath = "../../resources/fxml/mensa_viewDishes.fxml";
    private final Menu.MenuTypeFlag defaultDay = Menu.MenuTypeFlag.MONDAY;
    private final int viewDishesW = 800;
    private final int viewDishesH = 450;
    private ArrayList<PopOver> popOvers = new ArrayList<>();


    /*
        Initialization
    */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setControllerType();
        AbstractController.setCurrentController(this);
        columnAssociation();
        selectionMenuSetup();
        refreshDishes(defaultDay);
        ingredientPopupSetup();
    }

    protected void setControllerType() {
        controllerType = new PopupController();
    }

    private void ingredientPopupSetup() {
        tablePopupSetter(antipastoTable);
        tablePopupSetter(primoTable);
        tablePopupSetter(secondoTable);
        tablePopupSetter(dolceTable);
    }

    private void tablePopupSetter(TableView table){
        table.setRowFactory( tv -> {
            TableRow<StringPropertyDish> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    StringPropertyDish selected = row.getItem();
                    ControllerShowIngredientsPopOver.setDish(selected);
                    try {
                        popOvers.add(openPopOverWR(viewIngredientsFxmlPath, PopOver.ArrowLocation.LEFT_CENTER, row));
                    }catch (IOException e) { e.printStackTrace(); }
                }
            });
            return row ;
        });
    }

    private void columnAssociation() {
        antipastoColumn.setCellValueFactory(cellData -> cellData.getValue().nomePProperty());
        primoColumn.setCellValueFactory(cellData -> cellData.getValue().nomePProperty());
        secondoColumn.setCellValueFactory(cellData -> cellData.getValue().nomePProperty());
        dolceColumn.setCellValueFactory(cellData -> cellData.getValue().nomePProperty());
    }

    private void selectionMenuSetup() {
        ObservableList al = FXCollections.observableArrayList(Arrays.asList(Menu.MenuTypeFlag.values()));
        giorniComboBox.setItems(al);
        giorniComboBox.getSelectionModel().selectFirst();
        giorniComboBox.setOnAction(event -> refreshDishes(getSelectedDay()));
    }

    /*
        Refresh
    */

    private void refreshDishes(Menu.MenuTypeFlag day) {

        clearLists();

        List<Dish> dishArrayList = CLIENT.clientExtractDishesForMenuFromDb(day);
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
    private void clearLists() {
        dishList.clear();
        antipastiList.clear();
        primiList.clear();
        secondiList.clear();
        dolciList.clear();
    }

    private void setTables() {
        antipastoTable.setItems(antipastiList);
        primoTable.setItems(primiList);
        secondoTable.setItems(secondiList);
        dolceTable.setItems(dolciList);
    }

    /*
        GuiMethods
    */

    public void goHome() throws IOException {
        handleOpenedPopovers();
        controllerType.close(goHomeIV);
    }

    private void handleOpenedPopovers() {
        if(popOvers.size()>0){
            for(PopOver p: popOvers) if(p!= null) p.hide(Duration.millis(0));
        }
    }

    private void addDishToMenu(Dish.DishTypeFlag tipo, Node trigger) {

        try {
            ControllerMensaViewDishes.setDishType(tipo);
            ControllerMensaViewDishes.setMenuType(getSelectedDay());
            openPopup(trigger,viewDishesFxmlPath,viewDishesW,viewDishesH);
            refreshDishes(getSelectedDay());
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public void addAntipasto() {
        addDishToMenu(Dish.DishTypeFlag.ANTIPASTO, addAntipastoIV);
    }

    public void addPrimo() {
        addDishToMenu(Dish.DishTypeFlag.PRIMO, addPrimoIV);
    }

    public void addSecondo() {
        addDishToMenu(Dish.DishTypeFlag.SECONDO, addSecondoIV);
    }

    public void addDolce() {
        addDishToMenu(Dish.DishTypeFlag.DOLCE, addDolceIV);
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
        StringPropertyDish selectedDish = getSelectedDish(tipo);
        if(selectedDish != null) {
            if(isUserSure()) {
                CLIENT.clientDeleteDishFromMenuFromDb(getSelectedDay(), selectedDish.getNomeP());
                refreshDishes(getSelectedDay());
            }
        }else { createErrorPopup("Errore", "Non hai selezionato un piatto"); }
    }

    private Menu.MenuTypeFlag getSelectedDay(){
        return Menu.MenuTypeFlag.valueOf(giorniComboBox.getValue().toString());
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

    @Override
    public void refresh() {

    }

}
