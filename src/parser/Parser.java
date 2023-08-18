package parser;

import java.util.Arrays;

import logging.PikaLogger;
import parseTree.*;
import parseTree.nodeTypes.AssignmentStatementNode;
import parseTree.nodeTypes.BinaryOperatorNode;
import parseTree.nodeTypes.BooleanConstantNode;
import parseTree.nodeTypes.BreakStatementNode;
import parseTree.nodeTypes.CallStatementNode;
import parseTree.nodeTypes.CharacterConstantNode;
import parseTree.nodeTypes.ContinueStatementNode;
import parseTree.nodeTypes.DeallocStatementNode;
import parseTree.nodeTypes.BlockStatementNode;
import parseTree.nodeTypes.DeclarationNode;
import parseTree.nodeTypes.ErrorNode;
import parseTree.nodeTypes.IdentifierNode;
import parseTree.nodeTypes.IfStatementNode;
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
import tokens.*;
import lexicalAnalyzer.Keyword;
import lexicalAnalyzer.Lextant;
import lexicalAnalyzer.Punctuator;
import lexicalAnalyzer.Scanner;

public class Parser {
	private Scanner scanner;
	private Token nowReading;
	private Token previouslyRead;
	
	public static ParseNode parse(Scanner scanner) {
		Parser parser = new Parser(scanner);
		return parser.parse();
	}
	public Parser(Scanner scanner) {
		super();
		this.scanner = scanner;
	}
	
	public ParseNode parse() {
		readToken();
		return parseProgram();
	}

	////////////////////////////////////////////////////////////
	// "program" is the start symbol S
	// S -> EXEC blockStatement
	
	private ParseNode parseProgram() {
		if(!startsProgram(nowReading)) {
			return syntaxErrorNode("program");
		}
		ParseNode program = new ProgramNode(nowReading);
		
		while(startsDeclaration(nowReading) || startsFunctionDefinition(nowReading)) {
			if(startsDeclaration(nowReading)) {
				ParseNode globalVar = parseDeclaration();
				program.insertChild(globalVar);
			}
			
			if(startsFunctionDefinition(nowReading)) {
				ParseNode function = parseFunctionDefinition();
				program.appendChild(function);
			}
		}
		
		expect(Keyword.EXEC);
		ParseNode blockStatement = parseBlockStatement();
		program.appendChild(blockStatement);
		
		if(!(nowReading instanceof NullToken)) {
			return syntaxErrorNode("end of program");
		}
		
		return program;
	}
	private boolean startsProgram(Token token) {
		return token.isLextant(Keyword.EXEC) || startsFunctionDefinition(token) || startsDeclaration(token);
	}
	
	
	///////////////////////////////////////////////////////////
	// Function Body 
	
	// functionBody -> { statement* }
	private ParseNode parseFunctionBody() {
		if(!startsFunctionBody(nowReading)) {
			return syntaxErrorNode("functionBody");
		}
		ParseNode funcBodyNode = new FunctionBodyNode(nowReading);
		expect(Punctuator.OPEN_BRACE);
		
		while(startsStatement(nowReading)) {
			ParseNode statement = parseStatement();
			funcBodyNode.appendChild(statement);
		}
		expect(Punctuator.CLOSE_BRACE);
		return funcBodyNode;
	}
	private boolean startsFunctionBody(Token token) {
		return token.isLextant(Punctuator.OPEN_BRACE);
	}
	
	///////////////////////////////////////////////////////////
	// Block Statement
	
	// blockStatement -> { statement* }
	private ParseNode parseBlockStatement() {
		if(!startsBlockStatement(nowReading)) {
			return syntaxErrorNode("blockStatement");
		}
		ParseNode blockStatement = new BlockStatementNode(nowReading);
		expect(Punctuator.OPEN_BRACE);
		
		while(startsStatement(nowReading)) {
			ParseNode statement = parseStatement();
			blockStatement.appendChild(statement);
		}
		expect(Punctuator.CLOSE_BRACE);
		return blockStatement;
	}
	private boolean startsBlockStatement(Token token) {
		return token.isLextant(Punctuator.OPEN_BRACE);
	}
	
	
	///////////////////////////////////////////////////////////
	// statements
	
