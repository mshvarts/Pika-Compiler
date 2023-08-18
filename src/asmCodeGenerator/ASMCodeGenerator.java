package asmCodeGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMOpcode;
import asmCodeGenerator.runtime.MemoryManager;
import asmCodeGenerator.runtime.RunTime;
import lexicalAnalyzer.Keyword;
import lexicalAnalyzer.Lextant;
import lexicalAnalyzer.Punctuator;
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
import parseTree.nodeTypes.IdentifierNode;
import parseTree.nodeTypes.IfStatementNode;
import parseTree.nodeTypes.IntegerConstantNode;
import parseTree.nodeTypes.LambdaNode;
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
import semanticAnalyzer.types.Array;
import semanticAnalyzer.types.Function;
import semanticAnalyzer.types.Lambda;
import semanticAnalyzer.types.PrimitiveType;
import semanticAnalyzer.types.Type;
import symbolTable.Binding;
import symbolTable.Scope;
import static asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType.*;
import static asmCodeGenerator.codeStorage.ASMOpcode.*;
import static asmCodeGenerator.runtime.RunTime.STRING_HEADER_SIZE;
import static asmCodeGenerator.runtime.RunTime.getStoreOpcode;
import static asmCodeGenerator.runtime.RunTime.getLoadOpcode;

// do not call the code generator if any errors have occurred during analysis.
public class ASMCodeGenerator {
	ParseNode root;

	public static ASMCodeFragment generate(ParseNode syntaxTree) {
		ASMCodeGenerator codeGenerator = new ASMCodeGenerator(syntaxTree);
		return codeGenerator.makeASM();
	}
	public ASMCodeGenerator(ParseNode root) {
		super();
		this.root = root;
	}
	
	public ASMCodeFragment makeASM() {
		ASMCodeFragment code = new ASMCodeFragment(GENERATES_VOID);
		
		code.append( MemoryManager.codeForInitialization() );
		code.append( RunTime.getEnvironment() );
		code.append( globalVariableBlockASM() );
		code.append( programASM() );
		code.append( MemoryManager.codeForAfterApplication() );
		
		return code;
	}
	private ASMCodeFragment globalVariableBlockASM() {
		assert root.hasScope();
		Scope scope = root.getScope();
		int globalBlockSize = scope.getAllocatedSize();
		
		ASMCodeFragment code = new ASMCodeFragment(GENERATES_VOID);
		code.add(DLabel, RunTime.GLOBAL_MEMORY_BLOCK);
		code.add(DataZ, globalBlockSize);
		return code;
	}
	private ASMCodeFragment programASM() {
		ASMCodeFragment code = new ASMCodeFragment(GENERATES_VOID);
		
		code.add(    Label, RunTime.MAIN_PROGRAM_LABEL);
		code.append( programCode());
		code.add(    Halt );
		
		return code;
	}
	private ASMCodeFragment programCode() {
		CodeVisitor visitor = new CodeVisitor();
		root.accept(visitor);
		return visitor.removeRootCode(root);
	}
	
	protected class CodeVisitor extends ParseNodeVisitor.Default {
		private Map<ParseNode, ASMCodeFragment> codeMap;
		ASMCodeFragment code;
		
		public CodeVisitor() {
			codeMap = new HashMap<ParseNode, ASMCodeFragment>();
		}


		////////////////////////////////////////////////////////////////////
        // Make the field "code" refer to a new fragment of different sorts.
		private void newAddressCode(ParseNode node) {
			code = new ASMCodeFragment(GENERATES_ADDRESS);
			codeMap.put(node, code);
		}
		private void newValueCode(ParseNode node) {
			code = new ASMCodeFragment(GENERATES_VALUE);
			codeMap.put(node, code);
		}
		private void newVoidCode(ParseNode node) {
			code = new ASMCodeFragment(GENERATES_VOID);
			codeMap.put(node, code);
		}

	    ////////////////////////////////////////////////////////////////////
        // Get code from the map.
		private ASMCodeFragment getAndRemoveCode(ParseNode node) {
			ASMCodeFragment result = codeMap.get(node);
			codeMap.remove(node);
			//codeMap.remove(result);
			return result;
		}
	    public  ASMCodeFragment removeRootCode(ParseNode tree) {
			return getAndRemoveCode(tree);
		}		
		ASMCodeFragment removeValueCode(ParseNode node) {
			ASMCodeFragment frag = getAndRemoveCode(node);
			makeFragmentValueCode(frag, node);
			return frag;
		}		
		private ASMCodeFragment removeAddressCode(ParseNode node) {
			ASMCodeFragment frag = getAndRemoveCode(node);
			assert frag.isAddress();
			return frag;
		}		
		ASMCodeFragment removeVoidCode(ParseNode node) {
			ASMCodeFragment frag = getAndRemoveCode(node);
			assert frag.isVoid();
			return frag;
		}
		
