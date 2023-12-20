/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean hunt;
    private boolean toughTown;
    private boolean dug;
    private static boolean crown;
    private static boolean trophy;
    private static boolean gem;
    private static boolean dust;



    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness) {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
        dug = false;
    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (hunter.getMode().equals("easy") && (checkItemBreak())) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your " + item + ".";
            }
            dug = false;
            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        shop.enter(hunter, choice);
        printMessage = "You left the shop.";
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            printMessage = Colors.RED + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (TreasureHunter.getSamurai() == true && hunter.hasItemInKit("sword")) {
                printMessage += "You have a mighty sword there. Here, take my gold" + Colors.RESET;
                printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                hunter.changeGold(goldDiff);
            } else if (Math.random() > noTroubleChance) {
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold." + Colors.RESET;
                printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                hunter.changeGold(goldDiff);
            } else {
                printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!" + Colors.RESET;
                printMessage += "\nYou lost the brawl and pay " + Colors.RED + goldDiff + Colors.RESET + " gold.";
                hunter.changeGold(-goldDiff);
                if (hunter.getGold() < 0) {
                    TreasureHunter.gameOVER(printMessage);
                }
            }
        }
    }
    public void huntForTreasure() {
        int rand = (int) (Math.random()*4) + 1;
        if (rand == 1) {
            if (crown == false) {
                crown = true;
                hunter.addTreasure("crown");
                System.out.println("you found a crown!");
            } else {
                System.out.println("you already have a crown");
            }
        } else if (rand == 2) {
            if (trophy == false) {
                trophy = true;
                hunter.addTreasure("trophy");
                System.out.println("you found a trophy!");
            } else {
                System.out.println("you already have a trophy");
            }
        } else if (rand == 3 ) {
            if (gem == false) {
                gem = true;
                hunter.addTreasure("gem");
                System.out.println("you found a gem!");
            } else {
                System.out.println("you already have a gem");
            }
        } else {
            System.out.println("you found dust!");
        }
        hunt = true;
        if (gem == true && crown == true && trophy == true ) {
            TreasureHunter.win();
        }
    }

    public boolean hunted() {
        return hunt;
    }

    public void digForGold() {
        if (hunter.hasItemInKit("shovel")) {
            if (dug) {
                printMessage = "You already dug for gold in this town.";
            } else {
                if ((int) (Math.random() * 2 + 1) == 1) {
                    int gold = (int) (Math.random() * 20 + 1);
                    hunter.changeGold(gold);
                    printMessage = "You dug up " + gold + " gold!";
                } else {
                    printMessage = "You dug but only found dirt.";
                }
                dug = true;
            }
        } else {
            printMessage = "You can't dig for gold without a shovel.";
        }
    }

    public String toString() {
        return "This nice little town is surrounded by " + Colors.CYAN + terrain.getTerrainName() + Colors.RESET + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = (int) (Math.random() * 6 + 1);
        if (rnd == 1) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd == 2) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd == 3) {
            return new Terrain("Plains", "Horse");
        } else if (rnd == 4) {
            return new Terrain("Desert", "Water");
        } else if (rnd == 5) {
            return new Terrain("Jungle", "Machete");
        } else {
            return new Terrain("Marsh", "Boots");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }
}