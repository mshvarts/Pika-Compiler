package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.Divide;
import static asmCodeGenerator.codeStorage.ASMOpcode.Duplicate;
import static asmCodeGenerator.codeStorage.ASMOpcode.JumpFalse;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import asmCodeGenerator.runtime.RunTime;
import parseTree.ParseNode;

public class IntegerDivideCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment fragment = new ASMCodeFragment(CodeType.GENERATES_VALUE);
									 // [... arg1 arg2]
		fragment.add(Duplicate);	 // [... arg1 arg2 arg2]
		fragment.add(JumpFalse, 	 // [... arg1 arg2]
				RunTime.INTEGER_DIVIDE_BY_ZERO_RUNTIME_ERROR); 
		fragment.add(Divide);		 // [... arg1/arg2]
		
		return fragment;
	}
}
