package main.entities.normal_entities.Mensa;

import java.io.Serializable;

public class ChildIntolerance extends Intolerance implements Serializable {

    public ChildIntolerance() {
        super();
    }

    public ChildIntolerance(String codF, String nomeI) {
        super(codF, nomeI);
    }
}