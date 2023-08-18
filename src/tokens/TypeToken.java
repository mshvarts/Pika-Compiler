package tokens;

import inputHandler.TextLocation;

public class TypeToken extends TokenImp {
	
	protected TypeToken(TextLocation location, String lexeme) {
		super(location, lexeme);
	}
	public static TypeToken make(TextLocation location, String lexeme) {
		TypeToken result = new TypeToken(location, lexeme);
		return result;
	}
	
	@Override
	protected String rawString() {
		return "type, " + getLexeme();
	}
}
