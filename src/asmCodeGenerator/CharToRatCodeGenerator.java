package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.PushI;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import parseTree.ParseNode;

public class CharToRatCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment fragment = new ASMCodeFragment(CodeType.GENERATES_VALUE);
									 // [... value]
		fragment.add(PushI, 1);		 // [... value denominator]
		
		return fragment;
	}
}
