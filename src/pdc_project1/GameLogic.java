package pdc_project1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author Uniunt
 */
//Not creating any objects here so all static...
public class GameLogic {

    private static Scanner sc = new Scanner(System.in);
    private static Player player;
    public static boolean isRunning;

    //Stroy elements
    public static int place = 0;
    public static int act = 1;
    public static String[] places = {"Limgrave", "Liurnia of the Lakes", "Caelid", "Altus Plateau"};

    //random encounters
    public static String[] encounters = {"Battle", "Battle", "Battle", "Rest", "Rest"};

    //enemies
    public static String[] enemies = {"Banished Knight", "Albinauric", "Archer of Ordina", "Fire Monk", "Omen"};

    //IO
    private static final HashMap<String, Player> players = new HashMap();
    private static final String FILENAME = "./resources/players.txt";

    public static int readInt(String prompt, int userChoices) {
        int input;

        do {
            System.out.println(prompt);
            try {
                input = Integer.parseInt(sc.next());
            } catch (Exception e) {
                input = -1;
                System.out.println("Please enter an integer!");
            }
        } while (input < 1 || input > userChoices);

        return input;
    }

    //method to simulate clearing out the console
    public static void clearConsole() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }

    //method to print a seperator with length n
    public static void printSeperator(int n) {
        for (int i = 0; i < n; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    //method to print a heading
    public static void printHeading(String title) {
        printSeperator(30);
        System.out.println(title);
        printSeperator(30);
    }

    //method to stop the game until user enters anything
    public static void anythingToContinue() {
        System.out.println("\nEnter anything to continue...");
        sc.next();
    }

    //method to start the game
    public static void startGame() {
        boolean nameSet = false;
        String name;
        //print title screen
        clearConsole();
        printSeperator(40);
        printSeperator(30);
        System.out.println(" - Elder Ring -");
        System.out.println("A TEXT RBG");
        printSeperator(30);
        printSeperator(40);
        anythingToContinue();

        //getting the player name
        do {
            clearConsole();
            printHeading("What's your name?");
            name = sc.next();
            
            //Connect to a text file
            getPlayers(FILENAME);
            Player p = checkPlayers(name);
            updateName(p);
            
            //asking the player if he wants to correct his choice
            clearConsole();
            printHeading("Your name is " + name + "."
                    + "\nIs thst correct?");
            System.out.println("(1) Yes!");
            System.out.println("(2) No, I want to change my name.");
            int input = readInt("->", 2);

            if (input == 1) {
                nameSet = true;
            }
        } while (!nameSet);

        //print story lines
        Story.printIntro();

        //create new player object with the name
        player = new Player(name);

        Story.printIntro();

        isRunning = true;

        gameLoop();
    }

    //main game loop
    public static void gameLoop() {
        while (isRunning) {
            printMenu();
            int input = readInt("->", 3);
            if (input == 1) {
                continueJourney();
            } else if (input == 2) {
                characterInfo();
            } else {
                isRunning = false;
            }
        }
    }

    //method that changes the game's value based on player xp
    public static void checkAct() {
        //change acts based on xp
        if (player.xp >= 10 && act == 1) {
            act = 2;
            place = 1;
            //storyline
            Story.printFirstAct();
            //level up
            player.chooseTrait();
            //storyline
            Story.printSecondAct();
            //assign enemies "Banished Knight", "Albinauric", "Archer of Ordina", "Fire Monk", "Omen"
            enemies[0] = "Banished Knight";
            enemies[1] = "Albinauric";
            enemies[2] = "Archer of Ordina";
            enemies[3] = "Fire Monk";
            enemies[4] = "Omen";
            //assign encounters
            encounters[0] = "Battle";
            encounters[1] = "Battle";
            encounters[2] = "Battle";
            encounters[3] = "Rest";
            encounters[4] = "Shop";
        } else if (player.xp >= 50 && act == 2) {
            act = 3;
            place = 2;
            //storyline
            Story.printSecondAct();
            //level up
            player.chooseTrait();
            //storyline
            Story.printThirdAct();
            //assign enemies "Banished Knight", "Albinauric", "Archer of Ordina", "Fire Monk", "Omen"
            enemies[0] = "Banished Knight";
            enemies[1] = "Banished Knight";
            enemies[2] = "Omen";
            enemies[3] = "Omen";
            enemies[4] = "Archer of Ordina";
            //assign encounters
            encounters[0] = "Battle";
            encounters[1] = "Battle";
            encounters[2] = "Battle";
            encounters[3] = "Battle";
            encounters[4] = "Shop";
            //fully heal the player
            player.hp = player.maxHp;
        } else if (player.xp >= 100 && act == 3) {
            act = 4;
            place = 3;
            //storyline
            Story.printThirdAct();
            //level up
            player.chooseTrait();
            //storyline
            Story.printFourthAct();
            //calling the final battle
            finalBattle();
        }
    }

    //method to continue the journey
    public static void continueJourney() {
        checkAct();
        //check if game isn't in last act
        if (act != 4) {
            randomEncounter();
        }
    }

    //method to calculate a random encounter
    public static void randomEncounter() {
        //random number between 0 and the length of the encounyers array
        int encounter = (int) (Math.random() * encounters.length);
        //calling the respective methods
        if (encounters[encounter].equals("Battle")) {
            randomBattle();
        } else if (encounters[encounter].equals("Rest")) {
            takeRest();
        } else {
            shop();
        }
    }

    //creating a random battle
    public static void randomBattle() {
        int monster = (int) (Math.random() * enemies.length);
        clearConsole();
        printHeading("Behold, a " + enemies[monster] + " !");
        anythingToContinue();
        //creating random enemy
        battle(new Enemy(enemies[monster], player.xp));
    }

    //the main Battle method
    public static void battle(Enemy enemy) {
        //main battle loop
        while (true) {
            clearConsole();
            printHeading(enemy.name
                    + "\nHP: " + enemy.hp + "/" + enemy.maxHp);
            printHeading(player.name
                    + "\nHP: " + player.hp + "/" + player.maxHp);
            System.out.println("Choose an action:");
            printSeperator(20);

            System.out.println("(1) Fight");
            System.out.println("(2) Use Flask of Crimson Tears (" + player.pots + " left)");
            System.out.println("(3) Run Away");
            int input = readInt("->", 3);

            if (input == 1) {
                //FIGHT
                //calcuate damage and dmgTook
                int dmg = player.attack() - enemy.defend();
                int dmgTook = enemy.attack() - player.defend();
                //check negative
                if (dmgTook < 0) {
                    //add some dmg if player defends very well
                    dmg -= dmgTook / 2;
                    dmgTook = 0;
                }

                if (dmg < 0) {
                    dmg = 0;
                }
                //deal damage to both parties
                player.hp -= dmgTook;
                enemy.hp -= dmg;
                //print result of this round
                clearConsole();
                printHeading("BATTLE");
                System.out.println("Your dealt " + dmg + " damage to the " + enemy.name + ".");
                printSeperator(15);
                System.out.println("The " + enemy.name + " dealt " + dmgTook + " damage to you.");
                anythingToContinue();
                //check alive or not
                if (player.hp <= 0) {
                    playerDied();
                    break;
                } else if (enemy.hp <= 0) {
                    clearConsole();
                    printHeading("Enemy Felled: " + enemy.name);
                    player.xp += enemy.xp;
                    System.out.println("Rune grabbed: " + enemy.xp);
                    anythingToContinue();
                    //random drops
                    boolean addRest = (Math.random() * 5 + 1 <= 2.25);
                    int goldEarned = (int) (Math.random() * enemy.xp);

                    if (addRest) {
                        player.restsLeft++;
                        System.out.println("You earned a bonus rest!");
                    }

                    if (goldEarned > 0) {
                        player.gold += goldEarned;
                        System.out.println("You collect " + goldEarned + " gold from the " + enemy.name + "'s corpse!");
                    }
                    anythingToContinue();
                    break;
                }
            } else if (input == 2) {
                //USE POTION
                clearConsole();
                if (player.pots > 0 && player.hp < player.maxHp) {
                    printHeading("Do you want to drink a Flask of Crimson Tears? (" + player.pots + " left).");
                    System.out.println("(1) Yes");
                    System.out.println("(2) No, maybe later");
                    input = readInt("->", 2);

                    if (input == 1) {
                        player.hp = player.maxHp;
                        clearConsole();
                        printHeading("You drank a Flask of Crimson Tears. It has fully restored your health.");
                        anythingToContinue();
                    }
                } else {
                    System.out.println("So bad. You don't have any potions left!");
                }
            } else {
                //RUN AWAY
                clearConsole();
                //check if in final battle
                if (act != 4) {
                    //chance of 35%
                    if (Math.random() * 10 + 1 <= 3.5) {
                        printHeading("You ran away from the " + enemy.name + "!");
                        anythingToContinue();
                        break;
                    } else {
                        printHeading("It was a mistake, you have been noticed!");
                        int dmgTook = enemy.attack();
                        System.out.println("Somehow you took " + dmgTook + " damage!");
                        player.hp -= dmgTook;
                        anythingToContinue();

                        if (player.hp <= 0) {
                            playerDied();
                        }
                    }
                } else {
                    printHeading("THERE IS NOOOOO ESCAPE!!!");
                    anythingToContinue();
                }
            }
        }
    }

    //method if player is dead
    public static void playerDied() {
        clearConsole();
        printHeading("You Died");
        printHeading(player.xp + " rune you earned in total!");
        System.out.println("Arise, tarnished! To be the Elden Lord!");
        isRunning = false;
    }

    //the final boss fight
    public static void finalBattle() {
        battle(new Enemy("Radagon of the Golden Order", 550));
        Story.printEnd();
        isRunning = false;
    }

    //taking a rest
    public static void takeRest() {
        clearConsole();
        if (player.restsLeft >= 1) {
            printHeading("Do you want to take a rest (HP: " + player.hp + "/" + player.maxHp + ")? (" + player.restsLeft + " rest(s) left.)");
            System.out.println("(1) Yes");
            System.out.println("(2) Evil never rest, so do I");
            int input = readInt("->", 2);

            if (input == 1) {
                clearConsole();
                printHeading("Bonfire Lit");
                if (player.hp < player.maxHp) {
                    int hpRestored = (int) (Math.random() * (player.xp / 4 + 1) + 10);
                    player.hp += hpRestored;

                    if (player.hp > player.maxHp) {
                        player.hp = player.maxHp;
                    }

                    System.out.println("You have restored up to " + hpRestored + " health.");
                    System.out.println("Now you have " + player.hp + "/" + player.maxHp + " health.");
                    player.restsLeft--;
                }
            } else {
                System.out.println("Up to go!");
            }
            anythingToContinue();
        }
    }

    //shopping 
    public static void shop() {
        clearConsole();
        printHeading("You meet a merchant, Kale."
                + "\nHe offers you something:");
        int price = (int) (Math.random() * (10 + player.pots * 3) + 10 + player.pots);
        System.out.println("- Flask of Crimson Tears: " + price + " gold.");
        printSeperator(20);
        //ask the player to buy one
        System.out.println("Are you interested, tarnished?");
        System.out.println("(1) Yes.");
        System.out.println("(2) Next time.");
        int input = readInt("->", 2);

        if (input == 1) {
            clearConsole();
            //check if enough gold
            if (player.gold >= price) {
                printHeading("You bought a Flask of Crimson Tears for " + price + " gold.");
                player.pots++;
                player.gold -= price;
            } else {
                printHeading("No enough CASH!");
            }
            anythingToContinue();
        }
    }

    //printing out information about the character
    public static void characterInfo() {
        clearConsole();
        printHeading("STaTUS");
        System.out.println(player.name + "\tHP: " + player.hp + "/" + player.maxHp);
        printSeperator(20);
        //player xp and gold
        System.out.println("Rune: " + player.xp + "\tGold: " + player.gold);
        printSeperator(20);
        //# of pots
        System.out.println("Number of Flask of Crimson Tears: " + player.pots);
        printSeperator(20);

        if (player.numAtkUpgrades > 0) {
            System.out.println("Violent ability: " + player.atkUpgrades[player.numAtkUpgrades - 1]);
        }

        if (player.numDefUpgrades > 0) {
            System.out.println("Harmony ability: " + player.defUpgrades[player.numDefUpgrades - 1]);
        }

        anythingToContinue();
    }

    //printing the main menu
    public static void printMenu() {
        clearConsole();
        printHeading(places[place]);
        System.out.println("Choose your act:");
        printSeperator(20);
        System.out.println("(1) Continue this Journey, to be Elden Lord");
        System.out.println("(2) Check Character");
        System.out.println("(3) Exit Game");
    }

    //IO System
    public static void getPlayers(String fn) {
        FileInputStream fin;

        try {
            fin = new FileInputStream(fn);
            Scanner fileScanner = new Scanner(fin);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                StringTokenizer st = new StringTokenizer(line);
                Player p = new Player(st.nextToken());
                players.put(p.name, p);
            }

            fin.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static Player checkPlayers(String name) {
        Player p;

        if (players.containsKey(name)) {
            p = players.get(name);
        } else {
            p = new Player(name);
            players.put(name, p);
        }

        return p;
    }

    public static void updateName(Player player) {
        players.put(player.name, player);
        try {
            FileOutputStream fOut = new FileOutputStream(FILENAME);
            PrintWriter pw = new PrintWriter(fOut);

            for (Player p : players.values()) {
                pw.println(p.name + " " + p.xp);
            }

            pw.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
