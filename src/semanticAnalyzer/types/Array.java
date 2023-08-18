package semanticAnalyzer.types;

public class Array implements Type {

	private Type subType;
	// add other fields?
	//private boolean isReference;
	
	public Array(Type type) {
		this.subType = type;
	}

	public Type getSubType() {
		return subType;
	}
	
	@Override
	public int getSize() {
		return 4; // size of an address
	}

	@Override
	public String infoString() {
		return toString();
	}
	
	@Override
	public String toString() {
		return "array, " + subType;
	}

	@Override
	public boolean equivalent(Type otherType) {
		if(otherType instanceof Array) {
			Array otherArray = (Array)otherType;
			return subType.equivalent(otherArray.getSubType());
		}
		return false;
	}

	@Override
	public Type getConcerteType() {
		Type concreteSubtype = subType.getConcerteType();
		return new Array(concreteSubtype);
	}
	
	@Override
	public boolean isReferenceType() {
		return true;
	}
	
}
