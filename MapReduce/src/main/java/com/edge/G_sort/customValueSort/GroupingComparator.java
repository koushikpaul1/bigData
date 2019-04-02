/**
 * This comparator controls which keys are grouped together for a single call to the Reducer.reduce() function.
 */

package com.edge.G_sort.customValueSort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class GroupingComparator extends WritableComparator {

	protected GroupingComparator() {
		super(Employee.class, true);
	}

	@SuppressWarnings({ "rawtypes" })
	public int compare(WritableComparable w1, WritableComparable w2) {
		Employee emp1 = (Employee) w1;
		Employee emp2 = (Employee) w2;
		return emp2.getTitleName().compareTo(emp1.getTitleName());
	}
}
