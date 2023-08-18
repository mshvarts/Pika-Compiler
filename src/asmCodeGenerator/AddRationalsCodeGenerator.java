package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.Multiply;
import static asmCodeGenerator.codeStorage.ASMOpcode.Add;
import static asmCodeGenerator.codeStorage.ASMOpcode.Call;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import asmCodeGenerator.runtime.RunTime;
import parseTree.ParseNode;

public class AddRationalsCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment fragment = new ASMCodeFragment(CodeType.GENERATES_VALUE);
									// [... num(int) denom(int) num(int) denom(int)] 

		Macros.storeITo(fragment, RunTime.RATIONAL_DENOMINATOR_TEMP); // [... num2 den2 num]
		Macros.storeITo(fragment, RunTime.RATIONAL_NUMERATOR_TEMP); // [... num2 den2]
		Macros.storeITo(fragment, RunTime.RATIONAL_DENOMINATOR_TEMP2); // [... num2]
		Macros.storeITo(fragment, RunTime.RATIONAL_NUMERATOR_TEMP2); // [...]
		
		Macros.loadIFrom(fragment, RunTime.RATIONAL_NUMERATOR_TEMP);	// [... num]
		Macros.loadIFrom(fragment, RunTime.RATIONAL_DENOMINATOR_TEMP2); // [... num den2]
		fragment.add(Multiply);											// [... num*den2]
		Macros.loadIFrom(fragment, RunTime.RATIONAL_NUMERATOR_TEMP2);	// [... num*den2 num2]
		Macros.loadIFrom(fragment, RunTime.RATIONAL_DENOMINATOR_TEMP);	// [... num*den2 num2 den]
		fragment.add(Multiply);											// [... num*den2 num2*den]
		fragment.add(Add);												// [... num+num2]
		
		Macros.loadIFrom(fragment, RunTime.RATIONAL_DENOMINATOR_TEMP);	// [... num+num2 den]
		Macros.loadIFrom(fragment, RunTime.RATIONAL_DENOMINATOR_TEMP2); // [... num+num2 den den2]
		fragment.add(Multiply);	// [... num+num2 den*den2]
		
		fragment.add(Call, RunTime.LOWEST_TERMS); 	// [... newNum newDen]
		
		return fragment;
	}
}
