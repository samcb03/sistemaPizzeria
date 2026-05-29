package uv.lis;

/**
 * MainLauncher — Clase de arranque para proyectos JavaFX con Maven.
 * 
 * Cuando Main extiende Application, algunos launchers de Maven/JVM no pueden
 * detectar el main() correctamente. Esta clase actúa como puente.
 */
public class MainLauncher {
    public static void main(String[] args) {
        Main.main(args);
    }
}