	    ////////////////////////////////////////////////////////////////////
        // convert code to value-generating code.
		private void makeFragmentValueCode(ASMCodeFragment code, ParseNode node) {
			assert !code.isVoid();
			
			if(code.isAddress()) {
				turnAddressIntoValue(code, node);
				code.markAsValue();
			}	
		}
		private void turnAddressIntoValue(ASMCodeFragment code, ParseNode node) {
			Type nodeType = node.getType();
			
			/*if(node.getType() instanceof Lambda) {
				nodeType = ((Lambda)node.getType()).getReturnType();
			}*/
			if(node.getType() instanceof Function) {
				nodeType = ((Function)node.getType()).getReturnType();
			}
			
			if(node.getType() instanceof Lambda) {
				code.add(LoadI);
			}
			else if(nodeType == PrimitiveType.INTEGER) {
				code.add(LoadI);
			}	
			else if(nodeType == PrimitiveType.FLOATING) {
				code.add(LoadF);
			}
			else if(nodeType == PrimitiveType.BOOLEAN) {
				code.add(LoadC);
			}	
			else if(nodeType == PrimitiveType.STRING) {
				code.add(LoadI);
			}	
			else if(nodeType == PrimitiveType.CHARACTER) {
				code.add(LoadC);
			}	
			else if(nodeType == PrimitiveType.RATIONAL) {
				Macros.storeITo(code, RunTime.RATIONAL_ADDRESS_TEMP);

				// load numerator
				Macros.loadIFrom(code, RunTime.RATIONAL_ADDRESS_TEMP);
				code.add(LoadI); // [... num] 
				
				// load denominator
				Macros.loadIFrom(code, RunTime.RATIONAL_ADDRESS_TEMP);
				code.add(PushI, 4); 
				code.add(Add); 
				code.add(LoadI); // [... num den]
			}	
			else if(nodeType instanceof Array) {
				/*if(node instanceof BinaryOperatorNode) { } 
				else */
				
				code.add(LoadI); 	// [... addr]		
			}
			else if(nodeType == PrimitiveType.NO_TYPE) { // null function return type
				// Return label to function start
			}
			else {
				assert false : "node " + node;
			}
		}
		
	    ////////////////////////////////////////////////////////////////////
        // ensures all types of ParseNode in given AST have at least a visitLeave	
		public void visitLeave(ParseNode node) {
			assert false : "node " + node + " not handled in ASMCodeGenerator";
		}
		
		/////////////////////////////////////////////////////////////////////
		
		public void visitLeave(TypeNode node) {
			newValueCode(node);
		}
		
		/////////////////////////////////////////////////////////////////////
		
		public void visitLeave(StringSliceNode node) {
			newValueCode(node);
			
			ASMCodeFragment strIdentifier = removeValueCode(node.child(0));
			ASMCodeFragment arg1 = removeValueCode(node.child(1));
			ASMCodeFragment arg2 = removeValueCode(node.child(2));
			
			code.append(strIdentifier);
			code.append(arg1);
			code.append(arg2);
			
			ASMCodeFragment frag = new StringSliceCodeGenerator().generate(node);
			code.append(frag);
		}
		
		/////////////////////////////////////////////////////////////////////
		
		public void visitEnter(ForStatementNode node) {
			
			Labeller labeller = new Labeller("for");
			String continueLabel = labeller.newLabel("continue");
			String endLabel = labeller.newLabel("end");
			
			node.setBreakLabel(endLabel);
			node.setContinueLabel(continueLabel);
			
		}
		
		public void visitLeave(ForStatementNode node) {
			newVoidCode(node);
			
			ASMCodeFragment identifier = removeAddressCode(node.child(0));
			ASMCodeFragment sequence = removeValueCode(node.child(1));
			ASMCodeFragment body = removeVoidCode(node.child(2));
			
			Labeller labeller = new Labeller("for");
			String startLabel = labeller.newLabel("start");
			
			String continueLabel = node.getContinueLabel();
			String endLabel = node.getBreakLabel();
			
			code.append(sequence);										// [... addrSequence]
			
			Macros.storeITo(code, RunTime.TEMP_ADDR_STORAGE);
			Macros.loadIFrom(code, RunTime.TEMP_ADDR_STORAGE);
			
			if(node.child(1).getType() == PrimitiveType.STRING) {
				code.add(PushI, STRING_HEADER_SIZE);					// subtract header size
				code.add(Subtract);
				Macros.readIOffset(code, RunTime.STRING_LENGTH_OFFSET);	// [... length]
				Macros.storeITo(code, RunTime.LENGTH_TEMPORARY);		// [...]
			}
			else if(node.child(1).getType() instanceof Array) {
				Macros.readIOffset(code, RunTime.ARRAY_LENGTH_OFFSET);	// [... length]
				//code.add(PStack);
				Macros.storeITo(code, RunTime.LENGTH_TEMPORARY);		// [...]
			}
			code.add(PushI, 0);
			Macros.storeITo(code, RunTime.COUNTER_TEMPORARY);
			
			code.add(Label, startLabel);
			
			// update identifier index
			if(node.isIndexLoop()) {
				code.append(identifier);			// [... addrCounter]
				Macros.loadIFrom(code, RunTime.COUNTER_TEMPORARY);	// [... addrCounter counter]
				code.add(StoreI);					// [...]
			}
			else {
				if(node.child(1).getType() == PrimitiveType.STRING) {
					Macros.loadIFrom(code, RunTime.TEMP_ADDR_STORAGE);	// [... strElems]
					Macros.loadIFrom(code, RunTime.COUNTER_TEMPORARY);	// [... strElems counter]
					code.add(Add);						// [... strElems+counter]
					code.add(LoadC);					// [... elemVal]
					code.append(identifier);			// [... elementVal addrCounter]
					code.add(Exchange);					// [... addrCounter elementVal]
					code.add(StoreC);					// [...]
				}
				else if(node.child(1).getType() instanceof Array) {
					Type subType = ((Array)node.child(1).getType()).getSubType();
					Macros.loadIFrom(code, RunTime.TEMP_ADDR_STORAGE);	// [... addrSequence]
					code.add(PushI, RunTime.ARRAY_HEADER_SIZE);
					code.add(Add);
					Macros.loadIFrom(code, RunTime.COUNTER_TEMPORARY);	// [... arrElems counter]
					code.add(PushI, subType.getSize());	// [... arrElems counter typeSize]
					code.add(Multiply);					// [... arrElems counter*typeSize]
					code.add(Add);						// [... arrElems+counter*typeSize]
					code.append(getLoadOpcode(subType));// [... elementVal]
					code.append(identifier);			// [... elementVal addrCounter]
					code.add(Exchange);					// [... addrCounter elementVal]
					code.append(getStoreOpcode(subType));// [...]
				}
				else {
					assert false: "Could not determine the sequence node type";
				}
			}
			
			code.append(body);
			
			code.add(Label, continueLabel);
			
			Macros.loadIFrom(code, RunTime.LENGTH_TEMPORARY); 	// [... length]
			code.add(PushI, 1);									// [... length 1]
			code.add(Subtract);									// [... length-1]
			Macros.loadIFrom(code, RunTime.COUNTER_TEMPORARY);	// [... length-1 counter]
			code.add(Subtract);									// [... length-1-counter]
			code.add(JumpFalse, endLabel);						// [...]
			
			Macros.incrementInteger(code, RunTime.COUNTER_TEMPORARY);
			
			code.add(Jump, startLabel);
			code.add(Label, endLabel);
			
		}
		
