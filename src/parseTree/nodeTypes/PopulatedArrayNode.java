package parseTree.nodeTypes;


import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import tokens.Token;

public class PopulatedArrayNode extends ParseNode {
	public PopulatedArrayNode(Token token) {
		super(token);
	}
	public PopulatedArrayNode(ParseNode node) {
		super(node);
	}


///////////////////////////////////////////////////////////
// accept a visitor
	
	public void accept(ParseNodeVisitor visitor) {
		visitor.visitEnter(this);
		visitChildren(visitor);
		visitor.visitLeave(this);
	}
	
}
