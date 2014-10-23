=================Program 1 Write-up===================

Aaron Weaver
waaronl@okstate.edu
2014-10-02
Dr. A. T. Burrell
Programming Homework ONE

======================================================

Problem Description:

Create a program that uses Named Pipes to communicate
between two processes. These processes are divided
into a client/server model. The server should be
performing logic based on user input from the
client process. Both processes should synchronize
using expecte output from the named pipes, and
should close using a similar approach.

-----> For a more detailed look at the program
-----> see the source code and its comments.

======================================================

Running Instructions:

Running make will do 3 things:

1. Remove any pipes that could be created by the program
    (serverOutput, clientOutput)
2. Compile all java classes related to the program
    (SearchFileClient.java, SearchFileServer.java,
     SearchFileUtils.java, and NamedPipe.java)
3. Run SearchFileClient.

There are a few different commands that can be
run with the makefile, they are listed below:

make pipes: remove pipes created by the program
make classes: compiles all java files
make run: runs the program (SearchFileClient)
make clean: removes all .class files

If you would like to run without the make file
just compile using javac on all of the java files
in the program directory and run java SearchFileClient.

Questions?
E-mail me at: waaronl@okstate.edu

======================================================

Collaboration Details:

Some small collaboration was had with a few of my
classmates. Chris Portokalis and I discussed the
program at length many times. I also spoke with 
Victoria and Gabrielle on various other occasions about
this program. I did not use anyone else's code
but my own. For Java specific regular expression
help, I went to the internet (stackoverflow.com)
for questions, but constructed the REGEX myself.
I also went to Java API documentation and various
programming forums for help.

None of my code is copied from any source, and is
owned only by me.

======================================================

Problem Areas:

At first I thought that the Named Pipes were
the means with which we were to start the server
process. This, of course, was an erroneous thought,
but one that I spent upwards of 7 to 8 hours on.
After that I had some trouble synchronizing my
two processes so that the right output and input
would be retrieved at the right time, but finding
more clever uses of blocking solved these issues.
Overall this was a challenging program, but still
incredibly simple.

=======================================================

General Thoughts on Named Pipes:

After working through this assignment, I have
a few opinions on Named Pipes. Named Pipes seem
great for passing data between programs, but
it seems as though the same effect could be 
reached with simple text files, or using various
"languages" like JSON or XML. My main issue with
Named Pipes is that they are not portable, in that
they cannot be implemented on two disparate operating
systems without having to potentially change the
implementation to work. With UNIX, the pipe system
works well, as it is all self contained and needs
not to be rewritten. The benefits of piping in C
are also something to be mentioned. For creating
an OS specific program, piping seems to be the way
to go. If portability is a concern, using some other
means to communicate is more than likely a better
idea.

=======================================================

Notes on My Solution:

I enjoy getting feedback on any program I write,
and schoolwork is no different. If you have any
comments on my code, I would love to hear them!
If there are any questions on the functionality
of the code, or if it isn't working for you,
feel free to e-mail me at: waaronl@okstate.edu.
I was able to get it to compile and run properly
on the CSX machine, and I confirmed the output
with a few test cases that I will provide with my
final solution.

A list of the required Java files are as folows:

-SearchFileClient.java
-SearchFileServer.java
-SearchFileUtils.java
-NamedPipe.java

All of these items are also mentioned in the makefile
and will be compiled when the "make" command is run
in the program's directory.

=======================================================

Thank you,
      Aaron Weaver
