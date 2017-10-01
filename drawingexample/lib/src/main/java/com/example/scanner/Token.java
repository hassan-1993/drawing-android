package com.example.scanner;

public class Token {
	
	// ============================================================
	// Variables
	// ============================================================
	private String sequence;
	private TokenID	tokenID;
	
	// ============================================================
	// Constructors
	// ============================================================
	public Token(TokenID tokenID, String sequence){
		this.tokenID = tokenID;
		this.sequence = sequence;
	}
	
	// ============================================================
    // Methods
    // ============================================================
	public Token clone(){
		return new Token(tokenID, sequence);
	}
	
	// ============================================================
	// GETTERS / SETTERS
	// ============================================================
	public TokenID getTokenID(){
		return tokenID;
	}
	
	public String getSequence(){
		return sequence;
	}
}
