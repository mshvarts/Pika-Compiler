package semanticAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lexicalAnalyzer.Keyword;
import lexicalAnalyzer.Lextant;
import logging.PikaLogger;
import parseTree.ParseNode;
import parseTree.ParseNodeVisitor;
import parseTree.nodeTypes.AssignmentStatementNode;
import parseTree.nodeTypes.BinaryOperatorNode;
import parseTree.nodeTypes.BooleanConstantNode;
import parseTree.nodeTypes.BreakStatementNode;
import parseTree.nodeTypes.CharacterConstantNode;
import parseTree.nodeTypes.ContinueStatementNode;
import parseTree.nodeTypes.BlockStatementNode;
import parseTree.nodeTypes.DeclarationNode;
import parseTree.nodeTypes.ErrorNode;
import parseTree.nodeTypes.IdentifierNode;
import parseTree.nodeTypes.IntegerConstantNode;
import parseTree.nodeTypes.LambdaNode;
import parseTree.nodeTypes.LambdaTypeNode;
import parseTree.nodeTypes.MapOperatorNode;
import parseTree.nodeTypes.FloatConstantNode;
import parseTree.nodeTypes.FoldOperatorNode;
import parseTree.nodeTypes.ForStatementNode;
import parseTree.nodeTypes.FunctionBodyNode;
import parseTree.nodeTypes.FunctionDefinitionNode;
import parseTree.nodeTypes.FunctionInvocationNode;
import parseTree.nodeTypes.NewlineNode;
import parseTree.nodeTypes.PopulatedArrayNode;
import parseTree.nodeTypes.PrintStatementNode;
import parseTree.nodeTypes.ProgramNode;
import parseTree.nodeTypes.ReduceOperatorNode;
import parseTree.nodeTypes.ReturnStatementNode;
import parseTree.nodeTypes.SpaceNode;
import parseTree.nodeTypes.StringConstantNode;
import parseTree.nodeTypes.StringSliceNode;
import parseTree.nodeTypes.TabNode;
import parseTree.nodeTypes.TypeNode;
import parseTree.nodeTypes.ArrayNode;
import parseTree.nodeTypes.UnaryOperatorNode;
import parseTree.nodeTypes.WhileStatementNode;
import parseTree.nodeTypes.ZipOperatorNode;
import semanticAnalyzer.signatures.FunctionSignature;
import semanticAnalyzer.signatures.FunctionSignatures;
import semanticAnalyzer.types.PrimitiveType;
import semanticAnalyzer.types.Type;
import semanticAnalyzer.types.Array;
import semanticAnalyzer.types.Function;
import semanticAnalyzer.types.Lambda;
import symbolTable.Binding;
import symbolTable.Scope;
import tokens.LextantToken;
import tokens.Token;

class SemanticAnalysisNormalVisitor extends ParseNodeVisitor.Default {
	@Override
	public void visitLeave(ParseNode node) {
		throw new RuntimeException("Node class unimplemented in SemanticAnalysisVisitor: " + node.getClass());
	}
	
	///////////////////////////////////////////////////////////////////////////
	// constructs larger than statements
	@Override
	public void visitEnter(ProgramNode node) {
		enterProgramScope(node);
	}
	public void visitLeave(ProgramNode node) {
		leaveScope(node);
	}
	public void visitEnter(FunctionBodyNode node) {
		Scope baseScope = node.getLocalScope();
		Scope scope = baseScope.createProcedureScope();
		scope.enter();
		node.setScope(scope);
		
		Function functionNode = new Function(null, PrimitiveType.NO_TYPE);
		node.setType(functionNode);
	}
	public void visitLeave(FunctionBodyNode node) {
		node.getScope().leave();
	}
	
