package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.Duplicate;
import static asmCodeGenerator.codeStorage.ASMOpcode.JumpFalse;
import static asmCodeGenerator.codeStorage.ASMOpcode.FMultiply;
import static asmCodeGenerator.codeStorage.ASMOpcode.ConvertF;
import static asmCodeGenerator.codeStorage.ASMOpcode.ConvertI;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import asmCodeGenerator.runtime.RunTime;
import parseTree.ParseNode;

public class FloatingExpressOverCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment fragment = new ASMCodeFragment(CodeType.GENERATES_VALUE);
		 						// [... float over]
		fragment.add(Duplicate);  // [... float over over]
		fragment.add(JumpFalse,   // [... float over]
				RunTime.FLOAT_DIVIDE_BY_ZERO_RUNTIME_ERROR);  
		
		fragment.add(ConvertF); // [... float over]
		fragment.add(FMultiply); // [... float*over]	
		fragment.add(ConvertI); // [... float*over]	
		
		return fragment;
	}
}
