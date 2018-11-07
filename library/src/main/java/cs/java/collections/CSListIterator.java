package cs.java.collections;

import java.util.List;

public class CSListIterator<T> extends CSIterator<T> {
	private List<T> _list;

	public CSListIterator(List<T> list) {
		super(list.size());
		this._list = list;
	}

	 public T getCurrent() {
		return _list.get(index());
	}

	 protected void onRemove() {
		_list.remove(index());
	}

}
