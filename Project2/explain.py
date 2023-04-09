# The explain.py contains the code for generating the explanation.

class explain:

    # def _init_(self):
    #    self.count = 0

    def explain(self, database, query1, query2):
        print("working on explain")

        def get_node_types(raw_explanation):
            node_types = []
            for Plans in raw_explanation[0][0]:
                node_types=get_node_helper(Plans['Plan'])
            return node_types
        
        def get_node_helper(raw_explanation):
            return_list=[]
            if (raw_explanation.get('Plans') is None):
                return raw_explanation['Node Type']
            for Plans in raw_explanation.get('Plans'):
                temp=get_node_helper(Plans)
                if isinstance(temp,list):
                    for i in temp:
                        return_list.append(i)
                else:
                    return_list.append(temp)
            return_list.append(raw_explanation['Node Type'])
            return return_list

        raw_explanation_query1 = database.get_query_results(
            "explain (analyze true , format json) "+query1)
        raw_explanation_query2 = database.get_query_results(
            "explain (analyze true , format json) "+query2)
        print("node types in query 1:")
        node_types_query1 = get_node_types(raw_explanation_query1)
        explanation = ','.join(node_types_query1) + \
            " are performed in query 1. \n"
        print("node types in query 2:")
        node_types_query2 = get_node_types(raw_explanation_query2)
        explanation += ','.join(node_types_query2) + \
            " are performed in query 2. \n"
        # compare node type
        print("compare query")
        nodes_in_query1 = []
        nodes_in_query2 = []
        for i in node_types_query1:
            if i not in node_types_query2:
                nodes_in_query1.append(i)
        for j in node_types_query2:
            if j not in node_types_query1:
                nodes_in_query2.append(j)
        node1 = '\n'.join(nodes_in_query1)
        node2 = '\n'.join(nodes_in_query2)
        if (node1 == "" and node2 == ""):
            explanation += "No changes are made."
        elif (node1 == ""):
            explanation += node2 + " was added in query 2."
        elif (node2 == ""):
            explanation += node1 + " in query 1 was removed."
        else:
            explanation += node1 + ' in query 1 has now evolved to ' + node2 + ' in query 2.'
        
        return explanation


def string_format(node_type):
    if node_type == 'Seq Scan':
        return "Sequential Scan"
    else:
        return node_type
        # output = ""
        # explanation = ""
        # if query["Node Type"] == 'Seq Scan':
        #    print("hit")
        #    name = query["Relation Name"]
        #    alias = query["Alias"]
        #    explanation += "a sequential scan performed in P1"
        #    if "Filter" in query:
        #        explanation += " with filter {}".format(query["Filter"])
        #    #return name, output + explanation
        #
        # elif query["Node Type"] in ['Index Scan', 'Index Only Scan']:
        #    name = query["Relation Name"]
        #    alias = query["Alias"]
        #    index = query["Index Name"]
        #    explanation += "an index scan performed in P1"
        #    if "Filter" in query:
        #        explanation += " with filter {}".format(query["Filter"])
        #    if "Index Cond" in query:
        #        explanation += " where {}".format(query["Index Cond"])
        #    #return name, output + explanation
        #
        # elif query["Node Type"] == 'CTE Scan':
        #    name = query["CTE Name"]
        #    alias = query["Alias"]
        #    explanation += "a CTE scan performed in P1"
        #    if "Filter" in query:
        #        explanation += " with filter {}".format(query["Filter"])
        #    #return name, output + explanation
        #
        # elif query["Node Type"] == 'Foreign Scan':
        #    name = query["Relation Name"]
        #    alias = query["Alias"]
        #    schema = query["Schema"]
        #    explanation += "a foreign scan performed in P1"
        #    #return name, output + explanation
        #
        # elif query["Node Type"] == 'Function Scan':
        #    function = query["Function Name"]
        #    schema = query["Schema"]
        #    alias = query["Alias"]
        #    explanation += "a function scan performed in P1"
        #    if "Filter" in query:
        #        explanation += " with filter {}".format(query["Filter"])
        #    #return name, output + explanation
        #
        # elif query["Node Type"] == 'TID Scan':
        #    name = query["Relation Name"]
        #    alias = query["Alias"]
        #    explanation += "a Tuple ID scan performed in P1"
        #    #return name, output + explanation
        #
        # elif query["Node Type"] == 'Bitmap Index Scan':
        #    name = query["Relation Name"]
        #    alias = query["Alias"]
        #    index = query["Index Name"]
        #    explanation += "a bitmap index scan performed in P1"
        #    if "Filter" in query:
        #        explanation += " with filter {}".format(query["Filter"])
        #    if "Index Cond" in query:
        #        explanation += " where {}".format(query["Index Cond"])
        #    #return name, output + explanation
        #
        # elif query["Node Type"] == 'Bitmap Heap Scan':
        #    name = query["Relation Name"]
        #    alias = query["Alias"]
        #    explanation += "a bitmap heap scan performed in P1"
        #    if "Filter" in query:
        #        explanation += " with filter {}".format(query["Filter"])
        #    if "Index Cond" in query:
        #        explanation += " where {}".format(query["Index Cond"])
        #    #return name, output + explanation
        #
        # elif query["Node Type"] == 'Nested Loop':
        #    explanation += "a nexted loop join in P1"

        # elif query["Node Type"] == 'Hash Join':
        #    explanation += "a hash join in P1"

        # elif query["Node Type"] == 'Merge Join':
        #    explanation += "a merge join in P1"

        # explanation += "has now evolved to "

        # if query2["Node Type"] == 'Seq Scan':
        #     explanation += "a sequential scan performed in P2"
        #     if "Filter" in query:
        #         explanation += " with filter {}".format(query["Filter"])

        # elif query2["Node Type"] in ['Index Scan', 'Index Only Scan']:
        #     explanation += "an index scan in P2"
        #     if "Filter" in query:
        #         explanation += " with filter {}".format(query["Filter"])
        #     if "Index Cond" in query:
        #         explanation += " where {}".format(query["Index Cond"])

        # elif query2["Node Type"] == 'CTE Scan':
        #     explanation += "a CTE scan in P2"
        #     if "Filter" in query:
        #         explanation += " with filter {}".format(query["Filter"])

        # elif query2["Node Type"] == 'Foreign Scan':
        #     explanation += "a foreign scan in P2"

        # elif query2["Node Type"] == 'Function Scan':
        #     explanation += "a function scan in P2"
        #     if "Filter" in query:
        #         explanation += " with filter {}".format(query["Filter"])

        # elif query2["Node Type"] == 'TID Scan':
        #     explanation += "a Tuple ID scan in P2"

        # elif query2["Node Type"] == 'Bitmap Index Scan':
        #     explanation += "a bitmap index scan in P2"
        #     if "Filter" in query:
        #         explanation += " with filter {}".format(query["Filter"])
        #     if "Index Cond" in query:
        #         explanation += " where {}".format(query["Index Cond"])

        # elif query2["Node Type"] == 'Bitmap Heap Scan':
        #     explanation += "a bitmap heap scan in P2"
        #     if "Filter" in query:
        #         explanation += " with filter {}".format(query["Filter"])
        #     if "Index Cond" in query:
        #         explanation += " where {}".format(query["Index Cond"])

        # elif query2["Node Type"] == 'Nested Loop':
        #     explanation += "a nexted loop join in P2"

        # elif query2["Node Type"] == 'Hash Join':
        #     explanation += "a hash join in P2"

        # elif query2["Node Type"] == 'Merge Join':
        #     explanation += "a merge join in P2"

        # explanation += "due to changes in "
