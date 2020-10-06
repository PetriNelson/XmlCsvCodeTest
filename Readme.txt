Kod implemented by Petri Nelson
Mob 076 139 81 39
petri.nelson@avegagroup.se

Instructions:
I have added my maven settings, if you have trouble to download from maven.

How to run:
This code is tested and verified to run with small heap size(-Xmx32m)

Test import file .\src\main\resources\sampledata\small.in and make transformation to either XML or CSv format:

Run main method in class se.petrinelson.nordecodetest.NordeaCodeTest
In the system console enter XML to make XML transformation or CSV to  export as csv file( the file is named CSV_Output.csv)
The XML transformation is logged to console
The CSV file is not logged to console, see the file CSV_Output.csv
Cut and paste your data in the console and hit enter.
To stop the console press CTRL+D


To verify CSV with large input run the main method in class se.petrinelson.nordecodetest.NordeaCodeTestFileReader.class.
Note make sure to edit the filepath(had not time to put this in propertie files)

//Petri Nelson