		/////////////////////////////////////////////////////////////////////
		
		public void visitEnter(WhileStatementNode node) {
			Labeller labeller = new Labeller("while");
			String startLabel = labeller.newLabel("start");
			String endLabel = labeller.newLabel("end");
			
			node.setBreakLabel(endLabel);
			node.setContinueLabel(startLabel);
		}
		
		public void visitLeave(WhileStatementNode node) {
			
			newVoidCode(node);
			
			ASMCodeFragment condition = removeValueCode(node.child(0));
			ASMCodeFragment thenClause = removeVoidCode(node.child(1));
			
			String startLabel = node.getContinueLabel();
			String endLabel = node.getBreakLabel();
			
			code.add(Label, startLabel);
			
			code.append(condition);
			code.add(JumpFalse, endLabel);
			code.append(thenClause);
			code.add(Jump, startLabel);
			
			code.add(Label, endLabel);
			
		}
		
		/////////////////////////////////////////////////////////////////////
				
		public void visitLeave(IfStatementNode node) {
			Labeller labeller = new Labeller("if");
			String falseLabel = labeller.newLabel("false");
			String endLabel = labeller.newLabel("end");
			
			newVoidCode(node);
			
			ASMCodeFragment condition = removeValueCode(node.child(0));
			ASMCodeFragment thenClause = removeVoidCode(node.child(1));
			
			code.append(condition);
			code.add(JumpFalse, falseLabel);
			code.append(thenClause);
			code.add(Jump, endLabel);
			
			code.add(Label, falseLabel);
			
			if(hasElseClause(node)) {
				ASMCodeFragment elseClause = removeVoidCode(node.child(2));
				code.append(elseClause);
			}
			
			code.add(Label, endLabel);
		}

		private boolean hasElseClause(IfStatementNode node) {
			return node.nChildren() == 3;
		}
		
		///////////////////////////////////////////////////////////////////////////
		// constructs larger than statements
		public void visitLeave(ProgramNode node) {
			newVoidCode(node);
			for(ParseNode child : node.getChildren()) {
				ASMCodeFragment childCode = removeVoidCode(child);
				code.append(childCode);
			}
		}
		public void visitLeave(BlockStatementNode node) {
			newVoidCode(node);
			for(ParseNode child : node.getChildren()) { // block inside exec 
				ASMCodeFragment childCode = removeVoidCode(child);
				code.append(childCode);
			}
		}
		
		public void visitEnter(FunctionBodyNode node) {
			newVoidCode(node);
			
			Labeller labeller = new Labeller("function");
			String startLabel = labeller.newLabel("start");
			String endLabel = labeller.newLabel("end");
			String exitLabel = labeller.newLabel("exit");
			
			node.setExitLabel(exitLabel);
			node.setEndLabel(endLabel);
			node.setStartLabel(startLabel);
			
			code.add(PushPC);
			code.add(Jump, endLabel);
			// code.add(Label, node.getStartLabel());
		}
		
