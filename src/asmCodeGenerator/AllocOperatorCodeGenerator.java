package asmCodeGenerator;

//import static asmCodeGenerator.codeStorage.ASMOpcode.*;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import parseTree.ParseNode;
import parseTree.nodeTypes.ArrayNode;
import semanticAnalyzer.types.Type;
import asmCodeGenerator.runtime.RunTime;

public class AllocOperatorCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment code = new ASMCodeFragment(CodeType.GENERATES_ADDRESS);
															// [... type length]
		ArrayNode subTypeNode = (ArrayNode) (node.child(0));
		Type subtype = subTypeNode.getType();
		
		int statusFlag;
		if(subtype.isReferenceType()) {
			statusFlag = 0b0010; // isPerm, isDel, isRef, isImmut
		}
		else {
			statusFlag = 0b0000; // isPerm, isDel, isRef, isImmut
		}
		
		// TODO: initialize nested arrays with zeros

		RunTime.createEmptyArrayRecord(code, statusFlag, subtype.getSize()); 
		
		Macros.loadIFrom(code, RunTime.RECORD_CREATION_TEMPORARY); // [... ptrToArray]
		return code;
	}
}
