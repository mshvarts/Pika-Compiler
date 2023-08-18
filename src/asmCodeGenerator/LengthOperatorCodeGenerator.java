package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.*;
import static asmCodeGenerator.runtime.RunTime.*;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import parseTree.ParseNode;
import semanticAnalyzer.types.Array;
import semanticAnalyzer.types.PrimitiveType;

public class LengthOperatorCodeGenerator implements FullCodeGenerator {

	public ASMCodeFragment generate(ParseNode node, ASMCodeFragment... args) {
		ASMCodeFragment code = new ASMCodeFragment(CodeType.GENERATES_VALUE);
		
		if(node.child(0).getType() instanceof Array) {
		 													// [... array]
			code.add(Duplicate); 							// [... array array]
			code.add(JumpFalse, NULL_ARRAY_RUNTIME_ERROR); 	// [... array]
			
			Macros.readIOffset(code, ARRAY_LENGTH_OFFSET); 	// [... length]
		}
		else if(node.child(0).getType() == PrimitiveType.STRING) {
																// [... string]
			code.add(Duplicate); 								// [... string string]
			code.add(JumpFalse, NULL_STRING_RUNTIME_ERROR); 	// [... string]
			
			code.add(PushI, STRING_HEADER_SIZE);				// subtract header size
			code.add(Subtract);
			
			Macros.readIOffset(code, STRING_LENGTH_OFFSET); 	// [... length]
		}
		else {
			throw new RuntimeException("We do not know the type of the length operator child node");
		}
		return code;
	}
}