	public void visitEnter(BlockStatementNode node) {
		enterSubscope(node);
		
		if(node.getParent() instanceof ForStatementNode) {
			ForStatementNode forNode = (ForStatementNode) node.getParent();
			
			Type declarationType;
			IdentifierNode identifierNode = (IdentifierNode)forNode.child(0);
			ParseNode sequence = forNode.child(1);
			//ParseNode blockNode = forNode.child(2);

			if(sequence.getType() != PrimitiveType.STRING && !(sequence.getType() instanceof Array)) {
				logError("For statement sequence must be a string or array type.");
			}
			
			if(forNode.isIndexLoop()) {
				declarationType = PrimitiveType.INTEGER;
			}
			else if(sequence.getType() == PrimitiveType.STRING) {
				declarationType = PrimitiveType.CHARACTER;
			}
			else {
				declarationType = ((Array)sequence.getType()).getSubType();
			}
			
			//Scope scope = blockNode.getScope();
			Scope scope = node.getScope();
			Binding binding = scope.createBinding(identifierNode, declarationType, false, false);
			identifierNode.setBinding(binding);
		}
	}
	public void visitLeave(BlockStatementNode node) {
		leaveScope(node);
	}
	
	/*public void visitEnter(FunctionDefinitionNode node) {
		Scope scope = node.getScope();
		scope.enter();
		node.setScope(scope);
	}
	public void visitLeave(FunctionDefinitionNode node) {
		node.getScope().leave();
	}*/
	
	///////////////////////////////////////////////////////////////////////////
	// helper methods for scoping.
	private void enterProgramScope(ParseNode node) {
		Scope scope = node.getScope();
		scope.enter();
	}	
	private void enterSubscope(ParseNode node) {
		Scope baseScope = node.getLocalScope();
		Scope scope = baseScope.createSubscope();
		node.setScope(scope);
		scope.enter();
	}		
	private void leaveScope(ParseNode node) {
		node.getScope().leave();
	}
	
	///////////////////////////////////////////////////////////////////////////
	// statements and declarations
	@Override
	public void visitLeave(PrintStatementNode node) {
	}
	@Override
	public void visitLeave(DeclarationNode node) {
		IdentifierNode identifier = (IdentifierNode) node.child(0);
		ParseNode initializer = node.child(1);
		
		Type declarationType = initializer.getType();
		node.setType(declarationType);
		
		identifier.setType(declarationType);
		boolean isVar = node.getToken().isLextant(Keyword.VAR);
		if(node.isStatic()) {
			ParseNode programNode = findProgramNode(node);
			Scope programScope = programNode.getScope();
			
			Binding binding = programScope.createCompanionBinding(identifier);
			identifier.setCompanionBinding(binding);
			
			binding = programScope.createStaticBinding(identifier, declarationType, isVar);
			identifier.setBinding(binding);
			
			addBinding(identifier, declarationType, isVar, true); 
		}
		else {
			addBinding(identifier, declarationType, isVar, false);
		}
	}
	
	@Override
	public void visitLeave(StringSliceNode node) {
		if(node.child(0).getType() != PrimitiveType.STRING) {
			logError("Slices can only be used on string types.");
		}
		if(node.child(1).getType() != PrimitiveType.INTEGER || node.child(2).getType() != PrimitiveType.INTEGER) {
			logError("Slice indexes must be integer types");
		}
		node.setType(PrimitiveType.STRING);
	}
	
	@Override
	public void visitLeave(TypeNode node) {
		if(node.getToken().getLexeme().equals(Keyword.NULL.getLexeme())) {
			if(!(node.getParent() instanceof LambdaTypeNode)) {
				logError("The null type may only be used as the return type of a lambda.");
			}
		}
		node.setType(PrimitiveType.fromToken(node.getToken()));
	}
	
	public void visitLeave(ArrayNode node) {
		ParseNode childNode = node.child(0);
		Type arrayType = new Array(childNode.getType());
		node.setType(arrayType);
	}
	
	@Override
	public void visitLeave(PopulatedArrayNode node) {
		Array arrayType = new Array(node.child(0).getType());
		node.setType(arrayType);
	}
	
	@Override
	public void visitLeave(UnaryOperatorNode node) {
		if(node.getOperator() == Keyword.REVERSE) {
			node.setType(node.child(0).getType());
		}
		else if(node.getOperator() == Keyword.CLONE) {
			node.setType(node.child(0).getType());
		}
		else {
			node.setType(PrimitiveType.INTEGER);
		}
	}
	
	public void visitEnter(ForStatementNode node) { }
	
	public void visitLeave(ForStatementNode node) { }
	
	public void visitLeave(LambdaNode node) { 
		
	}
	