		public void visitLeave(FunctionBodyNode node) {
			ASMCodeFragment visitEnterCode = removeVoidCode(node);
			
			newVoidCode(node);
			code.append(visitEnterCode);		
			
			code.add(Label, node.getStartLabel());
			
			// callee enter handshake
			int routineFrameSize = 8;	 // space for dynamic link and return address
			
			// calculate arguments size
			ParseNode lambdaNode = node.getParent();
			int argumentsSize = 0;
			Lambda lambdaType = ((Lambda)lambdaNode.getType());
			List<Type> params = lambdaType.getParameters();
			for (Type param : params) {
				argumentsSize += param.getSize();
			}

			// store value of old frame pointer (a.k.a dynamic link) below the current stack pointer
			Macros.loadIFrom(code, RunTime.STACK_POINTER);	// [... returnAddr sp]
			code.add(PushI, -4);							// [... returnAddr sp -4]
			code.add(Add);									// [... returnAddr sp-4]
			Macros.loadIFrom(code, RunTime.FRAME_POINTER); 	// [... returnAddr sp-4 fp]
			code.add(StoreI);								// [... returnAddr]
			
			// store return address below dynamic link
			Macros.loadIFrom(code, RunTime.STACK_POINTER);	// [... returnAddr sp]
			code.add(PushI, -8);							// [... returnAddr sp -8]
			code.add(Add);									// [... returnAddr sp-8]
			code.add(Exchange); 							// [... sp-8 returnAddr]
			code.add(StoreI);								// [...]
			
			// set frame pointer to the stack pointer
			Macros.loadIFrom(code, RunTime.STACK_POINTER);	// [... sp]
			Macros.storeITo(code, RunTime.FRAME_POINTER);	// [...]
			
			// decrease stack pointer by 8
			code.add(PushI, -8);							// [... -8]
			Macros.addITo(code, RunTime.STACK_POINTER);		// [...]
			
			// decrease stack pointer by all memory necessary for the storage of local variables and statements
			for(ParseNode child : node.getChildren()) {
				code.add(PushI, -child.getType().getSize()); 
				Macros.addITo(code, RunTime.STACK_POINTER);
				routineFrameSize += child.getType().getSize();
			}
			for(ParseNode child : node.getChildren()) {
				ASMCodeFragment childCode = removeVoidCode(child);
				code.append(childCode);
				//code.add(PStack);
			}
			
			code.add(Label, node.getExitLabel());
			Type returnType;
			
			// callee exit handshake
			if(node.getParent().getType() instanceof Function) {
				returnType = ((Function)node.getParent().getType()).getReturnType();
			}
			else {
				returnType = ((Lambda)node.getParent().getType()).getReturnType();
			}

			if(returnType == PrimitiveType.NO_TYPE) { 
				// return type is null
				Macros.loadIFrom(code, RunTime.FRAME_POINTER); 	// [... fp]
				Macros.readIOffset(code, -8);  					// [... returnAddr]
				Macros.loadIFrom(code, RunTime.FRAME_POINTER); 	// [... returnAddr fp]
				Macros.readIOffset(code, -4); 					// [... returnAddr dynamic_link]
				Macros.storeITo(code, RunTime.FRAME_POINTER);	// [... returnAddr]
				
				code.add(PushI, routineFrameSize);				// [... returnAddr localVarsAndLinkSize]								// [... returnAddr returnValue frameSize]
				code.add(PushI, argumentsSize);					// [... returnAddr frameSize argsSize]
				code.add(Add);									// [... returnAddr frameSize+argsSize]
				Macros.addITo(code, RunTime.STACK_POINTER);		// [... returnAddr]
			}
			else if(returnType == PrimitiveType.RATIONAL) {
				
				Macros.storeITo(code, RunTime.RATIONAL_DENOMINATOR_TEMP); // [... num]
				Macros.storeITo(code, RunTime.RATIONAL_NUMERATOR_TEMP); // [...]
				Macros.loadIFrom(code, RunTime.FRAME_POINTER); 	// [... fp]
				Macros.readIOffset(code, -8);  					// [... returnAddr]
				Macros.loadIFrom(code, RunTime.FRAME_POINTER); 	// [... returnAddr fp]
				Macros.readIOffset(code, -4); 					// [... returnAddr dynamic_link]
				Macros.storeITo(code, RunTime.FRAME_POINTER);	// [... returnAddr]
				
				code.add(PushI, routineFrameSize);				// [... returnAddr localVarsAndLinkSize]								// [... returnAddr returnValue frameSize]
				code.add(PushI, argumentsSize);					// [... returnAddr frameSize argsSize]
				code.add(Add);									// [... returnAddr frameSize+argsSize]
				Macros.addITo(code, RunTime.STACK_POINTER);		// [... returnAddr]
				
				code.add(PushI, -returnType.getSize());			// [... returnAddr returnSize]				
				Macros.addITo(code, RunTime.STACK_POINTER);		// [... returnAddr]
				
				Macros.loadIFrom(code, RunTime.STACK_POINTER);	// [... returnAddr sp]
				
				Macros.loadIFrom(code, RunTime.RATIONAL_NUMERATOR_TEMP); // [... returnAddr sp num]
				Macros.loadIFrom(code, RunTime.RATIONAL_DENOMINATOR_TEMP); // [... returnAddr sp num den]

				appendOpcodesForStore(code, returnType);		// [... returnAddr]
			}
			else {
				Macros.loadIFrom(code, RunTime.FRAME_POINTER); 	// [... returnValue fp]
				Macros.readIOffset(code, -8);  					// [... returnValue returnAddr]
				Macros.loadIFrom(code, RunTime.FRAME_POINTER); 	// [... returnValue returnAddr fp]
				Macros.readIOffset(code, -4); 					// [... returnValue returnAddr dynamic_link]
				Macros.storeITo(code, RunTime.FRAME_POINTER);	// [... returnValue returnAddr]
				code.add(Exchange); 							// [... returnAddr returnValue]
				
				code.add(PushI, routineFrameSize);				// [... returnAddr returnValue localVarsAndLinkSize]								// [... returnAddr returnValue frameSize]
				code.add(PushI, argumentsSize);					// [... returnAddr returnValue frameSize argsSize]
				code.add(Add);									// [... returnAddr returnValue frameSize+argsSize]
				Macros.addITo(code, RunTime.STACK_POINTER);		// [... returnAddr returnValue]
				
				code.add(PushI, -returnType.getSize());			// [... returnAddr returnValue returnSize]				
				Macros.addITo(code, RunTime.STACK_POINTER);		// [... returnAddr returnValue]
				
				Macros.loadIFrom(code, RunTime.STACK_POINTER);	// [... returnAddr returnValue sp]
				code.add(Exchange);								// [... returnAddr sp returnValue]
				appendOpcodesForStore(code, returnType);		// [... returnAddr]
			}
			
			code.add(Return);									// [...]
			code.add(Label, node.getEndLabel());
		}
		
		public void visitLeave(CallStatementNode node) {
			newVoidCode(node);
			
			ParseNode funcInvocationNode = node.child(0);
			ASMCodeFragment functionCode = removeVoidCode(funcInvocationNode);
			code.append(functionCode);
			
			code.add(Pop); // pop the return value
		}

		public void visitLeave(ReturnStatementNode node) {
			newVoidCode(node);
			if(node.nChildren() != 0) {
				ParseNode returnValue = node.child(0);
				ASMCodeFragment childCode = removeValueCode(returnValue);
				code.append(childCode);
				//code.add(PStack);
				//code.add(Halt);
			}
			
			// find the function exit label and jump to it
			ParseNode functionNode = node.getParent();
			for(ParseNode parent : node.pathToRoot()) {
				if(parent instanceof FunctionBodyNode) {
					functionNode = parent;
					break;
				}
			}
			code.add(Jump, ((FunctionBodyNode)functionNode).getExitLabel());
		}
		
