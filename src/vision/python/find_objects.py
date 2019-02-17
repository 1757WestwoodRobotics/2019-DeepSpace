from westwood_vision_tools import *
import numpy

from publish_data import *


##################################################################################################################
# given an object type, this generates the three color pixel value

def generate_color_table(object_type):

    index=0
    color_table=[]
    while (index<256):

        if (object_type == constant.TYPE_BALL):
            # for a ball the primary color is 3
            color_1 = int(index * 0.383 - 40.9)
            color_2 = int(index * 0.505 - 28.6)
            color_3 = int(index)
        elif (object_type == constant.TYPE_FLOOR_TAPE):
            # for floor tape the primary color is ?
            color_1 = int(index)
            color_2 = int(index * -1.42 + 140)
            color_3 = int(index * -1.25 + 196)
        elif (object_type == constant.TYPE_REFLECTIVE_TAPE):
            # for reflective tape the primary color is 2
            color_1 = int(index * 0.0237 + 86.6)
            color_2 = int(index)
            color_3 = int(index * 0 + 255)
        else:
            print("Unknown object type in generate_color_table.")
            color_1 = int(0.0)
            color_2 = int(0.0)
            color_3 = int(0.0)

        color_table.append([color_1,color_2,color_3])
        index=index+1

    return color_table

###################################################################################################
# given the there "color" pixel value and object_type, this returns a "goodness of fit" for the pixel
# where the lower the value the better the fit expect -1 means no fit

def match_pixel_to_object(color,object_type):

    fit_distance = float(-1)

    if (object_type==constant.TYPE_REFLECTIVE_TAPE):
        if ((color[1]>30 and color[1])<120):
            target=reflective_tape_color_table[color[1]]
            if ((abs(color[0] - target[0]) < 60) and (abs(color[2] - target[2]) < 10)):
                fit_distance = euclidian_distance(color, target)

    elif (object_type==constant.TYPE_FLOOR_TAPE):
        if ((color[0] > 60) and color[0] < 110 and color[1] > 20 and color[1] < 60 and color[2] > 130 and color[2] < 190):
            target=floor_tape_color_table[color[1]]
            fit_distance = euclidian_distance(color, target)

    return fit_distance



##################################################################################################################
# given an object list, check the object properties and remove objects
# from the list that are not likely to be what they appear to be

def check_object_list(list_in):

    list_out=copy.copy(list_in)

    index=0

    while index<len(list_out):

        remove_object = False
        check_object=list_out[index]

        # gather information about the object position and shape
        altitude, azimuth = check_object.altitude_and_azimuth()
        aspect_ratio=check_object.aspect_ratio()
        perimeter=check_object.perimeter

        if (perimeter<4):
            remove_object=True
        elif (check_object.object_type==constant.TYPE_FLOOR_TAPE and False):
            if (altitude > 0):
                remove_object = True
        elif (check_object.object_type==constant.TYPE_REFLECTIVE_TAPE and False):
            if (altitude < 0):
                remove_object = True

        if (remove_object):
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

def report_object_list_to_table(object_list, count, parent_table, sub_table,robot_execution):

    # post the loop count to the table even if nothing is found, this lets the
    # listening code know we still exist
    publish_network_value("count", count, sub_table)

    for index in range(0, len(object_list)):
        object_info=object_list[index]
        x, y = object_info.normalized_center()
        alt, azimuth = object_info.altitude_and_azimuth()
        area = object_info.relative_area()
        aspect_ratio = object_info.aspect_ratio()
        object_type=object_info.object_type
        perimeter=object_info.perimeter
        fit=object_info.goodness_of_fit

        if (object_type==constant.TYPE_REFLECTIVE_TAPE):
            distance_m, x=distance_from_elevation(alt,constant.REFLECTIVE_TAPE_HEIGHT_M-constant.CAMERA_HEIGHT_M)
        elif (object_type==constant.TYPE_BALL):
            distance_m = distance_to_ball_meters(object_info)
        else:
            distance_m = 0


        if (index==0):
            descriptor_list = ["count", "object", "number", "elevation_angle", "azimuth", "distance_m"]
            publish_network_value("attributes", descriptor_list, parent_table)

        # send the object information
        value_table_tag = "object_" + str(index)
        value_list = [str(count), object_type, index, alt, azimuth, distance_m]
        publish_network_value(value_table_tag, value_list, parent_table)

        if (robot_execution==False):
            string_to_print="Object Type: " + str(object_type)
            string_to_print=string_to_print + " Azimuth: " + str(round(azimuth,2))
            string_to_print=string_to_print + " Alt: " + str(round(alt,2))
            string_to_print=string_to_print + " Rel. Area: " + str(round(area,5))
            string_to_print=string_to_print + " Aspect Ratio: " + str(round(aspect_ratio,2))
            string_to_print=string_to_print + " Perimeter: " + str(perimeter)
            string_to_print=string_to_print + " Distance, m: " + str(round(distance_m,3))
            string_to_print=string_to_print + " Fit: " + str(round(fit,3))
            print (string_to_print)


