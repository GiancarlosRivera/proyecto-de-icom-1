/**
 * Clase LibraryCatalog: Gestiona un catálogo de biblioteca.
 * 
 * Esta clase se utiliza para administrar un catálogo de libros y usuarios en una biblioteca.
 * Proporciona funcionalidad para agregar, eliminar, verificar y generar informes sobre libros y usuarios.
 *
 * @author Giancarlos Rivera Lasanta (University Student)
 * @version 1.0
 */
package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import interfaces.FilterFunction;
import interfaces.List;
import data_structures.ArrayList;

public class LibraryCatalog {
    private List<Book> bookCatalog;
    private List<User> users;
    /**
     * Constructor LibraryCatalog: Inicializa un nuevo catálogo de biblioteca.
     * 
     * Este constructor inicializa el catálogo de libros y la lista de usuarios cargando datos desde archivos.
     * 
     */
    
    public LibraryCatalog() throws IOException {
        bookCatalog = getBooksFromFiles();
        users = getUsersFromFiles();
    }

    /**
     * Reads book data from a CSV file and creates Book objects based on the data.
     *
     * @return A list of Book objects populated from the CSV file.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    
    private List<Book> getBooksFromFiles() throws IOException {
        String filePath = "data/catalog.csv";
        List<Book> books = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine(); // Skip the header line

            while ((line = reader.readLine()) != null) {
                String[] bookData = line.split(",");
                int id = Integer.parseInt(bookData[0]);
                String title = bookData[1];
                String author = bookData[2];
                String genre = bookData[3];
                LocalDate lastCheckoutDate = LocalDate.parse(bookData[4]);
                boolean checkedOut = Boolean.parseBoolean(bookData[5]);

                Book book = new Book(id, title, author, genre, lastCheckoutDate, checkedOut);
                books.add(book);
            }
        }

        return books;
    }

    private List<User> getUsersFromFiles() throws IOException {
        String filePath = "data/user.csv";
        List<User> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine(); // Skip the header line

            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                int id = Integer.parseInt(userData[0]);
                String fullName = userData[1].trim();
                User user = new User(id, fullName);

                if (userData.length >= 3) {
                    String Storage = userData[2].trim();
                    String[] Ids = Storage.replace("{", "").replace("}", "").split(" ");

                    for (String Id : Ids) {
                        int IdNumbers = Integer.parseInt(Id.trim());
                        user.getCheckedOutList().add(getBookById(IdNumbers));
                    }
                }

                users.add(user);
            }
        }

        return users;
    }
    /**
     * Gets the catalog of books in the library.
     *
     * @return The list of books in the catalog.
     */
    public List<Book> getBookCatalog() {
        return bookCatalog;
    }

    public List<User> getUsers() {
        return users;
    }
    /**
     * Retrieves a book based on its ID.
     *
     * @param id The ID of the book to retrieve.
     * @return The Book object with the specified ID, or null if not found.
     */
    private Book getBookById(int id) {
        for (Book book : bookCatalog) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }
    /**
     * Gets the maximum book ID in the catalog.
     *
     * @return The maximum book ID.
     */
    private int getMaxBookId() {
        int maxId = 0;

        for (Book book : bookCatalog) {
            int currentId = book.getId();
            if (currentId > maxId) {
                maxId = currentId;
            }
        }

        return maxId;
    }
    /**
     * Adds a new book to the catalog.
     *
     * @param title  The title of the book.
     * @param author The author of the book.
     * @param genre  The genre of the book.
     */
    public void addBook(String title, String author, String genre) {
        int newId = getMaxBookId() + 1;
        Book newBook = new Book(newId, title, author, genre, LocalDate.of(2023, 9, 15), false);
        bookCatalog.add(newBook);
    }
    /**
     * Removes a book from the catalog based on its ID.
     *
     * @param id The ID of the book to remove.
     */
    public void removeBook(int id) {
        Book bookToRemove = null;
        for (Book book : bookCatalog) {
            if (book.getId() == id) {
                bookToRemove = book;
                break;
            }
        }

        if (bookToRemove != null) {
            bookCatalog.remove(bookToRemove);
        }
    }
    
