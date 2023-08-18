package parseTree.nodeTypes;

import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import tokens.Token;

public class FunctionBodyNode extends ParseNode {
	private String startLabel;
	private String endLabel;
	private String exitLabel;
	
	public FunctionBodyNode(Token token) {
		super(token);
	}
	public FunctionBodyNode(ParseNode node) {
		super(node);
	}
	
	////////////////////////////////////////////////////////////
	// attributes

	public String getEndLabel() {
		return endLabel;
	}
	public void setEndLabel(String endLabel) {
		this.endLabel = endLabel;
	}
	public String getExitLabel() {
		return exitLabel;
	}
	public void setExitLabel(String endLabel) {
		this.exitLabel = endLabel;
	}
	public String getStartLabel() {
		return startLabel;
	}
	public void setStartLabel(String startLabel) {
		this.startLabel = startLabel;
	}
	
	///////////////////////////////////////////////////////////
	// boilerplate for visitors
	
	public void accept(ParseNodeVisitor visitor) {
		visitor.visitEnter(this);
		visitChildren(visitor);
		visitor.visitLeave(this);
	}
}
