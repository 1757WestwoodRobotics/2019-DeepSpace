from westwood_vision_tools import *
import numpy
from publish_data import *


##################################################################################################################
# given an object list of what is supposed to be balls, this checks the list and removes objects
# from the list that are not likely to be boxes

def check_ball_object_list(list_in):

    list_out=copy.copy(list_in)

    index=0
    while index<len(list_out):

        # the box is square and should have an aspect ratio near 1
        if (list_out[index].aspect_ratio > 1.3 or list_out[index].aspect_ratio < .75) and (list_out[index].relative_area()<0.003): # require a minimum size
            list_out.pop(index)
        elif (list_out[index].aspect_ratio < 1.75 or list_out[index].aspect_ratio > 2.3) and (list_out[index].relative_area()<0.006):
            list_out.pop(index)
        else:
            index+=1

    return list_out

######################################################################################################################
# this attempts to figure out the distance to the ball in meters based on how much of the relative area it consumes,
# this is is very dependent on the camera being used

def distance_to_ball_meters(ball_object_info):

    # because several boxes could be pushed against each other, make a guess
    # that aspect ratios of 2:1, 3:1 etc. are multiple boxes in a row
    # this is very crude
    aspect_ratio=ball_object_info.aspect_ratio()

    if ((aspect_ratio>0) and (aspect_ratio<1)):
        aspect_ratio=float(1/aspect_ratio)

    aspect_ratio = round(aspect_ratio)

    if (aspect_ratio>0):
        area = ball_object_info.relative_area()
        area=float(area/aspect_ratio)
        #distance_meters = 0.429 * numpy.power(area, -0.443)
        # area should drop with the square of the distance
        distance_meters = 0.429 * numpy.power(area, -0.5)
    else:
        distance_meters=-1

    return distance_meters


######################################################################################################################
# this takes the information from the object list and reports the information for each object in the
# list to the network table

def report_object_list_to_table(object_list, count, table):

    for index in range(0, len(object_list)):
        object_info=object_list[index]
        x, y = object_info.normalized_center()
        alt, azimuth = altAzi(x, y, 22.5, 23)
        area = object_info.relative_area()
        aspect_ratio = object_info.aspect_ratio()
        distance = distance_to_ball_meters(object_info)
        object_type=object_info.object_type

        if (index==0):
            descriptor_list = ["count", "object", "number", "elevation_angle", "azimuth", "distance_m"]
            publish_network_value("attributes", descriptor_list, table)

        value_table_tag = "object_" + str(index)
        value_list = [str(count), object_type, index, alt, azimuth, distance]
        publish_network_value(value_table_tag, value_list, table)

######################################################################################################################

tar1 = []
tar3 = []
for i in range(0, 256):
    tar1.append(0.922*i-21.8)
    tar3.append(1.18*i+114)
# given the value of one color component, calculate what
# the other two should be if this is a ball

