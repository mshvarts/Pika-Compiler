package parseTree.nodeTypes;

import lexicalAnalyzer.Keyword;
import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import tokens.Token;

public class FoldOperatorNode extends ParseNode {
	private ParseNode lambdaNode;
	
	public FoldOperatorNode(Token token) {
		super(token);
		assert(token.isLextant(Keyword.FOLD));
	}

	public FoldOperatorNode(ParseNode node) {
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
	
	public static FoldOperatorNode withChildren(Token token, ParseNode left, ParseNode right) {
		FoldOperatorNode node = new FoldOperatorNode(token);
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