	public void visitLeave(ZipOperatorNode node) {
		ParseNode arrayIdentifier = node.child(0);
		ParseNode arrayIdentifier2 = node.child(1);
		ParseNode lambdaIdentifier = node.child(2);
		
		if(!(arrayIdentifier.getType() instanceof Array)) {
			logError("zip operator's first argument must be an array");
		}
		if(!(arrayIdentifier2.getType() instanceof Array)) {
			logError("zip operator's second argument must be an array");
		}
		if(!(lambdaIdentifier.getType() instanceof Lambda)) {
			logError("zip operator's third argument must be a lambda");
		}
		
		var lambdaParams = ((Lambda)lambdaIdentifier.getType()).getParameters();
		if(lambdaParams.size() != 2) {
			logError("zip operator's lambda must have two parameters");
		}
		
		Type array1SubType = ((Array)arrayIdentifier.getType()).getSubType();
		Type array2SubType = ((Array)arrayIdentifier2.getType()).getSubType();
		var lambdaReturnType = ((Lambda)lambdaIdentifier.getType()).getReturnType();
		
		if(lambdaParams.get(0) != array1SubType || lambdaParams.get(1) != array2SubType) {
			logError("zip operator's lambda parameters type must match the arrays' subtypes respectively");
		}
		
		node.setType(new Array(lambdaReturnType));
		
		ParseNode lambdaNode = findLambdaNodeFromName(lambdaIdentifier);
		node.setLambdaNode(lambdaNode);
	}
	
	public void visitLeave(FoldOperatorNode node) {
		ParseNode arrayIdentifier;
		ParseNode lambdaIdentifier;
		ParseNode baseValue;
		
		arrayIdentifier = node.child(0);
		
		if(node.nChildren() == 2) {
			lambdaIdentifier = node.child(1);
		}
		else { 
			lambdaIdentifier = node.child(2);
		}
		
		if(!(arrayIdentifier.getType() instanceof Array)) {
			logError("fold operator's first argument must be an array");
		}
		if(!(lambdaIdentifier.getType() instanceof Lambda)) {
			logError("fold operator's second argument must be a lambda");
		}
		var lambdaParams = ((Lambda)lambdaIdentifier.getType()).getParameters();
		if(lambdaParams.size() != 2) {
			logError("fold operator's lambda must have two parameters");
		}
		
		Type arraySubType = ((Array)arrayIdentifier.getType()).getSubType();
		var lambdaReturnType = ((Lambda)lambdaIdentifier.getType()).getReturnType();
		
		if(node.nChildren() == 2) {
			if(lambdaParams.get(0) != arraySubType || lambdaParams.get(1) != arraySubType) {
				logError("fold operator's lambda parameters type must match the array's subtype");
			}
			if(lambdaReturnType != arraySubType) {
				logError("fold operator's lambda return type must match the array's subtype");
			}
			node.setType(arraySubType);
		}
		else {
			baseValue = node.child(1);
			if(lambdaParams.get(0) != baseValue.getType() || lambdaParams.get(1) != arraySubType) {
				logError("fold operator with base lambda parameters type must match base type and the array subtype");
			}
			if(lambdaReturnType != baseValue.getType()) {
				logError("fold operator with base lambda return type must match the base type");
			}
			node.setType(baseValue.getType());
		}
		
		ParseNode lambdaNode = findLambdaNodeFromName(lambdaIdentifier);
		node.setLambdaNode(lambdaNode);
	}
	
	public void visitLeave(ReduceOperatorNode node) {
		ParseNode arrayIdentifier = node.child(0);
		ParseNode lambdaIdentifier = node.child(1);
	
		if(!(arrayIdentifier.getType() instanceof Array)) {
			logError("reduce operator's first argument must be an array");
		}
		if(!(lambdaIdentifier.getType() instanceof Lambda)) {
			logError("reduce operator's second argument must be a lambda");
		}
		
		var lambdaParams = ((Lambda)lambdaIdentifier.getType()).getParameters();
		if(lambdaParams.size() != 1) {
			logError("reduce operator's lambda must only have one parameter");
		}
		
		Type arraySubType = ((Array)arrayIdentifier.getType()).getSubType();
		if(lambdaParams.get(0) != arraySubType) {
			logError("reduce operator's lambda parameter type must match the array's subtype");
		}
		
		var lambdaReturnType = ((Lambda)lambdaIdentifier.getType()).getReturnType();
		if(lambdaReturnType != PrimitiveType.BOOLEAN) {
			logError("reduce operator's lambda return type must be boolean");
		}
		
		node.setType(new Array(arraySubType));
		
		ParseNode lambdaNode = findLambdaNodeFromName(lambdaIdentifier);
		node.setLambdaNode(lambdaNode);
	}
	
