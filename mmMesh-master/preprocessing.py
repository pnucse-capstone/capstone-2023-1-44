import numpy as np
import pandas as pd
import datetime
import time


time_table = []
SET_WINDOW_TIME = 5 # 0.5s

def str2time(inp) : 
    return time.mktime(datetime.datetime.strptime(inp, '%Y-%m-%d %H:%M:%S.%f').timetuple())


def slidingWindow(npy_1843_array) :
    datasetmmWave = []
    for i in range(len(npy_1843_array) - SET_WINDOW_TIME + 1) :
        temp = []
        for j in range(i, i + SET_WINDOW_TIME) :
            temp.append(npy_1843_array[j])
        datasetmmWave.append(temp)
    datasetmmWave = np.array(datasetmmWave)

    return npy_1843_array


def preProcessing(npy_1843_array) :
    iwr_temp = []
    i = 0
    # IWR6843 frames to 10Hz optimization
    while i < len(npy_1843_array) :
        start_time = int(str(npy_1843_array[i][0]).split(".")[0])
        for j in range(i, i + 10) :
            if j - i == 10 : break
            if j < len(npy_1843_array) : 
                try : 
                    if int(str(npy_1843_array[j][0]).split(".")[0]) != int(str(npy_1843_array[i][0]).split(".")[0]) :
                        if j-i < 10 :
                            for k in range(10 - (j-i)) : 
                                iwr_temp.append(iwr_temp[len(iwr_temp)-1])
                            break
                except IndexError : 
                    for k in range(10 - (j-i)) : 
                        iwr_temp.append(iwr_temp[len(iwr_temp)-1])
                        break

                iwr_temp.append(npy_1843_array[j][1])
            else : iwr_temp.append(iwr_temp[len(iwr_temp)-1])
        i = j+1

    npy_1843_array = np.array(iwr_temp)
    npy_1843_array = fit_length(npy_1843_array)

    return  npy_1843_array

# fit max length 120
def fit_length(data):
    if data.shape[0] < 120:
        data = pad_along_axis(data, 120, axis=0) 
    elif data.shape[0] >= 120:
        data = data[:120]

    return data

# pad with 0
def pad_along_axis(array: np.ndarray, target_length: int, axis: int = 0) -> np.ndarray:
    pad_size = target_length - array.shape[axis]
    if pad_size <= 0:
        return array
    npad = [(0, 0)] * array.ndim
    npad[axis] = (0, pad_size)
    return np.pad(array, pad_width=npad, mode='constant', constant_values=0)
