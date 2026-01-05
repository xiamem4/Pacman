import java.awt.*;
import java.util.*;

public class Fantome extends EntiteMouvante{
    private Random rand = new Random();

    public Fantome( int x, int y) {
        super(x, y);
    }

    public void choisirDirectionAleatoire() {
        // Choisit une direction entre 0 et 3
        int direction = rand.nextInt(4);
        this.setDirection(direction);
    }

    public void bougerAlea(Terrain terrain) {
        
        ArrayList<Integer> directionsPossibles = new ArrayList<>();
        
        if (!terrain.estMur(x, y - 1)) directionsPossibles.add(0);
        if (!terrain.estMur(x, y + 1)) directionsPossibles.add(1);
        if (!terrain.estMur(x - 1, y)) directionsPossibles.add(2);
        if (!terrain.estMur(x + 1, y)) directionsPossibles.add(3);

        boolean estBloqueDevant = terrain.estMur(x + dx, y + dy);
        boolean estIntersection = directionsPossibles.size() > 2;

        if (estBloqueDevant || estIntersection) {
            int index = rand.nextInt(directionsPossibles.size());
            int nouvelleDir = directionsPossibles.get(index);
            setDirection(nouvelleDir);
            if (estBloqueDevant)
            System.out.println("Changement de direction");
        }
        
        super.bouger(terrain); 
    }

    // Dessiner le Fantome
    public void dessiner(Graphics g, int tailleCase) {
        g.setColor(Color.GREEN);
        g.fillOval(x * tailleCase, y * tailleCase, tailleCase, tailleCase);
    }
    
}
