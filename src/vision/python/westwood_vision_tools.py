# file last changed on 10February2018
import numpy
import cv2
import time
import copy
import PyWinMouse
import math


class constant_class():
    TYPE_BALL =            int(70)
    TYPE_FLOOR_TAPE =      int(90)
    TYPE_REFLECTIVE_TAPE = int(100)
    TYPE_HATCH_COVER =     int(120)

    # this is used in to translate the network tables from the robot that tells the vision
    # code what objects to look for
    TYPE_LIST_INT = [TYPE_BALL, TYPE_FLOOR_TAPE, TYPE_REFLECTIVE_TAPE, TYPE_HATCH_COVER]
    TYPE_LIST_STRING = ["CARGO", "TAPE", "TARGET", "PANEL"]  # This assumes that the list is all caps

    # camera half angle from center pixel to edge
    HORIZONTAL_HALF_ANGLE =     26.4
    VERTICAL_HALF_ANGLE =       26.4

    SORT_RELATIVE_AREA  = 0
    SORT_ASPECT_RATIO   = 1
    SORT_PERIMETER      = 2
    SORT_AZIMUTH_CENTER = 3
    SORT_OBJECT_TYPE    = 4

constant=constant_class()


#######################################################################################################################

def show_picture(title, picture, time_msec):

    cv2.imshow(title, picture)
    cv2.waitKey(time_msec)


#######################################################################################################################

#Takes original picture, in color. Two input parameters are
# 'show', a boolean that determines whether the image will be shown,
# and 'device_number', an integer that determines which device to use to take the picture.

def take_picture(show, device_number):

    cap = cv2.VideoCapture(device_number)
    ret, picture = cap.read()

    if (show==True):
        cv2.imshow("take picture", picture)
        cv2.waitKey(3000)
        cap.release()

    return picture

##################################################################################################################
'''takes picture'''

def take_picture2(stream):

    ret, picture = stream.read()

    return picture

#######################################################################################################################
# Given a row and column input, and a list of row/column pairs, this returns the index of the element in the list
# which is closest to the input row/column.

def closest(row, col, coordinates_list):

    close_index=-1
    closest_so_far=999999999999999999999999

    for list_index in range (0,len(coordinates_list),1):
        check_row=coordinates_list[list_index][0]
        check_col=coordinates_list[list_index][1]
        distance = numpy.sqrt((check_row-row)**2 + (check_col-col)**2)
        if distance<closest_so_far:
            closest_so_far=distance
            close_index=list_index

    return close_index

#######################################################################################################################
# this hasn't been tested

def create_circular_mask(radius, value_to_set):

    Y, X = numpy.ogrid[:radius*2, :radius*2]
    dist_from_center = numpy.sqrt((X - radius)**2 + (Y-radius)**2)

    # set to true any coordinate that's within the radius
    mask = dist_from_center <= radius

    # convert the true/false to 0/set_value
    mask=mask*value_to_set

    return mask

#######################################################################################################################
#Given a picture reduced to true/false values (0=false,  not zero=true), and a row/col coordinate that describes a coordinate
# in said picture, and a search radius in pixels, this returns a list of all the row/col pairs
# within that radius that have the same object type value. It does not return the original input pixel.

def in_range(picture, object_type, row, col, radius):

    rows, cols = picture.shape
    coords_to_check=[]
    pixels_found_list=[]

    create_circular_mask(radius,object_type)

    for check_row in range(row-radius, row+radius, 1):
        for check_col in range(col-radius, col+radius, 1):
            # makes sure pixel is within bounds of picture
            if ((check_row >= 0) and (check_col>=0) and (check_row<rows) and (check_col<cols)):
                distance = numpy.sqrt((row - check_row) ** 2 + (col - check_col) ** 2)
                if distance<=radius+0.5:
                     coords_to_check.append(copy.copy([check_row, check_col]))

    for list_index in range(0, len(coords_to_check), 1):
        check_row = coords_to_check[list_index][0]
        check_col = coords_to_check[list_index][1]

        if (picture[check_row, check_col]==object_type) and not(check_row==row and check_col==col):
            pixels_found_list.append(copy.copy([check_row, check_col]))

    return pixels_found_list

