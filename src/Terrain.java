import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Terrain {

    private char[][] grille;
    private ArrayList<PacGomme> pacgommes;

    public Terrain() {
        pacgommes = new ArrayList<>();
    }

    /** Charge le niveau depuis un fichier texte
     * @param numeroNiveau : numéro du niveau
     */

    public void chargerNiveau(int numeroNiveau) {
        String nomFichier = "niveaux/niveau" + numeroNiveau + ".txt";
        List<String> lignes = new ArrayList<>();

        // Lecture du fichier
        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                lignes.add(ligne);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du niveau " + numeroNiveau);
            e.printStackTrace();
            // Chargement d'un niveau de secours en cas d'erreur pour éviter le crash
            lignes.add("MMMMM");
            lignes.add("M.P.M");
            lignes.add("MMMMM");
        }

        int nbLignes = lignes.size();
        int nbColonnes = lignes.get(0).length();

        grille = new char[nbLignes][nbColonnes];
        pacgommes.clear();
        for (int y = 0; y < nbLignes; y++) {
            String ligneActuelle = lignes.get(y);
            
            int largeurLigne = Math.min(ligneActuelle.length(), nbColonnes);

            for (int x = 0; x < largeurLigne; x++) {
                char c = ligneActuelle.charAt(x);
                grille[y][x] = c;

                if (c == '.') {
                    pacgommes.add(new PacGomme(x, y));
                } else if(c == 'S'){
                    pacgommes.add(new SuperPacGomme(x, y));
                }
            }
        }
    }

    // Getter liste PacGommes
    public ArrayList<PacGomme> getGommes() {
        return pacgommes;
    }

    // Définir la grille
    public char[][] getGrille() {
        return grille;
    }

    // Récupérer le nombre de lignes de la grille
    public int getNbLignes() {
        return grille.length;
    }

    // Récupérer le nombre de colonnes de la grille
    public int getNbColonnes() {
        return grille[0].length;
    }

    // Gestion des PacGommes / SuperPacGommes
    public PacGomme mangerGomme(int x, int y) {
        Iterator<PacGomme> it = pacgommes.iterator();
        while (it.hasNext()) {
            PacGomme g = it.next();
            if (g.getX() == x && g.getY() == y) {
                it.remove();
                grille[y][x] = ' ';
                return g;
            }
        }
        return null;
    }

    // Vérifier si il y a un mur
    public boolean estMur(int x, int y) {
        if (x < 0 || x >= getNbColonnes() || y < 0 || y >= getNbLignes()) {
            return true;
        }
        return grille[y][x] == 'M';
    }

    // Mettre à jour l'emplacement du Pacman
    public void deplacerPacman(int ancienX, int ancienY, int nouvX, int nouvY) {
        grille[ancienY][ancienX] = ' ';
        grille[nouvY][nouvX] = 'P';
    }

    // Récupérer la position de départ
    public int[] getPositionPacman() {
        for (int y = 0; y < grille.length; y++) {
            for (int x = 0; x < grille[0].length; x++) {
                if (grille[y][x] == 'P') {
                    return new int[] { x, y };
                }
            }
        }
        return new int[] { 1, 1 };
    }

}
