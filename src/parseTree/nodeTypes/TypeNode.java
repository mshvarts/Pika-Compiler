package parseTree.nodeTypes;

import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import tokens.Token;
import tokens.TypeToken;

public class TypeNode extends ParseNode {
	public TypeNode(Token token) {
		super(token);
		assert(token instanceof TypeToken);
	}
	public TypeNode(ParseNode node) {
		super(node);
	}

////////////////////////////////////////////////////////////
// attributes
	
	public String getValue() {
		return typeToken().getLexeme();
	}

	public TypeToken typeToken() {
		return (TypeToken)token;
	}	

///////////////////////////////////////////////////////////
// accept a visitor
	
	public void accept(ParseNodeVisitor visitor) {
		visitor.visitLeave(this);
	}

}
