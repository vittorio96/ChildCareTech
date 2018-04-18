package main.StringPropertyClasses.Gite;

import javafx.beans.property.SimpleBooleanProperty;
import main.NormalClasses.Anagrafica.Child;
import main.StringPropertyClasses.Anagrafica.StringPropertyChild;

public class StringPropertyChildEnrollment extends StringPropertyChild {

    private StringPropertyBus selectedBus;

    public StringPropertyChildEnrollment(StringPropertyChild child, StringPropertyBus bus){
    super(child);
    }

    public StringPropertyBus getSelectedBus() {
        return selectedBus;
    }

    public void setSelectedBus(StringPropertyBus selectedBus) {
        this.selectedBus = selectedBus;
    }

    public void getDataG(){
        selectedBus.getDataG();
    }
    public void getNomeG(){
        selectedBus.getNomeG();
    }
    public void getTarga(){
        selectedBus.getTarga();
    }
}
