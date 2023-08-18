package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.*;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import asmCodeGenerator.runtime.RunTime;
import parseTree.ParseNode;
import semanticAnalyzer.types.Array;
import semanticAnalyzer.types.Type;

public class CloneOperatorCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment code = new ASMCodeFragment(CodeType.GENERATES_VOID);
										 	// [... array]
		Array arrayType = (Array)(node.child(0).getType());
		Type subtype = arrayType.getSubType();
		
		int statusFlag;
		if(subtype.isReferenceType()) {
			statusFlag = 0b0100; // Immut, isRef, isDel, Perm
		}
		else {
			statusFlag = 0b0000;
		}
		RunTime.cloneArrayRecord(code, statusFlag, subtype.getSize(), subtype); // [... ptrToArray]
		return code;
	}
}
