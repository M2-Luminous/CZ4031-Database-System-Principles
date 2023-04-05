from PyQt5 import QtWidgets
from PyQt5.QtWidgets import *
from PyQt5.QtGui import *
from PyQt5.QtCore import *
import psycopg2
import sys
from interface import *

#The project.py is the main file that invokes all the necessary procedures from other three files
class Database:
    def __init__(self,database_name):
        self.hostname="localhost"
        self.database=database_name
        self.username="postgres"
        self.pwd="abc123"
        self.portid=5432
        self.conn=psycopg2.connect(
        host=self.hostname,
        dbname=self.database,
        user=self.username,
        password=self.pwd,
        port=self.portid
    )
    def get_query_results(self,query):
        cur=self.conn.cursor()
        # cur.execute("explain select * from customer C, orders O where C.c_custkey = O.o_custkey and C.c_name like '%667'")
        cur.execute(query)
        return cur.fetchall()
    
    def print_explanation_results(self,queryresult):
        for i in queryresult:
            print(i[0])
    
    def print_query_results(self,queryresult):
        for i in queryresult:
            temp=""
            for j in i:
                temp+=str(j)+"|"
            print(temp)
        

    def get_conn(self):
        return self.conn

def window():
    app = QApplication(sys.argv)
    win = MyWindow()
    win.show()
    sys.exit(app.exec_())


def main():
    window()


if __name__ == '__main__':
    main()

# if __name__ == "__main__":
#     database=Database("CZ4031")
#     query="explain select * from customer C, orders O where C.c_custkey = O.o_custkey and C.c_name like '%667'"
#     print(query)
#     database.print_explanation_results(database.get_query_results(query))
#     query="select * from customer C, orders O where C.c_custkey = O.o_custkey and C.c_name like '%667'"
#     print(query)
#     database.print_query_results(database.get_query_results(query))
#     query="explain select * from customer C, orders O where C.c_custkey = O.o_custkey"
#     print(query)
#     database.print_explanation_results(database.get_query_results(query))
