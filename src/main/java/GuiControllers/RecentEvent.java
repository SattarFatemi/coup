package GuiControllers;

import Models.Event;
import Models.GameCharacter;
import Models.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;

public class RecentEvent extends HBox {

    private Event event;

    public RecentEvent(Event event) {
        this.event = event;
        init();
    }

    private void init() {

        setBounds();

        ImageView imageView = new ImageView();
        Label textLabel = new Label();

        setIcon(imageView);
        setText(textLabel);

        setBoundsOfChildren(imageView, textLabel);

        getChildren().add(imageView);
        getChildren().add(textLabel);

    }

    private void setBounds() {

        setPrefHeight(50.0);
        setMinHeight(50.0);
        setPrefWidth(347.0);
        setPadding(new Insets(0, 5.0, 0.0, 10.0));
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(10.0);

        if (event.getActingPlayer().getPlayerType() == Player.PlayerType.HUMAN) {
            setStyle("-fx-border-style: hidden solid hidden hidden; -fx-border-width: 5; -fx-border-color: #dc143c;");
        }

    }

    private void setIcon(ImageView imageView) {

        Image image;

        switch (event.getEventType()) {
            case CLAIM -> {
                switch (event.getActingGameCharacter()) {
                    case PRINCE -> image = new Image("Icons\\richIcon.png");
                    case MURDERER -> image = new Image("Icons\\murdererIcon.png");
                    case PRINCESS -> image = new Image("Icons\\princessIcon.png");
                    case COMMANDER -> image = new Image("Icons\\commanderIcon.png");
                    case AMBASSADOR -> image = new Image("Icons\\ambassadorIcon.png");
                    default -> image = new Image("Icons\\challengeIcon.png");
                }
            }
            case CHALLENGE, CHALLENGE_FAILED, CHALLENGE_SUCCESS -> image = new Image("Icons\\challengeIcon.png");
            case MURDER, MURDER_ATTEMPT -> image = new Image("Icons\\murdererIcon.png");
            case STOP_MURDER, BLOCK_FOREIGN_AID, STOP_TRIBUTE -> image = new Image("Icons\\stop.png");
            case AMBASSADOR_ACTION, EXCHANGE_CARD -> image = new Image("Icons\\exchange.png");
            case GAIN_ONE_COIN, GAIN_TWO_COINS -> image = new Image("Icons\\coinIcon.png");
            case COUP -> image = new Image("Icons\\coupIcon.png");
            case PRINCE_ACTION -> image = new Image("Icons\\richIcon.png");
            case START_TURN -> image = new Image("Icons\\startTurnIcon.png");
            case TRIBUTE, TRIBUTE_ATTEMPT -> image = new Image("Icons\\commanderIcon.png");
            case GAME_FINISHED -> image = new Image("Icons\\trophy.png");
            default -> image = new Image("Icons\\challengeIcon.png");
        }

        imageView.setImage(image);
    }

    private void setText(Label textLabel) {
        textLabel.setWrapText(true);
        textLabel.setFont(Font.font("Montserrat Medium", 12.0));
        textLabel.setTextFill(Color.WHITE);

        switch (getEvent().getEventType()) {
            case CLAIM -> textLabel.setText(getEvent().getActingPlayer().getNickName() + " claims to have " + getEvent().getActingGameCharacter());
            case BLOCK_FOREIGN_AID -> textLabel.setText(getEvent().getActingPlayer().getNickName() + " blocks " + getEvent().getTargetPlayer().getNickName());
            default -> textLabel.setText(getEvent().getActingPlayer().getNickName() + ": " + getEvent().getEventType());
        }

        if (event.getTargetPlayer() != null) textLabel.setText(textLabel.getText() + " -> " + event.getTargetPlayer().getNickName());

    }

    private void setBoundsOfChildren(ImageView imageView, Label textLabel) {

        imageView.setFitWidth(30.0);
        imageView.setFitHeight(30.0);

        textLabel.setPrefWidth(300.0);
        textLabel.setPrefHeight(80.0);
        textLabel.setPadding(new Insets(5.0));

    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
