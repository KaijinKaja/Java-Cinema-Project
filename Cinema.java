import java.util.Scanner;
public class Cinema {
    static Scanner kbd = new Scanner(System.in);

    //Declaration of Variables
    static String[] movies = {"Action Blast", "Comedy Hour", "Drama Nights", "Sci-Fi Saga"};
    static String[] showtimes = {"1:00 PM", "3:00 PM", "5:00 PM", "7:00 PM"};
    static int[] prices = {450, 450, 950, 1500};
    // Row and Collums of Seats
    static String[][][] seats = new String[4][4][10];

    //Display Choices
    public static void main(String[] args) {
        int option = 0;
        while(option != 4) {
            option = menu();
            switch(option) {
                case 1: reserveTicket();
                    break;
                case 2: cancelTicket();
                    break;
                case 3: checkSeats();
                    break;
                case 4: System.out.println("Thank you for using the Ticket Booking System!");
                    break;
                default: System.out.println("Invalid choice.");
            }
        }
    }
    public static int menu() {
        System.out.println("Cinema Ticket Booking System");
        System.out.println("1. Reserve Movie Ticket");
        System.out.println("2. Cancel Movie Ticket");
        System.out.println("3. Check Available Seats");
        System.out.println("4. Exit");
        System.out.print("Select an option: ");
        return kbd.nextInt();
    }

    //Selection
    public static void reserveTicket() {
        int movie = chooseMovie();
        int time = chooseShowtime();
        showSeatList(movie, time);
        System.out.print("Enter number of seats to reserve: ");

        int numSeats = kbd.nextInt();
        int available = countVacant(movie, time);

        if (numSeats > available) {
            System.out.println("Not enough seats available!");
            return;
        }
        int[] chosen = new int[numSeats];

        //Seat Selection
        for (int i = 0; i < numSeats; i++) {
            System.out.print("Enter seat number: ");
            int seatNum = kbd.nextInt();

            if (seatNum < 1 || seatNum > 10) {
                System.out.println("Invalid seat.");
                i--;
                continue;
            }

            if (seats[movie][time][seatNum - 1] != null) {
                System.out.println("Seat already taken!");
                i--;
                continue;
            }
            chosen[i] = seatNum;
        }
        //Seat Details
        kbd.nextLine();
        System.out.print("Enter reservation name: ");
        String name = kbd.nextLine();
        int cost = prices[time] * numSeats;
        System.out.println("Total cost: Php" + cost);
        System.out.print("Enter payment amount: ");
        int pay = kbd.nextInt();
        if (pay < cost) {
            System.out.println("Payment failed!");
            return;
        }
        System.out.println("Payment Successful!");
        System.out.println("Change: Php" + (pay - cost));
        for (int c : chosen) {
            seats[movie][time][c - 1] = name;
        }
        System.out.println("Booking Confirmed!");
    }

    //Cancellation
    public static void cancelTicket() {
        int movie = chooseMovie();
        int time = chooseShowtime();
        kbd.nextLine();
        System.out.print("Enter reservation name: ");
        String name = kbd.nextLine();

        boolean found = false;
        int seatCount = 0;
        for (int i = 0; i < 10; i++) {
            if (name.equals(seats[movie][time][i])) {
                seats[movie][time][i] = null;
                found = true;
                seatCount++;
            }
        }
        if (!found) {
            System.out.println("No reservation found.");
            return;
        }
        int refund = (int)(prices[time] * seatCount * 0.70);
        System.out.println("Reservation cancelled.");
        System.out.println("Refund: Php" + refund);
    }

    //Checking of Available Seats
    public static void checkSeats() {
        int movie = chooseMovie();
        int time = chooseShowtime();
        showSeatList(movie, time);
        int vacant = countVacant(movie, time);
        System.out.println("Total Vacant Seats: " + vacant);
    }

    //The Louisian Programmer Way (Methods)
    public static int chooseMovie() {
        System.out.println("Available Movies:");
        for (int i = 0; i < movies.length; i++) {
            System.out.println((i+1) + ". " + movies[i]);
        }
        System.out.print("Select a movie: ");
        return kbd.nextInt() - 1;
    }

    public static int chooseShowtime() {
        System.out.println("Available Showtimes:");
        for (int i = 0; i < showtimes.length; i++) {
            System.out.println((i+1) + ". " + showtimes[i] + " (Php" + prices[i] + ")");
        }
        System.out.print("Select showtime: ");
        return kbd.nextInt() - 1;
    }

    public static void showSeatList(int movie, int time) {
        System.out.println("Seat List:");
        for (int i = 0; i < 10; i++) {
            if (seats[movie][time][i] == null)
                System.out.println("[" + (i+1) + "] Vacant");
            else
                System.out.println("[" + (i+1) + "] " + seats[movie][time][i]);
        }
    }

    public static int countVacant(int movie, int time) {
        int count = 0;
        for (int i = 0; i < 10; i++) {
            if (seats[movie][time][i] == null) count++;
        }
        return count;
    }
}
