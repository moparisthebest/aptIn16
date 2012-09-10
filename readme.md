# aptIn16

aptIn16 is an implementation of the [Annotation Processing Tool (apt)][6] [com.sun.mirror.*][1] classes allowing classes implementing them to run as [Java 1.6 annotation processors][2] inside of javac ([JSR 269][3]). This is orders of magnitude faster than [apt][6]. It can be used by anyone who currently relies on apt but would like to switch to the newer/faster javac.

In short, it implements [com.sun.mirror][1] using  [javax.annotation.processing][4] and [javax.lang.model][5] in the background.

On my companies rather large project using [apache beehive][9] annotation processors, compilation of a single module (currently almost 800 .java files, only 300 or so using annotations) using apt took 13 minutes, that same module, using the same beehive processing code, running inside of aptIn16 takes about 45 seconds to compile now, and the output is identical.

Also apt was [deprecated][7] in JDK 7, and [will disappear][10] in JDK 8 ([JEP 117][8]), so you might as well switch sooner than later!

Notes
------------

1. Not every method in [com.sun.mirror.*][1] has been implemented, basically just the bare minimum to get apache beehive to work, so if you use something different, you may need to implement another method or two.  All the non-implemented methods are marked with 'todos' and, if you turn on certain static final booleans in [Debug][11], will print out the method name when called and the class calling it.  This is very useful for debugging as I haven't found a good way of debugging processors running inside of javac.  If you do implement other methods, or make improvements, please send the code my way so everyone can benefit.

2. This currently only works with a sun/oracle compiler, this is because Java 6 annotation processors [don't have a concept][12] of [SourcePosition][13], which is needed by apt, so I have to use the com.sun.source.tree to get this info. All of the sun-specific code is in com.moparisthebest.mirror.util.ConvertSourcePosition, so if you know how to implement this for eclipse or another compiler, please let me know or do so and send me patches.

3. The only*very*  hacky part of this is that apt gave the entire command line sent to javac as options to the processor, where as Java 6 style annotation processors only get the options specifically meant for annotation processors, and processors like apache beehive rely on some of these options, the hardest to get of which is -sourcepath, which I can get decently on Java 6, but have to really hack it to get in Java 7.  Sun compilers put the arguments in an System property, but there are no delimiters that tells you where one argument begins or ends, so that's a complete hack to try to parse it as well.  Basically I try to do my best and not halt program execution if something goes wrong.  Please tell me a better
way to handle this.

4. Currently, to use this with your own processors, look at the very simple [netui-compiler][14] for examples.  [apt-processor][15] is a project that should auto-discover and run all apt-style processors automatically, but it isn't quite finished and doesn't currently work with beehive.

Licensing
------------
Yes, the project is licensed under the [GNU/GPLv3][17], but that shouldn't affect anyone, even those writing propietary code, as your classes don't link to any of this code. It's simply another tool you run over your code to process it.  It would be similar to [gcc][18], just because you use it to compile your programs, doesn't mean your programs must now be under the [GNU/GPL][17].  If you think you have an issue with this, let me know and I'm sure we can work it out.

Contributing
------------

1. Fork it. (Alternatively, if you **really** can't use github/git, email me a patch.)
2. Create a branch (`git checkout -b my_aptIn16`)
3. Commit your changes (`git commit -am "Implemented method X"`)
4. Push to the branch (`git push origin my_aptIn16`)
5. Open a [Pull Request][16]
6. Enjoy a refreshing beverage and wait

[1]:   http://docs.oracle.com/javase/1.5.0/docs/guide/apt/mirror/overview-summary.html
[2]:   https://blogs.oracle.com/darcy/entry/an_apt_replacement
[3]:   http://www.jcp.org/en/jsr/detail?id=269
[4]:   http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/package-summary.html
[5]:   http://docs.oracle.com/javase/6/docs/api/javax/lang/model/package-summary.html
[6]:   http://docs.oracle.com/javase/1.5.0/docs/guide/apt/GettingStarted.html
[7]:   http://hg.openjdk.java.net/jdk7/tl/langtools/rev/d043adadc8b6
[8]:   http://openjdk.java.net/jeps/117
[9]:   http://beehive.apache.org/
[10]: https://blogs.oracle.com/darcy/entry/apt_ending
[11]: https://github.com/moparisthebest/aptIn16/blob/master/core/src/main/java/com/moparisthebest/mirror/log/Debug.java
[12]: https://blogs.oracle.com/darcy/entry/an_apt_replacement#comment-1248911688000
[13]: http://docs.oracle.com/javase/1.5.0/docs/guide/apt/mirror/com/sun/mirror/util/SourcePosition.html
[14]: https://github.com/moparisthebest/aptIn16/tree/master/netui-compiler
[15]: https://github.com/moparisthebest/aptIn16/blob/master/apt-processor/src/main/java/com/moparisthebest/mirror/AptProcessor.java
[16]: https://github.com/moparisthebest/aptIn16/pulls
[17]: http://www.gnu.org/licenses/gpl-3.0-standalone.html
[18]: http://gcc.gnu.org/
