import argparse

import numpy
import numpy as np
from numpy import matrix

# a = np.load("test/association_matrix.npy")
# logs_tuple = numpy.loadtxt("rel/dis_sys.csv", delimiter=",")
# r = a.shape[0]
# c = a.shape[1]
# logs_tuple = logs_tuple.tolist()
#
# queries = []
# ads=[]
# for i in range(r):
#     queries.append(float(i))
# for j in range(c):
#     ads.append(float(j+r))
#
# # Graph means the relations number
# graph = numpy.matrix(numpy.zeros([len(queries), len(ads)]))
# for log in logs_tuple:
#     query = log[0]
#     ad = log[1]
#     q_i = queries.index(query)
#     a_j = ads.index(ad)
#     graph[q_i, a_j] += 1
#
# query_sim = matrix(numpy.identity(len(queries)))
# print(query_sim.shape)
# ad_sim = matrix(numpy.identity(len(ads)))
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
    if a1 == a2 : return 1
    if get_queries_num(a1).sum() == 0 or get_queries_num(a2).sum() == 0: return 0
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
        query_sim = new_normalization(query_sim)
        return query_sim
    elif(type=="factor_mining"):
        # ads simrank
        new_ad_sim = matrix(numpy.identity(len(ads)))
        for ai in ads:
            for aj in ads:
                i = ads.index(ai)
                j = ads.index(aj)
                new_ad_sim[i,j] = ad_simrank(ai, aj, C)
        ad_sim = new_ad_sim
        ad_sim = new_normalization(ad_sim)
        return ad_sim

def get_matrix(a,r,c):
    len = a.shape[0]
    matrix = np.zeros([r,c],dtype=int)
    for i in range(len):
        matrix[a[i][0]][a[i][1]-r]=1
    return matrix
def get_simplify(matrix,dis,disease,fac,factor):
    location1 = []
    # print(np.where(dis==disease))
    for i in range(len(disease)):
        location1.append(np.where(dis==disease[i])[0][0])
    location2 = []
    for i in range(len(factor)):
        location2.append(np.where(fac==factor[i])[0][0])
    location1=np.array(location1)
    location2=np.array(location2)
    matrix = matrix[location1,:]
    matrix = matrix[:,location2]
    return matrix

def get_rel_from_matrix(a):
    r = a.shape[0]
    c = a.shape[1]
    p = 0
    rel = []
    for i in range(r):
        for j in range(c):
            if (a[i][j] == 1):
                b = [i, j + r]
                rel.append(b)
                p = p + 1
    rel = np.array(rel)
    return rel

def get_rel(sim):
    row, col = np.diag_indices_from(sim)
    sim[row, col] = 0
    sim = 10 * sim
    l = sim.shape[0]
    for i in range(l):
        for j in range(i):
            sim[j][i]=0
            # if (sim[i][j] > 0):
            #     link = [i, j,round(sim[i][j],4)]
            #     rel.append(link)
    a = np.sum(sim>0)
    max_n=[]
    if(l*3>a):
        n=a
    else: n=l*3
    for i in range(n):
        m = np.argmax(sim)
        r,c=divmod(m,sim.shape[1])
        link = [r,c,round(sim[r][c],4)]
        max_n.append(link)
        sim[r][c]=0
    print(max_n)

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("--disease",type=str,default="高血压")
    parser.add_argument("--factor",type=str,default="咳嗽")
    parser.add_argument("--task-type",type=str,default="disease_mining")
    parser.add_argument("--table-name",type=str,default="association")
    # parser.add_argument("--file-path",type=str,default="E:/code/DiseaseAssociationMining/src/main/resources/file/")
    parser.add_argument("--file-path",type=str,default="F:/java/medical/DiseaseAssociationMining/src/main/resources/file/")
    # parser.add_argument('--database-url', type=str, default='localhost:3306/medical')
    # parser.add_argument('--database-password', type=str, default='111111')
    # parser.add_argument('--database-user', type=str, default='root')
    args = parser.parse_args()
    disease = args.disease.split(',')
    factor = args.factor.split(',')
    type = args.task_type
    table = args.table_name
    filePath = args.file_path
    # dis = np.loadtxt("/home/data/WorkSpace/software5/file/"+table+"_disease.txt",dtype=str,encoding="utf-8")
    dis = np.loadtxt(filePath+table+"_disease.txt",dtype=str,encoding="utf-8")
    dis=dis[1:]
    fac = np.loadtxt(filePath+table+"_factor.txt",dtype=str,encoding="utf-8")
    fac=fac[1:]
    association = np.loadtxt(filePath+table+".txt",dtype=int,encoding="utf-8",skiprows=1)
    # mat = np.load("/home/data/WorkSpace/software5/file/"+table+"_matrix.npy")
    # mat = np.loadtxt("/home/data/WorkSpace/software5/file/"+table+"_matrix.txt")
    # print(dis,fac,association,matrix)
    mat = get_matrix(association,dis.shape[0],fac.shape[0])

    m = get_simplify(mat,dis,disease,fac,factor)
    r = m.shape[0]
    c = m.shape[1]
    rel = get_rel_from_matrix(m)
    rel = rel.tolist()


    queries = []
    ads = []
    for i in range(r):
        queries.append(float(i))
    for j in range(c):
        ads.append(float(j + r))

    # Graph means the relations number
    graph = numpy.matrix(numpy.zeros([len(queries), len(ads)]))
    for log in rel:
        query = log[0]
        ad = log[1]
        q_i = queries.index(query)
        a_j = ads.index(ad)
        graph[q_i, a_j] += 1

    query_sim = matrix(numpy.identity(len(queries)))
    ad_sim = matrix(numpy.identity(len(ads)))


    if(type=="disease_mining"):
        query_sim=simrank(type)
        get_rel(query_sim)
    elif(type=="factor_mining"):
        ad_sim=simrank(type)
        get_rel(ad_sim)

    # type="disease_mining"
    # simrank(type)
    # query_sim = new_normalization(query_sim)
    # print(query_sim)
    # print(sum(sum(query_sim)))
    # get_rel(query_sim)
    # np.save("./sim/d_food3.npy",query_sim)

# if __name__ == '__main__':
#     sim=np.load("F:/java/medical/DiseaseAssociationMining/src/main/resources/file/SimRank_d_nor.npy")
#     get_rel(sim)
    
