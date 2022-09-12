def LIS_Dynamic(A):
    # NOTE: -999 is a sentinel value that indicates the end of a sequence
    
    # If A is only one element
    if len(A) == 1:
        return [1, 0, [-999], 0]
    
    # The best length of the LIS on A[first element:C_i] (inclusive)
    C = [0 for x in range(len(A))]
    # The best index to go back to for a given S[i]
    S = [0 for x in range(len(A))]

    # This tells us the length of the ultimate LIS
    best_length_overall = 0
    # This tells us the last index of the ultimate LIS
    best_final_index = -999
    # This tells us where we should go next after best_final_index
    best_prev_index_overall = -999
    
    for i in range(len(A)):

        # For a given C[i], indicates what is the optimal previous
        # element to go back to in a LIS
        best_prev_index = 0

        # For a given C[i], indicates the LIS from elements 1
        # (index 0 in Python) to i-1
        prev_max_length = 0

        if i == 0:   
            C[0] = 1
            S[0] = -999
            continue
        
        for j in range(i):
            if A[j] <= A[i]:
                if C[j] > prev_max_length:
                    prev_max_length = C[j]
                    best_prev_index = j
                    
        C[i] = prev_max_length + 1

        # if LIS from a given element is length 1, then there is
        # no element to go back to
        if C[i] == 1:
            S[i] = -999
        else:
            S[i] = best_prev_index
            
        if C[i] > best_length_overall:
            best_length_overall = C[i] 
            best_prev_index_overall = S[i]
            best_final_index = i

    return [best_length_overall, best_prev_index_overall, S, best_final_index]

def printTrace(bestIndex, A, bestIndexList, bestFinalIndex):
    sequence = []

    # specialIndex starts at the best final index and iterates
    # backwards through the array according to the optimal
    # previous index at each element which was stored in S
    # and is passed into printTrace as bestIndexList
    specialIndex = bestFinalIndex

    while (specialIndex != -999):
        sequence.append(A[specialIndex])
        specialIndex = bestIndexList[specialIndex]

    return sequence[::-1]
    
A = [2, 3, 1, 9, 2, 6, 3, 8, 7]
A = [4, 3, 2]
A = [2]
A = [9, 8, 7, 6, 4, 7, 3, 5, 1]
A = [9, 1, 8, 2, 7, 3, 6, 4, 5, 5]
A = [10, 11, 12, 13, 14, 15, 0, 1, 2, 3, 4, 5, 6, 7]

bestList = LIS_Dynamic(A)
print(printTrace(bestList[1], A, bestList[2], bestList[3]))
