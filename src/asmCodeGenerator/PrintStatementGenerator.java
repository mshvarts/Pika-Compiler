package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.*;

import parseTree.ParseNode;
import parseTree.nodeTypes.NewlineNode;
import parseTree.nodeTypes.PrintStatementNode;
import parseTree.nodeTypes.SpaceNode;
import parseTree.nodeTypes.TabNode;
import semanticAnalyzer.types.Array;
import semanticAnalyzer.types.Function;
import semanticAnalyzer.types.Lambda;
import semanticAnalyzer.types.PrimitiveType;
import semanticAnalyzer.types.Type;
import asmCodeGenerator.ASMCodeGenerator.CodeVisitor;
import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.runtime.RunTime;

public class PrintStatementGenerator {
	ASMCodeFragment code;
	ASMCodeGenerator.CodeVisitor visitor;
	
	
	public PrintStatementGenerator(ASMCodeFragment code, CodeVisitor visitor) {
		super();
		this.code = code;
		this.visitor = visitor;
	}

	public void generate(PrintStatementNode node) {
		for(ParseNode child : node.getChildren()) {
			if(child instanceof NewlineNode || child instanceof SpaceNode || child instanceof TabNode) {
				ASMCodeFragment childCode = visitor.removeVoidCode(child);
				code.append(childCode);
			}
			else {
				appendPrintCode(child);
			}
		}
	}

	private void appendPrintCode(ParseNode node) {
		Type printType = node.getType();
		
		if(printType instanceof Lambda) {
			/*if(node.nChildren() == 0) {
				code.add(PushD, RunTime.LAMBDA_PRINT_STRING);
				code.add(PushD, RunTime.STRING_PRINT_FORMAT);
				code.add(Printf);
				return;
			} else*/
			printType = ((Lambda)printType).getReturnType();
			node.setType(printType);
		}
		
		if(printType instanceof Function) {
			printType = ((Function)printType).getReturnType();
		}
		
		if(printType instanceof Array) {
			printType = ((Array)printType).getSubType();
			handleArray(node, printType);
		}
		else if(printType == PrimitiveType.RATIONAL) {
			ASMCodeFragment out = visitor.removeValueCode(node);
			code.append(out);
			parseRational();
			code.add(Printf);
		}
		else {
			String format;
			format = printFormat(printType);
			ASMCodeFragment out = visitor.removeValueCode(node);
			code.append(out);
			convertToStringIfBoolean(printType);
			code.add(PushD, format);
			code.add(Printf);
		}
	}
	
	// [... addrToArray] -> [...]
	private void handleArray(ParseNode node, Type subType) {
		
		ASMCodeFragment out = visitor.removeValueCode(node);
		code.append(out);										// [... ptrToArray]
		/*if(node instanceof BinaryOperatorNode) {
			code.add(LoadI);
		}*/
		loadAndPrintArray(subType);
	}
	
