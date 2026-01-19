package pacman;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Jeu extends JPanel {

    private Terrain terrain;
    private ZoneDeJeu zoneDeJeu;
    private Pacman pacman;

    private List<Fantome> fantomes = new ArrayList<Fantome>(); // Liste des fantômes
    private List<Fantome> fantomesEnAttente = new ArrayList<>(); // Liste des fantômes dans la zone de départ
    private int compteurSortieFantome = 0; // Compteur de fantomes sortis
    private int[] positionPorte; // Position des la portes

    private boolean enCours; // Définit si le jeu est en cours

    private int vies = 3; // Nombre de vie
    private int score = 0; // Nombre de point
    private int compteurInvincibilite = 0; // Gestion du temps d'invincibilité

    private Interface hudPanel;

    private int departX;
    private int departY;

    private int compteurSuper = 0; // Temps sous la forme Super

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

        int[] pos = terrain.getPositionPacman();
        departX = pos[0];
        departY = pos[1];

        pacman = new Pacman(departX, departY);
        this.positionPorte = trouverPorte();

        zoneDeJeu = new ZoneDeJeu(terrain, pacman, fantomes);
        enCours = true;

        zoneDeJeu.setFantomesEnAttente(fantomesEnAttente);

        zoneDeJeu.setPacman(pacman);

        for (int i = 0; i < 4; i++) {
            preparerFantome();
        }

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

    // Trouver la porte
    private int[] trouverPorte() {
        char[][] grille = terrain.getGrille();
        for (int y = 0; y < grille.length; y++) {
            for (int x = 0; x < grille[0].length; x++) {
                if (grille[y][x] == 'D') {
                    return new int[] { x, y };
                }
            }
        }
        return terrain.getPositionPacman();
    }

    // Ajout du fantôme aléatoire
    public void preparerFantome() {
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
            int[] pos = positionsF.get(rand.nextInt(positionsF.size()));
            fantomesEnAttente.add(new Fantome(pos[0], pos[1]));
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

            pacman.bouger(terrain);

            // Vérification pomme mangée
            PacGomme gommeMangee = terrain.mangerGomme(pacman.getX(), pacman.getY());

            if (gommeMangee != null) {
                if (gommeMangee instanceof SuperPacGomme) {
                    score += 50;
                    compteurInvincibilite = 50;
                    compteurSuper = 50;
                } else {
                    score += 10;
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

            // Sortie des fantômes
            if (!fantomesEnAttente.isEmpty()) {
                compteurSortieFantome++;
                if (compteurSortieFantome >= 45) {
                    Fantome f = fantomesEnAttente.remove(0);
                    f.setX(positionPorte[0]);
                    f.setY(positionPorte[1]);
                    this.fantomes.add(f);
                    compteurSortieFantome = 0;
                }
            }
        }

        // Gestion des collisions et déplacement des fantômes
        Iterator<Fantome> it = fantomes.iterator();
        while (it.hasNext()) {
            Fantome f = it.next();
            f.choisirDirectionAleatoire();
            f.bougerAlea(terrain);

            // Vérification collision avec Pacman
            if (pacman != null && pacman.getX() == f.getX() && pacman.getY() == f.getY()) {
                if (compteurSuper > 0) {
                    score += 250;
                    hudPanel.updateScore(score);
                    it.remove();
                    preparerFantome();
                } else if (compteurInvincibilite == 0) {
                    perdreVie();
                    compteurInvincibilite = 20;
                    break;
                }
            }

            // Vérification collision avec un autre fantôme
            for (int i = 0; i < fantomes.size(); i++) {
                for (int j = i + 1; j < fantomes.size(); j++) {
                    Fantome f1 = fantomes.get(i);
                    Fantome f2 = fantomes.get(j);

                    // Si deux fantômes sont sur la même case
                    if (f1.getX() == f2.getX() && f1.getY() == f2.getY()) {
                        f1.demiTour();
                        f2.demiTour();

                        // On les fait reculer immédiatement d'une case pour éviter qu'ils restent bloqués l'un dans l'autre
                        f1.setX(f1.getX() + f1.getX());
                        f1.setY(f1.getY() + f1.getY());
                        f2.setX(f2.getX() + f2.getX());
                        f2.setY(f2.getY() + f2.getY());
                    }
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