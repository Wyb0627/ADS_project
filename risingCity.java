import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class risingCity {
	static function1 f1 = new function1(); // function related to the min heap
	static function_rbt frbt = new function_rbt();// function related to the red black tree
	public static int counter; // global time counter
	public static int count; // building time counter, make sure building is worked on until complete or for
								// 5 days
	static building_record_rbt Root = null; // root of the red black tree

	public static int[] work(buliding_record[] br, int[] t) { // Constructing
		if (br[0].executed_time < br[0].total_time - 1) {// building would not finish after this construction
			br[0].executed_time++; // update executed_time
			count++; // update building counter
			br[0].rbt.executed_time = br[0].executed_time; // update executed_time of the node stored in red black tree
			t[3] = br[0].executed_time; // update condition array
			if (count == 5) { // if the building was constructed 5 consecutive day
				buliding_record brt = br[0]; // do the heapify
				br = f1.del_top(br); // delete the top of the min heap and put it in the last leaf node
				br[f1.rlength(br)] = brt;
				if (f1.rlength(br) - 1 > 0) // if the building isn't the only one remain in the data structure
					br = f1.move(br, f1.rlength(br) - 1); // check and move the last leaf node bottom-up
				count = 0; // reset the building counter to 0
			}
		} else if (br[0].executed_time == br[0].total_time - 1) {// building would finish after this construction
			t[0] = br[0].buildingNum; // update condition array
			t[2] = br[0].total_time;
			t[3] = t[2];
			building_record_rbt rbt = br[0].rbt; // remove this building and do the heapify
			br = f1.del_top(br);
			Root = frbt.remove(rbt, Root);
			count = 0; // reset the building counter to 0
		}
		if (br[0] == null) // if all the building in data structure has been finished
			t[4] = 1;
		t[1] = count;
		return t;
	}

	static void addet(buliding_record[] br) {// do the construction
		br[0].executed_time++;
		br[0].rbt.executed_time++;
		if (count == 5) { // heapify
			buliding_record brt = br[0];
			br = f1.del_top(br);
			br[f1.rlength(br)] = brt;
			if (f1.rlength(br) - 1 > 0)
				br = f1.move(br, f1.rlength(br) - 1);
			count = 0;
		}
		if (br[0].executed_time == br[0].total_time) {
			building_record_rbt t = br[0].rbt;
			br = f1.del_top(br);
			Root = frbt.remove(t, Root);
		}
	}

	static void output(int[] ans, BufferedWriter out) throws IOException {
		counter++;
		if (ans[0] != -1)
			out.write("(" + ans[0] + "," + counter + ")\n");
	}

	static void process(String line, buliding_record[] br, BufferedWriter out) throws IOException {
		line = line.trim();
		int p = 0;
		String strc = "";
		int[] t = new int[5]; // An array contains building informations and conditions
		while (line.charAt(p) != ':') { // Read instruction time
			strc += line.charAt(p);
			p++;
		}
		int counter2 = Integer.parseInt(strc);
		String str1 = "";
		String str2 = "";
		while (counter < counter2) { // Comparing instruction time with global time counter
			if (br[0] != null) { // while instruction time < global time,
									// keep working until instruction time >= global time
				t[0] = -1; // t[0] stores the building number
				t[1] = -1; // t[1] stores the building counter 'count', max(count)==5
				t[2] = -1; // t[2] stores the total building time needed for a building
				t[3] = -1; // t[3] stores the executed time for a building
				t[4] = -1; // if t[4]==1, all buildings are finished
				int[] ans = work(br, t);
				count = t[1]; // reset the building counter
				output(ans, out);
			} else { // all the building in the data structure has been finished
				counter = counter2;
				break;
			}
		}
		if (line.contains("Insert")) {
			str1 = "";
			str2 = "";
			int mark = 0;
			for (int i = 0; i < line.length(); i++) {// read in the instruction
				if (line.charAt(i) == ':') // ignore numbers before ':"
					mark = 1;
				else if (line.charAt(i) >= 48 && line.charAt(i) <= 57 && mark == 1)
					str1 += line.charAt(i); // store numbers after ':' and before ','
				else if (line.charAt(i) == ',')
					mark = 2;
				else if (line.charAt(i) >= 48 && line.charAt(i) <= 57 && mark == 2)
					str2 += line.charAt(i); // store numbers after ','
			}
			int bn = Integer.parseInt(str1);
			int tt = Integer.parseInt(str2);

			buliding_record b = new buliding_record(bn, 0, tt);
			building_record_rbt node = new building_record_rbt(bn, 0, tt, false);
			b.rbt = node;
			node.array = b; // insert into red black tree and min heap
			while (count < 5 && count > 0 && br[0] != null) {// the previous building has not finished
																// or construction has not reach 5 days
				t[0] = -1; // t[0] stores the building number
				t[1] = -1; // t[1] stores the building counter 'count', max(count)==5
				t[2] = -1; // t[2] stores the total building time needed for a building
				t[3] = -1; // t[3] stores the executed time for a building
				t[4] = -1; // if t[4]==1, all buildings are finished
				int[] ans = work(br, t); // construct
				count = t[1];
				output(ans, out);

			}
			if (count == 5 || br[0] == null || count == 0) {// if the building already built for consecutive 5 day
															// or there are no building in the data structure
				count = 0;
				br = f1.insertarray(br, b);
				Root = frbt.insert(node, Root);
			}

		} else if (line.contains(",")) {
			str1 = "";
			str2 = "";
			int mark = 0;
			List<building_record_rbt> result = new ArrayList<building_record_rbt>();
			building_record_rbt r = Root;
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) == ':' || line.charAt(i) == 58)
					mark = 1;
				else if (line.charAt(i) >= 48 && line.charAt(i) <= 57 && mark == 1)
					str1 += line.charAt(i);
				else if (line.charAt(i) == ',')
					mark = 2;
				else if (line.charAt(i) >= 48 && line.charAt(i) <= 57 && mark == 2)
					str2 += line.charAt(i);
			}
			int bn_start = Integer.parseInt(str1);
			int bn_end = Integer.parseInt(str2);
			frbt.find_section(bn_start, bn_end, r, result);// find the section
			if (result.size() == 0)
				out.write("(0, 0, 0)");
			else
				for (int i = 0; i < result.size(); i++) {
					building_record_rbt ans = result.get(i);
					out.write("(" + ans.buildingNum + "," + ans.executed_time + "," + ans.total_time + "),");
				}
			out.write("\n");
			counter++;
			count++;
			if (br[0] != null)// do the construction
				addet(br);
		} else {
			str1 = "";
			str2 = "";
			int mark = 0;
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) == ':' || line.charAt(i) == 58)
					mark = 1;
				else if (line.charAt(i) >= 48 && line.charAt(i) <= 57 && mark == 1)
					str1 += line.charAt(i);
			}
			int bn = Integer.parseInt(str1);
			int[] ans = frbt.lookup(bn, Root);
			if (ans[0] == -1)
				out.write("(0, 0, 0)\n");
			else
				out.write("(" + ans[0] + "," + ans[3] + "," + ans[2] + ")\n");
			counter++;
			count++;
			if (br[0] != null)
				addet(br);
		}
	}

	public static void main(String[] args) {
		try {
			File writename = new File("./output_file.txt");
			writename.createNewFile();
			counter = 0;
			count = 0;
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			buliding_record[] buildr = new buliding_record[2000];
			// Read file
			String pathname = "./" + args[0];
			File filename = new File(pathname);
			InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			while (line != null) {
				int[] t = new int[5];
				line = br.readLine(); // Read one line each time
				if (line != null) // Still have instruction not read
					process(line, buildr, out);
				while (line == null && t[4] != 1) {// All instruction was readed but still have building not finished
					t[0] = -1; // t[0] stores the building number
					t[1] = -1; // t[1] stores the building counter 'count', max(count)==5
					t[2] = -1; // t[2] stores the total building time needed for a building
					t[3] = -1; // t[3] stores the executed time for a building
					t[4] = -1; // if t[4]==1, all buildings are finished
					t = work(buildr, t);
					count = t[1];
					output(t, out);
				}
			}
			// Write txt
			br.close();
			out.flush();
			out.close();
			System.out.println("Finished");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