	// [... ptrToArray] -> [...]
	private void loadAndPrintArray(Type subType) {

		Macros.storeITo(code, RunTime.ARRAY_DATA_PTR_TEMPORARY); 	// [...]
		Macros.loadIFrom(code, RunTime.ARRAY_DATA_PTR_TEMPORARY);	// [... addr]
		code.add(PushI, RunTime.ARRAY_LENGTH_OFFSET);				// [... addr lengthOffset]
		code.add(Add);												// [... addr+lengthOffset]
		code.add(LoadI);											// [... arrayLength]
		Macros.storeITo(code, RunTime.ARRAY_LENGTH_TEMPORARY);		// [...]
		Macros.loadIFrom(code, RunTime.ARRAY_LENGTH_TEMPORARY);		// [... arrayLength]
		
		code.add(PushI, 1);										    // [... arrayLength 1]
		code.add(Subtract);										    // [... arrayLength-1]
		Macros.storeITo(code, RunTime.ARRAY_INDEX_TEMPORARY);		// [...]
		
		Macros.loadIFrom(code, RunTime.ARRAY_DATA_PTR_TEMPORARY);  	// [... addr]
		code.add(PushI, RunTime.ARRAY_SUBTYPE_SIZE_OFFSET);			// [... addr subtypeSizeOffset]
		code.add(Add);												// [... addr+subtypeSizeOffset]
		code.add(LoadI);											// [... subtypeSize]
		Macros.storeITo(code, RunTime.ARRAY_SUBTYPE_SIZE_TEMPORARY);  // [...]
		
		Labeller labeller = new Labeller("loadArray");
		String loopLabel = labeller.newLabel("loop");
		String exitLabel = labeller.newLabel("exit");
		
		code.add(Label, loopLabel);									// [... ]
		Macros.loadIFrom(code, RunTime.ARRAY_DATA_PTR_TEMPORARY);	// [... addr]
		code.add(PushI, RunTime.ARRAY_HEADER_SIZE); 				// [... addr AHS]
		code.add(Add);												// [... elemsStart]
		
		Macros.loadIFrom(code, RunTime.ARRAY_INDEX_TEMPORARY); 	// [... elemsStart LastIndex]
		Macros.loadIFrom(code, 								   	// [... elemsStart LastIndex subtypeSize]
				RunTime.ARRAY_SUBTYPE_SIZE_TEMPORARY); 
		code.add(Multiply);										// [... elemsStart LastIndex*subtypeSize]
		code.add(Add); 											// [... elemsStart+LastIndex*subtypeSize]
		
		if(subType == PrimitiveType.BOOLEAN)
			code.add(LoadC);										// [... value]		
		else if(subType == PrimitiveType.CHARACTER)
			code.add(LoadC);										// [... value]		
		else if(subType == PrimitiveType.INTEGER)
			code.add(LoadI);										// [... value]		
		else if(subType == PrimitiveType.FLOATING)
			code.add(LoadF);										// [... value]
		else if(subType == PrimitiveType.RATIONAL) { 
			code.add(PushI, 4);										// [... elemsStart+LastIndex*subtypeSize 4]
			code.add(Add);											// [... elemsStart+LastIndex*subtypeSize+4]
			code.add(LoadI);	
			Macros.loadIFrom(code, RunTime.ARRAY_DATA_PTR_TEMPORARY);	// [... addr]
			code.add(PushI, RunTime.ARRAY_HEADER_SIZE); 				// [... addr AHS]
			code.add(Add);												// [... elemsStart]
			Macros.loadIFrom(code, RunTime.ARRAY_INDEX_TEMPORARY); 		// [... elemsStart LastIndex]
			Macros.loadIFrom(code, 								   		// [... elemsStart LastIndex subtypeSize]
					RunTime.ARRAY_SUBTYPE_SIZE_TEMPORARY); 
			code.add(Multiply);										// [... elemsStart LastIndex*subtypeSize]
			code.add(Add); 											// [... elemsStart+LastIndex*subtypeSize]
			code.add(LoadI);	
		}
		else if(subType == PrimitiveType.STRING) {
			code.add(LoadI);
		} 
		else if(subType instanceof Array) {
			code.add(LoadI);
		}
		else {
			assert false: "cant load unknown array sub type: " + subType;
		}
		Macros.decrementInteger(code, RunTime.ARRAY_INDEX_TEMPORARY); // [... value]
		Macros.loadIFrom(code, RunTime.ARRAY_INDEX_TEMPORARY);	// [... value index-1]
		code.add(JumpNeg, exitLabel); 							// [... value]
		code.add(Jump, loopLabel);								
		
		code.add(Label, exitLabel);								// [... values]
		
		Macros.loadIFrom(code, RunTime.ARRAY_LENGTH_TEMPORARY);	// [... values arrayLength]
		
		Macros.printChar(code, RunTime.ARRAY_OPENING_CHARACTER);
		
		Labeller labeller2 = new Labeller("printArray");
		String loopStart = labeller2.newLabel("start");
		String loopExit = labeller2.newLabel("exit");
		String labelSkipElement = labeller2.newLabel("skipElement");
		String labelContinue = labeller2.newLabel("continue");
		
																		// [... values arrayLength]
		Macros.storeITo(code, RunTime.ARRAY_INDEX_TEMPORARY);			// [... values]
		
		code.add(Label, loopStart);										// [... values]
		
		if(subType == PrimitiveType.CHARACTER) {
			code.add(PushD, RunTime.CHARACTER_PRINT_FORMAT); 			// [... values %c]
			code.add(Printf);											// [... values]
		}
		else if(subType == PrimitiveType.INTEGER) {	
			code.add(PushD, RunTime.INTEGER_PRINT_FORMAT); 				// [... values %i]
			code.add(Printf);											// [... values]
		}
		else if(subType == PrimitiveType.FLOATING) {
			code.add(PushD, RunTime.FLOATING_PRINT_FORMAT); 			// [... values %f]
			code.add(Printf);											// [... values]
		}
		else if(subType == PrimitiveType.RATIONAL) {
			parseRational();									 		// [... values %d %d]
			code.add(Printf);											// [... values]
		}
		else if(subType == PrimitiveType.STRING) {
			code.add(PushD, RunTime.STRING_PRINT_FORMAT); 				// [... values %s]
			code.add(Printf);											// [... values]
		}
		else if(subType == PrimitiveType.BOOLEAN) {
			convertToStringIfBoolean(subType);
			code.add(PushD, RunTime.BOOLEAN_PRINT_FORMAT); 				// [... values %s]
			code.add(Printf);											// [... values]
		}
		else if(subType instanceof Array) {
			// save array index on stack
			Macros.loadIFrom(code, RunTime.ARRAY_INDEX_TEMPORARY);		// [... arrayAddrs arrayIndexTemp]
			// exchange with element array ptr
			code.add(Exchange); 										// [... arrayAddrs arrayIndexTemp arrayAddr]
			// Skip empty array elements
			code.add(Duplicate); 										// [... arrayAddrs arrayIndexTemp arrayAddr arrayAddr]
			code.add(JumpFalse, labelSkipElement);						// [... arrayAddrs arrayIndexTemp arrayAddr]
			
			loadAndPrintArray(((Array) subType).getSubType()); 			// [... arrayAddrs arrayIndexTemp]
			code.add(Jump, labelContinue);
			code.add(Label, labelSkipElement);
			code.add(Pop);
			Macros.printChar(code, RunTime.ARRAY_OPENING_CHARACTER);
			Macros.printChar(code, RunTime.ARRAY_CLOSING_CHARACTER);
			code.add(Label, labelContinue);
			// load array index from stack
			Macros.storeITo(code, RunTime.ARRAY_INDEX_TEMPORARY);		// [... arrayAddrs]
		}
		else {
			assert false: "cant print unknown array sub type: " + subType;
		}
		
		Macros.decrementInteger(code, RunTime.ARRAY_INDEX_TEMPORARY);	// [... values]
		Macros.loadIFrom(code, RunTime.ARRAY_INDEX_TEMPORARY);			// [... values lastIndex]
		code.add(JumpFalse, loopExit);									// [... values]
		Macros.printChar(code, RunTime.ARRAY_SEPARATOR_CHARACTER);
		Macros.printChar(code, RunTime.SPACE_CHARACTER);
		code.add(Jump, loopStart);										// [...]

		code.add(Label, loopExit);										// [...]
		
		Macros.printChar(code, RunTime.ARRAY_CLOSING_CHARACTER);
	}
	
