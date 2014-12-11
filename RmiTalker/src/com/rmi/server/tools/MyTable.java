package com.rmi.server.tools;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class MyTable extends JTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyTable(Object[][] rows, String[] columns) {
		super(rows, columns);
	}
	
	public MyTable(TableModel tableModel) {
		super(tableModel);
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
