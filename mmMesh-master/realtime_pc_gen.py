from B_mmWave_data_capture.steaming import adcCapThread
from datetime import datetime
from pynput import keyboard
from matplotlib import cm
from matplotlib.widgets import Slider, Button
from mpl_toolkits.mplot3d import axes3d
from requests import post

import numpy as np
import matplotlib.pyplot as plt
import matplotlib.animation as animation
import C_point_cloud_generation.configuration as cfg
import C_point_cloud_generation.pc_generation_for_realtime as pcg
import numpy as np
import preprocessing
import time
import sys
import threading
import os

BYTES_ARRAY = []
POINT_ARRAY = []
GLOBAL_TIME = 0.0
KEY_PRESSED = ''


def on_press(key):
    global KEY_PRESSED
    try : 
        if key.char == 'r'   : KEY_PRESSED = 'R'
        elif key.char == 'e' : KEY_PRESSED = 'E'
        elif key.char == 'g' : KEY_PRESSED = 'G'
        elif key.char == 'v' : KEY_PRESSED = 'V'
        elif key == keyboard.Key.esc:
            # Stop listener
            return False
    except : pass


def on_release(key):
    if key == keyboard.Key.esc:
        # Stop listener
        return False

    
def data_capture() :
    global POINT_ARRAY, BYTES_ARRAY, KEY_PRESSED, GLOBAL_TIME
    # run data collect and preprocessing
    while True :
        if KEY_PRESSED == 'R' : 
            print("Record start : " + str(datetime.now()))
            a = None
            
            a = adcCapThread(1,"adc")
            a.start()
            t = time.time()
            BYTES_ARRAY = []
    
            f = open(os.getcwd() + "/E_Datas/" + str(t).split(".")[0]+".bin", "wb")
            GLOBAL_TIME = t

            while True:
                readItem,itemNum,lostPacketFlag=a.getFrame()
                if itemNum>0:
                    f.write(readItem.tobytes())
                    BYTES_ARRAY.append(readItem)
                elif itemNum==-1:
                    print(readItem)
                elif itemNum==-2:       
                    time.sleep(0.04)
                if KEY_PRESSED == 'E' :
                    print("Record end : " + str(datetime.now()))
                    print("Record length : " + str(len(BYTES_ARRAY)))
                    print('Done')
                    a.whileSign = False 
                    f.close()
                    break
            KEY_PRESSED = None

        if KEY_PRESSED == 'G' : 
            print("Point cloud generation")
            pointCloudProcessCFG = pcg.PointCloudProcessCFG()
            shift_arr=cfg.MMWAVE_RADAR_LOC
            POINT_ARRAY = []

            for frame_no in range(len(BYTES_ARRAY)) :
                np_frame = pcg.bin2np_frame(BYTES_ARRAY[frame_no])
                pointCloud = pcg.frame2pointcloud(np_frame, pointCloudProcessCFG)
                if pointCloud.shape[0]==0 or pointCloud.shape[1]==0: # in case, there is no point in a cloud
                    q_pointcloud.put(raw_points)
                    collected_frames+=1
                    continue
                raw_points=np.transpose(pointCloud, (1,0))
                raw_points[:,:3]=raw_points[:,:3]+shift_arr
                raw_points=pcg.reg_data(raw_points, 128) # if the points number is greater than 128, just randomly sample 128 points; if the points number is less than 128, randomly duplicate some points
                POINT_ARRAY.append([GLOBAL_TIME, raw_points])
                print('Frame %d:'%(frame_no), raw_points.shape)
                # print(raw_points)
            POINT_ARRAY = np.array(POINT_ARRAY)
            np.save(os.getcwd() + "/E_Datas/" + str(GLOBAL_TIME).split(".")[0]+".npy", arr=POINT_ARRAY, allow_pickle=True)
            post_data()
            KEY_PRESSED = None

        if KEY_PRESSED == 'V' :
            print("Point cloud visualizer")
            X, Y, Z = [], [], []
            for i in range(POINT_ARRAY.shape[0]) : 
                X.append(POINT_ARRAY[:,1][i][:,0])
                Y.append(POINT_ARRAY[:,1][i][:,1])
                Z.append(POINT_ARRAY[:,1][i][:,2])
            X, Y, Z = np.array(X), np.array(Y), np.array(Z)

            # 3D Visualizer parameters
            fig = plt.figure('Visualizer', figsize=(8,6))
            ax = fig.add_subplot(projection = '3d')
            ax.scatter(X[0], Y[0], Z[0], s=50)
            fig.subplots_adjust(bottom=0.25)
            index_fig = fig.add_axes([0.25, 0.15, 0.6, 0.03])
            index_slider = Slider(
                index_fig, "Index", 0, POINT_ARRAY.shape[0]-1,
                valinit = 0, valstep=1,
                color = "green"
            )

            def update(val) :
                ax.clear()
                index = index_slider.val
                ax.scatter(X[index], Y[index], Z[index], s=50)
                fig.canvas.draw_idle()
            index_slider.on_changed(update)

            ax_reset = fig.add_axes([0.8, 0.025, 0.1, 0.04])
            button = Button(ax_reset, 'Reset', hovercolor='0.975')

            def reset(event):
                index_slider.reset()
            button.on_clicked(reset)
            plt.show()
            KEY_PRESSED = None

        def post_data():
            data = np.load(os.getcwd() + "/E_Datas/" + str(GLOBAL_TIME).split(".")[0]+".npy",allow_pickle=True)
            data = preprocessing.preProcessing(data)
            post('http://localhost:5000/predict', data=data.tostring())


if __name__=='__main__':
    # run data_capture process threading
    thread = threading.Thread(target=data_capture, args=())
    thread.start()

    # Introduce the command
    print("-----------------------------------------------------")
    print("mmWave raw data capture program (IWR1843, DCA1000EVM)")
    print("-----------------------------------------------------")
    print("1. Record Start             : press R")
    print("2. Record End               : press E")
    print("3. Point cloud generation   : press G")
    print("4. Point cloud visualize    : press V")
    print("-----------------------------------------------------")
    
    # Collect events until released
    with keyboard.Listener(
            on_press=on_press,
            on_release=on_release) as listener:
        listener.join()
