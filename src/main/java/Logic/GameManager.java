package Logic;

import GuiControllers.GameBoard;
import Models.*;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;

public class GameManager {

    private static GameManager gameManager = new GameManager();

    public static Stage primaryStage;

    public static GameBoard controller;

    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<GameCharacter> gameCharacters = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();

    private Integer currentPlayerIndex = -1;

    private Event claimChallenge;
    private boolean botsThreadIsFinished;

    private Event blockForeignAidEvent;

    private boolean gameFinished = false;

    private GameManager() {
        this.gameManager = gameManager;
    }

    public static GameManager getInstance() {
        return gameManager;
    }

    public void start() {
        nextTurn();
    }

    public void nextTurn() {

        if (isGameFinished()) return;

        getController().reload();

        if (getCurrentPlayerIndex() > -1) getCurrentPlayer().endTurn();

        setCurrentPlayerIndex((getCurrentPlayerIndex() + 1) % 4);

        if (getCurrentPlayer().getPlayerStatus() == Player.PlayerStatus.OUT) {
            nextTurn();
            return;
        }

        getCurrentPlayer().startTurn();

    }

    public void finishGame() {


        addEvent(getOnePlayerIn(), null, null, null, Event.EventType.GAME_FINISHED);
        GameManager.getController().reload();

        setGameFinished(true);

        GameManager.getController().finishGame();
    }

    public void addEvent(Event event) {
        events.add(event);
        System.out.println(event.getActingPlayer().getNickName() + ": " + event.getEventType() + ((event.getTargetPlayer() == null) ? "" : ", target: " + event.getTargetPlayer().getNickName()));
    }

    public void addEvent(Player actingPlayer, Player targetPlayer, GameCharacter actingGameCharacter, GameCharacter targetGameCharacter, Event.EventType eventType) {
        addEvent(new Event(actingPlayer, targetPlayer, actingGameCharacter, targetGameCharacter, eventType));
    }

    public boolean waitForChallenge(Event event) {

        claimChallenge = null;

        getController().reload();

        ArrayList<Player> shuffledPlayers = new ArrayList<>(getPlayers());
        Collections.shuffle(shuffledPlayers);

        for (Player player : shuffledPlayers) {
            if (getClaimChallenge() != null) break;
            if (player.getId().equals(event.getActingPlayer().getId())) continue;
            if (player.getPlayerStatus() == Player.PlayerStatus.OUT) continue;

            player.timeToChallenge();
            getController().reload();
        }

        return !validateChallenge(getClaimChallenge());
    }

    public boolean validateChallenge(Event challenge) {

        if (challenge == null) {
            System.out.println("NO CHALLENGE");
            return false;
        }
        boolean challengeIsCorrect = !challenge.getTargetPlayer().haveGameCharacter(challenge.getTargetGameCharacter());

        if (challengeIsCorrect) {

            Event successfulChallenge = new Event(challenge.getActingPlayer(), challenge.getTargetPlayer(), null, challenge.getTargetGameCharacter(), Event.EventType.CHALLENGE_SUCCESS);
            addEvent(successfulChallenge);
            punishForChallenge(challenge.getTargetPlayer());

        } else {

            Event failedChallenge = new Event(challenge.getActingPlayer(), challenge.getTargetPlayer(), null, challenge.getTargetGameCharacter(), Event.EventType.CHALLENGE_FAILED);
            addEvent(failedChallenge);
            punishForChallenge(challenge.getActingPlayer());

            for (Card card : challenge.getTargetPlayer().getCards()) {
                if (card.getCardStatus() == Card.CardStatus.IN && card.getGameCharacter().equals(challenge.getTargetGameCharacter())) {

                    Card newCard = challenge.getTargetPlayer().changeCard(card);

                    if (challenge.getTargetPlayer().getId().equals(GameManager.getInstance().getUser().getId())) {
                        showInfo("avash shod! karte jadid: " + newCard.getGameCharacter().name());
                    }
                    break;
                }
            }

        }

        return challengeIsCorrect;
    }

    public void punishForChallenge(Player player) {
        player.selectOneCardToRemove();
    }

    public boolean waitForBlockForeignAid(Player actingPlayer) {

        blockForeignAidEvent = null;

        getController().reload();

        ArrayList<Player> shuffledPlayers = new ArrayList<>(getPlayers());
        Collections.shuffle(shuffledPlayers);

        for (Player player : shuffledPlayers) {
            if (blockForeignAidEvent != null) break;
            if (player.getId().equals(actingPlayer.getId())) continue;
            if (player.getPlayerStatus() == Player.PlayerStatus.OUT) continue;

            player.timeToBlockForeignAid(actingPlayer);
            getController().reload();
        }

        return blockForeignAidEvent == null;
    }

    public void shufflePlayers() {
        Collections.shuffle(this.players);
    }

    public void shuffleCards() {
        Collections.shuffle(this.cards);
    }

    public Card findCardById(Integer id) {

        for (Card card : getCards()) {
            if (card.getId().equals(id)) {
                return card;
            }
        }

        return null;
    }

    public Player findPlayerByNickName(String nickName) {

        for (Player player : getPlayers()) {
            if (player.getNickName().equals(nickName)) {
                return player;
            }
        }

        return null;
    }

    public void burnPlayer(Player player) {

        if (player.getId().equals(getUser().getId())) {
            showInfo("SHOMA HAZF SHODID...");
        }

        player.burnAllCards();
        player.setPlayerStatus(Player.PlayerStatus.OUT);

        if (getNumberOfPlayersIn() == 1) finishGame();
    }

    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("Deghghat konin!");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("INFORMATION");
        alert.setHeaderText("Dar jaryan bash!");
        alert.setContentText(message);
        alert.showAndWait();
    }


    public int getNumberOfPlayersIn() {
        int ans = 0;
        for (Player player : getPlayers()) if (player.getPlayerStatus() == Player.PlayerStatus.IN) ans++;
        return ans;
    }

    public Player getOnePlayerIn() {
        for (Player player : getPlayers()) if (player.getPlayerStatus() == Player.PlayerStatus.IN) return player;
        return null;
    }

    public Human getUser() {
        for (Player player : getPlayers()) {
            if (player instanceof Human) return (Human) player;
        }
        return null;
    }

    public ArrayList<Card> getCardsOut() {
        ArrayList<Card> cardsOut = new ArrayList<>();
        for (Card card : getCards()) {
            if (card.getCardStatus() == Card.CardStatus.OUT) cardsOut.add(card);
        }
        return cardsOut;
    }


    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<GameCharacter> getGameCharacters() {
        return gameCharacters;
    }

    public void setGameCharacters(ArrayList<GameCharacter> gameCharacters) {
        this.gameCharacters = gameCharacters;
    }

    public void addGameCharacter(GameCharacter gameCharacter) {
        this.getGameCharacters().add(gameCharacter);
    }

    public Player getCurrentPlayer() {
        return getPlayers().get(getCurrentPlayerIndex());
    }

    public Integer getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void setCurrentPlayerIndex(Integer currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public static GameBoard getController() {
        return controller;
    }

    public static void setController(GameBoard controller) {
        GameManager.controller = controller;
    }

    public Event getLastEvent() {
        if (events.isEmpty()) return null;
        return events.get(events.size() - 1);
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public Event getBlockForeignAidEvent() {
        return blockForeignAidEvent;
    }

    public void setBlockForeignAidEvent(Event blockForeignAidEvent) {
        this.blockForeignAidEvent = blockForeignAidEvent;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    public void setClaimChallenge(Event event) {
        this.claimChallenge = event;
    }

    public Event getClaimChallenge() {
        return claimChallenge;
    }
}