		public void visitLeave(FunctionDefinitionNode node) {
			newVoidCode(node);
			ParseNode functionName = node.child(0);
			ASMCodeFragment childCode = removeAddressCode(functionName);
			code.append(childCode);
			
			ParseNode lambdaNode = node.child(1);
			childCode = removeValueCode(lambdaNode);
			code.append(childCode);
			code.add(Pop); // pop the reference to lambda start function
		}
		
		public void visitLeave(FunctionInvocationNode node) {
			newValueCode(node);
			
			// caller enter handshake 
			// place arguments on the stack 
			for(int i = 1; i < node.nChildren(); i++) {
				ParseNode childNode = node.child(i);
				code.add(PushI, -childNode.getType().getSize());// [... -paramSize]
				Macros.addITo(code, RunTime.STACK_POINTER);		// [...]

				Macros.loadIFrom(code, RunTime.STACK_POINTER);	// [... sp]
				ASMCodeFragment paramCode = removeValueCode(childNode);
				code.append(paramCode);							// [... sp param]
				appendOpcodesForStore(code, childNode.getType());	// [...]
			}
			
			//ParseNode functionDefinitionNode = node.getFunctionDefinition();
			//ParseNode lambdaNode = functionDefinitionNode.child(1);
			
			ParseNode lambdaNode = node.getLambdaNode();
			ParseNode functionBodyNode = lambdaNode.child(1);
			String functionLabel = ((FunctionBodyNode)functionBodyNode).getStartLabel();
			code.add(Call, functionLabel);
			
			// caller exit handshake 
			Macros.loadIFrom(code, RunTime.STACK_POINTER);	// [... sp]
			//makeFragmentValueCode(code, node);				// [... returnVal]
			
			if(node.getType() instanceof Function) {
				Type returnType = ((Function)node.getType()).getReturnType();
				code.append(getLoadOpcode(returnType));
				code.add(PushI, returnType.getSize());		// [... returnVal returnSize]	
			} else {
				Type returnType = ((Lambda)node.getType()).getReturnType();
				code.append(getLoadOpcode(returnType));
				code.add(PushI, returnType.getSize());	// [... returnVal returnSize]
			}
			Macros.addITo(code, RunTime.STACK_POINTER);		// [... returnVal]
			
		}
		
		public void visitLeave(LambdaNode node) {
			newValueCode(node);
			ASMCodeFragment childCode;
			
			int numOfChildren = node.nChildren();
			
			// remove procedure body block
			childCode = removeVoidCode(node.child(numOfChildren-1));
			code.append(childCode);
			
		}
		
		public void visitLeave(MapOperatorNode node) {
			newValueCode(node);
			
			ASMCodeFragment array = removeValueCode(node.child(0));
			ASMCodeFragment lambda = removeAddressCode(node.child(1));
			code.append(array);
			code.append(lambda);
			
			ASMCodeFragment frag = new MapOperatorCodeGenerator().generate(node);
			code.append(frag);
		}
		
		public void visitLeave(ReduceOperatorNode node) {
			newValueCode(node);
			
			ASMCodeFragment array = removeValueCode(node.child(0));
			ASMCodeFragment lambda = removeAddressCode(node.child(1));
			code.append(array);
			code.append(lambda);
			
			ASMCodeFragment frag = new ReduceOperatorCodeGenerator().generate(node);
			code.append(frag);
		}
		
		public void visitLeave(ZipOperatorNode node) {
			newValueCode(node);
			
			ASMCodeFragment array1 = removeValueCode(node.child(0));
			ASMCodeFragment array2 = removeValueCode(node.child(1));
			ASMCodeFragment lambda = removeAddressCode(node.child(2));
			code.append(array1);
			code.append(array2);
			code.append(lambda);
			
			ASMCodeFragment frag = new ZipOperatorCodeGenerator().generate(node);
			code.append(frag);
		}
		
		public void visitLeave(FoldOperatorNode node) {
			newValueCode(node);
			
			if(node.nChildren() == 2) {
				ASMCodeFragment array = removeValueCode(node.child(0));
				ASMCodeFragment lambda = removeValueCode(node.child(1));
				code.append(array);
				code.append(lambda);
				
				ASMCodeFragment frag = new FoldOperatorCodeGenerator().generate(node);
				code.append(frag);
			}
			else {
				ASMCodeFragment array = removeValueCode(node.child(0));
				ASMCodeFragment base = removeValueCode(node.child(1));
				ASMCodeFragment lambda = removeValueCode(node.child(2));
				code.append(array);
				code.append(base);
				code.append(lambda);
				
				ASMCodeFragment frag = new FoldOperatorWithBaseCodeGenerator().generate(node);
				code.append(frag);
			}
		}
		
		///////////////////////////////////////////////////////////////////////////
		// statements and declarations

		public void visitLeave(PrintStatementNode node) {
			newVoidCode(node);
			new PrintStatementGenerator(code, this).generate(node);	
		}
		public void visit(NewlineNode node) {
			newVoidCode(node);
			code.add(PushD, RunTime.NEWLINE_PRINT_FORMAT);
			code.add(Printf);
		}
		public void visit(TabNode node) {
			newVoidCode(node);
			code.add(PushD, RunTime.TAB_PRINT_FORMAT);
			code.add(Printf);
		}
		public void visit(SpaceNode node) {
			newVoidCode(node);
			code.add(PushD, RunTime.SPACE_PRINT_FORMAT);
			code.add(Printf);
		}
		
