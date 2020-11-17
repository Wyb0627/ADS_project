public class building_record_rbt {
	int buildingNum;
	int executed_time;
	int total_time;
	boolean colorisred;
	building_record_rbt left;
	building_record_rbt right;
	building_record_rbt parent;
	buliding_record array;

	building_record_rbt(int bn, int et, int tt, boolean b) {
		this.buildingNum = bn;
		this.executed_time = et;
		this.total_time = tt;
		this.colorisred = b;
	}
}
