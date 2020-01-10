package dhi.optimizer.common;

import java.util.Comparator;

import dhi.optimizer.model.json.SettingsNNTrainDataConfig;

/**
 * Common Comparator Class. <br>
 * : 공통으로 사용하기 위한  Sort Class. 
 */
public class CommonComparator {

	/**
	 * Tag No 오름차순 정렬 Comparator.
	 */
	public class TagNoSortComparator implements Comparator<SettingsNNTrainDataConfig> {
		@Override
		public int compare(SettingsNNTrainDataConfig a, SettingsNNTrainDataConfig b) {
			return a.getTagNo() < b.getTagNo() ? -1 : a.getTagNo() == b.getTagNo() ? 0 : 1;
		}
	}
}
