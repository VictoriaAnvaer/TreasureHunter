import java.awt.*;
import java.util.Scanner;

/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all the display based on the messages it receives from the Town object. <p>
 *
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class TreasureHunter {
    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private boolean easyMode;
    private static boolean samurai;
    private static boolean gameOver;
    private OutputWindow window;

    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter(OutputWindow window) {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
        easyMode = false;
        gameOver = false;
        samurai = false;
        this.window = window;
    }

    /**
     * Starts the game; this is the only public method
     */
    public void play() {
        welcomePlayer();
        enterTown();
        showMenu();
    }
    public boolean getEasyMode() {
        return easyMode;
    }

    public static boolean getSamurai() {return samurai;}

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer() {
        window.addTextToWindow("Welcome to TREASURE HUNTER!", Color.cyan);
        window.addTextToWindow("\nGoing hunting for the big treasure, eh?", Color.black);
        window.addTextToWindow("\nWhat's your name, Hunter? ", Color.black);
        String name = SCANNER.nextLine().toLowerCase();

        // set hunter instance variable
        hunter = new Hunter(name, 10);

        window.addTextToWindow("\nEasy, normal or hard mode? (e/n/h) ",Color.gray);
        String hard = SCANNER.nextLine().toLowerCase();
        if (hard.equals("h")) {
            hardMode = true;
            hunter.setMode("hard");
        } else if (hard.equals("test")) {
            hunter.changeGold(90);
            hunter.addItem("water");
            hunter.addItem("rope");
            hunter.addItem("machete");
            hunter.addItem("horse");
            hunter.addItem("boat");
            hunter.addItem("boots");
            hunter.addItem("shovel");
            hunter.setMode("test");
        } else if (hard.equals("e")) {
            hunter.changeGold(10);
            easyMode = true;
            hunter.setMode("easy");
        } else if (hard.equals("s")) {
            samurai = true;
            hunter.setMode("normal");
        } else {
            hunter.setMode("normal");
        }
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown() {
        double markdown = 0.50;
        double toughness = 0.4;
        if (hardMode) {
            // in hard mode, you get less money back when you sell items
            markdown = 0.25;

            // and the town is "tougher"
            toughness = 0.75;
        }
        if (easyMode) {
            markdown = 1;
            toughness = 0.25;
        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu() {
        String choice = "";

        while (!choice.equals("x") && gameOver == false ) {
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow("\n" + currentTown.getLatestNews(), Color.black);
            window.addTextToWindow("\n***", Color.black);
            window.addTextToWindow("\n" + hunter, Color.pink);
            window.addTextToWindow("\n" + currentTown, Color.magenta);
            window.addTextToWindow("\n(B)uy something at the shop.", Color.blue);
            window.addTextToWindow("\n(S)ell something at the shop.", Color.blue);
            window.addTextToWindow("\n(M)ove on to a different town.", Color.blue);
            window.addTextToWindow("\n(L)ook for trouble!", Color.blue);
            window.addTextToWindow("\n(D)ig for gold!" , Color.blue);
            window.addTextToWindow("\n(H)unt for treasure!", Color.blue);
            window.addTextToWindow("\nGive up the hunt and e(X)it." Color.blue);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow("\nWhat's your next move? ", Color.black);
            choice = SCANNER.nextLine().toLowerCase();
            processChoice(choice);
        }
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice) {
        if (choice.equals("b") || choice.equals("s")) {
            currentTown.enterShop(choice);
        } else if (choice.equals("m")) {
            if (currentTown.leaveTown()) {
                // This town is going away so print its news ahead of time.
                System.out.println(currentTown.getLatestNews());
                enterTown();
            }
        } else if (choice.equals("l")) {
            currentTown.lookForTrouble();
        } else if (choice.equals("x")) {
            System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
        } else if (choice.equals("h")) {
                currentTown.huntForTreasure();
        } else if (choice.equals("d")) {
            currentTown.digForGold();
        } else {
            System.out.println(Colors.RED + "Yikes!" + Colors.RESET + " That's an invalid option! Try again.");
        }
    }

    public static void gameOVER(String message) {
        System.out.println(message);
        System.out.println(Colors.RED + "Game over!" + Colors.RESET + " You lost all your gold");
        gameOver = true;
    }


    public static void win() {
        System.out.println("Congrats! You have found the last of the three treasures," + Colors.WHITE + "you win!");
        gameOver = true;
    }
}

