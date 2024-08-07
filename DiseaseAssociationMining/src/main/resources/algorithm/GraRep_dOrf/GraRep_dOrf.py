"""Running GraRep"""

from model import GraRep
from param_parser import parameter_parser
from utils import read_graph, tab_printer, read_graph_from_db,get_dis_sim,get_fac_sim,print_sim

def learn_model(args):
    """
    Method to create adjacency matrix powers, read features, and learn embedding.
    :param args: Arguments object.
    """
    # A = read_graph(args.edge_path)
    # print(A)
    disease = args.disease.split(',')
    factor = args.factor.split(',')
    G,dis,fac = read_graph_from_db(args)
    dis_num = len(dis)
    model = GraRep(G, args)
    embeddings = model.optimize()
    # model.save_embedding()
    if(args.task_type == "disease_mining"):
        sim = get_dis_sim(embeddings,dis,disease)
    elif(args.task_type == "factor_mining"):
        sim = get_fac_sim(embeddings,dis_num,fac,factor)
    print_sim(sim)



if __name__ == "__main__":
    args = parameter_parser()
    # tab_printer(args)
    learn_model(args)
