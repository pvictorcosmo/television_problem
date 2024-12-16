package org.example.television_problem.view_model.sleeping_room;

import de.saxsys.mvvmfx.ViewModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BedComponentViewModel implements ViewModel {
    private ImageView createBedImage() {
        ImageView bedImageView = new ImageView();
        bedImageView.setImage(new Image(getClass().getResourceAsStream("/assets/bed.png"))); // Path to bed image
        bedImageView.setFitWidth(100); // Set desired width
        bedImageView.setPreserveRatio(true); // Maintain aspect ratio
        return bedImageView;
    }
}