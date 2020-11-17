import java.util.List;

public class function_rbt {

	public building_record_rbt leftRotate(building_record_rbt x, building_record_rbt Root) {
		building_record_rbt y = x.right;
		x.right = y.left;
		if (y.left != null)
			y.left.parent = x;
		y.parent = x.parent;

		if (x.parent == null) {
			Root = y;
		} else {
			if (x.parent.left == x)
				x.parent.left = y; // if x is the left child of it's parent, then set y as the left child of x's
									// parent
			else
				x.parent.right = y; // set y as the right child of x's parent
		}
		y.left = x;
		x.parent = y;
		return Root;
	}

	private building_record_rbt rightRotate(building_record_rbt y, building_record_rbt Root) {
		// set x as the the left child of the current node
		building_record_rbt x = y.left;
		// set the right child of x as the left child of y
		y.left = x.right;
		if (x.right != null) // set y as the parent of the right child of x
			x.right.parent = y;
		// set the parent of y as the parent of x
		x.parent = y.parent;
		if (y.parent == null) {
			Root = x; // set x=null if the parent of y is null
		} else {
			if (y == y.parent.right)
				y.parent.right = x; // set x as the right child of the parent of y if y is the right child of it's
									// parent
			else
				y.parent.left = x; // set x as the left child of it's parent
		}
		// set y as the right child of x
		x.right = y;
		// set x as the parent of y
		y.parent = x;
		return Root;
	}

	public building_record_rbt insert(building_record_rbt node, building_record_rbt Root) {
		building_record_rbt y = null;
		building_record_rbt x = Root;
		// 1.add node in a binary search tree way
		while (x != null) {
			y = x;
			if (node.buildingNum < x.buildingNum)
				x = x.left;
			else
				x = x.right;
		}

		node.parent = y;
		if (y != null) {
			if (node.buildingNum < y.buildingNum)
				y.left = node;
			else
				y.right = node;
		} else {
			Root = node;
		}
		// 2. set the color of node red
		node.colorisred = true;
		// 3. fix the tree, make it a red black tree
		Root = insertFixUp(node, Root);
		return Root;
	}

	public int[] lookup(int bn, building_record_rbt Root) {// look up a particular node in the red black tree
		int[] ans = new int[4];
		for (int i = 0; i < 2; i++)
			ans[i] = 0;
		building_record_rbt temp = Root;
		while (temp != null)
			if (bn < temp.buildingNum)
				temp = temp.left;
			else if (bn > temp.buildingNum)
				temp = temp.right;
			else if (bn == temp.buildingNum) {
				ans[0] = temp.buildingNum;
				ans[1] = temp.executed_time;
				ans[2] = temp.total_time;
				return ans;
			}
		if (temp == null)
			ans[0] = -1;
		return ans;
	}

	public void find_section(int bn_start, int bn_end, building_record_rbt br, List<building_record_rbt> result) {
		building_record_rbt tmp = br;
		if (tmp != null) {
			if (tmp.buildingNum == bn_start) {// find the start node of the section recursively
				result.add(tmp);
			} else if (tmp.buildingNum < bn_start) {
				tmp = tmp.right;
				find_section(bn_start, bn_end, tmp, result);
			} else if (tmp.buildingNum > bn_start && tmp.buildingNum < bn_end) {// add the node to list recursively
				result.add(tmp);
				find_section(bn_start, bn_end, tmp.left, result);
				find_section(bn_start, bn_end, tmp.right, result);
			} else if (tmp.buildingNum == bn_end)
				result.add(tmp);

		}
	}

	public building_record_rbt search(int bn, building_record_rbt Root) {
		building_record_rbt temp = Root;
		while (temp != null) {
			if (bn < temp.buildingNum)
				temp = temp.left;
			else if (bn > temp.buildingNum)
				temp = temp.right;
			else if (bn == temp.buildingNum)
				break;
		}
		return temp;
	}

	private building_record_rbt insertFixUp(building_record_rbt node, building_record_rbt Root) {
		building_record_rbt parent, gparent;
		// if the parent node is not null and is red
		while (((parent = node.parent) != null) && parent.colorisred) {
			gparent = parent.parent;
			// if the parent node is the left child of it's parent
			if (parent == gparent.left) {
				// Case 1: the grandparent node's right child node(uncle node) is red
				building_record_rbt uncle = gparent.right;
				if ((uncle != null) && uncle.colorisred) {
					uncle.colorisred = false;
					parent.colorisred = false;
					gparent.colorisred = true;
					node = gparent;
					continue;
				}
				// Case 2: the uncle node is black and current node is the right child of it's
				// parent
				if (parent.right == node) {
					building_record_rbt tmp;
					Root = leftRotate(parent, Root);
					tmp = parent;
					parent = node;
					node = tmp;
				}
				// Case 3: the uncle node is black and current node is the left child of it's
				// parent
				parent.colorisred = false;
				gparent.colorisred = true;
				Root = rightRotate(gparent, Root);
			} else { // if the parent node is the right child of it's parent
				// Case 1: the uncle node is red
				building_record_rbt uncle = gparent.left;
				if ((uncle != null) && uncle.colorisred) {
					uncle.colorisred = false;
					parent.colorisred = false;
					gparent.colorisred = true;
					node = gparent;
					continue;
				}
				// Case 2: the uncle node is black and current node is the left child of it's
				// parent
				if (parent.left == node) {
					building_record_rbt tmp;
					Root = rightRotate(parent, Root);
					tmp = parent;
					parent = node;
					node = tmp;
				}
				// Case 3: the uncle node is black and current node is the right child of it's
				// parent
				parent.colorisred = false;
				gparent.colorisred = true;
				Root = leftRotate(gparent, Root);
			}
		}
		// set the root black
		Root.colorisred = false;
		return Root;
	}

