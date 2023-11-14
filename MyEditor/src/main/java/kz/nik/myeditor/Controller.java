package kz.nik.myeditor;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;


import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable{
    enum Type {
        SPACE("#000000", '-'), GRASS("#009900", '0'), WALL("#555555", '1'), WATER("#5555ff", '2'), PORTAL("ff0000", 'x');

        String color;
        char symbol;

        static Type getTypeFromSymbol(char symbol) {
            for (Type o : Type.values()) {
                if (o.symbol == symbol) {
                    return o;
                }
            }
            return SPACE;
        }

        Type(String color, char symbol) {
            this.color = color;
            this.symbol = symbol;
        }
    }

    @FXML
    Canvas canvas;

    @FXML
    ToggleGroup lbrushes;

    @FXML
    ToggleGroup rbrushes;

    @FXML
    Spinner sizeXSpinner;

    @FXML
    Spinner sizeYSpinner;

    GraphicsContext gc;

    int size_x = 40;
    int size_y = 40;
    int cell_size = 20;

    Type[][] map;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = canvas.getGraphicsContext2D();
        canvas.setWidth(cell_size * size_x);
        canvas.setHeight(cell_size * size_y);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.SECONDARY) {
                int cx = (int) event.getX() / cell_size;
                int cy = (int) event.getY() / cell_size;
                RadioButton rbs = null;
                if (event.getButton() == MouseButton.PRIMARY) {
                    rbs = (RadioButton) lbrushes.getSelectedToggle();
                } else {
                    rbs = (RadioButton) rbrushes.getSelectedToggle();
                }
                map[cx][cy] = Type.valueOf(rbs.getText().toUpperCase());
                repaintMap();
            }
        });

        loadStandartMap();
        sizeXSpinner.valueProperty().addListener((obs, oldValue, newValue) -> resizeMapX());
        sizeYSpinner.valueProperty().addListener((obs, oldValue, newValue) -> resizeMapY());
    }

    public void clearMap100() {
        map = new Type[100][100];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                map[i][j] = Type.SPACE;
            }
        }
    }

    public void loadStandartMap() {
        clearMap100();
        size_x = 40;
        size_y = 40;
        for (int i = 0; i < size_x; i++) {
            for (int j = 0; j < size_y; j++) {
                map[i][j] = Type.GRASS;
            }
        }
        sizeXSpinner.getValueFactory().setValue(size_x);
        sizeYSpinner.getValueFactory().setValue(size_y);
        repaintMap();
    }

    public void loadMap() {
        clearMap100();
        File file = new File("core\\assets\\map01.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            size_x = Integer.parseInt(br.readLine());
            size_y = Integer.parseInt(br.readLine());
            for (int i = 0; i < size_y; i++) {
                String str = br.readLine();
                for (int j = 0; j < size_x; j++) {
                    map[j][i] = Type.getTypeFromSymbol(str.charAt(j));
                }
            }
            sizeXSpinner.getValueFactory().setValue(size_x);
            sizeYSpinner.getValueFactory().setValue(size_y);
            repaintMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMap() {
        File file = new File("core\\assets\\map01.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(String.valueOf(size_x) + '\n');
            bw.write(String.valueOf(size_y) + '\n');
            char[][] cmap = new char[size_y][size_x];
            for (int i = 0; i < size_x; i++) {
                for (int j = 0; j < size_y; j++) {
                    cmap[j][i] = map[i][j].symbol;
                }
            }
            for (int i = 0; i < size_y; i++) {
                bw.write(cmap[i]);
                bw.newLine();
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "map01.txt saved", ButtonType.OK);
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void repaintMap() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = 0; i < size_x; i++) {
            for (int j = 0; j < size_y; j++) {
                gc.setFill(Paint.valueOf(map[i][j].color));
                gc.fillRect(i * cell_size, j * cell_size, cell_size, cell_size);
            }
        }
        gc.setStroke(Paint.valueOf("#000000"));
        for (int i = 0; i <= size_x; i++) {
            gc.strokeLine(i * cell_size, 0, i * cell_size, size_y * cell_size);
            gc.strokeLine(i * cell_size, 0, i * cell_size, size_y * cell_size);
        }
        for (int i = 0; i <= size_y; i++) {
            gc.strokeLine(0, i * cell_size, size_x * cell_size, i * cell_size);
            gc.strokeLine(0, i * cell_size, size_x * cell_size, i * cell_size);
        }
    }

    public void resizeMapX() {
        size_x = (Integer) sizeXSpinner.getValue();
        canvas.setWidth(cell_size * size_x);
        repaintMap();
    }

    public void resizeMapY() {
        size_y = (Integer) sizeYSpinner.getValue();
        canvas.setHeight(cell_size * size_y);
        repaintMap();
    }

    public void quit(ActionEvent actionEvent) {
        Platform.exit();
    }
}
