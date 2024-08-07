"""Dataset reading utilities."""

import numpy as np
import pandas as pd
import networkx as nx
from scipy import sparse
from texttable import Texttable
import psycopg2
from sklearn.metrics.pairwise import cosine_similarity
from scipy.spatial.distance import cosine

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
def get_edges(mat):
    edges = []
    row_nodes = len(mat)
    col_nodes = len(mat[0])
    for i in range(row_nodes):
        for j in range(col_nodes):
            if mat[i][j] == 1:
                # Step 2: 对于列索引，将其加上行节点数量作为第二个节点的序号
                col_node = j + row_nodes
                # Step 3: 将得到的节点序号组成边，并存储到结果列表中
                edges.append((i, col_node))
                edges.append((col_node, i))
    return edges
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
def get_dis_sim(embeddings,dis,disease):
    # 找到疾病对应的低维嵌入
    location = []
    for i in range(len(disease)):
        # 解决如果病种或危险因素查找不到会报错的bug
        temp = np.where(dis == disease[i])
        if (len(temp[0]) != 0):
            location.append(temp[0][0])
    location = np.array(location)
    # 计算GraRep相似度
    embeddings = embeddings[0][location]
    sim_d = cosine_similarity(embeddings)
    sim_d = np.maximum(sim_d, 0)
    sim_d = new_normalization(sim_d)
    return sim_d
def get_fac_sim(embeddings,dis_num,fac,factor):

    # 找到危险因素对应的低维嵌入
    location = []
    for i in range(len(factor)):
        # 解决如果病种或危险因素查找不到会报错的bug
        temp = np.where(fac == factor[i])
        if (len(temp[0]) != 0):
            location.append(temp[0][0]+dis_num)
    location = np.array(location)
    # 计算GraRep相似度
    embeddings = embeddings[0][location]
    sim_f = cosine_similarity(embeddings)
    sim_f = np.maximum(sim_f, 0)
    sim_f = new_normalization(sim_f)
    return sim_f
def get_sim(embeddings,mat,dis,disease,fac,factor):
    dis_num = len(dis)
    # 找到疾病对应的低维嵌入
    location1 = []
    for i in range(len(disease)):
        # 解决如果病种或危险因素查找不到会报错的bug
        temp = np.where(dis == disease[i])
        if (len(temp[0]) != 0):
            location1.append(temp[0][0])
    location1 = np.array(location1)
    embeddings_dis = embeddings[0][location1]
    # 找到危险因素对应的低维嵌入
    location2 = []
    location3 = []  #危险因素中邻接矩阵是从0开始，因此单独用一个数组记录位置
    for i in range(len(factor)):
        # 解决如果病种或危险因素查找不到会报错的bug
        temp = np.where(fac == factor[i])
        if (len(temp[0]) != 0):
            location2.append(temp[0][0] + dis_num)
            location3.append(temp[0][0])
    location2 = np.array(location2)
    embeddings_fac = embeddings[0][location2]
    mat = mat[location1,:]
    mat = mat[:,location3]
    num1 = len(embeddings_dis)
    num2 = len(embeddings_fac)
    similarity_matrix = np.zeros((num1, num2))
    for i in range(num1):
        for j in range(num2):
            similarity_matrix[i, j] = 1 - cosine(embeddings_dis[i], embeddings_fac[j])
    return similarity_matrix,mat

# a代表得到的相似矩阵，a1代表对应的邻接矩阵，用来判断是否是新挖掘出的关联
def print_sim(a,a1):
    r = a.shape[0]
    c = a.shape[1]
    a = 10000*a
    b = np.sum(a > 0)
    max_n = []
    w = 0
    e = 0
    if ((r + c) * 5 > b):
        n = b
    else:
        n = (r + c) * 3
    for i in range(n):
        m = np.argmax(a)
        p, q = divmod(m, a.shape[1])
        if (a1[p][q] == 1):
            link = [p, q + r, round(a[p, q], 4), False]
            w = w + 1
        else:
            link = [p, q + r, round(a[p, q], 4), True]
            e = e + 1
        max_n.append(link)
        a[p][q] = 0
    print(max_n)


def create_inverse_degree_matrix(edges):
    """
    Creating an inverse degree matrix from an edge list.
    :param edges: Edge list.
    :return D_1: Inverse degree matrix.
    """
    graph = nx.from_edgelist(edges)
    ind = range(len(graph.nodes()))
    degs = [1.0/graph.degree(node) for node in range(graph.number_of_nodes())]

    D_1 = sparse.coo_matrix((degs, (ind, ind)),
                            shape=(graph.number_of_nodes(),
                            graph.number_of_nodes()),
                            dtype=np.float32)

    return D_1

def normalize_adjacency(edges):
    """
    Method to calculate a sparse degree normalized adjacency matrix.
    :param edges: Edge list of graph.
    :return A: Normalized adjacency matrix.
    """
    D_1 = create_inverse_degree_matrix(edges)
    index_1 = [edge[0] for edge in edges] + [edge[1] for edge in edges]
    index_2 = [edge[1] for edge in edges] + [edge[0] for edge in edges]
    values = [1.0 for edge in edges] + [1.0 for edge in edges]
    A = sparse.coo_matrix((values, (index_1, index_2)),
                          shape=D_1.shape,
                          dtype=np.float32)
    A = A.dot(D_1)
    return A

def read_graph(edge_path):
    """
    Method to read graph and create a target matrix.
    :param edge_path: Path to the ege list.
    :return A: Target matrix.
    """
    edges = pd.read_csv(edge_path).values.tolist()
    A = normalize_adjacency(edges)
    return A
def read_graph_from_db(args):
#     从数据库中读取图
    dbname = args.database_dbname
    user = args.database_user
    password = args.database_password
    host = args.database_host
    port = args.database_port
    tableName = args.table_name
    conn = connect_to_database(dbname, user, password, host, port)
    if conn is not None:
        # 获取数据
        columns, data = fetch_data_from_database(conn, tableName)
        # 关闭数据库连接
        conn.close()
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
    dis = np.unique(ass[:, 0])
    fac = np.unique(ass[:, 1])
    df = pd.DataFrame(ass, columns=['Disease', 'Symptom'])
    association = pd.crosstab(df['Disease'], df['Symptom'])
    # 获得关联矩阵
    mat = association.values
    # 根据矩阵获取关联边作为数据输入
    edges = get_edges(mat)
    G = normalize_adjacency(edges)
    return G,mat,dis,fac


def tab_printer(args):
    """
    Function to print the logs in a nice tabular format.
    :param args: Parameters used for the model.
    """
    args = vars(args)
    t = Texttable()
    t.add_rows([["Parameter", "Value"]])
    t.add_rows([[k.replace("_", " ").capitalize(), v] for k, v in args.items()])
    print(t.draw())
