package minesweeper;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        //String[][] board = new String[9][9];
        Board board = new Board();
        Scanner scanner = new Scanner(System.in);
        // write your code here
        System.out.println("How many mines do you want on the field?");
        int nMines = scanner.nextInt();
        List<int[]> userInputs = new ArrayList<>();
        //fillBoard(board,nMines);
        fillBoard(board, nMines);
        //fillHints(board);
        dotsInX(board);
        printBoard(board);
        boolean battling = true;
        while (battling) {
            System.out.println("Set/delete mine marks (x and y coordinates):");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            String command = scanner.next();
            if (x < 10 & x > 0 & y > 0 & y < 10) {
                x--;
                y--;
                String result = checkWin(board, x, y, command,userInputs);
                if(result.equals("win")){
                    System.out.println("Congratulations! You found all the mines!");
                    battling = false;
                }else if(result.equals("lose")){
                    System.out.println("You stepped on a mine and failed!");
                    battling =false;
                }
                printBoard(board);
                /*if(cellValue.equals(".")){
                    board.getRows()[y].getCells()[x]="*";
                    userInputs.add(new int[]{x,y});
                    printBoard(board);
                    win = checkWin(board,userInputs);
                }else if(cellValue.equals("*")){
                    board.getRows()[y].getCells()[x]=".";
                    int sizeUserInputs = userInputs.size();
                    for(int i=0;i<sizeUserInputs;i++){
                        int[] ints = userInputs.get(i);
                        if(ints[0]==x && ints[1]==y){
                            userInputs.remove(ints);
                            break;
                        }
                    }
                    printBoard(board);
                    win = checkWin(board,userInputs);
                }else{
                    System.out.println("There is a number here!");
                }
                if (win){
                    System.out.println("Congratulations! You found all the mines!");
                    battling = false;
                }*/
            }
        }
    }

    /*private static boolean checkWin(Board board, List<int[]> userInputs) {
        boolean winner = false;
        Map<int[],Boolean> inputsFound = new HashMap<>();
        int sizeUserInput = userInputs.size();
        int sizeBoardMines = board.minesCoordinates.size();
        if(sizeBoardMines != sizeUserInput){
            return winner;
        }
        for(int[] inputs : userInputs){
            List<int[]> mines = board.getMinesCoordinates();
            for(int[] mine : mines){
                if(inputs[0] == mine[0] & inputs[1] == mine[1] ){
                    inputsFound.put(mine,true);
                    break;
                }
            }
        }
       if(board.getMinesCoordinates().size() ==  inputsFound.values().size()){
           return !winner;
       }else {
           return  winner;
       }
    }*/
    private static String checkWin(Board board, int x, int y, String command,List<int[]> inputs) {
        List<int[]> mines = board.getMinesCoordinates();
        boolean mineStepped = false;
        for (int[] mine : mines) {
            if (mine[0] == x & mine[1] == y) {
                mineStepped = true;
                break;
            }
        }
        if (mineStepped && command.equals("free")) {
            showAllMines(board);
            return "lose";
        }
        if (command.equals("free")) {
           checkingForFreeAndHints(board,x,y,inputs);
        } else if (command.equals("mine")) {
            boolean inputExist = false;
            int[] inputReference = {};
            for(int[] input : inputs){
                if(input[0]==x & input[1] == y){
                    inputExist = true;
                    inputReference = input;
                    break;
                }
            }
            if (inputExist){
                board.getRows()[y].getCells()[x] = ".";
                inputs.remove(inputReference);
            }else {
                board.getRows()[y].getCells()[x] = "*";
                inputs.add(new int[] {x,y});
                boolean isClear = checkIfAllMinesAreCleared(board,inputs);
                if(isClear){
                    return "win";
                }
            }
        }
        return "continue";
    }

    private static void checkingForFreeAndHints(Board board,int x,int y,List<int[]> inputs) {
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{x, y});
        while (!queue.isEmpty()) {
            int[] coordinates = queue.peek();
            queue.remove(coordinates);
            String currentCell = board.getRows()[coordinates[1]].getCells()[coordinates[0]];
            if(currentCell.equals(".")| currentCell.equals("*")){
                int countForMine = 0;
                if (coordinates[0] - 1 >= 0) {
                    boolean isAMine = lookingForMine(board.getMinesCoordinates(), coordinates[0] - 1, coordinates[1]);
                    if (isAMine) {
                        countForMine++;
                    } else {
                        String cell = board.getRows()[coordinates[1]].getCells()[coordinates[0]-1];
                        if(cell.equals(".")|cell.equals("*")){
                            queue.add(new int[]{coordinates[0] - 1, coordinates[1]});
                        }
                    }
                }
                if (coordinates[0] + 1 <= 8) {
                    boolean isAMine = lookingForMine(board.getMinesCoordinates(), coordinates[0] + 1, coordinates[1]);
                    if (isAMine) {
                        countForMine++;
                    } else {
                        String cell = board.getRows()[coordinates[1]].getCells()[coordinates[0]+1];
                        if(cell.equals(".")|cell.equals("*")){
                            queue.add(new int[]{coordinates[0] + 1, coordinates[1]});
                        }
                    }
                }
                if (coordinates[1] - 1 >= 0) {
                    if (coordinates[0] - 1 >= 0) {
                        boolean isAMine = lookingForMine(board.getMinesCoordinates(), coordinates[0] - 1, coordinates[1] - 1);
                        if (isAMine) {
                            countForMine++;
                        } else {
                            String cell = board.getRows()[coordinates[1]-1].getCells()[coordinates[0]-1];
                            if(cell.equals(".")|cell.equals("*")){
                                queue.add(new int[]{coordinates[0] - 1, coordinates[1]-1});
                            }
                        }
                    }
                    if (coordinates[0] >= 0 & coordinates[0] <= 8) {
                        boolean isAMine = lookingForMine(board.getMinesCoordinates(), coordinates[0], coordinates[1] - 1);
                        if (isAMine) {
                            countForMine++;
                        } else {
                            String cell = board.getRows()[coordinates[1]-1].getCells()[coordinates[0]];
                            if(cell.equals(".")|cell.equals("*")){
                                queue.add(new int[]{coordinates[0] , coordinates[1]-1});
                            }
                        }
                    }
                    if (coordinates[0] + 1 <= 8) {
                        boolean isAMine = lookingForMine(board.getMinesCoordinates(), coordinates[0] + 1, coordinates[1] - 1);
                        if (isAMine) {
                            countForMine++;
                        } else {
                            String cell = board.getRows()[coordinates[1]-1].getCells()[coordinates[0]+1];
                            if(cell.equals(".")|cell.equals("*")){
                                queue.add(new int[]{coordinates[0] + 1, coordinates[1]-1});
                            }
                        }
                    }
                }
                if (coordinates[1] + 1 <= 8) {
                    if (coordinates[0] - 1 >= 0) {
                        boolean isAMine = lookingForMine(board.getMinesCoordinates(), coordinates[0] - 1, coordinates[1] + 1);
                        if (isAMine) {
                            countForMine++;
                        } else {
                            String cell = board.getRows()[coordinates[1]+1].getCells()[coordinates[0]-1];
                            if(cell.equals(".")|cell.equals("*")){
                                queue.add(new int[]{coordinates[0] - 1, coordinates[1]+1});
                            }
                        }
                    }
                    if (coordinates[0] >= 0 & coordinates[0] <= 8) {
                        boolean isAMine = lookingForMine(board.getMinesCoordinates(), coordinates[0], coordinates[1] + 1);
                        if (isAMine) {
                            countForMine++;
                        } else {
                            String cell = board.getRows()[coordinates[1]+1].getCells()[coordinates[0]];
                            if(cell.equals(".")|cell.equals("*")){
                                queue.add(new int[]{coordinates[0], coordinates[1]+1});
                            }
                        }
                    }
                    if (coordinates[0] + 1 <= 8) {
                        boolean isAMine = lookingForMine(board.getMinesCoordinates(), coordinates[0] + 1, coordinates[1] + 1);
                        if (isAMine) {
                            countForMine++;
                        } else {
                            String cell = board.getRows()[coordinates[1]+1].getCells()[coordinates[0]+1];
                            if(cell.equals(".")|cell.equals("*")){
                                queue.add(new int[]{coordinates[0] + 1, coordinates[1]+1});
                            }
                        }
                    }
                }
                if (countForMine>0){
                    if (board.getRows()[coordinates[1]].getCells()[coordinates[0]].equals("*")) {
                        int size = inputs.size();
                        for (int i = 0; i < size; i++) {
                            int[] input = inputs.get(i);
                            if (input[0] == coordinates[0] & input[1] == coordinates[1]) {
                                inputs.remove(input);
                                break;
                            }
                        }
                    }
                    board.getRows()[coordinates[1]].getCells()[coordinates[0]] =  String.valueOf(countForMine);
                }else{
                    board.getRows()[coordinates[1]].getCells()[coordinates[0]] = "/";
                }
            }
        }
    }

    private static boolean checkIfAllMinesAreCleared(Board board,List<int[]> inputs) {
        List<int[]> mines = board.getMinesCoordinates();
        if(mines.size() != inputs.size()){
            return false;
        }else{
            int countMinesFound=0;
            for(int[] input : inputs){
                for(int[] mine : mines){
                    if(input[0] == mine[0] & input[1]== mine[1]){
                        countMinesFound++;
                    }
                }
            }
            return countMinesFound == mines.size();
        }
    }

    private static void showAllMines(Board board) {
        List<int[]> mineList = board.getMinesCoordinates();
        for(int[] mine : mineList){
            board.getRows()[mine[1]].getCells()[mine[0]] = "X";
        }
    }

    private static boolean lookingForMine(List<int[]> mines, int x, int y) {
        for (int[] mine : mines) {
            if (mine[0] == x & mine[1] == y) {
                return true;
            }
        }
        return false;
    }

    private static int randomInt(int min, int max) {
        return (min + (int) (Math.random() * (max + 1)));
    }

    private static int randomInt(int max) {
        return (int) (Math.random() * (max + 1));
    }

    /*private static void fillBoard(String [][] board,int n){
        int minesPerLine = minesPerLine(n);
        int[] arrayMines = new int[9];
        List<int[]> coordinatesForMine = new ArrayList<>();
        fillArrayMines(arrayMines,n,minesPerLine);
        fillPlacesForMines(coordinatesForMine,arrayMines);
        for(int i=0;i<9;i++){
            int indexArrayPlaces =0;
            int[] placesArray = coordinatesForMine.get(i);
            int sizeArrayPlaces = placesArray.length;
            for (int j = 0; j < 9; j++){
               if(indexArrayPlaces == sizeArrayPlaces){
                   board[i][j]=".";
               }else{
                   if(j==placesArray[indexArrayPlaces]){
                       board[i][j] ="X";
                       indexArrayPlaces++;
                   }else{
                       board[i][j] =".";
                   }
               }
            }
        }
    }*/
    private static void fillBoard(Board board, int n) {
        int minesPerLine = minesPerLine(n);
        int[] arrayMines = new int[9];
        List<int[]> coordinatesForMine = new ArrayList<>();
        fillArrayMines(arrayMines, n, minesPerLine);
        fillPlacesForMines(coordinatesForMine, arrayMines);
        board.setMinesCoordinates(coordinatesForMine);
        for (int i = 0; i < 9; i++) {
            board.getRows()[i] = new Row();
            for (int j = 0; j < 9; j++) {
                board.getRows()[i].getCells()[j] = ".";
            }
        }
        for (int[] place : coordinatesForMine) {
            board.getRows()[place[1]].getCells()[place[0]] = "X";
        }
    }

    /***
     * Function to randomize the mines, this just determine the number of mines in each line considering 9 rows
     * @param arrayMines array with the number of mines per line
     * @param n total number of mines
     * @param minesPerLine a random number to decide max number of mines that a line can have
     */
    private static void fillArrayMines(int[] arrayMines, int n, int minesPerLine) {
        int count = 0;
        if (n < 6) {
            while (count < n) {
                int line = randomInt(8);
                int val = arrayMines[line];
                if (val == 0) {
                    int minesForLine = randomInt(minesPerLine);
                    if ((count + minesForLine) <= n) {
                        arrayMines[line] = minesForLine;
                        count += minesForLine;
                    }
                }
            }
        } else {
            while (count < n) {
                for (int i = 0; i < 9; i++) {
                    if (count == n) {
                        break;
                    }
                    if (arrayMines[i] < minesPerLine) {
                        if (arrayMines[i] == 0) {
                            boolean fillingSpace = true;
                            while (fillingSpace) {
                                int minesForThisLine = randomInt(minesPerLine);
                                if ((count + minesForThisLine) <= n) {
                                    arrayMines[i] = minesForThisLine;
                                    count += minesForThisLine;
                                    fillingSpace = false;
                                }
                            }
                        } else {
                            int minesForThisLine = arrayMines[i] + 1;
                            arrayMines[i] = minesForThisLine;
                            count++;
                        }
                    }
                }
            }
        }
    }

    private static void fillPlacesForMines(List<int[]> places, int[] minesInLine) {
        for (int nMines : minesInLine) {
            for (int i = 0; i < nMines; i++) {
                boolean creatingCoordinates = true;
                while (creatingCoordinates) {
                    int xCoordinate = randomInt(8);
                    int yCoordinate = randomInt(8);
                    boolean goodCoordinate = true;
                    for (int[] addedPlaces : places) {
                        if (addedPlaces[0] == xCoordinate && addedPlaces[1] == yCoordinate) {
                            goodCoordinate = false;
                            break;
                        }
                    }
                    if (goodCoordinate) {
                        creatingCoordinates = false;
                        places.add(new int[]{xCoordinate, yCoordinate});
                    }
                }
            }
        }
    }

    private static void fillHints(Board board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String cellValue = board.getRows()[i].getCells()[j];
                if (cellValue.equals(".")) {
                    int counterForCell = 0;
                    if (i - 1 >= 0) {
                        if (j - 1 >= 0) {
                            if (board.getRows()[i - 1].getCells()[j - 1].equals("X")) {
                                counterForCell++;
                            }
                        }
                        if (board.getRows()[i - 1].getCells()[j].equals("X")) {
                            counterForCell++;
                        }
                        if (j + 1 <= 8) {
                            if (board.getRows()[i - 1].getCells()[j + 1].equals("X")) {
                                counterForCell++;
                            }
                        }
                    }
                    if (j - 1 >= 0) {
                        if (board.getRows()[i].getCells()[j - 1].equals("X")) {
                            counterForCell++;
                        }
                    }
                    if (j + 1 <= 8) {
                        if (board.getRows()[i].getCells()[j + 1].equals("X")) {
                            counterForCell++;
                        }
                    }
                    if (i + 1 <= 8) {
                        if (j - 1 >= 0) {
                            if (board.getRows()[i + 1].getCells()[j - 1].equals("X")) {
                                counterForCell++;
                            }
                        }
                        if (board.getRows()[i + 1].getCells()[j].equals("X")) {
                            counterForCell++;
                        }
                        if (j + 1 <= 8) {
                            if (board.getRows()[i + 1].getCells()[j + 1].equals("X")) {
                                counterForCell++;
                            }
                        }
                    }
                    if (counterForCell > 0) {
                        board.getRows()[i].getCells()[j] = String.valueOf(counterForCell);
                    }
                }
            }
        }
    }

    private static void dotsInX(Board board) {
        int size = board.getMinesCoordinates().size();
        for (int i = 0; i < size; i++) {
            int[] coordinates = board.getMinesCoordinates().get(i);
            if (board.getRows()[coordinates[1]].getCells()[coordinates[0]].equals("X")) {
                board.getRows()[coordinates[1]].getCells()[coordinates[0]] = ".";
            }
        }
    }

    private static void printBoard(Board board) {
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (int i = 0; i < 9; i++) {
            System.out.printf("%d|", i + 1);
            for (int j = 0; j < 9; j++) {
                System.out.print(board.getRows()[i].getCells()[j]);
            }
            System.out.print("|\n");
        }
        System.out.println("-|---------|");
    }

    private static int minesPerLine(int n) {
        if (n > 5) {
            return (int) ((n * 0.01) * 10) + 2;
        } else {
            return 1;
        }
    }

}
