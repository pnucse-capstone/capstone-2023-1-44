from steaming import adcCapThread
from datetime import datetime
import time
import sys
DURATION_TIME = 10

if __name__=='__main__':
    print('\a')
    print("Record start : " + str(datetime.now()))

    a = adcCapThread(1,"adc")
    a.start()
    counter = 0
    t = time.time()
    time_min = 0
    
    f = open(str(t).split(".")[0]+".bin", "wb")
    
    try : 
        duration=int(sys.argv[1])
    except : 
        duration=DURATION_TIME
    print("Set duration time : " + str(duration))
    
    while True:
        readItem,itemNum,lostPacketFlag=a.getFrame()
        if itemNum>0:
            counter+=1
            f.write(readItem.tobytes())
            if counter == 10:
                counter = 0
                time_min+=1        
            
        elif itemNum==-1:
            print(readItem)
        elif itemNum==-2:       
            time.sleep(0.04)
        if time_min>=duration:    
           a.whileSign = False 
           f.close()
           break
    print('\a')
    print("Record end : " + str(datetime.now()))
    print('Done')
    print('\a')
