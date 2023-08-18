package semanticAnalyzer;


import java.util.ArrayList;
import java.util.List;

import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import parseTree.nodeTypes.IdentifierNode;
import parseTree.nodeTypes.LambdaNode;
import parseTree.nodeTypes.LambdaTypeNode;
import parseTree.nodeTypes.ArrayNode;
import parseTree.nodeTypes.FunctionDefinitionNode;
import parseTree.nodeTypes.ProgramNode;
import parseTree.nodeTypes.TypeNode;
import semanticAnalyzer.types.Array;
import semanticAnalyzer.types.Function;
import semanticAnalyzer.types.Lambda;
import semanticAnalyzer.types.PrimitiveType;
import semanticAnalyzer.types.Type;
import symbolTable.Binding;
import symbolTable.Scope;

class SemanticAnalysisFunctionVisitor extends ParseNodeVisitor.Default {
	
	///////////////////////////////////////////////////////////////////////////
	// constructs larger than statements
	@Override
	public void visitEnter(ProgramNode node) {
		enterProgramScope(node);
	}
	public void visitLeave(ProgramNode node) {
		leaveScope(node);
	}
	public void visitEnter(FunctionDefinitionNode node) {
		
	}
	
	public void visitLeave(FunctionDefinitionNode node) {
		IdentifierNode functionIdentifier = (IdentifierNode) node.child(0);
		LambdaNode lambda = (LambdaNode) node.child(1);
		Lambda lambdaType = ((Lambda)lambda.getType());
		addFunctionBinding(functionIdentifier, lambdaType.getParameters(), lambdaType.getReturnType());
	}
	
	///////////////////////////////////////////////////////////////////////////
	// helper methods for scoping.
	private void enterProgramScope(ParseNode node) {
		Scope scope = Scope.createProgramScope();
		scope.enter();
		node.setScope(scope);
	}		
	private void leaveScope(ParseNode node) {
		node.getScope().leave();
	}
	private void addFunctionBinding(IdentifierNode identifierNode, List<Type> parameters, Type returnType) {
		Scope scope = identifierNode.getLocalScope();
		Binding binding = scope.createFunctionBinding(identifierNode, new Function(parameters, returnType));
		identifierNode.setBinding(binding);
	}
	
	/*private void enterSubscope(ParseNode node) {
		Scope baseScope = node.getLocalScope();
		Scope scope = baseScope.createSubscope();
		node.setScope(scope);
		scope.enter();
	}	*/	
///////////////////////////////////////////////////////////////////////////
	// statements and declarations
	public void visitEnter(LambdaNode node) {
		Scope baseScope = node.getLocalScope();
		Scope scope = baseScope.createParameterScope();
		scope.enter();
		node.setScope(scope);
		List<Type> params = new ArrayList<Type>();
		
		LambdaTypeNode signature = (LambdaTypeNode) node.child(0);
		Type returnType = PrimitiveType.NO_TYPE;
		for(int i = 0; i < signature.getChildren().size(); i++) {
			ParseNode childNode = signature.child(i);
			if(i == signature.getChildren().size() - 1) {
				if(childNode instanceof ArrayNode) {
					Type subType = getSubType((ArrayNode)childNode);
					returnType = subType;
				}
				else if(childNode instanceof TypeNode) {
					Type nodeType = PrimitiveType.fromToken(childNode.getToken());
					returnType = nodeType;
				}
			}
			else if(childNode instanceof TypeNode) {
				Type nodeType = PrimitiveType.fromToken(childNode.getToken());
				childNode.setType(nodeType);
				params.add(nodeType);
			}
		}
		
		Type lambdaType = new Lambda(params, returnType);
		node.setType(lambdaType);
		
		//node.setType(returnType);	
	}
	private Type getSubType(ParseNode node) {
		if(node instanceof ArrayNode) {
			return new Array(getSubType(node.child(0)));
		}
		else return PrimitiveType.fromToken(node.getToken());
	}
	public void visitLeave(LambdaNode node) {
		leaveScope(node);
	}
	
	private void addBinding(IdentifierNode identifierNode, Type type, boolean isVar) {
		Scope scope = identifierNode.getLocalScope();
		Binding binding = scope.createBinding(identifierNode, type, isVar,false);
		identifierNode.setBinding(binding);
	}
	
	public void visitLeave(LambdaTypeNode node) {
		for(int i = 0; i < node.getChildren().size(); i++) {
			ParseNode param = node.child(i);
			if(param instanceof IdentifierNode) {
				Type identifierType = node.child(i-1).getType();
				addBinding((IdentifierNode)param, identifierType, false);
			}
		}
	}
}