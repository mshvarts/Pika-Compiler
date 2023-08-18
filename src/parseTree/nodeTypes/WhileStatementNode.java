package parseTree.nodeTypes;

import lexicalAnalyzer.Keyword;
import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import tokens.Token;

public class WhileStatementNode extends ParseNode {
	String breakLabel;
	String continueLabel;
	
	public WhileStatementNode(Token token) {
		super(token);
		assert(token.isLextant(Keyword.WHILE));
	}
	public WhileStatementNode(ParseNode node) {
		super(node);
	}
	
	////////////////////////////////////////////////////////////
	// Getters and setters
	
	public void setBreakLabel(String breakLabel) {
		this.breakLabel = breakLabel;
	}
	
	public void setContinueLabel(String continueLabel) {
		this.continueLabel = continueLabel;
	}
	
	public String getBreakLabel() {
		return breakLabel;
	}
	
	public String getContinueLabel() {
		return continueLabel;
	}
	
	////////////////////////////////////////////////////////////
	// convenience factory
	
	public static WhileStatementNode withChildren(Token token, ParseNode declaredName, ParseNode initializer) {
		WhileStatementNode node = new WhileStatementNode(token);
		node.appendChild(declaredName);
		node.appendChild(initializer);
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
