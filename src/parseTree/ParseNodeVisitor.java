package parseTree;

import parseTree.nodeTypes.*;

// Visitor pattern with pre- and post-order visits
public interface ParseNodeVisitor {
	
	// non-leaf nodes: visitEnter and visitLeave
	void visitEnter(BinaryOperatorNode node);
	void visitLeave(BinaryOperatorNode node);
	
	void visitEnter(BlockStatementNode node);
	void visitLeave(BlockStatementNode node);

	void visitEnter(DeclarationNode node);
	void visitLeave(DeclarationNode node);
	
	void visitEnter(AssignmentStatementNode node);
	void visitLeave(AssignmentStatementNode node);
	
	void visitEnter(IfStatementNode node);
	void visitLeave(IfStatementNode node);
	
	void visitEnter(WhileStatementNode node);
	void visitLeave(WhileStatementNode node);
	
	void visitEnter(PopulatedArrayNode node);
	void visitLeave(PopulatedArrayNode node);
	
	void visitEnter(ParseNode node);
	void visitLeave(ParseNode node);
	
	void visitEnter(PrintStatementNode node);
	void visitLeave(PrintStatementNode node);
	
	void visitEnter(ProgramNode node);
	void visitLeave(ProgramNode node);
	
	void visitEnter(UnaryOperatorNode node);
	void visitLeave(UnaryOperatorNode node);

	void visitEnter(FunctionInvocationNode node);
	void visitLeave(FunctionInvocationNode node);
	
	void visitEnter(FunctionDefinitionNode node);
	void visitLeave(FunctionDefinitionNode node);
	
	void visitEnter(LambdaNode node);
	void visitLeave(LambdaNode node);
	
	void visitEnter(LambdaTypeNode node);
	void visitLeave(LambdaTypeNode node);
	
	void visitEnter(ReturnStatementNode node);
	void visitLeave(ReturnStatementNode node);
	
	void visitEnter(CallStatementNode node);
	void visitLeave(CallStatementNode node);
	
	void visitEnter(FunctionBodyNode node);
	void visitLeave(FunctionBodyNode node);
	
	void visitEnter(ArrayNode node);
	void visitLeave(ArrayNode node);
	
	void visitLeave(TypeNode node);
	
	void visitEnter(StringSliceNode node);
	void visitLeave(StringSliceNode node);
	
	void visitEnter(ForStatementNode node);
	void visitLeave(ForStatementNode node);
	
	void visitEnter(MapOperatorNode node);
	void visitLeave(MapOperatorNode node);
	
	void visitEnter(ReduceOperatorNode node);
	void visitLeave(ReduceOperatorNode node);
	
	void visitEnter(FoldOperatorNode node);
	void visitLeave(FoldOperatorNode node);
	
	void visitEnter(ZipOperatorNode node);
	void visitLeave(ZipOperatorNode node);
	
	
	// leaf nodes: visitLeaf only
	void visit(BooleanConstantNode node);
	void visit(ErrorNode node);
	void visit(IdentifierNode node);
	void visit(IntegerConstantNode node);
	void visit(FloatConstantNode node);
	void visit(CharacterConstantNode node);
	void visit(StringConstantNode node);
	void visit(NewlineNode node);
	void visit(TabNode node);
	void visit(SpaceNode node);
	void visit(BreakStatementNode node);
	void visit(ContinueStatementNode node);
	
	public static class Default implements ParseNodeVisitor
	{
		public void defaultVisit(ParseNode node) {	}
		public void defaultVisitEnter(ParseNode node) {
			defaultVisit(node);
		}
		public void defaultVisitLeave(ParseNode node) {
			defaultVisit(node);
		}		
		public void defaultVisitForLeaf(ParseNode node) {
			defaultVisit(node);
		}
		
		public void visitEnter(BinaryOperatorNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(BinaryOperatorNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(DeclarationNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(DeclarationNode node) {
			defaultVisitLeave(node);
		}		
		public void visitEnter(AssignmentStatementNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(AssignmentStatementNode node) {
			defaultVisitLeave(node);
		}	
		public void visitEnter(BlockStatementNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(BlockStatementNode node) {
			defaultVisitLeave(node);
		}				
		public void visitEnter(ParseNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(ParseNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(PrintStatementNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(PrintStatementNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(ProgramNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(ProgramNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(IfStatementNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(IfStatementNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(WhileStatementNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(WhileStatementNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(PopulatedArrayNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(PopulatedArrayNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(UnaryOperatorNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(UnaryOperatorNode node) {
			defaultVisitLeave(node);
		}
		public void visitLeave(TypeNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(FunctionInvocationNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(FunctionInvocationNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(FunctionDefinitionNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(FunctionDefinitionNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(LambdaNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(LambdaNode node) {
			defaultVisitLeave(node);
		}
		
		public void visitEnter(LambdaTypeNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(LambdaTypeNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(ReturnStatementNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(ReturnStatementNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(CallStatementNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(CallStatementNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(FunctionBodyNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(FunctionBodyNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(ArrayNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(ArrayNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(StringSliceNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(StringSliceNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(ForStatementNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(ForStatementNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(MapOperatorNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(MapOperatorNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(ReduceOperatorNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(ReduceOperatorNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(FoldOperatorNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(FoldOperatorNode node) {
			defaultVisitLeave(node);
		}
		public void visitEnter(ZipOperatorNode node) {
			defaultVisitEnter(node);
		}
		public void visitLeave(ZipOperatorNode node) {
			defaultVisitLeave(node);
		}
		
		public void visit(BooleanConstantNode node) {
			defaultVisitForLeaf(node);
		}
		public void visit(ErrorNode node) {
			defaultVisitForLeaf(node);
		}
		public void visit(IdentifierNode node) {
			defaultVisitForLeaf(node);
		}
		public void visit(IntegerConstantNode node) {
			defaultVisitForLeaf(node);
		}
		public void visit(CharacterConstantNode node) {
			defaultVisitForLeaf(node);
		}
		public void visit(StringConstantNode node) {
			defaultVisitForLeaf(node);
		}
		public void visit(FloatConstantNode node) {
			defaultVisitForLeaf(node);
		}
		public void visit(NewlineNode node) {
			defaultVisitForLeaf(node);
		}	
		public void visit(TabNode node) {
			defaultVisitForLeaf(node);
		}	
		public void visit(SpaceNode node) {
			defaultVisitForLeaf(node);
		}
		public void visit(BreakStatementNode node) {
			defaultVisitForLeaf(node);
		}
		public void visit(ContinueStatementNode node) {
			defaultVisitForLeaf(node);
		}
	}
}
