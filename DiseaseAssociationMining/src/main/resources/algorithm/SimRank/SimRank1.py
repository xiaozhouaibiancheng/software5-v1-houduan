import numpy
import numpy as np
from numpy import matrix

# with open('test/sample1.txt', 'r') as log_fp:
#     logs = [ log.strip() for log in log_fp.readlines() ]

# logs_tuple = [ tuple(log.split(",")) for log in logs ]
a = np.load("test/disease_symptom.npy")
logs_tuple = numpy.loadtxt("rel/dis_sys.csv", delimiter=",")
r = a.shape[0]
c = a.shape[1]
logs_tuple = logs_tuple.tolist()
# print(logs_tuple)
# queries = list(set([ log[0] for log in logs_tuple ]))
# ads = list(set([ log[1] for log in logs_tuple ]))
queries = []
ads=[]
for i in range(r):
    queries.append(float(i))
for j in range(c):
    ads.append(float(j+r))

# Graph means the relations number
graph = numpy.matrix(numpy.zeros([len(queries), len(ads)]))
for log in logs_tuple:
    query = log[0]
    ad = log[1]
    q_i = queries.index(query)
    a_j = ads.index(ad)
    graph[q_i, a_j] += 1

query_sim = matrix(numpy.identity(len(queries)))
# print(query_sim.shape)
ad_sim = matrix(numpy.identity(len(ads)))
# print(ad_sim.shape)

def get_ads_num(query):
    q_i = queries.index(query)
    return graph[q_i]

def get_queries_num(ad):
    a_j = ads.index(ad)
    return graph.transpose()[a_j]

def get_ads(query):
    series = get_ads_num(query).tolist()[0]
    return [ ads[x] for x in range(len(series)) if series[x] > 0 ]

def get_queries(ad):
    series = get_queries_num(ad).tolist()[0]
    return [ queries[x] for x in range(len(series)) if series[x] > 0 ]

def new_normalization (w):
    m = w.shape[0]
    p = np.zeros([m,m])
    for i in range(m):
        for j in range(m):
            if i == j:
                p[i][j] = 1/2
            elif np.sum(w[i,:])-w[i,i]>0:
                p[i][j] = w[i,j]/(2*(np.sum(w[i,:])-w[i,i]))
    return p

def query_simrank(q1, q2, C):
    """
    in this, graph[q_i] -> connected ads
    """
    """
    print "q1.ads"
    print get_ads_num(q1).tolist()
    print "q2.ads"
    print get_ads_num(q2).tolist()
    """
    if q1 == q2 : return 1
    if get_ads_num(q1).sum()==0 or get_ads_num(q2).sum()==0 : return 0
    prefix = C / (get_ads_num(q1).sum() * get_ads_num(q2).sum())
    postfix = 0
    for ad_i in get_ads(q1):
        for ad_j in get_ads(q2):
            i = ads.index(ad_i)
            j = ads.index(ad_j)
            postfix += ad_sim[i, j]
    return prefix * postfix
    

def ad_simrank(a1, a2, C):
    """
    in this, graph need to be transposed to make ad to be the index
    """
    """
    print "a1.queries"
    print get_queries_num(a1)
    print "a2.queries"
    print get_queries_num(a2)
    """
    if a1 == a2 : return 1
    prefix = C / (get_queries_num(a1).sum() * get_queries_num(a2).sum())
    postfix = 0
    for query_i in get_queries(a1):
        for query_j in get_queries(a2):
            i = queries.index(query_i)
            j = queries.index(query_j)
            postfix += query_sim[i,j]
    return prefix * postfix


def simrank(type,C=0.8, times=1):
    global query_sim, ad_sim
    if(type=="disease_mining"):
        for run in range(times):
            # queries simrank
            new_query_sim = matrix(numpy.identity(len(queries)))  #创建方阵
            for qi in queries:
                for qj in queries:
                    i = queries.index(qi)
                    j = queries.index(qj)
                    new_query_sim[i,j] = query_simrank(qi, qj, C)
        query_sim = new_query_sim
    elif(type=="factor_mining"):
        # ads simrank
        new_ad_sim = matrix(numpy.identity(len(ads)))
        for ai in ads:
            for aj in ads:
                i = ads.index(ai)
                j = ads.index(aj)
                new_ad_sim[i,j] = ad_simrank(ai, aj, C)
        ad_sim = new_ad_sim

def get_rel(sim):
    row, col = np.diag_indices_from(sim)
    sim[row, col] = 0
    sim = 1000 * sim
    l = sim.shape[0]
    rel = []
    for i in range(l):
        for j in range(i):
            if (sim[i][j] != 0):
                link = [i, j,round(sim[i][j],4)]
                rel.append(link)
    print(rel)

# if __name__ == '__main__':
#     type="disease_mining"
#     simrank(type)
#     query_sim = new_normalization(query_sim)
#     print(query_sim)
#     print(sum(sum(query_sim)))
#     get_rel(query_sim)
    # np.save("./sim/d_food3.npy",query_sim)

if __name__ == '__main__':
    sim=np.load("../file/SimRank_d_nor.npy")
    get_rel(sim)
    
