from networktables import NetworkTables

##############################################################################################################

def init_network_tables(ip_address):

    NetworkTables.initialize(server=ip_address)    # Use 127.0.0.1 for local testing, 10.17.57.2 for roboRIO
    #global table
    parent_table = NetworkTables.getTable('Vision')
    settings_sub_table=parent_table.getSubTable("settings")

    return parent_table, settings_sub_table


##############################################################################################################

def read_network_value(key, table):

    return table.getEntry(key)


##############################################################################################################
# from command prompt enter the "outline viewer" directory type "gradlew run"

def publish_network_value(key, value, table):
    table.putValue(key, value)

##############################################################################################################

# Small test script to run. Needs time to connect and publish data.
# while True:
#     table = init_network_tables()
#     read_network_value('Test', table)
#     publish_network_value('ABC', 104.5, table)

