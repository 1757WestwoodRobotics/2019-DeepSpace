package org.whsrobotics.vision;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles the communication between a vision co-processor over NetworkTables and the robot for the 2019 FRC season.
 *
 * @author Larry Tseng
 *
 */
public class VisionNetwork {

    private static NetworkTable visionTable;    // reference to the Vision NetworkTables table
    private static NetworkTable visionSettingsTable;    // for settings and commands to the Jetson
    private static int latestCount;    // the latest global data count
    private static final int MAX_DELTA_COUNT = 10;  // the maximum count difference before objects are outdated

    private static volatile ConcurrentHashMap<VisionType, VisionObjectDetails> visionData;  // dictionary for local storage of vision values

    /**
     * Items that can be chosen for vision tracking
     */
    enum VisionType {

        /**
         * CARGO game piece
         */
        CARGO,

        /**
        * HATCH PANEL game piece
        */
        PANEL,

        /**
        * Reflective tape vision targets
        */
        TARGET,

        /**
        * Floor gaffer tape alignment lines
        */
        TAPE

    }

    enum VisionCommand {
        START, STOP, RESTART
    }

    /**
     * Nested inner class that structures the vision item's data.
     */
    static class VisionObjectDetails {

        private int count;
        private double elevationAngle;
        private double azimuth;
        private double distance;

        VisionObjectDetails(int count, double elevationAngle, double azimuth, double distance) {
            this.count = count;
            this.elevationAngle = elevationAngle;
            this.azimuth = azimuth;
            this.distance = distance;
        }

        int getCount() {
            return count;
        }

        double getElevationAngle() {
            return elevationAngle;
        }

        double getAzimuth() {
            return azimuth;
        }

        double getDistance() {
            return distance;
        }

        public String toString() {
            return "[count: " + getCount() +
                    ", elevAngle: " + getElevationAngle() +
                    ", azimuth: " + getAzimuth() +
                    ", distance: " + getDistance() + "]";
        }

    }

    static {

        visionTable = NetworkTableInstance.getDefault().getTable("Vision");
        visionSettingsTable = visionTable.getSubTable("settings");

        visionData = new ConcurrentHashMap<>(4);


        // Register NetworkTables Listeners

        for (VisionType type : VisionType.values()) {

            visionTable.addEntryListener(type.toString(), (table, key, entry, value, flags) -> {

                String[] stringArray = value.getStringArray();
                Double[] objectValues = Arrays.stream(stringArray)
                        .map(Double::parseDouble)
                        .toArray(Double[]::new);

                VisionObjectDetails details = new VisionObjectDetails(
                        objectValues[0].intValue(),
                        objectValues[1],
                        objectValues[2],
                        objectValues[3]);

                visionData.put(type, details);

                System.out.println("DEBUG: Vision Data updated: " + flags + " details: " + details.toString());

            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        }

        visionSettingsTable.addEntryListener("count", (table, key, entry, value, flags) -> {

            latestCount = (int) value.getDouble();
            System.out.println("DEBUG: Vision Data updated: count = " + value);

        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    }

    /**
     * @param visionType
     * @return
     */
    public static Optional<VisionObjectDetails> getVisionObjectDetails(VisionType visionType) {

        Optional<VisionObjectDetails> details = Optional.ofNullable(visionData.get(visionType));    // since HashMap returns null if no element exists

        // if VOD is outdated, remove it from the HashMap
        if (details.isPresent()) {
            if (isOutdated(latestCount, details.get().getCount())) {
                visionData.remove(visionType);
            }
        }

        return details;

    }

    private static boolean isOutdated(int globalCount, int countToCheck) {
        return globalCount > countToCheck + MAX_DELTA_COUNT;
    }

    public static Optional<Double> getAzimuth(VisionType visionType) {
        return getVisionObjectDetails(visionType)
                .map(VisionObjectDetails::getAzimuth);      // return azimuth ifPresent, otherwise empty
    }

    public static Optional<Double> getDistance(VisionType visionType) {
        return getVisionObjectDetails(visionType)
                .map(VisionObjectDetails::getDistance);
    }

    /**
     * @param visionTypes
     */
    public static void setVisionTypes(VisionType... visionTypes) {

        String[] vt = Arrays.stream(visionTypes)
                .map(Enum::toString)
                .toArray(String[]::new);

        visionSettingsTable.getEntry("vision_types").setStringArray(vt);

    }

    public static void setVisionStatus(VisionCommand visionCommand) {
        visionSettingsTable.getEntry("status").setString(visionCommand.toString());
    }

    // check if data is stale (can use a global latest_count variable and delete when accessing or periodically)
        // No need for deletion


}
