package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.Divide;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import parseTree.ParseNode;

public class RatToIntCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment fragment = new ASMCodeFragment(CodeType.GENERATES_VALUE);
									 // [... num(int) denom(int)]
		fragment.add(Divide);		 // [... num/demon(int)]
		
		return fragment;
	}
}
