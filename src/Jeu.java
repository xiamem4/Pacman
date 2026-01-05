import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class Jeu extends JFrame {

    private Terrain terrain;
    private ZoneDeJeu zoneDeJeu;
    private Pacman pacman;
    private Fantome aleaFantome;
    private boolean enCours;

    /**
     * Initialise le jeu :
     */

    public Jeu() {

        terrain = new Terrain();
        zoneDeJeu = new ZoneDeJeu(terrain, pacman, aleaFantome);
        enCours = true;

        int[] pos = terrain.getPositionPacman();
        pacman = new Pacman(pos[0], pos[1]);
        zoneDeJeu.setPacman(pacman);
        ajouterFantomeAleatoire();
        zoneDeJeu.setAleaFantome(aleaFantome);

        setTitle("Pacman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(zoneDeJeu);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Gestion des touches et du déplacement
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> pacman.setDirection(0);
                    case KeyEvent.VK_DOWN -> pacman.setDirection(1);
                    case KeyEvent.VK_LEFT -> pacman.setDirection(2);
                    case KeyEvent.VK_RIGHT -> pacman.setDirection(3);
                }
            }
        });
    }

    // Dans votre classe Jeu
    public void ajouterFantomeAleatoire() {
        Random rand = new Random();
        int x, y;
        int[] posPac = terrain.getPositionPacman();

        do {
            // Génère un nombre entre 0 et le max de colonnes/lignes
            x = rand.nextInt(terrain.getNbColonnes());
            y = rand.nextInt(terrain.getNbLignes());

            // On recommence si c'est un mur ou la position du Pacman
        } while (terrain.estMur(x, y) || (x == posPac[0] && y == posPac[1]));

        this.aleaFantome = new Fantome(x, y);
    }

    // Calculer la MAJ des déplacements et détection de la victoire
    public void mettreAJour() {
        // Pacman
        if (pacman != null) {
            pacman.bouger(terrain);
            if (terrain.getGommes().isEmpty()) {
                rafraichir();
                arreter();
                JOptionPane.showMessageDialog(this, "Félicitations, vous avez gagné !", "Victoire", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
                return;
            }
        }

        // Fantome aléatoire
        if (aleaFantome != null) {
            aleaFantome.choisirDirectionAleatoire();
            aleaFantome.bougerAlea(terrain);
        }
    }

    // Mette à jour de la zone de jeu
    public void rafraichir() {
        zoneDeJeu.repaint();
    }

    // Vérifier si le jeu est en cours
    public boolean estEnCours() {
        return enCours;
    }

    // Arrêter le jeu
    public void arreter() {
        enCours = false;
    }

    public static void main(String[] args) {

        Jeu jeu = new Jeu();
        Processus processus = new Processus(jeu);

        Thread thread = new Thread(processus);
        thread.start();
    }
}
