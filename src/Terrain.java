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

    /**
     * Charge le niveau depuis un fichier texte
     * 
     * @param numeroNiveau : Le numéro du niveau (ex: 1 pour niveau1.txt)
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

        // Conversion de la liste de lignes en tableau 2D (grille)
        int nbLignes = lignes.size();
        int nbColonnes = lignes.get(0).length(); // On suppose que la 1ère ligne donne la largeur

        grille = new char[nbLignes][nbColonnes];
        pacgommes.clear();
        for (int y = 0; y < nbLignes; y++) {
            String ligneActuelle = lignes.get(y);
            // Sécurité : on prend le minimum entre la taille de la ligne et la largeur
            // prévue
            int largeurLigne = Math.min(ligneActuelle.length(), nbColonnes);

            for (int x = 0; x < largeurLigne; x++) {
                char c = ligneActuelle.charAt(x);
                grille[y][x] = c;

                // Création des gommes
                if (c == '.') {
                    pacgommes.add(new PacGomme(x, y));
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

    public boolean mangerGomme(int x, int y) {
        // On utilise un itérateur pour supprimer proprement l'élément de la liste
        Iterator<PacGomme> it = pacgommes.iterator();
        while (it.hasNext()) {
            PacGomme g = it.next();
            if (g.getX() == x && g.getY() == y) {
                it.remove(); // On supprime la gomme de la liste
                grille[y][x] = ' '; // On met à jour la grille aussi (optionnel mais conseillé)
                return true; // On a trouvé, on arrête de chercher
            }
        }
        return false;
    }

    // Vérifier si il y a un mur
    public boolean estMur(int x, int y) {
        if (x < 0 || x >= getNbColonnes() || y < 0 || y >= getNbLignes()) {
            return true;
        }
        return grille[y][x] == 'M' || grille[y][x] == 'V';
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