		public void visitLeave(DeallocStatementNode node) {
			newVoidCode(node);
			ASMCodeFragment refType = removeAddressCode(node.child(0));	
			
			code.append(refType);	// [.. record]
			
			Labeller labeller = new Labeller("dealloc");
			String exitLabel = labeller.newLabel("exit");
			
			// check if isDel and isPerm bits are 0
			// Status - isPerm, isDel, isRef, isImmut
			Macros.readIOffset(code, RunTime.RECORD_STATUS_OFFSET); // [.. status]
			code.add(PushI, 4); // isDel bit (0100)
			code.add(BTAnd); // [... status&4]
			code.add(PushI, 4); // [... status&4 4]
			code.add(BTEqual);	// [... isDelBitSet(bool)]
			code.add(JumpTrue, RunTime.DOUBLE_FREE_RUNTIME_ERROR); 
			
			Macros.readIOffset(code, RunTime.RECORD_STATUS_OFFSET); // [.. status]
			code.add(PushI, 8); // isPerm bit (1000)
			code.add(BTAnd); // [... status&8]
			code.add(PushI, 8); // [... status&4 4]
			code.add(BTEqual);	// [... isDelBitSet(bool)]
			code.add(JumpTrue, exitLabel); // [...] 
			
			// set deleted bit to 1
			Macros.readIOffset(code, RunTime.RECORD_STATUS_OFFSET); // [.. status]
			code.add(PushI, 4); // isDel bit (0100)
			code.add(BTOr); // [... status|4]
			code.append(refType); // [.. newStatus record]
			Macros.writeIOffset(code, RunTime.RECORD_STATUS_OFFSET); // [...]
			
			code.append(refType); // [... record]
			code.add(Call, MemoryManager.MEM_MANAGER_DEALLOCATE); // [...]
			
			code.add(Label, exitLabel);  
		}
		
		public void visitLeave(UnaryOperatorNode node) {
			newValueCode(node);
			ASMCodeFragment arg1 = removeValueCode(node.child(0));
			code.append(arg1);
			
			if(node.getOperator() == Keyword.LENGTH) {
				ASMCodeFragment frag = new LengthOperatorCodeGenerator().generate(node);
				code.append(frag);
			}
			else if(node.getOperator() == Keyword.CLONE) { 
				ASMCodeFragment frag = new CloneOperatorCodeGenerator().generate(node);
				code.append(frag);
			}
			else if(node.getOperator() == Punctuator.BOOLEAN_NOT) {
				code.add(BNegate);
			}
			else if(node.getOperator() == Keyword.REVERSE) {
				if(node.child(0).getType() == PrimitiveType.STRING) {
					ASMCodeFragment frag = new ReverseStringCodeGenerator().generate(node);
					code.append(frag);
				}
				else if(node.child(0).getType() instanceof Array){
					ASMCodeFragment frag = new ReverseArrayCodeGenerator().generate(node);
					code.append(frag);
				}
				else {
					assert false: "Can not determine the type of reverse child node.";
				}
			}
		}
		
		public void visitLeave(ArrayNode node) {
			newValueCode(node);
			
			ASMCodeFragment subCode = removeValueCode(node.child(0));
			code.append(subCode);
		}
		
		public void visitLeave(PopulatedArrayNode node) {
			newValueCode(node);
			
			Type subType = ((Array)node.getType()).getSubType();
			int statusFlags;
			if(subType.isReferenceType() || node.child(0) instanceof IdentifierNode) {
				statusFlags = 0b0100; // isPerm, isDel, isRef, isImmut
			}
			else {
				statusFlags = 0b0000; // isPerm, isDel, isRef, isImmut
			}
			for(ParseNode child : node.getChildren()) {
				ASMCodeFragment value = removeValueCode(child);
				code.append(value);
			}
			RunTime.createPopulatedArrayRecord(code, statusFlags, subType.getSize(), node.getChildren().size(), subType);
			
		}
		
		public void visitLeave(AssignmentStatementNode node) {
			newVoidCode(node);
			ASMCodeFragment lvalue = removeAddressCode(node.child(0));	
			Type type = node.getType();
			if(type instanceof Array && node.child(1) instanceof PopulatedArrayNode) {
				ASMCodeFragment rvalue = removeValueCode(node.child(1));
				
				code.append(lvalue);				// [... arrayAddr]
				code.append(rvalue);				// [... arrayAddr addrToArrayRecord]
				
				code.add(StoreI);					// [...]
			}
			else if(type == PrimitiveType.RATIONAL) {
				ASMCodeFragment rvalue = removeValueCode(node.child(1));

				code.append(lvalue);	// [... ratAddr]
				code.append(rvalue);	// [... ratAddr num den]
				
				appendOpcodesForStore(code, type);
				
			}
			else if(type instanceof Lambda) {
				ASMCodeFragment rvalue = removeValueCode(node.child(1));
				code.append(lvalue);
				code.append(rvalue);
				appendOpcodesForStore(code, type);
			}
			else if(type instanceof Function) {
				ASMCodeFragment rvalue = removeValueCode(node.child(1));
				code.append(lvalue);
				code.append(rvalue);
				ParseNode funcInvocationNode = node.child(1);
				Type returnType = ((Function)funcInvocationNode.getType()).getReturnType();
				appendOpcodesForStore(code, returnType);
				//System.out.println(code);
			}
			else {
				ASMCodeFragment rvalue = removeValueCode(node.child(1));
				code.append(lvalue);
				code.append(rvalue);
				appendOpcodesForStore(code, type);
				//System.out.println(code);
			}
		}
		