	// statement-> declaration | printStmt | blockStatement | assnStmt...
	private ParseNode parseStatement() {
		if(!startsStatement(nowReading)) {
			return syntaxErrorNode("statement");
		}
		if(startsDeclaration(nowReading)) {
			return parseDeclaration();
		}
		if(startsArrayIndexedStatement(nowReading)) {
			return parseArrayIndexedStatement();
		}
		if(startsAssignmentStatement(nowReading)) {
			return parseAssignmentStatement();
		}
		if(startsIfStatement(nowReading)) {
			return parseIfStatement();
		}
		if(startsWhileStatement(nowReading)) {
			return parseWhileStatement();
		}
		if(startsForStatement(nowReading)) {
			return parseForStatement();
		}
		if(startsPrintStatement(nowReading)) {
			return parsePrintStatement();
		}
		if(startsBlockStatement(nowReading)) {
			return parseBlockStatement();
		}
		if(startsDeallocStatement(nowReading)) {
			return parseDeallocStatement();
		}
		if(startsBreakStatement(nowReading)) {
			return parseBreakStatement();
		}
		if(startsContinueStatement(nowReading)) {
			return parseContinueStatement();
		}
		if(startsReturnStatement(nowReading)) {
			return parseReturnStatement();
		}
		if(startsCallStatement(nowReading)) {
			return parseCallStatement();
		}
		return syntaxErrorNode("statement");
	}
	private boolean startsStatement(Token token) {
		return startsPrintStatement(token) ||
			   startsBlockStatement(token) ||
			   startsIfStatement(token) ||
			   startsWhileStatement(token) ||
			   startsArrayIndexedStatement(token) ||
			   startsAssignmentStatement(token) ||
			   startsDeclaration(token) ||
			   startsDeallocStatement(token) || 
			   startsBreakStatement(token) ||
			   startsContinueStatement(token) || 
			   startsCallStatement(token) || 
			   startsReturnStatement(token) ||
			   startsForStatement(token);
	}
	
	// ifStmt -> if ( expression ) blockStatement [else blockStatement]?
	private ParseNode parseIfStatement() {
		if(!startsIfStatement(nowReading)) {
			return syntaxErrorNode("ifStatement");
		}
		IfStatementNode result = new IfStatementNode(nowReading);
		readToken();
		expect(Punctuator.OPEN_PARENTHESIS);
		ParseNode boolCondition = parseExpression();
		result.appendChild(boolCondition);
		expect(Punctuator.CLOSE_PARENTHESIS);
		ParseNode mainStatement = parseBlockStatement();
		result.appendChild(mainStatement);
		if(nowReading.isLextant(Keyword.ELSE)) {
			expect(Keyword.ELSE);
			ParseNode elseStatement = parseBlockStatement();
			result.appendChild(elseStatement);
		}
		return result;
	}
	// ifStmt -> if ( expression ) blockStatement [else blockStatement]?
	private boolean startsIfStatement(Token token) {
		return token.isLextant(Keyword.IF);
	}
		
	// whileStmt -> while (expression) blockStatement
	private ParseNode parseWhileStatement() {
		if(!startsWhileStatement(nowReading)) {
			return syntaxErrorNode("whileStatement");
		}
		Token whileToken = nowReading;
		readToken();
		expect(Punctuator.OPEN_PARENTHESIS);
		ParseNode boolCondition = parseExpression();
		expect(Punctuator.CLOSE_PARENTHESIS);
		ParseNode blockStatement = parseBlockStatement();
		return WhileStatementNode.withChildren(whileToken, boolCondition, blockStatement);
	}
	// whileStmt -> while (expression) blockStatement
	private boolean startsWhileStatement(Token token) {
		return token.isLextant(Keyword.WHILE);
	}
	
	// forStmt -> for [index|elem] identifier of expression blockStatement
	private ParseNode parseForStatement() {
		if(!startsForStatement(nowReading)) {
			return syntaxErrorNode("forStatement");
		}
		Token forToken = nowReading;
		readToken();
		ForStatementNode forNode = new ForStatementNode(forToken);
		if(nowReading.isLextant(Keyword.INDEX)) {
			readToken();
			forNode.setToLoopIndexes();
		}
		else if(nowReading.isLextant(Keyword.ELEM)) {
			readToken();
			forNode.setToLoopElements();
		}
		else {
			syntaxErrorNode("expecting index or elem after for");
		}
		ParseNode identifier = parseIdentifier();
		forNode.appendChild(identifier);
		expect(Keyword.OF);
		ParseNode sequence = parseExpression();
		forNode.appendChild(sequence);
		ParseNode blockStatement = parseBlockStatement();
		forNode.appendChild(blockStatement);
		return forNode;
	}
	// forStmt -> for [index|elem] identifier of expression blockStatement
	private boolean startsForStatement(Token token) {
		return token.isLextant(Keyword.FOR);
	}
		
	// deallocStmt -> dealloc refType .
	private ParseNode parseDeallocStatement() {
		if(!startsDeallocStatement(nowReading)) {
			return syntaxErrorNode("deallocStatement");
		}
		DeallocStatementNode result = new DeallocStatementNode(nowReading);
		readToken();
		ParseNode refType = parseIdentifier();
		result.appendChild(refType);
		expect(Punctuator.TERMINATOR);
		return result;
	}
	// deallocStmt -> dealloc refType .
	private boolean startsDeallocStatement(Token token) {
		return token.isLextant(Keyword.DEALLOC);
	}	
		
	private ParseNode parseBreakStatement() {
		if(!startsBreakStatement(nowReading)) {
			return syntaxErrorNode("breakStatement");
		}
		BreakStatementNode result = new BreakStatementNode(nowReading);
		readToken();
		expect(Punctuator.TERMINATOR);
		return result;
	}
	// BreakStmt -> break .
	private boolean startsBreakStatement(Token token) {
		return token.isLextant(Keyword.BREAK);
	}	
	