    /**
     * Attempts to check out a book based on its ID.
     * If the book is available, sets its checkout status to true and updates the last checkout date.
     *
     * @param id The ID of the book to check out.
     * @return True if the book was successfully checked out, false otherwise.
     */
    public boolean checkOutBook(int id) {
        for (Book book : bookCatalog) {
            if (book.getId() == id) {
                if (!book.isCheckedOut()) {
                    book.setCheckedOut(true);
                    book.setLastCheckOut(LocalDate.now());
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
    /**
     * Attempts to return a book based on its ID.
     * If the book is checked out, sets its checkout status to false.
     *
     * @param id The ID of the book to return.
     * @return True if the book was successfully returned, false otherwise.
     */
    public boolean returnBook(int id) {
        for (Book book : bookCatalog) {
            if (book.getId() == id) {
                if (book.isCheckedOut()) {
                    book.setCheckedOut(false);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean getBookAvailability(int id) {
        Book book = getBookById(id);
        if (book != null) {
            return !book.isCheckedOut();
        }
        return false;
    }

    public int bookCount(String title) {
        int count = 0;
        for (Book book : bookCatalog) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                count++;
            }
        }
        return count;
    }

    private int calculateBookCountByGenre(String genre) {
        int count = 0;
        for (Book book : bookCatalog) {
            if (book.getGenre().equalsIgnoreCase(genre)) {
                count++;
            }
        }
        return count;
    }

    private int calculateTotalCheckedOutBooks() {
        int count = 0;
        for (Book book : bookCatalog) {
            if (book.isCheckedOut()) {
                count++;
            }
        }
        return count;
    }
    /**
     * Generates a report summarizing books in various genres, books currently checked out,
     * and users with outstanding fees. The report is printed and saved to a file.
     *
     * @throws IOException If an I/O error occurs while writing the report to a file.
     */
    public void generateReport() throws IOException {
        String output = "\t\t\t\tREPORT\n\n";
        output += "\t\tSUMMARY OF BOOKS\n";
        output += "GENRE\t\t\t\t\t\tAMOUNT\n";
        output += "Adventure\t\t\t\t\t" + calculateBookCountByGenre("Adventure") + "\n";
        output += "Fiction\t\t\t\t\t\t" + calculateBookCountByGenre("Fiction") + "\n";
        output += "Classics\t\t\t\t\t" + calculateBookCountByGenre("Classics") + "\n";
        output += "Mystery\t\t\t\t\t\t" + calculateBookCountByGenre("Mystery") + "\n";
        output += "Science Fiction\t\t\t\t\t" + calculateBookCountByGenre("Science Fiction") + "\n";
        output += "====================================================\n";
        output += "\t\t\tTOTAL AMOUNT OF BOOKS\t" + bookCatalog.size() + "\n\n";

        output += "\t\t\tBOOKS CURRENTLY CHECKED OUT\n\n";
        for (Book book : bookCatalog) {
            if (book.isCheckedOut()) {
                output += book.getTitle() + " BY " + book.getAuthor() + "\n";
            }
        }
        output += "====================================================\n";
        output += "\t\t\tTOTAL AMOUNT OF BOOKS\t" + calculateTotalCheckedOutBooks() + "\n\n";

        output += "\n\n\t\tUSERS THAT OWE BOOK FEES\n\n";
        float totalFees = 0.0f;

        List<User> usersCheckedOutBooks = searchForUsers(user -> !user.getCheckedOutList().isEmpty());

        for (User user : usersCheckedOutBooks) {
            float userTotalFees = 0.0f;
            for (Book book : user.getCheckedOutList()) {
                userTotalFees += book.calculateFees();
                totalFees += book.calculateFees();
            }
            output += user.getName() + "\t\t\t\t\t$" + String.format("%.2f", userTotalFees) + "\n";
        }

        output += "====================================================\n";
        output += "\t\t\t\tTOTAL DUE\t$" + String.format("%.2f", totalFees) + "\n\n\n";

        System.out.println(output);

        // Write to the file
        String filePath = "report.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(output);
        }

        System.out.println("Report generated and saved to " + filePath);
    }
    /**
     * Searches for books in the catalog based on a filter function.
     *
     * @param func The filter function to apply.
     * @return A list of books that match the filter criteria.
     */
    public List<Book> searchForBook(FilterFunction<Book> func) {
        List<Book> result = new ArrayList<>();

        for (Book book : bookCatalog) {
            if (func.filter(book)) {
                result.add(book);
            }
        }

        return result;
    }
    /**
     * Searches for users based on a filter function.
     *
     * @param func The filter function to apply.
     * @return A list of users that match the filter criteria.
     */
    public List<User> searchForUsers(FilterFunction<User> func) {
        List<User> result = new ArrayList<>();

        for (User user : users) {
            if (func.filter(user)) {
                result.add(user);
            }
        }

        return result;
    }
}