		// Don't forget to update AssignmentStatementNode
		public void visitLeave(DeclarationNode node) {
			newVoidCode(node);
			ASMCodeFragment lvalue = removeAddressCode(node.child(0));	
			Type type = node.getType();
			
			IdentifierNode identifier = (IdentifierNode) node.child(0);
			Labeller labeller = new Labeller("declaration");
			
			String exitDeclarationLabel = labeller.newLabel("exit");
			
			if(identifier.getCompanionBinding() != null) { // if this is a static variable
				Binding companionBinding = identifier.getCompanionBinding();
				Macros.loadIFrom(code, RunTime.GLOBAL_MEMORY_BLOCK);
				code.add(PushI, companionBinding.getMemoryLocation().getOffset());
				code.add(Add);
				code.add(LoadI);
				code.add(LoadC);
				code.add(JumpTrue, exitDeclarationLabel); // don't initialize static variables twice
				Macros.loadIFrom(code, RunTime.GLOBAL_MEMORY_BLOCK);
				code.add(PushI, companionBinding.getMemoryLocation().getOffset());
				code.add(Add);
				code.add(LoadI);
				code.add(PushI, 1); // set companion binding = True
				code.add(StoreC);
			}
			
			if(type instanceof Array && node.child(1) instanceof PopulatedArrayNode) {
				ASMCodeFragment rvalue = removeValueCode(node.child(1));
				
				code.append(lvalue);				// [... arrayAddr]
				code.append(rvalue);				// [... arrayAddr addrToArrayRecord]
				code.add(StoreI);					// [...]
			}
			else if(type == PrimitiveType.RATIONAL) {
				ASMCodeFragment rvalue = removeValueCode(node.child(1));
				code.append(lvalue);	// [... ratAddr]
				code.append(rvalue);	// [... ratAddr num den]

				appendOpcodesForStore(code, type);
			}
			else if(type instanceof Lambda) {
				ASMCodeFragment rvalue = removeValueCode(node.child(1));
				code.append(lvalue);
				code.append(rvalue);
				if(node.child(1) instanceof FunctionInvocationNode)
					node.setType(((Lambda)type).getReturnType());
				appendOpcodesForStore(code, node.getType());
			}
			else if(type instanceof Function) {
				ASMCodeFragment rvalue = removeValueCode(node.child(1));
				code.append(lvalue);
				code.append(rvalue);
				
				ParseNode funcInvocationNode = node.child(1);
				Type returnType = ((Function)funcInvocationNode.getType()).getReturnType();
				appendOpcodesForStore(code, returnType);
			}
			else {
				ASMCodeFragment rvalue = removeValueCode(node.child(1));
				code.append(lvalue);
				code.append(rvalue);
				appendOpcodesForStore(code, type);
			}
			
			code.add(Label, exitDeclarationLabel);
		} 
		// Don't forget to update AssignmentStatementNode
		
		private void appendOpcodesForStore(ASMCodeFragment code, Type type) {
			/*if(type instanceof Lambda) {
				type = ((Lambda)type).getReturnType();
			}*/
			
			if(type == PrimitiveType.RATIONAL) {	
																			// [... ratAddr num den]
				Macros.storeITo(code, RunTime.RATIONAL_DENOMINATOR_TEMP); 	// [... ratAddr num]
				Macros.storeITo(code, RunTime.RATIONAL_NUMERATOR_TEMP); 	// [... ratAddr]

				// store numerator
				code.add(Duplicate);										// [... ratAddr ratAddr]
				Macros.loadIFrom(code, RunTime.RATIONAL_NUMERATOR_TEMP); 	// [... ratAddr num]
				code.add(StoreI); 											// [... ratAddr]
				
				// store denominator 
				code.add(PushI, 4);											// [... ratAddr 4]
				code.add(Add); 												// [... ratAddr+4]
				Macros.loadIFrom(code, RunTime.RATIONAL_DENOMINATOR_TEMP); 	// [... ratAddr+4 den]
				code.add(StoreI); 											// [...]
			}
			else if(type instanceof Array) {
				code.add(StoreI);
			}
			else if(type instanceof Lambda) {
				code.add(StoreI);
			}
			else if(type == PrimitiveType.INTEGER) {
				code.add(StoreI);
			}
			else if(type == PrimitiveType.FLOATING) {
				code.add(StoreF);
			}
			else if(type == PrimitiveType.BOOLEAN) {
				code.add(StoreC);
			}
			else if(type == PrimitiveType.CHARACTER) {
				code.add(StoreC);
			}
			else if(type == PrimitiveType.STRING) {
				code.add(StoreI);
			}
			else {
				assert false: "Type " + type + " unimplemented in opcodeForStore()";
			}
		}


		///////////////////////////////////////////////////////////////////////////
		// expressions
		public void visitLeave(BinaryOperatorNode node) {
			Lextant operator = node.getOperator();

			if(isComparisonOperator(operator)) {
				visitComparisonOperatorNode(node, operator);
			}
			else {
				visitNormalBinaryOperatorNode(node);
			}
		}
		private boolean isComparisonOperator(Lextant operator) {
			return operator == Punctuator.GREATER || 
					operator == Punctuator.GREATER_EQ ||
					operator == Punctuator.EQUAL || 
					operator == Punctuator.NOT_EQUAL|| 
					operator == Punctuator.LESS || 
					operator == Punctuator.LESS_EQ;
		}
		
