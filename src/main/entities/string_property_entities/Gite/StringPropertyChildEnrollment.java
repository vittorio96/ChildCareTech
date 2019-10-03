package main.entities.string_property_entities.Gite;

import main.entities.string_property_entities.Anagrafica.StringPropertyChild;

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
