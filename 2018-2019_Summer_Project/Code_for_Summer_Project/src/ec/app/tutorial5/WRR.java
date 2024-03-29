package ec.app.tutorial5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ec.app.tutorial4.Task;
import ec.app.tutorial4.VirtualMachine;

public class WRR implements IAlgorithm {

	public WRR() {
		// TODO Auto-generated constructor stub
	}
	@SuppressWarnings("Duplicates")
	@Override
	public ArrayList<Object> taskMapping(ArrayList<Task> parentTasks, ArrayList<VirtualMachine> vms, Task t, int j) {
		ArrayList<Object> updatedVals = new ArrayList<Object>();

		int index = j % vms.size();

		VirtualMachine vmSel = vms.get(index);

		t.setAllocation_time(Utility.getMaxFinishTime( parentTasks));

		for (VirtualMachine vm : vms) {
			t.setExe_time(t.getTask_size() / (double) vm.getVelocity());

			double preFinishTime = Utility.getMaxFinishTime(vm.getPriority_queue());
			t.setStart_time(Utility.getMaxStartTime(preFinishTime, t.getAllocation_time()));
			t.setWaiting_time();
			t.setRelative_finish_time();
			vm.setRelativeFinish_time(t.getRelative_finish_time());
		}

		VirtualMachine vmMaxRFT = getVMWithMaxRFT(vms);

		if (vmSel.getId().equals(vmMaxRFT.getId())) {
			index = index + 1;
			if (index == vms.size())
				index = 0;
			vmSel = vms.get(index);

		}

		t.setExe_time(t.getTask_size() / (double) vmSel.getVelocity());

		double preFinishTime = Utility.getMaxFinishTime(vmSel.getPriority_queue());

		t.setStart_time(Utility.getMaxStartTime(preFinishTime, t.getAllocation_time()));
		t.setWaiting_time();
		t.setRelative_finish_time();
		t.setFinish_time();

		vmSel.setPriority_queue(t);

		updatedVals.add(t);
		updatedVals.add(vmSel);

		return updatedVals;
	}

	private VirtualMachine getVMWithMaxRFT(ArrayList<VirtualMachine> vms) {
		Collections.sort(vms, new Comparator<VirtualMachine>() {
			@Override
			public int compare(VirtualMachine v1, VirtualMachine v2) {
				return Double.compare(v1.getRelativeFinish_time(), v2.getRelativeFinish_time());
			}
		});

		return vms.get(vms.size() - 1);
	}

}