#######################################################################################################################
#Given a picture reduced to true/false values (0=false, not 0 =true), and a row/col coordinate that describes a coordinate
# in said picture, and a search radius in pixels, this sets all pixels within the radius of the original coordinate to
# false.

def obliterate(picture, row, col, radius):

    rows, cols = picture.shape

    return_picture = copy.copy(picture)

#creates a list of all the coordinates to check
    for check_row in range(row-radius, row+radius, 1):
        for check_col in range(col - radius, col + radius, 1):
             #makes sure pixel is within bounds of picture
             if ((check_row<rows) and (check_col<cols)) and (check_row>=0 and check_col>=0):
                 distance = numpy.sqrt((row - check_row) ** 2 + (col - check_col) ** 2)
                 if distance<=radius:
                     return_picture[check_row, check_col]=0

    return return_picture

#######################################################################################################################

# Given a picture reduced to true/false values (0=false, not 0 =true), this sets all pixels otherwise surrounded
# by the same number to 0
# pixels to false. Does not check the edges because that fringe problem is
# probably not worth spending a bunch of time and cpu to fix.
# there has to be a faster way to do this with masks

def hollow_out(picture):

     working=copy.copy(picture)

#    this doesn't work if you encode the object values in the image, it changes the values
#    kernel=numpy.ones((2,2),numpy.uint8)
#    working=cv2.morphologyEx(working,cv2.MORPH_GRADIENT,kernel)

     rows, cols = working.shape

     #     for row in range(1, rows - 2, 1):
     #         for col in range(1, cols - 2, 1):
     #             value= picture[row,col]
     #             if (value!=0):
     #                 mask=numpy.full((3,3),value)
     #                 test=picture[row-1:row+2,col-1:col+2]
     #                 diff=test-mask
     #                 if (not numpy.any(diff)):
     #                     working[row,col]=0

     for row in range(1, rows - 2, 1):
        for col in range(1, cols - 2, 1):
            if (picture[row,col]!=0):
                test=picture[row-1:row+2,col-1:col+2]
                diff=[test==picture[row,col]]
                if (numpy.all(diff)):
                    working[row,col]=0

 #    for row in range(1, rows - 2, 1):
 #        for col in range(1, cols - 2, 1):
 #            value = picture[row, col]
 #            if (value!=0):
 #               if (picture[row,col-1] == value and picture[row,col+1] == value and picture[row-1,col] == value and picture[row-1, col-1] == value and picture[row-1,col+1] == value and picture[row+1, col] == value and picture[row+1,col-1] == value and picture[row+1,col+1]==value):
 #                  working[row, col] = 0

#     for row in range(1, rows - 2, 1):
#         for col in range(1, cols - 2, 1):
#             value = picture[row, col]
#             if (value != 0):
#                 if(picture[row,col-1]==value):
#                     if(picture[row,col+1]==value):
#                         if(picture[row-1,col]==value):
#                            if(picture[row-1,col-1]==value):
#                                 if(picture[row-1,col+1]==value):
#                                    if(picture[row+1,col]==value):
#                                       if(picture[row+1,col-1]==value):
#                                         if(picture[row+1,col+1]==value):
#                                             working[row, col] = 0



     return working

#######################################################################################################################
# this class records the information associate with an object

