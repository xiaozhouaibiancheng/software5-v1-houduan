import numpy as np
import argparse
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

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("--disease",type=str,default="高血压")
    parser.add_argument("--factor",type=str,default="咳嗽")
    parser.add_argument("--task-type",type=str,default="disease_mining")
    parser.add_argument("--table-name",type=str,default="association")
    parser.add_argument("--file-path",type=str,default="E:/code/DiseaseAssociationMining/src/main/resources/file/")
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
    association = np.loadtxt(filePath + table + ".txt",
                             dtype=int, encoding="utf-8", skiprows=1)
    # mat = np.load("/home/data/WorkSpace/software5/file/"+table+"_matrix.npy")
    # mat = np.loadtxt("/home/data/WorkSpace/software5/file/"+table+"_matrix.txt")
    # print(dis,fac,association,matrix)
    mat = get_matrix(association, dis.shape[0], fac.shape[0])
    sim = np.load(filePath+table+"_LAGAN_prediction.npy")

    m = get_simplify(sim,dis,disease,fac,factor)
    m1 = get_simplify(mat,dis,disease,fac,factor)
    c = m.shape[1]
    r = m.shape[0]
    rel = get_rel_from_matrix(m,m1)
    # get_rel(rel)