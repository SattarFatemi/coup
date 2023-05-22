package GuiControllers;

import Logic.GameManager;
import Models.Card;
import Models.Event;
import Models.GameCharacter;
import Models.Player;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameBoard implements Initializable {

    @FXML
    public Button ambassadorActionButton;
    @FXML
    public Button commanderActionButton;
    @FXML
    public Button coupButton;
    @FXML
    public Button gainOneCoinButton;
    @FXML
    public Button gainTwoCoinsButton;
    @FXML
    public Button hintButton;
    @FXML
    public Pane hintPagePane;
    @FXML
    public Button murdererActionButton;
    @FXML
    public Label player1CoinsLabel;
    @FXML
    public ImageView player1FirstCardImageView;
    @FXML
    public Label player1NameLabel;
    @FXML
    public ImageView player1SecondCardImageView;
    @FXML
    public Label player2CoinsLabel;
    @FXML
    public ImageView player2FirstCardImageView;
    @FXML
    public Label player2NameLabel;
    @FXML
    public ImageView player2SecondCardImageView;
    @FXML
    public Label player3CoinsLabel;
    @FXML
    public ImageView player3FirstCardImageView;
    @FXML
    public Label player3NameLabel;
    @FXML
    public ImageView player3SecondCardImageView;
    @FXML
    public Label player4CoinsLabel;
    @FXML
    public ImageView player4FirstCardImageView;
    @FXML
    public Label player4NameLabel;
    @FXML
    public ImageView player4SecondCardImageView;
    @FXML
    public VBox recentEventsContainerVBox;
    @FXML
    public VBox actionButtonsVBox;
    @FXML
    public Button richActionButton;
    @FXML
    public VBox selectMurderTargetPlayerVBox;
    @FXML
    public Button murderPlayerButton1;
    @FXML
    public Button murderPlayerButton2;
    @FXML
    public Button murderPlayerButton3;
    @FXML
    public VBox selectTributeTargetPlayerVBox;
    @FXML
    public Button tributePlayerButton1;
    @FXML
    public Button tributePlayerButton2;
    @FXML
    public Button tributePlayerButton3;
    @FXML
    public VBox selectNewCardsVBox;
    @FXML
    public CheckBox newCard1CheckBox;
    @FXML
    public CheckBox newCard2CheckBox;
    @FXML
    public CheckBox newCard3CheckBox;
    @FXML
    public CheckBox newCard4CheckBox;
    @FXML
    public Button selectNewCardsButton;
    @FXML
    public Label selectNewCardsLabel;
    @FXML
    public Button coupPlayerButton1;
    @FXML
    public Button coupPlayerButton2;
    @FXML
    public Button coupPlayerButton3;
    @FXML
    public VBox selectCoupTargetPlayerVBox;
    @FXML
    public Button changeCardButton;
    @FXML
    public Pane finishPane;
    @FXML
    public Label winnerNickNameLabel;
    @FXML
    public Pane startPane;
    @FXML
    public ImageView startCard1ImageView;
    @FXML
    public ImageView startCard2ImageView;
    @FXML
    public Button startGameButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        GameManager.getInstance().setController(this);

        initPlayersArea();
        initActionAndReactionButtons();

        initStartPane();
    }

    public void initStartPane() {

        Image image1 = new Image(GameManager.getInstance().getUser().getCards().get(0).getImagePathLikeItsBurned());
        startCard1ImageView.setImage(image1);
        Image image2 = new Image(GameManager.getInstance().getUser().getCards().get(1).getImagePathLikeItsBurned());
        startCard2ImageView.setImage(image2);
    }

    public void startGame(ActionEvent actionEvent) {
        startPane.setVisible(false);
        GameManager.getInstance().start();
    }

    public void finishGame() {
        winnerNickNameLabel.setText(GameManager.getInstance().getOnePlayerIn().getNickName());

        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), finishPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(new Runnable() {
            @Override
            public void run() {
                fadeIn.play();
                finishPane.setVisible(true);
            }
        }, 5000, TimeUnit.MILLISECONDS);
    }

    public void reload() {
        reloadCardImages();
        reloadCoins();
        reloadLastEvents();
    }

    private void reloadLastEvents() {

        recentEventsContainerVBox.getChildren().removeIf(element -> element instanceof HBox);

        for (int i = GameManager.getInstance().getEvents().size() - 1; i >= 0; i--) {
            recentEventsContainerVBox.getChildren().add(new RecentEvent(GameManager.getInstance().getEvents().get(i)));
        }
    }

    private void initPlayersArea() {
        initNames();
        reloadCoins();
        reloadCardImages();
    }

    private void initNames() {
        player1NameLabel.setText(GameManager.getInstance().getPlayers().get(0).getNickName());
        player2NameLabel.setText(GameManager.getInstance().getPlayers().get(1).getNickName());
        player3NameLabel.setText(GameManager.getInstance().getPlayers().get(2).getNickName());
        player4NameLabel.setText(GameManager.getInstance().getPlayers().get(3).getNickName());
    }

    private void reloadCardImages() {

        Image image11 = new Image(GameManager.getInstance().getPlayers().get(0).getCards().get(0).getImagePath());
        player1FirstCardImageView.setImage(image11);
        Image image12 = new Image(GameManager.getInstance().getPlayers().get(0).getCards().get(1).getImagePath());
        player1SecondCardImageView.setImage(image12);

        Image image21 = new Image(GameManager.getInstance().getPlayers().get(1).getCards().get(0).getImagePath());
        player2FirstCardImageView.setImage(image21);
        Image image22 = new Image(GameManager.getInstance().getPlayers().get(1).getCards().get(1).getImagePath());
        player2SecondCardImageView.setImage(image22);

        Image image31 = new Image(GameManager.getInstance().getPlayers().get(2).getCards().get(0).getImagePath());
        player3FirstCardImageView.setImage(image31);
        Image image32 = new Image(GameManager.getInstance().getPlayers().get(2).getCards().get(1).getImagePath());
        player3SecondCardImageView.setImage(image32);

        Image image41 = new Image(GameManager.getInstance().getPlayers().get(3).getCards().get(0).getImagePath());
        player4FirstCardImageView.setImage(image41);
        Image image42 = new Image(GameManager.getInstance().getPlayers().get(3).getCards().get(1).getImagePath());
        player4SecondCardImageView.setImage(image42);
    }

    private void reloadCoins() {

        player1CoinsLabel.setText(GameManager.getInstance().getPlayers().get(0).getCoins().toString());
        player2CoinsLabel.setText(GameManager.getInstance().getPlayers().get(1).getCoins().toString());
        player3CoinsLabel.setText(GameManager.getInstance().getPlayers().get(2).getCoins().toString());
        player4CoinsLabel.setText(GameManager.getInstance().getPlayers().get(3).getCoins().toString());

    }

    private void initActionAndReactionButtons() {
        disableAllActionButtons();
    }

    private void flipCardIfNotBurned(ImageView imageView, Card card) {

        if (card.getPlayer().getPlayerType() == Player.PlayerType.BOT) return;
        if (card.getCardStatus() == Card.CardStatus.BURNED) return;

        flipCard(imageView, card);
    }

    private void flipCard(ImageView imageView, Card card) {

        ScaleTransition st1 = new ScaleTransition(Duration.millis(100), imageView);
        st1.setByX(-1);
        st1.play();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {

                        if (imageView.getImage().getUrl().equals("file:/E:/University/Lessons/Term2/AP/Coup/build/resources/main/CardImages%5cbackOfCard.png")) {
                            imageView.setImage(new Image(card.getImagePathLikeItsBurned()));
                        } else {
                            imageView.setImage(new Image("CardImages\\backOfCard.png"));
                        }

                        ScaleTransition st2 = new ScaleTransition(Duration.millis(100), imageView);
                        st2.setByX(1);
                        st2.play();
                    }
                },
                100
        );
    }

    public void toggleHintPagePane() {

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), hintPagePane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), hintPagePane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        if (hintPagePane.isVisible()) {

            fadeOut.play();

            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            service.schedule(new Runnable() {
                @Override
                public void run() {
                    hintPagePane.setVisible(false);
                }
            }, 300, TimeUnit.MILLISECONDS);

        } else {

            hintPagePane.setVisible(true);
            fadeIn.play();

        }

    }

    public void toggleHintPagePaneByTab(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.TAB)) toggleHintPagePane();
    }

    public void disableAllActionButtons() {

        richActionButton.setDisable(true);
        murdererActionButton.setDisable(true);
        commanderActionButton.setDisable(true);
        ambassadorActionButton.setDisable(true);
        coupButton.setDisable(true);
        gainOneCoinButton.setDisable(true);
        gainTwoCoinsButton.setDisable(true);
        changeCardButton.setDisable(true);

    }

    public void enableAllActionButtons() {

        richActionButton.setDisable(false);
        murdererActionButton.setDisable(false);
        commanderActionButton.setDisable(false);
        ambassadorActionButton.setDisable(false);
        coupButton.setDisable(false);
        gainOneCoinButton.setDisable(false);
        gainTwoCoinsButton.setDisable(false);
        changeCardButton.setDisable(false);

    }

    public void commanderAction(ActionEvent actionEvent) {
        GameManager.getInstance().getCurrentPlayer().commanderAction();
    }

    public void murdererAction(ActionEvent actionEvent) {

        if (GameManager.getInstance().getCurrentPlayer().getCoins() < 3) {
            GameManager.getInstance().showError("shoma nayad haddeaghal 3 sekkeh dashteh bashid :(");
            return;
        }

        GameManager.getInstance().getCurrentPlayer().murdererAction();

    }

    public void gainOneCoin(ActionEvent actionEvent) {
        GameManager.getInstance().getCurrentPlayer().gainOneCoin();
    }

    public void gainTwoCoins(ActionEvent actionEvent) {
        GameManager.getInstance().getCurrentPlayer().gainTwoCoins();
    }

    public void princeAction(ActionEvent actionEvent) {
        GameManager.getInstance().getCurrentPlayer().princeAction();
    }

    public void initCoupButtons() {

        coupPlayerButton1.setDisable(false);
        coupPlayerButton2.setDisable(false);
        coupPlayerButton3.setDisable(false);

        int i = 1;
        for (Player player : GameManager.getInstance().getPlayers()) {
            if (player.getId().equals(GameManager.getInstance().getUser().getId())) continue;

            switch (i) {
                case 1 -> {
                    coupPlayerButton1.setText(player.getNickName());
                    if (player.getPlayerStatus() == Player.PlayerStatus.OUT) coupPlayerButton1.setDisable(true);
                }
                case 2 -> {
                    coupPlayerButton2.setText(player.getNickName());
                    if (player.getPlayerStatus() == Player.PlayerStatus.OUT) coupPlayerButton2.setDisable(true);
                }
                case 3 -> {
                    coupPlayerButton3.setText(player.getNickName());
                    if (player.getPlayerStatus() == Player.PlayerStatus.OUT) coupPlayerButton3.setDisable(true);
                }
            }
            i++;
        }
    }

    public void coup(ActionEvent actionEvent) {

        if (GameManager.getInstance().getCurrentPlayer().getCoins() < 7) {
            GameManager.getInstance().showError("shoma nayad haddeaghal 7 sekkeh dashteh bashid :(");
            return;
        }

        GameManager.getInstance().getCurrentPlayer().selectPlayerToCoup();
    }

    public void coupPlayer1(ActionEvent actionEvent) {
        Player targetPlayer = GameManager.getInstance().findPlayerByNickName(coupPlayerButton1.getText());
        GameManager.getInstance().addEvent(GameManager.getInstance().getCurrentPlayer(), targetPlayer, null, null, Event.EventType.COUP);
        targetPlayer.selectOneCardToRemove();

        GameManager.getInstance().getCurrentPlayer().changeCoinBy(-7);

        selectCoupTargetPlayerVBox.setVisible(false);

        GameManager.getInstance().nextTurn();
    }

    public void coupPlayer2(ActionEvent actionEvent) {
        Player targetPlayer = GameManager.getInstance().findPlayerByNickName(coupPlayerButton2.getText());
        GameManager.getInstance().addEvent(GameManager.getInstance().getCurrentPlayer(), targetPlayer, null, null, Event.EventType.COUP);
        targetPlayer.selectOneCardToRemove();

        GameManager.getInstance().getCurrentPlayer().changeCoinBy(-7);

        selectCoupTargetPlayerVBox.setVisible(false);

        GameManager.getInstance().nextTurn();
    }

    public void coupPlayer3(ActionEvent actionEvent) {
        Player targetPlayer = GameManager.getInstance().findPlayerByNickName(coupPlayerButton3.getText());
        GameManager.getInstance().addEvent(GameManager.getInstance().getCurrentPlayer(), targetPlayer, null, null, Event.EventType.COUP);
        targetPlayer.selectOneCardToRemove();

        GameManager.getInstance().getCurrentPlayer().changeCoinBy(-7);

        selectCoupTargetPlayerVBox.setVisible(false);

        GameManager.getInstance().nextTurn();
    }

    public void initMurderButtons() {

        murderPlayerButton1.setDisable(false);
        murderPlayerButton2.setDisable(false);
        murderPlayerButton3.setDisable(false);

        int i = 1;
        for (Player player : GameManager.getInstance().getPlayers()) {
            if (player.getId().equals(GameManager.getInstance().getUser().getId())) continue;

            switch (i) {
                case 1 -> {
                    murderPlayerButton1.setText(player.getNickName());
                    if (player.getPlayerStatus() == Player.PlayerStatus.OUT) murderPlayerButton1.setDisable(true);
                }
                case 2 -> {
                    murderPlayerButton2.setText(player.getNickName());
                    if (player.getPlayerStatus() == Player.PlayerStatus.OUT) murderPlayerButton2.setDisable(true);
                }
                case 3 -> {
                    murderPlayerButton3.setText(player.getNickName());
                    if (player.getPlayerStatus() == Player.PlayerStatus.OUT) murderPlayerButton3.setDisable(true);
                }
            }
            i++;
        }
    }

    public void murderPlayer1(ActionEvent actionEvent) {
        Player targetPlayer = GameManager.getInstance().findPlayerByNickName(murderPlayerButton1.getText());
        GameManager.getInstance().addEvent(GameManager.getInstance().getCurrentPlayer(), targetPlayer, GameCharacter.MURDERER, null, Event.EventType.MURDER_ATTEMPT);
        targetPlayer.murdererReaction(GameManager.getInstance().getLastEvent());

        selectMurderTargetPlayerVBox.setVisible(false);

        GameManager.getInstance().nextTurn();
    }

    public void murderPlayer2(ActionEvent actionEvent) {
        Player targetPlayer = GameManager.getInstance().findPlayerByNickName(murderPlayerButton2.getText());
        GameManager.getInstance().addEvent(GameManager.getInstance().getCurrentPlayer(), targetPlayer, GameCharacter.MURDERER, null, Event.EventType.MURDER_ATTEMPT);
        targetPlayer.murdererReaction(GameManager.getInstance().getLastEvent());

        selectMurderTargetPlayerVBox.setVisible(false);

        GameManager.getInstance().nextTurn();
    }

    public void murderPlayer3(ActionEvent actionEvent) {
        Player targetPlayer = GameManager.getInstance().findPlayerByNickName(murderPlayerButton3.getText());
        GameManager.getInstance().addEvent(GameManager.getInstance().getCurrentPlayer(), targetPlayer, GameCharacter.MURDERER, null, Event.EventType.MURDER_ATTEMPT);
        targetPlayer.murdererReaction(GameManager.getInstance().getLastEvent());

        selectMurderTargetPlayerVBox.setVisible(false);

        GameManager.getInstance().nextTurn();
    }

    public void initTributeButtons() {

        tributePlayerButton1.setDisable(false);
        tributePlayerButton2.setDisable(false);
        tributePlayerButton3.setDisable(false);

        int i = 1;
        for (Player player : GameManager.getInstance().getPlayers()) {
            if (player.getId().equals(GameManager.getInstance().getUser().getId())) continue;

            switch (i) {
                case 1 -> {
                    tributePlayerButton1.setText(player.getNickName());
                    if (player.getPlayerStatus() == Player.PlayerStatus.OUT || player.getCoins() == 0)
                        tributePlayerButton1.setDisable(true);
                }
                case 2 -> {
                    tributePlayerButton2.setText(player.getNickName());
                    if (player.getPlayerStatus() == Player.PlayerStatus.OUT || player.getCoins() == 0)
                        tributePlayerButton2.setDisable(true);
                }
                case 3 -> {
                    tributePlayerButton3.setText(player.getNickName());
                    if (player.getPlayerStatus() == Player.PlayerStatus.OUT || player.getCoins() == 0)
                        tributePlayerButton3.setDisable(true);
                }
            }
            i++;
        }
    }

    public void tributePlayer1(ActionEvent actionEvent) {
        Player targetPlayer = GameManager.getInstance().findPlayerByNickName(tributePlayerButton1.getText());
        GameManager.getInstance().addEvent(GameManager.getInstance().getCurrentPlayer(), targetPlayer, GameCharacter.COMMANDER, null, Event.EventType.TRIBUTE_ATTEMPT);
        targetPlayer.commanderReaction(GameManager.getInstance().getLastEvent());

        selectTributeTargetPlayerVBox.setVisible(false);

        GameManager.getInstance().nextTurn();
    }

    public void tributePlayer2(ActionEvent actionEvent) {
        Player targetPlayer = GameManager.getInstance().findPlayerByNickName(tributePlayerButton2.getText());
        GameManager.getInstance().addEvent(GameManager.getInstance().getCurrentPlayer(), targetPlayer, GameCharacter.COMMANDER, null, Event.EventType.TRIBUTE_ATTEMPT);
        targetPlayer.commanderReaction(GameManager.getInstance().getLastEvent());

        selectTributeTargetPlayerVBox.setVisible(false);

        GameManager.getInstance().nextTurn();
    }

    public void tributePlayer3(ActionEvent actionEvent) {
        Player targetPlayer = GameManager.getInstance().findPlayerByNickName(tributePlayerButton3.getText());
        GameManager.getInstance().addEvent(GameManager.getInstance().getCurrentPlayer(), targetPlayer, GameCharacter.COMMANDER, null, Event.EventType.TRIBUTE_ATTEMPT);
        targetPlayer.commanderReaction(GameManager.getInstance().getLastEvent());

        selectTributeTargetPlayerVBox.setVisible(false);

        GameManager.getInstance().nextTurn();
    }

    public void ambassadorAction(ActionEvent actionEvent) {
        GameManager.getInstance().getCurrentPlayer().ambassadorAction();
    }

    public int[] mapNumberOfCheckBoxToCardId = new int[5];

    public void initChangeCardCheckBoxes() {

        newCard2CheckBox.setDisable(false);

        Player currentPlayer = GameManager.getInstance().getCurrentPlayer();

        ArrayList<Card> cardsOut = GameManager.getInstance().getCardsOut();

        Random random = new Random();

        mapNumberOfCheckBoxToCardId[1] = currentPlayer.getCards().get(0).getId();
        newCard1CheckBox.setText(currentPlayer.getCards().get(0).getGameCharacter().name());
        mapNumberOfCheckBoxToCardId[2] = currentPlayer.getCards().get(1).getId();
        newCard2CheckBox.setText(currentPlayer.getCards().get(1).getGameCharacter().name());

        mapNumberOfCheckBoxToCardId[3] = cardsOut.get(random.nextInt(cardsOut.size())).getId();
        newCard3CheckBox.setText(GameManager.getInstance().findCardById(mapNumberOfCheckBoxToCardId[3]).getGameCharacter().name());
        GameManager.getInstance().findCardById(mapNumberOfCheckBoxToCardId[3]).setCardStatus(Card.CardStatus.BURNED);
        mapNumberOfCheckBoxToCardId[4] = cardsOut.get(random.nextInt(cardsOut.size())).getId();
        newCard4CheckBox.setText(GameManager.getInstance().findCardById(mapNumberOfCheckBoxToCardId[4]).getGameCharacter().name());

        selectNewCardsLabel.setText("2 kart entekhab kon! (ba ehtesabe gheire faal)");

        if (currentPlayer.getNumberOfCardsIn() == 1) {
            newCard2CheckBox.setSelected(true);
            newCard2CheckBox.setDisable(true);
        }
    }

    public void selectNewCards(ActionEvent actionEvent) {

        int numberOfChecks = 0;
        if (newCard1CheckBox.isSelected()) numberOfChecks++;
        if (newCard2CheckBox.isSelected()) numberOfChecks++;
        if (newCard3CheckBox.isSelected()) numberOfChecks++;
        if (newCard4CheckBox.isSelected()) numberOfChecks++;

        if (numberOfChecks == 2) {
            Player player = GameManager.getInstance().getCurrentPlayer();
            player.setCardIds(new ArrayList<>());

            if (newCard1CheckBox.isSelected()) {
                Card card = GameManager.getInstance().findCardById(mapNumberOfCheckBoxToCardId[1]);

                if (newCard1CheckBox.isDisable()) card.setCardStatus(Card.CardStatus.BURNED);
                else card.setCardStatus(Card.CardStatus.IN);

                player.addCardAsItIs(card);
            } else {
                Card card = GameManager.getInstance().findCardById(mapNumberOfCheckBoxToCardId[1]);

                card.setCardStatus(Card.CardStatus.OUT);
            }

            if (newCard2CheckBox.isSelected()) {
                Card card = GameManager.getInstance().findCardById(mapNumberOfCheckBoxToCardId[2]);

                if (newCard2CheckBox.isDisable()) card.setCardStatus(Card.CardStatus.BURNED);
                else card.setCardStatus(Card.CardStatus.IN);

                player.addCardAsItIs(card);
            } else {
                Card card = GameManager.getInstance().findCardById(mapNumberOfCheckBoxToCardId[2]);

                card.setCardStatus(Card.CardStatus.OUT);
            }

            if (newCard3CheckBox.isSelected()) {
                Card card = GameManager.getInstance().findCardById(mapNumberOfCheckBoxToCardId[3]);

                if (newCard3CheckBox.isDisable()) card.setCardStatus(Card.CardStatus.BURNED);
                else card.setCardStatus(Card.CardStatus.IN);

                player.addCardAsItIs(card);
            } else {
                Card card = GameManager.getInstance().findCardById(mapNumberOfCheckBoxToCardId[3]);

                card.setCardStatus(Card.CardStatus.OUT);
            }

            if (newCard4CheckBox.isSelected()) {
                Card card = GameManager.getInstance().findCardById(mapNumberOfCheckBoxToCardId[4]);

                if (newCard4CheckBox.isDisable()) card.setCardStatus(Card.CardStatus.BURNED);
                else card.setCardStatus(Card.CardStatus.IN);

                player.addCardAsItIs(card);
            } else {
                Card card = GameManager.getInstance().findCardById(mapNumberOfCheckBoxToCardId[4]);

                card.setCardStatus(Card.CardStatus.OUT);
            }

            selectNewCardsVBox.setVisible(false);
            GameManager.getInstance().addEvent(GameManager.getInstance().getCurrentPlayer(), null, GameCharacter.AMBASSADOR, null, Event.EventType.AMBASSADOR_ACTION);
            GameManager.getInstance().nextTurn();
        } else {
            GameManager.getInstance().showError("daghighan 2 kart entekhab kon!");
        }
    }

    public void changeCard(ActionEvent actionEvent) {
        GameManager.getInstance().getCurrentPlayer().changeCardAction();
    }

    public void flipCard11() {
        flipCardIfNotBurned(player1FirstCardImageView, GameManager.getInstance().getPlayers().get(0).getCards().get(0));
    }

    public void flipCard12() {
        flipCardIfNotBurned(player1SecondCardImageView, GameManager.getInstance().getPlayers().get(0).getCards().get(1));
    }

    public void flipCard21() {
        flipCardIfNotBurned(player2FirstCardImageView, GameManager.getInstance().getPlayers().get(1).getCards().get(0));
    }

    public void flipCard22() {
        flipCardIfNotBurned(player2SecondCardImageView, GameManager.getInstance().getPlayers().get(1).getCards().get(1));
    }

    public void flipCard31() {
        flipCardIfNotBurned(player3FirstCardImageView, GameManager.getInstance().getPlayers().get(2).getCards().get(0));
    }

    public void flipCard32() {
        flipCardIfNotBurned(player3SecondCardImageView, GameManager.getInstance().getPlayers().get(2).getCards().get(1));
    }

    public void flipCard41() {
        flipCardIfNotBurned(player4FirstCardImageView, GameManager.getInstance().getPlayers().get(3).getCards().get(0));
    }

    public void flipCard42() {
        flipCardIfNotBurned(player4SecondCardImageView, GameManager.getInstance().getPlayers().get(3).getCards().get(1));
    }
}
