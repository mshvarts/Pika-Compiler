package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.FDivide;
import static asmCodeGenerator.codeStorage.ASMOpcode.Duplicate;
import static asmCodeGenerator.codeStorage.ASMOpcode.JumpFZero;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import asmCodeGenerator.runtime.RunTime;
import parseTree.ParseNode;

public class FloatingDivideCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment fragment = new ASMCodeFragment(CodeType.GENERATES_VALUE);
									 // [... arg1 arg2]
		fragment.add(Duplicate);	 // [... arg1 arg2 arg2]
		fragment.add(JumpFZero, 	 // [... arg1 arg2]
				RunTime.FLOAT_DIVIDE_BY_ZERO_RUNTIME_ERROR); 
		fragment.add(FDivide);		 // [... arg1/arg2]
		
		return fragment;
	}
}
