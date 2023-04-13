# The explain.py contains the code for generating the explanation.
import copy
class explain:

    # This function generates an explanation of the differences between two input queries
    def explain(self, database, query1, query2):
        print("working on explain")

        # This function gets node types and execution time from the query execution plan
        def get_node_types(raw_explanation):
            node_types_time = []
            node_types = []
            for Plans in raw_explanation[0][0]:
                if 'Limit' in Plans['Plan']['Node Type']:
                    node_types_time, node_types=get_node_helper(Plans['Plan']['Plans'][0])
                    node_types_time[-1]='Limit\n'+node_types_time[-1]
                    node_types[-1]='Limit\n'+node_types[-1]
                # call the recursive function
                else:
                    node_types_time, node_types=get_node_helper(Plans['Plan'])
            return node_types_time, node_types
        
        # This recursive function gets the node types and execution time in each depth
        def get_node_helper(raw_explanation):
            return_list_time=[]
            return_list=[]
            add_to_list_time=[]
            add_to_list=[]
            # return if there's no child node
            if (raw_explanation.get('Plans') is None):
                return [raw_explanation['Node Type']+' on '+raw_explanation['Alias']+' ('+str(raw_explanation['Actual Total Time'])+'miliseconds)'], [raw_explanation['Node Type']+' on '+raw_explanation['Alias']]
            # if raw_explanation.get('Plans') is not None
            for Plans in raw_explanation.get('Plans'):
                temp, temp2=get_node_helper(Plans)
                if isinstance(temp,list):
                    # add previous node types and execution time to list
                    for i in temp:
                        return_list_time.append(i)
                    # add previous node types to list
                    for i in temp2:
                        return_list.append(i)
                add_to_list_time.append(temp[-1].replace('\n','\n    '))
                add_to_list.append(temp2[-1].replace('\n','\n    '))
            # add node type and execution time to list
            if len(add_to_list_time)==2:
                return_list_time.append(raw_explanation['Node Type']+' ('+str(raw_explanation['Actual Total Time'])+'miliseconds)'+' on '+'\n    ->'+add_to_list_time[0]+'\n    ->'+add_to_list_time[1])
                return_list.append(raw_explanation['Node Type']+' on \n    ->'+add_to_list[0]+'\n    ->'+add_to_list[1])
            else:  
                return_list_time.append(raw_explanation['Node Type']+' ('+str(raw_explanation['Actual Total Time'])+'miliseconds)'+' on '+'\n    ->'+add_to_list_time[0])
                return_list.append(raw_explanation['Node Type']+' on \n    ->'+add_to_list[0])
            return return_list_time, return_list
        

        # get the depth of query plan    
        def check_depth(query):
            max_depth=0
            for i in query:
                if "->"in i:
                    spaces=i.split("->")[0]
                    if len(spaces)/4+1>max_depth:
                        max_depth=len(spaces)/4+1
            return int(max_depth)

        # get raw explanation of query 1
        raw_explanation_query1 = database.get_query_results(
            "explain (analyze true , format json) "+query1)
        # get raw explanation of query 2
        raw_explanation_query2 = database.get_query_results(
            "explain (analyze true , format json) "+query2)
        # add to explanation the operations performed in query 1
        print("node types in query 1:")
        node_types_time_query1, node_types_query1 = get_node_types(raw_explanation_query1)
        explanation="These operations are performed on query 1:\n"
        explanation += node_types_time_query1[-1] + "\n\n"
        # add to explanation the operations performed in query 2
        print("node types in query 2:")
        node_types_time_query2, node_types_query2 = get_node_types(raw_explanation_query2)
        explanation +="These operations are performed on query 2:\n"
        explanation += node_types_time_query2[-1] + "\n\n"
        # compare node type of two queries
        print("compare query")
        nodes_in_query1 = []
        nodes_in_query2 = []
        split_node_types_query1=node_types_query1[-1].split('\n')
        split_node_types_query2=node_types_query2[-1].split('\n')
        # get the operations unique to query 1
        for i in split_node_types_query1:
            if i not in split_node_types_query2:
                nodes_in_query1.append(i)
        # get the operations unique to query 2
        for j in split_node_types_query2:
            if j not in split_node_types_query1:
                nodes_in_query2.append(j)
        # convert list to string
        node1 = '\n'.join(nodes_in_query1)
        node2 = '\n'.join(nodes_in_query2)
        # describe changes from query 1 to query 2
        # if no unique operations in two queries
        if (node1 == "" and node2 == ""):
            explanation += "No changes are made."
        # if no unique operations in query 1
        elif (node1 == ""):
            explanation += node2 + "\nwas added in query 2\n"
        # if no unique operations in query 1
        elif (node2 == ""):
            explanation += node1 + "\nwas added in query 1\n"
        # if unique operations in both query 1 and queryl 2
        else:
            explanation += node1 + '\nin query 1 has now evolved to \n' + node2 + '\n in query 2.\n\n'
        

        # add to explanation changes on the depth of the query plan
        if check_depth(split_node_types_query1)!=check_depth(split_node_types_query2):
            explanation += 'Query 1 has a depth of '+str(check_depth(split_node_types_query1))+' while Query 2 has a depth of '+str(check_depth(split_node_types_query2))+'.\n\n'
        

        # add explanation for Gather operation
        for i in split_node_types_query1:
            print(i)
            if ("Gather" in i):
                explanation += "\nGather because parallel execution was used for Query 1"
            if ("Limit" in i):
                explanation += "\nLimit because number of tuples output is limited for Query 1\n"
            
        for i in split_node_types_query2:
            if ("Gather" in i):
                explanation += "Gather because parallel execution was used for Query 2"
            if ("Limit" in i):
                explanation += "\nLimit because number of tuples output is limited for Query 2\n"

        same_operation_diff_depth=[]
        temp_nodes_in_query1=copy.deepcopy(nodes_in_query1)
        temp_nodes_in_query2=copy.deepcopy(nodes_in_query2)
        for i in temp_nodes_in_query1:
            for j in temp_nodes_in_query2:                
                try:
                    temp1=i.split('->')[1]
                    temp2=j.split('->')[1]
                except:
                    continue
                if (temp1==temp2) and temp1 not in same_operation_diff_depth:
                    nodes_in_query1.remove(i)
                    nodes_in_query2.remove(j)
                    same_operation_diff_depth.append(temp1)
                    explanation += temp1 + ' in Query 1 executed at the depth of '+str(check_depth([i]))+' while this is executed in the depth of '+str(check_depth([j]))+' in Query 2.\n'
                    break
        

        
        
        # add to explanation unique operations in query 1
        if len(nodes_in_query1)!=0:
            explanation+="\nThese operations are unique to query 1:\n"  
            for i in nodes_in_query1:
                explanation+=i+'\n'
        # add to explanation unique operations in query 2
        if len(nodes_in_query2)!=0:
            explanation+="\nThese operations are unique to query 2:\n"
            for i in nodes_in_query2:
                explanation+=i+'\n'
        return explanation
    


def string_format(node_type):
    if node_type == 'Seq Scan':
        return "Sequential Scan"
    else:
        return node_type