######################################################################################################################
#This reads the network table and gets the String[] of items we are currently searching for, and uses that to update
#the list of items we are currently searching for
#Last updated 1/26/19 by Bryce Parazin

def read_object_list_from_table(Network_table):

    visionTable = Network_table.getEntry("vision_types")
    objectStringArray = visionTable.value
    objectIntArray = []

    if objectStringArray!=None:
        for x in range(len(objectStringArray)):
           for y in range(len(constant.TYPE_LIST_STRING)):
               if objectStringArray[x]==constant.TYPE_LIST_STRING[y]:
                   objectIntArray.append(constant.TYPE_LIST_INT[y])

   # print(objectIntArray)

    return objectIntArray

######################################################################################################################
# given a picture and a list of objects to look for, this searches the picture comparing the pixel values against
# the "color" table for each object

def search_for_objects(picture_in, acceleration, animate, objects_to_find, robot_execution):

    # use HSV for the image search
    if (robot_execution):
        working_picture = cv2.cvtColor(picture_in, cv2.cv.CV_BGR2HSV)
    else:
        working_picture=cv2.cvtColor(picture_in, cv2.COLOR_BGR2HSV)

    #show_picture("HSV", working_picture, 10)

 #   original_rows, original_cols, layers = picture_in.shape

    # remove pixels that aren't
    chatter_size=2
    # fill out pixels that are in clusters
    engorge_size=4

    #if this is too slow, make the picture smaller
    if (acceleration>1.0):
        scaling_factor=1.0/numpy.sqrt(acceleration)
        working_picture=cv2.resize(working_picture, (0,0), fx=scaling_factor, fy=scaling_factor)
        chatter_size=int(chatter_size*scaling_factor)
        if (chatter_size<2):
            chatter_size=2
        engorge_size=int(engorge_size*scaling_factor)
        if (engorge_size<2):
            engorge_size=2
    else:
        working_picture=copy.copy(working_picture)

    working_picture=cv2.bilateralFilter(working_picture,5,50,50)

    working_rows, working_cols, layers = working_picture.shape

    run_fast=False

    if run_fast:
        mask = cv2.inRange(working_picture, (0, 100, 150), (100, 255, 255))
    else:
        # create an initial mask where everything is 0 (i.e. nothing found)
        mask = numpy.zeros((working_rows, working_cols), numpy.uint8)

        # keep track of how well each pixel fits an object, -1 means no object
        goodness_of_fit = numpy.full((working_rows, working_cols), -1, numpy.float)

        # check each pixel and determine if its color profile is that of an object
        # since this searches for multiple objects, you have to keep track of
        # what object is the best fit, so compare the current fit of the against
        # the best so far
        best_fit=1e6

        # don't search the edges of the image, having "true" pixels along the
        # edge messes up the routines extract the blobs from the picture
        for row in range (constant.SEARCH_RADIUS, working_rows-constant.SEARCH_RADIUS, 1):
            for col in range (constant.SEARCH_RADIUS, working_cols-constant.SEARCH_RADIUS, 1):

                # searh for reflective tape
                if (constant.TYPE_REFLECTIVE_TAPE in objects_to_find):
                    current_fit=match_pixel_to_object(working_picture[row,col],constant.TYPE_REFLECTIVE_TAPE)
                    if (0<=current_fit< best_fit):
                        best_type=int(constant.TYPE_REFLECTIVE_TAPE)
                        best_fit=current_fit

                if (constant.TYPE_FLOOR_TAPE in objects_to_find):
                    current_fit=match_pixel_to_object(working_picture[row,col],constant.TYPE_FLOOR_TAPE)
                    if (0<=current_fit<best_fit):
                        best_type=int(constant.TYPE_FLOOR_TAPE)
                        best_fit=current_fit

                # if there has been a match, set the mask to the best fit
                if (best_fit<1e6):
                    mask[row, col] = int(best_type)
                    goodness_of_fit[row,col]=best_fit
                    best_fit=1e6

    # show_picture("HSV", mask, 10)

    # remove "found" pixels that are most likely isolated noise
    if (chatter_size>1):
        mask=remove_chatter(mask,chatter_size)

   #  show_picture("HSV", mask, 10)

    if run_fast:
        object_list = find_objects_fast(mask)
    else:
        object_list = find_objects(mask, goodness_of_fit, constant.SEARCH_RADIUS, animate)

    # remove items from the list that are probably just noise and not actual target objects
    object_list=check_object_list(object_list)

    # remove objects that are entirely surrounded by other objects
    object_list=remove_object_in_object(object_list)

    object_list = sort_object_info_list(object_list, constant.SORT_AZIMUTH_CENTER)

    return object_list

