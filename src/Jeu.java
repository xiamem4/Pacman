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
    private int compteurSuper = 0;

    /** Initialise le jeu
     * @param hudPanel : l'interface
     * @param niveau : labyrinthe du niveau
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

    // Ajout du fantôme aléatoire
    public void ajouterFantomeAleatoire() {
        Random rand = new Random();
        int x, y;
        int[] posPac = terrain.getPositionPacman();

        do {
            x = rand.nextInt(terrain.getNbColonnes());
            y = rand.nextInt(terrain.getNbLignes());

        } while (terrain.estMur(x, y) || (x == posPac[0] && y == posPac[1]));

        this.aleaFantome = new Fantome(x, y);
    }

    // Cyle du jeu
    public void mettreAJour() {

        // Gestion du temps d'invincibilité
        if (compteurInvincibilite > 0) {
            compteurInvincibilite--;
        }

        // Gestion du temps de Super Mode
        if (compteurSuper > 0) {
            compteurSuper--;
        }

        zoneDeJeu.setFantomesVulnerables(compteurSuper > 0);

        // Pacman
        if (pacman != null) {

            pacman.bouger(terrain);

            // Vérification si une gomme est mangée à la nouvelle position
            PacGomme gommeMangee = terrain.mangerGomme(pacman.getX(), pacman.getY());
            if (gommeMangee != null) { 
                if (gommeMangee instanceof SuperPacGomme) {
                    score += 50;
                    compteurSuper = 50;
                } else {
                    score +=10;
                }
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

            if (pacman.getX() == aleaFantome.getX() && pacman.getY() == aleaFantome.getY() && compteurInvincibilite == 0) {

                if (compteurSuper > 0 ){ // Si Super Mode
                    score += 250;
                    hudPanel.updateScore(score);
                    
                    ajouterFantomeAleatoire();
                    zoneDeJeu.setAleaFantome(aleaFantome);
                } else {
                    perdreVie();
                    compteurInvincibilite = 10;
                }
                
            }
        }
    }

    // Action de perdre une vie
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