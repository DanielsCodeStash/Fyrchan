package view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

public class JobListPanel
{



    static class ColorRectCell extends ListCell<String> {

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            BorderPane p = new BorderPane();



            if (item != null)
            {
                Label l1 = new Label(item);
                l1.setTextFill(Paint.valueOf(Color.BLACK.toString()));
                Label l2 = new Label("Status   ");



                HBox b = new HBox();
                b.getChildren().add(l2);
                StupidImageButton stupidButton = new StupidImageButton();
                stupidButton.setOnClick(() -> System.out.println("h?"));
                b.getChildren().add(stupidButton.getView());

                p.setLeft(l1);
                p.setRight(b);



                setGraphic(p);
            }
        }
    }

    public static VBox testListView()
    {
        ListView<String> listView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList(
                     "Single", "Double", "Suite", "Family App","Single", "Single", "Double", "Suite", "Family App","Single", "Single", "Double", "Suite", "Family App","Single", "Single", "Double", "Suite", "Family App","Single"
        );
        listView.setItems(items);
        listView.setId("jobListView");

        listView.setCellFactory(stringListView -> new ColorRectCell());


        VBox vBox = new VBox();
        vBox.setId("JobListPanel");

        Label l = new Label("Jobs");
        l.setPadding(new Insets(0, 0, 10, 0));

        vBox.getChildren().add(l);
        vBox.getChildren().add(listView);
        vBox.setPrefHeight(230);
        vBox.setPrefWidth(350);








        return vBox;
    }
}
