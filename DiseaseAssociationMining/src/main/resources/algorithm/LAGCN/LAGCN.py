import psycopg2
import argparse

import numpy
import numpy as np
import pandas as pd
from numpy import matrix

# def connect_to_database(dbname,user,password,host,port):
#     try:
#         # 连接到数据库
#         conn = psycopg2.connect(
#             dbname = dbname,
#             user = user,
#             password = password,
#             host = host,
#             port = port)
#         return conn
#     except psycopg2.Error as e:
#         return None
#
# def fetch_data_from_database(conn,tableName):
#     try:
#         # 创建游标
#         cur = conn.cursor()
#
#         # 执行查询
#         cur.execute(f'SELECT * FROM "{tableName}"')
#         #获取表头
#         columns = [desc[0] for desc in cur.description]
#         # 获取所有查询结果
#         rows = cur.fetchall()
#         data = np.array(rows)
#         # 关闭游标
#         cur.close()
#     except psycopg2.Error as e:
#         print("查询出错：", e)
#     return columns,data

# def get_simplify(matrix,dis,disease,fac,factor):
#     location1 = []
#     for i in range(len(disease)):
#         location1.append(np.where(dis==disease[i])[0][0])
#     location2 = []
#     for i in range(len(factor)):
#         location2.append(np.where(fac==factor[i])[0][0])
#     location1=np.array(location1)
#     location2=np.array(location2)
#     matrix = matrix[location1,:]
#     matrix = matrix[:,location2]
#     return matrix

def get_matrix(a,r,c):
    len = a.shape[0]
    matrix = np.zeros([r,c],dtype=int)
    for i in range(len):
        matrix[a[i][0]][a[i][1]-r]=1
    return matrix
def get_simplify(matrix,dis,disease,fac,factor):
    location1 = []
    # print(np.where(dis==disease))
    # print(len(np.where(dis == "高血压")[0]))
    for i in range(len(disease)):
        temp = np.where(dis == disease[i])
        if(len(temp[0])!=0):
            location1.append(temp[0][0])
    location2 = []
    for i in range(len(factor)):
        temp = np.where(fac == factor[i])
        if(len(temp[0])!=0):
            location2.append(temp[0][0])
    location1=np.array(location1)
    location2=np.array(location2)
    matrix = matrix[location1,:]
    matrix = matrix[:,location2]
    return matrix

def get_rel_from_matrix(a,a1):
    r = a.shape[0]
    c = a.shape[1]
    b= np.sum(a>0.3)
    max_n=[]
    w=0
    e=0
    if((r+c)*5>b):
        n=b
    else: n=(r+c)*3
    for i in range(n):
        m = np.argmax(a)
        p,q=divmod(m,a.shape[1])
        if (a1[p][q] == 1):
            link = [p, q + r,round(a[p,q],4),False]
            w=w+1
        else:
            link = [p, q + r,round(a[p,q],4),True]
            e=e+1
        max_n.append(link)
        a[p][q]=0
    # for i in range(r):
    #     for j in range(c):
    #         if (a[i][j] > 0):
    #             if(a1[i][j]==1):
    #                 b = [i, j + r,a[i,j],True]
    #             else: b = [i, j + r,a[i,j],False]
    #             rel.append(b)
    print(max_n)
    return max_n


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--disease", type=str, default="椎体爆裂骨折,呼吸性碱中毒,苍耳中毒,高血压,sakdjgferfg")
    parser.add_argument("--factor", type=str, default="鼻出血,肢体发凉,月经周期改变")
    parser.add_argument("--task-type", type=str, default="factor_mining")
    parser.add_argument("--table-name", type=str, default="disease_kb")
    parser.add_argument('--database-host', type=str, default='10.16.48.219')
    parser.add_argument('--database-dbname', type=str, default='software5')
    parser.add_argument('--database-port', type=str, default='5432')
    parser.add_argument('--database-password', type=str, default='111111')
    parser.add_argument('--database-user', type=str, default='pg')
    args = parser.parse_args()
    dbname = args.database_dbname
    user = args.database_user
    password = args.database_password
    host = args.database_host
    port = args.database_port
    tableName = args.table_name
    # 连接到数据库
    # conn = connect_to_database(dbname, user, password, host, port)
    # if conn is not None:
    #     # 获取数据
    #     columns, data = fetch_data_from_database(conn, tableName)
    #     # 关闭数据库连接
    #     conn.close()
    disease = args.disease.split(',')
    factor = args.factor.split(',')
    tasktype = args.task_type

    # disIndex = 0
    # facIndex = 0
    # for i in range(len(columns)):
    #     if (columns[i] == 'disease_name'):
    #         disIndex = i
    #     elif (columns[i] == 'factor_name'):
    #         facIndex = i
    # disColumn = data[:, disIndex]
    # facColumn = data[:, facIndex]
    # ass = np.column_stack((disColumn, facColumn))
    # # print(ass)
    #
    # df = pd.DataFrame(ass, columns=['Disease', 'Symptom'])
    # association = pd.crosstab(df['Disease'], df['Symptom'])
    filePath = "F:/java/medical/DiseaseAssociationMining_new (2)/DiseaseAssociationMining/src/main/resources/file/"
    dis = np.loadtxt(filePath + tableName + "_disease.txt", dtype=str, encoding="utf-8")
    dis = dis[1:]
    dis = dis[:, [0]]
    fac = np.loadtxt(filePath + tableName + "_factor.txt", dtype=str, encoding="utf-8")
    fac = fac[1:]
    fac = fac[:, [0]]
    association = np.loadtxt(filePath + tableName + ".txt",
                             dtype=int, encoding="utf-8", skiprows=1)
    mat = get_matrix(association, dis.shape[0], fac.shape[0])

    sim = np.load(filePath + tableName + "_LAGAN_prediction.npy")
    m = get_simplify(sim, dis, disease, fac, factor)
    m1 = get_simplify(mat, dis, disease, fac, factor)

    c = m.shape[1]
    r = m.shape[0]
    rel = get_rel_from_matrix(m,m1)