	private ParseNode parseContinueStatement() {
		if(!startsContinueStatement(nowReading)) {
			return syntaxErrorNode("continueStatement");
		}
		ContinueStatementNode result = new ContinueStatementNode(nowReading);
		readToken();
		expect(Punctuator.TERMINATOR);
		return result;
	}
	// ContinueStmt -> continue .
	private boolean startsContinueStatement(Token token) {
		return token.isLextant(Keyword.CONTINUE);
	}	
		
	private ParseNode parseReturnStatement() {
		if(!startsReturnStatement(nowReading)) {
			return syntaxErrorNode("returnStatement");
		}
		ReturnStatementNode result = new ReturnStatementNode(nowReading);
		readToken();
		if(startsExpression(nowReading)) {
			ParseNode expression = parseExpression();
			result.appendChild(expression);
		}
		expect(Punctuator.TERMINATOR);
		return result;
	}
	// ReturnStmt -> return expression? .
	private boolean startsReturnStatement(Token token) {
		return token.isLextant(Keyword.RETURN);
	}	
	
	private ParseNode parseCallStatement() {
		if(!startsCallStatement(nowReading)) {
			return syntaxErrorNode("callStatement");
		}
		CallStatementNode result = new CallStatementNode(nowReading);
		readToken();
		ParseNode functionInvocation = parseFunctionInvocation();
		result.appendChild(functionInvocation);
		expect(Punctuator.TERMINATOR);
		return result;
	}
	// CallStmt -> call functionInvocation .
	private boolean startsCallStatement(Token token) {
		return token.isLextant(Keyword.CALL);
	}	
	
	// printStmt -> PRINT printExpressionList .
	private ParseNode parsePrintStatement() {
		if(!startsPrintStatement(nowReading)) {
			return syntaxErrorNode("print statement");
		}
		PrintStatementNode result = new PrintStatementNode(nowReading);
		
		readToken();
		result = parsePrintExpressionList(result);
		
		expect(Punctuator.TERMINATOR);
		return result;
	}
	private boolean startsPrintStatement(Token token) {
		return token.isLextant(Keyword.PRINT);
	}	
	
	// asgnStmt -> target := expression .
	private ParseNode parseAssignmentStatement() {
		if(!startsAssignmentStatement(nowReading)) {
			return syntaxErrorNode("assignmentStatement");
		}
		ParseNode target = parseTarget();
		Token assnToken = nowReading;
		expect(Punctuator.ASSIGN);
		ParseNode expr = parseExpression();
		expect(Punctuator.TERMINATOR);
		
		return AssignmentStatementNode.withChildren(assnToken, target, expr);
		
	}
	// assnStmt -> target := expr .
	private boolean startsAssignmentStatement(Token token) {
		return startsTarget(token);
	}	
	
	// indexedStmt -> array[index] .
	private ParseNode parseArrayIndexedStatement() {
		if(!startsArrayIndexedStatement(nowReading)) {
			return syntaxErrorNode("arrayIndexedStatement");
		}
		ParseNode target = parseIndexingExpression();
		Token assnToken = nowReading;
		expect(Punctuator.ASSIGN);
		ParseNode expr = parseExpression();
		expect(Punctuator.TERMINATOR);
		
		return AssignmentStatementNode.withChildren(assnToken, target, expr);
		
	}
	
	// assnStmt -> target := expr .
	private boolean startsArrayIndexedStatement(Token token) {
		return startsArrayIndexingExpression(token);
	}	
	
	// target -> identifier
	private ParseNode parseTarget() {
		if(!startsTarget(nowReading)) {
			return syntaxErrorNode("target");
		}
		return parseIdentifier();
	}
	private boolean startsTarget(Token token) {
		return startsIdentifier(token);
	}
	
	// This adds the printExpressions it parses to the children of the given parent
	// printExpressionList -> printExpression* bowtie (,|;)  (note that this is nullable)

	private PrintStatementNode parsePrintExpressionList(PrintStatementNode parent) {
		while(startsPrintExpression(nowReading) || startsPrintSeparator(nowReading)) {
			parsePrintExpression(parent);
			parsePrintSeparator(parent);
		}
		return parent;
	}
	

	// This adds the printExpression it parses to the children of the given parent
	// printExpression -> (expr | nl | tab)?     (nullable)
	
	private void parsePrintExpression(PrintStatementNode parent) {
		if(startsExpression(nowReading)) {
			ParseNode child = parseExpression();
			parent.appendChild(child);
		}
		else if(nowReading.isLextant(Keyword.NEWLINE)) {
			readToken();
			ParseNode child = new NewlineNode(previouslyRead);
			parent.appendChild(child);
		}
		else if(nowReading.isLextant(Keyword.TAB)) {
			readToken();
			ParseNode child = new TabNode(previouslyRead);
			parent.appendChild(child);
		}
		// else we interpret the printExpression as epsilon, and do nothing
	}
	private boolean startsPrintExpression(Token token) {
		return startsExpression(token) || token.isLextant(Keyword.NEWLINE, Keyword.TAB) ;
	}
	
	
	// This adds the printExpression it parses to the children of the given parent
	// printExpression -> expr?  ? ,? 
	
