package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.*;
import static asmCodeGenerator.runtime.RunTime.STRING_HEADER_SIZE;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import asmCodeGenerator.runtime.RunTime;
import parseTree.ParseNode;

public class AddStringsCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment frag = new ASMCodeFragment(CodeType.GENERATES_ADDRESS);
														// [strPtr1 strPtr2]
		frag.add(PushI, STRING_HEADER_SIZE);			// subtract header size
		frag.add(Subtract);
		frag.add(Exchange);
		
		frag.add(PushI, STRING_HEADER_SIZE);			// subtract header size
		frag.add(Subtract);
		frag.add(Exchange);

		int statusFlags = 0b1001; // isPerm, isDel, isRef, isImmut
		RunTime.concatenateTwoStrings(frag, statusFlags);// [strPtrResult]

		return frag;
	}
}
