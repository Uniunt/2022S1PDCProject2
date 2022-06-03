package pdc_project1;

/**
 *
 * @author Uniunt
 */
public class Story {
    
    public static void printIntro() {
        GameLogic.clearConsole();
        GameLogic.printHeading("PROLOGUE");
        System.out.println("This is your very firt step...");
        GameLogic.anythingToContinue();  
    }   
    
    public static void printFirstAct(){
        GameLogic.clearConsole();
        GameLogic.printHeading("Chapter 1");
        System.out.println("All you see here is a great castle, Stromveil!");
        GameLogic.anythingToContinue();  
    }
    
    public static void printSecondAct(){
        GameLogic.clearConsole();
        GameLogic.printHeading("Chapter 2");
        System.out.println("Ahead of lake, is the mysterious Academy Raya Lucaria...");
        GameLogic.anythingToContinue();  
    }
    
    public static void printThirdAct(){
        GameLogic.clearConsole();
        GameLogic.printHeading("Chapter 3");
        System.out.println("Crimison rot is crawling in this land.");
        GameLogic.anythingToContinue();  
    }
    
    public static void printFourthAct(){
        GameLogic.clearConsole();
        GameLogic.printHeading("Chapter 4");
        System.out.println("Feast your eyes, the great golden tree!");
        GameLogic.anythingToContinue();  
    }
    
    public static void printEnd(){
        GameLogic.clearConsole();
        GameLogic.printHeading("The Final");
        System.out.println("This is your last challenge... have you satisfied?");
        GameLogic.anythingToContinue();  
    }
}
