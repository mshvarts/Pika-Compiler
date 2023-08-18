package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.*;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import asmCodeGenerator.runtime.RunTime;
import parseTree.ParseNode;

public class FormRationalCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment fragment = new ASMCodeFragment(CodeType.GENERATES_VALUE);
		 										// [... num den]
		
		//fragment.add(PStack);
		fragment.add(Call, RunTime.LOWEST_TERMS);  // [... num/gcd den/gcd] (call lowest terms subroutine)
		//fragment.add(PStack);
		return fragment;
	}
}
