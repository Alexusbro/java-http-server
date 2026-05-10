package ru.otus.java.basic.httpserver.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.java.basic.httpserver.db.DatabaseConnection;
import ru.otus.java.basic.httpserver.model.Book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private final DatabaseConnection dbConnection;
    private static final Logger logger = LogManager.getLogger(BookRepository.class.getName());

    public BookRepository(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<Book> findAllBook() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book_catalog";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getLong("id"),
                        rs.getString("author"),
                        rs.getString("title")
                ));
            }
        } catch (SQLException e) {
            logger.warn("error get book by id: {}", e.getMessage());
        }
        return books;
    }

    public Book getBookById(Long id) {
        String sql = "SELECT author, title FROM book_catalog WHERE id = ?";
        Book book = null;
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String author = rs.getString("author");
                    String title = rs.getString("title");
                    book = new Book(id, author, title);
                }
            }
        } catch (SQLException e) {
            logger.warn("failed find book by id {}: {}", id, e.getMessage());
        }
        return book;
    }


    public void saveBook(Book book) {
        String sql = "INSERT INTO book_catalog (author, title) VALUES(?, ?)";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, book.getAuthor());
            pstmt.setString(2, book.getTitle());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.warn("error insert book into DB: {}", e.getMessage());
        }
    }

    public void deleteBookById(long id) {
        String sql = "DELETE FROM book_catalog WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.warn("error delete book by id {} into DB: {}", id, e.getMessage());
        }
    }

    public void updateBookById(long id, Book book) {
        String sql = "UPDATE book_catalog SET author = ?, title = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, book.getAuthor());
            pstmt.setString(2, book.getTitle());
            pstmt.setLong(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.warn("error update book by id {}: {}", id, e.getMessage());
        }
    }
}