	private void parsePrintSeparator(PrintStatementNode parent) {
		if(!startsPrintSeparator(nowReading) && !nowReading.isLextant(Punctuator.TERMINATOR)) {
			ParseNode child = syntaxErrorNode("print separator");
			parent.appendChild(child);
			return;
		}
		
		if(nowReading.isLextant(Punctuator.SPACE)) {
			readToken();
			ParseNode child = new SpaceNode(previouslyRead);
			parent.appendChild(child);
		}
		else if(nowReading.isLextant(Punctuator.SEPARATOR)) {
			readToken();
		}		
		else if(nowReading.isLextant(Punctuator.TERMINATOR)) {
			// we're at the end of the bowtie and this printSeparator is not required.
			// do nothing.  Terminator is handled in a higher-level nonterminal.
		}
	}
	private boolean startsPrintSeparator(Token token) {
		return token.isLextant(Punctuator.SEPARATOR, Punctuator.SPACE) ;
	}
	
	
	// declaration -> (CONST | VAR) identifier := expression .
	private ParseNode parseDeclaration() {
		if(!startsDeclaration(nowReading)) {
			return syntaxErrorNode("declaration");
		}
		boolean isStatic = false;
		if(nowReading.isLextant(Keyword.STATIC)) {
			isStatic = true;
			readToken();
		}
		Token declarationToken = nowReading;
		DeclarationNode declarationNode = new DeclarationNode(declarationToken);
		declarationNode.setStatic(isStatic);
		readToken();
		
		ParseNode identifier = parseIdentifier();
		expect(Punctuator.ASSIGN);
		ParseNode initializer = parseExpression();
		expect(Punctuator.TERMINATOR);

		declarationNode.appendChild(identifier);
		declarationNode.appendChild(initializer);
		return declarationNode;
	}
	private boolean startsDeclaration(Token token) {
		return token.isLextant(Keyword.CONST, Keyword.VAR, Keyword.STATIC);
	}


	
	///////////////////////////////////////////////////////////
	// expressions
	// expr                     -> comparisonExpression
	// comparisonExpression     -> additiveExpression [>|>=|==|!=|<|<= additiveExpression]?
	// additiveExpression       -> multiplicativeExpression [+|- multiplicativeExpression]*  (left-assoc)
	// multiplicativeExpression -> atomicExpression [MULT|DIV atomicExpression]*  (left-assoc)
	// atomicExpression         -> literal
	// literal                  -> intNumber | identifier | booleanConstant

	// expr  -> ORExpression
	private ParseNode parseExpression() {		
		if(!startsExpression(nowReading)) {
			return syntaxErrorNode("expression");
		}
		if(startsORExpression(nowReading)) {
			return parseORExpression();
		}
		return syntaxErrorNode("expression");
	}
	private boolean startsExpression(Token token) {
		return startsORExpression(token);
	}
	
	private boolean startsParenthesis(Token token) {
		return token.isLextant(Punctuator.OPEN_PARENTHESIS); 
	}
	
	// FunctionDefinition -> func identifier LambdaExpression
	private ParseNode parseFunctionDefinition() {
		if(!startsFunctionDefinition(nowReading)) {
			return syntaxErrorNode("function definition");
		}
		FunctionDefinitionNode result = new FunctionDefinitionNode(nowReading);
		readToken();
		ParseNode left = parseIdentifier();
		result.appendChild(left);
		ParseNode right = parseLambdaExpression(); 
		result.appendChild(right);
		return result;
	}
	private boolean startsFunctionDefinition(Token token) {
		return token.isLextant(Keyword.FUNCTION); 
	}
		
	// lambdaExpression -> lambdaParamType { blockStatement }
	private ParseNode parseLambdaExpression() {
		if(!startsLambdaExpression(nowReading)) {
			return syntaxErrorNode("lambda expression");
		}
		LambdaNode result = new LambdaNode(nowReading);
		ParseNode left = parseLambdaType();
		result.appendChild(left);
		ParseNode right = parseFunctionBody(); 
		result.appendChild(right);
		return result;
	}
	private boolean startsLambdaExpression(Token token) {
		return token.isLextant(Punctuator.LESS); 
	}
	
	// lambdaParamType -> <parameterList?> -> type
	private ParseNode parseLambdaType() {
		Token lambdaTypeToken = nowReading;
		expect(Punctuator.LESS);
		LambdaTypeNode result = new LambdaTypeNode(lambdaTypeToken);
		
		if(startsType(nowReading)) {
			ParseNode parameter = parseType(); // parse first type
			result.appendChild(parameter);
			if(startsIdentifier(nowReading)) {
				result.appendChild(parseIdentifier());
			}
		}
		while(nowReading.isLextant(Punctuator.SEPARATOR)) {
			readToken(); // parse separator
			ParseNode parameter = parseType();
			result.appendChild(parameter);
			if(startsIdentifier(nowReading)) {
				result.appendChild(parseIdentifier());
			}
		}
		expect(Punctuator.GREATER);
		expect(Punctuator.LAMBDA);
		ParseNode returnType = parseType(); 
		result.appendChild(returnType);
		return result;
	}
	
