
/*
 * public class Node (subclass)
 * this class create b tree node, each leaf is consist of 10 node which represent each digit number
 * and it is received 4 parameters.
 * 1, digit, 2 position, 3 count, 4 original position
 * digit represent each digit number
 * position represent each position in hash bucket file and it is increased by 16 (byte of double and long)
 * count represent how many line used in each hash bucket
 * original position represent initial position of each hash bucket
 */
public class Node {

		public Node child;	// node can connect with child, and move to child node
		public Node next;	// node can move to next node
		private int digit;	// it is each digit 0 to 9
		private long position;	// position represent each position in hash bucket file and it is increased by 16 (byte of double and long)
		private int count;	//  count represent how many line used in each hash bucket
		private long originalPosition;	//  original position represent initial position of each hash bucket
		
		/*
		 * public Node(int digit, long position, int count, long originalPosition)
		 * this node make each of node in leaf node, and received digit, position, count, and original position
		 */
		public Node(int digit, long position, int count, long originalPosition){
			this.digit = digit;		// set digit
			this.position = position;	// set position
			this.count = count;	// set number of line
			this.originalPosition = originalPosition;	//set original position
		
		}
		/*
		 * getDigit()
		 * return digit
		 */
		public int getDigit(){
			return digit;
		}
		/*
		 * getPosition()
		 * return position
		 */
		public long getPosition(){
			return position;
		}
		/*
		 * getCount()
		 * return count
		 */
		public int getCount(){
			return count;
		}
		/*
		 * getOriginalPosition()
		 * return original Position
		 */
		public long getOriginalPosition(){
			return originalPosition;
		}
		/*
		 * addCount()
		 * whenever write records in binary file, number of line is increased by 1, so count is represent them
		 */
		public void addCount(){
			count++;
		}
		
		/*
		 * addByte()
		 * whenever write records in binary file, position should be increased by 16 ( byte of double and long)
		 */
		public void addByte(){
			position = position + 16;
		}
		
}
