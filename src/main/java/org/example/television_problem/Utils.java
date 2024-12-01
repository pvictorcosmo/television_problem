package org.example.television_problem;

public class Utils {

    // Modifica a função para aceitar um callback (Runnable) como parâmetro
    public static void timeCpuBound(double time, Runnable callback) {
        int remainingTime = (int) time; // Variável auxiliar para armazenar o tempo restante

        // Pega o tempo inicial em milissegundos
        long startTime = System.currentTimeMillis();

        // Executa enquanto o tempo restante for maior que 0
        while (remainingTime > 0) {
            // Verifica o tempo atual
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000; // Tempo passado em segundos

            // Se passou 1 segundo desde a última execução
            if (elapsedTime >= (time - remainingTime + 1)) {

                // Executa o callback (passado como argumento)
                callback.run();

                // Decrementa o tempo restante
                remainingTime--;
            }
        }
    }
}