	// castingExpression -> [Expression | type]?
	/*private ParseNode parseCastingExpression() {
		if(!startsCastingExpression(nowReading)) {
			return syntaxErrorNode("casting expression");
		}
		expect(Punctuator.OPEN_BRACKET);
		ParseNode left = parseExpression();
		if(nowReading.isLextant(Punctuator.CAST)) {
			Token castToken = nowReading;
			readToken();
			ParseNode right = parseType();
			
			left = BinaryOperatorNode.withChildren(castToken, left, right);
		}
		expect(Punctuator.CLOSE_BRACKET);
		return left;

	}
	private boolean startsCastingExpression(Token token) {
		return token.isLextant(Punctuator.OPEN_BRACKET); 
	}*/
	
	// ORExpression -> ANDExpression [|| ANDExpression]?
	private ParseNode parseORExpression() {
		if(!startsORExpression(nowReading)) {
			return syntaxErrorNode("OR expression");
		}
		
		ParseNode left = parseANDExpression();
		if(nowReading.isLextant(Punctuator.BOOLEAN_OR)) {
			Token BoolToken = nowReading;
			readToken();
			ParseNode right = parseANDExpression();
			
			return BinaryOperatorNode.withChildren(BoolToken, left, right);
		}
		return left;
	}
		
	private boolean startsORExpression(Token token) {
		return startsANDExpression(token);
	}
	
	// ANDExpression -> ComparisonExpression [&& ComparisonExpression]?
	private ParseNode parseANDExpression() {
		if(!startsANDExpression(nowReading)) {
			return syntaxErrorNode("AND expression");
		}

		ParseNode left = parseComparisonExpression();
		if(nowReading.isLextant(Punctuator.BOOLEAN_AND)) {
			Token BoolToken = nowReading;
			readToken();
			ParseNode right = parseComparisonExpression();
			
			return BinaryOperatorNode.withChildren(BoolToken, left, right);
		}
		return left;
	}
	
	private boolean startsANDExpression(Token token) {
		return startsComparisonExpression(token);
	}
		
	// comparisonExpression -> additiveExpression [>|>=|==|!=|<|<= additiveExpression]?
	private ParseNode parseComparisonExpression() {
		if(!startsComparisonExpression(nowReading)) {
			return syntaxErrorNode("comparison expression");
		}
		
		ParseNode left = parseAdditiveExpression();
		if(startsComparisonPunctuator(nowReading)) {
			Token compareToken = nowReading;
			readToken();
			ParseNode right = parseAdditiveExpression();
			
			return BinaryOperatorNode.withChildren(compareToken, left, right);
		}
		return left;

	}
	private boolean startsComparisonExpression(Token token) {
		return startsAdditiveExpression(token);
	}

	private boolean startsComparisonPunctuator(Token token) {
		return token.isLextant(Punctuator.GREATER, Punctuator.GREATER_EQ, Punctuator.EQUAL, 
				Punctuator.NOT_EQUAL, Punctuator.LESS, Punctuator.LESS_EQ);
	}
	
	// additiveExpression -> multiplicativeExpression [+ multiplicativeExpression]*  (left-assoc)
	private ParseNode parseAdditiveExpression() {
		if(!startsAdditiveExpression(nowReading)) {
			return syntaxErrorNode("additiveExpression");
		}
		
		ParseNode left = parseMultiplicativeExpression();
		while(nowReading.isLextant(Punctuator.ADD, Punctuator.SUBTRACT)) {
			Token additiveToken = nowReading;
			readToken();
			ParseNode right = parseMultiplicativeExpression();
			
			left = BinaryOperatorNode.withChildren(additiveToken, left, right);
		}
		return left;
	}
	private boolean startsAdditiveExpression(Token token) {
		return startsMultiplicativeExpression(token);
	}	

	// parseFunctionInvocation -> AtomicExpression((arguments...))?
	private ParseNode parseFunctionInvocation() {
		if(!startsFunctionInvocation(nowReading)) {
			return syntaxErrorNode("functionInvocation");
		}

		ParseNode base = parseAtomicExpression();
		if(nowReading.isLextant(Punctuator.OPEN_PARENTHESIS)) {
			ParseNode result = new FunctionInvocationNode(nowReading);
			result.appendChild(base); // Append the identifier
			expect(Punctuator.OPEN_PARENTHESIS);
			if(startsExpression(nowReading)) {
				base = parseExpression();
				result.appendChild(base);
				
				while(nowReading.isLextant(Punctuator.SEPARATOR)) {
					readToken(); // parse separator
					base = parseExpression();
					result.appendChild(base);
				}
			}
			expect(Punctuator.CLOSE_PARENTHESIS);
			return result;
		}
		return base;
	}
	private boolean startsFunctionInvocation(Token token) {
		return startsAtomicExpression(token);
	}
		
