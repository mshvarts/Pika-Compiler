package parseTree.nodeTypes;

import lexicalAnalyzer.Keyword;
import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import tokens.Token;

public class ZipOperatorNode extends ParseNode {
	private ParseNode lambdaNode;
	
	public ZipOperatorNode(Token token) {
		super(token);
		assert(token.isLextant(Keyword.ZIP));
	}

	public ZipOperatorNode(ParseNode node) {
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
	
	public static ZipOperatorNode withChildren(Token token, ParseNode left, ParseNode right) {
		ZipOperatorNode node = new ZipOperatorNode(token);
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