	// [... num den] -> [(num den|num den|num den whole) %s]
	private void parseRational() {
		Macros.storeITo(code, RunTime.RATIONAL_DENOMINATOR_TEMP); // [... num]
		Macros.storeITo(code, RunTime.RATIONAL_NUMERATOR_TEMP); // [...]
		
		Labeller labeller = new Labeller("rational");
		String IntegerNotZeroLabel = labeller.newLabel("integerNotZeroLabel");
		String FracIsZeroLabel = labeller.newLabel("fracIsZeroLabel");
		String NegFracLabel = labeller.newLabel("fracNegPrint");
		String NegWholeLabel = labeller.newLabel("wholeNegPrint");
		String exitPrintLabel = labeller.newLabel("exitPrint");
		
		Macros.loadIFrom(code, RunTime.RATIONAL_NUMERATOR_TEMP); // [... num]
		Macros.loadIFrom(code, RunTime.RATIONAL_DENOMINATOR_TEMP);// [... num den]
		
		code.add(Divide); // [... num/den]
		code.add(JumpTrue, IntegerNotZeroLabel); // [...]
		
		// whole integer is 0. (print _%d/%d)
		Macros.loadIFrom(code, RunTime.RATIONAL_DENOMINATOR_TEMP);// [... den]
		Macros.loadIFrom(code, RunTime.RATIONAL_NUMERATOR_TEMP); // [... den num]
		
		Macros.loadIFrom(code, RunTime.RATIONAL_DENOMINATOR_TEMP);// [... den num den]
		Macros.loadIFrom(code, RunTime.RATIONAL_NUMERATOR_TEMP); // [... den num den num]
		code.add(Multiply);										// [... den num den*num]
		code.add(JumpNeg, NegFracLabel);						// [... den num]
		
		code.add(PushD, RunTime.RATIONAL_FRAC_PRINT_FORMAT);	// [... den num %s]
		code.add(Jump, exitPrintLabel);
		
		code.add(Label, NegFracLabel);	// [... den num]
		Macros.setPositive(code);
		code.add(Exchange);				// [... num den]
		Macros.setPositive(code);
		code.add(Exchange);
		code.add(PushD, RunTime.RATIONAL_NEG_FRAC_PRINT_FORMAT);
		code.add(Jump, exitPrintLabel);
		
		// whole integer is not 0 
		code.add(Label, IntegerNotZeroLabel);
		
		Macros.loadIFrom(code, RunTime.RATIONAL_NUMERATOR_TEMP); // [... num]
		Macros.loadIFrom(code, RunTime.RATIONAL_DENOMINATOR_TEMP);// [... num den]
		code.add(Remainder);									 // [... num%den]
		code.add(JumpFalse, FracIsZeroLabel);					// [...]
		
		// fractional and whole is not 0 (print %d_%d/%d) 
		Macros.loadIFrom(code, RunTime.RATIONAL_DENOMINATOR_TEMP); // [... den]
		
		Macros.loadIFrom(code, RunTime.RATIONAL_NUMERATOR_TEMP); // [... den num]
		Macros.loadIFrom(code, RunTime.RATIONAL_DENOMINATOR_TEMP);// [... den num den]
		code.add(Remainder);									 // [...den num%den]
		
		Macros.loadIFrom(code, RunTime.RATIONAL_NUMERATOR_TEMP); // [... den num%den num]
		Macros.loadIFrom(code, RunTime.RATIONAL_DENOMINATOR_TEMP);// [... den num%den num den]
		code.add(Divide); 										// [... den num%den num/den]
		
		Macros.loadIFrom(code, RunTime.RATIONAL_NUMERATOR_TEMP);
		Macros.loadIFrom(code, RunTime.RATIONAL_DENOMINATOR_TEMP);
		code.add(Multiply);										
		code.add(JumpNeg, NegWholeLabel);
		
		code.add(PushD, RunTime.RATIONAL_WHOLE_FRAC_PRINT_FORMAT);// [... den num%den num/den %s]
		code.add(Jump, exitPrintLabel);
		
		code.add(Label, NegWholeLabel);
		Macros.storeITo(code, RunTime.TEMP_DATA_STORAGE);
		Macros.setPositive(code);
		code.add(Exchange);				
		Macros.setPositive(code);
		code.add(Exchange);
		Macros.loadIFrom(code, RunTime.TEMP_DATA_STORAGE);
		Macros.setPositive(code);
		
		code.add(PushD, RunTime.RATIONAL_NEG_WHOLE_FRAC_PRINT_FORMAT);
		code.add(Jump, exitPrintLabel);
		
		// fractional part is 0. (print %d)
		code.add(Label, FracIsZeroLabel);
		
		Macros.loadIFrom(code, RunTime.RATIONAL_NUMERATOR_TEMP); // [... num]
		Macros.loadIFrom(code, RunTime.RATIONAL_DENOMINATOR_TEMP);// [... num den]
		code.add(Divide); 										// [... num/den]
		
		code.add(PushD, RunTime.RATIONAL_WHOLE_PRINT_FORMAT); // [... whole %s]
		
		code.add(Label, exitPrintLabel);
	}
	
