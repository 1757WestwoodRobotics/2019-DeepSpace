from networktables import NetworkTables
import time
import copy



##############################################################################################################

def init_network_tables():

    NetworkTables.initialize(server='10.17.57.2')    # Use 127.0.0.1 for local testing, 10.17.57.2 for roboRIO

    parent_table = NetworkTables.getTable('Vision')
    sub_table = parent_table.getSubTable('settings')
    return parent_table, sub_table


######################################################################################################################
#This reads the network table and gets the String[] of items we are currently searching for, and uses that to update
#the list of items we are currently searching for
#Last updated 1/26/19 by Bryce Parazin

def monitor_tables(parent_table, sub_table, previous_count):


    object_number = 0
    check_again=True
    count=copy.copy(previous_count)

    # get the attributes description
    attributes = parent_table.getEntry('attributes')

    # check for objects
    while (check_again):
        object = parent_table.getEntry('object_' + str(object_number))
        if (object.value != None):
            string = ""
            if (object.value[0]>previous_count):
                count=object.value[0]
                for i in range(0, len(attributes.value)):
                    string += attributes.value[i] + ": " + str(round(float(object.value[i]), 3)) + ", "
                print(string)
            object_number=object_number+1
        else:
            check_again=False

    # remove the entries from the li
    check_again=True
    while (check_again and False):
        object = parent_table.getEntry('object_' + str(object_number))
        if (object.value != None):
            parent_table.delete('object_' + str(object_number))
            object_number=object_number+1
        else:
            check_again=False


    return count

#############################################################################################################

parent_table, sub_table=init_network_tables()
print(parent_table)

count=int(0)

while True:
    try:
        count=monitor_tables(parent_table, sub_table,count)
        time.sleep(0.1)
    except:
        pass

