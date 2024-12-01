package org.example.television_problem;

import java.util.logging.*;

public class LogManager {
    private static final Logger logger = Logger.getLogger(LogManager.class.getName());

    static {
        try {
            // Define o formato de saída do log
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);

            // Definir o nível de log
            logger.setLevel(Level.ALL);
        } catch (Exception e) {
            System.err.println("Erro ao configurar o Logger: " + e.getMessage());
        }
    }

    // Método para logar mensagens de INFO
    public static void logInfo(String message) {
        logger.info(message);
    }

    // Método para logar mensagens de WARNING
    public static void logWarning(String message) {
        logger.warning(message);
    }

    // Método para logar mensagens de ERRO
    public static void logError(String message) {
        logger.severe(message);
    }

    // Logar a criação de um hóspede
    public static void logGuestCreation(String guestId, int channel, int watchTime, int restTime) {
        logInfo("Hóspede criado: ID=" + guestId + ", Canal=" + channel + ", Assistindo por: " + watchTime + "s, Descansando por: " + restTime + "s");
    }

    // Logar a mudança de estado de um hóspede (assistindo, descansando, dormindo)
    public static void logGuestStateChange(int guestId, String state) {
        logInfo("Hóspede " + guestId + " agora está: " + state);
    }
}
