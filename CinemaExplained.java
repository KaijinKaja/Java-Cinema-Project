import java.util.Scanner;
public class CinemaExplained {
        // Create one Scanner object for the entire class. Used for all user inputs.
        static Scanner kbd = new Scanner(System.in);

        // Array storing movie titles. Index 0–3.
        static String[] movies = {
                "Wicked: For Good",
                "Frankenstein",
                "Now You See Me: Now You Don't",
                "The Fantastic Four: First Steps"
        };

        // Array storing the showtime labels (each corresponds to a price).
        static String[] showtimes = {
                "1:00 PM – Regular",
                "3:00 PM – Regular",
                "5:00 PM – 3D",
                "7:00 PM – Director’s Cut"
        };

        // Each index matches the showtimes[] arrays
        // showtime[0] has price prices[0].
        static int[] prices = {450, 450, 950, 1500};

        // 3D Array for seat reservations.
        // seats[movie][time][seat]
        // • movie index (0–3)
        // • showtime index (0–3)
        // • 10 seats per showtime
        // A seat is null = available, or holds a name = reserved.
        static String[][][] seats = new String[4][4][10];

        // MAIN MENU LOOP
        public static void main(String[] args) {
            int option;

            // This do–while loop keeps running until the user selects “Exit”
            do {
                System.out.println("Cinema Ticket Booking System");
                System.out.println("1. Reserve Movie Ticket");
                System.out.println("2. Cancel Movie Ticket");
                System.out.println("3. Check Available Seats");
                System.out.println("4. Exit");
                System.out.print("Select an option: ");

                try {
                    // Input is taken as a string then converted to int
                    option = Integer.parseInt(kbd.nextLine());
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter a number.\n");
                    option = -1; // Forces menu to repeat
                    continue;
                }

                // Switch executes different methods depending on user choice
                switch (option) {
                    case 1 -> reserveMovieTicket();
                    case 2 -> cancelMovieTicket();
                    case 3 -> checkSeats();
                    case 4 -> exit();
                    default -> System.out.println("Invalid Option.");
                }

            } while (option != 4); // Loop ends only when user chooses Exit
        }

        // RESERVE MOVIE TICKET
        static void reserveMovieTicket() {

            // User chooses a movie and showtime
            int movie = chooseMovie();
            int time = chooseShowtime();

            // Show seat status for the chosen movie/time
            showSeatList(movie, time);

            // Ask how many seats the user wants to reserve
            int numSeat;
            while (true) {
                // Loop continues until a valid number is entered
                System.out.print("Enter number of seats to reserve: ");
                try {
                    numSeat = Integer.parseInt(kbd.nextLine());

                    // Must be between 1-10
                    if (numSeat > 0 && numSeat <= 10) break;

                } catch (Exception e) {
                    // ignored — loop continues
                }

                System.out.println("Invalid seat number.");
            }

            // Check how many seats remain empty
            int available = countVacant(movie, time);

            // Cannot reserve more than available seats
            if (numSeat > available) {
                System.out.println("Not enough available seats for this showtime");
                return;
            }

            // Array holding the seat numbers chosen by the user
            int[] chosenSeats = new int[numSeat];

            // SEAT SELECTION LOOP
            for (int i = 0; i < numSeat; i++) {

                // This while loop forces the user to keep choosing
                // until they select a valid, available seat
                while (true) {
                    System.out.print("Enter seat number: ");

                    try {
                        int seatNum = Integer.parseInt(kbd.nextLine());

                        // Invalid seat range guard
                        if (seatNum < 1 || seatNum > 10) {
                            System.out.println("Invalid seat. Choose between 1–10.");
                            continue;
                        }

                        // Check if seat already reserved by someone else
                        if (seats[movie][time][seatNum - 1] != null) {
                            System.out.println("Seat is already taken.");
                            continue;
                        }

                        // Check if the user already selected this seat in the same reservation
                        if (alreadyChosen(chosenSeats, seatNum)) {
                            System.out.println("You already selected that seat.");
                            continue;
                        }

                        // If all checks passed then seat is stored
                        chosenSeats[i] = seatNum;
                        break;

                    } catch (Exception e) {
                        System.out.println("Invalid input");
                    }
                }
            }

            System.out.print("Enter reservation name: ");
            String name = kbd.nextLine();

            // Multiply seat price × number of seats
            int totalCost = prices[time] * numSeat;
            System.out.println("Total Payment: ₱" + totalCost);

            // Payment validation loop
            int payment;
            while (true) {
                System.out.print("Enter amount: ");
                try {
                    payment = Integer.parseInt(kbd.nextLine());
                    if (payment >= totalCost) break; // Must be enough to pay
                } catch (Exception e) {}

                System.out.println("Invalid or Insufficient payment.");
            }

            System.out.println("Payment Successful!");
            System.out.println("Change: ₱" + (payment - totalCost));

            // After payment → assign seats to the person's name
            for (int s : chosenSeats) {
                seats[movie][time][s - 1] = name;
            }

            // Receipt output
            System.out.println("Booking Confirmed for " + name);
            System.out.println("Movie: " + movies[movie]);
            System.out.println("Showtime: " + showtimes[time]);

            System.out.print("Reserved Seats: ");
            for (int s : chosenSeats) {
                System.out.print("[" + s + "] ");
            }
            System.out.println();
        }

