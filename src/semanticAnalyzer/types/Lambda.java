package semanticAnalyzer.types;

import java.util.List;

public class Lambda implements Type {

	private List<Type> parameters;
	private Type returnType;
	
	public Lambda(List<Type> parameters, Type returnType) {
		this.parameters = parameters;
		this.returnType = returnType;
	}

	public Type getReturnType() {
		return returnType;
	}
	
	public List<Type> getParameters() {
		return parameters;
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
		return "lambda, " + parameters + " -> " + returnType;
	}

	@Override
	public boolean equivalent(Type otherType) {
		if(otherType instanceof Lambda) {
			Lambda otherLambda = (Lambda)otherType;
			boolean paramsEqual = true;
			for(int i = 0; i < this.getParameters().size(); i++) {
				Type param = this.getParameters().get(i);
				if(!param.equivalent(otherLambda.getParameters().get(i))) {
					paramsEqual = false;
					break;
				}
			}
			return this.returnType.equivalent(otherLambda.getReturnType()) 
					&& paramsEqual;
		}
		return false;
	}

	@Override
	public Type getConcerteType() {
		return null;
	}

	@Override
	public boolean isReferenceType() { // reference to a piece of code
		return true;
	}
	
}
