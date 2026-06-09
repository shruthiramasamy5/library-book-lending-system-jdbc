package com.library.dao;
import java.sql.*;
import java.util.*;
import com.library.bean.Book;
import com.library.util.DBUtil;
public class BookDAO {
	public Book findBook(String bookID){
	    Book b=null;
	    try {
	        Connection con=DBUtil.getDBConnection();
	        String q="SELECT*FROM BOOK_TBL WHERE BOOK_ID=?";
	        PreparedStatement ps=con.prepareStatement(q);
	        ps.setString(1,bookID);
	        ResultSet rs=ps.executeQuery();
	        if(rs.next()){
	            b=new Book();
	            b.setBookID(rs.getString("BOOK_ID"));
	            b.setTitle(rs.getString("TITLE"));
	            b.setAuthor(rs.getString("AUTHOR"));
	            b.setTotalCopies(rs.getInt("TOTAL_COPIES"));
	            b.setAvailableCopies(rs.getInt("AVAILABLE_COPIES"));
	        }
	    } catch(Exception e){
	        e.printStackTrace();
	    }
	    return b;
	}

public List<Book> viewAllBooks(){
    List<Book> bookList=new ArrayList<Book>();
    try {
        Connection con=DBUtil.getDBConnection();
        String q="SELECT*FROM BOOK_TBL";
        PreparedStatement ps=con.prepareStatement(q);
        ResultSet rs=ps.executeQuery();
        while(rs.next()) {
            Book b = new Book();
            b.setBookID(rs.getString("BOOK_ID"));
            b.setTitle(rs.getString("TITLE"));
            b.setAuthor(rs.getString("AUTHOR"));
            b.setTotalCopies(rs.getInt("TOTAL_COPIES"));
            b.setAvailableCopies(rs.getInt("AVAILABLE_COPIES"));
            bookList.add(b);
        }
    } catch(Exception e) {
        e.printStackTrace();
    }
    return bookList;
}
public boolean insertBook(Book book) {
    boolean flag = false;
    try {
        Connection con =DBUtil.getDBConnection();
        String q ="INSERT INTO BOOK_TBL "+ "VALUES(?,?,?,?,?)";
        PreparedStatement ps =con.prepareStatement(q);
        ps.setString(1,book.getBookID());
        ps.setString(2,book.getTitle());
        ps.setString(3,book.getAuthor());
        ps.setInt(4,book.getTotalCopies());
        ps.setInt(5,book.getAvailableCopies());
        int rows =ps.executeUpdate();
        if(rows > 0) {
            flag = true;
        }
    } catch(Exception e) {
        e.printStackTrace();
    }
    return flag;
}
public boolean updateAvailableCopies(Connection con,String bookID,int newCount) {
    boolean flag = false;
    try {
        String q ="UPDATE BOOK_TBL "+ "SET AVAILABLE_COPIES=? "+ "WHERE BOOK_ID=?";
        PreparedStatement ps =con.prepareStatement(q);
        ps.setInt(1, newCount);
        ps.setString(2, bookID);
        int rows =ps.executeUpdate();
        if(rows > 0) {
            flag = true;
        }
    } catch(Exception e) {
        e.printStackTrace();
    }
    return flag;
}
public boolean deleteBook(String bookID) {
    boolean flag = false;
    try {
        Connection con=DBUtil.getDBConnection();
        String q="DELETE FROM BOOK_TBL "+ "WHERE BOOK_ID = ?";
        PreparedStatement ps=con.prepareStatement(q);
        ps.setString(1, bookID);
        int rows=ps.executeUpdate();
        if(rows>0) {
            flag=true;
        }
    } catch(Exception e) {
        e.printStackTrace();
    }
    return flag;
}
}