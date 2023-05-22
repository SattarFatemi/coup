package Models;

import Logic.GameManager;

public class Card {

    public enum CardStatus {
        IN,
        OUT,
        BURNED
    }

    private Integer id;
    private CardStatus cardStatus = CardStatus.OUT;
    private GameCharacter gameCharacter;

    public Card(Integer id, CardStatus cardStatus, GameCharacter gameCharacter) {
        this.id = id;
        this.cardStatus = cardStatus;
        this.gameCharacter = gameCharacter;
    }

    public Player getPlayer() {

        for (Player player : GameManager.getInstance().getPlayers()) {
            if (player.getCardIds().contains(getId())) {
                return player;
            }
        }

        return null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CardStatus getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(CardStatus cardStatus) {
        this.cardStatus = cardStatus;
    }

    public GameCharacter getGameCharacter() {
        return gameCharacter;
    }

    public void setGameCharacter(GameCharacter gameCharacter) {
        this.gameCharacter = gameCharacter;
    }

    public String getImagePath() {

        if (cardStatus == CardStatus.BURNED) {
            return getGameCharacter().getImagePath();
        }
        else {
            return "CardImages\\backOfCard.png";
        }

    }

    public String getImagePathLikeItsBurned() {
        return getGameCharacter().getImagePath();
    }

}
