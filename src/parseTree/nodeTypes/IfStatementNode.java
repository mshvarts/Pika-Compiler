package parseTree.nodeTypes;

import lexicalAnalyzer.Keyword;
import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import tokens.Token;

public class IfStatementNode extends ParseNode {

	public IfStatementNode(Token token) {
		super(token);
		assert(token.isLextant(Keyword.IF));
	}
	public IfStatementNode(ParseNode node) {
		super(node);
	}
	
	////////////////////////////////////////////////////////////
	// no attributes

	
	///////////////////////////////////////////////////////////
	// boilerplate for visitors
	
	public void accept(ParseNodeVisitor visitor) {
		visitor.visitEnter(this);
		visitChildren(visitor);
		visitor.visitLeave(this);
	}
}
