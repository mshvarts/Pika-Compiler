package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.Duplicate;
import static asmCodeGenerator.codeStorage.ASMOpcode.JumpFalse;
import static asmCodeGenerator.codeStorage.ASMOpcode.Multiply;
import static asmCodeGenerator.codeStorage.ASMOpcode.Divide;
import static asmCodeGenerator.codeStorage.ASMOpcode.Call;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import asmCodeGenerator.runtime.RunTime;
import parseTree.ParseNode;

public class RationalRationalizeCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment fragment = new ASMCodeFragment(CodeType.GENERATES_VALUE);
		 						  // [... num den over]
		fragment.add(Duplicate);  // [... num den over over]
		fragment.add(JumpFalse,   // [... num den over]
				RunTime.RATIONAL_DIVIDE_BY_ZERO_RUNTIME_ERROR);  
		
		Macros.storeITo(fragment, RunTime.EXPRESS_OVER_DENOMINATOR); // [... num den]
		Macros.storeITo(fragment, RunTime.RATIONAL_DENOMINATOR_TEMP); // [... num ]
		Macros.storeITo(fragment, RunTime.RATIONAL_NUMERATOR_TEMP); // [... ]
		
		Macros.loadIFrom(fragment, RunTime.RATIONAL_NUMERATOR_TEMP); // [... num ]
		Macros.loadIFrom(fragment, RunTime.EXPRESS_OVER_DENOMINATOR); // [... over ]
		fragment.add(Multiply); 									// [... num*over ]
		
		Macros.loadIFrom(fragment, RunTime.RATIONAL_DENOMINATOR_TEMP); // [... num*over den]
		fragment.add(Divide);											// [... num*over/den]
		
		Macros.loadIFrom(fragment, RunTime.EXPRESS_OVER_DENOMINATOR);	// [... num*over/den den]
		fragment.add(Call, RunTime.LOWEST_TERMS);						// [... (num*over/den)/gdc den/gdc]
		
		return fragment;
	}
}
