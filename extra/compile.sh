#!/bin/bash

# this file is originally how I tested javac vs apt, but now better functionality than this is incorporated in
# the netui-compiler-test module, which can be activated with the test-netui profile, use it instead

genfiles='./aptgen'
javacNotApt=${1-true}
nocompile=false
#processor="org.apache.beehive.netui.compiler.java6.PageFlowAnnotationProcessor"
#processor="org.apache.beehive.controls.runtime.generator.java6.ControlAnnotationProcessor"
#processor="$processor,org.apache.beehive.controls.runtime.generator.java6.ControlAnnotationProcessor"

#multi_processor="com.moparisthebest.mirror.AptProcessor"
#processor="$multi_processor"

username=$(whoami)
m2_path="C:/Users/$username/.m2/repository"
PATH="/home/$username/bin:/usr/local/bin:/usr/bin:$PATH"
cd "$(dirname $BASH_SOURCE)"

function echoArgs(){
echo "echo args---------------------------------"
while [ $# -ne 0 ]; do
echo "$1"
shift
done
echo "end  args---------------------------------"
}

rm -rf */*.class "$genfiles"
mkdir "$genfiles"

cp=".;../core/target/classes"
#cp="$cp;../apt-processor/target/classes"
cp="$cp;../netui-compiler/target/classes"

#cp="$cp;../netui/target/classes;../controls/target/classes" # modified beehive
# un-modified beehive
cp="$cp;$m2_path\org\apache\beehive\beehive-netui-compiler\1.0.2\beehive-netui-compiler-1.0.2.jar;$m2_path\org\apache\beehive\beehive-controls\1.0.2\beehive-controls-1.0.2.jar;$m2_path\org\apache\beehive\beehive-netui-core\1.0.2\beehive-netui-core-1.0.2.jar"

#cp="$cp;./netui16compiler.jar" # all the rest of the classes
#cp="$cp;../target  with  two  spaces/netui16compiler.jar" # all the rest of the classes

#cp="$cp;../netui-compiler/target/netui-compiler-0.1-jar-with-dependencies.jar" # all the rest of the classes

ofs="$IFS"
IFS="'"
common_args="-Aweb.content.root=$genfiles$IFS-d$IFS$genfiles$IFS-s$IFS$genfiles$IFS-sourcepath$IFS$genfiles$IFS-cp$IFS$cp"
#common_args="$common_args$IFS-verbose"

# add on source files
common_args="$common_args$IFS*/*.java"
#common_args="$common_args${IFS}dir with spaces/*/*.java"

#echoArgs $common_args && exit


if $javacNotApt
then
	[ "$processor" != "" ] && processor="-processor$IFS$processor"
	javac $processor $($nocompile && echo "-proc:only") $common_args
else
	[ "$processor" = "$multi_processor" ] && processor=""
	[ "$processor" != "" ] && processor="-factory$IFS"$(echo "$processor" | sed -e "s/\.java6\./.apt./" -e "s/,.*$//")"Factory"
	apt $processor $($nocompile && echo "-nocompile") $common_args
fi
IFS="$ofs"
md5name="apt"
$javacNotApt && md5name="javac"

find "$genfiles" -type f -print0 | xargs -0 md5sum > "$md5name".md5

#
# Usage: apt <apt and javac options> <source files>
# where apt options include:
  # -classpath <path>          Specify where to find user class files and annotation processor factories
  # -cp <path>                 Specify where to find user class files and annotation processor factories
  # -d <path>                  Specify where to place processor and javac generated class files
  # -s <path>                  Specify where to place processor generated source files
  # -source <release>          Provide source compatibility with specified release
  # -version                   Version information
  # -help                      Print a synopsis of standard options; use javac -help for more options
  # -X                         Print a synopsis of nonstandard options
  # -J<flag>                   Pass <flag> directly to the runtime system
  # -A[key[=value]]            Options to pass to annotation processors
  # -nocompile                 Do not compile source files to class files
  # -print                     Print out textual representation of specified types
  # -factorypath <path>        Specify where to find annotation processor factories
  # -factory <class>           Name of AnnotationProcessorFactory to use; bypasses default discovery process
# See javac -help for information on javac options.

# Usage: javac <options> <source files>
# where possible options include:
  # -g                         Generate all debugging info
  # -g:none                    Generate no debugging info
  # -g:{lines,vars,source}     Generate only some debugging info
  # -nowarn                    Generate no warnings
  # -verbose                   Output messages about what the compiler is doing
  # -deprecation               Output source locations where deprecated APIs are used
  # -classpath <path>          Specify where to find user class files and annotation processors
  # -cp <path>                 Specify where to find user class files and annotation processors
  # -sourcepath <path>         Specify where to find input source files
  # -bootclasspath <path>      Override location of bootstrap class files
  # -extdirs <dirs>            Override location of installed extensions
  # -endorseddirs <dirs>       Override location of endorsed standards path
  # -proc:{none,only}          Control whether annotation processing and/or compilation is done.
  # -processor <class1>[,<class2>,<class3>...]Names of the annotation processors to run; bypasses default discovery process
  # -processorpath <path>      Specify where to find annotation processors
  # -d <directory>             Specify where to place generated class files
  # -s <directory>             Specify where to place generated source files
  # -implicit:{none,class}     Specify whether or not to generate class files for implicitly referenced files
  # -encoding <encoding>       Specify character encoding used by source files
  # -source <release>          Provide source compatibility with specified release
  # -target <release>          Generate class files for specific VM version
  # -version                   Version information
  # -help                      Print a synopsis of standard options
  # -Akey[=value]              Options to pass to annotation processors
  # -X                         Print a synopsis of nonstandard options
  # -J<flag>                   Pass <flag> directly to the runtime system