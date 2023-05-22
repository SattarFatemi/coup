package Models;

import Logic.GameManager;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.Random;

public class Bot extends Player {

    private int numberOfChallengeSituations = 0;

    public int getNumberOfChallengeSituations() {
        return numberOfChallengeSituations;
    }

    public void setNumberOfChallengeSituations(int numberOfChallengeSituations) {
        this.numberOfChallengeSituations = numberOfChallengeSituations;
    }

    @Override
    public void startTurn() {
        GameManager.getInstance().addEvent(this, null, null, null, Event.EventType.START_TURN);

        if (getCoins() >= 10) {
            selectPlayerToCoup();
            return;
        }

        switch (getNickName()) {
            case "Koodetagar" -> {
                if (getCoins() >= 7) selectPlayerToCoup();
                else princeAction();
            }
            case "GhateleMohtat" -> {
                if (haveGameCharacter(GameCharacter.MURDERER) && getCoins() >= 3) murdererAction();
                else if (haveGameCharacter(GameCharacter.AMBASSADOR)) ambassadorAction();
                else if (getCoins() >= 1) changeCardAction();
                else gainTwoCoins();
            }
            case "General" -> {
                if (getCoins() >= 7) selectPlayerToCoup();
                else if (haveGameCharacter(GameCharacter.COMMANDER)) commanderAction();
                else if (haveGameCharacter(GameCharacter.MURDERER) && getCoins() >= 3) murdererAction();
                else princeAction();
            }
            default -> {
                Random rand = new Random();
                switch (rand.nextInt(11)) {
                    case 0 -> this.gainOneCoin();
                    case 1 -> this.gainTwoCoins();
                    case 2 -> this.princeAction();
                    case 3 -> this.ambassadorAction();
                    case 4, 5, 6 -> this.commanderAction();
                    case 7, 8 -> {
                        if (this.getCoins() >= 3) this.murdererAction();
                        else this.gainOneCoin();
                    }
                    default -> {
                        if (this.getCoins() >= 1) this.changeCardAction();
                        else this.gainOneCoin();
                    }
                }
            }
        }
    }

    @Override
    public void endTurn() {
        //NOTHING!
    }

    public void timeToChallenge() {
        switch (getNickName()) {
            case "Paranoid" -> {
                setNumberOfChallengeSituations(getNumberOfChallengeSituations() + 1);
                if (getNumberOfChallengeSituations() % 2 == 1) {
                    challenge();
                }
            }
            case "General" -> {
                if (GameManager.getInstance().getLastEvent().getTargetPlayer() != null
                        && GameManager.getInstance().getLastEvent().getTargetPlayer().getId().equals(this.getId())) {

                    challenge();
                }
                else {

                    Random random = new Random();

                    if (random.nextInt(10) < 2) {
                        challenge();
                    }

                }
            }
            default -> {

                Random random = new Random();

                if (GameManager.getInstance().getLastEvent().getTargetPlayer() != null
                        && GameManager.getInstance().getLastEvent().getTargetPlayer().getId().equals(this.getId())) {

                    if (random.nextInt(10) < 6) {
                        challenge();
                    }

                }
                else {

                    if (random.nextInt(10) < 2) {
                        challenge();
                    }

                }
            }
        }
    }

    @Override
    public void timeToBlockForeignAid(Player targetPlayer) {
        Random random = new Random();
        if (random.nextBoolean()) {
            if (claim(GameCharacter.PRINCE)) {
                blockForeignAid(targetPlayer);
            }
        }
    }

    @Override
    public void selectOneCardToRemove() {

        if (this.getNumberOfCardsIn() == 2) {
            Random random = new Random();
            if (random.nextBoolean()) {
                burnCard(this.getCards().get(0));
            } else {
                burnCard(this.getCards().get(1));
            }
        } else {
            GameManager.getInstance().burnPlayer(this);
        }
    }

    @Override
    public void timeToSelectTargetToMurder() {

        for (Player player : GameManager.getInstance().getPlayers()) {
            if (player.getPlayerStatus() == PlayerStatus.OUT) continue;
            if (player.getId().equals(this.getId())) continue;

            GameManager.getInstance().addEvent(this, player, GameCharacter.MURDERER, null, Event.EventType.MURDER_ATTEMPT);
            player.murdererReaction(GameManager.getInstance().getLastEvent());
            break;
        }

        GameManager.getInstance().nextTurn();
    }

