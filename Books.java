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
            System.out.println("\n========================================");
            System.out.println("--- PUBLIC LIBRARY & MEDIA CENTER ---");
            System.out.println("========================================");
            System.out.println("1. View Available Books");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            System.out.println("4. View All Rental Records");
            System.out.println("5. Add New Book");
            System.out.println("6. Search Book");
            System.out.println("7. Delete Book");
            System.out.println("8. View User Rental History");
            System.out.println("9. View Book Details");
            System.out.println("10. Exit");
            System.out.print("Select an option: ");

            // Error handling for non-integer inputs
            if (!input.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number (1-10).");
                input.nextLine();
                continue;
            }

            int choice = input.nextInt();
            input.nextLine(); // Consume newline

            if (choice == 10) {
                System.out.println("\nExiting system. Goodbye!");
                break;
                 }

            switch (choice) {
                case 1: // View Available Books
                    System.out.println("\n--- LIBRARY INVENTORY ---");
                    if (inventory.isEmpty()) {
                        System.out.println("No books in inventory.");
                    } else {
                        inventory.forEach((title, stock) ->
                                System.out.println("Title: " + title + " | Stock: " + stock));
                    }
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
                        System.out.println("✓ Success! Enjoy your reading.");
                    } else {
                        System.out.println("✗ Error: Book is either out of stock or title not found.");
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
                        System.out.println("✓ Success! Book returned to inventory.");
                    } else {
                        System.out.println("✗ Error: Book title not recognized.");
                    }
                    break;
                    case 4: // View All Rentals
                    System.out.println("\n--- RENTAL TRANSACTION LOGS ---");
                    if (rentalRecords.isEmpty()) {
                        System.out.println("No records found.");
                    } else {
                        for (String record : rentalRecords) {
                            System.out.println(record);
                        }
                    }
                    break;

                case 5: // Add New Book
                    System.out.print("Enter Book Title: ");
                    String newBook = input.nextLine();
                    
                    System.out.print("Enter Quantity: ");
                    if (!input.hasNextInt()) {
                        System.out.println("✗ Invalid quantity. Please enter a number.");
                        input.nextLine();
                        break;
                    }
                    int quantity = input.nextInt();
                    input.nextLine();

                    if (quantity <= 0) {
                        System.out.println("✗ Quantity must be greater than 0.");
                        break;
                    }

                    if (inventory.containsKey(newBook)) {
                        inventory.put(newBook, inventory.get(newBook) + quantity);
                        System.out.println("✓ Book quantity updated!");
                    } else {
                        inventory.put(newBook, quantity);
                        System.out.println("✓ New book added to inventory!");
                    }
                    break;

                case 6: // Search Book
                    System.out.print("Enter Book Title to Search: ");
                    String searchBook = input.nextLine();
                    String foundBook = findBookTitle(searchBook);

                    if (foundBook != null) {
                        int stock = inventory.get(foundBook);
                        System.out.println("✓ Book found!");
                        System.out.println("   Title: " + foundBook + " | Stock: " + stock);
                    } else {
                        System.out.println("✗ Book not found in inventory.");
                    }
                    break;

                case 7: // Delete Book
                    System.out.print("Enter Book Title to Delete: ");
                    String deleteBook = input.nextLine();
                    String bookToDelete = findBookTitle(deleteBook);

                    if (bookToDelete != null) {
                        inventory.remove(bookToDelete);
                        System.out.println("✓ Book removed from inventory!");
                    } else {
                        System.out.println("✗ Book not found.");
                    }
                    break;

                case 8: // User Rental History
                    System.out.print("Enter User Name: ");
                    String userName = input.nextLine();
                    System.out.println("\n--- " + userName + "'s RENTAL HISTORY ---");

                    boolean found = false;
                    for (String record : rentalRecords) {
                        if (record.startsWith(userName)) {
                            System.out.println(record);
                            found = true;
                        }
                    }

                    if (!found) {
                        System.out.println("No records found for this user.");
                    }
                    break;
                     case 9: // View Book Details
                    System.out.print("Enter Book Title: ");
                    String bookDetailsTitle = input.nextLine();
                    String detailBook = findBookTitle(bookDetailsTitle);

                    if (detailBook != null) {
                        int stock = inventory.get(detailBook);
                        String status = stock > 0 ? "Available" : "Out of Stock";
                        System.out.println("\n--- BOOK DETAILS ---");
                        System.out.println("Title: " + detailBook);
                        System.out.println("Stock: " + stock);
                        System.out.println("Status: " + status);
                    } else {
                        System.out.println("✗ Book not found.");
                    }
                    break;

                default:
                    System.out.println("✗ Invalid selection. Please choose 1-10.");
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
