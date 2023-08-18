package lexicalAnalyzer;

import tokens.LextantToken;
import tokens.Token;


public enum Keyword implements Lextant {
	CONST("const"),
	VAR("var"),
	PRINT("print"),
	NEWLINE("_n_"),
	TAB("_t_"),
	TRUE("_true_"),
	FALSE("_false_"),
	CHAR("char"),
	FLOAT("float"),
	RATIONAL("rat"),
	STRING("string"),
	INT("int"),
	BOOL("bool"),
	EXEC("exec"),
	// milestone 2
	ARRAY("array"),
	IF("if"),
	ELSE("else"),
	WHILE("while"),
	LENGTH("length"),
	CLONE("clone"),
	ALLOC("alloc"),
	DEALLOC("dealloc"),
	// milestone 3
	FUNCTION("func"),
	NULL("null"), // only usable as lambda return type
	RETURN("return"), // return expr : returns null if expr is empty
	CALL("call"),  // use want to invoke lambdas that return null or dont want to store returned value
	BREAK("break"), 
	CONTINUE("continue"), 
	// milestone 4
	REVERSE("reverse"),
	FOR("for"),
	INDEX("index"),
	ELEM("elem"),
	OF("of"),
	MAP("map"),
	REDUCE("reduce"),
	FOLD("fold"),
	ZIP("zip"),
	STATIC("static"),

	NULL_KEYWORD("");
	private String lexeme;
	private Token prototype;
	
	
	private Keyword(String lexeme) {
		this.lexeme = lexeme;
		this.prototype = LextantToken.make(null, lexeme, this);
	}
	public String getLexeme() {
		return lexeme;
	}
	public Token prototype() {
		return prototype;
	}
	
	public static Keyword forLexeme(String lexeme) {
		for(Keyword keyword: values()) {
			if(keyword.lexeme.equals(lexeme)) {
				return keyword;
			}
		}
		return NULL_KEYWORD;
	}
	public static boolean isAKeyword(String lexeme) {
		return forLexeme(lexeme) != NULL_KEYWORD;
	}
	
	public static boolean isAType(String lexeme) {
		return forLexeme(lexeme) == CHAR || forLexeme(lexeme) == FLOAT || forLexeme(lexeme) == STRING ||
				forLexeme(lexeme) == INT || forLexeme(lexeme) == BOOL ||  forLexeme(lexeme) == RATIONAL || 
				forLexeme(lexeme) == NULL;
	}
	
	/*   the following hashtable lookup can replace the serial-search implementation of forLexeme() above. It is faster but less clear. 
	private static LexemeMap<Keyword> lexemeToKeyword = new LexemeMap<Keyword>(values(), NULL_KEYWORD);
	public static Keyword forLexeme(String lexeme) {
		return lexemeToKeyword.forLexeme(lexeme);
	}
	*/
}
