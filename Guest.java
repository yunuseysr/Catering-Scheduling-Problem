/**
 * Lecture - Operating System - Lecturer: Didem ABIDIN
 *
 * @author Yunus Emre YAŞAR - 160315040 - Formal
 * @author Atakan ÖZKUMUR - 180315065 - Formal
 * 
 * 
 * @@code Description 
 * 
 * Our system was created with the multithread method. 
 * There are 10 threads for the guest and 1 thread for the waiter. 
 * There are borek, drink and cake for the guests in our system. 
 * Each guest gets one of the products he wants randomly. 
 * When they want to take the products more than once, it takes into account whether other guests have ever taken them. 
 * In the meantime, the waiter is constantly filling on the tray to please the guests. 
 * The waiter closes the tray to the guests' access to fill the tray when there is an empty tray or at most one piece of material left. 
 * Here, the ReentrantLock method is used. 
 * Guests continue to have access to food and drink until all ingredients are gone. 
 * Each content is accessed by the guest by the method method below. 
 * If the customer does not exceed the quota of the food or drink in the tray he chooses and the tray is not empty and the tray is not locked by the waiter, 
 * access to the tray is provided. We decrease the value of the tray by one and increase the value of the guest in the corresponding tray. 
 * For example, when the guest takes cake, the guest's tray variable is increased by one. In this way, 
 * it is calculated how many guests eat which material from which material. 
 * We added some color to the code to increase the readability and viewability of the code. 
 * We have also assigned some legendary players who have played in our country's teams to the name variable of the guests.
 */

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

class Guest implements Runnable {

    Plate beverage;
    Plate cake;
    Plate borek;
    ArrayList<Guest> guestSource;

    Guest(Plate _beverage, Plate _borek, Plate _cake, ArrayList<Guest> _source) {
        this.beverage = _beverage;
        this.borek = _borek;
        this.cake = _cake;
        this.guestSource = _source;
        
        noTaken.add("beverage");// 0
        noTaken.add("börek"); // 1
        noTaken.add("cake");// 2
        options.add("beverage");
        options.add("börek");
        options.add("cake");
    }
    //We wrote the colorful codes because of readability.
    public static final String RESET = "\033[0m";
    public static final String BLUE = "\u001B[34m";
    public static final String YELLOW = "\033[0;33m";
    public static final String GREEN = "\033[0;32m";
    public static final String BLACK = "\033[0;30m";
    public static final String PURPLE = "\033[0;35m";

    int eatenCake = 0;
    int eatenBorek = 0;
    int drinkBeverage = 0;
    String choosen;
    ArrayList<String> noTaken = new ArrayList<>();
    ArrayList<String> options = new ArrayList<>();

    public synchronized void borekIncrement() { 
        eatenBorek++;
    }

    public synchronized void beverageIncrement() {
        drinkBeverage++;
    }

    public synchronized void cakeIncrement() {
        eatenCake++;
    }

    @Override
    public void run() {
        while (!beverage.isTotalEmpty() || !cake.isTotalEmpty() || !borek.isTotalEmpty() || !beverage.isEmpty() || !cake.isEmpty() || !borek.isEmpty()) {
            Random rand = new Random();
            if (noTaken.size() != 0) {
                int n = rand.nextInt(noTaken.size());
                choosen = noTaken.get(n);
            } else {
                int j = rand.nextInt(3);
                choosen = options.get(j);
            }
            //Any guest controls the everyone takes at least one of piece.
            boolean isValid = true;
            if ((choosen == "börek" && !noTaken.contains("börek"))
                    || (choosen == "cake" && !noTaken.contains("cake"))
                    || (choosen == "beverage" && !noTaken.contains("beverage"))) {
                for (int i = 0; i < this.guestSource.size(); i++) {
                    Guest guest = this.guestSource.get(i);
                    if (guest.noTaken.contains(choosen)) {
                        isValid = false;
                    }
                }
            }
           
            if (isValid) {
                boolean taken = false;
                if (choosen == "beverage" && drinkBeverage < 5 && !beverage.isEmpty() && !beverage.Locked()) {  //Guest wants to take a beverage
                    beverage.SetLock(true);
                    beverage.Minus();
                    beverageIncrement();
                    System.out.println(BLUE + Thread.currentThread().getName()
                            + " \twent to take a glass of beverage. \tTotal of beverage " + drinkBeverage + " drank." + "\t Remaining beverages : "
                            + beverage.Length + RESET);
                    taken = true;
                    beverage.SetLock(false);
                }

                if (choosen == "börek" && eatenBorek < 5 && !borek.isEmpty() && !borek.Locked()) { //Guest wants to take a borek
                    borek.SetLock(true);
                    borek.Minus();
                    borekIncrement();
                    System.out.println(BLUE + Thread.currentThread().getName()
                            + " \twent to take a slice of borek. \t\tTotal of borek " + eatenBorek + " ate." + "\t\t Remaining boreks : "
                            + borek.Length + RESET);
                    taken = true;
                    borek.SetLock(false);
                }

                if (choosen == "cake" && eatenCake < 2 && !cake.isEmpty() && !cake.Locked()) { //Guest wants to take a kek
                    cake.SetLock(true);
                    cake.Minus();
                    cakeIncrement();
                    System.out.println(BLUE + Thread.currentThread().getName()
                            + " \twent to take a slice of cake. \t\tTotal " + eatenCake + " ate." + "\t\t\t Remaining cakes : "
                            + cake.Length + RESET);
                    taken = true;
                    cake.SetLock(false);
                }

                if (taken) {
                    noTaken.remove(choosen);
                }
            }

            int sleepTime = rand.nextInt(1000) * 3 + 1000; // Random Sleep value for Threads 
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
               
                e.printStackTrace();
            }
        }
        eaten();
    }
    //print of output
    public void eaten() {
        while (true) {
            System.out.println("");
            if (beverage.isTotalEmpty() && cake.isTotalEmpty() && borek.isTotalEmpty() && beverage.isEmpty() && cake.isEmpty() && borek.isEmpty()) {
                System.out.println(PURPLE + "Thanks " +  Thread.currentThread().getName() +" for joining party. " + "\t\t Börek : " + eatenBorek + "\t\tBeverage : " + drinkBeverage
                        + "\t\tCake : " + eatenCake + RESET);
                
                break;
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }
}
