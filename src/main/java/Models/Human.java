package Models;

import GuiControllers.GameBoard;
import Logic.GameManager;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Human extends Player {

    @Override
    public void startTurn() {

        GameManager.getInstance().addEvent(this, null, null, null, Event.EventType.START_TURN);
        GameManager.getController().reload();
        if (getCoins() >= 10) {
            selectPlayerToCoup();
            return;
        }
        GameManager.getController().enableAllActionButtons();
    }

    @Override
    public void endTurn() {

        GameBoard controller = GameManager.getController();
        controller.disableAllActionButtons();
    }

    @Override
    public void timeToChallenge() {

        ButtonType challengeButton = new ButtonType("Chalesh", ButtonBar.ButtonData.OK_DONE);
        ButtonType passButton = new ButtonType("Bikhial", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Aya ghasd dari " + GameManager.getInstance().getLastEvent().getActingPlayer().getNickName() + " ro be chalesh bekeshi?",
                challengeButton,
                passButton);

        alert.setTitle("ChallengeTime");
        alert.showAndWait().ifPresent(response -> {
            if (response == challengeButton) {
                challenge();
            }
        });
    }

    @Override
    public void timeToBlockForeignAid(Player targetPlayer) {

        ButtonType blockButton = new ButtonType("Block", ButtonBar.ButtonData.OK_DONE);
        ButtonType passButton = new ButtonType("Bikhial", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Aya ghasd dari " + GameManager.getInstance().getLastEvent().getActingPlayer().getNickName() + " ro block koni?",
                blockButton,
                passButton);

        alert.setTitle("BlockTime");
        alert.showAndWait().ifPresent(response -> {
            if (response == blockButton) {
                if (claim(GameCharacter.PRINCE)) {
                    blockForeignAid(targetPlayer);
                }
            }
        });
    }

    @Override
    public void selectOneCardToRemove() {

        if (this.getNumberOfCardsIn() == 2) {

            ButtonType card1Button = new ButtonType(this.getCards().get(0).getGameCharacter().name(), ButtonBar.ButtonData.LEFT);
            ButtonType card2Button = new ButtonType(this.getCards().get(1).getGameCharacter().name(), ButtonBar.ButtonData.RIGHT);
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Kodoom ro hazf mikoni?",
                    card1Button,
                    card2Button);

            alert.setTitle("RemoveCard");
            alert.showAndWait().ifPresent(response -> {
                if (response == card1Button) {
                    this.burnCard(this.getCards().get(0));
                }
                else if (response == card2Button) {
                    this.burnCard(this.getCards().get(1));
                }
            });
        }
        else {
            GameManager.getInstance().burnPlayer(this);
        }
    }

    @Override
    public void timeToSelectTargetToMurder() {
        GameManager.getController().initMurderButtons();
        GameManager.getController().selectMurderTargetPlayerVBox.setVisible(true);
        GameManager.getController().disableAllActionButtons();
    }

    @Override
    public void murdererReaction(Event murderAttempt) {

        ButtonType claimHavePrincess = new ButtonType("PRINCESS", ButtonBar.ButtonData.LEFT);
        ButtonType giveUp = new ButtonType("GIVE UP", ButtonBar.ButtonData.RIGHT);
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                murderAttempt.getActingPlayer().getNickName() + " be shoma sooeghasd kardeh. che mikonid?",
                claimHavePrincess,
                giveUp);

        alert.setTitle("MurderReaction");
        alert.showAndWait().ifPresent(response -> {
            if (response == claimHavePrincess) {
                if (this.claim(GameCharacter.PRINCESS)) {
                    Event murderStopped = new Event(this, murderAttempt.getActingPlayer(), GameCharacter.PRINCESS, GameCharacter.MURDERER, Event.EventType.STOP_MURDER);
                    GameManager.getInstance().addEvent(murderStopped);
                }
                else {
                    GameManager.getInstance().getCurrentPlayer().changeCoinBy(-3);
                    Event murder = new Event(murderAttempt.getActingPlayer(), murderAttempt.getTargetPlayer(), GameCharacter.MURDERER, null, Event.EventType.MURDER);
                    GameManager.getInstance().addEvent(murder);
                    this.selectOneCardToRemove();
                }
            }
            else if (response == giveUp) {
                GameManager.getInstance().getCurrentPlayer().changeCoinBy(-3);
                Event murder = new Event(murderAttempt.getActingPlayer(), murderAttempt.getTargetPlayer(), GameCharacter.MURDERER, null, Event.EventType.MURDER);
                GameManager.getInstance().addEvent(murder);
                this.selectOneCardToRemove();
            }
        });

    }

    @Override
    public void timeToChangeCards() {

        GameManager.getController().initChangeCardCheckBoxes();
        GameManager.getController().selectNewCardsVBox.setVisible(true);
        GameManager.getController().disableAllActionButtons();
    }

    @Override
    public void timeToSelectTargetToTribute() {

        GameManager.getController().initTributeButtons();
        GameManager.getController().selectTributeTargetPlayerVBox.setVisible(true);
        GameManager.getController().disableAllActionButtons();
    }

    @Override
    public void commanderReaction(Event tributeAttempt) {

        ButtonType defend = new ButtonType("DEFEND", ButtonBar.ButtonData.RIGHT);
        ButtonType giveUp = new ButtonType("GIVE UP", ButtonBar.ButtonData.LEFT);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                tributeAttempt.getActingPlayer().getNickName() + " mikhad az shoma baj begireh!",
                defend,
                giveUp);

        alert.setTitle("CommanderReaction");

        alert.showAndWait().ifPresent(response -> {
            if (response == defend) {

                ButtonType claimHaveAmbassador = new ButtonType("AMBASSADOR", ButtonBar.ButtonData.OTHER);
                ButtonType claimHaveCommander = new ButtonType("COMMANDER", ButtonBar.ButtonData.OTHER);
                Alert alert2 = new Alert(Alert.AlertType.WARNING,
                        "kodoom ro dari?",
                        claimHaveAmbassador,
                        claimHaveCommander);

                alert2.setTitle("CommanderReaction");

                alert2.showAndWait().ifPresent(response2 -> {
                    if (response2 == claimHaveAmbassador) {
                        if (this.claim(GameCharacter.AMBASSADOR)) {
                            Event tributeStopped = new Event(this, tributeAttempt.getActingPlayer(), GameCharacter.AMBASSADOR, GameCharacter.COMMANDER, Event.EventType.STOP_TRIBUTE);
                            GameManager.getInstance().addEvent(tributeStopped);
                        }
                        else {
                            tributeAttempt.getActingPlayer().changeCoinBy(-1 * tributeAttempt.getTargetPlayer().changeCoinBy(-2));
                            Event tribute = new Event(tributeAttempt.getActingPlayer(), tributeAttempt.getTargetPlayer(), GameCharacter.COMMANDER, null, Event.EventType.TRIBUTE);
                            GameManager.getInstance().addEvent(tribute);
                        }
                    }
                    else if (response2 == claimHaveCommander) {
                        if (this.claim(GameCharacter.COMMANDER)) {
                            Event tributeStopped = new Event(this, tributeAttempt.getActingPlayer(), GameCharacter.COMMANDER, GameCharacter.COMMANDER, Event.EventType.STOP_TRIBUTE);
                            GameManager.getInstance().addEvent(tributeStopped);
                        }
                        else {
                            tributeAttempt.getActingPlayer().changeCoinBy(-1 * tributeAttempt.getTargetPlayer().changeCoinBy(-2));
                            Event tribute = new Event(tributeAttempt.getActingPlayer(), tributeAttempt.getTargetPlayer(), GameCharacter.COMMANDER, null, Event.EventType.TRIBUTE);
                            GameManager.getInstance().addEvent(tribute);
                        }
                    }
                });

            }
            else if (response == giveUp) {
                tributeAttempt.getActingPlayer().changeCoinBy(-1 * tributeAttempt.getTargetPlayer().changeCoinBy(-2));
                Event tribute = new Event(tributeAttempt.getActingPlayer(), tributeAttempt.getTargetPlayer(), GameCharacter.COMMANDER, null, Event.EventType.TRIBUTE);
                GameManager.getInstance().addEvent(tribute);
            }
        });


    }

    @Override
    public void selectPlayerToCoup() {

        GameManager.getController().initCoupButtons();
        GameManager.getController().selectCoupTargetPlayerVBox.setVisible(true);
        GameManager.getController().disableAllActionButtons();
    }

    @Override
    public void selectOneCardToChange() {

        if (getNumberOfCardsIn() == 2) {

            ButtonType card1 = new ButtonType(this.getCards().get(0).getGameCharacter().name(), ButtonBar.ButtonData.LEFT);
            ButtonType card2 = new ButtonType(this.getCards().get(1).getGameCharacter().name(), ButtonBar.ButtonData.RIGHT);
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "kodoom kart ro moavezeh mikoni?",
                    card1,
                    card2);

            alert.setTitle("ChangeCard");

            alert.showAndWait().ifPresent(response -> {
                if (response == card1) GameManager.getInstance().showInfo("avaz shod! karte jadid: " + changeCard(this.getCards().get(0)).getGameCharacter().name());
                else if (response == card2) GameManager.getInstance().showInfo("avash shod! karte jadid: " + changeCard(this.getCards().get(1)).getGameCharacter().name());
            });
        }
        else {
            for (Card card : getCards()){
                if (card.getCardStatus() == Card.CardStatus.IN) {
                    GameManager.getInstance().showInfo("avaz shod! karte jadid: " + changeCard(card).getGameCharacter().name());
                }
            }
        }
    }
}
