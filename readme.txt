aptIn16 is an implementation of the com.sun.mirror.* classes allowing classes implementing them to run as
Java 1.6 javac in-process annotation processors. This is orders of magnitude faster than apt.
It can be used by anyone who currently relies on apt but would like to switch to the newer/faster javac.

On my companies rather large project using apache beehive annotation processors, compilation of a single
module using apt took 13 minutes, that same module, using the same beehive processing code, running inside
of aptIn16 takes about 45 seconds to compile now, and the output is identical.

Also oracle announced that apt will no longer be in Java 8, so you might as well switch now!

Notes:

1. Not every method in com.sun.mirror.* has been implemented, basically just the bare minimum to get apache beehive
to work, so if you use something different, you may need to implement another method or two.  If you do, please
send the code my way so everyone can benefit.

2. This currently only works with a sun/oracle compiler, this is because Java 6 annotation processors don't have a 
concept of SourcePosition, which is needed by apt, so I have to use the com.sun.source.tree to get this info.
All of the sun-specific code is in com.moparisthebest.mirror.util.ConvertSourcePosition, so if you know how to
implement this for eclipse or another compiler, please let me know or do so and send me patches.

3. The only *very* hacky part of this is that apt gave the entire command line sent to javac as options to the processor,
where as Java 6 style annotation processors only get the options specifically meant for annotation processors, and
processors like apache beehive rely on some of these options, the hardest to get of which is -sourcepath, which I can get
decently on Java 6, but have to really hack it to get in Java 7.  Sun compilers put the arguments in an System property,
but there are no delimiters that tells you where one argument begins or ends, so that's a complete hack to try to parse it
as well.  Basically I try to do my best and not halt program execution if something goes wrong.  Please tell me a better
way to handle this.

4. Currently, to use this with your own processors, look at netui-compiler for examples.  apt-processor is a project that
should auto-discover and run all apt-style processors automatically, but it isn't finished and doesn't quite work with beehive.
	