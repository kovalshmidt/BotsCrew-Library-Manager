import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Requests {

    private Scanner input;
    private final String ADD_BOOK = "INSERT INTO books( name, author) VALUES( ?, ?)";
    private final String DELETE_BOOK = "DELETE FROM books WHERE id = ?";
    private final String UPDATE_BOOK = "UPDATE books SET name = ?,author = ? WHERE id = ?;";

    private final DBController controller;
    private final Statement statement;
    ;

    private PreparedStatement prepStatAddBook;
    private PreparedStatement prepStatDeleteBook;
    private PreparedStatement prepStatSelectedBook;
    private PreparedStatement prepStatUpdateBook;
    private PreparedStatement prepStatAllBooksOrderedByName;


    public Requests() throws SQLException {
        controller = new DBController();
        statement = controller.getConnection().createStatement();
        ;
    }

    // returns all books by the specified name
    public Map<Integer, Books> booksByName(String bookName) throws SQLException {
        prepStatSelectedBook = controller.getConnection().prepareStatement(
                "SELECT * FROM books WHERE name = " + "'" + bookName + "'");

        ResultSet selectedBooks = prepStatSelectedBook.executeQuery();


        Map<Integer, Books> mapBooks = new TreeMap<Integer, Books>();
        int count = 1;

        while (selectedBooks.next()) {
            mapBooks.put(count, new Books(
                    selectedBooks.getInt("id"),
                    selectedBooks.getString("name"),
                    selectedBooks.getString("author")
            ));
            count++;
        }
        return mapBooks;
    }

    // adds book
    public void addBook() throws SQLException {
        prepStatAddBook = controller.getConnection().prepareStatement(ADD_BOOK);

        String bookName;
        String bookAuthor;
        //input Title
        System.out.println("Title: ");
        do{
            bookName = input.nextLine();
            if(bookName.equals("")){
                System.out.println("Type a valid title");
            }else
                break;
        }while (true);

        // input Author
        System.out.println("Author: ");
        bookAuthor = input.nextLine();
        if(bookAuthor.equals(""))
            bookAuthor = "Unknown";

        //execute statements in sql
        prepStatAddBook.setString(1, bookName);
        prepStatAddBook.setString(2, bookAuthor);
        prepStatAddBook.execute();

        System.out.printf("Book: \"%s\" %s  was added\n", bookName, bookAuthor);
    }

    // deletes book
    public void deleteBook() throws SQLException {
        prepStatDeleteBook = controller.getConnection().prepareStatement(DELETE_BOOK);

        boolean bookDoesNotExit;

        do {
            bookDoesNotExit = false;
            // input title of the book to delete
            System.out.println("Title of the book to delete: ");
            String bookName = input.nextLine();

            Map<Integer, Books> mapBooks = booksByName(bookName);

            if (mapBooks.size() == -1) {
                System.out.printf("\nBook with this title is not present in the Library\n");
                System.out.println("Digit:\n1 - to enter another title\n2 - to proceed\n");
                int x = input.nextInt();
                if (x == 1)
                    bookDoesNotExit = true;

            } else if (mapBooks.size() == 1) {
                prepStatDeleteBook.setInt(1, mapBooks.get(1).getBookId());
                prepStatDeleteBook.execute();
                System.out.printf("Book: \"%s\" %s was deleted\n",
                        mapBooks.get(1).getBookName(),
                        mapBooks.get(1).getBookAuthor()
                );
            } else if (mapBooks.size() > 1) {

                System.out.println("There are more than one book with this title." +
                        "\nPlease select the one you want to delete:");

                for (Map.Entry<Integer, Books> entry : mapBooks.entrySet()) {
                    System.out.printf("%d - '%s' %s\n",
                            entry.getKey(),
                            entry.getValue().getBookName(),
                            entry.getValue().getBookAuthor());
                }

                boolean wrongKey;
                int bookNum;

                do {
                    wrongKey = false;
                    bookNum = input.nextInt();

                    if (!mapBooks.containsKey(bookNum)) {
                        System.out.println("\nYou entered an invalid value! Please try again.\n");
                        wrongKey = true;
                    }
                } while (wrongKey);

                int id = mapBooks.get(bookNum).getBookId();
                String name = mapBooks.get(bookNum).getBookName();
                String author = mapBooks.get(bookNum).getBookAuthor();

                prepStatDeleteBook.setInt(1, id);
                prepStatDeleteBook.execute();

                System.out.printf("Book: \"%s\" %s was deleted\n", name, author);
            }
        } while (bookDoesNotExit);
    }

    // edits book
    public void editBook() throws SQLException {
        prepStatUpdateBook = controller.getConnection().prepareStatement(UPDATE_BOOK);
        boolean bookDoesNotExit;

        do {
            bookDoesNotExit = false;
            // input title of the book to edit
            System.out.println("Title of the book to edit: ");
            String bookName = input.nextLine();
            Map<Integer, Books> mapBooks = booksByName(bookName);

            if (mapBooks.size() == -1) {
                System.out.printf("\nBook with this title is not present in the Library\n");
                System.out.println("Digit:\n1 - to enter another title\n2 - to proceed\n");
                int x = input.nextInt();
                if (x == 1)
                    bookDoesNotExit = true;

            } else if (mapBooks.size() == 1) {

                int id = mapBooks.get(1).getBookId();
                String name = mapBooks.get(1).getBookName();
                String author = mapBooks.get(1).getBookAuthor();

                System.out.printf("For \"%s\" of %s", name, author);
                System.out.println("\nNew title:");
                String newName = input.nextLine();
                System.out.printf("\nNew author:");
                String newAuthor = input.nextLine();
                if(newAuthor.equals(""))
                    newAuthor = "Unknown";

                prepStatUpdateBook.setString(1, newName);
                prepStatUpdateBook.setString(2, newAuthor);
                prepStatUpdateBook.setInt(3, id);
                prepStatUpdateBook.execute();

                System.out.printf("\nThe book was updated!");

            } else if (mapBooks.size() > 1) {
                System.out.println("There are more than one book with this title." +
                        "\nPlease select the one you want to edit:");

                for (Map.Entry<Integer, Books> entry : mapBooks.entrySet()) {
                    System.out.printf("%d - '%s' %s\n",
                            entry.getKey(),
                            entry.getValue().getBookName(),
                            entry.getValue().getBookAuthor());
                }

                boolean invalidValue;
                int bookNum;

                do {
                    bookNum = input.nextInt();
                    invalidValue = false;

                    if (!mapBooks.containsKey(bookNum)) {
                        System.out.println("\nYou entered an invalid value! Please try again.\n");
                        invalidValue = true;
                    }
                } while (invalidValue);

                int id = mapBooks.get(bookNum).getBookId();
                String name = mapBooks.get(bookNum).getBookName();
                String author = mapBooks.get(bookNum).getBookAuthor();


                System.out.printf("For \"%s\" of %s", name, author);
                System.out.println("\nNew title:");
                // Skip the newline
                input.nextLine();
                String newName = input.nextLine();
                System.out.printf("\nNew author:");
                String newAuthor = input.nextLine();

                prepStatUpdateBook.setString(1, newName);
                prepStatUpdateBook.setString(2, newAuthor);
                prepStatUpdateBook.setInt(3, id);
                prepStatUpdateBook.execute();

                System.out.printf("\nThe book was updated!");
            }
        } while (bookDoesNotExit);
    }

    // console.log all the books in the DB ordered by name
    public void showAllBooks() throws SQLException, NullPointerException {
        prepStatAllBooksOrderedByName = controller.getConnection().prepareStatement(
                "SELECT * FROM books ORDER BY name");
        ResultSet orderedByNameBooks = prepStatAllBooksOrderedByName.executeQuery();
        int counter = 1;
        while (orderedByNameBooks.next()) {
            System.out.printf("%d - \"%s\" %s\n", counter,
                    orderedByNameBooks.getString("name"), orderedByNameBooks.getString("author"));
            counter++;
        }
    }

    public void go() throws SQLException {

        input = new Scanner(System.in);

        System.out.printf("WELCOME TO THE LIBRARY\n" +
                "------------------------------------\n" +
                "To add a book in the library, digit \"add\"\n" +
                "To delete from the library, digit \"remove\"\n" +
                "To edit an existing book, digit \"edit\"\n" +
                "To show all present books, digit \"show\"\n" +
                "To exit, digit \"exit\"\n" +
                "------------------------------------\n\n");
        do {
            System.out.println("\nDigit your command:");
            String value = input.nextLine();

            if (value.equals("add")) {
                addBook();
            } else if (value.equals("remove")) {
                deleteBook();
                } else if (value.equals("edit")) {
                    editBook();
                    } else if (value.equals("show")) {
                        showAllBooks();
                        } else if (value.equals("exit")) {
                            System.exit(0);
                            } else {
                                System.out.printf("\n----Wrong request, please try again.----\n");
                            }
        } while (true);
    }
}
