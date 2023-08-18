package parseTree.nodeTypes;

import lexicalAnalyzer.Punctuator;
import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import tokens.Token;

public class LambdaNode extends ParseNode {

	//private List<Type> parameters;
	
	public LambdaNode(Token token) {
		super(token);
		assert(token.isLextant(Punctuator.LESS));
	}
	public LambdaNode(ParseNode node) {
		super(node);
	}
	
	////////////////////////////////////////////////////////////
	// no attributes
	
	/*public void addParam(Type parameter) {
		parameters.add(parameter);
	}
	
	public List<Type> getParams() {
		return parameters;
	}*/
	
	///////////////////////////////////////////////////////////
	// boilerplate for visitors
	
	public void accept(ParseNodeVisitor visitor) {
		visitor.visitEnter(this);
		visitChildren(visitor);
		visitor.visitLeave(this);
	}
	
}
