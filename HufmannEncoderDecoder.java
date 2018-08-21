
/**
 * Assignment 1
 * Submitted by: 
 * Matan Chibotero. 	ID# 204076962
 * Liel Levi 	ID# 307983320
 * Yoni Amikam	ID# 311532774
 */

// Uncomment if you wish to use FileOutputStream and FileInputStream for file access.
//import java.io.FileOutputStream;
//import java.io.FileInputStream;
import base.compressor;

import java.io.*;
import java.util.BitSet;
import java.util.PriorityQueue;
import java.util.Vector;

public class HufmannEncoderDecoder implements compressor {
	class Node implements Comparable<Node> {
		char ch;
		int freq;
		boolean[] code;// hufmann code
		Node right = null;
		Node left = null;

		public Node(char _ch, int _freq) {
			ch = _ch;
			freq = _freq;
		}

		public void setcode(String s) {// set Hufmann code
			code = new boolean[s.length()];
			for (int i = 0; i < s.length(); i++)
				if (s.charAt(i) == '1')
					code[i] = true;
				else
					code[i] = false;
		}

		public Node(Node _left, Node _right) {
			left = _left;
			right = _right;
			freq = _left.freq + _right.freq;
			ch = '\0';
		}

		public int compareTo(Node node) {
			return freq - node.freq;
		}
	}

	public HufmannEncoderDecoder() {
		// TODO Auto-generated constructor stub
	}

	public byte[] CompressWithArray(String[] input_names, String[] output_names) throws IOException {
		int[] freq = new int[256];// all of ASCII code
		File file = new File(input_names[0]);
		InputStream input = new FileInputStream(file);
		File file2 = new File(output_names[0]);
		OutputStream out = new FileOutputStream(file2);
		byte[] Bytes = new byte[(int) file.length()];
		input.read(Bytes, 0, Bytes.length);// read the text and move it to the array byte
		for (int i = 0; i < Bytes.length; i++) { // the first passage on the file, to calculate the frequency
			freq[ByteToInt(Bytes[i])]++;
		}
		Node root = buildTree(freq); // create the Hufmann tree
		SetCode(root, "");
		Node[] nodes = new Node[256];
		fillNodes(root, nodes);
		String StringTree = RootToString(root);
		byte[] ByteTree = ByteFromString(StringTree);
		BitSet bits = new BitSet();
		int k = 0;// index for BitSet
		for (int i = 0; i < Bytes.length; i++) { // the second passage on the file, to encode the file
			for (int j = 0; j < nodes[ByteToInt(Bytes[i])].code.length; j++) {
				if (nodes[ByteToInt(Bytes[i])].code[j])
					bits.set(k);
				k++;
			}
		}
		bits.set(k);// we add one at the end to solve safety problem
		byte[] ByteCode = bits.toByteArray();
		byte[] ByteToSend = new byte[ByteTree.length + ByteCode.length];
		for (int i = 0; i < ByteTree.length; i++)
			ByteToSend[i] = ByteTree[i];
		for (int i = 0; i < ByteCode.length; i++)
			ByteToSend[i + ByteTree.length] = ByteCode[i];
		out.write(ByteToSend);
		out.close();
		input.close();
		return ByteToSend;
	}

	public String RootToString(Node root) {
		if (root.left == null && root.right == null) { // its a leaf in the Hufmann tree
			String str = Integer.toBinaryString(root.ch);
			while (str.length() != 8)// become to byte
				str = "0" + str;
			return "0" + str;
		}
		return "1" + RootToString(root.left) + RootToString(root.right);
	}

