package com.library.util;

public class ActiveIssueException extends Exception {
@Override

public String toString() {
return"Cannot delete book.Active issue record exist";
}
}
