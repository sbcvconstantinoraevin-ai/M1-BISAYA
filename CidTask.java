import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// ─── Model Classes ───────────────────────────────────────────────

class Book {
    private String bookId;
    private String title;
    private String author;

    public Book(String bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
    }

    public String getBookId() { return bookId; }
    public String getTitle()  { return title; }
    public String getAuthor() { return author; }

    @Override
    public String toString() {
        return String.format("[%s] \"%s\" by %s", bookId, title, author);
    }
}

class ReturnedBook {
    private Book book;
    private String borrowerId;
    private LocalDate returnDate;

    public ReturnedBook(Book book, String borrowerId, LocalDate returnDate) {
        this.book = book;
        this.borrowerId = borrowerId;
        this.returnDate = returnDate;
    }

    public Book getBook()           { return book; }
    public String getBorrowerId()   { return borrowerId; }
    public LocalDate getReturnDate(){ return returnDate; }
}

class Reservation {
    public enum Status { PENDING, CONFIRMED, CANCELLED, FULFILLED }

    private String reservationId;
    private Book book;
    private String memberId;
    private LocalDate reservationDate;
    private Status status;

    public Reservation(String reservationId, Book book, String memberId,
                       LocalDate reservationDate, Status status) {
        this.reservationId   = reservationId;
        this.book            = book;
        this.memberId        = memberId;
        this.reservationDate = reservationDate;
        this.status          = status;
    }

    public String getReservationId()       { return reservationId; }
    public Book getBook()                  { return book; }
    public String getMemberId()            { return memberId; }
    public LocalDate getReservationDate()  { return reservationDate; }
    public Status getStatus()              { return status; }
}

class BorrowRecord {
    private String recordId;
    private Book book;
    private String memberId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;   // null if not yet returned

    public BorrowRecord(String recordId, Book book, String memberId,
                        LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate) {
        this.recordId   = recordId;
        this.book       = book;
        this.memberId   = memberId;
        this.borrowDate = borrowDate;
        this.dueDate    = dueDate;
        this.returnDate = returnDate;
    }

    public String getRecordId()         { return recordId; }
    public Book getBook()               { return book; }
    public String getMemberId()         { return memberId; }
    public LocalDate getBorrowDate()    { return borrowDate; }
    public LocalDate getDueDate()       { return dueDate; }
    public LocalDate getReturnDate()    { return returnDate; }
    public boolean isReturned()         { return returnDate != null; }
}

// ─── Service Class ───────────────────────────────────────────────

class LibraryService {

    private final List<ReturnedBook>  returnedBooks  = new ArrayList<>();
    private final List<Reservation>   reservations   = new ArrayList<>();
    private final List<BorrowRecord>  borrowHistory  = new ArrayList<>();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    // ── Seed sample data ────────────────────────────────────────
    public void loadSampleData() {
        Book b1 = new Book("B001", "Clean Code",             "Robert C. Martin");
        Book b2 = new Book("B002", "The Pragmatic Programmer","David Thomas");
        Book b3 = new Book("B003", "Design Patterns",         "Gang of Four");
        Book b4 = new Book("B004", "Effective Java",          "Joshua Bloch");
        Book b5 = new Book("B005", "Refactoring",             "Martin Fowler");

        // Returned books
        returnedBooks.add(new ReturnedBook(b1, "M001", LocalDate.of(2025, 3, 10)));
        returnedBooks.add(new ReturnedBook(b3, "M002", LocalDate.of(2025, 3, 12)));
        returnedBooks.add(new ReturnedBook(b5, "M001", LocalDate.of(2025, 3, 14)));

        // Reservations
        reservations.add(new Reservation("R001", b2, "M001",
                LocalDate.of(2025, 3,  1), Reservation.Status.CONFIRMED));
        reservations.add(new Reservation("R002", b4, "M003",
                LocalDate.of(2025, 3,  5), Reservation.Status.PENDING));
        reservations.add(new Reservation("R003", b1, "M002",
                LocalDate.of(2025, 3,  8), Reservation.Status.FULFILLED));
        reservations.add(new Reservation("R004", b3, "M004",
                LocalDate.of(2025, 3, 11), Reservation.Status.CANCELLED));

        // Borrow history
        borrowHistory.add(new BorrowRecord("H001", b1, "M001",
                LocalDate.of(2025, 2,  1), LocalDate.of(2025, 2, 15), LocalDate.of(2025, 2, 14)));
        borrowHistory.add(new BorrowRecord("H002", b2, "M001",
                LocalDate.of(2025, 2, 20), LocalDate.of(2025, 3,  6), null));
        borrowHistory.add(new BorrowRecord("H003", b3, "M002",
                LocalDate.of(2025, 3,  1), LocalDate.of(2025, 3, 15), LocalDate.of(2025, 3, 12)));
        borrowHistory.add(new BorrowRecord("H004", b4, "M003",
                LocalDate.of(2025, 3,  5), LocalDate.of(2025, 3, 19), null));
    }

