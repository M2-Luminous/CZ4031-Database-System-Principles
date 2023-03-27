#The explain.py contains the code for generating the explanation.
class explain:

    def _init_(self):
        self.count = 0
        
    def explain(self, query, query2):
        output = ""
        if query["Node Type"] == 'Seq Scan':
            name = query["Relation Name"]
            alias = query["Alias"]
            explanation = "a sequential scan performed in P1"
            if "Filter" in query:
                explanation += " with filter {}".format(query["Filter"])
            return name, output + explanation
        elif query["Node Type"] in ['Index Scan', 'Index Only Scan']:
            name = query["Relation Name"]
            alias = query["Alias"]
            index = query["Index Name"]
            explanation = "an index scan performed in P1"
            if "Filter" in query:
                explanation += " with filter {}".format(query["Filter"])
            if "Index Cond" in query:
                explanation += " where {}".format(query["Index Cond"])
            return name, output + explanation
        elif query["Node Type"] == 'CTE Scan':
            name = query["CTE Name"]
            alias = query["Alias"]
            explanation = "a CTE scan performed in P1"
            if "Filter" in query:
                explanation += " with filter {}".format(query["Filter"])
            return name, output + explanation
        elif query["Node Type"] == 'Foreign Scan':
            name = query["Relation Name"]
            alias = query["Alias"]
            schema = query["Schema"]
            explanation = "a foreign scan performed in P1"
            return name, output + explanation
        elif query["Node Type"] == 'Function Scan':
            function = query["Function Name"]
            schema = query["Schema"]
            alias = query["Alias"]
            explanation = "a function scan performed in P1"
            if "Filter" in query:
                explanation += " with filter {}".format(query["Filter"])
            return name, output + explanation
        elif query["Node Type"] == 'TID Scan':
            name = query["Relation Name"]
            alias = query["Alias"]
            explanation = "a Tuple ID scan performed in P1"
            return name, output + explanation
        elif query["Node Type"] == 'Bitmap Index Scan':
            name = query["Relation Name"]
            alias = query["Alias"]
            index = query["Index Name"]
            explanation = "a bitmap index scan performed in P1"
            if "Filter" in query:
                explanation += " with filter {}".format(query["Filter"])
            if "Index Cond" in query:
                explanation += " where {}".format(query["Index Cond"])
            return name, output + explanation
        elif query["Node Type"] == 'Bitmap Heap Scan':
            name = query["Relation Name"]
            alias = query["Alias"]
            explanation = "a bitmap heap scan performed in P1"
            if "Filter" in query:
                explanation += " with filter {}".format(query["Filter"])
            if "Index Cond" in query:
                explanation += " where {}".format(query["Index Cond"])
            return name, output + explanation