		private void visitComparisonOperatorNode(BinaryOperatorNode node,
				Lextant operator) {

			ASMCodeFragment arg1 = removeValueCode(node.child(0));
			ASMCodeFragment arg2 = removeValueCode(node.child(1));
			
			Labeller labeller = new Labeller("compare");
			
			String startLabel = labeller.newLabel("arg1");
			String arg2Label  = labeller.newLabel("arg2");
			String subLabel   = labeller.newLabel("sub");
			String trueLabel  = labeller.newLabel("true");
			String falseLabel = labeller.newLabel("false");
			String joinLabel  = labeller.newLabel("join");
			
			newValueCode(node);
			code.add(Label, startLabel);
			code.append(arg1);
			code.add(Label, arg2Label);
			code.append(arg2);
			
			code.add(Label, subLabel);
			
			Type operandType = node.child(0).getType();
			if(operandType == PrimitiveType.FLOATING) {
				code.add(FSubtract);
			}
			else if(operandType == PrimitiveType.RATIONAL) {
				SubtractRationalsCodeGenerator generator = new SubtractRationalsCodeGenerator();
				ASMCodeFragment frag = generator.generate(node);
				code.append(frag);
				code.add(Pop); // pop denominator
			}
			else {
				code.add(Subtract);
			}
			
			if(operator == Punctuator.GREATER) {
				code.add(JumpPos, trueLabel);
				code.add(Jump, falseLabel);
			}
			else if(operator == Punctuator.LESS) {
				code.add(JumpNeg, trueLabel);
				code.add(Jump, falseLabel);
			}
			else if(operator == Punctuator.EQUAL) {
				code.add(JumpFalse, trueLabel);
				code.add(Jump, falseLabel);
			}
			else if(operator == Punctuator.NOT_EQUAL) {
				code.add(JumpTrue, trueLabel);
				code.add(Jump, falseLabel);
			}
			else if(operator == Punctuator.GREATER_EQ) {
				code.add(JumpNeg, falseLabel);
				code.add(Jump, trueLabel);
			}
			else if(operator == Punctuator.LESS_EQ) {
				code.add(JumpPos, falseLabel);
				code.add(Jump, trueLabel);
			}

			code.add(Label, trueLabel);
			code.add(PushI, 1);
			code.add(Jump, joinLabel);
			code.add(Label, falseLabel);
			code.add(PushI, 0);
			code.add(Jump, joinLabel);
			code.add(Label, joinLabel);

		}
		private void visitNormalBinaryOperatorNode(BinaryOperatorNode node) {
			newValueCode(node);
			ASMCodeFragment arg1 = removeValueCode(node.child(0));
			ASMCodeFragment arg2 = removeValueCode(node.child(1));
			
			Object variant = node.getSignature().getVariant();
			if(variant instanceof ASMOpcode) {
				code.append(arg1);
				code.append(arg2);
				//code.add(PStack);
				ASMOpcode opcode = (ASMOpcode)variant;
				//code.add(PStack); 
				code.add(opcode);
			}
			else if(variant instanceof SimpleCodeGenerator) {
				code.append(arg1);
				code.append(arg2);
				SimpleCodeGenerator generator = (SimpleCodeGenerator)variant;
				ASMCodeFragment fragment = generator.generate(node);
				code.append(fragment);
				if(fragment.isAddress()) {
					code.markAsAddress();
				}
			}
			else if(variant instanceof FullCodeGenerator) {
				FullCodeGenerator generator = (FullCodeGenerator)variant;
				ASMCodeFragment fragment = generator.generate(node, arg1, arg2);
				code.append(fragment);
				
				if(fragment.isAddress()) {
					code.markAsAddress();
				}
			}
			else {
				// throw exception
				throw new RuntimeException("Binary operator node variant is not an opcode");
			}
			
			// pika-0 code
			//ASMOpcode opcode = opcodeForOperator(node.getOperator());
			//code.add(opcode);							// type-dependent! (opcode is different for floats and for ints)
		}
		@SuppressWarnings("unused")
		private ASMOpcode opcodeForOperator(Lextant lextant) {
			assert(lextant instanceof Punctuator);
			Punctuator punctuator = (Punctuator)lextant;
			switch(punctuator) {
			case ADD: 	   		return Add;				// type-dependent!
			case MULTIPLY: 		return Multiply;		// type-dependent!
			default:
				assert false : "unimplemented operator in opcodeForOperator";
			}
			return null;
		}

		///////////////////////////////////////////////////////////////////////////
		// leaf nodes (ErrorNode not necessary)
		public void visit(BooleanConstantNode node) {
			newValueCode(node);
			code.add(PushI, node.getValue() ? 1 : 0);
		}
		public void visit(IdentifierNode node) {
			newAddressCode(node); 
			Binding binding = node.getBinding();
			
			binding.generateAddress(code);
		}		
		public void visit(IntegerConstantNode node) {
			newValueCode(node);
			
			code.add(PushI, node.getValue());
		}
		public void visit(FloatConstantNode node) {
			newValueCode(node);
			
			code.add(PushF, node.getValue());
		}
		public void visit(StringConstantNode node) {
			/* pika-1 code:
			newValueCode(node);
			Labeller labeller = new Labeller("stringLabel");
			String strLabel = labeller.newLabel("constant");
			
			code.add(DLabel, strLabel);
			code.add(DataS, node.getValue());
			code.add(PushD, strLabel); */
			
			// pika-2 code:
			newAddressCode(node);
			int statusFlags = 0b1001; // isPerm, isDel, isRef, isImmut
			RunTime.createStringRecord(code, statusFlags, node.getValue());
		}
		
		public void visit(CharacterConstantNode node) {
			newValueCode(node);
			
			code.add(PushI, node.getValue());
		}
		public void visit(ArrayNode node) {
			newValueCode(node);

		}
		public void visit(ContinueStatementNode node) {
			newVoidCode(node);
			if(node.getLoopNode() instanceof WhileStatementNode) {
				code.add(Jump, ((WhileStatementNode)node.getLoopNode()).getContinueLabel());
			}
			else if(node.getLoopNode() instanceof ForStatementNode) {
				code.add(Jump, ((ForStatementNode)node.getLoopNode()).getContinueLabel());
			}
			else {
				assert false: "Can not determine the continue statement's node type";
			}
		}
		public void visit(BreakStatementNode node) {
			newVoidCode(node);
			if(node.getLoopNode() instanceof WhileStatementNode) {
				code.add(Jump, ((WhileStatementNode)node.getLoopNode()).getBreakLabel());
			}
			else if(node.getLoopNode() instanceof ForStatementNode) {
				code.add(Jump, ((ForStatementNode)node.getLoopNode()).getBreakLabel());
			}
			else {
				assert false: "Can not determine the break statement's node type";
			}
		}
	}

}
