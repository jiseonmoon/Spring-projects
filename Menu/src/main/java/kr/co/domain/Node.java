package kr.co.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Node {

	private int myNo;
	private int parentNo;
	private String data;
	private Node leftChild;
	private Node rightSibling;

	public Node(int myNo, int parentNo, String data) {
		this.myNo = myNo;
		this.parentNo = parentNo;
		this.data = data;
	}

}