	private void convertToStringIfBoolean(Type type) {
		if(type != PrimitiveType.BOOLEAN) {
			return;
		}
		
		Labeller labeller = new Labeller("print-boolean");
		String trueLabel = labeller.newLabel("true");
		String endLabel = labeller.newLabel("join");

		code.add(JumpTrue, trueLabel);
		code.add(PushD, RunTime.BOOLEAN_FALSE_STRING);
		code.add(Jump, endLabel);
		code.add(Label, trueLabel);
		code.add(PushD, RunTime.BOOLEAN_TRUE_STRING);
		code.add(Label, endLabel);
	}

	private static String printFormat(Type type) {
		
		if(type instanceof PrimitiveType) {
			switch((PrimitiveType)type) {
				case INTEGER:	return RunTime.INTEGER_PRINT_FORMAT;
				case FLOATING:	return RunTime.FLOATING_PRINT_FORMAT;
				case BOOLEAN:	return RunTime.BOOLEAN_PRINT_FORMAT;
				case STRING:	return RunTime.STRING_PRINT_FORMAT;
				case CHARACTER:	return RunTime.CHARACTER_PRINT_FORMAT;
				default:		
					assert false : "Type " + type + " unimplemented in PrintStatementGenerator.printFormat()";
					return "";
				}
		} 
		else {
			assert false : "Type " + type + " unimplemented in PrintStatementGenerator.printFormat()";
			return "";
		}
	}
}
