package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.Duplicate;
import static asmCodeGenerator.codeStorage.ASMOpcode.Pop;
import static asmCodeGenerator.codeStorage.ASMOpcode.JumpTrue;
import static asmCodeGenerator.codeStorage.ASMOpcode.Jump;
import static asmCodeGenerator.codeStorage.ASMOpcode.Label;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import parseTree.ParseNode;

public class ShortCircuitOrCodeGenerator implements FullCodeGenerator {

	public ASMCodeFragment generate(ParseNode node, ASMCodeFragment... args) {
		ASMCodeFragment code = new ASMCodeFragment(CodeType.GENERATES_VALUE);
		 						// [... ]
		Labeller labeller = new Labeller("SC-Or");
		final String trueLabel = labeller.newLabel("short-circuit-true");
		final String endLabel = labeller.newLabel("end");
		
		// compute arg 1
		code.append(args[0]); // [... bool]
		
		// short circuiting test
		code.add(Duplicate);			 // [... bool bool]
		code.add(JumpTrue, trueLabel);	// [... bool]
		code.add(Pop);					// [... 0] -> [...]
		
		// compute arg 2 		
		code.append(args[1]);		// [... bool]
		code.add(Jump, endLabel);
		
		// the end
		code.add(Label, trueLabel); // [... 1]
		code.add(Label, endLabel);  // [... bool]
		
		return code;
	}
}
