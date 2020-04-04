package sudoku;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Cell {

	private int value;
	private boolean valueConfirmed=false;
	private PuzzleComponent column;
	private PuzzleComponent row;
	private PuzzleComponent square;
	private Set<Integer> availableOptions;
	private int guessStatus;
	private int guessIndex=0;
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public boolean isValueConfirmed() {
		return valueConfirmed;
	}
	public void setConfirmedValue(boolean valueConfirmed) {
		this.valueConfirmed = valueConfirmed;
	}
	public PuzzleComponent getColumn() {
		return column;
	}
	public void setColumn(PuzzleComponent column) {
		this.column = column;
	}
	public PuzzleComponent getRow() {
		return row;
	}
	public void setRow(PuzzleComponent row) {
		this.row = row;
	}
	public PuzzleComponent getSquare() {
		return square;
	}
	public void setSquare(PuzzleComponent square) {
		this.square = square;
	}
	
	public int getConfirmedValue()
	{
		if (valueConfirmed)
		{
			return value;
		}
		int confirmedValue;		
		Set<Integer> valuesUsed = getUsedValues();		
		if(valuesUsed.size()!=8)
		{
			return 0;
		}
		Set<Integer> allValues = getAllValues();
		allValues.removeAll(valuesUsed);
		confirmedValue=allValues.iterator().next().intValue();
		return confirmedValue;
	}
	private Set<Integer> getAllValues() {
		Set<Integer> allValues = new HashSet<Integer>();
		for(int i=1; i<10; i++)
		{
			allValues.add(i);
		}
		return allValues;
	}
	private Set<Integer> getUsedValues() {
		Set<Integer> valuesUsed = new HashSet<Integer>();
		addComponentValue(valuesUsed,row);
		addComponentValue(valuesUsed,column);
		addComponentValue(valuesUsed,square);
		return valuesUsed;
	}
	private void addComponentValue(Set<Integer> valuesUsed, PuzzleComponent component) {
		for(Cell cell : component.getCells())
		{
			if(cell.getValue()!=0)
			{
				valuesUsed.add(cell.getValue());
			}			
		}	
	}
	private void addToUniqueSet(Set<Integer> valuesUsed) {

	}
	public void setAvailableOptions() {
		Set<Integer> valuesUsed = getUsedValues();		
		Set<Integer> allValues = getAllValues();
		allValues.removeAll(valuesUsed);
		availableOptions=allValues;		
	}
	public Set<Integer> getAvailableOptions() {
		return availableOptions;
	}
	public int getGuessStatus() {
		return guessStatus;
	}
	public void setGuessStatus(int guessStatus) {
		this.guessStatus = guessStatus;
	}
	public int getGuessIndex() {
		return guessIndex;
	}
	public void setGuessIndex(int guessIndex) {
		this.guessIndex = guessIndex;
	}
	
	
}
