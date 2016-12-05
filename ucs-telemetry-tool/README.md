## Telemetry converter tool

Telemetry tool allows to convert .tml files into .csv for detalization of telemetry, to filter out file columns by fields.

To convert .tlm to .csv you must run the app with several arguments of command line:

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

Application creates one or more files, separated by flights. Time between flights given with -t <seconds> or --tolerance <seconds> argument.
If you want to see certain fields in .csv file, you have to run application with -l <fileName> or --fields <fileName> parameter.

Fields file represents list of fields, placed on a separate line of its. Example of the fields file:
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

Prefix field aliases:
        CONTROL_SERVER : cs
        FLIGHT_CONTROLLER : fc
        GIMBAL : gb
        CAMERA : cam
        ADSB_TRANSPONDER : at

'#' - character is a comment and is not considered in the derivation of the fields.

Convert .tlm to .kml produced the same way.
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

## Output

Output file format:

```
<vehicle_name>-yyyyMMdd_hhmmss.csv
<vehicle_name>-yyyyMMdd_hhmmss.kml
```

.csv file consists of following columns:
        The first column: timestamp, format: `YYYY-MM-DD'T'hh:mm:Ss.sss`
        The second and subsequent columns: value of telemetry fields, specified in fields file or all fields if fields file is empty.
                Field value cell has a following format: `subsystem:code#subsystemId`

Row is formed as follows:
        All of rows ordered by timestamp column, each value of column contains the actual to time data.

## Running the app

Run the application from command line or terminal using .bat or .sh file, placed into root project directory.

1. Open command line (Win + R) (Windows) or Terminal (*nix) at .bat or .sh placed
2. Follow --help instruction, for example: 
``` 
tlm2kml -t 60 -f telemetry.tlm -d kml -l fieldsFile.txt
```
.kml files will be available in ./kml directory
.tlm and fieldsFile.txt must be placed on same app directory for this sample

Running without parameters print help.

## Set the JAVA_HOME Variable

Once you have the JDK installation path:

1. Right-click the My Computer icon on your desktop and select Properties.
2. Click the Advanced tab, then click the Environment Variables button.
3. Under System Variables, click New.
4. Enter the variable name as JAVA_HOME.
5. Enter the variable value as the installation path for the Java Development Kit.
6. Click OK.
7. Click Apply Changes.