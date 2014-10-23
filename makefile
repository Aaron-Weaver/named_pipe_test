JFLAGS = -g
JC = javac
JCR = java

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	SearchFileClient.java \
	SearchFileServer.java \
	SearchFileUtils.java \
	NamedPipe.java

PIPES = \
	clientOutput \
	serverOutput

MAIN = SearchFileClient

default: pipes classes run

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	 @$(JCR) $(MAIN)

clean:
	$(RM) *.class *~

pipes:
	@$(RM) serverOuput
	@$(RM) clientOutput
