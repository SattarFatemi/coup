import Logic.GameManager;
import Logic.Loader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Main extends Application {

    public static Stage publicPrimaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Loader.start();
        //Loader.configuredStart();

        GameManager.primaryStage = primaryStage;

        primaryStage.setResizable(true);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameBoard.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
