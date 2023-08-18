package parseTree.nodeTypes;

import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import lexicalAnalyzer.Keyword;
import tokens.Token;

public class CallStatementNode extends ParseNode {

	public CallStatementNode(Token token) {
		super(token);
		assert(token.isLextant(Keyword.CALL));
	}

	public CallStatementNode(ParseNode node) {
		super(node);
	}
	
	///////////////////////////////////////////////////////////
	// boilerplate for visitors
			
	public void accept(ParseNodeVisitor visitor) {
		visitor.visitEnter(this);
		visitChildren(visitor);
		visitor.visitLeave(this);
	}
}