	// parseIndexingExpression -> parseFunctionInvocation([index(,index)?])?
	private ParseNode parseIndexingExpression() {
		if(!startsArrayIndexingExpression(nowReading)) {
			return syntaxErrorNode("arrayIndexingExpression");
		}

		ParseNode base = parseFunctionInvocation();
		while(nowReading.isLextant(Punctuator.OPEN_BRACKET)) {
			Token realToken = nowReading;
			// create a artificial ARRAY_INDEXING token after open bracket
			Token indexToken = LextantToken.artificial(realToken, Punctuator.ARRAY_INDEXING);
			
			readToken();
			ParseNode index = parseExpression();
			if(nowReading.isLextant(Punctuator.SEPARATOR)) {
				readToken(); // read separator
				ParseNode secondIndex = parseExpression();
				ParseNode sliceNode = new StringSliceNode(realToken);
				sliceNode.appendChild(base);
				sliceNode.appendChild(index);
				sliceNode.appendChild(secondIndex);
				expect(Punctuator.CLOSE_BRACKET);
				return sliceNode;
			}
			else {
				base = BinaryOperatorNode.withChildren(indexToken, base, index);
			}
			expect(Punctuator.CLOSE_BRACKET);
		}
		return base;
	}
	private boolean startsArrayIndexingExpression(Token token) {
		return startsFunctionInvocation(token);
	}
	
	// parsePrefixUnaryExpression -> [! | clone | length | reverse] ArrayIndexingExpression 
	// parsePrefixUnaryExpression -> zip array array lambda
	private ParseNode parsePrefixUnaryExpression() {
		if(!startsPrefixUnaryExpression(nowReading)) {
			return syntaxErrorNode("prefixUnaryExpression");
		}
		
		ParseNode right;
		if(startsZipOperator(nowReading)) {
			Token zipToken = nowReading;
			readToken();
			ParseNode array1 = parseExpression();
			expect(Punctuator.SEPARATOR);
			ParseNode array2 = parseExpression();
			expect(Punctuator.SEPARATOR);
			ParseNode lambda = parseExpression();
			
			ParseNode zipNode = new ZipOperatorNode(zipToken);
			zipNode.appendChild(array1);
			zipNode.appendChild(array2);
			zipNode.appendChild(lambda);
			return zipNode;
		}
		else if(startsUnaryOperator(nowReading)) {
			Token prefixUnaryToken = nowReading;
			readToken();
			right = parseIndexingExpression();
			
			ParseNode unaryNode = new UnaryOperatorNode(prefixUnaryToken);
			unaryNode.appendChild(right);
			return unaryNode;
		}
		right = parseIndexingExpression();
		return right;
	}
	private boolean startsPrefixUnaryExpression(Token token) {
		return startsUnaryPrefix(token) || startsZipOperator(token) || startsArrayIndexingExpression(token);
	}
	
	private boolean startsZipOperator(Token token) {
		return token.isLextant(Keyword.ZIP);
	}
	
	private boolean startsUnaryOperator(Token token) {
		return startsUnaryPrefix(token);
	}
	 
	// mapAndReduceExpression -> unaryExpression [map/reduce expression2]? (left-assoc)
	private ParseNode parseMapAndReduceExpression() {
		if(!startsMapAndReduceExpression(nowReading)) {
			return syntaxErrorNode("mapAndReduceExpression");
		}
		
		ParseNode left = parsePrefixUnaryExpression();
		if(nowReading.isLextant(Keyword.MAP)) {
			Token mapToken = nowReading;
			readToken();
			ParseNode right = parseExpression();
			return MapOperatorNode.withChildren(mapToken, left, right);
		}
		else if(nowReading.isLextant(Keyword.REDUCE)) {
			Token reduceToken = nowReading;
			readToken();
			ParseNode right = parseExpression();
			return ReduceOperatorNode.withChildren(reduceToken, left, right);
		}
		return left;
	}
	private boolean startsMapAndReduceExpression(Token token) {
		return startsPrefixUnaryExpression(token);
	}
	
	// foldExpression -> mapAndReduceExpression fold ([expression])? mapAndReduceExpression]*  (left-assoc)
	private ParseNode parseFoldExpression() {
		if(!startsFoldExpression(nowReading)) {
			return syntaxErrorNode("foldExpression");
		}
		
		ParseNode left = parseMapAndReduceExpression(); 
		if(nowReading.isLextant(Keyword.FOLD)) {
			Token foldToken = nowReading;
			readToken();
			
			FoldOperatorNode foldNode = new FoldOperatorNode(foldToken);
			foldNode.appendChild(left);
			
			if(nowReading.isLextant(Punctuator.OPEN_BRACKET)) {
				readToken();
				ParseNode baseValue = parseLiteral();
				foldNode.appendChild(baseValue);
				expect(Punctuator.CLOSE_BRACKET);
			}
			ParseNode right = parseMapAndReduceExpression();
			foldNode.appendChild(right);
			
			return foldNode;
		}
		return left;
	}
	private boolean startsFoldExpression(Token token) {
		return startsMapAndReduceExpression(token);
	}
		
