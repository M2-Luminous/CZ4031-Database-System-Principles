import psycopg2

#The project.py is the main file that invokes all the necessary procedures from other three files
def main():
    hostname="localhost"
    database="CZ4031"
    username="postgres"
    pwd="admin"
    portid=5432
    conn=psycopg2.connect(
        host=hostname,
        dbname=database,
        user=username,
        password=pwd,
        port=portid
    )
    cur=conn.cursor()
    cur.execute('select * from customer C, orders O where C.c_custkey = O.o_custkey')
    print(cur.fetchall())

if __name__ == "__main__":
    main()