	public void visitLeave(MapOperatorNode node) {
		ParseNode arrayIdentifier = node.child(0);
		ParseNode lambdaIdentifier = node.child(1);
	
		if(!(arrayIdentifier.getType() instanceof Array)) {
			logError("map operator's first argument must be an array");
		}
		if(!(lambdaIdentifier.getType() instanceof Lambda)) {
			logError("map operator's second argument must be a lambda");
		}
		
		var lambdaParams = ((Lambda)lambdaIdentifier.getType()).getParameters();
		if(lambdaParams.size() != 1) {
			logError("map operator's lambda must only have one parameter");
		}
		
		Type arraySubType = ((Array)arrayIdentifier.getType()).getSubType();
		if(lambdaParams.get(0) != arraySubType) {
			logError("map operator's lambda parameter type must match the array's subtype");
		}
		
		var lambdaReturnType = ((Lambda)lambdaIdentifier.getType()).getReturnType();
		node.setType(new Array(lambdaReturnType));
		
		ParseNode lambdaNode = findLambdaNodeFromName(lambdaIdentifier);
		node.setLambdaNode(lambdaNode);
	}
	
	public void visitLeave(FunctionInvocationNode node) {
		
		IdentifierNode identifierNode = (IdentifierNode) node.child(0);
		Binding binding = identifierNode.findVariableBinding();
		
		//node.setType(((Function)binding.getType()).getReturnType());
		node.setType(binding.getType());
		
		int numOfInvocationArgs = node.getChildren().size() - 1;
		
		ParseNode functionDefinitionNode = findFunctionNodeFromName(identifierNode);
		
		/*if(functionDefinitionNode == null) {
			logError("Could not find a function or lambda named " + identifierNode);
			
			// A lambda must exist in the local scope then
		} */
		
		ParseNode lambdaNode;
		if(functionDefinitionNode == null) {
			lambdaNode = findLambdaNodeFromName(identifierNode);
		}
		else {
			lambdaNode = functionDefinitionNode.child(1);
		}
		
		ParseNode bodyNode = lambdaNode.child(1);
		bodyNode.setType(binding.getType());
		Lambda lambdaType = ((Lambda)((LambdaNode)lambdaNode).getType());
		
		var parameters = lambdaType.getParameters();
		int numOfFunctionDefArgs = parameters.size();
		if(numOfFunctionDefArgs != numOfInvocationArgs) {
			logError("Number of function arguments given specified do not match definition");
		}
		
		// check for match types between definition and invocation
		for(int i = 1; i < node.nChildren(); i++) { 
			// starts at i = 1 because first child is the function identifier node
			Type paramType = parameters.get(i-1); 
			if(node.child(i).getType() != paramType){
				logError("Argument " + i + " with type " + node.child(i).getType() + 
						" does not match parameter type " + paramType);
			}
		}
			
		node.setLambdaNode(lambdaNode);
	}
	
	private ParseNode findProgramNode(ParseNode node) {
		ParseNode rootNode = null;
		for(ParseNode parent : node.pathToRoot()) {
			rootNode = parent;
		}
		return rootNode;
	}
	
	private ParseNode findLambdaNodeFromName(ParseNode identifier) {
		for(ParseNode parent : identifier.pathToRoot()) {
			for(ParseNode node : parent.getChildren()) {
				if(node instanceof DeclarationNode) {
					if(((IdentifierNode)node.child(0)).getToken().getLexeme() == identifier.getToken().getLexeme()) {
						return node.child(1);
					}
				}
			}
		}
		return null;
	}
	
	private ParseNode findFunctionNodeFromName(ParseNode identifier) {
		// Find program node
		ParseNode parentNode = identifier;
		while(parentNode.getParent() != null) {
			parentNode = parentNode.getParent();
		}
		ParseNode programNode = parentNode;
		for(ParseNode node : programNode.getChildren()) {
			if(node instanceof FunctionDefinitionNode) {
				if(((IdentifierNode)node.child(0)).getToken().getLexeme() == identifier.getToken().getLexeme()) {
					return node;
				}
			}
		}
		return null;
	}
	
