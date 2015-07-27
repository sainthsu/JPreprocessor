package com.mob.gradle.tasks;

import com.mob.ExpressionParser;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.util.ArrayList;

public class MapTask extends Task {
	private String name;
	private ArrayList<MapItem> map = new ArrayList<MapItem>();
	
	public void setName(String name) {
		this.name = name;
	}
	
	public MapItem createItem() {
		MapItem item = new MapItem();
		map.add(item);
		return item;
    }
	
	public void execute() throws BuildException {
		int size = map.size();
		getProject().setProperty(name + "#size", String.valueOf(size));
		ExpressionParser parser = new ExpressionParser();
		for (int i = 0; i < size; i++) {
			String key = map.get(i).name;
			String value = parser.parse(map.get(i).value);
			getProject().setProperty(name + "#" + key, value);
		}
	}
	
	public class MapItem {
		private String name;
		private String value;
		
		public void setName(String name) {
			this.name = name;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
	
	}
	
}
