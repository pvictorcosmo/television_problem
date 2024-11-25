package com.example.television_problem.services;

import java.util.concurrent.Semaphore;

class ControlTvService {
    private final Semaphore control = new Semaphore(1, true);
    private int actuallyChannel = -1;

    public void watchTv(int id,int channel) {

        if(channel == actuallyChannel) {
            control.release();
        }

    }
//
//    public void assistirTV(int id, int canal) throws InterruptedException {
//            while (canalAtual != -1 && canalAtual != canal) {
//                System.out.println("Hóspede " + id + " está esperando pelo canal " + canal + ".");
//                wait();
//            }
//
//            if (canalAtual == -1) {
//                canalAtual = canal;
//                System.out.println("Hóspede " + id + " mudou para o canal " + canal + ".");
//            }
//
//            assistindoNoCanal++;
//            System.out.println("Hóspede " + id + " está assistindo ao canal " + canal + ".");
//        }
//        controle.release(); // Libera o controle para outro hóspede que queira assistir
//    }
//
//    public void pararDeAssistir(int id, int canal) {
//        synchronized (this) {
//            assistindoNoCanal--;
//            System.out.println("Hóspede " + id + " parou de assistir ao canal " + canal + ".");
//
//            if (assistindoNoCanal == 0) {
//                canalAtual = -1;
//                System.out.println("O controle da TV agora está livre.");
//                notifyAll(); // Acorda os hóspedes esperando pelo controle
//            }
//        }
//    }

}
