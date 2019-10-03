package main.entities.normal_entities.Mensa;

import java.io.Serializable;

public class PersonIntolerance extends Intolerance implements Serializable {

    public PersonIntolerance() {
        super();
    }

    public PersonIntolerance(String codF, String nomeI) {
        super(codF, nomeI);
    }

}
