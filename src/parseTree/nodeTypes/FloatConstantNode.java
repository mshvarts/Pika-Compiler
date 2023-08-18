package parseTree.nodeTypes;

import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import tokens.FloatingToken;
import tokens.Token;

public class FloatConstantNode extends ParseNode {
	public FloatConstantNode(Token token) {
		super(token);
		assert(token instanceof FloatingToken);
	}
	public FloatConstantNode(ParseNode node) {
		super(node);
	}

////////////////////////////////////////////////////////////
// attributes
	
	public double getValue() {
		return numberToken().getValue();
	}

	public FloatingToken numberToken() {
		return (FloatingToken)token;
	}	

///////////////////////////////////////////////////////////
// accept a visitor
	
	public void accept(ParseNodeVisitor visitor) {
		visitor.visit(this);
	}

}
