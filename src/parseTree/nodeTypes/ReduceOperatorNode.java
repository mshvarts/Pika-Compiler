package parseTree.nodeTypes;

import lexicalAnalyzer.Keyword;
import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import tokens.Token;

public class ReduceOperatorNode extends ParseNode {
	private ParseNode lambdaNode;
	
	public ReduceOperatorNode(Token token) {
		super(token);
		assert(token.isLextant(Keyword.REDUCE));
	}

	public ReduceOperatorNode(ParseNode node) {
		super(node);
	}
	
	////////////////////////////////////////////////////////////
		
	public ParseNode getLambdaNode() {
		return lambdaNode;
	}
	public void setLambdaNode(ParseNode lambdaNode) {
		this.lambdaNode = lambdaNode;
	}

	////////////////////////////////////////////////////////////
	// convenience factory
	
	public static ReduceOperatorNode withChildren(Token token, ParseNode left, ParseNode right) {
		ReduceOperatorNode node = new ReduceOperatorNode(token);
		node.appendChild(left);
		node.appendChild(right);
		return node;
	}
	
	
	///////////////////////////////////////////////////////////
	// boilerplate for visitors
			
	public void accept(ParseNodeVisitor visitor) {
		visitor.visitEnter(this);
		visitChildren(visitor);
		visitor.visitLeave(this);
	}
}
