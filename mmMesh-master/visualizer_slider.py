from datetime import datetime
from pynput import keyboard
from matplotlib import cm
from matplotlib.widgets import Slider, Button
from mpl_toolkits.mplot3d import axes3d

import sys
import os
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.animation as animation

def visualize(POINT_ARRAY) :
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

if __name__=='__main__':
    
    try : 
        bin_filename='/E_Datas/' + sys.argv[1]
        POINT_ARRAY = np.load(os.getcwd() + bin_filename, allow_pickle=True)
        visualize(POINT_ARRAY)
    except : print("Error! Please check the file name")
    