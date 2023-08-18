package semanticAnalyzer.types;

//import asmCodeGenerator.codeStorage.ASMOpcode;

public class TypeVariable implements Type {
	private String name;
	private Type typeConstraint;
	
	public TypeVariable(String name) {
		this.name = name;
		this.typeConstraint = PrimitiveType.NO_TYPE;
	}

	private void setTypeConstraint(Type typeConstraint) {
		this.typeConstraint = typeConstraint;
	}
	
	private Type getTypeConstraint() {
		return this.typeConstraint;
	}
	
	public void reset() {
		this.typeConstraint = PrimitiveType.NO_TYPE;
	}
	
	@Override
	public int getSize() {
		return typeConstraint.getSize();
	}

	@Override
	public String infoString() {
		return toString();
	}
	
	public String pikaNativeString() {
		return toString();
	}
	
	public String toString() {
		return "<" + getName() + ">";
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equivalent(Type otherType) {
		if(otherType instanceof TypeVariable) {
			throw new RuntimeException("equivalent attempted on two types containing type variables.");
		}
		if(this.getTypeConstraint() == PrimitiveType.NO_TYPE) {
			setTypeConstraint(otherType);
			return true;
		}
		return this.getTypeConstraint().equivalent(otherType);
	}

	@Override
	public Type getConcerteType() {
		return getTypeConstraint().getConcerteType();
	}
	
	@Override
	public boolean isReferenceType() {
		return getConcerteType().isReferenceType();
	}

}
