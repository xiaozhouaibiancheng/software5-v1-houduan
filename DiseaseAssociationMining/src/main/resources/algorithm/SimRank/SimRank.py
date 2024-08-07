import psycopg2
import argparse

import numpy
import numpy as np
import pandas as pd
from numpy import matrix

def connect_to_database(dbname,user,password,host,port):
    try:
        # 连接到数据库
        conn = psycopg2.connect(
            dbname = dbname,
            user = user,
            password = password,
            host = host,
            port = port)
        return conn
    # print("连接数据库成功")
    except psycopg2.Error as e:
        return None

def fetch_data_from_database(conn,tableName):
    try:
        # 创建游标
        cur = conn.cursor()

        # 执行查询
        cur.execute(f'SELECT * FROM "{tableName}"')
        #获取表头
        columns = [desc[0] for desc in cur.description]
        # 获取所有查询结果
        rows = cur.fetchall()
        data = np.array(rows)
        # 关闭游标
        cur.close()
    except psycopg2.Error as e:
        print("查询出错：", e)
    return columns,data

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
    for i in range(len(disease)):
        # 解决如果病种或危险因素查找不到会报错的bug
        # location1.append(np.where(dis==disease[i])[0][0])
        temp = np.where(dis == disease[i])
        if(len(temp[0])!=0):
            location1.append(temp[0][0])
    location2 = []
    for i in range(len(factor)):
        # location2.append(np.where(fac==factor[i])[0][0])
        temp = np.where(fac == factor[i])
        if(len(temp[0])!=0):
            location2.append(temp[0][0])
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


# def main():


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--disease", type=str, default="椎体爆裂骨折,呼吸性碱中毒,苍耳中毒")
    parser.add_argument("--factor", type=str, default="鼻出血,肢体发凉,月经周期改变")
    parser.add_argument("--task-type", type=str, default="factor_mining")
    parser.add_argument("--table-name", type=str, default="disease_kb")
    parser.add_argument("--database-host", type=str, default="10.16.48.219")
    parser.add_argument("--database-dbname", type=str, default="software5")
    parser.add_argument("--database-port", type=str, default="5432")
    parser.add_argument("--database-password", type=str, default="111111")
    parser.add_argument("--database-user", type=str, default="pg")
    args = parser.parse_args()
    dbname = args.database_dbname
    user = args.database_user
    password = args.database_password
    host = args.database_host
    port = args.database_port
    tableName = args.table_name
    # 连接到数据库
    conn = connect_to_database(dbname, user, password, host, port)
    if conn is not None:
        # 获取数据
        columns, data = fetch_data_from_database(conn, tableName)
        # 关闭数据库连接
        conn.close()
    disease = args.disease.split(',')
    factor = args.factor.split(',')
    tasktype = args.task_type

    disIndex = 0
    facIndex = 0
    for i in range(len(columns)):
        if (columns[i] == 'disease_name'):
            disIndex = i
        elif (columns[i] == 'factor_name'):
            facIndex = i
    disColumn = data[:, disIndex]
    facColumn = data[:, facIndex]
    ass = np.column_stack((disColumn, facColumn))
    # print(ass)

    df = pd.DataFrame(ass, columns=['Disease', 'Symptom'])
    association = pd.crosstab(df['Disease'], df['Symptom'])
    mat = association.values
    dis = np.unique(ass[:, 0])
    fac = np.unique(ass[:, 1])
    m = get_simplify(mat, dis, disease, fac, factor)

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

    if (tasktype == "disease_mining"):
        query_sim = simrank(tasktype)
        get_rel(query_sim)
    elif (tasktype == "factor_mining"):
        ad_sim = simrank(tasktype)
        get_rel(ad_sim)