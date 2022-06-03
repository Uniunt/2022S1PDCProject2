package pdc_project1;

/**
 *
 * @author Uniunt
 */
public class Player extends Character {

    //integers to store number of upgrades/skills in each path
    public int numAtkUpgrades, numDefUpgrades;

    //additional player stats
    int gold, restsLeft, pots;
    
    //Arrays to store skill names
    public String[] atkUpgrades = {"Lion's Claw", "Blood Tax", "Giant Hunt", "Carian Grandeur"};
    public String[] defUpgrades = {"Parry", "Carian Retaliation", "Storm Wall", "Thops's Barrier"};

    //Player specifc constructor
    public Player(String name) {
        super(name, 100, 0);
        this.numAtkUpgrades = 0;
        this.numDefUpgrades = 0;
        //set additional stats
        this.gold = 5;
        this.restsLeft = 1;
        this.pots = 0;
        
        chooseTrait();
    }

    //Player specific methods
    @Override
    public int attack() {
        return xp/10 + numAtkUpgrades * 2 + numDefUpgrades + 1;
    }

    @Override
    public int defend() {
        return xp/10 + numDefUpgrades * 2 + numAtkUpgrades + 1;
    }

    //let the player choose a trait when creating a new character
    public void chooseTrait() {
        GameLogic.clearConsole();
        GameLogic.printHeading("Choose your abilityt: ");
        System.out.println("(1) " + atkUpgrades[numAtkUpgrades]);
        System.out.println("(2) " + defUpgrades[numDefUpgrades]);

        int input = GameLogic.readInt("-> ", 2);
        GameLogic.clearConsole();

        if (input == 1) {
            GameLogic.printHeading("You chose " + atkUpgrades[numAtkUpgrades] + "!");
            this.numAtkUpgrades++;
        } else {
            GameLogic.printHeading("You chose " + defUpgrades[numDefUpgrades] + "!");
            this.numDefUpgrades++;
        }
        GameLogic.anythingToContinue();
    }
}
