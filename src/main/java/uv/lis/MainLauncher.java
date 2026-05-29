package uv.lis;

/**
 * MainLauncher — clase puente para lanzar JavaFX con Maven.
 * Necesaria porque Main extiende Application y el exec-maven-plugin
 * no puede detectar su main() correctamente.
 */
public class MainLauncher {
    public static void main(String[] args) {
        Main.main(args);
    }
}