	// multiplicativeExpression -> foldExpression [MULT/DIV/OVER/EXPRESSOVER/RATIONALIZE foldExpression]*  (left-assoc)
	private ParseNode parseMultiplicativeExpression() {
		if(!startsMultiplicativeExpression(nowReading)) {
			return syntaxErrorNode("multiplicativeExpression");
		}
		
		ParseNode left = parseFoldExpression(); 
		while(startsMultiplicativePunctuator(nowReading)) {
			Token multiplicativeToken = nowReading;
			readToken();
			ParseNode right = parseFoldExpression();
			
			left = BinaryOperatorNode.withChildren(multiplicativeToken, left, right);
		}
		return left;
	}
	private boolean startsMultiplicativeExpression(Token token) {
		return startsFoldExpression(token);
	}
	
	private boolean startsMultiplicativePunctuator(Token token) {
		return token.isLextant(Punctuator.MULTIPLY, Punctuator.DIVIDE, 
				Punctuator.OVER, Punctuator.EXPRESS_OVER, Punctuator.RATIONALIZE);
	}
	
	// atomicExpression -> emptyArray | parenthesis | PopulatedArray | casting | literal
	private ParseNode parseAtomicExpression() {
		if(!startsAtomicExpression(nowReading)) {
			return syntaxErrorNode("atomic expression");
		}
		if(startsEmptyArrayCreation(nowReading)) {
			ParseNode expression = parseEmptyArrayCreation();
			return expression;
		}
		if(startsParenthesis(nowReading)) {
			expect(Punctuator.OPEN_PARENTHESIS);
			ParseNode expression = parseExpression();
			expect(Punctuator.CLOSE_PARENTHESIS);
			return expression;
		}
		
		if(startsCastOrPopulatedArrayCreation(nowReading)) {
			ParseNode expression = parseCastOrPopulatedArrayCreation();
			return expression;
		}
		
		if(startsLambdaExpression(nowReading)) {
			ParseNode expression = parseLambdaExpression();
			return expression;
		}
		
		/*if(startsCastingExpression(nowReading)) {
			ParseNode expression = parseCastingExpression();
			return expression;
		}*/
		return parseLiteral();
	}
	
	// emptyArray -> alloc [arrayType] (IntNumber)
	private ParseNode parseEmptyArrayCreation() {
		if(!startsEmptyArrayCreation(nowReading)) {
			return syntaxErrorNode("empty array creation");
		}
		Token allocToken = nowReading;
		readToken();
		
		ParseNode type = parseType();
		expect(Punctuator.OPEN_PARENTHESIS);
		ParseNode right = parseIntNumber();
		expect(Punctuator.CLOSE_PARENTHESIS);
		return BinaryOperatorNode.withChildren(allocToken, type, right);
	}
	private boolean startsEmptyArrayCreation(Token token) {
		return token.isLextant(Keyword.ALLOC);
	}
	
	/*// populatedArray -> [expressionList]
	private ParseNode parsePopulatedArrayCreation() {
		if(!startsPopulatedArrayCreation(nowReading)) {
			return syntaxErrorNode("populated array creation");
		}
		ParseNode result = new PopulatedArrayNode(nowReading);
		readToken();
		ParseNode element = parseExpression(); // parse first element
		result.appendChild(element);
		
		while(nowReading.isLextant(Punctuator.SEPARATOR)) {
			readToken(); // parse separator
			element = parseExpression();
			result.appendChild(element);
		}
		expect(Punctuator.CLOSE_BRACKET);
		return result;
	}
	private boolean startsPopulatedArrayCreation(Token token) {
		return token.isLextant(Punctuator.OPEN_BRACKET);
	}*/
	
	// populatedArray -> [expressionList]
	// castingExpression -> [Expression | type]?
	private ParseNode parseCastOrPopulatedArrayCreation() {
		if(!startsCastOrPopulatedArrayCreation(nowReading)) {
			return syntaxErrorNode("populated array creation");
		}
		expect(Punctuator.OPEN_BRACKET);
		ParseNode left = parseExpression();
		if(nowReading.isLextant(Punctuator.CAST)) {
			Token castToken = nowReading;
			readToken();
			ParseNode right = parseType();
			
			left = BinaryOperatorNode.withChildren(castToken, left, right);
		}
		else {
			ParseNode result = new PopulatedArrayNode(nowReading);
			result.appendChild(left);
			while(nowReading.isLextant(Punctuator.SEPARATOR)) {
				readToken(); // parse separator
				ParseNode element = parseExpression();
				result.appendChild(element);
			}
			expect(Punctuator.CLOSE_BRACKET);
			return result;
		}
		expect(Punctuator.CLOSE_BRACKET);
		return left;
	}
	private boolean startsCastOrPopulatedArrayCreation(Token token) {
		return token.isLextant(Punctuator.OPEN_BRACKET);
	}
	
	private boolean startsAtomicExpression(Token token) {
		return startsLiteral(token) || 
				startsParenthesis(token) || 
				startsCastOrPopulatedArrayCreation(token) || 
				startsEmptyArrayCreation(token) ||
				startsLambdaExpression(token) ;
				//|| startsCastingExpression(token);
	}
	
