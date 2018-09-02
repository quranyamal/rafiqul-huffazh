package org.tangaya.rafiqulhuffazh.util.diff;/*
 * Word Based Text Diff.
 *
 * Copyright 2017 Nadav Benedek.
 *
 * Takes the output of Diff Match Patch (character based - http://code.google.com/p/google-diff-match-patch/) 
 * and produce a  word based output.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.LinkedList;

//import wordbasedtextdiff.DoublyLinkedList;
//import wordbasedtextdiff.DoublyLinkedList.Node;
//import textdiff.diff_match_patch;
//import textdiff.diff_match_patch.Diff;

import org.tangaya.rafiqulhuffazh.util.diff.diff_match_patch.Diff;
import org.tangaya.rafiqulhuffazh.util.diff.DoublyLinkedList.Node;


public class AdvancedWordBasedDiff {
    
    static String wordDelimiters = " \n.,:;\""; // Space, newline, comma, colon, semicolon, double quotes
    
    public static LinkedList<Diff> createWordBasedDiffLinkedList(LinkedList<Diff> linkedList){
        diff_match_patch dmp = new diff_match_patch();
        String source = dmp.diff_text1(linkedList);
        String target = dmp.diff_text2(linkedList);

        return getWordBasedDiff(source, target);
    }
    public static LinkedList<Diff> getWordBasedDiff(String source, String target) {
        diff_match_patch dmp = new diff_match_patch();
        LinkedList<Diff> characterBasedDiff = dmp.diff_main(source, target);
        return getWordBasedDiff(characterBasedDiff);
    }
    
    public static class DiffWithPointers extends Diff{                  
        Node<Diff> pointerToOtherList = null;   // Points to the other SRC or TAR list
        
        public DiffWithPointers(diff_match_patch.Operation operation, String text) {            
            super(operation, text);
        }
    }
  
    public static boolean isDelimiterOnLeft(String s){
        if (s.isEmpty()) throw new RuntimeException("Got empty string! Len: "+s.length());
        if (wordDelimiters.indexOf(s.charAt(0)) != -1) return true;              
        return false;
    }
    
    public static boolean isDelimiterOnRight(String s){
        if (wordDelimiters.indexOf(s.charAt(s.length()-1)) != -1) return true;              
        return false;
    }
    
    public static void eatOneEqualLeftUpToDelimiter(Node<Diff> master, Node<Diff> left, 
                                                   diff_match_patch.Operation otherListOperation ){
        if (left.getItem().operation != diff_match_patch.Operation.EQUAL) throw new RuntimeException("Not equal to the left");
        String leftText = left.getItem().text;  
        Node<Diff> otherListEqual = ((DiffWithPointers) left.getItem()).pointerToOtherList;
        // Find position of delimiter
        int positionOfDelimiter = lastIndexOfDelimiter(leftText);
        if (positionOfDelimiter == -1){
            // No delimiter, so we have to kill this equal op (eat completly)
            master.getItem().text = leftText + master.getItem().text;
            otherListEqual.getItem().operation = otherListOperation; // It's no more equal henceforth
            ((DiffWithPointers)otherListEqual.getItem()).pointerToOtherList = null; // We are no equal anymore
            left.remove(); // Kill the left equal cell
            
            // Now we need to check that we are not adjacent to the same op on the left. If it was Space, E, D, and the space was deleted, it will happen while we process the D. It can't happen twice because we're keeping the Invariant
            if (master.hasPrevious()&& master.previous().getItem().operation == master.getItem().operation){
                master.getItem().text =  master.previous().getItem().text + master.getItem().text; 
                master.previous().remove();
            }
            
            // Now in the other list we may be adjacent to another INS op, so we need to compress it
            if (otherListEqual.hasPrevious() && otherListEqual.previous().getItem().operation == otherListOperation){
                otherListEqual.getItem().text = otherListEqual.previous().getItem().text + otherListEqual.getItem().text;
                otherListEqual.previous().remove();
            } // or to the right
            if (otherListEqual.hasNext()&& otherListEqual.next().getItem().operation == otherListOperation){
                otherListEqual.next().getItem().text = otherListEqual.getItem().text + otherListEqual.next().getItem().text ;
                otherListEqual.remove();
            }
            
        } else {
            // There is delimiter. shrink the equal (inclusive of the space)
            // Here we want to bit to the RIGHT of the delimiter, that's why we put +1
            String breakLeft = leftText.substring(0, positionOfDelimiter+1);
            String breakRight = leftText.substring(positionOfDelimiter+1, leftText.length());
//            System.out.println("breakright is: ["+breakRight+"]. Original: ["+leftText+"]. Delimiter: "+positionOfDelimiter);
//            System.out.println("breakLeft is: ["+breakLeft+"]. Original: ["+leftText+"]. Delimiter: "+positionOfDelimiter);
            left.getItem().text   = breakLeft;
            master.getItem().text =  breakRight + master.getItem().text;
            otherListEqual.getItem().text = breakLeft;
            Node<Diff> whatWasOnceEqual = otherListEqual.injectAfter(new Diff(otherListOperation, breakRight));
            
            // Now in the other list we may be adjacent to another INS op, so we need to compress it
            if (whatWasOnceEqual.hasPrevious() && whatWasOnceEqual.previous().getItem().operation == otherListOperation){
                whatWasOnceEqual.getItem().text = whatWasOnceEqual.previous().getItem().text + whatWasOnceEqual.getItem().text;
                whatWasOnceEqual.previous().remove();
            } // or to the right
            if (whatWasOnceEqual.hasNext() && whatWasOnceEqual.next().getItem().operation == otherListOperation){
                whatWasOnceEqual.next().getItem().text = whatWasOnceEqual.getItem().text + whatWasOnceEqual.next().getItem().text ;
                whatWasOnceEqual.remove();
            }
            
        }
                
    } // EOF eatOneEqualLeftUpToDelimiter
          
    public static void eatOneEqualRightUpToDelimiter(Node<Diff> master, Node<Diff> right, 
                                                   diff_match_patch.Operation otherListOperation ){
        if (right.getItem().operation != diff_match_patch.Operation.EQUAL) {
            throw new RuntimeException("Not equal to the right. Instead op is: "+right.getItem().operation);
        }
        String rightText = right.getItem().text;  
        Node<Diff> otherListEqual = ((DiffWithPointers) right.getItem()).pointerToOtherList;
        // Find position of delimiter
        int positionOfDelimiter = firstIndexOfDelimiter(rightText);
        if (positionOfDelimiter == -1){
            // No delimiter, so we have to kill this equal op (eat completly)
            master.getItem().text =  master.getItem().text + rightText;
            otherListEqual.getItem().operation = otherListOperation; // It's no more equal henceforth
            ((DiffWithPointers)otherListEqual.getItem()).pointerToOtherList = null; // We are no equal anymore
            right.remove(); // Kill the left equal cell
            // Now we need to check that we are not adjacent to the same op on the right. If it was D,E,D and we ate right, we could be next to D. We only need to check once, because after that D, there must be E...
            if (master.hasNext()&& master.next().getItem().operation == master.getItem().operation){
                master.getItem().text = master.getItem().text + master.next().getItem().text ;
                master.next().remove();
            }
            
            // Now in the other list we may be adjacent to another INS op, so we need to compress it. Search to left
            if (otherListEqual.hasPrevious() && otherListEqual.previous().getItem().operation == otherListOperation){
                otherListEqual.getItem().text = otherListEqual.previous().getItem().text + otherListEqual.getItem().text;
                otherListEqual.previous().remove();
            } // or to the right, or both
            if (otherListEqual.hasNext()&& otherListEqual.next().getItem().operation == otherListOperation){
                otherListEqual.next().getItem().text = otherListEqual.getItem().text + otherListEqual.next().getItem().text ;
                otherListEqual.remove();
            }
            
        } else {
            // There is delimiter. shrink the equal (inclusive of the space)
            // Here we want to bite LEFT to the delimeter, that's why no +1
            String breakLeft  = rightText.substring(0, positionOfDelimiter);
            String breakRight = rightText.substring(positionOfDelimiter, rightText.length());
//            System.out.println("breakright is: ["+breakRight+"]. Original: ["+rightText+"]. Delimiter: "+positionOfDelimiter);
//                        System.out.println("breakLeft is: ["+breakLeft+"]. Original: ["+rightText+"]. Delimiter: "+positionOfDelimiter);
            right.getItem().text  = breakRight;
            master.getItem().text =   master.getItem().text + breakLeft;
            otherListEqual.getItem().text = breakRight;
            Node<Diff> whatWasOnceEqual = otherListEqual.injectBefore(new Diff(otherListOperation, breakLeft));
            
            // Now in the other list we may be adjacent to another INS op, so we need to compress it
            if (whatWasOnceEqual.hasPrevious() && whatWasOnceEqual.previous().getItem().operation == otherListOperation){
                whatWasOnceEqual.getItem().text = whatWasOnceEqual.previous().getItem().text + whatWasOnceEqual.getItem().text;
                whatWasOnceEqual.previous().remove();
            } // or to the right
            if (whatWasOnceEqual.hasNext() && whatWasOnceEqual.next().getItem().operation == otherListOperation){
                whatWasOnceEqual.next().getItem().text = whatWasOnceEqual.getItem().text + whatWasOnceEqual.next().getItem().text ;
                whatWasOnceEqual.remove();
            }
        }
                
    }
    
    public static int lastIndexOfDelimiter(String str){
        for (int i=str.length() -1 ; i>=0; i--){
            if (wordDelimiters.indexOf(str.charAt(i)) != -1) return i; 
        }
        return -1;
    }
    
    public static int firstIndexOfDelimiter(String str){
        for (int i=0 ; i<str.length(); i++){
            if (wordDelimiters.indexOf(str.charAt(i)) != -1) return i; 
        }
        return -1;
    }
    
    public static LinkedList<Diff> mergeDiffLists(LinkedList<Diff> dels, LinkedList<Diff> ins){
        // Remember the lists are half compress so can't be D,D or I,I, only E,E
        int sIndex = 0;
        int tIndex = 0;
        
        LinkedList<Diff> combined = new LinkedList<>();
        
        if (dels.size() == 0) return combined;
        if (ins.size() == 0) return combined;
        
        while (true){
            if (sIndex == dels.size() && tIndex == ins.size()) break;
            
            if (sIndex == dels.size()){ // Finished with source, so target can't be equal...
                Diff insDiff = ins.get(tIndex);
                if (insDiff.operation == diff_match_patch.Operation.EQUAL) throw new RuntimeException("can't be equal...");
                combined.add(insDiff);
                tIndex++;
                if (tIndex!=ins.size()) throw new RuntimeException("tIndex must reach the end");                
                break;
            }
            
            if (tIndex == ins.size()){ // Finished with target, so source can't be equal...
                Diff delDiff = dels.get(sIndex);
                if (delDiff.operation == diff_match_patch.Operation.EQUAL) throw new RuntimeException("can't be equal...2");
                combined.add(delDiff);
                sIndex++;
                if (sIndex!=dels.size()) throw new RuntimeException("sIndex must reach the end");                
                break;
            }
            
            Diff delDiff = dels.get(sIndex);
            Diff insDiff = ins.get(tIndex);
            
            
            if (delDiff.operation == diff_match_patch.Operation.EQUAL && insDiff.operation == diff_match_patch.Operation.EQUAL){
                if (delDiff.operation != insDiff.operation) {
                    throw new RuntimeException("Commands not equal. sIndex: "+sIndex+" "+dels.get(sIndex).toString()+" tIndex: "+tIndex+" "+ins.get(tIndex).toString()+". Previous delList: "+dels.get(sIndex-1).toString()+" insList: "+ins.get(tIndex -1).toString());
                }
                if (delDiff.text.compareTo(insDiff.text) != 0) {
                    throw new RuntimeException("Equals not equal. sIndex: "+sIndex+" ["+delDiff.text+"] tIndex: "+tIndex+" ["+insDiff.text+"]");
                }
                combined.add(delDiff);
                sIndex++;
                tIndex++;
            } else if (delDiff.operation == diff_match_patch.Operation.EQUAL && insDiff.operation == diff_match_patch.Operation.INSERT){
                combined.add(insDiff);
                tIndex++; // Now it must point to EQ
            } else if (delDiff.operation == diff_match_patch.Operation.DELETE && insDiff.operation == diff_match_patch.Operation.EQUAL) {
                combined.add(delDiff);
                sIndex++; // now it must be on equal
            } else if (delDiff.operation == diff_match_patch.Operation.DELETE && insDiff.operation == diff_match_patch.Operation.INSERT) {
                combined.add(delDiff);
                combined.add(insDiff);
                sIndex++;
                tIndex++;               
            }
        }
                
        return combined;
    } // EOF mergeDiffLists

    // Check that both are half compressed (no D,D no I,I)
    // Check that the number of equal parts are equal
    public static void checkAlgorithmInvariant(DoublyLinkedList<Diff> src, DoublyLinkedList<Diff> tar){
        System.out.println("DM: checkAlgorithmInvariant");
        int numEQsrc = 0;
        Node<Diff> srcNode = src.preNode();
        diff_match_patch.Operation lastOperation = null;
        while (srcNode.hasNext()) {
            srcNode  = srcNode.next();
            Diff srcOp = srcNode.getItem();
            if (srcOp.operation == diff_match_patch.Operation.EQUAL){
                numEQsrc++;
            }
            if ((lastOperation != null) && (lastOperation==diff_match_patch.Operation.DELETE) && srcOp.operation==diff_match_patch.Operation.DELETE) {
                throw new RuntimeException("Found two adjacents DELs!");
            }
            lastOperation = srcOp.operation;
        }
        
        int numEQtar = 0;
        Node<Diff> tarNode = tar.preNode();
        lastOperation = null;
        while (tarNode.hasNext()) {
            tarNode  = tarNode.next();
            Diff tarOp = tarNode.getItem();
            if (tarOp.operation == diff_match_patch.Operation.EQUAL){
                numEQtar++;
            }
            if ((lastOperation != null) && (lastOperation==diff_match_patch.Operation.INSERT) && tarOp.operation==diff_match_patch.Operation.INSERT) {
                throw new RuntimeException("Found two adjacents INS!");
            }
            lastOperation = tarOp.operation;
        }
        
        if (numEQsrc != numEQtar){
            throw new RuntimeException("Num EQ elements in src and tar is not the same!");
        }
    } // checkAlgorithmInvariant
    
    public static LinkedList<Diff> getWordBasedDiff(LinkedList<Diff> characterBasedDiff){
         // (1) Split the list to source (EQ, DEL) and target (EQ, INS)         
         DoublyLinkedList<Diff> sourceDiff = new DoublyLinkedList<>();
         DoublyLinkedList<Diff> targetDiff = new DoublyLinkedList<>();
         
         for (Diff diff : characterBasedDiff){
             if (diff.operation == diff_match_patch.Operation.EQUAL){
                 DiffWithPointers eqDiffSource = new DiffWithPointers(diff_match_patch.Operation.EQUAL, diff.text);
                 DiffWithPointers eqDiffTarget = new DiffWithPointers(diff_match_patch.Operation.EQUAL, diff.text);
                 
                 Node nodeSource = sourceDiff.addAtTheEnd(eqDiffSource);
                 Node nodeTarget = targetDiff.addAtTheEnd(eqDiffTarget);
                 
                 // Set cross reference pointers
                 eqDiffSource.pointerToOtherList = nodeTarget;
                 eqDiffTarget.pointerToOtherList = nodeSource;
                 
                 
             } else if (diff.operation == diff_match_patch.Operation.DELETE){
                 sourceDiff.addAtTheEnd(new Diff(diff_match_patch.Operation.DELETE, diff.text));                 
             } else if (diff.operation == diff_match_patch.Operation.INSERT){
                 targetDiff.addAtTheEnd(new Diff(diff_match_patch.Operation.INSERT, diff.text));                 
             }
         }                                    
  
         // DEBUG
//         textdiff.diff_match_patch.printAsString(sourceDiff.convertToLinkedList());
//         textdiff.diff_match_patch.printAsString(targetDiff.convertToLinkedList());
//         System.out.println("==========");
         
         // Process the Source list (DELS)
         Node<Diff> srcNode = sourceDiff.preNode();
         while (srcNode.hasNext()){
             srcNode   = srcNode.next();                                       
             Diff srcOp = srcNode.getItem();
//             System.out.println(srcOp.text);                                                    
             if (srcOp.operation == diff_match_patch.Operation.DELETE){
                 // Check LEFT
                 if (!isDelimiterOnLeft(srcOp.text)) { // If we already contain a delimiter in the left inner text, no need to eat left                 
                     // So before us only nothing, or equal (since we're always half-compressed), and the equal was already been dealt with, in it's left side
                     // Check if the left operation has a NON delimeter character, if so, need to 'eat' it
                     while (true) { // Continue to eat until no bites were successful
                         if (srcNode.hasPrevious()) {
                             String leftText = srcNode.previous().getItem().text;
                             if (!isDelimiterOnRight(leftText)) {
//                                 System.out.println("the DEL [" + srcOp.text + "] needs to eat left");
                                 eatOneEqualLeftUpToDelimiter(srcNode, srcNode.previous(), diff_match_patch.Operation.INSERT);
                                 // Convert the equal on INS list to insert                                                                                       
                             } else {
                                 break;
                             }
                         } else {
                             break;
                         }
                     }
                 }
                 if (!isDelimiterOnRight(srcOp.text)) { 
                     // EAT RIGHT
                     while (true) { // Continue to eat until no bites were successful
                         if (srcNode.hasNext()) {
                             String rightText = srcNode.next().getItem().text;
                             if (!isDelimiterOnLeft(rightText)) {
                                 eatOneEqualRightUpToDelimiter(srcNode, srcNode.next(), diff_match_patch.Operation.INSERT);
                             } else {
                                 break;
                             }
                         } else {
                             break;
                         }
                     }                     
                 }                 
             }    
         } // EOF all source list elements (DELS)
                  
         
         // Process the Target list (INS)
         srcNode = targetDiff.preNode();
         while (srcNode.hasNext()){
             srcNode   = srcNode.next();                                       
             Diff srcOp = srcNode.getItem();
//             System.out.println(srcOp.text);                                                    
             if (srcOp.operation == diff_match_patch.Operation.INSERT){
                 // Check LEFT
                 if (!isDelimiterOnLeft(srcOp.text)) { // If we already contain a delimiter in the left inner text, no need to eat left                 
                     // So before us only nothing, or equal (since we're always half-compressed), and the equal was already been dealt with, in it's left side
                     // Check if the left operation has a NON delimeter character, if so, need to 'eat' it
                     while (true) { // Continue to eat until no bites were successful
                         if (srcNode.hasPrevious()) {
                             String leftText = srcNode.previous().getItem().text;
                             if (!isDelimiterOnRight(leftText)) {
//                                 System.out.println("the INS [" + srcOp.text + "] needs to eat left");
                                 eatOneEqualLeftUpToDelimiter(srcNode, srcNode.previous(), diff_match_patch.Operation.DELETE);
                                 // Convert the equal on INS list to insert                                                                                       
                             } else {
                                 break;
                             }
                         } else {
                             break;
                         }
                     }
                 }
                 if (!isDelimiterOnRight(srcOp.text)) { 
                     // EAT RIGHT
                     while (true) { // Continue to eat until no bites were successful
                         if (srcNode.hasNext()) {
                             String rightText = srcNode.next().getItem().text;
                             if (!isDelimiterOnLeft(rightText)) {
//                                 System.out.println("the INS [" + srcOp.text + "] needs to eat right");
                                 eatOneEqualRightUpToDelimiter(srcNode, srcNode.next(), diff_match_patch.Operation.DELETE);
//                                 System.out.println("Done eating");                                                                                    
                             } else {
                                 break;
                             }
                         } else {
                             break;
                         }
                     }                     
                 }                 
             }    
         } // EOF all source list elements (INS)
         
//         textdiff.diff_match_patch.printAsString(sourceDiff.convertToLinkedList());
//         textdiff.diff_match_patch.printAsString(targetDiff.convertToLinkedList());
         
         beautifyByEliminatingOneChars(sourceDiff, targetDiff);                           
         
         LinkedList<Diff> result = mergeDiffLists(sourceDiff.convertToLinkedList(), targetDiff.convertToLinkedList());
         
//         textdiff.diff_match_patch.printAsString(result);
         
         return result;
    }
    
    // Beautify. If there is an EQ part of length of 1 char that is wraped in both sides in src
    // by DELS and in target by INS, merge it.
    public static void beautifyByEliminatingOneChars(DoublyLinkedList<Diff> sourceDiff, DoublyLinkedList<Diff> targetDiff){
        
        if (sourceDiff.isEmpty()) return;
        if (targetDiff.isEmpty()) return;
        
//        checkAlgorithmInvariant(sourceDiff, targetDiff);
        
        Node<Diff> delNode = sourceDiff.preNode().next();
        Node<Diff> insNode = targetDiff.preNode().next();
         
        while (true){
            if (delNode == null && insNode == null) break;
            if ((delNode == null || !delNode.hasNext()) && (insNode == null || !insNode.hasNext())) break;

            Diff delDiff = delNode.getItem();
            Diff insDiff = insNode.getItem();
                        
            // Check for different kind of operation types                 
            if (delDiff.operation == diff_match_patch.Operation.EQUAL && insDiff.operation == diff_match_patch.Operation.EQUAL) {
                if (delDiff.text.compareTo(insDiff.text) != 0) throw new RuntimeException("Equal texts are not equal!");

                if (delDiff.text.length() == 1) {
                    if (delNode.hasPrevious() && delNode.previous().getItem().operation == diff_match_patch.Operation.DELETE
                       && insNode.hasPrevious() && insNode.previous().getItem().operation == diff_match_patch.Operation.INSERT
                       && delNode.hasNext() && delNode.next().getItem().operation == diff_match_patch.Operation.DELETE
                       && insNode.hasNext() && insNode.next().getItem().operation == diff_match_patch.Operation.INSERT) {
                       // Merge the space
                       delNode.getItem().text = delNode.previous().getItem().text + delDiff.text + delNode.next().getItem().text;
                       delNode.getItem().operation = diff_match_patch.Operation.DELETE;
                       delNode.previous().remove();
                       delNode.next().remove();
                       
                       insNode.getItem().text = insNode.previous().getItem().text + insDiff.text + insNode.next().getItem().text;
                       insNode.getItem().operation = diff_match_patch.Operation.INSERT;
                       insNode.previous().remove();
                       insNode.next().remove();                                               
                    }
                }

                delNode = delNode.next(); // If there isn't more, we'll get null.
                insNode = insNode.next();                                
               
            } else if (delDiff.operation == diff_match_patch.Operation.EQUAL && insDiff.operation == diff_match_patch.Operation.INSERT){               
                // We only want to advance the ins, so it will be in sync with the DEL
                insNode = insNode.next();  // now must be EQ, or null
            } else if (delDiff.operation == diff_match_patch.Operation.DELETE && insDiff.operation == diff_match_patch.Operation.EQUAL) {                
                // We only want to advance the dels, so it will be in sync with the INS                
                delNode = delNode.next(); // now must be EQ, or null
            } else if (delDiff.operation == diff_match_patch.Operation.DELETE && insDiff.operation == diff_match_patch.Operation.INSERT) {                            
                delNode = delNode.next();
                insNode = insNode.next();  // now  both must be EQ, or one or two nulls               
            }                        
        }                
    } // beautify
    
    public static void main(String... args){
        
        String source = "This is a test of word diff";
        String target = "These are tests of diffs";
        LinkedList<Diff> linkedList = getWordBasedDiff(source, target);

        System.out.println(linkedList);
    }
}
