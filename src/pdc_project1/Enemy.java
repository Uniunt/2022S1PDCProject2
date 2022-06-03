package pdc_project1;

/**
 *
 * @author Uniunt
 */
public class Enemy extends Character {

    //variable to store the players current xp
    int playerXp;

    //enemy specific constructor
    public Enemy(String name, int playerXp) {
        super(name,
                playerXp / 3 + 5,
                (int) (Math.random() * (playerXp / 4 + 2) + 1));
        this.playerXp = playerXp;
    }

    @Override
    public int attack() {
        return xp / 4 + 5;
    }

    @Override
    public int defend() {
        return xp / 4 + 5;
    }

}
