package main;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
public class Book {
	private int id;
	private String title;
	private String author;
	private String genre;
	private LocalDate lastCheckOutDate;
	private boolean checkedOut;
	
	/**
     * Constructor for creating a Book with all details.
     *
     * @param id               The ID of the book.
     * @param title            The title of the book.
     * @param author           The author of the book.
     * @param genre            The genre of the book.
     * @param lastCheckOutDate The date the book was last checked out.
     * @param checkedOut       Whether the book is checked out or not.
     */
	public Book(int id, String title, String author, String genre, LocalDate lastCheckOutDate, boolean checkedOut) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.lastCheckOutDate = lastCheckOutDate;
		this.checkedOut = checkedOut;
		}
	
	
	// todos los getters y setters 
	public Book() {}
	public int getId() {
		return id;
}
	public void setId(int id) {
		this.id = id;
		}
	public String getTitle() {
		return title;
		}
	public void setTitle(String title) {
		this.title = title;
		}
	public String getAuthor() {
		return author;
		}
	public void setAuthor(String author) {
		this.author = author;
		}
	public String getGenre() {
		return genre;
		}
	public void setGenre(String genre) {
		this.genre = genre;
		}
	public LocalDate getLastCheckOut() {
		return lastCheckOutDate;
		}
	public void setLastCheckOut(LocalDate lastCheckOutDate) {
		this.lastCheckOutDate = lastCheckOutDate;
		}
	public boolean isCheckedOut() {
		return checkedOut;
		}
	public void setCheckedOut(boolean checkedOut) {
		this.checkedOut = checkedOut;
		}
	@Override

	public String toString() {
/*
* This is supposed to follow the format
* 
* {TITLE} By {AUTHOR}
* 
* Both the title and author are in uppercase.
*/
		return "" + title.toUpperCase() + " BY " + author.toUpperCase() + "";
		}
	public float calculateFees() {
/*
* fee (if applicable) = base fee + 1.5 per additional day
*/
// esto calcula el precio, gracias a la formula dada
		// Calculate the difference in days between the last checkout date and today
		// Base fee and fee per day for overdue books
	    float baseFee = 10.0f;
	    float feePerDay = 1.5f;

	    // Get the current date
	    LocalDate currentDate = LocalDate.of(2023, 9, 15);

	    // Calculate the difference in days between the last checkout date and today
	    long daysOverdue = ChronoUnit.DAYS.between(lastCheckOutDate, currentDate);

	    // Calculate fees for each overdue day
	    if(daysOverdue <31) {
	    	return 0;
	    }
	    float totalFees = baseFee + (feePerDay * Math.max(0, daysOverdue - 31));

	    return totalFees;
	}

	}