	public byte[] ByteFromString(String s) {
		int len;
		if (s.length() % 8 == 0)
			len = s.length() / 8;
		else
			len = s.length() / 8 + 1;
		BitSet f = new BitSet();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '1')
				f.set(i);
		}
		byte[] b = f.toByteArray();
		if (b.length != len) {// we will add zeros by the need, to make sure we don't lose information of the
								// file
			byte[] d = new byte[len];
			for (int i = 0; i < b.length; i++)
				d[i] = b[i];
			d[len - 1] = (byte) 0;
			b = d;
		}
		return b;
	}

	public void fillNodes(Node root, Node[] nodes) {
		if (root.right == null && root.left == null) {
			nodes[(int) root.ch] = root; // put in the right place(in the ascii index of the char) the right node
			return;
		}
		fillNodes(root.left, nodes);
		fillNodes(root.right, nodes);
	}

	public void SetCode(Node root, String s) {
		if (root.right == null && root.left == null) {
			root.setcode(s); // every node has his own Hufmann code for the char, here we place it
			return;
		}
		SetCode(root.right, s + '0');
		SetCode(root.left, s + '1');
	}

	public Node buildTree(int[] freq) {
		PriorityQueue<Node> pq = new PriorityQueue<Node>();// Min heap
		for (int i = 0; i < freq.length; i++) {// Create Nodes
			if (freq[i] != 0) { // choose only relevant char
				Node node = new Node((char) i, freq[i]);
				pq.add(node);
			}
		}
		while (pq.size() > 1) {// Run until we stay with a root
			Node right = pq.poll();
			Node left = pq.poll();
			pq.add(new Node(right, left));
		}

		return pq.poll();// return root
	}

	public int ByteToInt(byte b) { // change the binary numbers to only positive binary numbers
		int ret = 0;
		BitSet set = BitSet.valueOf(new byte[] { b });
		for (int i = 0; i < 8; i++) {
			if (set.get(i))
				ret = (int) (ret + Math.pow(2, i));
		}
		return ret;
	}

	@Override

	public byte[] DecompressWithArray(String[] input_names, String[] output_names) throws IOException {
		File file = new File(input_names[0]);
		InputStream finput = new FileInputStream(file);
		byte[] Bytes = new byte[(int) file.length()];
		finput.read(Bytes, 0, Bytes.length);
		BitSet bits = new BitSet();
		bits = BitSet.valueOf(Bytes);// read bits
		Node root = new Node('\0', 0);// Create root
		int[] k = new int[2];// k[0] index
		k[0] = 0;// index for the bitset
		BuildTree(root, bits, k); // to recreate the Hufmann tree
		int size = k[0] % 8;
		for (int i = 0; i < 8 - size; i++)// complete to byte
			k[0]++;
		Vector<Byte> b = TreeToByte(bits, root, k); // to create vector of bytes, that this vector we use to the output
													// file
		File file2 = new File(output_names[0]);
		OutputStream out = new FileOutputStream(file2);
		byte[] ByteToSend = new byte[b.size()];
		for (int i = 0; i < b.size(); i++)
			ByteToSend[i] = b.get(i);
		out.write(ByteToSend);
		out.close();
		finput.close();
		return ByteToSend;
	}

	public Vector<Byte> TreeToByte(BitSet ls, Node root, int[] k) {
		Node p = root;
		Vector<Byte> lb = new Vector<Byte>();
		while (k[0] - 1 < ls.length() - 1) { // here we remove the last bit we add before because of the safety problem
			if (p.left == null && p.right == null) {
				lb.add((byte) p.ch);// Add the deciphered byte
				p = root;
			} else if (ls.get(k[0])) {
				p = p.left;
				k[0]++;
			} else {
				p = p.right;
				k[0]++;
			}
		}
		return lb;
	}

	public void BuildTree(Node root, BitSet bits, int[] k) {
		if (!bits.get(k[0])) {// zero
			k[0]++;
			int ret = 0, j = 7;
			for (int i = 0; i < 8; i++) {// calculate the char ASCII
				if (bits.get(k[0]))
					ret = (int) (ret + Math.pow(2, j));
				j--;
				k[0]++;
			}
			root.ch = (char) ret;
			return;
		}
		if (bits.get(k[0]))
			k[0]++;
		root.left = new Node('\0', 0);
		root.right = new Node('\0', 0);
		BuildTree(root.left, bits, k);
		BuildTree(root.right, bits, k);
	}

	@Override
	public void Compress(String[] input_names, String[] output_names) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void Decompress(String[] input_names, String[] output_names) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

	}
}