def search_for_objects(picture_in, acceleration, animate):

    picture_out=copy.copy(picture_in)

    original_rows, original_cols, layers = picture_in.shape

    # remove pixels that aren't
    chatter_size=10
    # fill out pixels that are in clusters
    engorge_size=10

    #if this is too slow, make the picture smaller
    if (acceleration>1.0):
        scaling_factor=1.0/numpy.sqrt(acceleration)
        working_picture=cv2.resize(picture_in, (0,0), fx=scaling_factor, fy=scaling_factor)
        chatter_size=int(chatter_size*scaling_factor)
        if (chatter_size<2):
            chatter_size=2
        engorge_size=int(engorge_size*scaling_factor)
        if (engorge_size<2):
            engorge_size=2
    else:
        working_picture=copy.copy(picture_in)

    working_picture=cv2.bilateralFilter(working_picture,10,150,150)

    working_rows, working_cols, layers = working_picture.shape

    run_fast=False

    if run_fast:
        mask = cv2.inRange(working_picture, (0, 100, 150), (100, 255, 255))
    else:
        # create an intial mask where evertying is false
        mask = numpy.zeros((working_rows, working_cols), numpy.uint8)

        #check each pixel and determine if its color profile is that of a box

        for row in range (0, working_rows-1, 1):
            for col in range (0, working_cols-1, 1):
                color=working_picture[row,col]

                # apply the "color" mapping for each object that you may want to find and if found
                #assign the object the object code
                # if the value of the 1st component is within the expected range
                # then check the other two color components
                if ((color[1] > 40) and (color[1] < 170) and (color[2] < 255)):
                    if (abs(color[0] - tar1[color[1]]) < 21) and (abs(color[2] - tar3[color[1]]) < 21):
                        mask[row, col] = 255  # ball is 1

                if ((color[0]>30) and (color[0]<70) and (color[1] >80) and (color[1]< 110) and (color[2] > 80) and (color[2] <120)):
                    mask[row, col] = 254  # floor tape is 2

    mask=remove_chatter(mask,chatter_size)
    mask=remove_spurious_falses(mask,engorge_size)

    if run_fast:
        object_list = find_objects_fast(mask)
    else:
        object_list = find_objects(mask, 3, animate)

    # remove items from the list that are probably just noise or not balls
    object_list=check_ball_object_list(object_list)
    object_list=remove_object_in_object(object_list)

    object_list = sort_object_info_list(object_list, 0)

    for i in object_list:
        x, y = i.normalized_center()
        alt, azimuth = altAzi(x,y,22.5,23)
        area= i.relative_area()
        aspect_ratio = i.aspect_ratio()
        distance=distance_to_ball_meters(i)
        object_type=i.object_type

        # send the object information to the network table
        # report_ball_info_to_table(i, table)

        #draw a circle around the center of the object
        abs_col=int(i.relative_center_col()*original_cols)
        abs_row=int(i.relative_center_row()*original_rows)
        abs_width=int(i.relative_width()*original_cols)
        abs_height=int(i.relative_height()*original_rows)
        if (abs_width>abs_height):
            radius=int(abs_width/2)
        else:
            radius=int(abs_height/2)

        # if the ball has an actual width
        if (radius>=1):
            if (object_type==255):
                cv2.circle(picture_out, (abs_col, abs_row), radius, (0, 0, 255), 1)
            else:
                cv2.circle(picture_out, (abs_col, abs_row), radius, (0, 255, 0), 1)


            # draw a box around the object
            min_row=int(i.relative_min_row()*original_rows)
            min_col=int(i.relative_min_col()*original_cols)
            max_row=int(i.relative_max_row()*original_rows)
            max_col=int(i.relative_max_col()*original_cols)

            if (object_type==255):
                cv2.rectangle(picture_out, (min_col, min_row), (max_col, max_row), (0, 0, 255), 2)
            else:
                cv2.rectangle(picture_out, (min_col, min_row), (max_col, max_row), (0, 255, 0), 2)

        print ("Object Type: ", object_type, "Alt: ", round(alt,2), "Azimuth: ", round(azimuth,2), "Relative Area: ", round(area,4), "Aspect Ratio: ", round(aspect_ratio,2), "Perimeter: ", i.perimeter, "Distance, m: ", round(distance,3))


    return [picture_out, object_list]




###################################################################################################

#picture = take_picture(False, 1)
#picture = cv2.imread("C:\Users/20jgrassi\Pictures\Camera Roll\edited.jpg")
table = init_network_tables()


cap = cv2.VideoCapture(0)
cap.set(cv2.CAP_PROP_SETTINGS, 1) #to fix things
cap.set(cv2.CAP_PROP_BRIGHTNESS, 30)
cap.set(cv2.CAP_PROP_EXPOSURE, -7)
cap.set(cv2.CAP_PROP_CONTRAST, 5)
cap.set(cv2.CAP_PROP_SATURATION, 83)
cap.set(cv2.CAP_PROP_FRAME_WIDTH,640)
cap.set(cv2.CAP_PROP_FRAME_HEIGHT,480)

count=0

while True:

    picture = take_picture2(cap)
    #start_time = time.time()
    processed_picture, object_list=search_for_objects(picture,10, False)
    #stop_time=time.time()
    #print(stop_time-start_time)
    report_object_list_to_table(object_list,count,table)
    show_picture("processed",processed_picture,10)

    count=count+1
s