package inputHandler;

/** Value object for holding a character and its location in the input text.
 *  Contains delegates to select character operations.
 *
 */
public class LocatedChar {
	Character character;
	TextLocation location;
	
	public LocatedChar(Character character, TextLocation location) {
		super();
		this.character = character;
		this.location = location;
	}

	
	//////////////////////////////////////////////////////////////////////////////
	// getters
	
	public Character getCharacter() {
		return character;
	}
	public TextLocation getLocation() {
		return location;
	}
	public boolean isChar(char c) {
		return character == c;
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////////
	// toString
	
	public String toString() {
		return "(" + charString() + ", " + location + ")";
	}
	private String charString() {
		if(Character.isWhitespace(character)) {
			int i = character;
			return String.format("'\\%d'", i);
		}
		else {
			return character.toString();
		}
	}

	
	//////////////////////////////////////////////////////////////////////////////
	// delegates
	
	public boolean isCommentStart() {
		return character == '#';
	}
	public boolean isCommentEnd() {
		return isCommentStart() || character == '\n';
	}
	public boolean isSubsequentComment() {
		return character != '#' && character != '\n';
	}
	public boolean isArrayTypeStart() {
		return character == '[';
	}
	public boolean isArrayTypeEnd() {
		return character == ']';
	}
	public boolean isCharacterStart() {
		return character == '^';
	}
	public boolean isCharacterEnd() {
		return isCharacterStart();
	}
	public boolean isSubsequentCharacter() {
		return character >= 32 && character <= 126 && !isCharacterStart();
	}
	public boolean isStringStart() {
		return character == '"';
	}
	public boolean isStringEnd() {
		return isStringStart();
	}
	public boolean isSubsequentString() {
		return character != '"' && character != '\n';
	}
	public boolean isIdentifierStart() {
		return Character.isLowerCase(character) || Character.isUpperCase(character) || character == '_';
	}
	public boolean isSubsequentIdentifier() {
		return Character.isLowerCase(character) || Character.isUpperCase(character) || 
				Character.isDigit(character) || character == '_' || character == '$';
	}
	public boolean isLowerCase() {
		return Character.isLowerCase(character) || (character == '_');
	}
	public boolean isDigit() {
		return Character.isDigit(character);
	}
	public boolean isWhitespace() {
		return Character.isWhitespace(character);
	}
	public boolean isNumberStart(PushbackCharStream input) {
		LocatedChar nextChar = input.peek();
		return Character.isDigit(character) || isNumericSign() || 
				(character == '.' && nextChar.isDigit());
	}
	public boolean isNumericSign() {
		return character == '+' || character == '-';
	}
}