	// literal -> number | float | identifier | boolean | character | string
	private ParseNode parseLiteral() {
		if(!startsLiteral(nowReading)) {
			return syntaxErrorNode("literal");
		}
		
		if(startsIntNumber(nowReading)) {
			return parseIntNumber();
		}
		if(startsFloatNumber(nowReading)) {
			return parseFloatNumber();
		}
		if(startsIdentifier(nowReading)) {
			return parseIdentifier();
		}
		if(startsBooleanConstant(nowReading)) {
			return parseBooleanConstant();
		}
		if(startsCharacterConstant(nowReading)) {
			return parseCharacterConstant();
		}
		if(startsStringConstant(nowReading)) {
			return parseStringConstant();
		}

		return syntaxErrorNode("literal");
	}
	private boolean startsLiteral(Token token) {
		return startsIntNumber(token) || 
				startsFloatNumber(token) || 
				startsIdentifier(token) || 
				startsCharacterConstant(token) || 
				startsStringConstant(token) || 
				startsBooleanConstant(token);
	}

	private boolean startsUnaryPrefix(Token token) {
		return token.isLextant(Keyword.CLONE, Punctuator.BOOLEAN_NOT, Keyword.LENGTH, Keyword.REVERSE);
	}
	
	// character (terminal)
	private ParseNode parseCharacterConstant() {
		if(!startsCharacterConstant(nowReading)) {
			return syntaxErrorNode("character constant");
		}
		readToken();
		return new CharacterConstantNode(previouslyRead);
	}
	private boolean startsCharacterConstant(Token token) {
		return token instanceof CharacterToken;
	}
	
	// string (terminal)
	private ParseNode parseStringConstant() {
		if(!startsStringConstant(nowReading)) {
			return syntaxErrorNode("string constant");
		}
		readToken();
		return new StringConstantNode(previouslyRead);
	}
	private boolean startsStringConstant(Token token) {
		return token instanceof StringToken;
	}
		
	// number (terminal)
	private ParseNode parseIntNumber() {
		if(!startsIntNumber(nowReading)) {
			return syntaxErrorNode("integer constant");
		}
		readToken();
		return new IntegerConstantNode(previouslyRead);
	}
	private boolean startsIntNumber(Token token) {
		return token instanceof IntegerToken;
	}
	
	// float (terminal)
	private ParseNode parseFloatNumber() {
		if(!startsFloatNumber(nowReading)) {
			return syntaxErrorNode("float constant");
		}
		readToken();
		return new FloatConstantNode(previouslyRead);
	}
	private boolean startsFloatNumber(Token token) {
		return token instanceof FloatingToken;
	}

	// Type (terminal)
	private ParseNode parseType() {
		if(!startsType(nowReading)) {
			return syntaxErrorNode("type");
		}
		if(startsArrayType(nowReading)) {
			ParseNode arrayType = parseArrayType();
			expect(Punctuator.CLOSE_BRACKET);
			return arrayType;
		}
		readToken();
		return new TypeNode(previouslyRead);
	}
	private boolean startsType(Token token) {
		return token instanceof TypeToken || token.isLextant(Punctuator.OPEN_BRACKET);
	}
	
	// Array type
	private ParseNode parseArrayType() {
		if(!startsArrayType(nowReading)) {
			return syntaxErrorNode("arrayType");
		}
		readToken();
		ParseNode arrayNode = new ArrayNode(previouslyRead);
		ParseNode subType = parseType();
		arrayNode.appendChild(subType);
		return arrayNode;
	}

	private boolean startsArrayType(Token token) {
		return token.isLextant(Punctuator.OPEN_BRACKET);
	}
	
	// identifier (terminal)
	private ParseNode parseIdentifier() {
		if(!startsIdentifier(nowReading)) {
			return syntaxErrorNode("identifier");
		}
		readToken();
		return new IdentifierNode(previouslyRead);
	}
	private boolean startsIdentifier(Token token) {
		return token instanceof IdentifierToken;
	}

	// boolean constant (terminal)
	private ParseNode parseBooleanConstant() {
		if(!startsBooleanConstant(nowReading)) {
			return syntaxErrorNode("boolean constant");
		}
		readToken();
		return new BooleanConstantNode(previouslyRead);
	}
	private boolean startsBooleanConstant(Token token) {
		return token.isLextant(Keyword.TRUE, Keyword.FALSE);
	}

	private void readToken() {
		previouslyRead = nowReading;
		nowReading = scanner.next();
	}	
	
	// if the current token is one of the given lextants, read the next token.
	// otherwise, give a syntax error and read next token (to avoid endless looping).
	private void expect(Lextant ...lextants ) {
		if(!nowReading.isLextant(lextants)) {
			syntaxError(nowReading, "expecting " + Arrays.toString(lextants));
		}
		readToken();
	}	
	private ErrorNode syntaxErrorNode(String expectedSymbol) {
		syntaxError(nowReading, "expecting " + expectedSymbol);
		ErrorNode errorNode = new ErrorNode(nowReading);
		readToken();
		return errorNode;
	}
	private void syntaxError(Token token, String errorDescription) {
		String message = "" + token.getLocation() + " " + errorDescription;
		error(message);
	}
	private void error(String message) {
		PikaLogger log = PikaLogger.getLogger("compiler.Parser");
		log.severe("syntax error: " + message);
	}	
}

