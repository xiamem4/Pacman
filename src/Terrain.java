import java.util.ArrayList;
import java.util.Iterator;

public class Terrain {

    private char[][] grille;
    private ArrayList<PacGomme> pacgommes;

    public Terrain() {
        pacgommes = new ArrayList<>();

        String[] laby = {
                "MMMMMMMMMM",
                "M........M",
                "M.MMMMMM.M",
                "M........M",
                "M.MMM.MM.M",
                "M.MMM.MM.M",
                "M....P...M",
                "M.MMM.MM.M",
                "M........M",
                "MMMMMMMMMM"
        };

        grille = new char[laby.length][laby[0].length()];

        for (int ligne = 0; ligne < laby.length; ligne++) {
            grille[ligne] = laby[ligne].toCharArray();
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

    public void mangerGomme(int x, int y) {
        // On utilise un itérateur pour supprimer proprement l'élément de la liste
        Iterator<PacGomme> it = pacgommes.iterator();
        while (it.hasNext()) {
            PacGomme g = it.next();
            if (g.getX() == x && g.getY() == y) {
                it.remove(); // On supprime la gomme de la liste
                grille[y][x] = ' '; // On met à jour la grille aussi (optionnel mais conseillé)
                break; // On a trouvé, on arrête de chercher
            }
        }
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
