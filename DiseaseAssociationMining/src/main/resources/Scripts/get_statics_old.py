import psycopg2
import pandas as pd
import numpy as np
from scipy.stats import kurtosis, skew, shapiro, kstest
import json
import sys
import argparse
def analyze_table_statistics(database, user, password, host, port, table_name):
    # 连接到PostgreSQL数据库
    conn = psycopg2.connect(database=database, user=user, password=password, host=host, port=port, options='-c client_encoding=utf-8')
    cursor = conn.cursor()

    # 执行SQL查询，获取表数据
    query = f'SELECT * FROM "{tableName}";'
    cursor.execute(query)
    rows = cursor.fetchall()

    # 获取字段名
    field_names = [desc[0] for desc in cursor.description]

    # 将查询结果转换为DataFrame对象
    data_df = pd.DataFrame(rows, columns=field_names)

    # 关闭数据库连接
    cursor.close()
    conn.close()

    # 统计结果的列表
    results = []

    # 循环处理每个字段，并进行统计分析
    for column in field_names:
        selected_data = data_df[column]

        # 尝试将数据转换为数值类型
        converted_data = pd.to_numeric(selected_data, errors='coerce')

        # 如果转换后的数据包含至少一个非NA的数值，则进行统计分析
        if converted_data.notna().any():
            result = {"column_name": column}
            result["mean"] = converted_data.mean()
            result["std"] = converted_data.std()
            result["skew"] = skew(converted_data.dropna(), bias=False)
            result["kur"] = kurtosis(converted_data.dropna(), bias=False)
            # 进行Shapiro-Wilk检验
            shapiro_stats = shapiro(converted_data.dropna())
            result["Shapiro_Wilk_D"] = shapiro_stats[0]
            result["Shapiro_Wilk_p"] = shapiro_stats[1]

            # 进行Kolmogorov-Smirnov检验，注意这里使用了"norm"假设数据符合正态分布
            ks_stats = kstest(converted_data.dropna(), 'norm')
            result["Kolmogorov_Smirnov_D"] = ks_stats[0]
            result["Kolmogorov_Smirnov_p"] = ks_stats[1]
            # 将统计结果添加到列表中
            results.append(result)

    return results

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("--tablename", type=str, default=None)
    args = parser.parse_args()
    tableName = args.tablename
    results = analyze_table_statistics("software5", "pg", "111111", "10.16.48.219", "5432", tableName)
    result_str = json.dumps(results, ensure_ascii=False, indent=4)
    print(result_str)
