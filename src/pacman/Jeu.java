package pacman;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Jeu extends JPanel {

    private Terrain terrain;
    private int niveau;
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

    // Gestion des fruits / clés

    private int timerFruit = 0; // Compteur de ticks total
    private boolean fruitApparu = false;
    private int totalGommesInitial;
    private int[] positionFruit;

    private final Map<Integer, Integer> pointsFruits = Map.ofEntries(
            Map.entry(1, 100), // Cerise
            Map.entry(2, 300), // Fraise
            Map.entry(3, 500), Map.entry(4, 500), // Orange
            Map.entry(5, 700), Map.entry(6, 700), // Pomme
            Map.entry(7, 1000), Map.entry(8, 1000), // Melon
            Map.entry(9, 2000), Map.entry(10, 2000), // Banane
            Map.entry(11, 3000), Map.entry(12, 3000), // Cloche
            Map.entry(13, 5000) // Clé (13+)
    );

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

        this.niveau = niveau;

        int[] pos = terrain.getPositionPacman();
        departX = pos[0];
        departY = pos[1];

        pacman = new Pacman(departX, departY);
        this.positionPorte = trouverPorte();

        this.totalGommesInitial = terrain.getGommes().size();

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
                int newDirection = -1;

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> newDirection = 0;
                    case KeyEvent.VK_DOWN -> newDirection = 1;
                    case KeyEvent.VK_LEFT -> newDirection = 2;
                    case KeyEvent.VK_RIGHT -> newDirection = 3;
                }

                if (newDirection != -1 && pacman.peutTourner(terrain, newDirection)) {
                    pacman.setDirection(newDirection);
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

        timerFruit++;

        // Gestion du temps d'invincibilité
        if (compteurInvincibilite > 0) {
            compteurInvincibilite--;
        }

        // Gestion du temps de Super Mode
        if (compteurSuper > 0) {
            compteurSuper--;
        }

        // Ajoute d'une vie si 10000
        if (score >= 10000) {
            vies += 1;
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
                if (this.fantomes.isEmpty() || compteurSortieFantome >= 30) {
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

            // Position futur du fantome
            int futurFX = f.getX() + f.dx;
            int futurFY = f.getY() + f.dy;

            // Position futur du Pacman
            int futurPX = pacman.getX() + pacman.dx;
            int futurPY = pacman.getY() + pacman.dy;

            f.bougerAlea(terrain);

            // Collision "swap"
            if (pacman != null && pacman.getX() == futurFX && pacman.getY() == futurFY && futurPX == f.getX() && futurPY == f.getY()) {
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

            // Collision classiques
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
        }

        // Apparition du fruit
        if (!fruitApparu && timerFruit >= 150) {
            int gommesRestantes = terrain.getGommes().size();
            double pourcentageMange = 1.0 - ((double) gommesRestantes / totalGommesInitial);

            if (pourcentageMange >= 0.40) {
                genererFruit();
            }
        }

        // Collision Pacman et fruit
        if (fruitApparu && pacman.getX() == positionFruit[0] && pacman.getY() == positionFruit[1]) {
            score += pointsFruits.getOrDefault(this.niveau, 5000);
            hudPanel.updateScore(score);
            fruitApparu = false;
            terrain.getGrille()[positionFruit[1]][positionFruit[0]] = ' ';
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

    // Faire apparaître le fruit
    private void genererFruit() {
        char[][] grille = terrain.getGrille();
        List<int[]> casesVides = new ArrayList<>();

        for (int y = 0; y < grille.length; y++) {
            for (int x = 0; x < grille[0].length; x++) {
                if (grille[y][x] == ' ' && (x != pacman.getX()) && (y != pacman.getY())) {
                    casesVides.add(new int[] { x, y });
                }
            }
        }

        if (!casesVides.isEmpty()) {
            int[] choisi = casesVides.get(new Random().nextInt(casesVides.size()));
            this.positionFruit = choisi;
            grille[choisi[1]][choisi[0]] = 'B';
            fruitApparu = true;
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