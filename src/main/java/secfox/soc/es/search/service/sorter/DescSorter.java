package secfox.soc.es.search.service.sorter;

import java.util.Comparator;

public class DescSorter implements Comparator<String>{

	@Override
	public int compare(String o1, String o2) {
		// TODO Auto-generated method stub
		return o2.compareTo(o1);
	}

}
