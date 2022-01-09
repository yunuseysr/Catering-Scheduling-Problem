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

public class CateringSchedulingProblem {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Welcome to our crazy party. Have fun.");
        Plate borek = new Plate(0, 30); 
        Plate beverage = new Plate(0, 30);
        Plate cake = new Plate(0, 15);

        ArrayList<Guest> guestSource = new ArrayList<Guest>(); //We took values of the all of pieces for everyone takes at least one.
        //Created the 10 Threads
        String[] names = new String[]{"George HAGI", "Sneijder", "Metin OKTAY", "Mario JARDEL", "Legend BOBO", "Quaresma", "Pascal NOUMA", "Sergen Y.", "M. Gomez", "Hernandez"};
        for (int i = 0; i < 10; i++) {
            Guest guest = new Guest(beverage, borek, cake, guestSource);
            Thread thread = new Thread(guest);
            thread.setName(names[i]);
            thread.start();
            guestSource.add(guest);
        }
        //Created Waiter Thread 
        Thread waitress = new Thread(new Waitress(beverage, borek, cake));
        waitress.setPriority(Thread.MAX_PRIORITY); //On tray, There is no pieces or has one pieces, for Waiter was created to fill the tray.
        waitress.setName("(GARSON) Taffarel");
        waitress.start();
        
        System.out.println("");
        
        
        
    }

}
