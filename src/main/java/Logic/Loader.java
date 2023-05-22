package Logic;

import Models.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class Loader {

    private static Logger log = LogManager.getLogger(Loader.class);

    public static void configuredStart() {
        loadGameCharacters();
        createCards();
        loadPlayersConfigured();
    }

    public static void start() {
        loadAndSelectPlayers();
        loadGameCharacters();
        createCards();
        spreadCards();
    }

    public static void loadPlayersConfigured() {

        Gson gson = new Gson();

        try {
            File file = new File("src/main/resources/Database/Humans.json");
            JsonReader reader = new JsonReader(new FileReader(file));

            Type humansArrayType = new TypeToken<ArrayList<Human>>(){}.getType();
            ArrayList<Human> humansToLoad = gson.fromJson(reader, humansArrayType);

            for (int i = 0; i < humansToLoad.size(); i++) {
                GameManager.getInstance().addPlayer(humansToLoad.get(i));
            }

        } catch (FileNotFoundException e) {
            log.error("Loading Bots Failed");
            e.printStackTrace();
        }

        try {
            File file = new File("src/main/resources/Database/Bots.json");
            JsonReader reader = new JsonReader(new FileReader(file));

            Type botsArrayType = new TypeToken<ArrayList<Bot>>(){}.getType();
            ArrayList<Bot> botsToLoad = gson.fromJson(reader, botsArrayType);

            // load first three bots
            for (int i = 0; i < botsToLoad.size() - 1; i++) {
                GameManager.getInstance().addPlayer(botsToLoad.get(i));
            }

        } catch (FileNotFoundException e) {
            log.error("Loading Bots Failed");
            e.printStackTrace();
        }

        for (Player player : GameManager.getInstance().getPlayers()) {
            for (Card card : player.getCards()) card.setCardStatus(Card.CardStatus.IN);
        }

    }

    private static void loadAndSelectPlayers() {

        Gson gson = new Gson();

        try {
            File file = new File("src/main/resources/Database/Bots.json");
            JsonReader reader = new JsonReader(new FileReader(file));

            Type botsArrayType = new TypeToken<ArrayList<Bot>>(){}.getType();
            ArrayList<Bot> botsToLoad = gson.fromJson(reader, botsArrayType);

            for (int i = 0; i < botsToLoad.size(); i++) {
                GameManager.getInstance().addPlayer(botsToLoad.get(i));
            }

        } catch (FileNotFoundException e) {
            log.error("Loading Bots Failed");
            e.printStackTrace();
        }

        // delete one bot randomly
        Random random = new Random();
        int randomBotIndex = random.nextInt(4);
        log.info("Player with index " + randomBotIndex + " is deleted!");
        GameManager.getInstance().getPlayers().remove(randomBotIndex);

        try {
            File file = new File("src/main/resources/Database/Humans.json");
            JsonReader reader = new JsonReader(new FileReader(file));

            Type humansArrayType = new TypeToken<ArrayList<Human>>(){}.getType();
            ArrayList<Human> humansToLoad = gson.fromJson(reader, humansArrayType);

            for (int i = 0; i < humansToLoad.size(); i++) {
                GameManager.getInstance().addPlayer(humansToLoad.get(i));
            }

        } catch (FileNotFoundException e) {
            log.error("Loading Bots Failed");
            e.printStackTrace();
        }

        // empty player card arrays
        for (Player player : GameManager.getInstance().getPlayers()) {
            player.setCardIds(new ArrayList<>());
        }

        GameManager.getInstance().shufflePlayers();

    }

    private static void loadGameCharacters() {

        GameCharacter gameCharacter = GameCharacter.AMBASSADOR;
        GameManager.getInstance().addGameCharacter(gameCharacter);

        gameCharacter = GameCharacter.MURDERER;
        GameManager.getInstance().addGameCharacter(gameCharacter);

        gameCharacter = GameCharacter.PRINCE;
        GameManager.getInstance().addGameCharacter(gameCharacter);

        gameCharacter = GameCharacter.PRINCESS;
        GameManager.getInstance().addGameCharacter(gameCharacter);

        gameCharacter = GameCharacter.COMMANDER;
        GameManager.getInstance().addGameCharacter(gameCharacter);

    }

    private static void createCards() {

        int i = 0;
        for (GameCharacter gameCharacter : GameManager.getInstance().getGameCharacters()) {

            for (int j = 0; j < 3; j++) {

                Card card = new Card(i, Card.CardStatus.OUT, gameCharacter);
                GameManager.getInstance().addCard(card);
                i++;

            }

        }

    }

    private static void spreadCards() {

        GameManager.getInstance().shuffleCards();

        for (int j = 0; j < 8; j++) {
            GameManager.getInstance().getPlayers().get(j/2).addCard(GameManager.getInstance().getCards().get(j));
        }

    }
}
