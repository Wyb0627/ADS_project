public class function1 {
	function_rbt frbt = new function_rbt();

	int returnparent(int l) {
		int p = (int) (Math.ceil((float) l / 2) - 1);
		return p;
	}

	int rlength(buliding_record[] b) {// return the length of the min heap
		int i = 0;
		while (b[i] != null)
			i++;
		return i;
	}

	buliding_record[] move(buliding_record[] a, int l) { // check and move the last leaf node bottom-up
															// until it's executed_time> it's parent node's
															// executed_time
		int p = returnparent(l); // find parent of a node
		buliding_record[] ans = a;
		while (ans[l].executed_time <= ans[p].executed_time && l != 0) {// while the node is not the root node and
																		// it's executed_time<= it's parent node's
																		// executed_time
			if (ans[l].executed_time < ans[p].executed_time) {// if it's executed_time<= it's parent node's
																// executed_time
				buliding_record t = ans[l]; // do the exchange
				ans[l] = ans[p];
				ans[p] = t;
				l = p;
				p = returnparent(l);
			} else if (p >= 0 && ans[l].executed_time == ans[p].executed_time
					&& ans[l].buildingNum < ans[p].buildingNum) {// if it's executed_time=it's parent node's
																	// executed_time
				buliding_record t = ans[l]; // and it's building number < it's parent's building number
				ans[l] = ans[p]; // do the exchange
				ans[p] = t;
				l = p;
				p = returnparent(l);
			} else // finished, break
				break;
			if (p < 0)// reach the top, break
				break;
		}
		return ans; // return the min heap after the modification
	}

	int check(buliding_record[] a, buliding_record b) {// to check whether the building number already exist
		for (int i = 0; i < rlength(a) - 1; i++) {
			if (a[i].buildingNum == b.buildingNum)
				return 0;
		}
		return 1;
	}

	public buliding_record[] insertarray(buliding_record[] a, buliding_record b) {
		int l = rlength(a);
		buliding_record[] ans;
		if (check(a, b) == 1) {// to check whether the building number already exist
			a[l] = b;
			if (l != 0)
				ans = move(a, l);
			else
				ans = a;
			return ans;
		} else
			return a;
	}

	public void movetd(buliding_record[] br, int s) {// check and move the last leaf node bottom-up
														// until it's executed_time> it's parent node's executed_time
		int n = 2 * s + 1; // find the left son node
		while (br[s].executed_time >= br[n].executed_time) {// while the node is not the root node and
			// it's executed_time>= it's son node's executed_time
			if (br[s].executed_time > br[n].executed_time) {// while the node is not the root node and
				// it's executed_time> it's son node's executed_time
				buliding_record tmp = br[s]; // do the exchange
				br[s] = br[n];
				br[n] = tmp;
				s = n;
				n = 2 * s + 1;
			} else if (br[s].executed_time == br[n].executed_time && br[s].buildingNum > br[n].buildingNum) {
				buliding_record tmp = br[s]; // it's executed_time= it's son node's executed_time
				br[s] = br[n]; // and it's building number < it's son's building number
				br[n] = tmp;
				s = n;
				n = 2 * s + 1;
			} else
				break;
			if (br[n] == null) // if the node is the leaf node
				break;
		}
	}

	public buliding_record[] del_top(buliding_record[] br) {
		int len = rlength(br);
		if (len - 1 > 0) {
			br[0] = br[len - 1]; // delete the root node of the min heap
			br[len - 1] = null; // by moving the last node to overwrite the root node
			len--;
			if (br[1] != null) // if the structure is not empty, heapify
				movetd(br, 0);
		} else
			br[0] = null;
		return br;
	}

}