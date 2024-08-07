"""Running GraRep"""

from grarep import GraRep
from param_parser import parameter_parser
from utils import read_graph, tab_printer, read_graph_from_db,get_dis_sim,get_fac_sim,get_sim,print_sim

def learn_model(args):
    """
    Method to create adjacency matrix powers, read features, and learn embedding.
    :param args: Arguments object.
    """
    # A = read_graph(args.edge_path)
    # print(A)
    disease = args.disease.split(',')
    factor = args.factor.split(',')
    G,mat,dis,fac = read_graph_from_db(args)
    model = GraRep(G, args)
    embeddings = model.optimize()
    # model.save_embedding()
    # 需要获取计算的相似度矩阵和原有的邻接矩阵
    sim,rel = get_sim(embeddings, mat,dis, disease,fac,factor)
    print_sim(sim,rel)



if __name__ == "__main__":
    args = parameter_parser()
    # tab_printer(args)
    learn_model(args)
