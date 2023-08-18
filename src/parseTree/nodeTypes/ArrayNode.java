package parseTree.nodeTypes;

import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import semanticAnalyzer.types.Type;
import tokens.Token;

public class ArrayNode extends ParseNode {
	private Type subType;
	
	public ArrayNode(Token token) {
		super(token);
	}
	public ArrayNode(ParseNode node) {
		super(node);
	}

	///////////////////////////////////////////////////////////
	
	public Type getSubType() {
		return subType;
	}
	public void setSubType(Type subType) {
		this.subType = subType;
	}
	
	
	///////////////////////////////////////////////////////////
	// accept a visitor
		
	public void accept(ParseNodeVisitor visitor) {
		visitor.visitEnter(this);
		visitChildren(visitor);
		visitor.visitLeave(this);
	}
	

}
