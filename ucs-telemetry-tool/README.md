## Telemetry Tool

Telemetry tool allows to convert [UgCS telemetry files (.tlm)](https://github.com/ugcs/ugcs-java-sdk/wiki/.tlm-file-format "UgCS telemetry format") into the CSV and KML output formats.

You need to set the JAVA_HOME environment variable before running the tool. Follow [this instruction](http://www.robertsindall.co.uk/blog/setting-java-home-variable-in-windows/ "Set the JAVA_HOME variable") for assistance.

## Converting to CSV

To convert telemetry to CSV run the tlm2csv script. Follow the `--help` instructions.

```
tlm2csv [-h] [-t <seconds>] [-d <output dir>] [-l <fields>] -f <fileName>

Parameters:

-t, --tolerance : Tolerance, number of seconds between telemetry records that
                  should be interpreted as a separate flights. Default 60 seconds.
-f              : Path to the source .tlm file.
-d              : Path to the destination directory where to put output files.
                  Default is a current directory.
-l, --fields    : Additional file containing list of output fields.
-h, --help      : Help, display this message.
```

Application creates one or more files by the number of separate flight in a source telemetry. Flights detection interval can be specified by the `-t <seconds>` or `--tolerance <seconds>` argument and has a default value of 60 seconds.

To get a certain set of fields in the output CSV file, run application with the `-l <fileName>` or `--fields <fileName>` parameter. Fields file should contain a list of fields, each on a separate line. The order of fields determines the order of CSV columns. '#' - character is a comment and is not considered in the derivation of the fields.

Example of the fields file:

```
fc:latitude
fc:longitude
fc:altitude_amsl
cs:altitude_agl
fc:roll
fc:pitch
fc:ground_speed
fc:vertical_speed
# gps_fix
```

A field name is given in a form `<subsystem alias:telemetry field>`, where subsystem alias is one of the following:

 - cs: control server (ground station)
 - fc: flight controller
 - gb: gimbal
 - cam: camera
 - at: ADS-B transponder

Output file format:

```
<vehicle name>-<flight start time>.csv
```

CSV file consists of the following columns:
 - The first column: timestamp, format: `YYYY-MM-DD'T'hh:mm:Ss.sss`.
 - The second and subsequent columns: value of telemetry fields, specified in fields file or all fields if fields file is empty. Header format: `subsystem:code#subsystemId`.

Every row of the file is a snapshot of the vehicle's telemetry at the given timestamp.

Example:

```
$ tlm2csv -t 60 -f telemetry.tlm -d csv -l fieldsFile.txt
```

 - CSV files will be stored in the ./csv directory.
 - telemetry.tlm and fieldsFile.txt files must be placed in the application directory for this example.

## Converting to KML

To convert telemetry to KML run the tlm2kml script. Follow the `--help` instructions.

```
tlm2kml [-h] [-t <seconds>] [-d <output dir>] -f <fileName>

Parameters:

-t, --tolerance : Tolerance, number of seconds between telemetry records that
                  should be interpreted as a separate flights. Default 60 seconds.
-f              : Path to the source .tlm file.
-d              : Path to the destination directory where to put output files.
                  Default is a current directory.
-h, --help      : Help, display this message.
```

Output file format:

```
<vehicle name>-<flight start time>.kml
```

KML geometry is based on the latitude, longitude and altitude_amsl telemetry fields. It can be opened with the Google Earth for checking the flight routing.

Example:

```
$ tlm2kml -t 60 -f telemetry.tlm -d kml
```

 - KML files will be stored in the ./kml directory.
 - telemetry.tlm file must be be placed on same application directory for this example.