################################################################################################################################################
# this superimposes boxes and circles around found objects

def outline_objects(picture_in, object_list):

    picture_out=copy.copy(picture_in)

    original_rows, original_cols, layers = picture_in.shape

    for object in object_list:
        x, y = object.normalized_center()
        object_type=object.object_type

        #draw a circle around the center of the object
        abs_col=int(object.relative_center_col()*original_cols)
        abs_row=int(object.relative_center_row()*original_rows)
        abs_width=int(object.relative_width()*original_cols)
        abs_height=int(object.relative_height()*original_rows)
        if (abs_width>abs_height):
            radius=int(abs_width/2)
        else:
            radius=int(abs_height/2)

        # if the object has an actual width
        if (radius>=1):

            if (object_type == constant.TYPE_REFLECTIVE_TAPE):
                color=(0,0,255)
            elif (object_type==constant.TYPE_FLOOR_TAPE):
                color=(0,255,0)
            elif (object_type==constant.TYPE_HATCH_COVER):
                color=(255,0,0)
            else:
                color=(255,0,255)

            cv2.circle(picture_out, (abs_col, abs_row), radius, color, 1)

            # draw a box around the object
            min_row=int(object.relative_min_row()*original_rows)
            min_col=int(object.relative_min_col()*original_cols)
            max_row=int(object.relative_max_row()*original_rows)
            max_col=int(object.relative_max_col()*original_cols)

            cv2.rectangle(picture_out, (min_col, min_row), (max_col, max_row), color, 1)

    return (picture_out)

################################################################################################################################################
# if given a test picture, this searches the picture for object.  If test picture is None, it takes pictures
# from the camera, test_picture allows for testing the routine against a consistent image
# acceleration attempts to speed up the processing by shrinking the picture, acceleration == 1 means no
# acceleration, 2 means twice as fast, 3 three times as fast....

def this_is_it(test_picture, camera_number,acceleration_factor, robot_execution):

    #picture = take_picture(False, 1)
    #picture = cv2.imread("C:\Users/20jgrassi\Pictures\Camera Roll\edited.jpg")

    # setup the communication with the network tables
    # this is how the code communicates with the robot
    parent_table, sub_table = init_network_tables()

    # keep track of how many images have been processed
    # this is used as an ID number so someone processing
    # the information knows if the data has been updated
    count=0

    # if not using a test picture, configure the camera
    #if (test_picture!=None):
    cap=configure_camera(camera_number,robot_execution)

    start_time = time.time()
    while True:

        if (test_picture==None):
            picture = take_picture2(cap)
        else:
            picture=test_picture

        # read the network table to find out what the robot wants the code to look for
        # this returns a list of object type numbers
        if (robot_execution==True):
            objects_to_find = read_object_list_from_table(sub_table)
        else:
            objects_to_find = [constant.TYPE_REFLECTIVE_TAPE, constant.TYPE_FLOOR_TAPE]

        # search the picture for objects
        object_list=search_for_objects(picture, acceleration_factor, False, objects_to_find,robot_execution)

        # superimpose boxes around the found objects in the picture
        processed_picture= outline_objects(picture,object_list)

        # post the object information to the network table, this is how the code communicates
        # with the robot
        report_object_list_to_table(object_list,count,parent_table,sub_table,robot_execution)

        #   print(time.time()-start_time)/(count+1)

        if (robot_execution == False):
            show_picture("processed",processed_picture,10)

        # increment the image count
        count=count+1


##############################################################################################################


# generate the color tables for each object
floor_tape_color_table      = generate_color_table(constant.TYPE_FLOOR_TAPE)
reflective_tape_color_table = generate_color_table(constant.TYPE_REFLECTIVE_TAPE)


# parameters are test picture or None if using the camera
# camera number
# acceleration factor, 1== no acceleration
# robot execution True or False
this_is_it(None,1,1,False)