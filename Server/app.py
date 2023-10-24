from flask import Flask, request, jsonify
import numpy as np
import model
import database

app = Flask(__name__)

@app.route('/')
def hello():
    return "hello"

@app.route('/predict', methods=['GET','POST'])
def predict():
    # POST 요청에서 데이터 추출
    data = np.frombuffer(request.data, dtype=np.float64)
    print(data.shape)
    data_received = np.frombuffer(request.data, dtype=np.float64).reshape((120,128,6))

    result = model.test_model(data_received)
    print(result)
    database.insert_data(result)

    return jsonify(result)

# 현재 행동 data 가져오기
@app.route('/get_data', methods=['GET'])
def get_data():
    # 여기서 필요한 작업 수행 (데이터 생성 또는 가져오기)
    data = database.get_data()
    # print(data[0])
    data = {"num": data[0], "action":data[1]}
    data = jsonify(data)
    print(data)
    # JSON 형식으로 데이터 반환
    return data

# 위험행동 data list 불러오기 (ID, action, time)
@app.route('/abnormal', methods=['GET'])
def get_abnormal():
    data = database.get_abnormal()

    result = []
    for i in range(len(data)):
        time = data[i][2].strftime("%Y/%m/%d %H:%M:%S")
        tmp = {
                "id": data[i][0],
                "name": data[i][1],
                "time": time
              }
        result.append(tmp)

    result = jsonify(result)

    return result

if __name__ == '__main__':
    app.run('0.0.0.0', port=5000, debug=True)