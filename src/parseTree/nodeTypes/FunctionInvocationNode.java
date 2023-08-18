package parseTree.nodeTypes;

import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import tokens.Token;

public class FunctionInvocationNode extends ParseNode {
	private ParseNode lambdaNode;
	
	public FunctionInvocationNode(Token token) {
		super(token);
	}
	public FunctionInvocationNode(ParseNode node) {
		super(node);
	}
	
	////////////////////////////////////////////////////////////
	
	public ParseNode getLambdaNode() {
		return lambdaNode;
	}
	public void setLambdaNode(ParseNode lambdaNode) {
		this.lambdaNode = lambdaNode;
	}
	
	/*public ParseNode getFunctionDefinition() {
		return functionDefinition;
	}
	public void setFunctionDefinition(ParseNode functionDefinition) {
		this.functionDefinition = functionDefinition;
	}*/
	
	///////////////////////////////////////////////////////////
	// boilerplate for visitors
	
	public void accept(ParseNodeVisitor visitor) {
		visitor.visitEnter(this);
		visitChildren(visitor);
		visitor.visitLeave(this);
	}
	
}
