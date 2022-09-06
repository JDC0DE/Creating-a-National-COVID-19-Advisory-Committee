import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class ass1_comp3010 {
    // initialising of global variables
    public static int userListn = 0;
    public static int numGroups = 0;
    public static ArrayList<ArrayList<Integer>> listofLists = new ArrayList<>();
    public static ArrayList<Integer> relistIds = new ArrayList<>();
    public static ArrayList<Integer> wholeInput = new ArrayList<>();
    public static ArrayList<Integer> tempHold = new ArrayList<>();
    public static long startTime = 0;
    public static Scanner userInput = new Scanner(System.in);
    public static HashMap<Integer, Integer> repeatIds = new HashMap<>();

    public static void main(String[] args) {
        // main method where program is started calling first method that allows the
        // program to function
        parsingInput();
    }

    public static void parsingInput() {
        // Reading the user input into data that is useful is done by prompting the user
        // for initial information using the scanner object. The program loops through
        // the specified number of groups with each iteration creating a list
        // that will hold each set of data. The inner loops checks for any more expected
        // inputs whilst converting users input from strings to integers where it is
        // added to a list of the whole input and another list for each set where 0
        // denotes the end of list. These individual list are added to a global list of
        // lists before being broken out of inner loop to start the next iteration of
        // the outer loop.
        int valHold = 0;
        int temp = 0;
        System.out.println("Enter the number of groups from which you must find representatives: ");
        numGroups = Integer.parseInt(userInput.next());
        System.out.println("Enter the list of members of each group (one group per line, each terminated by 0): ");
        //PRE-condition: a valid set of inputs is parsed from the user
        //POST-condition: the inputs will be organised into their corresponding data storage
        //Invariant: i >= 0 && i <= numGroups
        for (int i = 0; i < numGroups; i++) {
            ArrayList<Integer> groupMembers = new ArrayList<Integer>();
            while (userInput.hasNext()) {
                userListn = Integer.parseInt(userInput.next());
                wholeInput.add(userListn);
                if (userListn == 0) {
                    groupMembers.add(userListn);
                    listofLists.add(groupMembers);
                    break;
                } else {
                    groupMembers.add(userListn);
                }
            }
        }
        // Using the whole input list to loop through the values are checked to see if
        // they are not 0 as they are not needed to be checked for duplicates. Each id
        // is checked if it is in the hashmap, initially nothing will be meaning
        // each id will be given a value of 1 meaning 1st occurence and put into the
        // hashmap. Now if that id is found later in the list its value of repetitions
        // is stored and incremented meaning more than 1 occurences and then put back
        // into the hashmap with the id and updated value
        int i = 0;
        while (i < wholeInput.size()) {
            if (wholeInput.get(i) != 0) {
                temp = wholeInput.get(i);
                if (repeatIds.containsKey(temp)) { // Conatins key is used to check as the key of the hashmap represents
                                                   // the ids and value is the number of repeats
                    valHold = repeatIds.get(temp);
                    valHold++;
                    repeatIds.put(temp, valHold);
                } else {
                    repeatIds.put(temp, 1);
                }
            }
            i++;
        }
        userInput.close(); // The scanner object is closed as it is no longer need to read inputs
        repeats();
    }

    public static void sort(ArrayList<Integer> input, int si, int ei) {
        // Part of the mergeSort referenced in the report as [1] link:
        // https://www.withexample.com/merge-sort-using-arraylist-java-example/
        if (si < ei && (ei - si) >= 1) {
            int mid = (ei + si) / 2;
            sort(input, si, mid);
            sort(input, mid + 1, ei);
            merge(input, si, mid, ei);
        }
    }

    public static void merge(ArrayList<Integer> input, int si, int mi, int ei) {
        // Part of the mergeSort referenced in the report as [1] link:
        // https://www.withexample.com/merge-sort-using-arraylist-java-example/
        ArrayList<Integer> mergedSortedArray = new ArrayList<Integer>();

        int leftIndex = si;
        int rightIndex = mi + 1;

        while (leftIndex <= mi && rightIndex <= ei) {
            if (input.get(leftIndex) >= input.get(rightIndex)) {
                mergedSortedArray.add(input.get(leftIndex));
                leftIndex++;
            } else {
                mergedSortedArray.add(input.get(rightIndex));
                rightIndex++;
            }
        }
        while (leftIndex <= mi) {
            mergedSortedArray.add(input.get(leftIndex));
            leftIndex++;
        }

        while (rightIndex <= ei) {
            mergedSortedArray.add(input.get(rightIndex));
            rightIndex++;
        }

        int i = 0;
        int j = si;
        while (i < mergedSortedArray.size()) {
            input.set(j, mergedSortedArray.get(i++));
            j++;
        }
    }

    public static void repeats() {
        // Using an enhanced for loop each key and value of the hashmap is grabbed and
        // checks it value to determine which ids have the greatest number of
        // repetitions where the value and ids of each added to corresponding lists.
        // Alternativley the ids which are not the greatest duplicates, but still have
        // repest are added to a seperate list for precuationary worst-case responses.
        int tempval = 0;
        ArrayList<Integer> valHold = new ArrayList<>();
        //PRE-condition: grabs each valid id with at least 1 instance of itself
        //POST-condition: the ids are organised into list for the greatest and lesser duplicate ids
        //Invariant: tempMap.getKey > 0 && tempMap.getValue > 0
        for (Map.Entry<Integer, Integer> tempMap : repeatIds.entrySet()) {
            if (tempMap.getValue() > 1 && tempMap.getValue() >= tempval) {
                valHold.add(tempMap.getValue());
                tempval = tempMap.getValue();
                relistIds.add(tempMap.getKey());
            } else if (tempMap.getValue() > 1 && tempMap.getValue() < tempval) {
                tempHold.add(tempMap.getKey());
            }

        }
        // The values are sorted using mergeSort in a descending order where it can be
        // checked if the initial value is greater than the last value of the list where
        // the ids are then sorted into a descending order and the next function is
        // called.
        // Another condition is after the values are sorted and both the first and last
        // value are equal then this input had ids which all had the same amount of
        // repetitions meaning the ids don't have to be sorted as they are all valid.
        sort(valHold, 0, valHold.size() - 1);
        if (valHold.get(0) > valHold.get(valHold.size() - 1)) {
            sort(relistIds, 0, relistIds.size() - 1);
            remove();
        } else if (valHold.get(0) == valHold.get(valHold.size() - 1)) {
            remove();
        }
    }

    public static void remove() {
        // Now that the ids have been selected they can be compared against the initial
        // input list of lists to determine which list corresponds to the greatest ids
        // so that they can be set to empty.
        // However checks were put into place to esnure ids were still valid and had
        // duplicates as lists that match with an id and are set to empty may have had
        // other repeated ids in them, thus potentially voiding the state of that
        // duplicate id i.e. id == 3 repeats
        // after lists have been removed id may still == 3 repeats, but actually now has
        // 1. So now if there is 1 match then the index of that list is stored in
        // preperation, if that id has at least 2 matches then the id is still optimal
        // and the lists are removed and the id added
        // to a result list anything greater than 2 is set to empty as intended.
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Integer> emptyList = new ArrayList<>();
        int checker = 0;
        int temp = 0;
        boolean emptyCheck = false;
        int listCounter = 0;
        //PRE-condition: there must be at least 1 greatest id
        //POST-condition: each id will be matched to a list and checked before setting that list to empty
        //Invariant: i >= 0 && i <= relistids.size
        //Invariant: j >= 0 && i <= listofLists.size
        for (int i = 0; i < relistIds.size(); i++) {
            checker = 0;
            for (int j = 0; j < listofLists.size(); j++) {
                if (listofLists.get(j).contains(relistIds.get(i))) { // The contains method will check if the duplicate
                                                                     // id is in the current list
                    checker++;
                    if (checker == 1) {
                        temp = j;
                    } else if (checker == 2) {
                        listofLists.set(temp, emptyList);
                        listofLists.set(j, emptyList);
                        result.add(relistIds.get(i));
                    } else if (checker > 2) {
                        listofLists.set(j, emptyList);
                    }
                }

            }

        }
        checker = 0;// variables are reset to be reused again
        temp = 0;

        // As a precautionary worst-case check the list of lists is checked if any of
        // its elements are not empty and how many aren't.
        for (int i = 0; i < listofLists.size(); i++) {
            if (!listofLists.get(i).isEmpty()) {
                emptyCheck = true;
                listCounter++;
            }
        }
        // If there is any lists that aren't empty due to not matching to any of the
        // greatest ids then using the list of lesser duplicate ids and the same checks
        // as prior an optimal choice will be made and those remaining lists will be set
        // to empty.
        //PRE-condition: there must be at least 1 lesser id
        //POST-condition: each id will be matched to a list and checked before setting that list to empty
        //Invariant: i >= 0 && i <= tempHold.size
        //Invariant: j >= 0 && i <= listofLists.size
        if (emptyCheck == true) {
            for (int i = 0; i < tempHold.size(); i++) {
                checker = 0;
                for (int j = 0; j < listofLists.size(); j++) {
                    if (listofLists.get(j).contains(tempHold.get(i))) {
                        checker++;
                        if (checker == 1) {
                            temp = j;
                        } else if (checker == 2) {
                            listofLists.set(temp, emptyList);
                            listofLists.set(j, emptyList);
                            result.add(tempHold.get(i));
                        } else if (checker > 2) {
                            listofLists.set(j, emptyList);
                        }
                    }

                }
            }
        }
        // A edge case of the worst-case scenario was determined if only 1 list remained
        // where none of the prior checks would work as there would be no match 2 or
        // greater. Thus in this scenario whichever id matches from both the greatest
        // and lesser ids list
        // matches 1st will be chosen and added to the result list and that list being
        // set to empty. The program has functioned correctly if all list of lists are
        // now empty.
        if (listCounter == 1) {
            for (int i = 0; i < relistIds.size(); i++) {
                if (listofLists.get(temp).contains(relistIds.get(i))) {
                    listofLists.set(temp, emptyList);
                    result.add(relistIds.get(i));
                }
            }
            for (int j = 0; j < tempHold.size(); j++) {
                if (listofLists.get(temp).contains(tempHold.get(j))) {
                    listofLists.set(temp, emptyList);
                    result.add(tempHold.get(j));
                }
            }
        }
        result(result);
    }

    public static void result(ArrayList<Integer> input) {
        // Outputs the results of the program
        System.out.println("The number of members selected and their ids are :");
        System.out.println(input.size());
        for (int j = 0; j < input.size(); j++) {
            System.out.print(input.get(j) + " ");
        }
        System.out.println();
    }
}
