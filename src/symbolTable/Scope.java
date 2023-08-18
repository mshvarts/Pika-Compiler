package symbolTable;

import inputHandler.TextLocation;
import logging.PikaLogger;
import parseTree.nodeTypes.IdentifierNode;
import semanticAnalyzer.types.PrimitiveType;
import semanticAnalyzer.types.Type;
import tokens.Token;

public class Scope {
	private Scope baseScope;
	private MemoryAllocator allocator;
	private SymbolTable symbolTable;
	private static int staticCounter = 0;
	
//////////////////////////////////////////////////////////////////////
// factories

	public static Scope createProgramScope() {
		return new Scope(programScopeAllocator(), nullInstance());
	}
	public Scope createSubscope() {
		return new Scope(allocator, this);
	}

	public Scope createParameterScope() {
		return new Scope(parameterScopeAllocator(), this);
	}
	
	public Scope createProcedureScope() {
		MemoryAllocator procedureAllocator = procedureScopeAllocator();
		procedureAllocator.allocate(8); // allocate 8 bytes of memory
		return new Scope(procedureAllocator, this); 
	}
	
	private static MemoryAllocator programScopeAllocator() {
		return new PositiveMemoryAllocator(
				MemoryAccessMethod.DIRECT_ACCESS_BASE, 
				MemoryLocation.GLOBAL_VARIABLE_BLOCK);
	}

	private static MemoryAllocator procedureScopeAllocator() {
		return new NegativeMemoryAllocator(
				MemoryAccessMethod.INDIRECT_ACCESS_BASE, 
				MemoryLocation.FRAME_POINTER);
	}
	
	private static MemoryAllocator parameterScopeAllocator() {
		return new ParameterMemoryAllocator(
				MemoryAccessMethod.INDIRECT_ACCESS_BASE, 
				MemoryLocation.FRAME_POINTER);
	}
	
//////////////////////////////////////////////////////////////////////
// private constructor.	
	private Scope(MemoryAllocator allocator, Scope baseScope) {
		super();
		this.baseScope = (baseScope == null) ? this : baseScope;
		this.symbolTable = new SymbolTable();
		
		this.allocator = allocator;
	}
	
///////////////////////////////////////////////////////////////////////
//  basic queries	
	public Scope getBaseScope() {
		return baseScope;
	}
	public MemoryAllocator getAllocationStrategy() {
		return allocator;
	}
	public SymbolTable getSymbolTable() {
		return symbolTable;
	}
	
///////////////////////////////////////////////////////////////////////
//memory allocation
	// must call leave() when destroying/leaving a scope.
	public void leave() {
		allocator.restoreState();
	}
	public int getAllocatedSize() {
		return allocator.getMaxAllocatedSize();
	}
	public void enter() {
		allocator.saveState();
	}
///////////////////////////////////////////////////////////////////////
//bindings
	public Binding createStaticBinding(IdentifierNode identifierNode, Type type, boolean isVar) {
		Token token = identifierNode.getToken();
		symbolTable.errorIfAlreadyDefined(token);

		String lexeme = token.getLexeme() + "-static-" + staticCounter++;
		Binding binding = allocateNewBinding(type, isVar, token.getLocation(), lexeme, true);	
		symbolTable.install(lexeme, binding);

		return binding;
	}
	
	public Binding createCompanionBinding(IdentifierNode identifierNode) {
		Token token = identifierNode.getToken();
		symbolTable.errorIfAlreadyDefined(token);

		String lexeme = token.getLexeme() + "-companion-" + staticCounter;
		Binding binding = allocateNewBinding(PrimitiveType.CHARACTER, true, token.getLocation(), lexeme, true);	
		symbolTable.install(lexeme, binding);

		return binding;
	}
	
	public Binding createBinding(IdentifierNode identifierNode, Type type, boolean isVar, boolean isStatic) {
		Token token = identifierNode.getToken();
		symbolTable.errorIfAlreadyDefined(token);

		String lexeme = token.getLexeme();
		Binding binding = allocateNewBinding(type, isVar, token.getLocation(), lexeme, isStatic);	
		symbolTable.install(lexeme, binding);

		return binding;
	}
	private Binding allocateNewBinding(Type type, boolean isVar, TextLocation textLocation, String lexeme, boolean isStatic) {
		MemoryLocation memoryLocation = allocator.allocate(type.getSize());
		return new Binding(type, isVar, textLocation, memoryLocation, lexeme, isStatic);
	}
	
	public Binding createFunctionBinding(IdentifierNode identifierNode, Type type) {
		Token token = identifierNode.getToken();
		symbolTable.errorIfAlreadyDefined(token);

		String lexeme = token.getLexeme();
		Binding binding = allocateNewBinding(type, token.getLocation(), lexeme);	
		symbolTable.install(lexeme, binding);

		return binding;
	}
	private Binding allocateNewBinding(Type type, TextLocation textLocation, String lexeme) {
		MemoryLocation memoryLocation = new MemoryLocation(
				MemoryAccessMethod.INDIRECT_ACCESS_BASE, 
				MemoryLocation.FRAME_POINTER, 0);
		return new Binding(type, true, textLocation, memoryLocation, lexeme, false);
	}
	
///////////////////////////////////////////////////////////////////////
//toString
	public String toString() {
		String result = "scope: ";
		result += " hash "+ hashCode() + "\n";
		result += symbolTable;
		return result;
	}

////////////////////////////////////////////////////////////////////////////////////
//Null Scope object - lazy singleton (Lazy Holder) implementation pattern
	public static Scope nullInstance() {
		return NullScope.instance;
	}
	private static class NullScope extends Scope {
		private static NullScope instance = new NullScope();

		private NullScope() {
			super(	new PositiveMemoryAllocator(MemoryAccessMethod.NULL_ACCESS, "", 0),
					null);
		}
		public String toString() {
			return "scope: the-null-scope";
		}
		@Override
		public Binding createBinding(IdentifierNode identifierNode, Type type, boolean isVar, boolean isStatic) {
			unscopedIdentifierError(identifierNode.getToken());
			return super.createBinding(identifierNode, type, isVar, isStatic);
		}
		// subscopes of null scope need their own strategy.  Assumes global block is static.
		public Scope createSubscope() {
			return new Scope(programScopeAllocator(), this);
		}
	}


///////////////////////////////////////////////////////////////////////
//error reporting
	private static void unscopedIdentifierError(Token token) {
		PikaLogger log = PikaLogger.getLogger("compiler.scope");
		log.severe("variable " + token.getLexeme() + 
				" used outside of any scope at " + token.getLocation());
	}

}
