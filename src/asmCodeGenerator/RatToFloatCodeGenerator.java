package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.FDivide;
import static asmCodeGenerator.codeStorage.ASMOpcode.ConvertF;
import static asmCodeGenerator.codeStorage.ASMOpcode.Exchange;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import parseTree.ParseNode;

public class RatToFloatCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment fragment = new ASMCodeFragment(CodeType.GENERATES_VALUE);
									 // [... num(int) denom(int)]
		fragment.add(ConvertF);	 // [... num(int) denom(float)]
		fragment.add(Exchange); 	 // [... denom(float) num(int)]
		fragment.add(ConvertF);	 // [...  denom(float) num(float)]
		fragment.add(Exchange); 	 // [... num(float) denom(float)]
		fragment.add(FDivide);		 // [... num/denom(float)]
		
		return fragment;
	}
}