    // ── 1. View Returned Books ───────────────────────────────────
    public void viewReturnedBooks() {
        System.out.println("║           RETURNED BOOKS                        ║");

        if (returnedBooks.isEmpty()) {
            System.out.println("  No returned books found.");
            return;
        }

        System.out.printf("  %-8s %-35s %-10s %-14s%n",
                "Book ID", "Title", "Borrower", "Return Date");
        System.out.println("  " + "─".repeat(72));

        for (ReturnedBook rb : returnedBooks) {
            System.out.printf("  %-8s %-35s %-10s %-14s%n",
                    rb.getBook().getBookId(),
                    rb.getBook().getTitle(),
                    rb.getBorrowerId(),
                    rb.getReturnDate().format(FMT));
        }
        System.out.printf("%n  Total returned: %d book(s)%n", returnedBooks.size());
    }

    // ── 2. View Reservation Status ───────────────────────────────
    public void viewReservationStatus() {

        System.out.println("           RESERVATION STATUS                    ");


        if (reservations.isEmpty()) {
            System.out.println("  No reservations found.");
            return;
        }

        System.out.printf("  %-6s %-8s %-28s %-10s %-14s %-10s%n",
                "Res ID", "Book ID", "Title", "Member", "Date", "Status");
        System.out.println("  " + "─".repeat(82));

        for (Reservation r : reservations) {
            System.out.printf("  %-6s %-8s %-28s %-10s %-14s %-10s%n",
                    r.getReservationId(),
                    r.getBook().getBookId(),
                    r.getBook().getTitle(),
                    r.getMemberId(),
                    r.getReservationDate().format(FMT),
                    r.getStatus());
        }

        // Summary breakdown
        System.out.println();
        Map<Reservation.Status, Long> summary = new LinkedHashMap<>();
        for (Reservation.Status s : Reservation.Status.values()) summary.put(s, 0L);
        for (Reservation r : reservations)
            summary.merge(r.getStatus(), 1L, Long::sum);

        System.out.println("  Summary:");
        summary.forEach((status, count) ->
                System.out.printf("    %-12s : %d%n", status, count));
    }

    // ── 3. View Borrowing History ────────────────────────────────
    public void viewBorrowingHistory() {
        System.out.println("           BORROWING HISTORY                     ");


        if (borrowHistory.isEmpty()) {
            System.out.println("  No borrowing records found.");
            return;
        }

        System.out.printf("  %-6s %-8s %-28s %-8s %-14s %-14s %-14s %-10s%n",
                "Rec ID", "Book ID", "Title", "Member",
                "Borrow Date", "Due Date", "Return Date", "Status");
        System.out.println("  " + "─".repeat(104));

        for (BorrowRecord rec : borrowHistory) {
            String returnDateStr = rec.isReturned()
                    ? rec.getReturnDate().format(FMT)
                    : "—";
            String status = rec.isReturned() ? "Returned" : "Active";

            System.out.printf("  %-6s %-8s %-28s %-8s %-14s %-14s %-14s %-10s%n",
                    rec.getRecordId(),
                    rec.getBook().getBookId(),
                    rec.getBook().getTitle(),
                    rec.getMemberId(),
                    rec.getBorrowDate().format(FMT),
                    rec.getDueDate().format(FMT),
                    returnDateStr,
                    status);
        }

        long returned = borrowHistory.stream().filter(BorrowRecord::isReturned).count();
        long active   = borrowHistory.size() - returned;
        System.out.printf("%n  Total Records: %d  |  Returned: %d  |  Active: %d%n",
                borrowHistory.size(), returned, active);
    }
}

// ─── Main / Menu ─────────────────────────────────────────────────

public class CidTask {

    public static void main(String[] args) {
        LibraryService service = new LibraryService();
        service.loadSampleData();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("       LIBRARY MANAGEMENT SYSTEM     ");
            System.out.println("  1. View Returned Books             ");
            System.out.println("  2. View Reservation Status         ");
            System.out.println("  3. View Borrowing History          ");
            System.out.println("  0. Exit                            ");
            System.out.print("  Select an option: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> service.viewReturnedBooks();
                case "2" -> service.viewReservationStatus();
                case "3" -> service.viewBorrowingHistory();
                case "0" -> {
                    System.out.println("\n  Goodbye!");
                    running = false;
                }
                default  -> System.out.println("\n  ⚠ Invalid option. Please enter 0–3.");
            }
        }

        scanner.close();
    }
}
