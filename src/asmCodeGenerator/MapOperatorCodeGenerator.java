package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.*;
import static asmCodeGenerator.runtime.RunTime.*;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import asmCodeGenerator.runtime.RunTime;
import parseTree.ParseNode;
import parseTree.nodeTypes.FunctionBodyNode;
import parseTree.nodeTypes.MapOperatorNode;
import semanticAnalyzer.types.Array;
import semanticAnalyzer.types.Type;

public class MapOperatorCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment frag = new ASMCodeFragment(CodeType.GENERATES_ADDRESS);
														// [... arrayPtr lambdaRef]
		Type subType = ((Array)node.child(0).getType()).getSubType();
		int subtypeSize = subType.getSize();
		Type subTypeDest = ((Array)node.getType()).getSubType();
		int subtypeSizeDest = subTypeDest.getSize();
		 
		int statusFlags;
		if(subTypeDest.isReferenceType()) {
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
		
		createEmptyArrayRecord(frag, statusFlags, subtypeSizeDest); // [...]
		
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
		
		Labeller labeller = new Labeller("mapOperator");
		String loopArrCopyStart = labeller.newLabel("loopStart");
		
		frag.add(Label, loopArrCopyStart);					// [... srcArrAddr]
		frag.add(Duplicate);								// [... srcArrAddr srcArrAddr]
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX);		// [... srcArrAddr srcArrAddr index]
		//frag.add(PStack);
		frag.add(PushI, subtypeSize);						// [... srcArrAddr srcArrAddr index typeSize]
		frag.add(Multiply);									// [... srcArrAddr srcArrAddr index*typeSize]
		frag.add(Add);										// [... srcArrAddr srcArrAddr+index*typeSize]
		frag.append(getLoadOpcode(subType));				// [... srcArrAddr *(srcArrAddr+elemOffSet)]  
		//frag.add(PStack);
		
		Macros.loadIFrom(frag, ARRAY_DATA_PTR_TEMPORARY);	// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr]
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX);		// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr index]
		frag.add(PushI, subtypeSizeDest);					// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr index typeSize]
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
		ParseNode lambdaNode = ((MapOperatorNode)node).getLambdaNode();
		ParseNode functionBodyNode = lambdaNode.child(1);
		String functionLabel = ((FunctionBodyNode)functionBodyNode).getStartLabel();
		frag.add(Call, functionLabel);					// [... srcArrAddr dstArrAddr+elemOffSet]
		
		// get return value and store to destination array
		Macros.loadIFrom(frag, RunTime.STACK_POINTER);	// [... srcArrAddr dstArrAddr+elemOffSet sp]
		frag.append(getLoadOpcode(subTypeDest));		// [... srcArrAddr dstArrAddr+elemOffSet returnVal]
		frag.add(PushI, subtypeSizeDest);				// [... srcArrAddr dstArrAddr+elemOffSet returnVal returnSize]
		Macros.addITo(frag, RunTime.STACK_POINTER);		// [... srcArrAddr dstArrAddr+elemOffSet returnVal]
		//frag.add(PStack);
		frag.append(getStoreOpcode(subTypeDest));		// [... srcArrAddr]
		
		Macros.loadIFrom(frag, ARRAY_LENGTH_TEMPORARY); // [... srcArrAddr length]
		// TODO: need to figure out why we need a off by one error
		//frag.add(PushI, 1); 							// [... srcArrAddr length 1]
		//frag.add(Subtract);								// [... srcArrAddr length-1]
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX); 	// [... srcArrAddr length index]
		frag.add(Subtract);								// [... srcArrAddr length-index]
		Macros.incrementInteger(frag, ARRAY_INDEXING_INDEX);// [... srcArrAddr length-index]

		frag.add(JumpPos, loopArrCopyStart);				// [... srcArrAddr]
		frag.add(Pop);										// [...]
		
		Macros.loadIFrom(frag, ARRAY_LENGTH_TEMPORARY); 	// [... length]
		Macros.writeIPtrOffset(frag, 						// [...]
				RECORD_CREATION_TEMPORARY,ARRAY_LENGTH_OFFSET);	
		
		Macros.loadIFrom(frag, ARRAY_DATA_PTR_TEMPORARY);	// [... dstElems]
		frag.add(PushI, ARRAY_HEADER_SIZE);					// [... dstElems+AHS]
		frag.add(Subtract);									// [... dstArray]
		
		return frag;
	}
}
