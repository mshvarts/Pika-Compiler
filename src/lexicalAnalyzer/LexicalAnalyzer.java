package lexicalAnalyzer;


import logging.PikaLogger;

import inputHandler.InputHandler;
import inputHandler.LocatedChar;
import inputHandler.LocatedCharStream;
import inputHandler.PushbackCharStream;
import inputHandler.TextLocation;
import tokens.IdentifierToken;
import tokens.LextantToken;
import tokens.NullToken;
import tokens.StringToken;
import tokens.IntegerToken;
import tokens.CharacterToken;
import tokens.FloatingToken;
import tokens.Token;
import tokens.TypeToken;

import static lexicalAnalyzer.PunctuatorScanningAids.*;

public class LexicalAnalyzer extends ScannerImp implements Scanner {
	public static LexicalAnalyzer make(String filename) {
		InputHandler handler = InputHandler.fromFilename(filename);
		PushbackCharStream charStream = PushbackCharStream.make(handler);
		return new LexicalAnalyzer(charStream);
	}

	public LexicalAnalyzer(PushbackCharStream input) {
		super(input);
	}

	
	//////////////////////////////////////////////////////////////////////////////
	// Token-finding main dispatch	

	@Override
	protected Token findNextToken() {
		LocatedChar ch = nextNonWhitespaceChar();
		
		if(ch.isNumberStart(input)) {
			return scanNumber(ch);
		}
		else if(ch.isIdentifierStart()) {
			return scanIdentifier(ch);
		}
		else if(ch.isCommentStart()) {
			return scanComment(ch);
		}
		else if(ch.isCharacterStart()) {
			return scanCharacter(ch);
		}
		/*else if(ch.isTypeStart()) {
			return scanType(ch);
		}*/
		else if(ch.isStringStart()) {
			return scanString(ch);
		}
		else if(isPunctuatorStart(ch)) {
			return PunctuatorScanner.scan(ch, input);
		}
		else if(isEndOfInput(ch)) {
			return NullToken.make(ch.getLocation());
		}
		else {
			lexicalError(ch);
			return findNextToken();
		}
	}

