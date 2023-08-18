package parseTree.nodeTypes;

import lexicalAnalyzer.Keyword;
import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import tokens.Token;

public class ForStatementNode extends ParseNode {
	String breakLabel;
	String continueLabel;
	int loopType;
	//int indexCounter;
	
	public ForStatementNode(Token token) {
		super(token);
		assert(token.isLextant(Keyword.FOR));
		//indexCounter = 0;
		loopType = 0;
	}
	public ForStatementNode(ParseNode node) {
		super(node);
	}
	
	////////////////////////////////////////////////////////////
	// Getters and setters
	
	/*public int getCounter() {
		return indexCounter;
	}
	
	public void incrementCounter() {
		this.indexCounter++;
	}*/
	
	public boolean isIndexLoop() {
		return loopType == 1;
	}
	
	public boolean isElementLoop() {
		return loopType == 2;
	}
	
	public void setToLoopIndexes() {
		this.loopType = 1;
	}
	
	public void setToLoopElements() {
		this.loopType = 2;
	}
	
	public void setBreakLabel(String breakLabel) {
		this.breakLabel = breakLabel;
	}
	
	public String getBreakLabel() {
		return breakLabel;
	}
	
	public void setContinueLabel(String continueLabel) {
		this.continueLabel = continueLabel;
	}
	
	public String getContinueLabel() {
		return continueLabel;
	}
	
	////////////////////////////////////////////////////////////
	// convenience factory
	
	public static ForStatementNode withChildren(Token token, ParseNode declaredName, ParseNode initializer) {
		ForStatementNode node = new ForStatementNode(token);
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
