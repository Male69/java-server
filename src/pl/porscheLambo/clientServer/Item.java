package pl.porscheLambo.clientServer;

import java.util.ArrayList;
import java.util.List;

public class Item {
	
	private static int uniqueId = 1;
	private int itemId;
	private String itemName;
	
	public Item(int itemId, String itemName) {
		this.itemId = itemId;
		this.itemName = "Jarek";
		uniqueId++;
	}
	
	public void print() {
		System.out.println("UniqueId:" + uniqueId + "ItemId: " + itemId + "ItemName: " + itemName);
	}

	public static void main(String[] args) {
		//Item obj1 = new Item(5, "jarek");
		//obj1.print();
		//Item obj2 = new Item(10, "kuba");
		//obj2.print();
		List<String> tab1 = new ArrayList<String>();
		List<String> tab2 = new ArrayList<String>();
		tab2.add("jeden");
		tab2.add("jeden");
		tab2.add("jeden");
		tab1 = tab2;
		String result = null ;
		//for (String elem : tab1) {
			result = String.join(":", tab1);
		//}
		System.out.println("Print:" + tab1.get(0) + tab1.get(1) + tab1.get(2) );
		System.out.println(result);
		//tab2.clear();
		//tab2.add(result.split(":"));
	}

}