	public void visitLeave(ReturnStatementNode node) {
		if(node.nChildren() == 1) {
			node.setType(node.child(0).getType());
		}
		
		boolean insideALambda = false;
		ParseNode functionNode = node.getParent();
		for(ParseNode parent : node.pathToRoot()) {
			if(parent instanceof FunctionBodyNode) {
				insideALambda = true;
				functionNode = parent;
				break;
			}
		}
		if(!insideALambda) {
			logError("Return statement must be inside inside a lambda");
		}
		ParseNode lambdaNode = functionNode.getParent();
		Lambda lambdaType = ((Lambda)((LambdaNode)lambdaNode).getType());
		
		Type returnType = node.getType();
		
		if(node.getType() instanceof Function) {
			returnType = ((Function)node.getType()).getReturnType();
		}
		
		if(node.getType() instanceof Lambda) {
			returnType = ((Lambda)node.getType()).getReturnType();
		}
		
		if(!returnType.equivalent(lambdaType.getReturnType())) {
			logError("Return statement must match the return type of the lambda. \n" + 
					node.getType() + "\n" + lambdaType.getReturnType());
		}
	}
	
	@Override
	public void visitLeave(AssignmentStatementNode node) {
		if(node.child(0) instanceof IdentifierNode) {
			IdentifierNode identifier = (IdentifierNode) node.child(0);
			ParseNode expression = node.child(1);
			
			// check identifier's type vs. expression's type.
			if(!identifier.getType().equals(expression.getType())) {
				typeCheckError(node, Arrays.asList(identifier.getType(), expression.getType()));
			}
			// check identifier is not CONST.
			if(!identifier.getBinding().isVar()) {
				assignToConstError(node);
			}
			Type assignmentType = expression.getType();
			node.setType(assignmentType);
			
			identifier.setType(assignmentType);
		}
		else if(node.child(0) instanceof BinaryOperatorNode) { 
			BinaryOperatorNode indexedNode = (BinaryOperatorNode)node.child(0);
			
			ParseNode expression = node.child(1);
			Type assignmentType = expression.getType();
			node.setType(assignmentType);
			
			if(indexedNode.child(0).getType() == PrimitiveType.STRING) {
				logError("Can not target indexed string elements. Strings are immutable.");
			}
			
			if(!indexedNode.getType().equivalent(expression.getType())) {
				typeCheckError(node, Arrays.asList(indexedNode.getType(), expression.getType()));
			}
			
			indexedNode.setType(assignmentType);
		}
		else if(node.child(0) instanceof StringSliceNode) {
			logError("Can not target sliced string elements. Strings are immutable.");
		}
		else {
			logError("Could not identify the type of assignmentNode's first child node");
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// expressions
	@Override
	public void visitLeave(BinaryOperatorNode node) {
		assert node.nChildren() == 2;
		ParseNode left  = node.child(0);
		ParseNode right = node.child(1);
		
		List<Type> childTypes = new ArrayList<Type>();//= Arrays.asList(left.getType(), right.getType());
		if(left.getType() instanceof Lambda) {
			childTypes.add(((Lambda)left.getType()).getReturnType());
		}
		else childTypes.add(left.getType());
		
		if(right.getType() instanceof Lambda) {
			childTypes.add(((Lambda)right.getType()).getReturnType());
		}
		else childTypes.add(right.getType());
		
		Lextant operator = operatorFor(node);
		FunctionSignatures signatures = FunctionSignatures.signaturesOf(operator);
		FunctionSignature signature = signatures.acceptingSignature(childTypes);
		
		if(signature.accepts(childTypes)) {
			node.setType(signature.resultType());
			node.setSignature(signature);
		}
		else {
			typeCheckError(node, childTypes);
			node.setType(PrimitiveType.ERROR);
		}
	}
	private Lextant operatorFor(BinaryOperatorNode node) {
		LextantToken token = (LextantToken) node.getToken();
		return token.getLextant();
	}


	///////////////////////////////////////////////////////////////////////////
	// simple leaf nodes
	@Override
	public void visit(BooleanConstantNode node) {
		node.setType(PrimitiveType.BOOLEAN);
	}
	@Override
	public void visit(ErrorNode node) {
		node.setType(PrimitiveType.ERROR);
	}
	@Override
	public void visit(IntegerConstantNode node) {
		node.setType(PrimitiveType.INTEGER);
	}
	@Override
	public void visit(FloatConstantNode node) {
		node.setType(PrimitiveType.FLOATING);
	}
	@Override
	public void visit(StringConstantNode node) {
		node.setType(PrimitiveType.STRING);
	}
	@Override
	public void visit(CharacterConstantNode node) {
		node.setType(PrimitiveType.CHARACTER);
	}
	@Override
	public void visit(NewlineNode node) {
	}
	@Override
	public void visit(TabNode node) {
	}
	@Override
	public void visit(SpaceNode node) {
	}
	///////////////////////////////////////////////////////////////////////////
	@Override
	public void visit(BreakStatementNode node) {
		boolean insideLoop = false;
		ParseNode loopNode = null;
		// check if it is inside a while or for statement 
		for (ParseNode parent : node.pathToRoot()) {
			if(parent instanceof WhileStatementNode || parent instanceof ForStatementNode) {
				insideLoop = true;
				loopNode = parent;
				break;
			}
		}
		if(!insideLoop) {
			logError("The node " + node + " is not inside a loop statement.");
		} 
		else {
			node.setLoopNode(loopNode);
		}
	}
	@Override
	public void visit(ContinueStatementNode node) {
		boolean insideLoop = false;
		ParseNode loopNode = null;
		// check if it is inside a while or for statement 
		for (ParseNode parent : node.pathToRoot()) {
			if(parent instanceof WhileStatementNode || parent instanceof ForStatementNode) {
				insideLoop = true;
				loopNode = parent;
				break;
			}
		}
		if(!insideLoop) {
			logError("The node " + node + " is not inside a loop statement.");
		} 
		else {
			node.setLoopNode(loopNode);
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// IdentifierNodes, with helper methods
	@Override
	public void visit(IdentifierNode node) {
		if(!isBeingDeclared(node) && !isLambdaType(node) && !isFunctionType(node) && !isForLoopIndex(node)) {		
			Binding binding = node.findVariableBinding();
			
			node.setType(binding.getType());
			node.setBinding(binding);
		}
		// else the parent (DeclarationNode, etc..) does the processing.
	}
	private boolean isBeingDeclared(IdentifierNode node) {
		ParseNode parent = node.getParent();
		return (parent instanceof DeclarationNode) && (node == parent.child(0));
	}
	private boolean isLambdaType(IdentifierNode node) {
		ParseNode parent = node.getParent();
		return (parent instanceof LambdaTypeNode);
	}
	private boolean isFunctionType(IdentifierNode node) {
		ParseNode parent = node.getParent();
		return (parent instanceof FunctionDefinitionNode);
	}
	private boolean isForLoopIndex(IdentifierNode node) {
		ParseNode parent = node.getParent();
		return (parent instanceof ForStatementNode && parent.child(0) == node);
	}
	private void addBinding(IdentifierNode identifierNode, Type type, boolean isVar, boolean isStatic) {
		Scope scope = identifierNode.getLocalScope();
		Binding binding = scope.createBinding(identifierNode, type, isVar, isStatic);
		identifierNode.setBinding(binding);
	}
	
	
	/*private void setBinding(IdentifierNode identifierNode, Type type) {
		Scope scope = identifierNode.getLocalScope();
		String identifierName = identifierNode.getBinding().getLexeme();
		if(scope.getSymbolTable().containsKey(identifierName)) {
			Binding binding = scope.getSymbolTable().lookup(identifierName);
			identifierNode.setBinding(binding);
		} else {
			logError("identifier " + identifierName + " is not defined in this scope ");
		}
	}*/
	
	///////////////////////////////////////////////////////////////////////////
	// error logging/printing
	private void assignToConstError(ParseNode node) {
		Token token = node.getToken();
		
		logError("attempt to assign to CONST var-declared variable at " + token.getLocation());	
	}
	private void typeCheckError(ParseNode node, List<Type> operandTypes) {
		Token token = node.getToken();
		
		logError("operator " + token.getLexeme() + " not defined for types " 
				 + operandTypes  + " at " + token.getLocation());	
	}
	private void logError(String message) {
		PikaLogger log = PikaLogger.getLogger("compiler.semanticAnalyzer");
		log.severe(message);
	}
}