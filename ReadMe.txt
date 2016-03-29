START INSTRUCTIONS:

1. Use Eclipse Version: Mars (4.5.1)
2. Create a new Java project under "File" -> "New"
3. Make sure you are in the Navigator tab on the left
4. Open your project folder, open the "src" folder and drag the .java source files into it
5. You will be prompted if you want to copy or link to the files, I recommend copy
6. Create a run configuration by hitting the down arrow next to the green play button
7. Make sure "Java Application" is highlighted and click the icon to create a new configuration
8. Input your project name in the text box and make sure to use "DFAMinimization" in the Main class text box.
9. Click "Run"


RUN OPTIONS:

NB: I do not recommend running this in the Eclipse IDE as its built-in Console tab doesn't take user input well.  
Instead I recommend running this through the command line.

Follow these steps:
1. Step into your project's bin directory in a command prompt window.
2. Type "java <project name>" without the .class or .java and hit enter
3. It should prompt you for the input file's absolute path


TROUBLESHOOTING:
If you encounter problems with any of this make sure you have the most recent Java JRE and JDK installed and make sure
the JDK bin directory is in your System Variables path.

File Input Format:
This program will take in a file containing a description of a DFA and will output the minimized form.  The input file must follow the following example:
(states, (0,1,2,3,4,5))
(alpha, (1,2))
(trans-func, ((0,1,3),(0,2,4),(1,2,3),(2,1,5),(3,1,2),(4,2,1),(5,1,4)))
(start, 0)
(final, (1))

