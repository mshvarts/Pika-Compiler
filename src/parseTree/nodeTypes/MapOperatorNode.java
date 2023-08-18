package parseTree.nodeTypes;

import lexicalAnalyzer.Keyword;
import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import tokens.Token;

public class MapOperatorNode extends ParseNode {
	private ParseNode lambdaNode;
	
	public MapOperatorNode(Token token) {
		super(token);
		assert(token.isLextant(Keyword.MAP));
	}

	public MapOperatorNode(ParseNode node) {
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
	
	public static MapOperatorNode withChildren(Token token, ParseNode left, ParseNode right) {
		MapOperatorNode node = new MapOperatorNode(token);
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
