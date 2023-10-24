import pymysql

def insert_data(data):
    try:
        class_num = data[0]['class_number']
        class_name = data[0]['class_name']
        setdata = (class_num, class_name)

        conn = pymysql.connect(host='127.0.0.1',
                            user='root',
                            password='root',
                            db='action_db',
                            charset='utf8')
        curs = conn.cursor()

        if conn.open:
            print('connected')

        # 위험행동이면 위험행동 db에 행동이름, 시간 저장
        if class_num == 6 or class_num == 7:
            name = (class_name)
            curs.execute("INSERT INTO abnormal_action (abnormal_name) VALUES (%s)", name)

        # 저장된 행동이 있는지 확인
        sql = 'SELECT COUNT(*) FROM action'
        curs.execute(sql)
        result = curs.fetchall()
        count = result[0][0]

        # 행동이 있으면 삭제
        if count == 1:
            curs.execute("DELETE from action")

        # 현재 행동 저장
        curs.execute("INSERT INTO action (action_num, action_name) VALUES (%s, %s)", setdata)
        conn.commit()

    except Exception as e:
        print('db error:', e)
    finally:
        conn.close()

def get_data():
    try:
        conn = pymysql.connect(host='127.0.0.1',
                            user='root',
                            password='root',
                            db='action_db',
                            charset='utf8')
        curs = conn.cursor()

        if conn.open:
            print('connected')

        sql = 'SELECT * FROM action'
        curs.execute(sql)
        result = curs.fetchall()

    except Exception as e:
        print('db error:', e)

    finally:
        conn.close()
        return result[0]
    
def get_abnormal():
    try:
        conn = pymysql.connect(host='127.0.0.1',
                            user='root',
                            password='root',
                            db='action_db',
                            charset='utf8')
        curs = conn.cursor()

        if conn.open:
            print('connected')

        sql = 'SELECT * FROM abnormal_action'
        curs.execute(sql)
        result = curs.fetchall()
        print(result)

    except Exception as e:
        print('db error:', e)

    finally:
        conn.close()
        return result