	private LocatedChar nextNonWhitespaceChar() {
		LocatedChar ch = input.next();
		while(ch.isWhitespace()) {
			ch = input.next();
		}
		return ch;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////
	// Comment lexical analysis	
	
	private Token scanComment(LocatedChar firstChar) {
		StringBuffer buffer = new StringBuffer();
		if(!firstChar.isCommentStart()) {
			PikaLogger log = PikaLogger.getLogger("compiler.lexicalAnalyzer");
			log.severe("Lexical error: expected a comment start token");
		}
		appendSubsequentComment(buffer);
		return findNextToken();
	}
	private void appendSubsequentComment(StringBuffer buffer) {
		LocatedChar c = input.next();
		while(c.isSubsequentComment()) {
			buffer.append(c.getCharacter());
			c = input.next();
		}
		if(!c.isCommentEnd()) {
			PikaLogger log = PikaLogger.getLogger("compiler.lexicalAnalyzer");
			log.severe("Lexical error: did not find end of comment token");
		}
	}

	//////////////////////////////////////////////////////////////////////////////
	// String lexical analysis	
	
	private Token scanString(LocatedChar firstChar) {
		StringBuffer buffer = new StringBuffer();
		if(!firstChar.isStringStart()) {
			PikaLogger log = PikaLogger.getLogger("compiler.lexicalAnalyzer");
			log.severe("Lexical error: expected a string start token");
		}
		appendSubsequentString(buffer);
		String lexeme = buffer.toString();
		return StringToken.make(firstChar.getLocation(), lexeme);
	}
	private void appendSubsequentString(StringBuffer buffer) {
		LocatedChar c = input.next();
		while(c.isSubsequentString()) {
			buffer.append(c.getCharacter());
			c = input.next();
		}
		if(!c.isStringEnd()) {
			PikaLogger log = PikaLogger.getLogger("compiler.lexicalAnalyzer");
			log.severe("Lexical error: did not find end of string token");
		}
	}

	//////////////////////////////////////////////////////////////////////////////
	// Casting lexical analysis	
	
	/*private Token scanType(LocatedChar firstChar) {
		StringBuffer buffer = new StringBuffer();
		if(!firstChar.isTypeStart()) {
			PikaLogger log = PikaLogger.getLogger("compiler.lexicalAnalyzer");
			log.severe("Lexical error: expected a type start token");
		}
		appendSubsequentType(buffer);
		String lexeme = buffer.toString();
		return TypeToken.make(firstChar.getLocation(), lexeme);
	}
	private void appendSubsequentType(StringBuffer buffer) {
		LocatedChar c = input.next();
		while(!c.isWhitespace()) {
			buffer.append(c.getCharacter());
			c = input.next();
		}
		c = nextNonWhitespaceChar();
		if(!c.getCharacter().toString().equals(Punctuator.CAST.getLexeme())) {
			PikaLogger log = PikaLogger.getLogger("compiler.lexicalAnalyzer");
			log.severe("Lexical error: did not find verticle bar casting token");
		}
		c = nextNonWhitespaceChar();
		while(!c.isCastingEnd() && !c.isWhitespace()) {
			buffer.append(c.getCharacter());
			c = input.next();
		}
		if(!c.isTypeEnd()) {
			PikaLogger log = PikaLogger.getLogger("compiler.lexicalAnalyzer");
			log.severe("Lexical error: did not find end of casting token");
		}
	}*/

	//////////////////////////////////////////////////////////////////////////////
	// Character lexical analysis	
	
	private Token scanCharacter(LocatedChar firstChar) {
		StringBuffer buffer = new StringBuffer();
		if(!firstChar.isCharacterStart()) {
			PikaLogger log = PikaLogger.getLogger("compiler.lexicalAnalyzer");
			log.severe("Lexical error: expected a character start token");
		}
		appendSubsequentCharacter(buffer);
		String lexeme = buffer.toString();
		return CharacterToken.make(firstChar.getLocation(), lexeme);
	}
	private void appendSubsequentCharacter(StringBuffer buffer) {
		LocatedChar c = input.next();
		while(c.isSubsequentCharacter()) {
			buffer.append(c.getCharacter());
			c = input.next();
		}
		if(!c.isCharacterEnd()) {
			PikaLogger log = PikaLogger.getLogger("compiler.lexicalAnalyzer");
			log.severe("Lexical error: did not find end of character token");
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	// Integer lexical analysis	

	private Token scanNumber(LocatedChar firstChar) {
		StringBuffer buffer = new StringBuffer();
		Character DECIMAL_POINT = '.';
		Character E_NOTATION = 'E';
		
		LocatedChar next = input.peek();
		if(firstChar.isNumericSign() && 
			!next.isDigit() && next.getCharacter() != DECIMAL_POINT ) {
			return PunctuatorScanner.scan(firstChar, input);
		}
		
		buffer.append(firstChar.getCharacter());
		
		if(firstChar.isDigit() || firstChar.isNumericSign()) {
			appendSubsequentDigits(buffer);
			next = input.next();
			LocatedChar secondNext = input.peek();
			boolean isAnInteger = next.getCharacter() != DECIMAL_POINT || 
					(!secondNext.isDigit() && secondNext.getCharacter() != DECIMAL_POINT);
			if(isAnInteger) {
				input.pushback(next);
				return IntegerToken.make(firstChar.getLocation(), buffer.toString());
			}
			buffer.append(next.getCharacter());
		}
		// either way through if, we've just added '.' to buffer.
		// check if there is at least one digit after decimal 
		next = input.peek();
		if(!next.isDigit()) {
			//throw new IllegalArgumentException("Lexical error: did not find digits after decimal point in float");
			PikaLogger log = PikaLogger.getLogger("compiler.lexicalAnalyzer");
			log.severe("Lexical error: did not find digits after decimal point in float");
		}
		appendSubsequentDigits(buffer);
		// scan E notation
		next = input.peek();
		if(next.getCharacter() == E_NOTATION) {
			next = input.next();
			buffer.append(next.getCharacter());
			
			next = input.peek();
			if(next.isNumericSign()) {
				next = input.next();
				buffer.append(next.getCharacter());
			}
			appendSubsequentDigits(buffer);
		}
		return FloatingToken.make(firstChar.getLocation(), buffer.toString());
	}
	private void appendSubsequentDigits(StringBuffer buffer) {
		LocatedChar c = input.next();
		while(c.isDigit()) {
			buffer.append(c.getCharacter());
			c = input.next();
		}
		input.pushback(c);
	}
	
	
	//////////////////////////////////////////////////////////////////////////////
	// Identifier and keyword lexical analysis	

	private Token scanIdentifier(LocatedChar firstChar) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(firstChar.getCharacter());
		appendSubsequentIdentifier(buffer);

		String lexeme = buffer.toString();
		if(Keyword.isAType(lexeme)){
			return TypeToken.make(firstChar.getLocation(), lexeme);
		}
		else if(Keyword.isAKeyword(lexeme)) {
			return LextantToken.make(firstChar.getLocation(), lexeme, Keyword.forLexeme(lexeme));
		}
		else { // is identifier
			if(lexeme.length() > 32) {
				PikaLogger log = PikaLogger.getLogger("compiler.lexicalAnalyzer");
				log.severe("Lexical error: identifier length can be at most 32 characters");
			}
			return IdentifierToken.make(firstChar.getLocation(), lexeme);
		}
	}
	private void appendSubsequentIdentifier(StringBuffer buffer) {
		LocatedChar c = input.next();
		while(c.isSubsequentIdentifier()) {
			buffer.append(c.getCharacter());
			c = input.next();
		}
		input.pushback(c);
	}
	
	
	//////////////////////////////////////////////////////////////////////////////
	// Punctuator lexical analysis	
	// old method left in to show a simple scanning method.
	// current method is the algorithm object PunctuatorScanner.java

	@SuppressWarnings("unused")
	private Token oldScanPunctuator(LocatedChar ch) {
		TextLocation location = ch.getLocation();
		
		switch(ch.getCharacter()) {
		case '*':
			return LextantToken.make(location, "*", Punctuator.MULTIPLY);
		case '+':
			return LextantToken.make(location, "+", Punctuator.ADD);
		case '>':
			return LextantToken.make(location, ">", Punctuator.GREATER);
		case ':':
			if(ch.getCharacter()=='=') {
				return LextantToken.make(location, ":=", Punctuator.ASSIGN);
			}
			else {
				throw new IllegalArgumentException("found : not followed by = in scanOperator");
			}
		case ',':
			return LextantToken.make(location, ",", Punctuator.SEPARATOR);
		case ';':
			return LextantToken.make(location, ";", Punctuator.TERMINATOR);
		default:
			throw new IllegalArgumentException("bad LocatedChar " + ch + "in scanOperator");
		}
	}

	

	//////////////////////////////////////////////////////////////////////////////
	// Character-classification routines specific to Pika scanning.	

	private boolean isPunctuatorStart(LocatedChar lc) {
		char c = lc.getCharacter();
		return isPunctuatorStartingCharacter(c);
	}

	private boolean isEndOfInput(LocatedChar lc) {
		return lc == LocatedCharStream.FLAG_END_OF_INPUT;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////
	// Error-reporting	

	private void lexicalError(LocatedChar ch) {
		PikaLogger log = PikaLogger.getLogger("compiler.lexicalAnalyzer");
		log.severe("Lexical error: invalid character " + ch);
	}

	
}