	public building_record_rbt remove(building_record_rbt node, building_record_rbt Root) {
		building_record_rbt child, parent;
		boolean color;
		// if both of the child node of the deleting node is not null
		if ((node.left != null) && (node.right != null)) {
			// make the right child node of the deleting node as a replace node, then delete
			// the deleting node
			building_record_rbt replace = node;
			// get next replace node
			replace = replace.right;
			while (replace.left != null)
				replace = replace.left;
			// if node is not the root
			if (node.parent != null) {
				if (node.parent.left == node)
					node.parent.left = replace;
				else
					node.parent.right = replace;
			} else { // update the root
				Root = replace;
			}

			// child is the right child of the replace node
			child = replace.right;
			parent = replace.parent;
			// save the color of the replace node
			color = replace.colorisred;

			// if the deleting node is the parent of it's replace node
			if (parent == node) {
				parent = replace;
			} else {
				if (child != null)
					child.parent = parent;
				parent.left = child;
				replace.right = node.right;
				node.right.parent = replace;
			}
			replace.parent = node.parent;
			replace.colorisred = node.colorisred;
			replace.left = node.left;
			node.left.parent = replace;
			if (color == false)
				Root = removeFixUp(child, parent, Root);
			node = null;
			return Root;
		}
		if (node.left != null) {
			child = node.left;
		} else {
			child = node.right;
		}
		parent = node.parent;
		color = node.colorisred;
		if (child != null)
			child.parent = parent;
		// if node is not the root
		if (parent != null) {
			if (parent.left == node)
				parent.left = child;
			else
				parent.right = child;
		} else {
			Root = child;
		}

		if (color == false)
			Root = removeFixUp(child, parent, Root);
		node = null;
		return Root;
	}

	private building_record_rbt removeFixUp(building_record_rbt node, building_record_rbt parent,
			building_record_rbt Root) {
		building_record_rbt other;

		while ((node == null || node.colorisred == false) && (node != Root)) {
			if (parent.left == node) {
				other = parent.right;
				if (other.colorisred) {
					// Case 1: another child(other) of the parent is red
					other.colorisred = false;
					parent.colorisred = true;
					Root = leftRotate(parent, Root);
					other = parent.right;
				}

				if ((other.left == null || other.left.colorisred == false)
						&& (other.right == null || other.right.colorisred == false)) {
					// Case 2: other is black, and all of the children of other is black
					other.colorisred = true;
					node = parent;
					parent = node.parent;
				} else {

					if (other.right == null || other.right.colorisred == false) {
						// Case 3: other is black, the left child of other is red, the right child of
						// other is black
						other.left.colorisred = false;
						other.colorisred = true;
						Root = rightRotate(other, Root);
						other = parent.right;
					}
					// Case 4: other is black, and the right child of other is red
					other.colorisred = parent.colorisred;
					parent.colorisred = false;
					other.right.colorisred = false;
					Root = leftRotate(parent, Root);
					node = Root;
					break;
				}
			} else {
				other = parent.left;
				if (other.colorisred) {
					// Case 1: another child(other) of the parent is red
					other.colorisred = false;
					parent.colorisred = true;
					Root = rightRotate(parent, Root);
					other = parent.left;
				}
				if ((other.left == null || other.left.colorisred == false)
						&& (other.right == null || other.right.colorisred == false)) {
					// Case 2: other is black, and all of the children of other is black
					other.colorisred = true;
					node = parent;
					parent = node.parent;
				} else {

					if (other.left == null || other.left.colorisred == false) {
						// Case 3: other is black, the left child of other is red, the right child of
						// other is black
						other.right.colorisred = false;
						other.colorisred = true;
						Root = leftRotate(other, Root);
						other = parent.left;
					}
					// Case 4: other is black, and the right child of other is red
					other.colorisred = parent.colorisred;
					parent.colorisred = false;
					other.left.colorisred = false;
					Root = rightRotate(parent, Root);
					node = Root;
					break;
				}
			}
		}
		if (node != null)
			node.colorisred = false;
		return Root;
	}

}
