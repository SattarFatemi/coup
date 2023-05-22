package Models;

import Logic.GameManager;

public class Event {

    public enum EventType {
        START_TURN,
        CLAIM,
        CHALLENGE,
        CHALLENGE_FAILED,
        CHALLENGE_SUCCESS,
        GAIN_ONE_COIN,
        GAIN_TWO_COINS,
        MURDER,
        MURDER_ATTEMPT,
        STOP_MURDER,
        PRINCE_ACTION,
        BLOCK_FOREIGN_AID,
        AMBASSADOR_ACTION,
        TRIBUTE_ATTEMPT,
        TRIBUTE,
        STOP_TRIBUTE,
        COUP,
        EXCHANGE_CARD,
        GAME_FINISHED,
    }

    private Player actingPlayer;
    private Player targetPlayer;
    private GameCharacter actingGameCharacter;
    private GameCharacter targetGameCharacter;
    private EventType eventType;

    public Event(Player actingPlayer, Player targetPlayer, GameCharacter actingGameCharacter, GameCharacter targetGameCharacter, EventType eventType) {
        this.actingPlayer = actingPlayer;
        this.targetPlayer = targetPlayer;
        this.actingGameCharacter = actingGameCharacter;
        this.targetGameCharacter = targetGameCharacter;
        this.eventType = eventType;
        GameManager.getController().reload();
    }

    public Player getActingPlayer() {
        return actingPlayer;
    }

    public void setActingPlayer(Player actingPlayer) {
        this.actingPlayer = actingPlayer;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }

    public void setTargetPlayer(Player targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    public GameCharacter getActingGameCharacter() {
        return actingGameCharacter;
    }

    public void setActingGameCharacter(GameCharacter actingGameCharacter) {
        this.actingGameCharacter = actingGameCharacter;
    }

    public GameCharacter getTargetGameCharacter() {
        return targetGameCharacter;
    }

    public void setTargetGameCharacter(GameCharacter targetGameCharacter) {
        this.targetGameCharacter = targetGameCharacter;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
