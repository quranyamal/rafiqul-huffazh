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

public class DoublyLinkedList<Item> {
    final private Node<Item> pre;     // sentinel before first item
    final private Node<Item> post;    // sentinel after last item

    public DoublyLinkedList() {
        pre       = new Node<Item>(this);
        post      = new Node<Item>(this);
        pre.next  = post;
        post.prev = pre;
    }

    public Node<Item> firstNode(){
        if (pre.next == post) return null;
        return pre.next;
    }
    
    public Node<Item> preNode(){
        return pre;
    }
    // linked list node helper data type
    // If static, it can be instansiated outside standalone mode
    public static class Node<NodeItem> {
        private NodeItem item;
        private Node<NodeItem> next;
        private Node<NodeItem> prev;
        private DoublyLinkedList list;
        
        public Node(DoublyLinkedList parentList){
            this.list = parentList;
        }
        
        public boolean hasPrevious()  { return prev != list.pre; }
        public boolean hasNext()      { 
            return next != list.post; 
        }
        
        public NodeItem getItem(){
            return item;
        }
        
        // If there isn't more, we'll get null
        public Node<NodeItem> next()            { 
            if (!hasNext()) return null;
            return next;
        }
        
        public Node<NodeItem> previous()            { 
            if (!hasPrevious()) return null;
            return prev;
        }
        
        public void remove() {             
            Node<NodeItem> x = prev;
            Node<NodeItem> y = next;
            x.next = y;
            y.prev = x;            
            this.prev = null;
            this.next = null;
            this.list = null;
        }
        
        public Node<NodeItem> injectAfter(NodeItem item) {            
            Node x = new Node(this.list);
            x.item = item;
            x.next = next;
            x.prev = this;
            next.prev = x;
            this.next = x;
            return x;
        }
        
        public Node<NodeItem> injectBefore(NodeItem item) {            
            Node x = new Node(this.list);
            
            x.item = item;
            x.next = this;
            x.prev = prev;
            
            prev.next = x;
            this.prev = x;            
            return x;
        }
    } // EOF Node

    public boolean isEmpty()    { return pre.next == post; }

    // addAtTheEnd the item to the list
    public Node<Item> addAtTheEnd(Item item) {
        Node last = post.prev;
        Node x = new Node(this);
        x.item = item;
        x.next = post;
        x.prev = last;        
        post.prev = x;
        last.next = x;
        return x;
    }
    
    public LinkedList<Item> convertToLinkedList(){
        LinkedList<Item> result = new LinkedList<Item>();
        
        Node<Item> srcNode = this.preNode();
        while (srcNode.hasNext()) {
            srcNode = srcNode.next();
            result.add(srcNode.getItem());
        }
        
        return result;
    }
}
