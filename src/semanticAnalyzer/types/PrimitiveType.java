package semanticAnalyzer.types;

import tokens.Token;

public enum PrimitiveType implements Type {
	BOOLEAN(1),
	CHARACTER(1),
	INTEGER(4),
	FLOATING(8),
	STRING(4),
	RATIONAL(8),
	ERROR(0),			// use as a value when a syntax error has occurred
	NO_TYPE(0, "");		// use as a value when no type has been assigned.
	
	private int sizeInBytes;
	private String infoString;
	
	public static PrimitiveType fromToken(Token type) {
		switch(type.getLexeme()) {
			case "bool": 
				return BOOLEAN;
			case "char": 
				return CHARACTER;
			case "string": 
				return STRING;
			case "int": 
				return INTEGER;
			case "float": 
				return FLOATING;
			case "rat": 
				return RATIONAL;
			default:
				return NO_TYPE;	
		}
	}
	
	private PrimitiveType(int size) {
		this.sizeInBytes = size;
		this.infoString = toString();
	}
	private PrimitiveType(int size, String infoString) {
		this.sizeInBytes = size;
		this.infoString = infoString;
	}
	public int getSize() {
		return sizeInBytes;
	}
	public String infoString() {
		return infoString;
	}

	@Override
	public boolean equivalent(Type type) {
		return this == type;
	}

	@Override
	public Type getConcerteType() {
		return this;
	}
	
	@Override
	public boolean isReferenceType() {
		return (this == STRING);
	}
	
}
