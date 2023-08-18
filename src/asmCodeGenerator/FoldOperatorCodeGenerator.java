package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.*;
import static asmCodeGenerator.runtime.RunTime.*;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import asmCodeGenerator.runtime.RunTime;
import parseTree.ParseNode;
import parseTree.nodeTypes.FoldOperatorNode;
import parseTree.nodeTypes.FunctionBodyNode;
import semanticAnalyzer.types.Array;
import semanticAnalyzer.types.Type;

public class FoldOperatorCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment frag = new ASMCodeFragment(CodeType.GENERATES_ADDRESS);
														// [... arrayPtr lambdaRef]
		Type subType = ((Array)node.child(0).getType()).getSubType();
		int subtypeSize = subType.getSize();
		Type returnType = node.getType();
		int returnTypeSize = node.getType().getSize();
		
		Labeller labeller = new Labeller("foldOperator");
		String loopArrStart = labeller.newLabel("loopStart");
		String returnFirstElement = labeller.newLabel("returnFirstElement");
		
		Macros.storeITo(frag, LAMBDA_REF_TEMP);// [... arrayPtr]
		Macros.storeITo(frag, ARRAY_INDEXING_ARRAY);// [...]
		Macros.loadIFrom(frag, ARRAY_INDEXING_ARRAY);// [... arrayPtr]
		
		frag.add(PushI, ARRAY_LENGTH_OFFSET); 	//[... arrayPtr lengthOffset]
		frag.add(Add);				 			//[... arrayPtr+lengthOffset]
		frag.add(LoadI);						//[... arrayLength] 
		Macros.storeITo(frag, ARRAY_LENGTH_TEMPORARY);  // [...]
		Macros.loadIFrom(frag, ARRAY_LENGTH_TEMPORARY); // [... length]
		
		frag.add(JumpFalse, RunTime.ZERO_LENGTH_RUNTIME_ERROR); // [...]
		
		Macros.loadIFrom(frag, ARRAY_INDEXING_ARRAY);		// [... srcAddr]
		frag.add(PushI, ARRAY_HEADER_SIZE);					// [... srcAddr SHS]
		frag.add(Add);										// [... srcAddr+SHS]
		
		frag.append(getLoadOpcode(subType));				// [... A[0]]
		Macros.storeITo(frag, TEMP_DATA_STORAGE);			// [...]
		
		// Return A[0] if length A == 1
		Macros.loadIFrom(frag, ARRAY_LENGTH_TEMPORARY); 	// [... length]
		frag.add(PushI, 1);									// [... length 1]
		frag.add(Subtract);									// [... length-1]
		frag.add(JumpFalse, returnFirstElement); 			// [... ]
		
		Macros.loadIFrom(frag, ARRAY_INDEXING_ARRAY);		// [... srcAddr]
		frag.add(PushI, ARRAY_HEADER_SIZE);					// [... srcAddr SHS]
		frag.add(Add);										// [... srcAddr+SHS]
		
		// set i = 1
		frag.add(PushI, 1);									// [... srcArrAddr 0]
		Macros.storeITo(frag, ARRAY_INDEXING_INDEX);		// [... srcArrAddr]
		
		frag.add(Label, loopArrStart);						// [... srcArrAddr]
		
		// load previous iteration result x{i}
		Macros.loadIFrom(frag, TEMP_DATA_STORAGE);			// [... srcArrAddr x{i}] 
		
		// place previous iteration result x{i} on the stack
		frag.add(PushI, -returnTypeSize);					// [... srcArrAddr x{i} -paramSize]
		Macros.addITo(frag, RunTime.STACK_POINTER);		// [... srcArrAddr x{i}]
		Macros.loadIFrom(frag, RunTime.STACK_POINTER);	// [... srcArrAddr x{i} sp]
		frag.add(Exchange); 							// [... srcArrAddr sp x{i}]
		frag.append(getStoreOpcode(returnType));		// [... srcArrAddr]
		
		// load i-th element from source array
		frag.add(Duplicate);							// [... srcArrAddr srcArrAddr]		
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX);	// [... srcArrAddr srcArrAddr index]
		frag.add(PushI, subtypeSize);					// [... srcArrAddr srcArrAddr index typeSize]
		frag.add(Multiply);								// [... srcArrAddr srcArrAddr index*typeSize]
		frag.add(Add);									// [... srcArrAddr srcArrAddr+index*typeSize]
		frag.append(getLoadOpcode(subType));			// [... srcArrAddr *(srcArrAddr+elemOffSet)]   
		
		// place i-th argument on the stack
		frag.add(PushI, -subtypeSize);					// [... srcArrAddr *(srcArrAddr+elemOffSet) -paramSize]
		Macros.addITo(frag, RunTime.STACK_POINTER);		// [... srcArrAddr *(srcArrAddr+elemOffSet)]
		Macros.loadIFrom(frag, RunTime.STACK_POINTER);	// [... srcArrAddr *(srcArrAddr+elemOffSet) sp]
		frag.add(Exchange); 							// [... srcArrAddr sp *(srcArrAddr+elemOffSet)]
		frag.append(getStoreOpcode(subType));			// [... srcArrAddr]
		
		// call lambda
		ParseNode lambdaNode = ((FoldOperatorNode)node).getLambdaNode();
		ParseNode functionBodyNode = lambdaNode.child(1);
		String functionLabel = ((FunctionBodyNode)functionBodyNode).getStartLabel();
		frag.add(Call, functionLabel);					// [... srcArrAddr]
		
		// get return value
		Macros.loadIFrom(frag, RunTime.STACK_POINTER);	// [... srcArrAddr sp]
		frag.append(getLoadOpcode(returnType));		// [... srcArrAddr returnVal]
		frag.add(PushI, returnTypeSize);				// [... srcArrAddr returnVal returnSize]
		Macros.addITo(frag, RunTime.STACK_POINTER);	// [... srcArrAddr returnVal]
		
		Macros.storeITo(frag, TEMP_DATA_STORAGE);	// [... srcArrAddr]
		
		// update loop counter and exit if reached last index
		Macros.loadIFrom(frag, ARRAY_LENGTH_TEMPORARY); // [... srcArrAddr length]
		frag.add(PushI, 1); 							// [... srcArrAddr length 1]
		frag.add(Subtract);								// [... srcArrAddr length-1]
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX); 	// [... srcArrAddr length-1 index]
		frag.add(Subtract);								// [... srcArrAddr length-1-index]
		Macros.incrementInteger(frag, ARRAY_INDEXING_INDEX);// [... srcArrAddr length-1-index]

		frag.add(JumpPos, loopArrStart);				// [... srcArrAddr]
		frag.add(Pop);									// [...]
		
		frag.add(Label, returnFirstElement);
		Macros.loadIFrom(frag, TEMP_DATA_STORAGE); 		// [... x{n-1} or A[0]]
		
		return frag;
	}
}
