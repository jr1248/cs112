package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode curr=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		curr = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		curr = new TagNode("html", null, null);
        TagNode point = null;
        Stack<TagNode> tree = new Stack<TagNode>();
        while (sc.hasNextLine()) {
            String treeTag = sc.nextLine();
            if (treeTag.equals("<html>")) {
                tree.push(curr);
            }
            else if (treeTag.charAt(0) == '<') {
                if (treeTag.charAt(1) == '/') {
                    tree.pop();
                    continue;
                }
                else if (tree.peek().firstChild == null) {
                    String temp = treeTag.replaceAll("<", "");
                    temp = temp.replaceAll(">", "");
                    point = new TagNode(temp, null, null);
                    tree.peek().firstChild = point;
                    tree.push(point);
                }
                else {
                    TagNode temp = tree.peek().firstChild;
                    while (temp.sibling != null) {
                        temp = temp.sibling;
                    }
                    String a = treeTag.replaceAll("<", "");
                    a = a.replaceAll(">", "");
                    point = new TagNode(a, null, null);
                    temp.sibling = point;
                    tree.push(point);
                }
            }
            else {
                if (tree.peek().firstChild == null) {
                    tree.peek().firstChild = new TagNode(treeTag, null, null);
                }
                else {
                    TagNode temp = tree.peek().firstChild;
                    while (temp.sibling != null) {
                        temp = temp.sibling;
                    }
                    temp.sibling = new TagNode(treeTag, null, null);
                }
            }       
        }           

    }

	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	private static void replaceTag(TagNode root, String oldTag, String newTag) {		  ;	  
	if(root == null) {
		return;
	}
	if(root.tag.equals(oldTag) && root.firstChild != null) {
		root.tag = newTag;
	}
	replaceTag(root.sibling, oldTag, newTag);
	replaceTag(root.firstChild, oldTag, newTag);
}
	public void replaceTag(String oldTag, String newTag) {
		replaceTag(curr, oldTag, newTag);
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	private static void boldCol(TagNode col){
		   if(col.sibling != null){
		       col=col.sibling;
		       TagNode bolden=new TagNode("b", col.firstChild, null);
		       col.firstChild=bolden;
		       boldCol(col);  
		   }
		   else {
		       return;
		   }
		}
	private void boldRowAssist(int row,TagNode root) {
		Stack<TagNode> bold=new Stack<TagNode>();
		int count=1;
		if(root==null) {
			return;
		}
		while(root!=null || !bold.isEmpty()) {
			if(root!= null) {
				if(root.tag.equals("table")) {
					TagNode temp=root.firstChild;
	                   while(count != row){
	                       temp=temp.sibling;
	                       count++;
	                   }
	                   TagNode ptr=temp.firstChild;
	                   TagNode boldFace=new TagNode("b", ptr.firstChild, null);
	                   ptr.firstChild=boldFace;
	                   boldCol(ptr);
				}
				 bold.push(root);
		         root=root.firstChild;
			}
			else{
				root=bold.pop();
		        root=root.sibling;
			}
		}
	}
	public void boldRow(int row) {
		boldRowAssist(row,curr);
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	private void removeTagAssist(TagNode root, String tag) {
		if(root==null){
			return;
		}
		if ((root.firstChild != null) && (root.firstChild.firstChild != null) && root.firstChild.tag.equals(tag)){ 
			if (root.firstChild.firstChild.sibling != null) {
				TagNode curr = root.firstChild.firstChild.sibling;
				while (curr.sibling != null) {
					curr = curr.sibling;
				}
				curr.sibling = root.firstChild.sibling;
				TagNode temp = root.firstChild.firstChild;
				root.firstChild = temp;
			}
			else {
				TagNode temp = root.firstChild.firstChild;
				temp.sibling = root.firstChild.sibling;
				root.firstChild = temp;
			}			
		removeTagAssist(root, tag);
		}
		if ((root.sibling != null) && root.sibling.tag.equals(tag)) { 
			if (root.sibling.firstChild.sibling != null){
				TagNode curr = root.sibling.firstChild.sibling;
				while (curr.sibling != null) {
					curr = curr.sibling;
				}
				curr.sibling = root.sibling.sibling;
				TagNode temp = root.sibling.firstChild;
				root.sibling = temp;
			}
			else {
				TagNode temp = root.sibling.firstChild;
				temp.sibling = root.sibling.sibling;
				root.sibling = temp;
			}
			removeTagAssist(root,tag);
		}
	else {
		removeTagAssist(root.firstChild,tag);
		removeTagAssist(root.sibling,tag);
	}
	}

	private void removeTagAssistOlUl(TagNode root, String tag){
		if(root == null) return;
		if(root.tag.equals(tag) && root.firstChild != null) {
			root.tag = "p";
			TagNode ptr = null;
			for(ptr = root.firstChild; ptr.sibling != null; ptr = ptr.sibling) ptr.tag = "p"; 
			ptr.tag = "p";
			ptr.sibling = root.sibling;
			root.sibling = root.firstChild.sibling;
			root.firstChild = root.firstChild.firstChild;
		}
		removeTagAssistOlUl(root.firstChild, tag); 
		removeTagAssistOlUl(root.sibling, tag);
	}
	
	public void removeTag(String tag) {
		if(tag.equals("b") || tag.equals("em") || tag.equals("p")) {
			removeTagAssist(curr,tag);
		}
		if(tag.equals("ol") || tag.equals("ul")) {
			removeTagAssistOlUl(curr,tag);
		}
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	private static boolean same(String word, String target){
		   word = word.toLowerCase();
		   target = target.toLowerCase();
		   Character punctionMark=word.charAt(word.length()-1);
		   if(punctionMark=='?' || punctionMark=='!' || punctionMark==':' || punctionMark==';' || punctionMark=='.') {
		       word=word.replace(word.substring(word.length()-1, word.length()-1), "");
		   }
		   if(target.equals(word)) {
		       return true;
		   }
		   if(target.equals(word.substring(0, word.length()-1))) {
		       return true;
		   }
		   else {
		       return false;
		   }
		}
private void addTagAssist(TagNode ptr, TagNode curr, String word, String tag){
   word=word.toLowerCase(); 
   if(curr==null)
       return; 
   if(ptr != null && ptr.tag.equals(tag) && !curr.tag.toLowerCase().contains(word)) 
	   return;
       if(curr.tag.toLowerCase().contains(word)){   
           String[] split=curr.tag.split(" ");
           int count=0;
           for(int i=0; i<split.length; i++){
               if(same(split[i], word)){
                   count++;  
               }
           }
           if(split.length==1){
               if(same(split[0], word)){
                   if(ptr.firstChild==curr && curr.sibling==null){
                       TagNode temp=new TagNode(tag, curr, null);
                       ptr.firstChild=temp;
                   }
                   else if(ptr.firstChild==curr && curr.sibling != null){
                       TagNode temp=new TagNode(tag, curr, curr.sibling);
                       ptr.firstChild=temp;
                       curr.sibling=null;
                   }
               }
           }
           else if(split.length==2){
               TagNode temp=new TagNode(null, null, null);
               if(same(split[0], word)){
                   String found=split[0];
                   if(ptr.firstChild==curr){
                	   temp.tag=found;                 
                	   TagNode next=new TagNode(split[1], null, null);
                	   TagNode tags=new TagNode(tag, temp, next);
                	   ptr.firstChild=tags;
                	   next.sibling=curr.sibling;
                	   temp.sibling=null;
                	   addTagAssist(tags, tags.sibling, word, tag);
                   }
                   else{
                       temp.tag=found;
                       TagNode next=new TagNode(split[1], null, null);
                       TagNode tags=new TagNode(tag, temp, next);
                       ptr.sibling=tags;
                       next.sibling=curr.sibling;
                   }
               }
               else{
                   String found=split[1];
                   if(ptr.firstChild==curr){
                	   temp.tag=found;
                	   TagNode prev=new TagNode(split[0], null, null);
                	   ptr.firstChild=prev;
                	   TagNode tags=new TagNode(tag, temp, null);
                	   prev.sibling=tags;
                	   tags.sibling=curr.sibling;
                	   temp.sibling=null;
                   }
                   else{
                	   temp.tag=found;
                	   TagNode prev=new TagNode(split[0], null, null);
                	   ptr.sibling=prev;
                	   TagNode tags=new TagNode(tag, temp, null);
                	   prev.sibling=tags;
                	   tags.sibling=curr.sibling;
                	   temp.sibling=null;
                   }                  
               }                            
           }          
           else if(split.length>2 && ptr.firstChild==curr){
        	   String prev="";
        	   String found="";
        	   String next="";
        	   int equal=0;
        	   TagNode prevNode=new TagNode(null, null, null);
        	   TagNode foundNode=new TagNode(null, null, null);
        	   TagNode nextNode=new TagNode(null, null, null);
        	   for(int i=0; i<split.length; i++){
        		   if(same(split[i], word)){
        			   equal=i;
        			   found=split[i];
        			   foundNode.tag=found;
        			   break;
        		   }
        	   }
        	   for(int i=0; i<split.length; i++){
        		   if(i<equal){
        			   prev+=split[i] + " ";
        		   }
        		   if(i>equal) {
        			   next+=split[i] + " ";
        		   }
        	   }
        	   prevNode.tag=prev;
        	   nextNode.tag=next;          
           if(same(split[0], word)){
               TagNode tags=new TagNode(tag, foundNode, nextNode);
               ptr.firstChild=tags;
               nextNode.sibling=curr.sibling;
               foundNode.sibling=null;              
               addTagAssist(tags, tags.sibling, word, tag);
           }
           else if(same(split[split.length-1], word) && count==1){
               TagNode tags=new TagNode(tag, foundNode, curr.sibling);
               prevNode.sibling=tags;
               ptr.firstChild=prevNode;
               foundNode.sibling=null;
           }          
           else{         
        	   TagNode tags=new TagNode(tag, foundNode, nextNode);
        	   prevNode.sibling=tags;
        	   ptr.firstChild=prevNode;
        	   foundNode.sibling=null;
        	   nextNode.sibling=curr.sibling;
        	   addTagAssist(tags, tags.sibling, word, tag);
           	}
           }
           else if(split.length>2 && ptr.sibling==curr){              
               String prev="";
               String found="";
               String next="";
               int equal=0;
               TagNode prevNode=new TagNode(null, null, null);
               TagNode foundNode=new TagNode(null, null, null);
               TagNode nextNode=new TagNode(null, null, null);
               for(int i=0; i<split.length; i++){
                   if(same(split[i], word)){                     
                       equal=i;
                       found=split[i];
                       foundNode.tag=found;
                       break;
                   }
               }
               for(int i=0; i<split.length; i++){
                   if(i<equal){
                       prev+=split[i] + " ";
                   }
                   if(i>equal) {
                       next+=split[i] + " ";
                   }
               }
               prevNode.tag=prev;
               nextNode.tag=next;
               if(same(split[0], word)){
                   TagNode tags=new TagNode(tag, foundNode, nextNode);
                   nextNode.sibling=curr.sibling;
                   ptr.sibling=tags;
                   foundNode.sibling=null;
                   addTagAssist(tags, tags.sibling, word, tag);
               }
               else if(same(split[split.length-1], word) && count==1){
                   TagNode tags=new TagNode(tag, foundNode, curr.sibling);
                   prevNode.sibling=tags;
                   ptr.sibling=prevNode;
                   foundNode.sibling=null;
               }              
               else{
                   TagNode tags=new TagNode(tag, foundNode, nextNode);
                   ptr.sibling=prevNode;
                   prevNode.sibling=tags;
                   nextNode.sibling=curr.sibling;
                   foundNode.sibling=null;
                   if(count >= 2) {
                       addTagAssist(tags, tags.sibling, word, tag);
                   }
               }      
           }      
       }      
       addTagAssist(curr, curr.firstChild, word, tag);
       addTagAssist(curr, curr.sibling, word, tag);
   }		
	public void addTag(String word, String tag) {
		 if(tag.equals("em") || tag.equals("b")) {  
		       addTagAssist(null, curr, word, tag);
		 }
	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(curr, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(curr, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.curr) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
