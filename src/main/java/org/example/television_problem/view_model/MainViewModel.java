package org.example.television_problem.view_model;

import javafx.scene.Node;

import java.util.concurrent.Semaphore;

import org.example.television_problem.service.ControlTvService;

import de.saxsys.mvvmfx.ViewModel;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class MainViewModel implements ViewModel {

    private final Semaphore favoriteChannelSemaphore = new Semaphore(1);
    private final Semaphore tvOfflineSemaphore = new Semaphore(1);
    private int actuallyChannel = -1;
    private int spectators;

    private ListProperty<StackPane> squares = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ListProperty<StackPane> squaresProperty() {
        return squares;
    }

    public ObservableList<StackPane> getSquares() {
        return squares.get();
    }

    // Método para adicionar um quadrado com o ID
    public void addSquare(int id) {
        Platform.runLater(() -> {
            Rectangle rectangle = new Rectangle(100, 100, Color.LIGHTBLUE);
            Label label = new Label("ID: " + id);
            label.setTextFill(Color.BLACK);

            // Combina o retângulo e o texto dentro de um StackPane
            StackPane square = new StackPane(rectangle, label);

            // Adiciona o quadrado à lista observável
            squares.get().add(square);
        });
    }

    // Método para remover o quadrado pelo ID
    public void removeSquare(int id) {
        Platform.runLater(() -> {
            // Itera sobre a lista de quadrados e remove o que corresponde ao ID
            squares.get().removeIf(square -> {
                // Obtém o pai do square e verifica se é uma VBox
                if (square.getParent() instanceof VBox vbox) {
                    // Itera sobre os filhos da VBox
                    for (Node child : vbox.getChildren()) {
                        // Verifica se o filho é um StackPane
                        if (child instanceof StackPane stackPane) {
                            // Itera sobre os filhos do StackPane para encontrar uma Label
                            for (Node stackChild : stackPane.getChildren()) {
                                if (stackChild instanceof Label label) {
                                    System.out.println("STATUS: " + label.getText().equals("ID: " + id) + " " + id);
                                    // Compara o texto da Label com o ID desejado
                                    return label.getText().equals("ID: " + id);
                                }
                            }
                        }
                    }
                }
                return false; // Não remove se nenhuma Label correspondente for encontrada
            });
        });
    }

    public int getSpectators() {
        return this.spectators;
    }

    public void increaseSpectators() {
        spectators++;
    }

    public void decreaseSpectators() {
        spectators--;
    }

    public void acquireFavoriteChannel() throws InterruptedException {
        favoriteChannelSemaphore.acquire();
    }

    public void releaseFavoriteChannel() {
        favoriteChannelSemaphore.release();
    }

    public void acquireTvOffline() throws InterruptedException {
        tvOfflineSemaphore.acquire();
    }

    public void releaseTvOffline() {
        tvOfflineSemaphore.release();
    }

    public void setActuallyChannel(int channel) {
        this.actuallyChannel = channel;
    }

    public int getActuallyChannel() {
        return this.actuallyChannel;
    }
}
