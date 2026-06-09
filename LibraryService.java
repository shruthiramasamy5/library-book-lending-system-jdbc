package com.library.service;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import com.library.bean.*;
import com.library.dao.*;
import com.library.util.*;
public class LibraryService {
    private BookDAO bookDAO = new BookDAO();
    private IssueDAO issueDAO = new IssueDAO();
public Book viewBookDetails(String bookID) {
        if(bookID==null||bookID.trim().isEmpty()) {
            return null;
        }
        return bookDAO.findBook(bookID);
    }
public List<Book> viewAllBooks() {
    return bookDAO.viewAllBooks();
}
public boolean addNewBook(Book book)throws ValidationException {
    if(book == null) {
        throw new ValidationException();
    }
    if(book.getBookID() == null ||book.getBookID().trim().isEmpty()) {
        throw new ValidationException();
    }
    if(book.getTitle() == null ||book.getTitle().trim().isEmpty()) {
        throw new ValidationException();
    }
    if(book.getAuthor() == null ||book.getAuthor().trim().isEmpty()) {
        throw new ValidationException();
    }
    if(book.getTotalCopies() <= 0 ||book.getAvailableCopies() < 0) {
        throw new ValidationException();
    }
    Book existing =bookDAO.findBook(book.getBookID());
    if(existing != null) {
        return false;
    }
    return bookDAO.insertBook(book);
}
public boolean hasActiveIssue(String bookID) {
    boolean flag = false;
    try {
        Connection con =DBUtil.getDBConnection();
        String q ="SELECT COUNT(*) FROM ISSUE_TBL "+ "WHERE BOOK_ID=? "+ "AND RETURN_DATE IS NULL";
        PreparedStatement ps =con.prepareStatement(q);
        ps.setString(1, bookID);
        ResultSet rs =ps.executeQuery();
        if(rs.next()) {
            if(rs.getInt(1) > 0) {
                flag = true;
            }
        }
    } catch(Exception e) {
        e.printStackTrace();
    }
    return flag;
}
public boolean issueBook(String bookID,String studentID,String studentName)throws ValidationException,BookUnavailableException {
    if(bookID == null ||studentID == null ||studentName == null ||bookID.trim().isEmpty() ||studentID.trim().isEmpty() ||studentName.trim().isEmpty()) {
        throw new ValidationException();
    }
    Book book =bookDAO.findBook(bookID);
    if(book == null) {
        return false;
    }
    if(book.getAvailableCopies() == 0) {
        throw new BookUnavailableException();
    }
    Connection con = null;
    try {
        con = DBUtil.getDBConnection();
        con.setAutoCommit(false);
        int updatedCopies =book.getAvailableCopies() - 1;
        boolean b1 =bookDAO.updateAvailableCopies(con,bookID,updatedCopies);
        Issue issue = new Issue();
        issue.setIssueID(issueDAO.generateIssueID());
        issue.setBookID(bookID);
        issue.setStudentID(studentID);
        issue.setStudentName(studentName);
        issue.setIssueDate(Date.valueOf(LocalDate.now()));
        issue.setReturnDate(null);
        boolean b2 =issueDAO.recordIssue(con,issue);
        if(b1 && b2) {
            con.commit();
            return true;
        }
        con.rollback();
    } catch(Exception e) {
        try {
            if(con != null) {
                con.rollback();
            }
        } catch(Exception ex) {}

        e.printStackTrace();
    }
    return false;
}
public boolean returnBook(int issueID)
throws ValidationException {
    if(issueID <= 0) {
        throw new ValidationException();
    }
    Issue issue =issueDAO.findIssue(issueID);
    if(issue == null) {
        return false;
    }
    Book book =bookDAO.findBook(issue.getBookID());
    Connection con = null;
    try {
        con = DBUtil.getDBConnection();
        con.setAutoCommit(false);
        int updatedCopies =book.getAvailableCopies() + 1;
        boolean b1 =bookDAO.updateAvailableCopies(con,book.getBookID(),updatedCopies);
        boolean b2 =issueDAO.closeIssue(con,issueID);
        if(b1 && b2) {
            con.commit();
            return true;
        }
        con.rollback();
    } catch(Exception e) {
        try {
            if(con != null) {
                con.rollback();
            }
        } catch(Exception ex) {}
        e.printStackTrace();
    }
    return false;
}
}

