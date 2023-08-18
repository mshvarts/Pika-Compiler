package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.Call;
import static asmCodeGenerator.codeStorage.ASMOpcode.Multiply;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import asmCodeGenerator.runtime.RunTime;
import parseTree.ParseNode;

public class MultiplyRationalsCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment fragment = new ASMCodeFragment(CodeType.GENERATES_VALUE);
									// [... num(int) denom(int) num(int) denom(int)] 

		Macros.storeITo(fragment, RunTime.RATIONAL_DENOMINATOR_TEMP); // [... num2 den2 num]
		Macros.storeITo(fragment, RunTime.RATIONAL_NUMERATOR_TEMP); // [... num2 den2]
		Macros.storeITo(fragment, RunTime.RATIONAL_DENOMINATOR_TEMP2); // [... num2]
		Macros.storeITo(fragment, RunTime.RATIONAL_NUMERATOR_TEMP2); // [...]
		
		Macros.loadIFrom(fragment, RunTime.RATIONAL_NUMERATOR_TEMP2);	// [... num2]
		Macros.loadIFrom(fragment, RunTime.RATIONAL_NUMERATOR_TEMP);	// [... num2 num]
		fragment.add(Multiply);	// [... num2*num]
		
		Macros.loadIFrom(fragment, RunTime.RATIONAL_DENOMINATOR_TEMP);	// [... num2*num den]
		Macros.loadIFrom(fragment, RunTime.RATIONAL_DENOMINATOR_TEMP2); // [... num2*num den den2]
		fragment.add(Multiply);	// [... num2*num den*den2]
		
		fragment.add(Call, RunTime.LOWEST_TERMS); 
		
		return fragment;
	}
}