        // CANCEL RESERVATION
        static void cancelMovieTicket() {

            int movie = chooseMovie();
            int time = chooseShowtime();

            System.out.print("Enter reservation name: ");
            String name = kbd.nextLine();

            boolean found = false;
            int seatCount = 0;

            // Loop through all 10 seats
            for (int i = 0; i < 10; i++) {

                // If the seat matches the name then cancels it
                if (name.equals(seats[movie][time][i])) {
                    seats[movie][time][i] = null; // remove reservation
                    found = true;
                    seatCount++;
                }
            }

            if (!found) {
                System.out.println("No reservation under that name.");
                return;
            }

            // Refund 70% of the paid amount
            int refund = (int) (prices[time] * seatCount * 0.70);
            System.out.println("Reservation canceled for " + name + ".");
            System.out.println("Refund Amount: ₱" + refund);
        }

        // CHECK SEAT
        static void checkSeats() {
            int movie = chooseMovie();
            int time = chooseShowtime();

            showSeatList(movie, time);
            System.out.println("Total Vacant Seats: " + countVacant(movie, time));
        }

        // HELPER: Check if user already selected a seat
        static boolean alreadyChosen(int[] chosen, int seatNum) {
            // Loop through the chosen seats
            for (int n : chosen) {
                if (n == seatNum) return true;
            }
            return false;
        }


        // MOVIE CHOOSER
        static int chooseMovie() {
            int choice = -1;

            // Repeats until valid selection
            while (true) {
                System.out.println("\nAvailable Movies:");
                for (int i = 0; i < movies.length; i++) {
                    System.out.println((i + 1) + ". " + movies[i]);
                }
                System.out.print("Select a movie: ");

                try {
                    choice = Integer.parseInt(kbd.nextLine()) - 1;

                    // Valid index range check
                    if (choice >= 0 && choice < movies.length) break;

                } catch (Exception e) {}

                System.out.println("Invalid movie option.");
            }

            return choice;
        }

        // SHOWTIME SELECTION
        static int chooseShowtime() {
            int choice = -1;

            while (true) {
                System.out.println("\nAvailable Showtimes:");
                for (int i = 0; i < showtimes.length; i++) {
                    System.out.println((i + 1) + ". " + showtimes[i] + " (₱" + prices[i] + ")");
                }

                System.out.print("Select showtime: ");

                try {
                    choice = Integer.parseInt(kbd.nextLine()) - 1;

                    // Checks valid index
                    if (choice >= 0 && choice < showtimes.length) break;

                } catch (Exception e) {}

                System.out.println("Invalid showtime option.");
            }

            return choice;
        }

        // DISPLAY SEATS
        static void showSeatList(int movie, int time) {
            System.out.println("\nSeat List:");

            // Loop shows all 10 seats
            for (int i = 0; i < 10; i++) {

                // null = Vacant, otherwise display the name
                if (seats[movie][time][i] == null)
                    System.out.println("[" + (i + 1) + "] Vacant");
                else
                    System.out.println("[" + (i + 1) + "] " + seats[movie][time][i]);
            }
        }

        // COUNT VACANT SEATS
        static int countVacant(int movie, int time) {
            int count = 0;

            // Loop counts how many of the 10 seats are null
            for (int i = 0; i < 10; i++) {
                if (seats[movie][time][i] == null) count++;
            }

            return count;
        }

        // EXIT MESSAGE
        static void exit() {
            System.out.println("Thank you, Goodbye");
        }
    }
/* Overview
A program that simulates a ticket booking system. There are only 4 movies, with 10 seat capacity each,
and 4 showtime schedules.
1. User Menu
   -Display options: Reserve Movie ticket, Cancel Movie Ticket, Check Available Seats, and Exit.
2. Reserve Movie Ticket
   -Displays the movie array with the select input.
   -After user selected, it displays the details for the selection
    of showtime and seats.
   -input validation included.
3. Cancel Movie Ticket
   -User selects which movie, what showtime, and name used for reservation.
   -the name will be searched through the seat array.
    *If found: "Reservation canceled for " + name +"."
    *If not found: "No reservation under that name." Return 70% of
     the payment.
4. Check Available Seats
   -User selects movie and showtime.
   -will count the vacant seat in the selected showtime.
   -displays a 'map' of seats and the total vacant seats.
5. Exit
   -The program will terminate only if the user picks exit.
6. Utilities/Helper
 */
