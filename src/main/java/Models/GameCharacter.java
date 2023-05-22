package Models;

public enum GameCharacter {
    PRINCE,
    MURDERER,
    PRINCESS,
    COMMANDER,
    AMBASSADOR;

    public String getImagePath() {

        return switch (this) {
            case PRINCE -> "CardImages\\Rich.png";
            case MURDERER -> "CardImages\\Murderer.png";
            case PRINCESS -> "CardImages\\Princess.png";
            case COMMANDER -> "CardImages\\Commander.png";
            case AMBASSADOR -> "CardImages\\Ambassador.png";
        };

    }
}