package parseTree.nodeTypes;

import lexicalAnalyzer.Keyword;
import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import tokens.Token;

public class BreakStatementNode extends ParseNode {
	ParseNode loopNode;
	
	public BreakStatementNode(Token token) {
		super(token);
		assert(token.isLextant(Keyword.BREAK));
	}
	public BreakStatementNode(ParseNode node) {
		super(node);
	}
	
	////////////////////////////////////////////////////////////
	// attributes
	
	public ParseNode getLoopNode() {
		return loopNode;
	}
	
	public void setLoopNode(ParseNode node) {
		loopNode = node;
	}

	///////////////////////////////////////////////////////////
	// boilerplate for visitors
	
	public void accept(ParseNodeVisitor visitor) {
		visitor.visit(this);
	}
}
