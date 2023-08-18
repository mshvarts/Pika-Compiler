package parseTree.nodeTypes;


import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import tokens.Token;

public class StringSliceNode extends ParseNode {
	public StringSliceNode(Token token) {
		super(token);
	}
	public StringSliceNode(ParseNode node) {
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
