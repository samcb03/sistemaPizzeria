package uv.lis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uv.lis.modelo.conexion.ConexionBD;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(
            getClass().getResource("/uv/lis/gui/login/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Italia Pizza — Sistema de Administración");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        ConexionBD.cerrarConexion();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
