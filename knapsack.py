import math


def remove_dominated_items(weights, values):
    # Initialize the list with indices [0, 1, ..., n-1]
    ndIndices = list(range(len(weights)))
    indices_to_iterate = ndIndices.copy()  # Copy of the list for iteration

    for j in indices_to_iterate[:-1]:
        if j not in ndIndices:  # Skip if j has been removed in a previous iteration
            continue
        for k in indices_to_iterate[j + 1:]:
            if k not in ndIndices:  # Skip if k has been removed in a previous iteration
                continue
            wj = weights[j]
            wk = weights[k]
            vj = values[j]
            vk = values[k]

            if math.floor(wk / wj) * vj >= vk:
                ndIndices.remove(k)
            elif math.floor(wj / wk) * vk >= vj:
                ndIndices.remove(j)
                break

    return ndIndices


def upper_bound(weights, values, capacity, node_index):
    if len(weights) == 1:
        return math.floor(capacity / weights[0] * values[0])
    elif len(weights) == 2:
        return math.floor(capacity / weights[0] * values[0]) + math.floor((capacity - math.floor(capacity / weights[0]) * weights[0]) / weights[1] * values[1])
    W = capacity
    w1, w2, w3 = weights[node_index], weights[node_index +
                                              1], weights[node_index + 2]
    v1, v2, v3 = values[node_index], values[node_index +
                                            1], values[node_index + 2]

    W_prime = W - math.floor(W / w1) * w1
    z_prime = math.floor(W / w1) * v1 + math.floor(W_prime / w2) * v2
    W_double_prime = W_prime - math.floor(W_prime / w2) * v2

    U_prime = z_prime + math.floor(W_double_prime * v3 / w3)
    U_double_prime = z_prime + math.floor((W_double_prime + math.ceil(1 / w1) * (
        w2 - W_double_prime) * w1) * (v2 / w2) - math.ceil(1 / w1) * (w2 - W_double_prime) * v1)

    U = max(U_prime, U_double_prime)

    return U


def knapsackBnB(values, weights, capacity):
    ndIndices = remove_dominated_items(weights, values)
    weights = [weights[i] for i in ndIndices]
    values = [values[i] for i in ndIndices]
    # Step 1. [Initialize]
    n = len(weights)
    ratios = [v/w for v, w in zip(values, weights)]
    sorted_indices = sorted(range(n), key=lambda i: -ratios[i])
    weights = [weights[i] for i in sorted_indices]
    values = [values[i] for i in sorted_indices]
    x = [0]*n
    x_hat = x[:]
    i = 0
    z_hat = 0
    M = {}
    x[0] = capacity // weights[0]
    V_N = values[0] * x[0]
    W_prime = capacity - weights[0] * x[0]
    U = upper_bound(weights, values, capacity, 0)
    mi = [0] * len(weights)
    for i in range(len(mi)):
        mi[i] = min(weights[i + 1:] or [0])

    if len(weights) == 1:
        return V_N
    while True:
        # Step 2. [Develop]
        if W_prime < mi[i]:
            if z_hat < V_N:
                z_hat = V_N
                x_hat = x[:]
                if z_hat == U:
                    break
            i += 1
        else:
            j = next((j for j in range(i+1, n) if weights[j] <= W_prime), n)
            if j == n or V_N == U or M.get((j, W_prime), 0) >= V_N:
                i += 1
            else:
                x[j] = W_prime // weights[j]
                V_N += values[j] * x[j]
                W_prime -= weights[j] * x[j]
                M[(i, W_prime)] = V_N
                i = j

        # Step 3. [Backtrack]
        while True:
            j = max((j for j in range(i) if x[j] > 0), default=-1)
            if j < 0:
                return z_hat
            i = j
            x[i] -= 1
            V_N -= values[i]
            W_prime += weights[i]
            if i+1 < len(values) and i+1 < len(weights):
                if W_prime >= mi[i-1] and V_N + math.floor(W_prime * ((values[i+1])/(weights[i+1]))) > z_hat:
                    break
            V_N -= values[i] * x[i]
            W_prime += weights[i] * x[i]
            x[i] = 0

        # Step 4. [Replace a jth item with an hth item]
        j = i
        h = j + 1
        while h < n:
            if z_hat >= V_N + math.floor(W_prime * ((values[h])/(weights[h]))):
                h += 1
            elif weights[h] >= weights[j]:
                if weights[h] == weights[j] or weights[h] > W_prime or z_hat >= V_N + values[h]:
                    h += 1
                else:
                    z_hat = V_N + values[h]
                    x_hat = x[:]
                    x_hat[h] = 1
                    if z_hat == U:
                        return z_hat
                    j = h
                    h += 1
            else:
                if W_prime - weights[h] < mi[h-1]:
                    h += 1
                else:
                    i = h
                    x[i] = W_prime // weights[i]
                    V_N += values[i] * x[i]
                    W_prime -= weights[i] * x[i]
                    break
    return z_hat


W = 100
n = 3
val = [1, 30, 50]
wt = [1, 50, 100]


weights = [2, 5, 10, 5]
values = [40, 30, 50, 10]
capacity = 16
print(knapsackBnB(values, weights, capacity))
