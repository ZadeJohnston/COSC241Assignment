/* File: WordSalad.java - April 2018 */
package week09;


import java.util.Arrays;

/**
 *  Skeleton implementation of the WordSalad class.
 *
 *  @author Michael Albert
 */
public class WordSalad implements Iterable<String> {

    private WordNode first;
    private WordNode last;

    public WordSalad() {
        this.first = null;
        this.last = null;
    }

    public WordSalad(java.util.List<String> words) {
        for (String word : words) {
            addLast(word);
        }
    }

    public void add(String word) {
        if (this.first == null) {
            this.first = new WordNode(word, null);
            this.last = this.first;
            return;
        }
        WordNode newFirst = new WordNode(word, this.first);
        this.first = newFirst;
    }

    public void addLast(String word) {
        if (this.first == null) {
            add(word);
            return;
        }
        WordNode newLast = new WordNode(word, null);
        this.last.next = newLast;
        this.last = newLast;
    }

    private static class WordNode {
        private String word;
        private WordNode next;

        private WordNode(String word, WordNode next) {
            this.word = word;
            this.next = next;
        }

    }

    public java.util.Iterator<String> iterator() {
        return new java.util.Iterator<String>() {
            private WordNode current = first;

            public boolean hasNext() {
                return current != null;
            }

            public String next() {
                String result = current.word;
                current = current.next;
                return result;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public String toString() {
        StringBuilder result = new StringBuilder("[");
        WordNode node = first;
        while (node != null) {
            result.append(node.word);
            result.append(node.next == null ? "" : ", ");
            node = node.next;
        }
        return result.toString() + "]";
    }

    // Method stubs to be completed for the assignment.
    // See the assignment description for specification of their behaviour.

    public WordSalad[] distribute(int k) {
        return null;
    }

    public WordSalad[] chop(int k) {
        return null;
    }

    /**
     *
     * @param k the count required to split the current WordSalad object
     * @return a WordSalad array containing the split WordSalad
     */
    //working
    public WordSalad[] split(int k) {
        //get initial length and array size needed to store split WordSalad
        double length = count();
        int arraySize = arraySize(length, k);
        WordSalad[] split = new WordSalad[arraySize];
        WordSalad temp = new WordSalad();
        //index for entering into array
        int index = 0;
        //while there are still items in the current WordSalad
        while (length > 0) {
            //set current to first
            WordNode curr = this.first;
            //get number of words for this part of split
            int numberOfWords = (int)Math.ceil(length / k); // TODO explain length / k up top

            for (int i = 0; i < numberOfWords; i++) {
                //add the current word to the temporary WordSalad
                //then remove the node from the this WordSalad
                //and jump forward k spots to the next node
                //needed for this split
                temp.addLast(curr.word);
                remove(curr);
                curr = jumpForward(curr, k);
            }
            //update the length of this WordSalad
            length = count();
            //add the temporary WordSalad to the array
            //and create a new one for the next iteration
            split[index++] = temp;
            temp = new WordSalad();
        }
        return split;
    }

    /**
     * Helper function to remove a WordNode from
     * the WordSalad. Covers special case of first
     * being target.
     * @param target the WordNode to be removed
     */
    private void remove(WordNode target) {
        //special case of the target being the first node
        //redefine first as the next in the linked list
        if (first.equals(target)) {
            this.first = this.first.next;
            return;
        }

        WordNode curr = first;
        //move up to node before target
        while (curr.next != null && !curr.next.equals(target)) {
            curr = curr.next;
        }
        //change current's next link to target's next link
        if (curr.next == null) {
            return;
        }
        curr.next = target.next;
    }

    // need to throw exception for 0 and negative
    //working

    /**
     * A helper method to generate the size of the WordSalad
     * array needed to split a WordSalad object with count k.
     *
     * @param length the length of the WordSalad to be split
     * @param k the count to split the WordSalad with
     * @return an integer representing the size of the array
     */
    private int arraySize(double length, int k) {
        if (k <= 0) {
            //throw exception? something else?
        }
        double l = length;
        int count = 0;
        while (l > 0) {
            int b = (int)Math.ceil(l / k);
            l -= b;
            count++;
        }
        return count;
    }

    //working

    /**
     * Helper method to give the number of WordNodes
     * in a WordSalad object. Covers special case
     * of WordSalad being empty.
     * @return the size of the WordSalad object.
     */
    private int count() {
        //special case of WordSalad being empty
        if (this.first == null) {
            return 0;
        }
        WordNode current = first;
        int count = 1;
        //traverse through links, incrementing count each time
        //until we reach a null (= end of WordSalad)
        while (current.next != null) {
            current = current.next;
            count++;
        }
        return count;
    }

    //working

    /**
     * Helper method to jump through a WordSalad object k times,
     * and return the WordNode reached.
     * I.e. start at one WordNode and traverse the links for
     * k WordNodes and then return.
     * @param curr the WordNode to start traversing at
     * @param k the number of WordNodes to jump forward
     * @return the WordNode reached at the end of jumping forward
     */
    private static WordNode jumpForward(WordNode curr, int k) {
        WordNode c = curr;
        for (int i = 0; (c.next != null) && (i < k); i++) {
            c = c.next;
        }
        return c;
    }

    public static WordSalad merge(WordSalad[] blocks) {
        return null;
    }

    public static WordSalad join(WordSalad[] blocks) {
        return null;
    }

    /**
     * Method to recombine a WordSalad array into a single WordSalad
     * object with a given count of k.
     * Works backwards through the blocks array adding
     * Inverse of split().
     * @param blocks
     * @param k
     * @return the recombined WordSalad
     */
    public static WordSalad recombine(WordSalad[] blocks, int k) {
        int arraySize = blocks.length;
        WordSalad recombined = new WordSalad();
        WordNode curr = null;
        while (arraySize > 0) {
            WordSalad block = blocks[arraySize-1];
            boolean addFirst = true;
            for (String nodeString : block) {
                WordNode toAdd = new WordNode(nodeString, null);
                if (addFirst) {
                    toAdd.next = recombined.first;
                    recombined.first = toAdd;
                    curr = recombined.first;
                    addFirst = !addFirst;
                } else {
                    curr = jumpForward(curr, k-1);
                    addNext(curr, toAdd);
                    curr = toAdd;
                }
            }
            arraySize--;
        }

        return recombined;
    }

    /**
     * Helper method for recombine to link a WordNode to a specified
     * WordNode in the WordSalad.
     * @param target the WordNode to be linked from
     * @param toAdd the WordNode to be linked to the target
     */
    public static void addNext(WordNode target , WordNode toAdd) {
        toAdd.next = target.next;
        target.next = toAdd;
    }

    public static void main(String[] args) {
        WordSalad wordList = new WordSalad();
        wordList.addLast("The");
        wordList.addLast("quick");
        wordList.addLast("brown");
        wordList.addLast("fox");
        wordList.addLast("jumps");
        wordList.addLast("over");
        wordList.addLast("the");
        wordList.addLast("lazy");
        wordList.addLast("dog");

        System.out.println(wordList.toString());
        System.out.println("Length: " + wordList.count());
        int k = 7;
        System.out.println("Jump: " + wordList.jumpForward(wordList.first, k).word);
        System.out.println("SplitSize: " + wordList.arraySize(wordList.count(), k));
        int arraySize = wordList.arraySize(wordList.count(), k);
        WordSalad[] split = wordList.split(k);
        System.out.println(Arrays.deepToString(split));
        WordSalad recombine = wordList.recombine(split, k);
        System.out.println(recombine.toString());

    }

}
