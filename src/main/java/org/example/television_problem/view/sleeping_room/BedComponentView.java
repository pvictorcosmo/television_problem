package org.example.television_problem.view.sleeping_room;

import org.example.television_problem.view_model.sleeping_room.BedComponentViewModel;

import de.saxsys.mvvmfx.FxmlView;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class BedComponentView implements FxmlView<BedComponentViewModel>{
    // Integer property to store the number of beds
    private IntegerProperty numberOfBeds = new SimpleIntegerProperty();

    // Getter and Setter for numberOfBeds
    public int getNumberOfBeds() {
        return numberOfBeds.get();
    }

    public void setNumberOfBeds(int numberOfBeds) {
        this.numberOfBeds.set(numberOfBeds);
    }

    public IntegerProperty numberOfBedsProperty() {
        return numberOfBeds;
    }

}