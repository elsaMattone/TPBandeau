package bandeau;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ExerciceAvecThreads {

    public static void main(String[] args) {
        ExerciceAvecThreads instance = new ExerciceAvecThreads();
        instance.exemple();
    }

    public void exemple() {

        Scenario s = makeScenario();
        // On cree les bandeaux
        Bandeau b1 = new Bandeau();
        Bandeau b2 = new Bandeau();
        Bandeau b3 = new Bandeau();
        System.out.println("CTRL-C pour terminer le programme");

        Lock lockB1 = new ReentrantLock(); // Verrou pour b1
        Lock lockB2 = new ReentrantLock(); // Verrou pour b2
        Lock lockB3 = new ReentrantLock(); // Verrou pour b3

        // Créer des threads pour chaque bandeau et jouer le scénario en parallèle
        Thread thread1 = new Thread(() -> {
            playScenario(s, b1, lockB1);
        });
        Thread thread2 = new Thread(() -> {
            playScenario(s, b2, lockB2);
        });
        Thread thread3 = new Thread(() -> {
            playScenario(s, b3, lockB3);
        });

        // Démarrer les threads
        thread1.start();
        thread2.start();
        thread3.start();

        try {
            // Attendre que tous les threads se terminent
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // On rejoue le scénario sur b1 quand le premier jeu est fini
        playScenario(s, b1, lockB1);
    }

    private void playScenario(Scenario scenario, Bandeau bandeau, Lock lockBandeau) {
        lockBandeau.lock(); // Acquérir le verrou pour le bandeau
        try {
            scenario.playOn(bandeau); // Jouer le scénario
        } finally {
            lockBandeau.unlock(); // Libérer le verrou pour le bandeau
        }
    }

    private Scenario makeScenario() {
        // On crée un scenario
        Scenario s = new Scenario();
        // On lui ajoute des effets
        s.addEffect(new RandomEffect("Le jeu du pendu", 700), 1);
        // s.addEffect(new TeleType("Je m'affiche caractère par caractère", 100), 1);
        // s.addEffect(new Blink("Je clignote 10x", 100), 10);
        // s.addEffect(new Zoom("Je zoome", 50), 1);
        // s.addEffect(new FontEnumerator(10), 1);
        // s.addEffect(new Rainbow("Comme c'est joli !", 30), 1);
        s.addEffect(new Rotate("2 tours à droite", 180, 4000, true), 2);
        // s.addEffect(new Rotate("2 tours à gauche", 180, 4000, false), 2);
        return s;
    }
}
