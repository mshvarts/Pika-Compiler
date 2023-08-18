package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.*;
import static asmCodeGenerator.runtime.RunTime.*;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import asmCodeGenerator.runtime.RunTime;
import parseTree.ParseNode;
import parseTree.nodeTypes.FunctionBodyNode;
import parseTree.nodeTypes.ZipOperatorNode;
import semanticAnalyzer.types.Array;
import semanticAnalyzer.types.Lambda;
import semanticAnalyzer.types.Type;

public class ZipOperatorCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment frag = new ASMCodeFragment(CodeType.GENERATES_ADDRESS);
														// [... arrayPtr arrayPtr2 lambdaRef]
		Type subType = ((Array)node.child(0).getType()).getSubType();
		int subtypeSize = subType.getSize();
		Type subType2 = ((Array)node.child(1).getType()).getSubType();
		int subtypeSize2 = subType2.getSize();
		Type returnType = ((Lambda)node.child(2).getType()).getReturnType();
		int returnTypeSize = returnType.getSize();
		
		int statusFlags;
		if(subType.isReferenceType()) {
			statusFlags = 0b0010; // isPerm, isDel, isRef, isImmut
		} else {
			statusFlags = 0b0000; // isPerm, isDel, isRef, isImmut
		}

		Labeller labeller = new Labeller("zipOperator");
		String loopArrStart = labeller.newLabel("loopStart");
		
		Macros.storeITo(frag, LAMBDA_REF_TEMP);			// [... arrayPtr arrayPtr2]
		Macros.storeITo(frag, ARRAY_INDEXING_ARRAY2);	// [... arrayPtr]
		Macros.storeITo(frag, ARRAY_INDEXING_ARRAY);	// [...]
		
		Macros.loadIFrom(frag, ARRAY_INDEXING_ARRAY);	// [... arrayPtr]
		frag.add(PushI, ARRAY_LENGTH_OFFSET); 			// [... arrayPtr lengthOffset]
		frag.add(Add);				 					// [... arrayPtr+lengthOffset]
		frag.add(LoadI);								// [... arrayLength1] 
		Macros.storeITo(frag, ARRAY_LENGTH_TEMPORARY);  // [...]
		Macros.loadIFrom(frag, ARRAY_LENGTH_TEMPORARY); // [... length]
		Macros.loadIFrom(frag, ARRAY_INDEXING_ARRAY2);	// [... length arrayPtr2]
		frag.add(PushI, ARRAY_LENGTH_OFFSET); 			// [... length arrayPtr2 lengthOffset]
		frag.add(Add);				 					// [... length arrayPtr2+lengthOffset]
		frag.add(LoadI);								// [... length arrayLength2] 
		
		frag.add(Subtract);								// [... lengthDiff] 
		frag.add(JumpTrue, RunTime.DIFFERENT_ARRAY_LENGTHS_RUNTIME_ERROR);	// [...] 
		
		Macros.loadIFrom(frag, ARRAY_LENGTH_TEMPORARY); // [... length]
		
		createEmptyArrayRecord(frag, statusFlags, returnTypeSize); // [...]
		
		Macros.loadIFrom(frag,  RECORD_CREATION_TEMPORARY); // [... arrayPtr]
		frag.add(PushI, ARRAY_HEADER_SIZE);					// [... ptr AHS]
		frag.add(Add);										// [... elemsPtr]
		Macros.storeITo(frag, ARRAY_DATA_PTR_TEMPORARY2);	// [...]
		
		// set i = 0
		frag.add(PushI, 0);									// [... 0]
		Macros.storeITo(frag, ARRAY_INDEXING_INDEX);		// [...]
		
		frag.add(Label, loopArrStart);						// [...]
		
		// load element from array 2
		Macros.loadIFrom(frag, ARRAY_INDEXING_ARRAY2);		// [... srcAddr]
		frag.add(PushI, ARRAY_HEADER_SIZE);					// [... srcAddr SHS]
		frag.add(Add);										// [... srcAddr+SHS]
		
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX);		// [... srcArrAddr2 index]
		frag.add(PushI, subtypeSize2);						// [... srcArrAddr2 index typeSize]
		frag.add(Multiply);									// [... srcArrAddr2 index*typeSize]
		frag.add(Add);										// [... srcArrAddr2+index*typeSize]
		frag.append(getLoadOpcode(subType2));				// [... *(srcArrAddr2+elemOffSet)] 
		//frag.add(PStack);
		
		// place element on the stack
		frag.add(PushI, -subtypeSize2);					// [... *(srcArrAddr2+elemOffSet) -paramSize]
		Macros.addITo(frag, RunTime.STACK_POINTER);		// [... *(srcArrAddr2+elemOffSet)]
		Macros.loadIFrom(frag, RunTime.STACK_POINTER);	// [... *(srcArrAddr2+elemOffSet) sp]
		frag.add(Exchange); 							// [... sp *(srcArrAddr2+elemOffSet)]
		frag.append(getStoreOpcode(subType2));			// [...]
		
		// load element from array 1
		Macros.loadIFrom(frag, ARRAY_INDEXING_ARRAY);		// [... srcAddr]
		frag.add(PushI, ARRAY_HEADER_SIZE);					// [... srcAddr SHS]
		frag.add(Add);										// [... srcAddr+SHS]
		
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX);		// [... srcArrAddr index]
		frag.add(PushI, subtypeSize);						// [... srcArrAddr index typeSize]
		frag.add(Multiply);									// [... srcArrAddr index*typeSize]
		frag.add(Add);										// [... srcArrAddr+index*typeSize]
		frag.append(getLoadOpcode(subType));				// [... *(srcArrAddr+elemOffSet)] 
		//frag.add(PStack);
		
		// place element on the stack
		frag.add(PushI, -subtypeSize);					// [... *(srcArrAddr+elemOffSet) -paramSize]
		Macros.addITo(frag, RunTime.STACK_POINTER);		// [... *(srcArrAddr+elemOffSet)]
		Macros.loadIFrom(frag, RunTime.STACK_POINTER);	// [... *(srcArrAddr+elemOffSet) sp]
		frag.add(Exchange); 							// [... sp *(srcArrAddr+elemOffSet)]
		frag.append(getStoreOpcode(subType));			// [...]
		
		
		// call lambda
		ParseNode lambdaNode = ((ZipOperatorNode)node).getLambdaNode();
		ParseNode functionBodyNode = lambdaNode.child(1);
		String functionLabel = ((FunctionBodyNode)functionBodyNode).getStartLabel();
		frag.add(Call, functionLabel);					// [... srcArrAddr]
		
		// get return value 
		Macros.loadIFrom(frag, RunTime.STACK_POINTER);	// [... sp]
		frag.append(getLoadOpcode(returnType));		// [... returnVal]
		frag.add(PushI, returnTypeSize);			// [... returnVal returnSize]
		Macros.addITo(frag, RunTime.STACK_POINTER);	// [... returnVal]
		
		// store return value into result array
		Macros.loadIFrom(frag, ARRAY_DATA_PTR_TEMPORARY2);// [... returnVal dstArrAddr]
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX);	// [... returnVal dstArrAddr index]
		frag.add(PushI, returnTypeSize);				// [... returnVal dstArrAddr index typeSize]
		//frag.add(PStack);
		frag.add(Multiply);								// [... returnVal dstArrAddr index*typeSize]
		frag.add(Add);									// [... returnVal dstArrAddr+index*typeSize]
		//frag.add(PStack);
		frag.add(Exchange);								// [... dstArrAddr+elemOffSet returnVal]
		frag.append(getStoreOpcode(returnType));		// [...]
		
		// update loop counter and exit if reached last index
		Macros.loadIFrom(frag, ARRAY_LENGTH_TEMPORARY); // [... length]
		frag.add(PushI, 1); 							// [... length 1]
		frag.add(Subtract);								// [... length-1]
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX); 	// [... length-1 index]
		frag.add(Subtract);								// [... length-1-index]
		Macros.incrementInteger(frag, ARRAY_INDEXING_INDEX);// [... length-1-index]

		frag.add(JumpPos, loopArrStart);					// [...]
		
		// set array length to the number of elements written
		Macros.loadIFrom(frag, ARRAY_LENGTH_TEMPORARY); 	// [... length]
		Macros.writeIPtrOffset(frag, 						// [...]
				RECORD_CREATION_TEMPORARY,ARRAY_LENGTH_OFFSET);	
		
		Macros.loadIFrom(frag, ARRAY_DATA_PTR_TEMPORARY2);	// [... dstElems]
		frag.add(PushI, ARRAY_HEADER_SIZE);					// [... dstElems+AHS]
		frag.add(Subtract);									// [... dstArray]
		
		return frag;
	}
}
