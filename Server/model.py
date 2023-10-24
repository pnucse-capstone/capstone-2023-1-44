import numpy as np
import tensorflow as tf
from tensorflow import keras

model = keras.models.load_model("./my_model.h5")
class_name = ["Lying", "Run", "SitChair", "SitFloor", "Standing", "Walking", "Groggy", "Fall"]

def test_model(data):
    data = np.reshape(data,(1,120,128,6))

    predictions = model.predict(data)
    prediction_class = np.argmax(predictions, axis=1)
    # 클래스 숫자와 이름을 한꺼번에 반환
    class_info = [{"class_number": int(class_number), "class_name": class_name[class_number]} 
                    for class_number in prediction_class]

    return class_info

