# The explain.py contains the code for generating the explanation.

class explain:

    # def _init_(self):
    #    self.count = 0




    def explain(self, database, query1, query2):
        print("working on explain")

        def get_node_types(raw_explanation):
            node_types_time = []
            node_types = []
            for Plans in raw_explanation[0][0]:
                node_types_time, node_types=get_node_helper(Plans['Plan'])
            return node_types_time, node_types
        
        def get_node_helper(raw_explanation):
            return_list_time=[]
            return_list=[]
            add_to_list_time=[]
            add_to_list=[]
            if (raw_explanation.get('Plans') is None):
                return [raw_explanation['Node Type']+' on '+raw_explanation['Alias']+' ('+str(raw_explanation['Actual Total Time'])+'miliseconds)'], [raw_explanation['Node Type']+' on '+raw_explanation['Alias']]
            for Plans in raw_explanation.get('Plans'):
                temp, temp2=get_node_helper(Plans)
                if isinstance(temp,list):
                    for i in temp:
                        return_list_time.append(i)
                    for i in temp2:
                        return_list.append(i)
                add_to_list_time.append(temp[-1].replace('\n','\n    '))
                add_to_list.append(temp2[-1].replace('\n','\n    '))
            return_list_time.append(raw_explanation['Node Type']+' ('+str(raw_explanation['Actual Total Time'])+'miliseconds)'+' on '+'\n    ->'+add_to_list_time[0]+'\n    ->'+add_to_list_time[1])
            return_list.append(raw_explanation['Node Type']+' on \n    ->'+add_to_list[0]+'\n    ->'+add_to_list[1])
            return return_list_time, return_list
        
            
        def check_depth(query):
            max_depth=0
            for i in query:
                if "->"in i:
                    spaces=i.split("->")[0]
                    if len(spaces)/4+1>max_depth:
                        max_depth=len(spaces)/4+1
            return int(max_depth)

        raw_explanation_query1 = database.get_query_results(
            "explain (analyze true , format json) "+query1)
        raw_explanation_query2 = database.get_query_results(
            "explain (analyze true , format json) "+query2)
        print("node types in query 1:")
        node_types_time_query1, node_types_query1 = get_node_types(raw_explanation_query1)
        explanation="These operations are performed on query 1:\n"
        explanation += node_types_time_query1[-1] + "\n\n"
        print("node types in query 2:")
        node_types_time_query2, node_types_query2 = get_node_types(raw_explanation_query2)
        explanation +="These operations are performed on query 2:\n"
        explanation += node_types_time_query2[-1] + "\n\n"
        # compare node type
        print("compare query")
        nodes_in_query1 = []
        nodes_in_query2 = []
        split_node_types_query1=node_types_query1[-1].split('\n')
        split_node_types_query2=node_types_query2[-1].split('\n')
        for i in split_node_types_query1:
            if i not in split_node_types_query2:
                nodes_in_query1.append(i)
        for j in split_node_types_query2:
            if j not in split_node_types_query1:
                nodes_in_query2.append(j)
        node1 = '\n'.join(nodes_in_query1)
        node2 = '\n'.join(nodes_in_query2)
        if (node1 == "" and node2 == ""):
            explanation += "No changes are made."
        elif (node1 == ""):
            explanation += node2 + "\n was added in query 2"
            for i in nodes_in_query2:
                if (i == "Gather"):
                    explanation += " because parallel execution was enabled"
        elif (node2 == ""):
            explanation += node1 + "\n in query 1 was removed"
            for i in nodes_in_query1:
                if (i == "Gather"):
                    explanation += " because parallel execution was disabled"
        else:
            explanation += node1 + '\n in query 1 has now evolved to \n' + node2 + '\n in query 2.\n\n'
        
        explanation += 'Query 1 has a depth of '+str(check_depth(split_node_types_query1))+' while Query 2 has a depth of '+str(check_depth(split_node_types_query2))

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
