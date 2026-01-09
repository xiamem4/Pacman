import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class Jeu extends JPanel {

    private Terrain terrain;
    private ZoneDeJeu zoneDeJeu;
    private Pacman pacman;
    private Fantome aleaFantome;
    private boolean enCours;
    private int vies = 3;
    private int score = 0;
    private int compteurInvincibilite = 0;
    private Interface hudPanel;
    private int departX;
    private int departY;

    /**
     * Initialise le jeu :
     */

    public Jeu(Interface hudPanel, int niveau) {

        this.hudPanel = hudPanel;

        setLayout(new BorderLayout());
        terrain = new Terrain();
        terrain.chargerNiveau(niveau);
        zoneDeJeu = new ZoneDeJeu(terrain, pacman, aleaFantome);
        enCours = true;

        int[] pos = terrain.getPositionPacman();
        departX = pos[0];
        departY = pos[1];

        pacman = new Pacman(departX, departY);
        zoneDeJeu.setPacman(pacman);
        ajouterFantomeAleatoire();
        zoneDeJeu.setAleaFantome(aleaFantome);

        setFocusable(true);
        add(zoneDeJeu);
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

    // Calculer la MAJ des déplacements, la victoire ET la défaite
    public void mettreAJour() {

        // Gestion du temps d'invincibilité
        if (compteurInvincibilite > 0) {
            compteurInvincibilite--;
        }

        // Pacman
        if (pacman != null) {

            pacman.bouger(terrain);

            // Vérification si une gomme est mangée à la nouvelle position
            if (terrain.mangerGomme(pacman.getX(), pacman.getY())) { 
                score += 10;
                hudPanel.updateScore(score);
            }

            // Vérification de la victoire
            if (terrain.getGommes().isEmpty()) {
                rafraichir();
                arreter();
                JOptionPane.showMessageDialog(this, "Félicitations, vous avez gagné !", "Victoire",
                        JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
                return;
            }
        }

        // Fantome aléatoire

        if (aleaFantome != null) {
            aleaFantome.choisirDirectionAleatoire();
            aleaFantome.bougerAlea(terrain);
        }

        // Gestion des collisions
        if (pacman != null && aleaFantome != null) {

            // Si collisions
            if (pacman.getX() == aleaFantome.getX() && pacman.getY() == aleaFantome.getY() && compteurInvincibilite == 0) {
                perdreVie();
                compteurInvincibilite = 10;
            }
        }
    }

    public void perdreVie() {
        vies--;
        hudPanel.updateVie(vies);

        if (vies > 0) {
            terrain.deplacerPacman(pacman.getX(), pacman.getY(), departX, departY);
            pacman.setX(departX);
            pacman.setY(departY);
        }

        // Défaites
        if (vies <= 0) {
            rafraichir();
            arreter();
            JOptionPane.showMessageDialog(this, "Game Over ! Vous avez perdu.", "Défaite", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
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
}