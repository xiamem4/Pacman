public class Processus implements Runnable {

    private Jeu jeu;

    public Processus(Jeu jeu) {
        this.jeu = jeu;
    }

    @Override
    public void run() {

        while (jeu.estEnCours()) {
            jeu.mettreAJour();

            jeu.rafraichir();

            try {
                Thread.sleep(200); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