class object_info_class(object):
        def __init__(self):
            self.source_dimensions=[0,0]
            # the center of the object in row, col coordinates
            self.center_RC = [0.0,0.0]
            self.perimeter = 0
            self.max_row = [0,0]
            self.max_col = [0,0]
            self.min_row = [0,0]
            self.min_col = [0,0]
            self.object_type=0

        # this returns the center coordinates of the object normalized to the dimensions of the image
        # in cartesian coordinates.  The center of the image is 0,0.  The upper right hand corner is 1,1
        # the lower left hand corner is -1, -1
        def normalized_center(self):
            norm_x=float(self.center_RC[1]-(1.0*self.source_dimensions[1]/2))
            norm_x=float(norm_x/(1.0*self.source_dimensions[1]/2))

            norm_y=float((1.0*self.source_dimensions[0]/2) - self.center_RC[0])
            norm_y=float(norm_y/(1.0*self.source_dimensions[0]/2))

            return norm_x, norm_y

        def relative_area(self):
            return float((self.max_row[0]-self.min_row[0]+1)*(self.max_col[1]-self.min_col[1]+1)/(1.0*self.source_dimensions[0]*self.source_dimensions[1]))

        def aspect_ratio(self):
            return float(self.max_row[0]-self.min_row[0]+1)/(self.max_col[1]-self.min_col[1]+1)

        def relative_width(self):
            return float((self.max_col[1] - self.min_col[1] + 1) / (1.0 * self.source_dimensions[1]))

        def relative_height(self):
            return float((self.max_row[0] - self.min_row[0] + 1) / (1.0 * self.source_dimensions[0]))

        def relative_center_row(self):
            return float(1.0*self.center_RC[0]/self.source_dimensions[0])

        def relative_center_col(self):
            return float(1.0*self.center_RC[1]/self.source_dimensions[1])

        def relative_min_row(self):
            return float(1.0*self.min_row[0]/self.source_dimensions[0])

        def relative_min_col(self):
            return float(1.0*self.min_col[1]/self.source_dimensions[1])

        def relative_max_row(self):
            return float(1.0*self.max_row[0]/self.source_dimensions[0])

        def relative_max_col(self):
            return float(1.0*self.max_col[1]/self.source_dimensions[1])

        def altitude_and_azimuth(self):
            norm_x,norm_y = self.normalized_center()
            return (altitude_and_azimuth(norm_x, norm_y, constant.HORIZONTAL_HALF_ANGLE, constant.VERTICAL_HALF_ANGLE))


#######################################################################################################################
# this is a faster version of find_objects

