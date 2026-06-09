package com.library.util;

public class BookUnavailableException extends Exception{
@Override
public String toString() {
	return "Book is not Available for Issue";
}
}
