package ar.edu.unicen.exa.intia.imgProc.mobile.view.fragments.list;

import java.util.ArrayList;
import java.util.List;

public class ItemModel<T> {
	private T item;
//	private Integer position;
	private boolean selected;
	
	public static <E> ItemModel<E> buildItem(E item) {
		return buildItem(item, null);
	}

	protected static <E> ItemModel<E> buildItem(E item, Integer position) {
		ItemModel<E> itemModel = new ItemModel<E>();
		itemModel.item = item;
//		itemModel.position = position;
		itemModel.selected = false;
		return itemModel;
	}

	public static <E> List<ItemModel<E>> buildModel(List<E> listado) {
		List<ItemModel<E>> result = new ArrayList<ItemModel<E>>();
		if (listado != null && !listado.isEmpty()) {
			int pos = 0;
			for (E item : listado) {
//				result.add(buildItem(item, pos));
				result.add(buildItem(item));
				pos++;
			}
		}
		return result;
	}
	
	public static <E> List<ItemModel<E>> buildModel(E[] listado) {
		List<ItemModel<E>> result = new ArrayList<ItemModel<E>>();
		if (listado != null && listado.length > 0) {
			int pos = 0;
			for (E item : listado) {
				result.add(buildItem(item, pos));
				pos++;
			}
		}
		return result;
	}

	public void setItem(T item) {
		this.item = item;
	}
	
	public T getItem() {
		return item;
	}
	
//	public void setPosition(Integer position) {
//		this.position = position;
//	}
//	
//	public Integer getPosition() {
//		return position;
//	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	@Override
	public String toString() {
		return item.toString();
	}
}