Lambda Framework
================

Lambda is an extensible Browser Automation framework for Humans. By Abstracting the Selenium \ TestNG interface into a format suitable for Testers (such as a spreadsheet, .txt file etc.)
it allows for a much more productive QA team.

Install Lambda - *nix
----------------------
    unzip lambda-<release>.zip
    cd lambda-<release>
    bin/lambda.sh

Install Lambda - Windows
------------------------
    unzip lambda-<release>.zip
    cd lambda-<release>
    start bin/wlambda.bat
    
Install the Selenium CVS export format
--------------------------------------
To install the Selenium IDE CSV Exporter do the following:

1. Open the Selenium IDE
2. Copy the contents of $LAMDA_HOME/conf/sel2csv.js
3. In the Selenium IDE menu, choose Options > Options... > Formats > Add
4. Name the format Lambda CSV
5. Paste the contents into the textarea below
6. Click save.

You can now export from Selenium into a CSV format which can easily be saved as an XLSX file.


Run Lambda from the command-line interface (Human mode)
-------------------------------------------------------


Run Lambda from a continuous integration system
-----------------------------------------------


Supported Operating Systems \ Browsers
--------------------------------------

<table>
    <tr>
        <th>Operating System</th><th>Firefox</th><th>Internet Explorer</th><th>Chrome</th><th>HTMLUnit</th>
    </tr>
    <tr>
        <td>Windows XP</td><td>yes</td><td>yes</td><td>yes</td><td>yes</td>
    </tr>
    <tr>
        <td>Windows Vista (32-bit)</td><td>yes</td><td>yes</td><td>yes</td><td>yes</td>
    </tr>
    <tr>
        <td>Windows Vista (64-bit)</td><td>yes</td><td>yes</td><td>yes</td><td>yes</td>
    </tr>
    <tr>
        <td>Windows 7 (32-bit)</td><td>yes</td><td>yes</td><td>yes</td><td>yes</td>
    </tr>
    <tr>
        <td>Windows 7 (64-bit)</td><td>yes</td><td>yes</td><td>yes</td><td>yes</td>
    </tr>
    <tr>
        <td>Mac OSX Lion (64-bit)</td><td>yes</td><td>no</td><td>yes</td><td>yes</td>
    </tr>
    <tr>
        <td>Linux (32-bit Ubuntu)</td><td>yes</td><td>no</td><td>yes</td><td>yes</td>
    </tr>
</table>