def find_objects_fast(picture):

    object_info = object_info_class()
    object_info_list = []

    rows, cols = picture.shape

    # find the contours - regions of white - in the image
    im2, contours, hierarchy = cv2.findContours(picture, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    for contour in contours:
        object_info.source_dimensions = [rows, cols]
        object_info.perimeter=cv2.arcLength(contour,True)

        object_info.max_row=tuple(contour[contour[:,:,1].argmax()][0])
        object_info.max_col=tuple(contour[contour[:,:,0].argmax()][0])
        object_info.min_row=tuple(contour[contour[:,:,1].argmin()][0])
        object_info.min_col=tuple(contour[contour[:,:,0].argmin()][0])

        x, y, w, h = cv2.boundingRect(contour)
        object_info.center_RC = [y + (h / 2), x + (w / 2)]

        object_info_list.append(object_info)

    return object_info_list

#######################################################################################################################
# Given a bitmap of 0 and object type and a search radius, this locates discrete blobs
# of the same object type by tracing their outlines with accuracy of search_radius, and
# returns a list (object_info_list) of 'object_info_class'

def find_objects(picture, search_radius, animate):
    object_info = object_info_class()
    object_info_list = []

    # hollow out the blobs
    picture=hollow_out(picture)

    working_image=copy.copy(picture)

    rows, cols = working_image.shape

    objects_found=0

    # search each pixel in the image
    for row in range(0, rows-1, 1):
        for col in range(0, cols-1, 1):

            if working_image[row, col] != 0:

                pixel_found=True
                object_info.object_type=working_image[row,col]
                object_info.max_row=[row, col]
                object_info.min_row=[row, col]
                object_info.max_col=[row, col]
                object_info.min_col=[row, col]

                sum_row=0
                sum_col=0
                total_pixels_found=0

                check_row=row
                check_col=col

                # follow the outline of the object
                while(pixel_found):
                    total_pixels_found+=1
                    sum_row+=check_row
                    sum_col+=check_col

                    if check_row>object_info.max_row[0]:
                        object_info.max_row=[check_row,check_col]
                    elif check_row<object_info.min_row[0]:
                        object_info.min_row=[check_row,check_col]

                    if check_col>object_info.max_col[1]:
                        object_info.max_col=[check_row,check_col]
                    elif check_col<object_info.min_col[1]:
                        object_info.min_col=[check_row,check_col]

                    working_image[check_row, check_col] = 0

                    if (animate!=0):
                        cv2.imshow("working", working_image)
                        cv2.waitKey(1)

                    # find all the pixels of the same object type within a radius
                    close_by=in_range(working_image,object_info.object_type,check_row,check_col, search_radius)

                    if (len(close_by)>0):
                        closest_index = closest(check_row,check_col,close_by)
                        check_row=close_by[closest_index][0]
                        check_col=close_by[closest_index][1]
                    else:
                        object_info.perimeter = total_pixels_found
                        object_info.center_RC = [float(1.0*sum_row/total_pixels_found), float(1.0*sum_col/total_pixels_found)]
                        object_info.source_dimensions=[rows, cols]
                        pixel_found = False
                        object_info_list.append(copy.copy(object_info))
                        objects_found+=1


    return object_info_list

#######################################################################################################################
# given a mask this removes any True that isn't surrounded by other True pixels based on the width of the box

def remove_chatter(mask_in,square_width):

    mask_out=copy.copy(mask_in)
    kernel=numpy.ones((square_width,square_width),numpy.uint8)
    mask_out=cv2.erode(mask_in,kernel,iterations=1)

    return mask_out

#######################################################################################################################
# given a mask this makes True any pixel that falls within box size of a True
# this has the effect of removing False pixels that are surrounded by True pixels

def remove_spurious_falses(mask_in,square_width):

    mask_out=copy.copy(mask_in)
    kernel=numpy.ones((square_width,square_width),numpy.uint8)
    mask_out=cv2.dilate(mask_in,kernel,iterations=1)

    return mask_out

#######################################################################################################################
#Given t/f bitmap, every pixel that isn't surrounded completely by otherwise true pixels is set to false.

def clean_edge(picture):

    working=copy.copy(picture)

    rows, cols = working.shape

    for row in range(1, rows - 1, 1):
        for col in range(1, cols - 1, 1):
            if (picture[row-1,col-1]==0):
                working[row,col]=0
            elif (picture[row-1, col] == 0):
                working[row, col] = 0
            elif (picture[row-1, col+1] == 0):
                working[row, col] = 0
            elif (picture[row, col-1] == 0):
                working[row, col] = 0
            elif (picture[row, col] == 0):
                working[row, col] = 0
            elif (picture[row, col + 1] == 0):
                working[row, col] = 0
            elif (picture[row+1, col-1] == 0):
                working[row, col] = 0
            elif (picture[row+1, col] == 0):
                working[row, col] = 0
            elif (picture[row+1, col+1] == 0):
                working[row, col] = 0



    return working

#######################################################################################################################

#Given a color picture, and low and high hue, saturation, and value thresholds, this returns a true/false bitmap where
# everything that falls within all 3 thresholds is true (255) and everything else is false (0).

def find_blobs(picture, low, high):
    hsv = cv2.cvtColor(picture, cv2.COLOR_BGR2HSV)
    hsv2 = cv2.bilateralFilter(hsv, 9, 150, 150)
    mask = cv2.inRange(hsv2, low, high)

    return mask

#######################################################################################################################

# Given img, row, col, this tells you the absolute x and y position of the row and col
# centered on an image with center 0, 0.

def centerCoordinates(image, row, col):
    rows, cols = image.shape
    y = 0 - (row - rows / 2)
    x = col - cols / 2
    return x,y

#######################################################################################################################

# Same as the one below, but it extracts rows and cols from an image

def normalizeCoordinatesI(image, row, col):
    rows, cols = image.shape
    normx = 1.0 * row / (cols / 2)
    normy = 1.0 * col / (rows / 2)
    return normx, normy

#######################################################################################################################

# Given the width of the image in rows and columns, and a row, column position, this returns the normalized
# x, y position of the row, column where the center of the picture is 0, 0 where x=+1 is rightmost and x=-1 is leftmost,
# y=+1 is topmost and y=-1 is bottommost.

def normalizecoordinatesRC(rows, cols, row, col):
    normx = 1.0 * col / (cols / 2)
    normy = 1.0 * row / (rows / 2)
    return normx, normy

#######################################################################################################################

# Given a normalized x y position where x=+1 is rightmost and x=-1 is leftmost,
# y=+1 is topmost and y=-1 is bottommost, horizontal angle is angle from center to rightmost viewing angle,
# this returns the altitude and azimuth of that normalized position.

def altitude_and_azimuth(normx, normy, horizontal_angle, vertical_angle):

    altitude = normy * vertical_angle
    azimuth = normx * horizontal_angle

    return altitude, azimuth

#######################################################################################################################
#sorts the object list,  sorts largest to smallest.


def sort_object_info_list(unsorted_list, sort_by):

    # start with a sorted list of the first object in the unsorted list
    sorted_list=[]
    if (len(unsorted_list)>0):
        sorted_list.append(copy.copy(unsorted_list[0]))

    for unsorted_index in range(1, len(unsorted_list), 1):

        found=False
        sorted_index=0

        while found==False:

            if sort_by==constant.SORT_RELATIVE_AREA:
                unsorted_value=unsorted_list[unsorted_index].relative_area()
                sorted_value=sorted_list[sorted_index].relative_area()
            elif sort_by == constant.SORT_ASPECT_RATIO:
                unsorted_value = unsorted_list[unsorted_index].aspect_ratio()
                sorted_value = sorted_list[sorted_index].aspect_ratio()
            elif sort_by == constant.SORT_PERIMETER:
                unsorted_value = unsorted_list[unsorted_index].perimeter
                sorted_value = sorted_list[sorted_index].perimeter
            elif sort_by == constant.SORT_AZIMUTH_CENTER:
                unsorted_value, x = unsorted_list[unsorted_index].altitude_and_azimuth()
                sorted_value, x = sorted_list[sorted_index].altitude_and_azimuth()
                unsorted_value=1-numpy.absolute(unsorted_value)
                sorted_value=1-numpy.absolute(sorted_value)
            elif sort_by == constant.SORT_OBJECT_TYPE:
                unsorted_value = unsorted_list[unsorted_index].object_type
                sorted_value   = sorted_list[sorted_index].object_type

            if unsorted_value>sorted_value:
                found=True
                sorted_list.append(copy.copy(sorted_list[len(sorted_list)-1]))
                for end_index in range(len(sorted_list)-2, sorted_index, -1):
                    sorted_list[end_index]=copy.copy(sorted_list[end_index-1])
                sorted_list[sorted_index]=copy.copy(unsorted_list[unsorted_index])
            else:
                sorted_index+=1
                if sorted_index==len(sorted_list):
                    sorted_list.append(copy.copy(unsorted_list[unsorted_index]))
                    found=True


    return sorted_list

##################################################################################################################
# given an object info list, this gets rid of a "box" if it is entirely within the area of another box

def remove_object_in_object(list_in):

    list_out=copy.copy(list_in)

    # do one pass then reverse the list and do another
    # this addresses the a in b and b in a condition
    for thing in range (0,2,1):
        # start at the end of the list and work to the front
        last_index=len(list_out)-1

        while(last_index>0):
            check_index=0
            check_next=True
            while (check_next):

                check_object=list_out[check_index]
                last_object=list_out[last_index]

                if (check_object.relative_max_row()>last_object.relative_max_row() and
                    check_object.relative_max_col()>last_object.relative_max_col() and
                    check_object.relative_min_row()<last_object.relative_min_row() and
                    check_object.relative_min_col()<last_object.relative_min_col() and
                    check_object.object_type==last_object.object_type):
                    list_out.pop(last_index)
                    check_next=False
                else:
                    check_index+=1

                if (check_index>=last_index):
                    check_next=False

            last_index-=1

        list_out.reverse()

    return list_out

#######################################################################################################################
# given a picture, this allows the user to select a locations on the picture and reports the three color
# values associated with the location
# this is intended as a tool to held with object identification
# to exit the function, the user enters a negative X location

def get_pixel_values(picture):

    [rows, cols, depth] = picture.shape

    run_again=True
    rel_row = 50
    rel_col = 50

    old_col, old_row = PyWinMouse.Mouse().get_mouse_pos()

    while run_again:
        time.sleep(0.25)

        new_col, new_row = PyWinMouse.Mouse().get_mouse_pos()

        rel_col=+0.08*(new_col-old_col)
        rel_row=+0.08*(new_row-old_row)

        if (rel_row<0):
            rel_row=0
        elif rel_row>100:
            rel_row=100

        if (rel_col<0):
            rel_col=0
        elif rel_col>100:
            rel_col=100

        #convert from relative position to absolute row and colmn
        abs_col=int(cols*1.0*rel_col/100)

        abs_row=int(rows*1.0*rel_row/100)

        if (abs_row>=0 and abs_row<rows and abs_col>=0 and abs_col<cols):
             working=copy.copy(picture)
             cv2.circle(working,(abs_col,abs_row),5,(255,0,0),2)
             cv2.imshow("location", working)
             cv2.waitKey(10)
             print(picture[abs_row, abs_col])

######################################################################################################################
# given a picture, this allows the user to select a locations on the picture and reports the three color
# values associated with the location
# this is intended as a tool to held with object identification
# to exit the function, the user enters a negative X location

def get_pixel_values_momin(picture):

    [rows, cols, depth] = picture.shape

    run_again=True

    while run_again:
        time.sleep(0.25)

        new_col, new_row = PyWinMouse.Mouse().get_mouse_pos()

        if (0<=new_row<rows and 0<=new_col<cols):
             working=copy.copy(picture)
             cv2.circle(working,(new_col,new_row),5,(0,0,255),2)
             cv2.imshow("location", working)
             cv2.waitKey(10)
             print(picture[new_row, new_col])

#######################################################################################################################
# given a picture, this allows the user to select a locations on the picture and reports the three color
# values associated with the location
# this is intended as a tool to held with object identification
# to exit the function, the user enters a negative X location

def get_all_pixel_values(picture):

    [rows, cols, depth] = picture.shape

    run_again=True
    abs_topLeftPixel=[0, 0]
    abs_bottomRightPixel=[0, 0]

    while run_again:
        print("Input top left coordinate (row, col) will display as blue:")
        row, col = input()
        topLeftPixel=[row, col]

        print("Input bottom right coordinate (row, col) will display as red:")
        row, col = input()
        bottomRightPixel=[row, col]

#        bottomRightPixel = input("Input bottom right coordinate (row, col) will display as red:")

        # make sure the relative coordinates are between 0 and 100%
        if (topLeftPixel[1]<0):
            topLeftPixel[1]=0
        elif topLeftPixel[1]>100:
            topLeftPixel[1]=100

        if (topLeftPixel[0]<0):
            topLeftPixel[0]=0
        elif topLeftPixel[0]>100:
            topLeftPixel[0]=100

        if (bottomRightPixel[1]<0):
            bottomRightPixel[1]=0
        elif bottomRightPixel[1]>100:
            bottomRightPixel[1]=100

        if (bottomRightPixel[0]<0):
            bottomRightPixel[0]=0
        elif bottomRightPixel[0]>100:
            bottomRightPixel[0]=100

        if (topLeftPixel[1] > bottomRightPixel[1]):
            topLeftPixel[1] = bottomRightPixel[1]

        if (topLeftPixel[0] > bottomRightPixel[0]):
            topLeftPixel[0] = bottomRightPixel[0]

        #convert from relative position to absolute row and colmn
        abs_topLeftPixel[1] = int(cols*1.0*topLeftPixel[1] / 100)
        abs_topLeftPixel[0] = int(rows * 1.0 * topLeftPixel[0] / 100)

        abs_bottomRightPixel[1] = int(cols * 1.0 * bottomRightPixel[1] / 100)
        abs_bottomRightPixel[0] = int(rows * 1.0 * bottomRightPixel[0] / 100)

        working=copy.copy(picture)
        cv2.circle(working,(abs_topLeftPixel[1], abs_topLeftPixel[0]),5,(255,0,0),2)
        cv2.circle(working, (abs_bottomRightPixel[1], abs_bottomRightPixel[0]), 5, (0, 0, 255), 2)
        cv2.imshow("location", working)
        cv2.waitKey(10)

        happy=input("Are you happy with these coordinates (0, 1)")
        if (happy==1):
            run_again=False

    list_one=[]
    list_two=[]
    list_three=[]

    for abs_row in range (abs_topLeftPixel[0], abs_bottomRightPixel[0], 1):
        for abs_col in range(abs_topLeftPixel[1], abs_bottomRightPixel[1], 1):
            one, two, three = picture[abs_row, abs_col]
            list_one.append(one)
            list_two.append(two)
            list_three.append(three)

    mean_one=numpy.mean(list_one)
    mean_two=numpy.mean(list_two)
    mean_three=numpy.mean(list_three)

    std_one=numpy.std(list_one)
    std_two=numpy.std(list_two)
    std_three=numpy.std(list_three)

    mean_one = round(mean_one, 1)
    mean_two = round(mean_two, 1)
    mean_three = round(mean_three, 1)

    std_one = round(std_one, 1)
    std_two = round(std_two, 1)
    std_three = round(std_three, 1)

    print("Mean: one, two and three ", mean_one, mean_two, mean_three)
    print("STD: one, two three ", std_one, std_two, std_three)

    fit_two_vs_one=numpy.polyfit(list_two, list_one, 2)
    print("Fit two (x) vs. one (y), highest order to lowest: ", fit_two_vs_one)

    fit_two_vs_three=numpy.polyfit(list_two,list_three,2)
    print("Fit two (x) vs three (y), highest order to lowest", fit_two_vs_three)


#######################################################################################################################
def euclidian_distance(one, two):

    distance=numpy.linalg.norm(one-two)

    return distance


#####################################################################################################################
# this calculates the horizontal distance and hypotenuse given an elevation angle and height of the opposing
# target

def distance_from_elevation(angle_degrees,opposite_m):

    horizontal=-1
    hypotenuse=-1

    if (angle_degrees!=0):
        horizontal=opposite_m/math.tan(math.radians(angle_degrees))

    if (angle_degrees<90 and angle_degrees!=0):
        hypotenuse=opposite_m/math.sin(math.radians(angle_degrees))

    return horizontal, hypotenuse


#####################################################################################################################


def two_source_distance(separation_m, angle1_deg, angle2_deg):
    """given two viewpoints where being to the left of the viewpoint is considered a negative angle and right is positive, calculates
     distance to an object in meters"""
    a1_rad = numpy.deg2rad(angle1_deg)
    a2_rad = numpy.deg2rad(angle2_deg)
    x = separation_m
    r1 = -((x/math.tan(a1_rad)* math.sin(a2_rad))/math.sin(a1_rad - a2_rad) + x/math.sin(a1_rad))
    r2 = -((x/math.tan(a1_rad)* math.sin(math.pi - a1_rad))/math.sin(a1_rad - a2_rad))
    distance_m = [r1, r2]

    return distance_m



