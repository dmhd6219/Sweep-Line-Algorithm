/**
 * Class with quick sort algorithm.
 *
 * @param <T> the type parameter
 */
public class Sort<T extends Comparable<T>> {
    /**
     * Quick sort method.
     *
     * @param arr   the array to sort
     * @param begin the beginning index
     * @param end   the end index
     */
    public void quickSort(T[] arr, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    /**
     * Positions the last component as the pivot.
     * After that, if the value of any element is smaller than the pivot, it is replaced.
     *
     * @param arr   the array to sort
     * @param begin the beginning index
     * @param end   the end index
     * @return position of pivot
     */
    private int partition(T[] arr, int begin, int end) {
        T pivot = arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (arr[j].compareTo(pivot) > 0) {
                i++;

                T swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }

        T swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

}
