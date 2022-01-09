/**
 * Lecture - Operating System - Lecturer: Didem ABIDIN
 *
 * @author Yunus Emre YAŞAR - 160315040 - Formal
 * @author Atakan ÖZKUMUR - 180315065 - Formal
 * @@code Description
 * <p>
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

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

class Waitress implements Runnable {

    Plate beverage;
    Plate cake;
    Plate borek;

    Waitress(Plate _beverage, Plate _borek, Plate _cake) {
        this.beverage = _beverage;
        this.borek = _borek;
        this.cake = _cake;
    }

    @Override
    public void run() {
        boolean init = false;
        while (!beverage.isTotalEmpty() || !cake.isTotalEmpty() || !borek.isTotalEmpty() || !beverage.isEmpty() || !cake.isEmpty() || !borek.isEmpty()) {
            if (!init && borek.isEmpty() && beverage.isEmpty() && cake.isEmpty()) { //First of all, Waiter fill the standart values to the tray. Every tray will be 5 pieces.
                beverage.Fill(5);
                System.out.println(Guest.GREEN + "(Garson) TAFFAREL was placed 5 glass of beverage on the tray." + Guest.RESET);

                cake.Fill(5);
                System.out.println(Guest.GREEN + "(Garson) TAFFAREL was placed 5 slice of cakes on the tray." + Guest.RESET);

                borek.Fill(5);
                System.out.println(Guest.GREEN + "(Garson) TAFFAREL was placed 5 slice of böreks on the tray." + Guest.RESET);

                System.out.println("");
            }
            System.out.println("");
            if (beverage.Length <= 1 && !beverage.isTotalEmpty()) {
                beverage.SetLock(true);

                if (beverage.Total >= 5) {

                    beverage.Fill(5 - beverage.Length);
                } else {
                    beverage.Fill(beverage.Total);
                }
                System.out.println("");
                System.out.println(Guest.GREEN + Thread.currentThread().getName() + " was placed the glasses, remaining beverage = \ttotal : " + beverage.Total + " and  \ton tray : " + beverage.Length + Guest.RESET);
                System.out.println("");
                beverage.SetLock(false);
            } else if (borek.Length <= 1 && !borek.isTotalEmpty()) {
                borek.SetLock(true);

                if (borek.Total >= 5) {

                    borek.Fill(5 - borek.Length);
                } else {
                    borek.Fill(borek.Total);
                }
                System.out.println("");
                System.out.println(Guest.GREEN + Thread.currentThread().getName() + " was placed the boreks, remaining börek = \ttotal : " + borek.Total + " and  \ton tray : " + borek.Length + Guest.RESET);
                System.out.println("");
                borek.SetLock(false);
            } else if (cake.Length <= 1 && !cake.isTotalEmpty()) {
                cake.SetLock(true);

                if (cake.Total >= 5) {

                    cake.Fill(5 - cake.Length);
                } else {
                    cake.Fill(cake.Total);
                }
                System.out.println("");
                System.out.println(Guest.GREEN + Thread.currentThread().getName() + " was placed the cakes, remaining cakes = \ttotal : " + cake.Total + " and  \ton tray : " + cake.Length + Guest.RESET);
                System.out.println("");
                cake.SetLock(false);
            }
            init = true;

            Random rand = new Random();
            int sleepTime = rand.nextInt(1000) * 3 + 1000;
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }
}
