package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LibraryGUI extends JFrame {

    private LibraryCatalog libraryCatalog;
    private JTextArea displayArea;

    public LibraryGUI(LibraryCatalog libraryCatalog) {
        this.libraryCatalog = libraryCatalog;

        setTitle("Library GUI");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        displayArea = new JTextArea();
        panel.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JButton addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        JButton removeBookButton = new JButton("Remove Book");
        removeBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeBook();
            }
        });

        JButton displayBooksButton = new JButton("Display Books");
        displayBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayBooks();
            }
        });

        panel.add(addBookButton, BorderLayout.NORTH);
        panel.add(removeBookButton, BorderLayout.WEST);
        panel.add(displayBooksButton, BorderLayout.EAST);

        add(panel);
        setVisible(true);
    }

    private void addBook() {
        String title = JOptionPane.showInputDialog(null, "Enter book title:");
        String author = JOptionPane.showInputDialog(null, "Enter author:");
        String genre = JOptionPane.showInputDialog(null, "Enter genre:");

        libraryCatalog.addBook(title, author, genre);
        displayBooks();
    }

    private void removeBook() {
        String idString = JOptionPane.showInputDialog(null, "Enter book ID to remove:");
        try {
            int id = Integer.parseInt(idString);
            libraryCatalog.removeBook(id);
            displayBooks();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid book ID.");
        }
    }

    private void displayBooks() {
        StringBuilder booksList = new StringBuilder("Books in the Library:\n");
        for (Book book : libraryCatalog.getBookCatalog()) {
            booksList.append("ID: ").append(book.getId()).append(", ").append(book.toString()).append("\n");
        }
        displayArea.setText(booksList.toString());
    }

    public static void main(String[] args) {
        try {
            LibraryCatalog libraryCatalog = new LibraryCatalog();
            SwingUtilities.invokeLater(() -> new LibraryGUI(libraryCatalog));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}