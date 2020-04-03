package kr.co.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import kr.co.domain.Node;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MenuServiceImpl implements MenuService {

	public Map<Integer, Node> createNodeMap() {
		Map<Integer, Node> nodeMap = new TreeMap<Integer, Node>();
		nodeMap.put(0, new Node(0, 0, ""));
		return nodeMap;
	}

	public void createNode(Map<Integer, Node> nodeMap, Map<Integer, Integer> noMap, String line) {
		String[] splitByTab = line.split("\t");
		String myNo = splitByTab[0];
		String parentNo = splitByTab[1];
		String data = splitByTab[2];
		nodeMap.put(Integer.parseInt(myNo), new Node(Integer.parseInt(myNo), Integer.parseInt(parentNo), data));
		noMap.put(Integer.parseInt(myNo), Integer.parseInt(parentNo));
	}

	public void readFile(Map<Integer, Node> nodeMap, Map<Integer, Integer> noMap) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("C:/menu/menu.txt"));
			String line = "";
			while ((line = br.readLine()) != null) {
				createNode(nodeMap, noMap, line);
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void add(Node parent, Node child) {
		if (parent.getLeftChild() == null) {
			parent.setLeftChild(child);
		} else {
			Node temp = parent.getLeftChild();
			while (temp.getRightSibling() != null) {
				temp = temp.getRightSibling();
			}
			temp.setRightSibling(child);
		}
	}

	public void connect(Map<Integer, Node> nodeMap, Map<Integer, Integer> noMap) {
		for (Map.Entry<Integer, Integer> entry : noMap.entrySet()) {
			Integer myNo = entry.getKey();
			Integer parentNo = entry.getValue();
			add(nodeMap.get(parentNo), nodeMap.get(myNo));
		}
	}

	public void printTree1(Node node, int depth) {
		for (int i = 1; i < depth; i++) {
			System.out.print("\t");
		}
		System.out.println(node.getData());
		if (node.getLeftChild() != null) {
			printTree1(node.getLeftChild(), depth + 1);
		}
		if (node.getRightSibling() != null) {
			printTree1(node.getRightSibling(), depth);
		}
	}

	public void printTree2(Node node, int level, Map<Integer, Node> nodeMap) {
		while (node != null) {
			while (node.getLeftChild() != null) {
				System.out.println(node.getData());
				node = node.getLeftChild();
			}
			while (node.getRightSibling() != null) {
				System.out.print("\t");
				System.out.println(node.getData());
				node = node.getRightSibling();
			}
			System.out.print("\t");
			System.out.println(node.getData());
			node = nodeMap.get(node.getParentNo()).getRightSibling();
			continue;
		}
	}

	public List<Integer> printLevel(Node node, int level, Map<Integer, List<Integer>> levelMap) {
		int depth = 0;
		Node tempChild = node;
		Node tempParent = node;
		List<Integer> levelList = new ArrayList<Integer>();
		while (depth <= level) {
			if (depth == level) {
				while (tempChild != null) {
					levelList.add(tempChild.getMyNo());
					tempChild = tempChild.getRightSibling();
				}
				if (tempParent.getRightSibling() != null) {
					tempParent = tempParent.getRightSibling();
					tempChild = tempParent.getLeftChild();
				} else
					break;
			} else {
				tempParent = tempChild;
				tempChild = tempChild.getLeftChild();
				depth++;
			}
		}
		return levelList;
	}

	public Map<String, Object> tree() {
		Map<Integer, Node> nodeMap = createNodeMap();
		Map<Integer, Integer> noMap = new TreeMap<Integer, Integer>();
		readFile(nodeMap, noMap);
		connect(nodeMap, noMap);
//		System.out.println("---print1---");
//		printTree1(nodeMap.get(0), 0);
//		System.out.println("---print1---");
//		System.out.println("---print2---");
//		printTree2(nodeMap.get(0), 0, nodeMap);
//		System.out.println("---print2---");
		Map<Integer, List<Integer>> levelMap = new TreeMap<Integer, List<Integer>>();
		for (int i = 0; i < 3; i++) {
			List<Integer> levelList = printLevel(nodeMap.get(0), i, levelMap);
			levelMap.put(i, levelList);
		}
		System.out.println(levelMap);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nodeMap", nodeMap);
		map.put("noMap", noMap);
		map.put("levelMap", levelMap);
		return map;
	}

}
