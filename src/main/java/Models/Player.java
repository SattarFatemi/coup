package Models;

import Logic.GameManager;

import java.util.ArrayList;
import java.util.Random;

public abstract class Player {

    public enum PlayerStatus {
        IN,
        OUT
    }

    public enum PlayerType {
        HUMAN,
        BOT
    }

    private String nickName;
    private Integer id;
    private Integer coins;
    private ArrayList<Integer> cardIds = new ArrayList<>();
    private PlayerStatus playerStatus = PlayerStatus.IN;
    private PlayerType playerType;

    public abstract void startTurn();
    public abstract void endTurn();
    public abstract void timeToChallenge();
    public abstract void timeToBlockForeignAid(Player targetPlayer);
    public abstract void selectOneCardToRemove();
    public abstract void timeToSelectTargetToMurder();
    public abstract void murdererReaction(Event murderAttempt);
    public abstract void timeToChangeCards();
    public abstract void timeToSelectTargetToTribute();
    public abstract void commanderReaction(Event tributeAttempt);
    public abstract void selectPlayerToCoup();
    public abstract void selectOneCardToChange();

    public boolean claim(GameCharacter actingGameCharacter) {
        GameManager.getInstance().addEvent(this, null, actingGameCharacter, null, Event.EventType.CLAIM);
        return GameManager.getInstance().waitForChallenge(GameManager.getInstance().getLastEvent());
    }

    public void challenge() {
        Event lastClaim = GameManager.getInstance().getLastEvent();
        Event challengeEvent = new Event(this, lastClaim.getActingPlayer(), null, lastClaim.getActingGameCharacter(), Event.EventType.CHALLENGE);

        GameManager.getInstance().addEvent(challengeEvent);
        GameManager.getInstance().setClaimChallenge(challengeEvent);
    }

    public void blockForeignAid(Player targetPlayer) {

        Event blockForeignAid = new Event(this, targetPlayer, null, GameCharacter.PRINCE, Event.EventType.BLOCK_FOREIGN_AID);

        GameManager.getInstance().addEvent(blockForeignAid);
        GameManager.getInstance().setBlockForeignAidEvent(blockForeignAid);
    }

    public void gainOneCoin() {

        GameManager.getInstance().getCurrentPlayer().changeCoinBy(1);
        GameManager.getController().reload();

        GameManager.getInstance().addEvent(GameManager.getInstance().getCurrentPlayer(), null, null, null, Event.EventType.GAIN_ONE_COIN);

        GameManager.getInstance().nextTurn();
    }

    public void gainTwoCoins() {

        if (GameManager.getInstance().waitForBlockForeignAid(this)) {
            GameManager.getInstance().getCurrentPlayer().changeCoinBy(2);
            GameManager.getController().reload();

            GameManager.getInstance().addEvent(GameManager.getInstance().getCurrentPlayer(), null, null, null, Event.EventType.GAIN_TWO_COINS);
        }

        GameManager.getInstance().nextTurn();
    }

    public void princeAction() {

        if (GameManager.getInstance().getCurrentPlayer().claim(GameCharacter.PRINCE)) {

            GameManager.getInstance().getCurrentPlayer().changeCoinBy(3);
            GameManager.getController().reload();

            GameManager.getInstance().addEvent(GameManager.getInstance().getCurrentPlayer(), null, GameCharacter.PRINCE, null, Event.EventType.PRINCE_ACTION);
        }

        GameManager.getInstance().nextTurn();
    }

    public void murdererAction() {

        if (GameManager.getInstance().getCurrentPlayer().claim(GameCharacter.MURDERER)) {
            this.timeToSelectTargetToMurder();
        }
        else {
            GameManager.getInstance().nextTurn();
        }
    }

    public void ambassadorAction() {

        if (GameManager.getInstance().getCurrentPlayer().claim(GameCharacter.AMBASSADOR)) {
            this.timeToChangeCards();
        }
        else {
            GameManager.getInstance().nextTurn();
        }
    }

    public void commanderAction() {

        if (GameManager.getInstance().getCurrentPlayer().claim(GameCharacter.COMMANDER)) {
            this.timeToSelectTargetToTribute();
        }
        else {
            GameManager.getInstance().nextTurn();
        }
    }

    public void changeCardAction() {

        if (this.getCoins() >= 1) {
            selectOneCardToChange();
            GameManager.getInstance().addEvent(this, null, null, null, Event.EventType.EXCHANGE_CARD);
            changeCoinBy(-1);
            GameManager.getInstance().nextTurn();
        }
        else {
            GameManager.getInstance().showError("avval poola! haddeaghal 1 sekkeh.");
        }
    }

    public boolean haveGameCharacter(GameCharacter gameCharacter) {

        boolean have = false;
        for (Card card : this.getCards()) if (card.getCardStatus() == Card.CardStatus.IN &&
                card.getGameCharacter() == gameCharacter) {

            have = true;
            break;
        }
        return have;
    }

    public Integer changeCoinBy(Integer numberOfCoinsToChange) {
        Integer coinsBeforeChange = this.getCoins();
        this.setCoins(Math.max(coinsBeforeChange + numberOfCoinsToChange, 0));
        return this.getCoins() - coinsBeforeChange;
    }

    public int getNumberOfCardsIn() {

        int numberOfCardsIn = 0;
        for (Card card : getCards()) {
            if (card.getCardStatus() == Card.CardStatus.IN) numberOfCardsIn++;
        }

        return numberOfCardsIn;
    }

    public void burnCard(Card card) {

        card.setCardStatus(Card.CardStatus.BURNED);
        GameManager.getController().reload();
    }

    public void burnAllCards() {
        for (Card card : getCards()) burnCard(card);
    }

    public Card changeCard(Card oldCard) {

        Random random = new Random();
        Card newCard = GameManager.getInstance().getCardsOut().get(random.nextInt(GameManager.getInstance().getCardsOut().size()));

        this.removeCard(oldCard);
        this.addCard(newCard);

        return newCard;
    }

    public void addCard(Card card) {
        card.setCardStatus(Card.CardStatus.IN);
        cardIds.add(card.getId());
    }

    public void addCardAsItIs(Card card) {
        cardIds.add(card.getId());
    }

    public void removeCard(Card card) {
        card.setCardStatus(Card.CardStatus.OUT);
        cardIds.remove(card.getId());
    }

    public Card findCardByGameCharacter(GameCharacter gameCharacter) {

        for (Card card : getCards()) {
            if (card.getGameCharacter() == gameCharacter) return card;
        }
        return null;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<Integer> getCardIds() {
        return cardIds;
    }

    public void setCardIds(ArrayList<Integer> cardIds) {
        this.cardIds = cardIds;
    }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public ArrayList<Card> getCards() {

        ArrayList<Card> cards = new ArrayList<>();
        cards.add(GameManager.getInstance().findCardById(this.getCardIds().get(0)));
        cards.add(GameManager.getInstance().findCardById(this.getCardIds().get(1)));
        return cards;
    }
}
