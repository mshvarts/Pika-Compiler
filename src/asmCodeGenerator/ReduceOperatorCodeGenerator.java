package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.*;
import static asmCodeGenerator.runtime.RunTime.*;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import asmCodeGenerator.runtime.RunTime;
import parseTree.ParseNode;
import parseTree.nodeTypes.FunctionBodyNode;
import parseTree.nodeTypes.ReduceOperatorNode;
import semanticAnalyzer.types.Array;
import semanticAnalyzer.types.Type;

public class ReduceOperatorCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment frag = new ASMCodeFragment(CodeType.GENERATES_ADDRESS);
														// [... arrayPtr lambdaRef]
		Type subType = ((Array)node.child(0).getType()).getSubType();
		int subtypeSize = subType.getSize();
		 
		int statusFlags;
		if(subType.isReferenceType()) {
			statusFlags = 0b0010; // isPerm, isDel, isRef, isImmut
		} else {
			statusFlags = 0b0000; // isPerm, isDel, isRef, isImmut
		}

		Macros.storeITo(frag, LAMBDA_REF_TEMP);// [... arrayPtr]
		Macros.storeITo(frag, ARRAY_INDEXING_ARRAY);// [...]
		Macros.loadIFrom(frag, ARRAY_INDEXING_ARRAY);// [... arrayPtr]
		
		frag.add(PushI, ARRAY_LENGTH_OFFSET); 	//[... arrayPtr lengthOffset]
		frag.add(Add);				 			//[... arrayPtr+lengthOffset]
		frag.add(LoadI);						//[... arrayLength] 
		Macros.storeITo(frag, ARRAY_LENGTH_TEMPORARY);  // [...]
		Macros.loadIFrom(frag, ARRAY_LENGTH_TEMPORARY); // [... length]
		
		createEmptyArrayRecord(frag, statusFlags, subtypeSize); // [...]
		
		Macros.loadIFrom(frag,  RECORD_CREATION_TEMPORARY); // [... arrayPtr]
		frag.add(PushI, ARRAY_HEADER_SIZE);					// [... ptr AHS]
		frag.add(Add);										// [... elemsPtr]
		Macros.storeITo(frag, ARRAY_DATA_PTR_TEMPORARY);	// [...]
		
		Macros.loadIFrom(frag, ARRAY_INDEXING_ARRAY);		// [... srcAddr]
		frag.add(PushI, ARRAY_HEADER_SIZE);					// [... srcAddr SHS]
		frag.add(Add);										// [... srcAddr+SHS]
		
		// load element from source array
		frag.add(PushI, 0);									// [... srcArrAddr 0]
		Macros.storeITo(frag, ARRAY_INDEXING_INDEX);		// [... srcArrAddr]
		frag.add(PushI, 0);									// [... srcArrAddr 0]
		Macros.storeITo(frag, ARRAY_INDEX_TEMPORARY);		// [... srcArrAddr]
		
		Labeller labeller = new Labeller("reduceOperator");
		String loopArrStart = labeller.newLabel("loopStart");
		String addToArrayLabel = labeller.newLabel("addToArray");
		String dontAddToArrayLabel = labeller.newLabel("dontAddToArray");
		String goToNextLevelLabel = labeller.newLabel("goToNextElememt");
	
		frag.add(Label, loopArrStart);						// [... srcArrAddr]
		frag.add(Duplicate);								// [... srcArrAddr srcArrAddr]
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX);		// [... srcArrAddr srcArrAddr index]
		frag.add(PushI, subtypeSize);						// [... srcArrAddr srcArrAddr index typeSize]
		frag.add(Multiply);									// [... srcArrAddr srcArrAddr index*typeSize]
		frag.add(Add);										// [... srcArrAddr srcArrAddr+index*typeSize]
		frag.append(getLoadOpcode(subType));				// [... srcArrAddr *(srcArrAddr+elemOffSet)]  
		Macros.storeITo(frag, RunTime.TEMP_DATA_STORAGE);	// [... srcArrAddr ]  
		Macros.loadIFrom(frag, RunTime.TEMP_DATA_STORAGE);	// [... srcArrAddr *(srcArrAddr+elemOffSet)]  
		Macros.loadIFrom(frag, ARRAY_DATA_PTR_TEMPORARY);	// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr]
		Macros.loadIFrom(frag, ARRAY_INDEX_TEMPORARY);		// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr index]
		frag.add(PushI, subtypeSize);						// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr index typeSize]
		frag.add(Multiply);									// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr index*typeSize]
		frag.add(Add);										// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr+index*typeSize]
		frag.add(Exchange);									// [... srcArrAddr dstArrAddr+elemOffSet *(srcArrAddr+elemOffSet)]
		
		// place argument on the stack
		frag.add(PushI, -subtypeSize);					// [... srcArrAddr dstArrAddr+elemOffSet *(srcArrAddr+elemOffSet) -paramSize]
		Macros.addITo(frag, RunTime.STACK_POINTER);		// [... srcArrAddr dstArrAddr+elemOffSet *(srcArrAddr+elemOffSet)]
		Macros.loadIFrom(frag, RunTime.STACK_POINTER);	// [... srcArrAddr dstArrAddr+elemOffSet *(srcArrAddr+elemOffSet) sp]
		frag.add(Exchange); 							// [... srcArrAddr dstArrAddr+elemOffSet sp *(srcArrAddr+elemOffSet)]
		frag.append(getStoreOpcode(subType));			// [... srcArrAddr dstArrAddr+elemOffSet]
		
		// call lambda
		ParseNode lambdaNode = ((ReduceOperatorNode)node).getLambdaNode();
		ParseNode functionBodyNode = lambdaNode.child(1);
		String functionLabel = ((FunctionBodyNode)functionBodyNode).getStartLabel();
		frag.add(Call, functionLabel);					// [... srcArrAddr dstArrAddr+elemOffSet]
		
		// get return value and store original value to destination array if it is true
		Macros.loadIFrom(frag, RunTime.STACK_POINTER);	// [... srcArrAddr dstArrAddr+elemOffSet sp]
		frag.add(LoadC);								// [... srcArrAddr dstArrAddr+elemOffSet returnVal]
		frag.add(PushI, subtypeSize);				// [... srcArrAddr dstArrAddr+elemOffSet returnVal returnSize]
		Macros.addITo(frag, RunTime.STACK_POINTER);	// [... srcArrAddr dstArrAddr+elemOffSet returnVal]
		
		frag.add(JumpFalse, dontAddToArrayLabel);	// [... srcArrAddr dstArrAddr+elemOffSet]
		
		frag.add(Label, addToArrayLabel);
		Macros.loadIFrom(frag, RunTime.TEMP_DATA_STORAGE);// [... srcArrAddr dstArrAddr+elemOffSet elementVal]
		frag.append(getStoreOpcode(subType));		// [... srcArrAddr]
		Macros.incrementInteger(frag, RunTime.ARRAY_INDEX_TEMPORARY);
		frag.add(Jump, goToNextLevelLabel);			// [... srcArrAddr]
		
		frag.add(Label, dontAddToArrayLabel);		// [... srcArrAddr dstArrAddr+elemOffSet]
		frag.add(Pop);								// [... srcArrAddr]
		
		frag.add(Label, goToNextLevelLabel);		// [... srcArrAddr]
		
		Macros.loadIFrom(frag, ARRAY_LENGTH_TEMPORARY); // [... srcArrAddr length]
		frag.add(PushI, 1); 							// [... srcArrAddr length 1]
		frag.add(Subtract);								// [... srcArrAddr length-1]
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX); 	// [... srcArrAddr length-1 index]
		frag.add(Subtract);								// [... srcArrAddr length-1-index]
		Macros.incrementInteger(frag, ARRAY_INDEXING_INDEX);// [... srcArrAddr length-1-index]

		frag.add(JumpPos, loopArrStart);					// [... srcArrAddr]
		frag.add(Pop);										// [...]
		
		// set array length to the number of elements written
		Macros.loadIFrom(frag, ARRAY_INDEX_TEMPORARY); 	// [... length]
		Macros.writeIPtrOffset(frag, 						// [...]
				RECORD_CREATION_TEMPORARY,ARRAY_LENGTH_OFFSET);	
		
		Macros.loadIFrom(frag, ARRAY_DATA_PTR_TEMPORARY);	// [... dstElems]
		frag.add(PushI, ARRAY_HEADER_SIZE);					// [... dstElems+AHS]
		frag.add(Subtract);									// [... dstArray]
		
		return frag;
	}
}
