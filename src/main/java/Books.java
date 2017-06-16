
public class Books {

    private String bookName;
    private String bookAuthor;
    private int bookId;



    //Constructor handles unknown author
    public Books(int id, String name, String author){
        setBookId(id);
        setBookName(name);

        if(author == null || author.equals(""))
            setBookAuthor("Unknown");
        else
            setBookAuthor(author);
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\nAuthor: %s\n", bookName, bookAuthor);
    }
}
