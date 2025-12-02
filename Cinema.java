import java.util.Scanner;
public class FinalProject { 
    static Scanner kbd = new Scanner(System.in);

    // Movies & Showtimes
    static String[] movies = {
            "Wicked: For Good",
            "Frankenstein",
            "Now You See Me: Now You Don't",
            "The Fantastic Four: First Steps"
    };
        static String[] showtimes = {
                "1:00 PM – Regular",
                "3:00 PM – Regular",
                "5:00 PM – 3D",
                "7:00 PM – Director’s Cut"
        };

        // Prices
        static int[] prices = {450, 450, 950, 1500};

        // Seats
        static String[][][] seats = new String[4][4][10];

        public static void main(String[] args) {
            int option;

            do {
                System.out.println("Cinema Ticket Booking System");
                System.out.println("1. Reserve Movie Ticket");
                System.out.println("2. Cancel Movie Ticket");
                System.out.println("3. Check Available Seats");
                System.out.println("4. Exit");
                System.out.print("Select an option: ");

                option = Integer.parseInt(kbd.nextLine());

                switch (option) {
                    case 1 -> reserveMovieTicket();
                    case 2 -> cancelMovieTicket();
                    case 3 -> checkSeats();
                    case 4 -> exit();
                    default -> System.out.println("Invalid Option.");
                }

            } while (option != 4);
        }
        // Reservation
        static void reserveMovieTicket() {
            int movie = chooseMovie();
            int time = chooseShowtime();

            showSeatList(movie, time);

            System.out.print("Enter number of seats to reserve: ");
            int numSeat = Integer.parseInt(kbd.nextLine());

            int available = countVacant(movie, time);

            if (numSeat > available) {
                System.out.println("Not enough available seats for this showtime");
                return;
            }

            int[] chosenSeats = new int[numSeat];

            // Seat selection loop
            for (int i = 0; i < numSeat; i++) {
                System.out.print("Enter seat number: ");
                int seatNum = Integer.parseInt(kbd.nextLine());

                if (seatNum < 1 || seatNum > 10) {
                    System.out.println("Invalid seat. Choose between 1–10.");
                    i--;
                    continue;
                }
                if (seats[movie][time][seatNum - 1] != null) {
                    System.out.println("Seat already taken.");
                    i--;
                    continue;
                }
                chosenSeats[i] = seatNum;
            }

            // Payment
            System.out.print("Enter reservation name: ");
            String name = kbd.nextLine();
            int totalCost = prices[time] * numSeat;
            System.out.println("Total Payment: ₱" + totalCost);
            System.out.print("Enter amount: ");
            int payment = Integer.parseInt(kbd.nextLine());
            if (payment < totalCost) {
                System.out.println("Payment failed");
                return;
            }
            System.out.println("Payment successful!");
            System.out.println("Change: ₱" + (payment - totalCost));

            // Save seats
            for (int s : chosenSeats) {
                seats[movie][time][s - 1] = name;
            }

            // Receipt
            System.out.println("Payment Successful!");
            System.out.println("Change: ₱" + (payment - totalCost));
            System.out.println("Booking Confirmed for " + name);
            System.out.println("Movie: " + movies[movie]);
            System.out.println("Showtime: " + showtimes[time]);

            System.out.print("Reserved Seats: ");
            for (int s : chosenSeats) {
                System.out.print("[" + s + "] ");
            }
            System.out.println();
        }

        // Cancellation of Reservation
        static void cancelMovieTicket() {
            int movie = chooseMovie();
            int time = chooseShowtime();
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
                System.out.println("No reservation under that name.");
                return;
            }

            // Refund = 70%
            int refund = (int) (prices[time] * seatCount * 0.70);
            System.out.println("Reservation canceled.");
            System.out.println("Refund Amount: ₱" + refund);
        }

        // Checking of Seats
        static void checkSeats() {
            int movie = chooseMovie();
            int time = chooseShowtime();
            showSeatList(movie, time);
            System.out.println("Total Vacant Seats: " + countVacant(movie, time));
        }

        // Louisian Programming Methods
        static int chooseMovie() {
            System.out.println("\nAvailable Movies:");
            for (int i = 0; i < movies.length; i++) {
                System.out.println((i + 1) + ". " + movies[i]);
            }
            System.out.print("Select a movie: ");
            return Integer.parseInt(kbd.nextLine()) - 1;
        }

        static int chooseShowtime() {
            System.out.println("\nAvailable Showtimes:");
            for (int i = 0; i < showtimes.length; i++) {
                System.out.println((i + 1) + ". " + showtimes[i] + " (₱" + prices[i] + ")");
            }
            System.out.print("Select showtime: ");
            return Integer.parseInt(kbd.nextLine()) - 1;
        }

        static void showSeatList(int movie, int time) {
            System.out.println("\nSeat List:");
            for (int i = 0; i < 10; i++) {
                if (seats[movie][time][i] == null)
                    System.out.println("[" + (i + 1) + "] Vacant");
                else
                    System.out.println("[" + (i + 1) + "] " + seats[movie][time][i]);
            }
        }

        static int countVacant(int movie, int time) {
            int count = 0;
            for (int i = 0; i < 10; i++) {
                if (seats[movie][time][i] == null) count++;
            }
            return count;
        }

        static void exit() {
            System.out.println("Thank you, Goodbye");
        }
    }






