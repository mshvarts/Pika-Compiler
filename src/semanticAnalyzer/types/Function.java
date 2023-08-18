package semanticAnalyzer.types;

import java.util.List;

public class Function implements Type {

	private List<Type> parameters;
	private Type returnType;
	
	public Function(List<Type> types, Type returnType) {
		this.parameters = types;
		this.returnType = returnType;
	}

	public List<Type> getParameterTypes() {
		return parameters;
	}
	
	public Type getReturnType() {
		return returnType;
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
		return "function, " + parameters;
	}

	@Override
	public boolean isReferenceType() {
		return false;
	}

	@Override
	public boolean equivalent(Type Type) {
		return false;
	}

	@Override
	public Type getConcerteType() {
		return null;
	}
	
}
