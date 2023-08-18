package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.PushI;
import static asmCodeGenerator.codeStorage.ASMOpcode.Xor;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import parseTree.ParseNode;

public class IntToBoolCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment fragment = new ASMCodeFragment(CodeType.GENERATES_VALUE);
									 // [... arg1 ]
		fragment.add(PushI, 0);	 // [... arg1 0]
		fragment.add(Xor); 	 // [... result]
		
		return fragment;
	}
}
