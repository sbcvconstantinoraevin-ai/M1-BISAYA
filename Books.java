import java.util.*;

public class Books {
    // Database simulation
    static Map<String, Integer> inventory = new HashMap<>();
    static List<String> rentalRecords = new ArrayList<>();

    public static void main(String[] args) {
        // Initial Books - Using Title Case
        inventory.put("Redo of Healer", 3);
        inventory.put("One Piece", 2);
        inventory.put("Naruto", 5);

        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- PUBLIC LIBRARY & MEDIA CENTER ---");
            System.out.println("1. View Available Books");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            System.out.println("4. View All Rental Records");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");

            // Error handling for non-integer inputs
            if (!input.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number (1-5).");
                input.nextLine();
                continue;
            }

            int choice = input.nextInt();
            input.nextLine(); // Consume newline

            if (choice == 5) {
                System.out.println("Exiting system. Goodbye!");
                break;
            }

            switch (choice) {
                case 1: // New Feature: List Inventory
                    System.out.println("\n--- LIBRARY INVENTORY ---");
                    inventory.forEach((title, stock) ->
                            System.out.println("Title: " + title + " | Stock: " + stock));
                    break;

                case 2: // Borrow Feature
                    System.out.print("Enter Book Title: ");
                    String borrowBook = input.nextLine();

                    // Logic to find book regardless of Uppercase/Lowercase
                    String matchedBorrow = findBookTitle(borrowBook);

                    if (matchedBorrow != null && inventory.get(matchedBorrow) > 0) {
                        System.out.print("Enter User Name: ");
                        String borrowUser = input.nextLine();
                        inventory.put(matchedBorrow, inventory.get(matchedBorrow) - 1);
                        rentalRecords.add(borrowUser + " | Borrowed | " + matchedBorrow);
                        System.out.println("Success! Enjoy your reading.");
                    } else {
                        System.out.println("Error: Book is either out of stock or title not found.");
                    }
                    break;

                case 3: // Return Feature
                    System.out.print("Enter Book Title: ");
                    String returnBook = input.nextLine();
                    String matchedReturn = findBookTitle(returnBook);

                    if (matchedReturn != null) {
                        System.out.print("Enter User Name: ");
                        String returnUser = input.nextLine();
                        inventory.put(matchedReturn, inventory.get(matchedReturn) + 1);
                        rentalRecords.add(returnUser + " | Returned | " + matchedReturn);
                        System.out.println("Success! Book returned to inventory.");
                    } else {
                        System.out.println("Error: Book title not recognized.");
                    }
                    break;

                case 4: // View Rentals
                    System.out.println("\n--- RENTAL TRANSACTION LOGS ---");
                    if (rentalRecords.isEmpty()) {
                        System.out.println("No records found.");
                    } else {
                        for (String record : rentalRecords) {
                            System.out.println(record);
                        }
                    }
                    break;

                default:
                    System.out.println("Invalid selection. Please choose 1-5.");
            }
        }
        input.close();
    }

    // Helper method to handle Case Sensitivity
    private static String findBookTitle(String inputTitle) {
        for (String actualTitle : inventory.keySet()) {
            if (actualTitle.equalsIgnoreCase(inputTitle)) {
                return actualTitle;
            }
        }
        return null;
    }
}
