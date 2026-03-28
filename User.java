import java.util.*;

public class User {

    static HashMap<String, String> accounts = new HashMap<>();

    // { Title, Status, Section }
    static String[][] books = {
            {"Ibong Adarna","Available", "Section 10"},
            {"Noli Me Tangere", "Available", "Section 10"},
            {"El Filibusterismo","Available", "Section 10"},
            {"Florante at Laura","Available", "Section 10"},
            {"Pedro Penduko","Available", "Section 10"},
            {"Pogi",  "Available", "Section 10"},
    };

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Public Library & Media Center ===");

        while (true) {
            System.out.println("\n1. Create Account");
            System.out.println("2. Log In");
            System.out.println("3. Exit");
            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();

            if (choice.equals("1")) {
                System.out.print("Enter Username: ");
                String user = sc.nextLine().trim();
                if (accounts.containsKey(user)) {
                    System.out.println("Username already taken.");
                } else {
                    System.out.print("Enter Password: ");
                    String pass = sc.nextLine().trim();
                    accounts.put(user, pass);
                    System.out.println("Account created successfully!");
                }

            } else if (choice.equals("2")) {
                System.out.print("Enter Username: ");
                String user = sc.nextLine().trim();
                System.out.print("Enter Password: ");
                String pass = sc.nextLine().trim();

                if (accounts.containsKey(user) && accounts.get(user).equals(pass)) {
                    System.out.println("Login successful! Welcome, " + user + "!");
                    searchMenu(sc);
                } else {
                    System.out.println("Invalid username or password.");
                }

            } else if (choice.equals("3")) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
    static void searchMenu(Scanner sc) {
        while (true) {
            System.out.println("\n1. Search Books");
            System.out.println("2. Show All Books");
            System.out.println("3. Log Out");
            System.out.print("Choice: ");
            String c = sc.nextLine().trim();

            if (c.equals("1")) {
                System.out.print("Enter book title to search: ");
                String query = sc.nextLine().trim().toLowerCase();
                boolean found = false;
                for (String[] book : books) {
                    if (book[0].toLowerCase().contains(query)) {
                        System.out.println("  Title: " + book[0] + " | Status: " + book[1] + " | " + book[2]);
                        found = true;
                    }
                }
   if (!found) System.out.println("No books found.");

            } else if (c.equals("2")) {
                System.out.println("All Books:");
                for (String[] book : books) {
                    System.out.println("  Title: " + book[0] + " | Status: " + book[1] + " | " + book[2]);
                }

            } else if (c.equals("3")) {
                System.out.println("Logged out.");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}
         
