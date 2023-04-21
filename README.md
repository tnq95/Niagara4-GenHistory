# Add History Extension programmatically in Java - Niagara N4
1> Using Program 
    - Import package 
        + baja          --> javax.baja.collection
        + control-rt    --> javax.baja.control
        + baja          --> javax.baja.naming
        + history-rt    --> javax.baja.history
        + history-rt    --> javax.baja.history.ext

    - Add slot typeHist         --> baja:StatusEnum
    - Add slot queryString      --> baja:String
    - Add slot historyConfig    --> history:HistoryConfig
    - Add slot Interval         --> baja:RelTime    

2> Using Bog
    - Extract the BOG file from the zip on to your local computer.
    - In Workbench, find the BOG file in the "My File System" tree.
    - Drag the 'AutoGenerateHistory.bog' file on to your station.

    - Click the Compile icon or go to the Program Editor menu item and select the Compile menu item
    - Go to the Property Sheet for the 'AutoGenerateHistory' program object and set the typeHist, queryString, Interval value.
    - Right-click on the 'AutoGenerateHistory' program object and go to Actions > Execute

3> Building Niagara4 Component
...



[
    typeHist    => NumericInterval, BooleanInterval, ...
    queryString => station:|slot:/Apps/Points|bql:select * from control:NumericPoint
]

