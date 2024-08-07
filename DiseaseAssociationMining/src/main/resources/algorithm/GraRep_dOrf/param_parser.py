"""Parsing parameters from the command line."""

import argparse

def parameter_parser():
    """
    A method to parse up command line parameters. By default it gives an embedding of Cora.
    The default hyperparameters give a good quality representation without grid search.
    Representations are sorted by node ID.
    """
    parser = argparse.ArgumentParser(description="Run GraRep.")
    parser.add_argument("--disease", type=str, default="Leber遗传性视神经病变,椎体爆裂骨折,呼吸性碱中毒,苍耳中毒,龋齿")
    parser.add_argument("--factor", type=str, default="Q-T间期延长,鼻出血,肢体发凉,月经周期改变,鼻端缺损")
    parser.add_argument("--task-type", type=str, default="factor_mining")
    parser.add_argument("--table-name", type=str, default="knowledgebase")
    parser.add_argument('--database-host', type=str, default='10.16.48.219')
    parser.add_argument('--database-dbname', type=str, default='software5')
    parser.add_argument('--database-port', type=str, default='5432')
    parser.add_argument('--database-password', type=str, default='111111')
    parser.add_argument('--database-user', type=str, default='pg')

    # parser.add_argument('--edge-path',
    #                     nargs='?',
    #                     default='D:/medical/图嵌入源码/graph-embedding/GraRep-master1/GraRep-master/input/dis_sys.csv',
	#                 help='Input edges.')
    #
    # parser.add_argument('--output-path',
    #                     nargs='?',
    #                     default='D:/medical/图嵌入源码/graph-embedding/GraRep-master1/GraRep-master/output/dis_sys.csv',
	#                 help='Output embedding.')

    parser.add_argument('--dimensions',
                        type=int,
                        default=16,
	                help='Number of dimensions. Default is 16.')

    parser.add_argument('--order',
                        type=int,
                        default=5,
	                help='Approximation order. Default is 5.')

    parser.add_argument('--seed',
                        type=int,
                        default=42,
	                help='Random seed. Default is 42.')

    parser.add_argument('--iterations',
                        type=int,
                        default=20,
	                help='SVD iterations. Default is 20.')

    return parser.parse_args()
