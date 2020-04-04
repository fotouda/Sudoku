package sudoku;

import java.util.ArrayList;

public class PuzzleComponent {
	private ArrayList<Cell> cells ;
	public PuzzleComponent()
	{
		cells = new ArrayList<Cell>();
	}
	public ArrayList<Cell> getCells() {
		return cells;
	}
	public void setCells(ArrayList<Cell> cells) {
		this.cells = cells;
	}
	
}
