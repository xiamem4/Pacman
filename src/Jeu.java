import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class Jeu extends JPanel {

    private Terrain terrain;
    private ZoneDeJeu zoneDeJeu;
    private Pacman pacman;
    private List<Fantome> fantomes = new ArrayList<Fantome>();
    private Fantome aleaFantome;
    private boolean enCours;
    private int vies = 3;
    private int score = 0;
    private int compteurInvincibilite = 0;
    private Interface hudPanel;
    private int departX;
    private int departY;
    private int compteurSuper = 0;

    /**
     * Initialise le jeu
     * 
     * @param hudPanel : l'interface
     * @param niveau   : labyrinthe du niveau
     */
    public Jeu(Interface hudPanel, int niveau) {

        this.hudPanel = hudPanel;

        setLayout(new BorderLayout());
        terrain = new Terrain();
        terrain.chargerNiveau(niveau);
        zoneDeJeu = new ZoneDeJeu(terrain, pacman, fantomes);
        enCours = true;

        int[] pos = terrain.getPositionPacman();
        departX = pos[0];
        departY = pos[1];

        pacman = new Pacman(departX, departY);
        zoneDeJeu.setPacman(pacman);
        for (int i = 0; i < 4; i++) {
            ajouterFantomeAleatoire();
        }
        zoneDeJeu.setFantomes(fantomes);

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
        ArrayList<int[]> positionsF = new ArrayList<>();
        char[][] grille = terrain.getGrille();

        // Parcour de la grille
        for (int y = 0; y < grille.length; y++) {
            for (int x = 0; x < grille[0].length; x++) {
                if (grille[y][x] == 'F') {
                    positionsF.add(new int[] { x, y });
                }
            }
        }

        if (!positionsF.isEmpty()) {
            int[] posChoisie = positionsF.get(rand.nextInt(positionsF.size()));
            this.fantomes.add(new Fantome(posChoisie[0], posChoisie[1]));
        } else {
            int x, y;
            int[] posPac = terrain.getPositionPacman();
            do {
                x = rand.nextInt(terrain.getNbColonnes());
                y = rand.nextInt(terrain.getNbLignes());
            } while (terrain.estMur(x, y) || (x == posPac[0] && y == posPac[1]));

            this.fantomes.add(new Fantome(x, y));
        }
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
            for (int i = 0; i < fantomes.size(); i++) {
                Fantome f = fantomes.get(i);
                if (pacman.getX() == f.getX() && pacman.getY() == f.getY() && compteurInvincibilite == 0) {
                    if (compteurSuper > 0) {
                        score += 250;
                        hudPanel.updateScore(score);
                        fantomes.remove(i);
                        ajouterFantomeAleatoire();
                        i--;
                    } else {
                        perdreVie();
                        compteurInvincibilite = 10;
                        break;
                    }
                }
            }
        }

        // Fantome aléatoire

        for (Fantome fantome : fantomes) {
            fantome.choisirDirectionAleatoire();
            fantome.bougerAlea(terrain);
        }

        // Gestion des collisions
        if (pacman != null && aleaFantome != null) {

            if (pacman.getX() == aleaFantome.getX() && pacman.getY() == aleaFantome.getY()
                    && compteurInvincibilite == 0) {

                if (compteurSuper > 0) { // Si Super Mode
                    score += 250;
                    hudPanel.updateScore(score);

                    ajouterFantomeAleatoire();
                    zoneDeJeu.setFantomes(fantomes);
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