```python
def train_knnm(x, labels, erd, representatives=list()):
    states = np.zeros(len(x))  # Mark all the instance as 'not covered'
    distance_matrix = get_distance_matrix(x)
    not_covered = get_not_covered(states)

    # while there are examples to group
    while len(not_covered) > 0:
        max_neighbourhood = list()
        global min_md
        tuple_max_neighbourhood = None
        for i in not_covered:
            # get distance from i to all other tuples
            distances = distance_matrix[i]

            # sort distances
            sorted_distances = [d for d in sorted(enumerate(distances), key=lambda a: a[1])]

            # filter only those which has not been yet covered
            sorted_distances = [d for d in sorted_distances if states[d[0]] == 0]

            # compute neighbourhood
            q = 0
            neighbourhood = list()
            errors = 0
            while q < len(sorted_distances) and (labels[sorted_distances[q][0]] == labels[i] or errors < erd):#根绝容忍度等判断簇是否继续扩散
                neighbourhood.append(sorted_distances[q][0])
                if labels[sorted_distances[q][0]] != labels[i]:
                    errors += 1
                q += 1

            if len(neighbourhood) > len(max_neighbourhood):#找当前最大的簇
                max_neighbourhood = neighbourhood
                tuple_max_neighbourhood = i
        # add representative
        # representatives format (rep(di), all_tuples in neighbourhood, class(di), Sim(di))
        rep = (tuple_max_neighbourhood, x[tuple_max_neighbourhood])
        num = max_neighbourhood
        cls = labels[tuple_max_neighbourhood]
        sim = distance_matrix[tuple_max_neighbourhood, max_neighbourhood[-1]]
        lay = 0
        representatives.append([rep, num, cls, sim, lay])#当前最大的簇形成新簇
        if len(num)/sim<min_md :
            min_md=len(num)/sim
        # update states array
        for i in max_neighbourhood:#更新覆盖状态
            states[i] = 1
        not_covered = get_not_covered(states)

        # print(len(representatives), len(representatives[len(representatives)-1][1]))

    return representatives

```