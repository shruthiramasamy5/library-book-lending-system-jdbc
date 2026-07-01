# Library Book Lending System (JDBC)

A console-based Java application for managing library book inventory and student book issue/return, built using plain JDBC with Oracle Database and transaction-safe issue/return operations.

## Features
- Add new books with validation
- View a single book or list all books
- Issue a book to a student (with availability check and transactional stock update)
- Return a book (updates available copies and closes the issue record)
- Prevents deleting a book with an active (unreturned) issue

## Project Structure
com.library.bean     → Book, Issue (POJOs)
com.library.dao      → BookDAO, IssueDAO (raw JDBC data access)
com.library.service  → LibraryService (business logic, transaction handling)
com.library.util     → DBUtil (connection), custom exceptions
com.library.app       → LibraryMain (console entry point)

## Custom Exceptions
| Exception | Thrown When |
|-----------|-------------|
| `ValidationException` | Missing/invalid book or issue input |
| `BookUnavailableException` | No available copies to issue |
| `ActiveIssueException` | Attempting to delete a book with an active issue |

## Tech Stack
- Java
- JDBC (`java.sql`)
- Oracle Database (XE)

## Key Design Points
- **Transactional issue/return**: `issueBook()` and `returnBook()` use manual commit/rollback (`con.setAutoCommit(false)`) to keep the book's available-copy count and the issue record in sync.
- **Auto-generated Issue ID**: `IssueDAO.generateIssueID()` computes the next ID as `MAX(ISSUE_ID) + 1` (starting at 30001).
- **Active issue tracking**: An issue with `RETURN_DATE IS NULL` is considered active/unreturned.

## Database Setup

```sql
CREATE TABLE BOOK_TBL (
    BOOK_ID          VARCHAR2(10) PRIMARY KEY,
    TITLE            VARCHAR2(100),
    AUTHOR           VARCHAR2(100),
    TOTAL_COPIES     NUMBER,
    AVAILABLE_COPIES NUMBER
);

CREATE TABLE ISSUE_TBL (
    ISSUE_ID      NUMBER PRIMARY KEY,
    BOOK_ID       VARCHAR2(10),
    STUDENT_ID    VARCHAR2(10),
    STUDENT_NAME  VARCHAR2(100),
    ISSUE_DATE    DATE,
    RETURN_DATE   DATE,
    FOREIGN KEY (BOOK_ID) REFERENCES BOOK_TBL(BOOK_ID)
);
```

## Setup & Run
1. Create the Oracle user `library` (password `library123`) and the tables above
2. Update `DBUtil.java` if your DB URL/credentials differ
3. Compile:
javac com/library/bean/.java com/library/util/.java com/library/dao/.java com/library/service/.java com/library/app/*.java
4. Run:
java com.library.app.LibraryMain

## Sample Flow (as demonstrated in `LibraryMain.java`)
1. Issue book `BK101` to student `ST5001` (Meera Nair)
2. Return the book via issue ID `30001
