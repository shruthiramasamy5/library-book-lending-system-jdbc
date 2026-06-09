package com.library.dao;
import java.sql.*;
import com.library.bean.Issue;
import com.library.util.DBUtil;
public class IssueDAO {
	public int generateIssueID() {
	    int id = 30001;
	    try {
	        Connection con =DBUtil.getDBConnection();
	        String qry ="SELECT NVL(MAX(ISSUE_ID),30000)+1 "+"FROM ISSUE_TBL";
	        PreparedStatement ps =con.prepareStatement(qry);
	        ResultSet rs =ps.executeQuery();
	        if(rs.next()) {
	            id = rs.getInt(1);
	        }
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    return id;
	}
	public boolean recordIssue(Connection con,Issue issue) {
	    boolean flag = false;
	    try {
	        String q ="INSERT INTO ISSUE_TBL "+ "VALUES(?,?,?,?,?,?)";
	        PreparedStatement ps =con.prepareStatement(q);
	        ps.setInt(1,issue.getIssueID());
	        ps.setString(2,issue.getBookID());
	        ps.setString(3,issue.getStudentID());
	        ps.setString(4,issue.getStudentName());
	        ps.setDate(5,(Date) issue.getIssueDate());
	        ps.setDate(6,(Date) issue.getReturnDate());
	        int rows =ps.executeUpdate();
	        if(rows > 0) {
	            flag = true;
	        }

	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    return flag;
	}
	public boolean closeIssue(Connection con,int issueID) {
	    boolean flag = false;
	    try {
	        String q ="UPDATE ISSUE_TBL "+ "SET RETURN_DATE = SYSDATE "+ "WHERE ISSUE_ID=?";
	        PreparedStatement ps =con.prepareStatement(q);
	        ps.setInt(1, issueID);
	        int rows =ps.executeUpdate();
	        if(rows > 0) {
	            flag = true;
	        }
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    return flag;
	}
	public Issue findIssue(int issueID) {
	    Issue issue = null;
	    try {
	        Connection con =DBUtil.getDBConnection();
	        String q="SELECT * FROM ISSUE_TBL "+ "WHERE ISSUE_ID = ? "+ "AND RETURN_DATE IS NULL";
	        PreparedStatement ps =con.prepareStatement(q);
	        ps.setInt(1, issueID);
	        ResultSet rs =ps.executeQuery();
	        if(rs.next()) {
	            issue = new Issue();
	            issue.setIssueID(rs.getInt("ISSUE_ID"));
	            issue.setBookID(rs.getString("BOOK_ID"));
	            issue.setStudentID(rs.getString("STUDENT_ID"));
	            issue.setStudentName(rs.getString("STUDENT_NAME"));
	            issue.setIssueDate(rs.getDate("ISSUE_DATE"));
	            issue.setReturnDate(rs.getDate("RETURN_DATE"));
	        }
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    return issue;
	}
	 public boolean hasActiveIssue(String bookID) {
	        boolean flag = false;
	        try {
	            Connection con =DBUtil.getDBConnection();
	            String q="SELECT COUNT(*) FROM ISSUE_TBL "+ "WHERE BOOK_ID = ? "+ "AND RETURN_DATE IS NULL";
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
	}

