import time
import numpy as np
import random

# set random state
np.random.seed(42)

# membuat dataset untuk mengetes running time
sorted_small = np.arange(1, 2**9 + 1)
sorted_medium = np.arange(1, 2**13 + 1)
sorted_big = np.arange(1, 2**16 + 1)

random_small = np.random.randint(1, 2**9, 2**9)
random_medium = np.random.randint(1, 2**13, 2**13)
random_big = np.random.randint(1, 2**16, 2**16 + 1)

reversed_small= np.arange(2**9, 0, -1)
reversed_medium= np.arange(2**13, 0, -1)
reversed_big= np.arange(2**16, 0, -1)

# function untuk menghitung running time
def calculate_running_time(func):
    def wrapper(*args, **kwargs):
        start_time = time.time()  
        result = func(*args, **kwargs) 
        end_time = time.time()  
        elapsed_time = (end_time - start_time) * 1000
        print(f"Function '{func.__name__}' took {elapsed_time:.4f} miliseconds to run.")
        return result
    return wrapper

def randomized_shell_sort(arr):
    n = len(arr)
    gap_sequence = [random.randint(1, n // 2) for _ in range(n // 2)]

    for gap in gap_sequence:
        for i in range(gap, n):
            temp = arr[i]
            j = i
            while j >= gap and arr[j - gap] > temp:
                arr[j] = arr[j - gap]
                j -= gap
            arr[j] = temp
    return arr


def heapify(arr, n, i):
	largest = i # Initialize largest as root
	l = 2 * i + 1 # left = 2*i + 1
	r = 2 * i + 2 # right = 2*i + 2

	if l < n and arr[i] < arr[l]:
		largest = l

	if r < n and arr[largest] < arr[r]:
		largest = r

	if largest != i:
		(arr[i], arr[largest]) = (arr[largest], arr[i]) 
		heapify(arr, n, largest)



def heapSort(arr):
	n = len(arr)

	for i in range(n // 2 - 1, -1, -1):
		heapify(arr, n, i)

	for i in range(n - 1, 0, -1):
		(arr[i], arr[0]) = (arr[0], arr[i]) # swap
		heapify(arr, i, 0)



# Function to calculate time the max heap sort
@calculate_running_time
def calculate_heap_sort(arr):
	heapSort(arr)

# Function to calculate time the max heap sort
@calculate_running_time
def calculate_randomized_shell_sort(arr):
	randomized_shell_sort(arr)


subject_randomized_shell = reversed_medium.copy()
subject_max_heap = random_medium.copy()


print(calculate_randomized_shell_sort(sorted_medium))
print(calculate_randomized_shell_sort(random_medium))
print(calculate_randomized_shell_sort(reversed_medium))
