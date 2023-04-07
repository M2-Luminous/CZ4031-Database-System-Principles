#The explain.py contains the code for generating the explanation.
from interface import *
from project import *
class explain:

    #def _init_(self):
    #    self.count = 0
        
    def explain(self, database, query1, query2):
        print("working on explain")
        def get_node_types(raw_explanation):
            node_types = []
            for Plans in raw_explanation[0][0]:
              node_types.append(Plans['Plan']['Node Type'])
              for PlansIns in Plans['Plan']['Plans']:
                node_types.append(PlansIns['Node Type'])
                # if no plans skip  .. unsure how to implement.
                if (PlansIns.get('Plans') is None):
                    continue
                for x in PlansIns['Plans']:
                    node_types.append(x['Node Type'])
            print('print: print nodes in node_types')
            print(node_types)
            print('finishes')
            return node_types
        raw_explanation_query1 = database.get_query_results(
            "explain (analyze true , format json)"+query1)
        raw_explanation_query2 = database.get_query_results(
            "explain (analyze true , format json)"+query2)
        node_types_query1 = get_node_types(raw_explanation_query1)
        node_types_query2 = get_node_types(raw_explanation_query2)
        
        #compare node typeF
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
        explanation = node1 + ' in query 1 has now evolved to ' + node2 + ' in query 2'
        print(explanation)
        return explanation
        #output = ""
        #explanation = ""
        #if query["Node Type"] == 'Seq Scan':
        #    print("hit")
        #    name = query["Relation Name"]
        #    alias = query["Alias"]
        #    explanation += "a sequential scan performed in P1"
        #    if "Filter" in query:
        #        explanation += " with filter {}".format(query["Filter"])
        #    #return name, output + explanation
        #
        #elif query["Node Type"] in ['Index Scan', 'Index Only Scan']:
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
        #elif query["Node Type"] == 'CTE Scan':
        #    name = query["CTE Name"]
        #    alias = query["Alias"]
        #    explanation += "a CTE scan performed in P1"
        #    if "Filter" in query:
        #        explanation += " with filter {}".format(query["Filter"])
        #    #return name, output + explanation
        #
        #elif query["Node Type"] == 'Foreign Scan':
        #    name = query["Relation Name"]
        #    alias = query["Alias"]
        #    schema = query["Schema"]
        #    explanation += "a foreign scan performed in P1"
        #    #return name, output + explanation
        #
        #elif query["Node Type"] == 'Function Scan':
        #    function = query["Function Name"]
        #    schema = query["Schema"]
        #    alias = query["Alias"]
        #    explanation += "a function scan performed in P1"
        #    if "Filter" in query:
        #        explanation += " with filter {}".format(query["Filter"])
        #    #return name, output + explanation
        #
        #elif query["Node Type"] == 'TID Scan':
        #    name = query["Relation Name"]
        #    alias = query["Alias"]
        #    explanation += "a Tuple ID scan performed in P1"
        #    #return name, output + explanation
        #
        #elif query["Node Type"] == 'Bitmap Index Scan':
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
        #elif query["Node Type"] == 'Bitmap Heap Scan':
        #    name = query["Relation Name"]
        #    alias = query["Alias"]
        #    explanation += "a bitmap heap scan performed in P1"
        #    if "Filter" in query:
        #        explanation += " with filter {}".format(query["Filter"])
        #    if "Index Cond" in query:
        #        explanation += " where {}".format(query["Index Cond"])
        #    #return name, output + explanation
        #
        #elif query["Node Type"] == 'Nested Loop':
        #    explanation += "a nexted loop join in P1"
#
        #elif query["Node Type"] == 'Hash Join':
        #    explanation += "a hash join in P1"
#
        #elif query["Node Type"] == 'Merge Join':
        #    explanation += "a merge join in P1"
#
        #explanation += "has now evolved to "

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

        #explanation += "due to changes in "



