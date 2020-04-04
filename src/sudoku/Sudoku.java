package sudoku;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Sudoku {
	private static final int ERROR = 0;
	private static final int FOUND = 1;
	private static final int REMAINS = 2;
	private PuzzleComponent[] rows = new PuzzleComponent[9];
	private PuzzleComponent[] columns = new PuzzleComponent[9];
	private PuzzleComponent[] squares = new PuzzleComponent[9];
	private List<Cell> unsolved = new ArrayList<Cell>();

	private int [] input = {
	        9,2,0,0,0,5,8,0,0,
	        0,0,1,7,2,6,3,0,9,
	        0,0,3,8,9,1,2,0,6,
	        0,9,0,0,0,0,1,0,2,
	        7,0,0,0,6,0,5,0,8,
	        0,0,0,0,3,0,7,0,0,
	        5,0,8,0,1,3,0,0,7,
	        0,4,0,6,0,7,9,1,5,
	        0,0,0,2,0,0,6,0,0
};
	public static void main(String[] args) {
		Sudoku sudoku = new Sudoku();
		sudoku.setInput("resources\\sudokuHard.txt");
		
		sudoku.setPuzzle(sudoku.input);

		System.out.println("Oritinal Puzzle");
		sudoku.printPuzzle();
		sudoku.easyAlgorithm();
		System.out.println("After Easy Algorithm");
		sudoku.printPuzzle();
		boolean faultySudoko=sudoku.bruteForce();
		sudoku.printPuzzle();
		if(faultySudoko)
		{
			System.out.println("Error in Sudoku Puzzle.");
			System.out.println("Please check the puzzle you entered.");
		}
		else
		{

			System.out.println("Solved");
			sudoku.printPuzzle();
			int c=sudoku.rows[6].getCells().get(6).getConfirmedValue();
			System.out.println(c);

		}
		
	}
	
	private boolean bruteForce() {
		boolean faultySudoko=setAvailbleOptions();
		System.out.println("finished available options");
		
		
		if(faultySudoko)
		{
			return faultySudoko;
		}
		for(int index=0;index<unsolved.size(); index++)
		{
			Cell cell=unsolved.get(index);
			cell.setAvailableOptions();
			switch (cell.getAvailableOptions().size()) 
			{
			case 0:
				cell.setGuessStatus(ERROR);		
				if(index==0)
				{
					return true;
					
				}
				cell.setValue(0);
				index=findPreviousGuess(index);
				break;
			case 1:
				cell.setGuessStatus(FOUND);				
				cell.setValue(cell.getAvailableOptions().iterator().next());
				break;
			default:
				boolean exhaustedGuesses = guess(index);
				if (exhaustedGuesses)
				{
					cell.setValue(0);
					cell.setGuessStatus(ERROR);
					index=findPreviousGuess(index);
				}
				else
				{
					cell.setGuessStatus(REMAINS);
					
				}
				break;
			};
		}
		return faultySudoko;
		
	}
	
	private boolean guess(int index) {
		boolean exhaustedGuesses=false;
		Cell cell=unsolved.get(index);
		int guessNumber=cell.getGuessIndex()+1;
		if(cell.getAvailableOptions().size()< guessNumber)
		{
			exhaustedGuesses=true;
			cell.setValue(0);
			cell.setGuessIndex(0);
			
		}
		else
		{
			cell.setValue((Integer)cell.getAvailableOptions().toArray()[guessNumber-1]);
			cell.setGuessIndex(guessNumber);
		}
		return exhaustedGuesses;
	}

	private int findPreviousGuess(int index) {
		for(int i=index; i>-1; i--)
		{
			Cell cell=unsolved.get(i);
			cell.setValue(0);
			if(cell.getGuessStatus()==FOUND)
			{
				cell.setGuessIndex(0);
				continue;
			}
			if(i==index)
			{
				cell.setGuessIndex(0);
			}
			else if(cell.getAvailableOptions().size() > cell.getGuessIndex())
			{
				return i-1;
			}
			else
			{
				cell.setGuessIndex(0);
			}
			
		}	
		return 0;
	}

	private boolean setAvailbleOptions() 
	{
		boolean faultySudoko=false;
		unsolved=new ArrayList<Cell>();
		for (PuzzleComponent row : rows) 
		{
			for (Cell cell : row.getCells()) 
			{
				if (!cell.isValueConfirmed()) 
				{
					cell.setAvailableOptions();
					switch (cell.getAvailableOptions().size()) {
					case 0:
						cell.setGuessStatus(ERROR);
						faultySudoko=true;
						break;
					case 1:
						cell.setGuessStatus(FOUND);
						cell.setValue(cell.getAvailableOptions().iterator().next());
						cell.setConfirmedValue(true);
						break;
					default:
						cell.setGuessStatus(REMAINS);
						unsolved.add(cell);
						break;
					}
				}
			}
		}
		return faultySudoko;
	}

	private void printPuzzle() {
		for(PuzzleComponent row : rows)
		{
			for(Cell cell : row.getCells())
			{
				System.out.print(cell.getValue() + "\t");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		
	}

	private void easyAlgorithm() 
	{
		boolean found = true;
		while (found) 
		{
			found = false;
			for (int rowIndex=0; rowIndex<9 ;rowIndex++) 
			{
				for (int cellIndex=0; cellIndex<9; cellIndex++) 
				{
					Cell cell=rows[rowIndex].getCells().get(cellIndex);
					if (cell.getValue()==0 ) 
					{
						cell.setValue(cell.getConfirmedValue());
						if(cell.getValue()!=0)
						{
							found = true;
							cell.setConfirmedValue(true);
						}
						
						
					}
				}
			}
		}
	}

	private void setPuzzle(int [] intArray)
	{
		int inputIndex = 0;
		for(int rowIndex=0; rowIndex<9; rowIndex++)
		{
			for(int colIndex=0; colIndex<9; colIndex++)
			{
				Cell cell=new Cell();
				cell.setValue(input[inputIndex]);
				if(input[inputIndex]!=0)
				{
					cell.setConfirmedValue(true);
				}
				int squareIndex=getSquareIndex(rowIndex, colIndex);
				if(rows[rowIndex]==null)
				{
					PuzzleComponent row=new PuzzleComponent();
					rows[rowIndex]=row;
				}
				rows[rowIndex].getCells().add(colIndex, cell);
				if(columns[colIndex]==null)
				{
					PuzzleComponent column=new PuzzleComponent();
					columns[colIndex]=column;
				}
				columns[colIndex].getCells().add(rowIndex, cell);
				if(squares[squareIndex]==null)
				{
					PuzzleComponent square=new PuzzleComponent();
					squares[squareIndex]=square;
				}
				squares[squareIndex].getCells().add(cell);
				
				cell.setRow(rows[rowIndex]);
				cell.setColumn(columns[colIndex]);
				cell.setSquare(squares[squareIndex]);
				inputIndex++;
				
			}
		}
		
		
	}
	private int getSquareIndex(int rowIndex, int colIndex) {
		int squareIndex=8;
		if(rowIndex >-1 && rowIndex < 3 && colIndex >-1 && colIndex < 3)
		{
			squareIndex=0;
		}
		else if ( rowIndex >-1 && rowIndex <3 && colIndex >2 && colIndex < 6 )
		{
			squareIndex=1;
		}
		else if ( rowIndex >-1 && rowIndex <3 && colIndex >5 && colIndex < 9)
		{
			squareIndex=2;
		}
		else if(rowIndex >2 && rowIndex < 6 && colIndex >-1 && colIndex < 3)
		{
			squareIndex=3;
		}
		else if(rowIndex >2 && rowIndex < 6 && colIndex >2 && colIndex < 6)
		{
			squareIndex=4;
		}
		else if(rowIndex >2 && rowIndex < 6 && colIndex >5 && colIndex < 9)
		{
			squareIndex=5;
		}
		else if(rowIndex >5 && rowIndex < 9 && colIndex >-1 && colIndex < 3)
		{
			squareIndex=6;
		}
		else if(rowIndex >5 && rowIndex < 9 && colIndex >2 && colIndex < 6)
		{
			squareIndex=7;
		}
		else
		{
			squareIndex=8;
		}
		return squareIndex;
	}

	public PuzzleComponent[] getRows() {
		return rows;
	}
	public void setRows(PuzzleComponent[] rows) {
		this.rows = rows;
	}
	public PuzzleComponent[] getColumns() {
		return columns;
	}
	public void setColumns(PuzzleComponent[] columns) {
		this.columns = columns;
	}
	public PuzzleComponent[] getSquares() {
		return squares;
	}
	public void setSquares(PuzzleComponent[] squares) {
		this.squares = squares;
	}
	private void setInput(String fileName)
	{
		BufferedReader bufferReader;
		  
	    try
	    {
	    	bufferReader = new BufferedReader(new FileReader(fileName), 1024);
	    }
	    catch (FileNotFoundException e)
	    {
	      return;
	    }
		
	    boolean successReading=true;
	    int inputIndex=0;
	    while(successReading)
	    {
	    	try {
	    		String line=bufferReader.readLine();
	    		if(line==null || line.length()==0 )
	    		{
	    			successReading=false;
	    		}
	    			
	    		for(int index=0; index<line.length();index++)
	    		{
	    			char c = line.charAt(index); 
	    			String s = String.valueOf(c);
	    			try {
	    				input[inputIndex] = Integer.valueOf(s).intValue();
	    				inputIndex++;
						
					} catch (Exception e) {
						// TODO: handle exception
					}
	    		}
			} catch (Exception e) {
				// TODO: handle exception
			}
	    }
	    try {
	    	bufferReader.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}


}