    @Override
    public void murdererReaction(Event murderAttempt) {

        Random rand = new Random();
        switch (rand.nextInt(2)) {
            case 0 -> {
                GameManager.getInstance().getCurrentPlayer().changeCoinBy(-3);
                Event murder = new Event(murderAttempt.getActingPlayer(), murderAttempt.getTargetPlayer(), GameCharacter.MURDERER, null, Event.EventType.MURDER);
                GameManager.getInstance().addEvent(murder);
                this.selectOneCardToRemove();
            }
            case 1 -> {
                if (this.claim(GameCharacter.PRINCESS)) {
                    Event murderStopped = new Event(this, murderAttempt.getActingPlayer(), GameCharacter.PRINCESS, GameCharacter.MURDERER, Event.EventType.STOP_MURDER);
                    GameManager.getInstance().addEvent(murderStopped);
                } else {
                    GameManager.getInstance().getCurrentPlayer().changeCoinBy(-3);
                    Event murder = new Event(murderAttempt.getActingPlayer(), murderAttempt.getTargetPlayer(), GameCharacter.MURDERER, null, Event.EventType.MURDER);
                    GameManager.getInstance().addEvent(murder);
                    this.selectOneCardToRemove();
                }
            }
        }
    }

    @Override
    public void timeToChangeCards() {

        ArrayList<Card> cardsOut = GameManager.getInstance().getCardsOut();

        Random random = new Random();
        Card newCard1 = cardsOut.get(random.nextInt(cardsOut.size()));
        this.addCard(newCard1);

        random = new Random();
        Card newCard2 = cardsOut.get(random.nextInt(cardsOut.size()));
        this.addCard(newCard2);

        if (getNickName().equals("GhateleMohtat")) {

            if (newCard1.getGameCharacter() == GameCharacter.MURDERER) {

                this.removeCard(newCard2);

                if (this.getCards().get(0).getCardStatus() == Card.CardStatus.IN)
                    this.getCards().remove(this.getCards().get(0));
                else this.getCards().remove(this.getCards().get(1));

            } else if (newCard2.getGameCharacter() == GameCharacter.MURDERER) {

                this.removeCard(newCard1);

                if (this.getCards().get(0).getCardStatus() == Card.CardStatus.IN)
                    this.getCards().remove(this.getCards().get(0));
                else this.getCards().remove(this.getCards().get(1));

            } else {
                this.removeCard(newCard1);
                this.removeCard(newCard2);
            }

        } else {

            this.removeCard(newCard2);

            if (this.getCards().get(0).getCardStatus() == Card.CardStatus.IN)
                this.getCards().remove(this.getCards().get(0));
            else this.getCards().remove(this.getCards().get(1));

        }

        GameManager.getInstance().addEvent(GameManager.getInstance().getCurrentPlayer(), null, GameCharacter.AMBASSADOR, null, Event.EventType.AMBASSADOR_ACTION);
        GameManager.getInstance().nextTurn();
    }

    @Override
    public void timeToSelectTargetToTribute() {

        for (Player player : GameManager.getInstance().getPlayers()) {
            if (player.getPlayerStatus() == PlayerStatus.OUT) continue;
            if (player.getId().equals(this.getId())) continue;

            GameManager.getInstance().addEvent(this, player, GameCharacter.COMMANDER, null, Event.EventType.TRIBUTE_ATTEMPT);
            player.commanderReaction(GameManager.getInstance().getLastEvent());
            break;
        }

        GameManager.getInstance().nextTurn();
    }

    @Override
    public void commanderReaction(Event tributeAttempt) {

        if (this.haveGameCharacter(GameCharacter.AMBASSADOR)) {
            // always true:
            this.claim(GameCharacter.AMBASSADOR);

            GameManager.getInstance().addEvent(this, tributeAttempt.getActingPlayer(), GameCharacter.AMBASSADOR, GameCharacter.COMMANDER, Event.EventType.STOP_TRIBUTE);
        } else if (this.haveGameCharacter(GameCharacter.COMMANDER)) {
            // always true:
            this.claim(GameCharacter.COMMANDER);

            GameManager.getInstance().addEvent(this, tributeAttempt.getActingPlayer(), GameCharacter.COMMANDER, GameCharacter.COMMANDER, Event.EventType.STOP_TRIBUTE);
        } else {
            tributeAttempt.getActingPlayer().changeCoinBy(-1 * this.changeCoinBy(-2));

            GameManager.getInstance().addEvent(tributeAttempt.getActingPlayer(), this, GameCharacter.COMMANDER, null, Event.EventType.TRIBUTE);
        }
    }

    @Override
    public void selectPlayerToCoup() {

        for (Player targetPlayer : GameManager.getInstance().getPlayers()) {
            if (targetPlayer.getPlayerStatus() == PlayerStatus.OUT) continue;
            if (targetPlayer.getId().equals(this.getId())) continue;

            GameManager.getInstance().addEvent(this, targetPlayer, null, null, Event.EventType.COUP);
            targetPlayer.selectOneCardToRemove();
            this.changeCoinBy(-7);
            break;
        }

        GameManager.getInstance().nextTurn();
    }

    @Override
    public void selectOneCardToChange() {

        if (getNumberOfCardsIn() == 2) {
            Random random = new Random();
            changeCard(this.getCards().get(random.nextInt(2)));
        } else {
            for (Card card : getCards()) {
                if (card.getCardStatus() == Card.CardStatus.IN) changeCard(card);
            }
        }
    }
}

